package com.example.setcardgame.Model;

import android.icu.text.Edits;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class Game {

    private int gameId;
    private UUID player1;
    private UUID player2;
    private GameStatus status;
    private ArrayList<Card> board = new ArrayList<>();
    private UUID winner;
    private ArrayList<Card> cardDeck = new ArrayList<>();
    private final int BOARD_SIZE = 9;
    private UUID blockedBy;
    private ArrayList<Integer> selectedCardIndexes = new ArrayList<>();
    private Map<UUID, Integer> points = new HashMap<>();

    public Game(JSONObject game) {
        try {
            setGameIdString(game.getString("gameId"));
            if (!game.getString("player1").equals("null")){
                this.player1 = UUID.fromString(game.getString("player1"));
            }
            if (!game.getString("player2").equals("null")){
                this.player2 = UUID.fromString(game.getString("player2"));
            }
            setBoardString(game.getString("board"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
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
        boardString = boardString.replaceAll("color", "");
        boardString = boardString.replaceAll("shape", "");
        boardString = boardString.replaceAll("quantity", "");
        String[] words = boardString.split(",");

        for (int i=0; words.length>i;i++){
            words[i] = words[i].trim();
        }

        int i = 0;
        while (words.length>i){
            Card newCard = new Card(words[i++],words[i++],words[i++]);
            board.add(newCard);
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

    public ArrayList<Card> getCardDeck() {
        return cardDeck;
    }

    public void setCardDeck(ArrayList<Card> cardDeck) {
        this.cardDeck = cardDeck;
    }

    public int getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public UUID getBlockedBy() {
        return blockedBy;
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

    public Map<UUID, Integer> getPoints() {
        return points;
    }

    public void setPoints(Map<UUID, Integer> points) {
        this.points = points;
    }

    public void setPointsString(String pointsString) {
        if (player1 != null && player2 != null){

            pointsString = pointsString.replace('"', ' ');
            pointsString = pointsString.replace('{', ' ');
            pointsString = pointsString.replace('}', ' ');
            pointsString = pointsString.replace(':', ',');
            String[] pointWords = pointsString.split(",");

            for (int i = 0; pointWords.length>i;i++){
                pointWords[i] = pointWords[i].trim();
            }

            int i = 0;
            while (pointWords.length>i){
                points.put(UUID.fromString(pointWords[i++]), Integer.parseInt(pointWords[i++]));
            }
        }
    }
}
