package com.example.setcardgame.ViewModel.multiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.setcardgame.R;
import com.example.setcardgame.ViewModel.MainActivity;

public class WaitingForGameActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_game);
        Intent wfg = getIntent();
        username = wfg.getStringExtra("username");
    }

    public void switchToMultiplayer(View v){
        //websocket stuff
        Intent mp = new Intent(this, MultiplayerActivity.class);
        mp.putExtra("username", username);
        startActivity(mp);
    }

    public void switchBackToSelectMultiplayerType(View v){
        //delete game
        Intent mp = new Intent(this, SelectMultiplayerTypeActivity.class);
        mp.putExtra("username", username);
        startActivity(mp);
    }
}