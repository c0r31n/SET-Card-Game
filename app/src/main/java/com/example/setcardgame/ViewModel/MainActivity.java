package com.example.setcardgame.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.setcardgame.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void switchToScoreboard(View v){
        Intent sb = new Intent(this, ScoreboardActivity.class);
        startActivity(sb);
    }

    public void switchToDifficulty(View v){
        Intent d = new Intent(this, DifficultyActivity.class);
        startActivity(d);
    }

    public void switchToMultiplayer(View v){
        Intent mp = new Intent(this, MultiplayerActivity.class);
        startActivity(mp);
    }

    public void switchToHowToPage(View v){
        Intent htp = new Intent(this, HowToPageActivity.class);
        startActivity(htp);
    }
}