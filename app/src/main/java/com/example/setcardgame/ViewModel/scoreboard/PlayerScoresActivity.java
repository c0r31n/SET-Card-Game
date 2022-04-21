package com.example.setcardgame.ViewModel.scoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.setcardgame.Service.ScoreboardDataService;
import com.example.setcardgame.Model.Scoreboard;
import com.example.setcardgame.Model.Username;
import com.example.setcardgame.R;

import java.util.List;

public class PlayerScoresActivity extends AppCompatActivity {

    private String username = Username.getUsername();
    private ScoreboardDataService scoreboardDataService = new ScoreboardDataService(PlayerScoresActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_scores);
        getMyScores();
    }

    public void switchToScoreboard(View v){
        Intent sb = new Intent(this, ScoreboardActivity.class);
        startActivity(sb);
    }

    public void getMyScores(){
        scoreboardDataService.getPlayerScores(true, username, new ScoreboardDataService.ScoreboardResponseListener(){
            @Override
            public void onError(String message) {
                Toast.makeText(PlayerScoresActivity.this, "Can't get scores.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Scoreboard> scoreboardModels) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(PlayerScoresActivity.this, android.R.layout.simple_list_item_1, scoreboardModels);

                ListView scoresListView = findViewById(R.id.scoresListView);
                scoresListView.setAdapter(arrayAdapter);
            }
        } );
    }
}