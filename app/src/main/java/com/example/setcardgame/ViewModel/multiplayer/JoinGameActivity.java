package com.example.setcardgame.ViewModel.multiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.setcardgame.R;

public class JoinGameActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        Intent jg = getIntent();
        username = jg.getStringExtra("username");
    }

    public void switchToMultiplayer(View v){
        //websocket stuff
        Intent mp = new Intent(this, MultiplayerActivity.class);
        mp.putExtra("username", username);
        startActivity(mp);
    }
}