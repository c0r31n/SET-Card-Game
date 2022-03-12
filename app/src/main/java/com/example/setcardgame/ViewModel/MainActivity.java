package com.example.setcardgame.ViewModel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.setcardgame.R;
import com.example.setcardgame.ViewModel.multiplayer.SelectMultiplayerTypeActivity;
import com.example.setcardgame.ViewModel.scoreboard.ScoreboardActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sp.getString("username", "default").isEmpty()){
            SharedPreferences.Editor editor = sp.edit();
            UUID username = UUID.randomUUID();
            editor.putString("username", username.toString());
            editor.commit();
        }
    }

    public void switchToScoreboard(View v){
        Intent sb = new Intent(this, ScoreboardActivity.class);
        sb.putExtra("username", sp.getString("username", "default"));
        startActivity(sb);
    }

    public void switchToDifficulty(View v){
        Intent d = new Intent(this, DifficultyActivity.class);
        d.putExtra("username", sp.getString("username", "default"));
        startActivity(d);
    }

    public void switchToMultiplayer(View v){
        Intent mp = new Intent(this, SelectMultiplayerTypeActivity.class);
        mp.putExtra("username", sp.getString("username", "default"));
        startActivity(mp);
    }

    public void switchToHowToPage(View v){
        Intent htp = new Intent(this, HowToPageActivity.class);
        startActivity(htp);
    }
}