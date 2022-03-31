package com.example.setcardgame.ViewModel.multiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.setcardgame.Model.Username;
import com.example.setcardgame.R;
import com.example.setcardgame.ViewModel.MainActivity;
import com.example.setcardgame.ViewModel.SingleplayerActivity;

public class MultiplayerEndScreenActivity extends AppCompatActivity {

    private String username = Username.getUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_end_screen);
        Intent mpes = getIntent();
        String opponentScore = mpes.getStringExtra("opponentScore");
        String ownScore = mpes.getStringExtra("ownScore");
        String winner = mpes.getStringExtra("winner");
        TextView score = findViewById(R.id.scoreForMultiTextView);
        TextView ownScoreTV = findViewById(R.id.ownScoreTextView);
        TextView opponentScoreTV = findViewById(R.id.opponentScoreTextView);
        ownScoreTV.setText(ownScore);
        opponentScoreTV.setText(opponentScore);

        if (winner.equals(username)){
            score.setText("WON");
            score.setTextColor(Color.parseColor("#008000"));
            ownScoreTV.setTextColor(Color.parseColor("#008000"));
        }
        else {
            score.setText("LOST");
            score.setTextColor(Color.parseColor("#C50202"));
            ownScoreTV.setTextColor(Color.parseColor("#C50202"));
        }
    }

    public void switchToRandomGame(View v){
        Intent wfg = new Intent(this, WaitingForGameActivity.class);
        startActivity(wfg);
    }

    public void backToMenu(View v){
        Intent m = new Intent(this, MainActivity.class);
        startActivity(m);
    }
}