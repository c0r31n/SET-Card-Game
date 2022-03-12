package com.example.setcardgame.ViewModel.multiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.setcardgame.R;

public class SelectMultiplayerTypeActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_multiplayer_type);
        Intent smt = getIntent();
        username = smt.getStringExtra("username");
    }

    public void switchToPrivateGame(View v){
        Intent sb = new Intent(this, PrivateGameActivity.class);
        sb.putExtra("username", username);
        startActivity(sb);
    }

    public void switchToRandomGame(View v){
        Intent wfg = new Intent(this, WaitingForGameActivity.class);
        wfg.putExtra("username", username);
        startActivity(wfg);
    }
}