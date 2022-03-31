package com.example.setcardgame.ViewModel.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.setcardgame.R;

public class SelectMultiplayerTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_multiplayer_type);
    }

    public void switchToPrivateGame(View v) {
        Intent sb = new Intent(this, PrivateGameActivity.class);
        startActivity(sb);
    }

    public void switchToRandomGame(View v) {
        Intent wfg = new Intent(this, WaitingForGameActivity.class);
        startActivity(wfg);
    }
}