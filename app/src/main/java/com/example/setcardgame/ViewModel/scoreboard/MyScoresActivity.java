package com.example.setcardgame.ViewModel.scoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.setcardgame.R;

public class MyScoresActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scores);
        Intent ms = getIntent();
        username = ms.getStringExtra("username");
    }

    public void switchToScoreboard(View v){
        Intent sb = new Intent(this, ScoreboardActivity.class);
        sb.putExtra("username", username);
        startActivity(sb);
    }
}