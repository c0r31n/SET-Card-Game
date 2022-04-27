package com.example.setcardgame.ViewModel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.setcardgame.Model.Difficulty;
import com.example.setcardgame.Model.SingleplayerGame;
import com.example.setcardgame.Model.card.Card;
import com.example.setcardgame.Model.card.Color;
import com.example.setcardgame.Model.card.Quantity;
import com.example.setcardgame.Model.card.Shape;
import com.example.setcardgame.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class SingleplayerActivity extends AppCompatActivity {

    private final ArrayList<ImageView> board = new ArrayList<>();
    private final ArrayList<Card> cards = new ArrayList<>();
    private final ArrayList<Card> boardCards = new ArrayList<>();
    private final ArrayList<Card> selectedCards = new ArrayList<>();
    private final ArrayList<Integer> selectedCardIds = new ArrayList<>();
    private final Timer resetBackgroundTimer = new Timer();
    private TextView pointTextView;
    private TextView timerTextView;
    private Difficulty difficulty = Difficulty.NORMAL;
    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;
    private boolean stopUserInteractions = false;
    private SingleplayerGame game = new SingleplayerGame();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);

        Intent sp = getIntent();
        if (!sp.getStringExtra("diffMode").isEmpty()) {
            difficulty = Difficulty.getDifficultyFromString(sp.getStringExtra("diffMode"));
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
                        int time = getTimer();
                        int seconds = time % 60;
                        int minutes = time / 60;
                        timerTextView.setText(String.format("%d:%02d", minutes, seconds));
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private int getTimer() {
        int rounded = (int) Math.round(time);

        return (rounded % 86400) % 3600;
    }

    private void startGame() {
        board.clear();
        boardCards.clear();
        cards.clear();
        selectedCards.clear();
        selectedCardIds.clear();

        board.add((ImageView) findViewById(R.id.card0));
        board.add((ImageView) findViewById(R.id.card1));
        board.add((ImageView) findViewById(R.id.card2));
        board.add((ImageView) findViewById(R.id.card3));
        board.add((ImageView) findViewById(R.id.card4));
        board.add((ImageView) findViewById(R.id.card5));
        board.add((ImageView) findViewById(R.id.card6));
        board.add((ImageView) findViewById(R.id.card7));
        board.add((ImageView) findViewById(R.id.card8));

        TableLayout tableLayout = (TableLayout) findViewById(R.id.gameTableLayout);

        if (difficulty == Difficulty.NORMAL) {
            TableRow lastTableRow = (TableRow) findViewById(R.id.tableRow3);
            tableLayout.removeView(lastTableRow);
        }

        if (difficulty == Difficulty.EASY) {
            board.add((ImageView) findViewById(R.id.card9));
            board.add((ImageView) findViewById(R.id.card10));
            board.add((ImageView) findViewById(R.id.card11));

            if (getScreeSizeInInches() < 5.3) {
                ImageView card = (ImageView) findViewById(R.id.card8);
                ViewGroup.LayoutParams params = card.getLayoutParams();
                params.height *= 0.8;
                params.width *= 0.8;

                for (ImageView cards : board) {
                    cards.setLayoutParams(params);
                }
            }
        }

        do {
            cards.clear();
            for (Color color : Color.values()) {
                for (Shape shape : Shape.values()) {
                    for (Quantity quantity : Quantity.values()) {
                        cards.add(new Card(color, shape, quantity));
                    }
                }
            }

            Collections.shuffle(cards);

            for (int i = 0; board.size() > i; i++) {
                ImageView img = board.get(i);
                int resImage = getResources().getIdentifier(cards.get(0).toString(), "drawable", getPackageName());
                img.setImageResource(resImage);
                img.setContentDescription(cards.get(0).toString());
                boardCards.add(cards.get(0));
                cards.remove(0);
            }
        }while (!game.hasSet(boardCards, board));

        pointTextView = findViewById(R.id.opponentPointTextView);
        timerTextView = findViewById(R.id.timerTextView);
        pointTextView.setText("0");
        timerTextView.setText("0:00");
    }

    public void onCardClick(View view) {
        if (!selectedCardIds.contains(view.getId())) {
            boolean found = false;
            int counter = 0;
            while (!found && board.size() > counter) {
                if (board.get(counter).getId() == view.getId()) {
                    found = true;
                    view.setBackgroundResource(R.drawable.card_background_selected);
                    selectedCardIds.add(view.getId());
                    selectedCards.add(boardCards.get(counter));
                }
                counter++;
            }

            if (selectedCardIds.size() == 3) {
                if(game.hasSet(selectedCards, board)){
                    for (int i = 0; board.size() > i; i++) {
                        if (board.get(i).getId() == selectedCardIds.get(0)
                                || board.get(i).getId() == selectedCardIds.get(1)
                                || board.get(i).getId() == selectedCardIds.get(2)) {
                            board.get(i).setBackgroundResource(R.drawable.card_background_right);
                        }
                    }
                    int point = Integer.parseInt((String) pointTextView.getText());
                    pointTextView.setText(String.valueOf(++point));
                    stopUserInteractions = true;
                    removeCardsFromBoard();

                    if(game.isGameOver(board) || !game.hasSet(boardCards, board)){
                        timerTask.cancel();
                        endGame();
                    }

                } else {
                    for (int i = 0; board.size() > i; i++) {
                        if (board.get(i).getId() == selectedCardIds.get(0)
                                || board.get(i).getId() == selectedCardIds.get(1)
                                || board.get(i).getId() == selectedCardIds.get(2)) {
                            board.get(i).setBackgroundResource(R.drawable.card_background_wrong);
                        }
                    }
                    stopUserInteractions = true;
                }
                selectedCardIds.clear();
                selectedCards.clear();
                resetCardBackgrounds();
            }
        } else {
            for (int i = 0; board.size() > i; i++) {
                if (board.get(i).getId() == view.getId()) {
                    board.get(i).setBackgroundResource(R.color.trans);
                }
            }
            selectedCards.remove(selectedCardIds.indexOf(view.getId()));
            selectedCardIds.remove((Integer) view.getId());
        }

    }

    private void resetCardBackgrounds() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resetBackgroundTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; board.size() > i; i++) {
                        board.get(i).setBackgroundResource(R.color.trans);
                    }
                    stopUserInteractions = false;
                }
            }, 300);
        } else {
            for (int i = 0; board.size() > i; i++) {
                board.get(i).setBackgroundResource(R.color.trans);
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

    private void removeCardsFromBoard() {
        for (int i = 0; board.size() > i; i++) {
            ImageView img = board.get(i);
            if (img.getId() == selectedCardIds.get(0)
                    || img.getId() == selectedCardIds.get(1)
                    || img.getId() == selectedCardIds.get(2)) {
                if (cards.size() > 0) {
                    int resImage = getResources().getIdentifier(cards.get(0).toString(), "drawable", getPackageName());
                    img.setImageResource(resImage);
                    img.setContentDescription(cards.get(0).toString());
                    boardCards.set(i, cards.get(0));
                    cards.remove(0);
                } else {
                    img.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void endGame() {
        Intent egs = new Intent(this, EndGameScreenActivity.class);
        egs.putExtra("time", "" + getTimer());
        egs.putExtra("score", pointTextView.getText());
        egs.putExtra("diff", difficulty.toString());
        startActivity(egs);
    }

    private double getScreeSizeInInches() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double mWidthPixels = dm.widthPixels;
        double mHeightPixels = dm.heightPixels;
        double x = Math.pow(mWidthPixels / dm.xdpi, 2);
        double y = Math.pow(mHeightPixels / dm.ydpi, 2);
        return Math.sqrt(x + y);
    }
}