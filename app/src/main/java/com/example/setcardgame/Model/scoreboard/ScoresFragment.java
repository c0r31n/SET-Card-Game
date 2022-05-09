package com.example.setcardgame.Model.scoreboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.setcardgame.R;

import java.util.List;

public class ScoresFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private List<Scoreboard> scoreList;

    public ScoresFragment(List<Scoreboard> scoreList) {
        this.scoreList = scoreList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_score, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerViewScoreAdapter adapter = new RecyclerViewScoreAdapter(getContext(), scoreList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }
}
