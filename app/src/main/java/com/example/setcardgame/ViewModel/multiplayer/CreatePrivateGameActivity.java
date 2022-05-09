package com.example.setcardgame.ViewModel.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.setcardgame.Model.MultiplayerGame;
import com.example.setcardgame.Model.Username;
import com.example.setcardgame.Config.WebSocketClient;
import com.example.setcardgame.R;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

public class CreatePrivateGameActivity extends AppCompatActivity {

    private final String username = Username.getUsername();
    private MultiplayerGame game;
    private TextView connectionCodeTV;

    private final String TAG = "privateGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_private_game);
        connectionCodeTV = findViewById(R.id.connectionCodeTV);

        WebSocketClient.createWebSocket(WebSocketClient.URL+"multiconnect");
        Disposable topic = WebSocketClient.mStompClient.topic("/topic/waiting").subscribe(topicMessage -> {
            try{
                JSONObject msg = new JSONObject(topicMessage.getPayload());
                if(username.equals(msg.getString("player1"))){
                    game = new MultiplayerGame(msg);
                    Log.d(TAG, topicMessage.getPayload());

                    runOnUiThread (new Thread(new Runnable() {
                        public void run() {
                            connectionCodeTV.setText(Integer.toString(game.getGameId()));
                        }
                    }));

                    if (!msg.getString("player2").equals("null")){
                        switchToMultiplayer();
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, throwable -> {
            Log.d(TAG, "error at subscribing");
        });
        WebSocketClient.compositeDisposable.add(topic);

        JSONObject jsonPlayer = new JSONObject();
        try {
            jsonPlayer.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WebSocketClient.mStompClient.send("/app/create", jsonPlayer.toString()).subscribe();
    }

    public void switchToMultiplayer(){
        Intent mp = new Intent(this, MultiplayerActivity.class);
        mp.putExtra("gameId", Integer.toString(game.getGameId()));
        startActivity(mp);
    }

    public void deleteGame(View v){
        if (game != null){
            JSONObject destroyGame = new JSONObject();
            try {
                destroyGame.put("gameId", game.getGameId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            WebSocketClient.mStompClient.send("/app/game/destroy", destroyGame.toString()).subscribe();

            Log.d(TAG, "Game destroyed");

        }
        WebSocketClient.disconnectWebSocket();
        game = null;

        Intent pg = new Intent(this, PrivateGameActivity.class);
        startActivity(pg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (game != null){
            JSONObject destroyGame = new JSONObject();
            try {
                destroyGame.put("gameId", game.getGameId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            WebSocketClient.mStompClient.send("/app/game/destroy", destroyGame.toString()).subscribe();

            Log.d(TAG, "Game destroyed");
        }
        WebSocketClient.disconnectWebSocket();
        game = null;
    }
}