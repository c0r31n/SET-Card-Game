package com.example.setcardgame.Model.scoreboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.setcardgame.R;

import java.util.List;

public class RecyclerViewScoreAdapter extends RecyclerView.Adapter<RecyclerViewScoreAdapter.ViewHolder> {

    Context context;
    List<Scoreboard> list;

    public RecyclerViewScoreAdapter(Context context, List<Scoreboard> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewScoreAdapter.ViewHolder holder, int position) {
        String placementContent = list.get(position).getPlacement() + ".";
        String pointsContent = String.format("%s: %d", context.getString(R.string.pointsText),list.get(position).getScore());

        int time = list.get(position).getTime();
        int seconds = time % 60;
        int minutes = time / 60;
        String timeContent = String.format("%s: %d:%02d", context.getString(R.string.timeText) , minutes, seconds);

        String myScoreContent = "";
        if(list.get(position).isMyScore()){
            myScoreContent = String.format("(%s)", context.getString(R.string.ownText));
        }

        holder.setData(placementContent, pointsContent, timeContent, myScoreContent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView placement;
        private TextView points;
        private TextView time;
        private TextView myScoreText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            placement = itemView.findViewById(R.id.placement);
            points = itemView.findViewById(R.id.pointsView);
            time = itemView.findViewById(R.id.timeView);
            myScoreText = itemView.findViewById(R.id.myScoreView);
        }

        public void setData(String placementContent, String pointsContent, String timeContent, String myScoreContent) {
            placement.setText(placementContent);
            points.setText(pointsContent);
            time.setText(timeContent);
            myScoreText.setText(myScoreContent);
        }
    }
}
