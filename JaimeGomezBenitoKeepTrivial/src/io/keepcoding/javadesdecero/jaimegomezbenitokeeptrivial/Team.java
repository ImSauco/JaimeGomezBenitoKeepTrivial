package io.keepcoding.javadesdecero.jaimegomezbenitokeeptrivial;

import java.util.ArrayList;

public class Team {
    private String name;
    private int score;

    public Team(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public boolean hasCompletedAllTopics(ArrayList<Topic> topics) {
        for (Topic topic : topics) {
            if (!topic.isCompletedForTeam(this)) {
                return false;
            }
        }
        return true;
    }
}