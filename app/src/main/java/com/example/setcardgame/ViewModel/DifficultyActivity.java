package com.example.setcardgame.ViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.setcardgame.Model.Difficulty;
import com.example.setcardgame.R;

public class DifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
    }

    public void switchToSingleplayer(View v){
        Intent sp = new Intent(this, SingleplayerActivity.class);
        if (findViewById(v.getId())==findViewById(R.id.easyBtn)){
            sp.putExtra("diffMode", Difficulty.EASY.toString());
        }
        else {
            sp.putExtra("diffMode", Difficulty.NORMAL.toString());
        }
        startActivity(sp);
    }
}