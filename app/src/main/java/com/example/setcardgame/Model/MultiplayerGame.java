package com.example.setcardgame.Model;

import com.example.setcardgame.Model.card.Card;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MultiplayerGame {

    private int gameId;
    private UUID player1;
    private UUID player2;
    private ArrayList<Card> board = new ArrayList<>();
    private UUID winner;
    private UUID blockedBy;
    private ArrayList<Integer> selectedCardIndexes = new ArrayList<>();
    private Map<UUID, Integer> points = new HashMap<>();
    private ArrayList<Integer> nullCardIndexes = new ArrayList<>();

    public MultiplayerGame(JSONObject game) {
        try {
            setGameIdString(game.getString("gameId"));
            if (!game.getString("player1").equals("null")) {
                this.player1 = UUID.fromString(game.getString("player1"));
            }

            if (!game.getString("player2").equals("null")) {
                this.player2 = UUID.fromString(game.getString("player2"));
            }

            if (!game.getString("blockedBy").equals("null")) {
                this.blockedBy = UUID.fromString(game.getString("blockedBy"));
            } else {
                this.blockedBy = null;
            }

            if (!game.getString("winner").equals("null")) {
                this.winner = UUID.fromString(game.getString("winner"));
            }

            setNullCardIndexesString(game.getString("nullCardIndexes"));
            setBoardString(game.getString("board"));
            setSelectedCardIndexesString(game.getString("selectedCardIndexes"));
            setPointsString(game.getString("points"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getNullCardIndexes() {
        return nullCardIndexes;
    }

    public void setNullCardIndexes(ArrayList<Integer> nullCardIndexes) {
        this.nullCardIndexes = nullCardIndexes;
    }

    public void setNullCardIndexesString(String nullCardIndexesString) {
        if (!nullCardIndexesString.equals("[]")) {
            nullCardIndexes.clear();
            nullCardIndexesString = nullCardIndexesString.replace('[', ' ');
            nullCardIndexesString = nullCardIndexesString.replace(']', ' ');
            nullCardIndexesString = nullCardIndexesString.trim();
            String[] nullCardIndexesStrings = nullCardIndexesString.split(",");
            for (int i = 0; nullCardIndexesStrings.length > i; i++) {
                nullCardIndexes.add(Integer.parseInt(nullCardIndexesStrings[i]));
            }
        }
    }

    public void clearSelectedCardIndexes() {
        selectedCardIndexes.clear();
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setGameIdString(String gameId) {
        this.gameId = Integer.parseInt(gameId);
    }

    public UUID getPlayer1() {
        return player1;
    }

    public void setPlayer1(UUID player1) {
        this.player1 = player1;
    }

    public void setPlayer1String(String player1) {
        this.player1 = UUID.fromString(player1);
    }

    public void setPlayer2String(String player2) {
        this.player2 = UUID.fromString(player2);
    }

    public UUID getPlayer2() {
        return player2;
    }

    public void setPlayer2(UUID player2) {
        this.player2 = player2;
    }

    public ArrayList<Card> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<Card> board) {
        this.board = board;
    }

    public void setBoardString(String boardString) {
        board.clear();
        boardString = boardString.replace('"', ' ');
        boardString = boardString.replace('[', ' ');
        boardString = boardString.replace(']', ' ');
        boardString = boardString.replace('{', ' ');
        boardString = boardString.replace('}', ' ');
        boardString = boardString.replace(':', ' ');
        boardString = boardString.replaceAll("color", "");
        boardString = boardString.replaceAll("shape", "");
        boardString = boardString.replaceAll("quantity", "");
        String[] words = boardString.split(",");

        for (int i = 0; words.length > i; i++) {
            words[i] = words[i].trim();
        }

        int i = 0;
        while (words.length > i) {
            if (!words[i].equals("null")) {
                Card newCard = new Card(words[i++], words[i++], words[i++]);
                board.add(newCard);
            } else {
                board.add(null);
            }
        }
    }

    public UUID getWinner() {
        return winner;
    }

    public void setWinner(UUID winner) {
        this.winner = winner;
    }

    public void setWinnerString(String winner) {
        this.winner = UUID.fromString(winner);
    }

    public UUID getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(UUID blockedBy) {
        this.blockedBy = blockedBy;
    }

    public void setBlockedByString(String blockedBy) {
        this.blockedBy = UUID.fromString(blockedBy);
    }

    public ArrayList<Integer> getSelectedCardIndexes() {
        return selectedCardIndexes;
    }

    public void setSelectedCardIndexes(ArrayList<Integer> selectedCardIndexes) {
        this.selectedCardIndexes = selectedCardIndexes;
    }

    public void setSelectedCardIndexesString(String selectedCardIndexesString) {
        if (!selectedCardIndexesString.equals("[]")) {
            selectedCardIndexes.clear();
            selectedCardIndexesString = selectedCardIndexesString.replace('[', ' ');
            selectedCardIndexesString = selectedCardIndexesString.replace(']', ' ');
            selectedCardIndexesString = selectedCardIndexesString.trim();
            String[] selectedCardIndexesStrings = selectedCardIndexesString.split(",");
            for (int i = 0; selectedCardIndexesStrings.length > i; i++) {
                selectedCardIndexes.add(Integer.parseInt(selectedCardIndexesStrings[i]));
            }
        }
    }

    public Map<UUID, Integer> getPoints() {
        return points;
    }

    public void setPoints(Map<UUID, Integer> points) {
        this.points = points;
    }

    public ArrayList<Integer> getPurePoints() {
        ArrayList<Integer> pointsArray = new ArrayList<>();
        pointsArray.add(points.get(player1));
        pointsArray.add(points.get(player2));

        return pointsArray;
    }

    public boolean hasSamePoints(Map<UUID, Integer> otherPoints) {
        boolean same = points.get(player1).equals(otherPoints.get(player1));
        if (!points.get(player2).equals(otherPoints.get(player2))) {
            same = false;
        }
        return same;
    }

    public void setPointsString(String pointsString) {
        if (player1 != null && player2 != null) {

            pointsString = pointsString.replace('"', ' ');
            pointsString = pointsString.replace('{', ' ');
            pointsString = pointsString.replace('}', ' ');
            pointsString = pointsString.replace(':', ',');
            String[] pointWords = pointsString.split(",");

            for (int i = 0; pointWords.length > i; i++) {
                pointWords[i] = pointWords[i].trim();
            }

            int i = 0;
            while (pointWords.length > i) {
                points.put(UUID.fromString(pointWords[i++]), Integer.parseInt(pointWords[i++]));
            }
        }
    }
}
