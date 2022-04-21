package com.example.setcardgame.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.setcardgame.Model.ScoreboardDataService;
import com.example.setcardgame.Model.ScoreboardModel;
import com.example.setcardgame.Model.Username;
import com.example.setcardgame.R;
import com.example.setcardgame.ViewModel.scoreboard.ScoreboardActivity;

import org.json.JSONObject;

public class EndGameScreenActivity extends AppCompatActivity {
    private int finalTime;
    private int minutes;
    private int seconds;
    private String finalScore;
    private String finalDifficulty;
    private final String username = Username.getUsername();
    private final ScoreboardDataService scoreboardDataService = new ScoreboardDataService(EndGameScreenActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_screen);

        Intent egs = getIntent();
        finalTime = Integer.parseInt(egs.getStringExtra("time"));
        finalScore = egs.getStringExtra("score");
        finalDifficulty = egs.getStringExtra("diff");
        seconds = finalTime % 60;
        minutes = finalTime / 60;
        TextView finalTimeTextView = (TextView) findViewById(R.id.finalTimeTextView);
        TextView finalScoreTextView = (TextView) findViewById(R.id.finalPointTextView);
        TextView finalDifficultyTextView = (TextView) findViewById(R.id.difficultyTextView);
        finalTimeTextView.setText(String.format("%d:%02d", minutes, seconds));
        finalScoreTextView.setText(finalScore);
        finalDifficultyTextView.setText(finalDifficulty);
        addScoreToDB();
    }

    public void newSingleplayerGame(View v) {
        Intent sp = new Intent(this, SingleplayerActivity.class);
        sp.putExtra("diffMode", finalDifficulty);
        startActivity(sp);
    }

    public void backToMenu(View v) {
        Intent m = new Intent(this, MainActivity.class);
        startActivity(m);
    }

    public void goToScoreBoard(View v) {
        Intent sb = new Intent(this, ScoreboardActivity.class);
        startActivity(sb);
    }

    public void addScoreToDB() {
        ScoreboardModel scoreboardModel = new ScoreboardModel(username, finalDifficulty, Integer.parseInt(finalScore), finalTime);
        scoreboardDataService.addScore(scoreboardModel, new ScoreboardDataService.ScoreAddedResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(EndGameScreenActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(JSONObject scoreboardModel) {
//                Log.d("score", scoreboardModel.toString());
            }
        });
    }
}