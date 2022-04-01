package com.example.setcardgame.ViewModel.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.setcardgame.Model.Difficulty;
import com.example.setcardgame.Model.Game;
import com.example.setcardgame.Model.Username;
import com.example.setcardgame.Model.WebsocketClient;
import com.example.setcardgame.Model.card.Card;
import com.example.setcardgame.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.Disposable;

public class MultiplayerActivity extends AppCompatActivity {

    private final String TAG = "multi";
    private final ArrayList<ImageView> boardIV = new ArrayList<>();
    private final ArrayList<Card> selectedCards = new ArrayList<>();
    private final ArrayList<Integer> selectedCardIds = new ArrayList<>();
    private TextView opponentPointTextView;
    private TextView ownPointTextView;
    private Button setBtn;
    private TableLayout tableLayout;
    private final Difficulty difficulty = Difficulty.NORMAL;
    private final String username = Username.getUsername();
    private int gameId;
    private Game game;
    private final Timer resetBackgroundTimer = new Timer();
    private Timer resetTimer;
    private final Timer punishTimer = new Timer();
    private boolean stopUserInteractions = false;
    private int resetInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        Intent mp = getIntent();
        gameId = Integer.parseInt(mp.getStringExtra("gameId"));
        setBtn = findViewById(R.id.callSETBtn);

        JSONObject jsonGameId = new JSONObject();
        try {
            jsonGameId.put("gameId", gameId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Disposable topic = WebsocketClient.mStompClient.topic("/topic/game-progress/" + gameId).subscribe(topicMessage -> {
            try {
                JSONObject msg = new JSONObject(topicMessage.getPayload());
                Game tempGame = new Game(msg);
                Log.d(TAG, msg.toString());
                if (tempGame.getPlayer1() != null && tempGame.getPlayer2() != null) {
                    runOnUiThread(new Thread(new Runnable() {
                        public void run() {
                            //start game
                            if (game == null) {
                                game = new Game(msg);
                                startGame();
                            } else {
                                //SET button press
                                if (tempGame.getBlockedBy() != null && tempGame.getBlockedBy().toString().equals(username) && tempGame.getSelectedCardIndexes().isEmpty()) {
//                                    Log.d(TAG, "my block");
                                    try {
                                        game.setBlockedByString(msg.getString("blockedBy"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (tempGame.getBlockedBy() != null && !tempGame.getBlockedBy().toString().equals(username) && tempGame.getSelectedCardIndexes().isEmpty()) {
//                                    Log.d(TAG, "opponent's block");
                                    try {
                                        game.setBlockedByString(msg.getString("blockedBy"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    setBtn.setEnabled(false);
                                    setBtn.setBackgroundTintList(ContextCompat.getColorStateList(MultiplayerActivity.this, R.color.dark_red));
                                }

                                //opponent is selecting cards
                                if (tempGame.getBlockedBy() != null && !tempGame.getBlockedBy().toString().equals(username)) {
                                    game.setSelectedCardIndexes(tempGame.getSelectedCardIndexes());
                                    setSelectedCardsBackgroundForOpponent(game.getSelectedCardIndexes());
                                }

                                //3 cards have been selected
                                if (tempGame.getSelectedCardIndexes().size() == 3 && tempGame.getBlockedBy() == null) {
                                    if (game.getBlockedBy().toString().equals(username)) {
                                    }

                                    game.setSelectedCardIndexes(tempGame.getSelectedCardIndexes());
                                    setSelectedCardsBackgroundForOpponent(game.getSelectedCardIndexes());

                                    if (game.hasSamePoints(tempGame.getPoints())) {
                                        //wrong combo
                                        unCorrectSelectedCards(tempGame.getSelectedCardIndexes());

                                        game.clearSelectedCardIndexes();
                                        selectedCardIds.clear();
                                        selectedCards.clear();
                                        resetCardBackgrounds();
                                        resetButtonAndCardClicks();

                                        if (game.getBlockedBy().toString().equals(username)) {
                                            resetButtonAndCardClicksOnError();
                                            punishPlayerError();
                                            Log.d(TAG, "you didn't find the set");
                                        } else {
                                            resetButtonAndCardClicks();
                                        }
                                        game.setBlockedBy(null);
                                    } else {
                                        //right combo, board changed
                                        correctSelectedCards(tempGame.getSelectedCardIndexes());
                                        game.setPoints(tempGame.getPoints());
                                        updatePointTextViews();
                                        game.setBoard(tempGame.getBoard());
                                        game.setNullCardIndexes(tempGame.getNullCardIndexes());

                                        for (int i = 0; game.getSelectedCardIndexes().size() > i; i++) {
                                            if (!game.getNullCardIndexes().isEmpty()) {
                                                boardIV.get(game.getSelectedCardIndexes().get(i)).setVisibility(View.INVISIBLE);
                                            } else {
                                                ImageView img = boardIV.get(game.getSelectedCardIndexes().get(i));
                                                int resImage = getResources().getIdentifier(game.getBoard().get(game.getSelectedCardIndexes().get(i)).toString(), "drawable", getPackageName());
                                                img.setImageResource(resImage);
                                                img.setContentDescription(game.getBoard().get(game.getSelectedCardIndexes().get(i)).toString());
                                            }
                                        }

                                        if (tempGame.getWinner() != null) {
                                            game.setWinner(tempGame.getWinner());
                                            Log.d(TAG, "END");
                                            endGame();
                                        }

                                        resetButtonAndCardClicks();
                                        game.setBlockedBy(null);
                                        game.clearSelectedCardIndexes();
                                        selectedCardIds.clear();
                                        selectedCards.clear();
                                        resetCardBackgrounds();
                                    }
                                }
                                if (game.getBlockedBy() != null && tempGame.getSelectedCardIndexes().size() != 3 && tempGame.getBlockedBy() == null) {    //time ran out. button has been reset
                                    Log.d(TAG, "entered");
                                    game.clearSelectedCardIndexes();
                                    selectedCardIds.clear();
                                    selectedCards.clear();
                                    resetCardBackgrounds();
                                    if (game.getBlockedBy().toString().equals(username)) {
                                        resetButtonAndCardClicksOnError();
                                        punishPlayerError();
                                        Log.d(TAG, "punished");
                                    } else {
                                        resetButtonAndCardClicks();
                                        Log.d(TAG, "not punished");
                                    }
                                    game.setBlockedBy(null);
                                }
                            }
                        }
                    }));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, throwable -> {
            Log.d(TAG, "ez meg error");
        });
        WebsocketClient.compositeDisposable.add(topic);
        WebsocketClient.mStompClient.send("/app/start", jsonGameId.toString()).subscribe();
    }

    private void startGame() {
        boardIV.clear();
        selectedCards.clear();
        selectedCardIds.clear();

        boardIV.add((ImageView) findViewById(R.id.card0));
        boardIV.add((ImageView) findViewById(R.id.card1));
        boardIV.add((ImageView) findViewById(R.id.card2));
        boardIV.add((ImageView) findViewById(R.id.card3));
        boardIV.add((ImageView) findViewById(R.id.card4));
        boardIV.add((ImageView) findViewById(R.id.card5));
        boardIV.add((ImageView) findViewById(R.id.card6));
        boardIV.add((ImageView) findViewById(R.id.card7));
        boardIV.add((ImageView) findViewById(R.id.card8));

        for (int i = 0; boardIV.size() > i; i++) {
            ImageView img = boardIV.get(i);
            img.setEnabled(false);
            int resImage = getResources().getIdentifier(game.getBoard().get(i).toString(), "drawable", getPackageName());
            img.setImageResource(resImage);
            img.setContentDescription(game.getBoard().get(i).toString());
        }

        opponentPointTextView = findViewById(R.id.opponentPointTextView);
        opponentPointTextView.setText("0");
        ownPointTextView = findViewById(R.id.ownPointTextView);
        ownPointTextView.setText("0");
        setBtn = findViewById(R.id.callSETBtn);

        tableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
        tableLayout.setVisibility(View.VISIBLE);
    }

    public void onSETBtnClick(View view) {
        setBtn.setEnabled(false);
        setBtn.setBackgroundTintList(ContextCompat.getColorStateList(MultiplayerActivity.this, R.color.green));
        switchBoardClicks(true);

        JSONObject buttonPressJson = new JSONObject();
        try {
            buttonPressJson.put("gameId", gameId);
            buttonPressJson.put("playerId", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WebsocketClient.mStompClient.send("/app/gameplay/button", buttonPressJson.toString()).subscribe();

        //start timer for 3 sec, if it the player does not select 3 cards reset everything and punish them
        selectTimeCountDown(buttonPressJson);
    }

    private void selectTimeCountDown(JSONObject buttonPressJson) {
        resetTimer = new Timer();
        resetTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                WebsocketClient.mStompClient.send("/app/gameplay/button", buttonPressJson.toString()).subscribe();
            }
        }, 3000);
    }

    private void punishPlayerError() {
        punishTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (game.getBlockedBy() == null) {
                            resetButtonAndCardClicks();
                        }
                    }
                });
            }
        }, 5000);
    }

    private void resetButtonAndCardClicks() {
        switchBoardClicks(false);
        setBtn.setEnabled(true);
        setBtn.setBackgroundTintList(ContextCompat.getColorStateList(MultiplayerActivity.this, R.color.dark_blue));
    }

    private void resetButtonAndCardClicksOnError() {
        switchBoardClicks(false);
        setBtn.setEnabled(false);
        setBtn.setBackgroundTintList(ContextCompat.getColorStateList(MultiplayerActivity.this, R.color.dark_red));
    }

    private void updatePointTextViews() {
        if (game.getPlayer1().toString().equals(username)) {
            ownPointTextView.setText(game.getPoints().get(game.getPlayer1()).toString());
            opponentPointTextView.setText(game.getPoints().get(game.getPlayer2()).toString());
        } else {
            ownPointTextView.setText(game.getPoints().get(game.getPlayer2()).toString());
            opponentPointTextView.setText(game.getPoints().get(game.getPlayer1()).toString());
        }
    }

    public void onCardClick(View view) {
        if (game.getBlockedBy().toString().equals(username) && selectedCardIds.size() < 3) {
            if (!selectedCardIds.contains(view.getId())) {
                resetInt++;
                if (resetInt==3){
                    resetTimer.cancel();
                    resetTimer.purge();
                    resetInt=0;
                }
                boolean found = false;
                int counter = 0;
                while (!found && boardIV.size() > counter) {
                    if (boardIV.get(counter).getId() == view.getId()) {
                        found = true;
                        view.setBackgroundResource(R.drawable.card_background_selected);
                        selectedCardIds.add(view.getId());
                        selectedCards.add(game.getBoard().get(counter));

                        JSONObject gameplayJson = new JSONObject();
                        try {
                            gameplayJson.put("gameId", gameId);
                            gameplayJson.put("playerId", username);
                            gameplayJson.put("select", true);
                            gameplayJson.put("selectedCardIndex", counter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        WebsocketClient.mStompClient.send("/app/gameplay", gameplayJson.toString()).subscribe();
                    }
                    counter++;
                }
            } else {
                resetInt--;
                for (int i = 0; boardIV.size() > i; i++) {
                    if (boardIV.get(i).getId() == view.getId()) {
                        boardIV.get(i).setBackgroundResource(R.drawable.card_background_empty);

                        JSONObject gameplayJson = new JSONObject();
                        try {
                            gameplayJson.put("gameId", gameId);
                            gameplayJson.put("playerId", username);
                            gameplayJson.put("select", false);
                            gameplayJson.put("selectedCardIndex", i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        WebsocketClient.mStompClient.send("/app/gameplay", gameplayJson.toString()).subscribe();
                    }
                }
                selectedCards.remove(selectedCardIds.indexOf(view.getId()));
                selectedCardIds.remove((Integer) view.getId());
            }
        }
    }

    private void correctSelectedCards(ArrayList<Integer> indexes) {
        for (int i = 0; indexes.size() > i; i++) {
            boardIV.get(indexes.get(i)).setBackgroundResource(R.drawable.card_background_right);
        }
        stopUserInteractions = true;
    }

    private void unCorrectSelectedCards(ArrayList<Integer> indexes) {
        for (int i = 0; indexes.size() > i; i++) {
            boardIV.get(indexes.get(i)).setBackgroundResource(R.drawable.card_background_wrong);
        }
        stopUserInteractions = true;
    }

    private void setSelectedCardsBackgroundForOpponent(ArrayList<Integer> indexes) {
        for (int i = 0; boardIV.size() > i; i++) {
            boardIV.get(i).setBackgroundResource(R.drawable.card_background_empty);
        }
        for (int i = 0; indexes.size() > i; i++) {
            boardIV.get(indexes.get(i)).setBackgroundResource(R.drawable.card_background_selected);
            selectedCardIds.add(boardIV.get(indexes.get(i)).getId());
            selectedCards.add(game.getBoard().get(indexes.get(i)));
        }
    }

    private void resetCardBackgrounds() {
        resetBackgroundTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; boardIV.size() > i; i++) {
                    boardIV.get(i).setBackgroundResource(R.drawable.card_background_empty);
                }
                stopUserInteractions = false;
            }
        }, 300);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (stopUserInteractions) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    private void switchBoardClicks(boolean on) {
        for (int i = 0; boardIV.size() > i; i++) {
            boardIV.get(i).setEnabled(on);
        }
    }

    private void endGame() {
        Intent mpes = new Intent(this, MultiplayerEndScreenActivity.class);
        mpes.putExtra("opponentScore", opponentPointTextView.getText());
        mpes.putExtra("ownScore", ownPointTextView.getText());
        mpes.putExtra("winner", game.getWinner().toString());
        startActivity(mpes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebsocketClient.disconnectWebsocket();
    }
}