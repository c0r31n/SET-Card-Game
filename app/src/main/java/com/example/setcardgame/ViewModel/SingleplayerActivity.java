package com.example.setcardgame.ViewModel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.setcardgame.Model.Card;
import com.example.setcardgame.Model.Color;
import com.example.setcardgame.Model.Difficulty;
import com.example.setcardgame.Model.Quantity;
import com.example.setcardgame.Model.Shape;
import com.example.setcardgame.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class SingleplayerActivity extends AppCompatActivity {

    private ArrayList<ImageView> board = new ArrayList<>();
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Card> boardCards = new ArrayList<>();
    private ArrayList<Card> selectedCards = new ArrayList<>();
    private ArrayList<Integer> selectedCardIds = new ArrayList<>();
    private TextView pointTextView;
    private TextView timerTextView;
    private Difficulty difficulty = Difficulty.NORMAL;

    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    private Timer resetBackgroundTimer = new Timer();
    private boolean stopUserInteractions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Intent sp = getIntent();
        if (!sp.getStringExtra("diffMode").isEmpty()){
            difficulty = Difficulty.valueOf(sp.getStringExtra("diffMode"));
        }
        startGame();
        timer = new Timer();
        startTimer();
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timerTextView.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1000);
    }

    private String getTimerText() {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400)%3600)%60;
        int minutes = ((rounded % 86400)%3600)/60;

        return String.format("%d:%02d", minutes, seconds);
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

        if (difficulty == Difficulty.NORMAL){
            TableLayout tableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
            TableRow lastTableRow = (TableRow) findViewById(R.id.tableRow3);
            tableLayout.removeView(lastTableRow);
        }

        if (difficulty == Difficulty.EASY){
            board.add((ImageView)findViewById(R.id.card9));
            board.add((ImageView)findViewById(R.id.card10));
            board.add((ImageView)findViewById(R.id.card11));
        }

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

        pointTextView = findViewById(R.id.pointTextView);
        timerTextView = findViewById(R.id.timerTextView);
        pointTextView.setText("0");
        timerTextView.setText("0:00");
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
                    int point = Integer.parseInt((String) pointTextView.getText());
                    pointTextView.setText(String.valueOf(++point));
                    stopUserInteractions = true;
                    removeCardsFromBoard();

                    if (isGameOver() || !hasSet(boardCards)) {
                        timerTask.cancel();
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
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
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
        else{
            for (int i=0; board.size()>i; i++){
                board.get(i).setBackgroundResource(R.drawable.card_background_empty);
            }
            stopUserInteractions = false;
        }

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
        egs.putExtra("time", timerTextView.getText());
        egs.putExtra("score", pointTextView.getText());
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