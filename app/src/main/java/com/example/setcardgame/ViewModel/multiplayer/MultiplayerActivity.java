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

import com.example.setcardgame.Model.Username;
import com.example.setcardgame.Model.card.Card;
import com.example.setcardgame.Model.card.Color;
import com.example.setcardgame.Model.Difficulty;
import com.example.setcardgame.Model.Game;
import com.example.setcardgame.Model.card.Quantity;
import com.example.setcardgame.Model.card.Shape;
import com.example.setcardgame.Model.WebsocketClient;
import com.example.setcardgame.R;
import com.example.setcardgame.ViewModel.EndGameScreenActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.StompClient;

public class MultiplayerActivity extends AppCompatActivity {

    private ArrayList<ImageView> board = new ArrayList<>();
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Card> boardCards = new ArrayList<>();
    private ArrayList<Card> selectedCards = new ArrayList<>();
    private ArrayList<Integer> selectedCardIds = new ArrayList<>();
    private TextView opponentPointTextView;
    private TextView ownPointTextView;
    private Button setBtn;
    private TableLayout tableLayout;
    private Difficulty difficulty = Difficulty.NORMAL;
    private String username = Username.getUsername();
    private int gameId;

    private Game game;

    private final String TAG = "multi";

    private Timer resetBackgroundTimer = new Timer();
    private boolean stopUserInteractions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        Intent mp = getIntent();
        gameId = Integer.parseInt(mp.getStringExtra("gameId"));
        startGame();

        JSONObject jsonGameId = new JSONObject();
        try {
            jsonGameId.put("gameId", gameId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Disposable topic = WebsocketClient.mStompClient.topic("/topic/game-progress/" + gameId).subscribe(topicMessage -> {
            try{
                JSONObject msg = new JSONObject(topicMessage.getPayload());
                game = new Game(msg);
                Log.d(TAG, "ez itt: "+game.getGameId());
                runOnUiThread (new Thread(new Runnable() {
                    public void run() {
                        if (tableLayout.getVisibility()==View.INVISIBLE){
                            tableLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }));

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, throwable -> {
            Log.d(TAG, "ez meg error");
        });
        WebsocketClient.compositeDisposable.add(topic);

        WebsocketClient.mStompClient.send("/app/start", jsonGameId.toString()).subscribe();
    }

    private void startGame() {
        board.clear();
        boardCards.clear();
        cards.clear();
        selectedCards.clear();
        selectedCardIds.clear();

        board.add((ImageView)findViewById(R.id.card0));
        board.add((ImageView)findViewById(R.id.card1));
        board.add((ImageView)findViewById(R.id.card2));
        board.add((ImageView)findViewById(R.id.card3));
        board.add((ImageView)findViewById(R.id.card4));
        board.add((ImageView)findViewById(R.id.card5));
        board.add((ImageView)findViewById(R.id.card6));
        board.add((ImageView)findViewById(R.id.card7));
        board.add((ImageView)findViewById(R.id.card8));

        do {
            cards.clear();
            for (Color color : Color.values()){
                for (Shape shape : Shape.values()){
                    for (Quantity quantity : Quantity.values()){
                        cards.add(new Card(color, shape, quantity));
                    }
                }
            }

            Collections.shuffle(cards);

            for (int i =0; board.size()>i; i++){
                ImageView img = board.get(i);
                int resImage = getResources().getIdentifier(cards.get(0).toString(), "drawable", getPackageName());
                img.setImageResource(resImage);
                img.setContentDescription(cards.get(0).toString());
                boardCards.add(cards.get(0));
                cards.remove(0);
            }
        }while(!hasSet(boardCards));

        opponentPointTextView = findViewById(R.id.opponentPointTextView);
        opponentPointTextView.setText("0");
        ownPointTextView = findViewById(R.id.ownPointTextView);
        ownPointTextView.setText("0");
        setBtn = findViewById(R.id.callSETBtn);

        tableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
        tableLayout.setVisibility(View.INVISIBLE);
    }

    public void onCardClick(View view){
        if (!selectedCardIds.contains(view.getId())){
            boolean found = false;
            int counter = 0;
            while (!found && board.size()>counter) {
                if(board.get(counter).getId() == view.getId()){
                    found = true;
                    view.setBackgroundResource(R.drawable.card_background_selected);
                    selectedCardIds.add(view.getId());
                    selectedCards.add(boardCards.get(counter));
                }
                counter++;
            }

            if (selectedCardIds.size()==3){
                if(hasSet(selectedCards)){
                    for (int i=0; board.size()>i; i++){
                        if (board.get(i).getId() == selectedCardIds.get(0)
                                || board.get(i).getId() == selectedCardIds.get(1)
                                || board.get(i).getId() == selectedCardIds.get(2)){
                            board.get(i).setBackgroundResource(R.drawable.card_background_right);
                        }
                    }
                    int point = Integer.parseInt((String) ownPointTextView.getText());
                    ownPointTextView.setText(String.valueOf(++point));
                    stopUserInteractions = true;
                    removeCardsFromBoard();

                    if (isGameOver() || !hasSet(boardCards)) {
                        endGame();
                    }

                }
                else{
                    for (int i=0; board.size()>i; i++){
                        if (board.get(i).getId() == selectedCardIds.get(0)
                                || board.get(i).getId() == selectedCardIds.get(1)
                                || board.get(i).getId() == selectedCardIds.get(2)){
                            board.get(i).setBackgroundResource(R.drawable.card_background_wrong);
                        }
                    }
                    stopUserInteractions = true;
                }
                selectedCardIds.clear();
                selectedCards.clear();
                resetCardBackgrounds();
            }
        }
        else{
            for (int i = 0; board.size()>i;i++){
                if (board.get(i).getId() == view.getId()){
                    board.get(i).setBackgroundResource(R.drawable.card_background_empty);
                }
            }
            selectedCards.remove(selectedCardIds.indexOf(view.getId()));
            selectedCardIds.remove(selectedCardIds.indexOf(view.getId()));
        }

    }

    private void resetCardBackgrounds(){
        resetBackgroundTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i=0; board.size()>i; i++){
                    board.get(i).setBackgroundResource(R.drawable.card_background_empty);
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

    private boolean hasSet(ArrayList<Card> cards){
        if(cards.size()>=3){
            ArrayList<Boolean> propertyChecks = new ArrayList<>();
            for (int i=0; 3>i;i++) propertyChecks.add(false);

            for (int i=0; cards.size()>i;i++){
                for (int j=i+1; cards.size()>j;j++){
                    for (int k=j+1; cards.size()>k;k++){
                        for (int x=0; 3>x;x++) propertyChecks.set(x,false);
                        if (cards.get(i).getColor()==cards.get(j).getColor() && cards.get(i).getColor()==cards.get(k).getColor()) propertyChecks.set(0, true);
                        if (cards.get(i).getColor()!=cards.get(j).getColor() && cards.get(i).getColor() != cards.get(k).getColor() && cards.get(j).getColor()!=cards.get(k).getColor()) propertyChecks.set(0, true);
                        if (cards.get(i).getShape()==cards.get(j).getShape() && cards.get(i).getShape()==cards.get(k).getShape()) propertyChecks.set(1, true);
                        if (cards.get(i).getShape()!=cards.get(j).getShape() && cards.get(i).getShape() != cards.get(k).getShape() && cards.get(j).getShape()!=cards.get(k).getShape()) propertyChecks.set(1, true);
                        if (cards.get(i).getQuantity()==cards.get(j).getQuantity() && cards.get(i).getQuantity()==cards.get(k).getQuantity()) propertyChecks.set(2, true);
                        if (cards.get(i).getQuantity()!=cards.get(j).getQuantity() && cards.get(i).getQuantity() != cards.get(k).getQuantity() && cards.get(j).getQuantity()!=cards.get(k).getQuantity()) propertyChecks.set(2, true);

                        if (!propertyChecks.contains(false)){
                            ArrayList<Boolean> visibilityChecks = new ArrayList<>();
                            for (int z=0; 3>z;z++) visibilityChecks.add(false);

                            String cardDesc1 = cards.get(i).toString();
                            String cardDesc2 = cards.get(j).toString();
                            String cardDesc3 = cards.get(k).toString();

                            for (int y=0; board.size()>y;y++){
                                String boardDesc = (String)board.get(y).getContentDescription();

                                if (boardDesc.equals(cardDesc1)){
                                    if (board.get(y).getVisibility()==View.VISIBLE){
                                        visibilityChecks.set(0,true);
                                    }
                                }
                                if (boardDesc.equals(cardDesc2)){
                                    if (board.get(y).getVisibility()==View.VISIBLE){
                                        visibilityChecks.set(1,true);
                                    }
                                }
                                if (boardDesc.equals(cardDesc3)){
                                    if (board.get(y).getVisibility()==View.VISIBLE){
                                        visibilityChecks.set(2,true);
                                    }
                                }
                            }

                            if (!visibilityChecks.contains(false)){
                                Log.d("cheat", "card1: " + cardDesc1);
                                Log.d("cheat", "card2: " + cardDesc2);
                                Log.d("cheat", "card3: " + cardDesc3);
                                Log.d("cheat", " ");
                                propertyChecks.clear();
                                visibilityChecks.clear();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void removeCardsFromBoard(){
        for (int i=0; board.size()>i; i++){
            ImageView img = board.get(i);
            if (img.getId() == selectedCardIds.get(0)
                    || img.getId() == selectedCardIds.get(1)
                    || img.getId() == selectedCardIds.get(2)){
                if (cards.size()>0){
                    int resImage = getResources().getIdentifier(cards.get(0).toString() , "drawable", getPackageName());
                    img.setImageResource(resImage);
                    img.setContentDescription(cards.get(0).toString());
                    boardCards.set(i,cards.get(0));
                    cards.remove(0);
                }
                else {
                    img.setVisibility(View.INVISIBLE);
                }

            }
        }
    }

    private void endGame(){
        Intent egs = new Intent(this, EndGameScreenActivity.class);
        egs.putExtra("opponentScore", opponentPointTextView.getText());
        egs.putExtra("ownScore", ownPointTextView.getText());
        egs.putExtra("diff", difficulty.toString());
        startActivity(egs);
    }

    private boolean isGameOver(){
        boolean hasVisible = false;

        for (int i = 0; board.size()>i && !hasVisible;i++){
            if (board.get(i).getVisibility()==View.VISIBLE){
                hasVisible = true;
            }
        }
        return !hasVisible;
    }
}