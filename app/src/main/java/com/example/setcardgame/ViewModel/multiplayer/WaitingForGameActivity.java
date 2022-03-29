package com.example.setcardgame.ViewModel.multiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.setcardgame.Model.Game;
import com.example.setcardgame.Model.Username;
import com.example.setcardgame.Model.WebsocketClient;
import com.example.setcardgame.R;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WaitingForGameActivity extends AppCompatActivity {

    private Game game;
    private String username = Username.getUsername();

    private final String TAG = "waiting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_game);

        WebsocketClient.createWebsocket(WebsocketClient.URL+"multiconnect");
        Disposable topic = WebsocketClient.mStompClient.topic("/topic/waiting").subscribe(topicMessage -> {
            try{
                JSONObject msg = new JSONObject(topicMessage.getPayload());
                if(username.equals(msg.getString("player1"))){
                    game = new Game(msg);
                    Log.d(TAG, game.getGameId()+"");
                    if (!msg.getString("player2").equals("null")){
                        switchToMultiplayer();
                    }
                }
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
        WebsocketClient.compositeDisposable.add(topic);

        JSONObject jsonPlayer = new JSONObject();
        try {
            jsonPlayer.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WebsocketClient.mStompClient.send("/app/connect/random", jsonPlayer.toString()).subscribe();
    }

    public void switchToMultiplayer(){
        Intent mp = new Intent(this, MultiplayerActivity.class);
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

            WebsocketClient.mStompClient.send("/app/game/destroy", destroyGame.toString()).subscribe();

            Log.d(TAG, "Game destroyed");

        }
        WebsocketClient.disconnectWebsocket();
        game = null;

        Intent mp = new Intent(this, SelectMultiplayerTypeActivity.class);
        startActivity(mp);
    }
}