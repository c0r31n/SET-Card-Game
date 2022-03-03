package com.example.setcardgame.ViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.setcardgame.R;

public class EndGameScreen extends AppCompatActivity {
    private String finalTime;
    private String finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_screen);

        Intent egs = getIntent();
        finalTime = egs.getStringExtra("time");
        finalScore = egs.getStringExtra("score");
        TextView finalTimeTextView = (TextView)findViewById(R.id.finalTimeTextView);
        TextView finalScoreTextView = (TextView)findViewById(R.id.finalPointTextView);
        finalTimeTextView.setText(finalTime);
        finalScoreTextView.setText(finalScore);
    }

    public void newSingleplayerGame(View v){
        Intent sp = new Intent(this, SingleplayerActivity.class);
        startActivity(sp);
    }

    public void backToMenu(View v){
        Intent m = new Intent(this, MainActivity.class);
        startActivity(m);
    }
}