package com.example.setcardgame.ViewModel.scoreboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.setcardgame.Model.Difficulty;
import com.example.setcardgame.Model.Username;
import com.example.setcardgame.Model.scoreboard.ScoresFragment;
import com.example.setcardgame.Model.scoreboard.ViewPagerAdapter;
import com.example.setcardgame.Model.scoreboard.Scoreboard;
import com.example.setcardgame.R;
import com.example.setcardgame.Service.ScoreboardDataService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class WorldScoresActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private final String username = Username.getUsername();
    private final ScoreboardDataService scoreboardDataService = new ScoreboardDataService(WorldScoresActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        tabLayout = findViewById(R.id.tabLayoutPlayer);
        viewPager = findViewById(R.id.viewPagerPlayer);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        scoreboardDataService.getPlayerScores(false, username, new ScoreboardDataService.ScoreboardResponseListener(){
            @Override
            public void onError(String message) {
                Toast.makeText(WorldScoresActivity.this, getString(R.string.cantGetScores), Toast.LENGTH_SHORT).show();
                Log.d("score", getString(R.string.cantGetScores));
            }

            @Override
            public void onResponse(List<Scoreboard> scoreboardModels) {
                int easyCounter = 0;
                int normalCounter = 0;
                ArrayList<Scoreboard> easyScoreList = new ArrayList<>();
                ArrayList<Scoreboard> normalScoreList = new ArrayList<>();

                for(Scoreboard score : scoreboardModels) {
                    if(score.getPlayerId().toString().equals(username)){
                        score.setMyScore(true);
                    }
                    if(score.getDifficulty() == Difficulty.EASY && easyCounter<100){
                        easyCounter++;
                        score.setPlacement(easyCounter);
                        easyScoreList.add(score);
                    }
                    if(score.getDifficulty() == Difficulty.NORMAL && normalCounter<100){
                        normalCounter++;
                        score.setPlacement(normalCounter);
                        normalScoreList.add(score);
                    }
                }

                adapter.addFragment(new ScoresFragment(easyScoreList), String.format("%s", getString(R.string.easy)));
                adapter.addFragment(new ScoresFragment(normalScoreList), String.format("%s", getString(R.string.normal)));

                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        } );
    }
}