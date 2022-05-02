package com.example.setcardgame.ViewModel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.setcardgame.Model.Username;
import com.example.setcardgame.R;
import com.example.setcardgame.ViewModel.multiplayer.SelectMultiplayerTypeActivity;
import com.example.setcardgame.ViewModel.scoreboard.ScoreboardActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sp.getString("username", "default").equals("default")) {
            SharedPreferences.Editor editor = sp.edit();
            UUID username = UUID.randomUUID();
            editor.putString("username", username.toString());
            editor.apply();
        }
        Username.setUsername(sp.getString("username", "def"));

        checkServer();
    }

    private void checkServer() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://test-set-card-game.herokuapp.com/check/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("check", "available");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("check", "not available");
                Toast.makeText(MainActivity.this, getString(R.string.serverUnavailable), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    public void switchToScoreboard(View v) {
        Intent sb = new Intent(this, ScoreboardActivity.class);
        startActivity(sb);
    }

    public void switchToDifficulty(View v) {
        Intent d = new Intent(this, DifficultyActivity.class);
        startActivity(d);
    }

    public void switchToMultiplayer(View v) {
        Intent mp = new Intent(this, SelectMultiplayerTypeActivity.class);
        startActivity(mp);
    }

    public void switchToHowToPage(View v) {
        Intent htp = new Intent(this, HowToPageActivity.class);
        startActivity(htp);
    }
}