package com.example.setcardgame.ViewModel.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.setcardgame.Model.MultiplayerGame;
import com.example.setcardgame.Model.Username;
import com.example.setcardgame.Config.WebSocketClient;
import com.example.setcardgame.R;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

public class JoinGameActivity extends AppCompatActivity {

    private final String TAG = "alma";
    private final String username = Username.getUsername();
    private EditText connectionCodeET;
    private MultiplayerGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        connectionCodeET = findViewById(R.id.connectionCodeET);

        WebSocketClient.createWebSocket(WebSocketClient.URL + "multiconnect");
        Disposable topic = WebSocketClient.mStompClient.topic("/topic/waiting").subscribe(topicMessage -> {
            try {
                JSONObject msg = new JSONObject(topicMessage.getPayload());
                if (username.equals(msg.getString("player2")) && !msg.getString("player1").equals("null")) {
                    game = new MultiplayerGame(msg);
                    switchToMultiplayer();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, throwable -> {
            Log.d(TAG, "error");
        });
        WebSocketClient.compositeDisposable.add(topic);
    }

    public void joinGame(View view) {
        if (!connectionCodeET.getText().equals("")) {
            JSONObject jsonConnect = new JSONObject();
            try {
                jsonConnect.put("gameId", connectionCodeET.getText());
                jsonConnect.put("playerId", username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            WebSocketClient.mStompClient.send("/app/connect", jsonConnect.toString()).subscribe();
        }
    }

    private void switchToMultiplayer() {
        Intent mp = new Intent(this, MultiplayerActivity.class);
        mp.putExtra("gameId", Integer.toString(game.getGameId()));
        startActivity(mp);
    }
}