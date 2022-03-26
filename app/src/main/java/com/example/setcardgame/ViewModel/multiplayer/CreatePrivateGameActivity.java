package com.example.setcardgame.ViewModel.multiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.example.setcardgame.R;

public class CreatePrivateGameActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_private_game);
        Intent cpg = getIntent();
        username = cpg.getStringExtra("username");
    }

    public void switchToMultiplayer(View v){
        //websocket stuff
        Intent mp = new Intent(this, MultiplayerActivity.class);
        mp.putExtra("username", username);
        startActivity(mp);
    }

    public void deleteGame(View v){
        //websocket stuff
        Intent pg = new Intent(this, PrivateGameActivity.class);
        pg.putExtra("username", username);
        startActivity(pg);
    }
}