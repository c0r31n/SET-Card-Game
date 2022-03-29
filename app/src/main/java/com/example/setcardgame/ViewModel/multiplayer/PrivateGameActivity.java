package com.example.setcardgame.ViewModel.multiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.setcardgame.R;

public class PrivateGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_game);
    }

    public void switchToJoinGame(View v){
        Intent jg = new Intent(this, JoinGameActivity.class);
        startActivity(jg);
    }

    public void switchToCreateGame(View v){
        Intent cpg = new Intent(this, CreatePrivateGameActivity.class);
        startActivity(cpg);
    }
}