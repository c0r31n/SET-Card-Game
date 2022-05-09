package com.example.setcardgame.Service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.setcardgame.Config.RequestQueueSingleton;
import com.example.setcardgame.Model.scoreboard.Scoreboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardDataService {

    public static final String SCOREBOARD_URL = "https://test-set-card-game.herokuapp.com/scoreboard/";
    Context context;

    public ScoreboardDataService(Context context) {
        this.context = context;
    }

    public void getPlayerScores(boolean usesUsername, String username, ScoreboardResponseListener scoreboardResponseListener) {
        List<Scoreboard> scores = new ArrayList<>();
        String url = "";
        if (usesUsername) {
            url = SCOREBOARD_URL + "player/" + username;
        } else {
            url = SCOREBOARD_URL + "top";
        }

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; response.length() > i; i++) {
                                JSONObject JSONScore = response.getJSONObject(i);
                                Scoreboard score = new Scoreboard(JSONScore.getInt("scoreId"), JSONScore.getString("playerId"), JSONScore.getString("difficulty"), JSONScore.getInt("score"), JSONScore.getInt("time"));
                                scores.add(score);
                            }
                            scoreboardResponseListener.onResponse(scores);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                scoreboardResponseListener.onError("Did not get score");
            }
        });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(arrayRequest);
    }

    public void addScore(Scoreboard scoreboardModel, ScoreAddedResponseListener scoreAddedResponseListener) {
        String url = SCOREBOARD_URL + "add";

        JSONObject postObj = new JSONObject();
        try {
            postObj.put("playerId", scoreboardModel.getPlayerId());
            postObj.put("difficulty", scoreboardModel.getDifficulty().toString());
            postObj.put("score", scoreboardModel.getScore());
            postObj.put("time", scoreboardModel.getTime());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        scoreAddedResponseListener.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                scoreAddedResponseListener.onError(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface ScoreboardResponseListener {
        void onError(String message);

        void onResponse(List<Scoreboard> scoreboardModels);
    }

    public interface ScoreAddedResponseListener {
        void onError(String message);

        void onResponse(JSONObject scoreboardModels);
    }
}
