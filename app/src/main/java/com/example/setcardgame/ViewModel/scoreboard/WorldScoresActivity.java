package com.example.setcardgame.ViewModel.scoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.setcardgame.Model.ScoreboardDataService;
import com.example.setcardgame.Model.ScoreboardModel;
import com.example.setcardgame.R;

import java.util.List;
import java.util.UUID;

public class WorldScoresActivity extends AppCompatActivity {

    private String username;
    private ScoreboardDataService scoreboardDataService = new ScoreboardDataService(WorldScoresActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_scores);
        Intent ws = getIntent();
        username = ws.getStringExtra("username");
        getWorldScores();
    }

    public void switchToScoreboard(View v){
        Intent sb = new Intent(this, ScoreboardActivity.class);
        sb.putExtra("username", username);
        startActivity(sb);
    }

    public void getWorldScores(){
        scoreboardDataService.getPlayerScores(false, username, new ScoreboardDataService.ScoreboardResponseListener(){
            @Override
            public void onError(String message) {
                Toast.makeText(WorldScoresActivity.this, "Can't get world scores.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<ScoreboardModel> scoreboardModels) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(WorldScoresActivity.this, android.R.layout.simple_list_item_1, scoreboardModels);
                ListView scoresListView = findViewById(R.id.scoresListView);
                scoresListView.setAdapter(arrayAdapter);
            }
        } );
    }
}