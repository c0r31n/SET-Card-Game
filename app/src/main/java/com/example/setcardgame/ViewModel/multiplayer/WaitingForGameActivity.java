package com.example.setcardgame.ViewModel.multiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.setcardgame.Model.Game;
import com.example.setcardgame.R;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WaitingForGameActivity extends AppCompatActivity {

    private StompClient client;
    private Game game;
    private Disposable disposable;
    private String username;
    private final String url = "wss://test-set-card-game.herokuapp.com/";

    private final String TAG = "waiting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_game);
        Intent wfg = getIntent();
        username = wfg.getStringExtra("username");

        client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url+"multiconnect");
        client.connect();

        JSONObject jsonPlayer = new JSONObject();
        try {
            jsonPlayer.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.send("/app/connect/random", jsonPlayer.toString()).subscribe();

        disposable = client.topic("/topic/waiting").subscribe(topicMessage -> { //stop search után nem jön info
            try{
                JSONObject msg = new JSONObject(topicMessage.getPayload());
                if(username.equals(msg.getString("player1"))){
                    game = new Game(msg);
                    Log.d(TAG, game.getGameId()+"");
                    if (!msg.getString("player2").equals("null")){                    //második kliens nem iratkozik fel?
                        switchToMultiplayer();
                    }
                }

                Log.d(TAG, "player1: " + msg.getString("player1"));
                Log.d(TAG, "player2: " + msg.getString("player2"));
                Log.d(TAG, "username: " + username);
                if (username.equals(msg.getString("player2")) && !msg.getString("player1").equals("null")){
                    game = new Game(msg);
                    switchToMultiplayer();
                }

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, throwable -> {
            Log.d(TAG, "error");
        });

        client.withClientHeartbeat(15000);
    }

    public void switchToMultiplayer(){
        disposable.dispose();
        client.disconnect();
        Intent mp = new Intent(this, MultiplayerActivity.class);
        mp.putExtra("username", username);
        mp.putExtra("gameId", Integer.toString(game.getGameId()));
        startActivity(mp);
    }

    public void switchBackToSelectMultiplayerType(View v){
        if (game != null){
            JSONObject destroyGame = new JSONObject();
            try {
                destroyGame.put("gameId", game.getGameId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            client.send("/app/game/destroy", destroyGame.toString()).subscribe();

            Log.d(TAG, "Game destroyed");

        }
        disposable.dispose();
        client.disconnect();
        game = null;
        client = null;

        Intent mp = new Intent(this, SelectMultiplayerTypeActivity.class);
        mp.putExtra("username", username);
        startActivity(mp);
    }
}