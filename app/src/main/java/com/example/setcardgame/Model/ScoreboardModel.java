package com.example.setcardgame.Model;

import java.util.UUID;

public class ScoreboardModel {

    private int scoreId;
    private UUID playerId;
    private Difficulty difficulty;
    private int score;
    private int time;

    public ScoreboardModel(String playerId, String difficulty, int score, int time) {
        this.playerId = UUID.fromString(playerId);
        this.difficulty = Difficulty.getDifficultyFromString(difficulty);
        this.score = score;
        this.time = time;
    }

    public ScoreboardModel(int scoreId, String playerId, String difficulty, int score, int time) {
        this.scoreId = scoreId;
        this.playerId = UUID.fromString(playerId);
        this.difficulty = Difficulty.getDifficultyFromString(difficulty);
        this.score = score;
        this.time = time;
    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        int minutes = time/60;
        int seconds = time%60;
        return difficulty + ", " + score + " points, " + String.format("%d:%02d", minutes, seconds);
    }
}
