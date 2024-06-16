package io.keepcoding.javadesdecero.jaimegomezbenitokeeptrivial;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String name;
    private List<Question> questions;
    private boolean completed;
    private List<Team> teamsCompleted;

    public Topic(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
        this.completed = false;
        this.teamsCompleted = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public boolean isCompletedForTeam(Team team) {
        return teamsCompleted.contains(team);
    }

    public void setCompletedForTeam(Team team, boolean completed) {
        if (completed) {
            teamsCompleted.add(team);
        } else {
            teamsCompleted.remove(team);
        }
        this.completed = teamsCompleted.size() == teamsCompleted.size(); 
    }

    public boolean isCompleted() {
        return completed;
    }
}