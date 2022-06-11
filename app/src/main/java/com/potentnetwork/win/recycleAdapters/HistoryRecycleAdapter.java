package com.potentnetwork.win.recycleAdapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potentnetwork.win.R;
import com.potentnetwork.win.modalClass.HistoryPost;

import java.util.List;

public class HistoryRecycleAdapter extends RecyclerView.Adapter<HistoryRecycleAdapter.ViewHolder> {
    public List<HistoryPost> historyPosts;
    public HistoryRecycleAdapter(List<HistoryPost> historyPosts){
        this.historyPosts = historyPosts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_post,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        String countryView = historyPosts.get(position).getCountry();
        holder.setCountrytxt(countryView);

        String dateView = historyPosts.get(position).getTimestamp();
        holder.setDateTextView(dateView);
        String clubView = historyPosts.get(position).getClub();
        holder.setClubTextView(clubView);
        String predictionView = historyPosts.get(position).getPrediction();
        holder.setPredictionTextView(predictionView);
        String OddView = historyPosts.get(position).getOdd();
        holder.setOddTextView(OddView);
        String gamedayView = historyPosts.get(position).getGameday();
        holder.setGamedayTextView(gamedayView);

        String gamestateView = historyPosts.get(position).getGamestate();
        if (gamestateView.equals("WON")){
            holder.setGameStateTextView(gamestateView);
            holder.gameStateTextView.setTextColor(Color.BLUE);
        }else if (gamestateView.equals("LOST")){
            holder.setGameStateTextView(gamestateView);
            holder.gameStateTextView.setTextColor(Color.RED);
        }else {
            holder.setGameStateTextView(gamestateView);
            holder.gameStateTextView.setTextColor(Color.GRAY);
        }
        String outcomeView = historyPosts.get(position).getScore();
        holder.setOutcomeTextView(outcomeView);


    }

    @Override
    public int getItemCount() {
        return historyPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private TextView countryTextView;
        private TextView dateTextView;
        private TextView clubTextView;
        private TextView predictionTextView;
        private TextView oddTextView;
        private TextView outcomeTextView;
        private  TextView gameStateTextView;
        private TextView gamedayTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCountrytxt(String countrytxt){
            countryTextView = mView.findViewById(R.id.hlegueTextView3);
            countryTextView.setText(countrytxt);
        }
        public void setDateTextView(String date){
            dateTextView = mView.findViewById(R.id.hdisplayDateTextView3);
            dateTextView.setText(date);
        }

        public void setClubTextView(String club){
            clubTextView = mView.findViewById(R.id.hclubTextView3);
            clubTextView.setText(club);
        }
        public void setPredictionTextView(String prediction){
            predictionTextView = mView.findViewById(R.id.hPredictionTxtVw31);
            predictionTextView.setText(prediction);
        }

        public void setOddTextView(String odd){
            oddTextView = mView.findViewById(R.id.hoddTxtVw3);
            oddTextView.setText(odd);

        }
        public void setOutcomeTextView(String outcome){
            outcomeTextView = mView.findViewById(R.id.houtcomeTextVw);
            outcomeTextView.setText(outcome);
        }
        public void setGameStateTextView(String gameState){
            gameStateTextView = mView.findViewById(R.id.hgamestateTextVw);
            gameStateTextView.setText(gameState);
        }
        public void setGamedayTextView(String gameday){
            gamedayTextView = mView.findViewById(R.id.wgameday3);
            gamedayTextView.setText(gameday);
        }
    }

}
