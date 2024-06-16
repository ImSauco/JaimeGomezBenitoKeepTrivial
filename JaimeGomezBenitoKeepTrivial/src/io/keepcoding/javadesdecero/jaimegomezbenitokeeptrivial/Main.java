package io.keepcoding.javadesdecero.jaimegomezbenitokeeptrivial;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ArrayList<Topic> topics = getQuestions();

        Scanner scanner = new Scanner(System.in);
        List<Team> teams = new ArrayList<>();
        boolean addMoreTeams = true;

        while (addMoreTeams) {
            System.out.print("Ingrese el nombre del equipo: ");
            teams.add(new Team(scanner.nextLine()));
            while (true) {
                System.out.print("¿Desea agregar otro equipo? (s/n): ");
                String response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("n")) {
                    addMoreTeams = false;
                    break;
                } else if (response.equals("s")) {
                    break;
                } else {
                    System.out.println("Respuesta no válida. Por favor, ingrese 's' para sí o 'n' para no.");
                }
            }
        }

        boolean exit = false;
        int currentTeamIndex = 0;
        Team winningTeam = null;

        do {
            Team currentTeam = teams.get(currentTeamIndex);
            System.out.println("Turno del equipo: " + currentTeam.getName());

            Question question = getRandomQuestion(topics, currentTeam);
            System.out.println("Topic: " + question.getTopic().getName());
            System.out.println("Pregunta: " + question.getQuestion());
            System.out.println("1: " + question.getAnswer1());
            System.out.println("2: " + question.getAnswer2());
            System.out.println("3: " + question.getAnswer3());
            System.out.println("4: " + question.getAnswer4());
            System.out.print("Ingrese el número de la respuesta correcta: ");

            int answer = scanner.nextInt();
            scanner.nextLine();
            if (answer == question.getRightOption()) {
                currentTeam.addScore(1);
                System.out.println("¡Correcto! Puntuación del equipo " + currentTeam.getName() + ": " + currentTeam.getScore());

                // Marcar el tema como completado para este equipo
                question.getTopic().setCompletedForTeam(currentTeam, true);
            } else {
                System.out.println("Incorrecto. La respuesta correcta era: " + question.getRightOption());
            }
            System.out.println("Puntuación del equipo " + currentTeam.getName() + ": " + currentTeam.getScore());

            currentTeamIndex = (currentTeamIndex + 1) % teams.size();

            System.out.println("Clasificación:");
            for (Team team : teams) {
                System.out.print(team.getName() + ": ");
                for (Topic topic : topics) {
                    int completed = topic.isCompletedForTeam(team) ? 1 : 0;
                    System.out.print(topic.getName() + ": " + completed + " ");
                }
                System.out.println("Puntuación total: " + team.getScore());
            }

            System.out.println();

            // Verificar si algún equipo ha completado todos los temas
            for (Team team : teams) {
                if (team.hasCompletedAllTopics(topics)) {
                    exit = true;
                    winningTeam = team;
                    break;
                }
            }

        } while (!exit);
        System.out.println();
        title("Ha ganado: " + winningTeam.getName());
    }

    public static void title(String text) {
        int length = text.length();
        printHashtagLine(length + 4);

        System.out.println("# " + text + " #");

        printHashtagLine(length + 4);
    }

    public static void printHashtagLine(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("#");
        }
        System.out.println();
    }

    private static int getRandomInt(int max) {
        return new Random().nextInt(max);
    }

    private static Question getRandomQuestion(ArrayList<Topic> topics, Team team) {
        // Filtrar solo temas que no están completos para este equipo
        List<Topic> availableTopics = new ArrayList<>();
        for (Topic topic : topics) {
            if (!topic.isCompletedForTeam(team)) {
                availableTopics.add(topic);
            }
        }

        Topic topic = availableTopics.get(getRandomInt(availableTopics.size()));
        List<Question> questions = topic.getQuestions();
        return questions.get(getRandomInt(questions.size()));
    }

    private static ArrayList<Topic> getQuestions() {
        ArrayList<Topic> list = new ArrayList<>();

        File folder = new File("questions");
        if (!folder.exists()) {
            title("Error al cargar el fichero");
        } else {
            File[] filesList = folder.listFiles();

            for (File file : filesList) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    String topicName = file.getName().substring(0, file.getName().length() - 4);
                    Topic topic = new Topic(topicName);

                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        List<String> block = new ArrayList<>();

                        while ((line = br.readLine()) != null) {
                            block.add(line);

                            if (block.size() == 6) {
                                String question = block.get(0);
                                String answer1 = block.get(1);
                                String answer2 = block.get(2);
                                String answer3 = block.get(3);
                                String answer4 = block.get(4);
                                int rightOption = Integer.parseInt(block.get(5));

                                Question q = new Question(question, answer1, answer2, answer3, answer4, rightOption, topic);
                                topic.addQuestion(q);
                                block.clear();
                            }
                        }
                        list.add(topic);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return list;
    }
}