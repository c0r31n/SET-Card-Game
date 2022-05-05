package com.example.setcardgame.ViewModel.multiplayer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.setcardgame.Model.Username;
import com.example.setcardgame.R;
import com.example.setcardgame.ViewModel.MainActivity;

public class MultiplayerEndScreenActivity extends AppCompatActivity {

    private final String username = Username.getUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_end_screen);
        Intent mpes = getIntent();
        String opponentScore = mpes.getStringExtra("opponentScore");
        String ownScore = mpes.getStringExtra("ownScore");
        String winner = mpes.getStringExtra("winner");
        String opponent = mpes.getStringExtra("opponent");
        TextView score = findViewById(R.id.scoreForMultiTextView);
        TextView ownScoreTV = findViewById(R.id.ownScoreTextView);
        TextView opponentScoreTV = findViewById(R.id.opponentScoreTextView);
        ownScoreTV.setText(ownScore);
        opponentScoreTV.setText(opponentScore);

        if (winner.equals(username)) {
            score.setText(R.string.won);
            score.setTextColor(Color.parseColor("#008000"));
            ownScoreTV.setTextColor(Color.parseColor("#008000"));
        } else if(winner.equals(opponent)){
            score.setText(R.string.lost);
            score.setTextColor(Color.parseColor("#C50202"));
            ownScoreTV.setTextColor(Color.parseColor("#C50202"));
        }
        else {
            score.setText(R.string.draw);
            score.setTextColor(Color.parseColor("#115EA1"));
        }
    }

    public void switchToMultiplayerType(View v) {
        Intent wfg = new Intent(this, SelectMultiplayerTypeActivity.class);
        startActivity(wfg);
    }

    public void backToMenu(View v) {
        Intent m = new Intent(this, MainActivity.class);
        startActivity(m);
    }
}