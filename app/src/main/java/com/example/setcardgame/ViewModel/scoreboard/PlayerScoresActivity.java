package com.example.setcardgame.ViewModel.scoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.setcardgame.Model.ScoreboardDataService;
import com.example.setcardgame.Model.ScoreboardModel;
import com.example.setcardgame.R;
import com.example.setcardgame.ViewModel.MainActivity;

import java.util.List;

public class PlayerScoresActivity extends AppCompatActivity {

    private String username;
    private ScoreboardDataService scoreboardDataService = new ScoreboardDataService(PlayerScoresActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_scores);
        Intent ms = getIntent();
        username = ms.getStringExtra("username");
        Log.d("name", username);
        getMyScores();
    }

    public void switchToScoreboard(View v){
        Intent sb = new Intent(this, ScoreboardActivity.class);
        sb.putExtra("username", username);
        startActivity(sb);
    }

    public void getMyScores(){
        scoreboardDataService.getPlayerScores(true, username, new ScoreboardDataService.ScoreboardResponseListener(){
            @Override
            public void onError(String message) {
                Toast.makeText(PlayerScoresActivity.this, "Can't get scores.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<ScoreboardModel> scoreboardModels) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(PlayerScoresActivity.this, android.R.layout.simple_list_item_1, scoreboardModels);

                ListView scoresListView = findViewById(R.id.scoresListView);
                scoresListView.setAdapter(arrayAdapter);
            }
        } );
    }
}