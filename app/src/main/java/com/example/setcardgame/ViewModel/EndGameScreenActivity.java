package com.example.setcardgame.ViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.setcardgame.Model.Difficulty;
import com.example.setcardgame.R;

public class EndGameScreenActivity extends AppCompatActivity {
    private String finalTime;
    private String finalScore;
    private String finalDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_screen);

        Intent egs = getIntent();
        finalTime = egs.getStringExtra("time");
        finalScore = egs.getStringExtra("score");
        finalDifficulty = egs.getStringExtra("diff");
        TextView finalTimeTextView = (TextView)findViewById(R.id.finalTimeTextView);
        TextView finalScoreTextView = (TextView)findViewById(R.id.finalPointTextView);
        TextView finalDifficultyTextView = (TextView)findViewById(R.id.difficultyTextView);
        finalTimeTextView.setText(finalTime);
        finalScoreTextView.setText(finalScore);
        finalDifficultyTextView.setText(finalDifficulty);
    }

    public void newSingleplayerGame(View v){
        Intent sp = new Intent(this, SingleplayerActivity.class);
        sp.putExtra("diffMode", finalDifficulty);
        startActivity(sp);
    }

    public void backToMenu(View v){
        Intent m = new Intent(this, MainActivity.class);
        startActivity(m);
    }
}