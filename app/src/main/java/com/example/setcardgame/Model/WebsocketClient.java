package com.example.setcardgame.Model;

import android.app.Activity;
import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebsocketClient {
    public static StompClient mStompClient;
    public static CompositeDisposable compositeDisposable;
    public static final String URL = "wss://test-set-card-game.herokuapp.com/";
    private static String TAG = "websocket";

    public static void createWebsocket(String WEBSOCKET_CONNECT_URL) {
        compositeDisposable = new CompositeDisposable();
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_CONNECT_URL);
        Disposable lifecycle = mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.i(TAG, "Stomp Connection Opened");
                    break;
                case ERROR:
                    Log.d(TAG, "Error ", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.w(TAG, "Stomp Connection Closed");

                    break;
                case FAILED_SERVER_HEARTBEAT:
                    Log.d(TAG, "Failed Server Heartbeat");
                    break;
            }
        });
        if (!mStompClient.isConnected()) {
            mStompClient.connect();
        }
        compositeDisposable.add(lifecycle);
        mStompClient.withClientHeartbeat(15000);
    }

    public static void disconnectWebsocket() {
        if (mStompClient != null) {
            if (mStompClient.isConnected()) {
                mStompClient.disconnect();
                compositeDisposable.dispose();
            }
        }

    }
}