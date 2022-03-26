package com.example.setcardgame.ViewModel.multiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.setcardgame.R;

public class PrivateGameActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_game);
        Intent pg = getIntent();
        username = pg.getStringExtra("username");
    }

    public void switchToJoinGame(View v){
        Intent jg = new Intent(this, JoinGameActivity.class);
        jg.putExtra("username", username);
        startActivity(jg);
    }

    public void switchToCreateGame(View v){
        Intent cpg = new Intent(this, CreatePrivateGameActivity.class);
        cpg.putExtra("username", username);
        startActivity(cpg);
    }
}