package com.potentnetwork.win.recycleAdapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potentnetwork.win.R;
import com.potentnetwork.win.modalClass.TennisPost;

import java.util.List;

public class TennisRecyclerAdapter extends RecyclerView.Adapter<TennisRecyclerAdapter.ViewHolder>{

    public List<TennisPost> tennisPosts;
    public TennisRecyclerAdapter(List<TennisPost> tennisPosts){
        this.tennisPosts = tennisPosts;
    }

    @NonNull
    @Override
    public TennisRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tennis,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TennisRecyclerAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        String countryVw = tennisPosts.get(position).getCountry();
        holder.setCountrytxt(countryVw);

        String clubVw = tennisPosts.get(position).getClub();
        holder.setClubtxt(clubVw);
        String oddVw = tennisPosts.get(position).getOdd();
        holder.setOddText(oddVw);

        String dateView = tennisPosts.get(position).getTimestamp();
//        long dayVw = drawPosts.get(position).getTimestamp().getTime();
//        String dateString = DateFormat.format("EEE,dd MMM, yy",new Date(dayVw)).toString();
        holder.setTimeTxt(dateView);
//        String gameVw = tennisPosts.get(position).getGameday();
//        holder.setGameDay(gameVw);
        String kickVw = tennisPosts.get(position).getTime();
        holder.setKickoffText(kickVw);
        String predictVw = tennisPosts.get(position).getPrediction();
        holder.setPredictionTxt(predictVw);
        String outcomeView = tennisPosts.get(position).getScore();
        holder.setOutcomeText(outcomeView);

        //NotificationHelper.displayNotification2(context,gameVw,"KickOff: "+kickVw);

        String gamestateView = tennisPosts.get(position).getGamestate();
        if (gamestateView.equals("WON")){
            holder.setGamestateText(gamestateView);
            holder.gamestateText.setTextColor(Color.BLUE);
        }else if (gamestateView.equals("LOST")){
            holder.setGamestateText(gamestateView);
            holder.gamestateText.setTextColor(Color.RED);
        }else{
            holder.setGamestateText(gamestateView);
            holder.gamestateText.setTextColor(Color.GRAY);
        }

    }

    @Override
    public int getItemCount() {
        return tennisPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private TextView countrytxt;
        private TextView clubText;
        private TextView oddText;
        private TextView timeTxt;
        private TextView kickoffText;
        private TextView predictionTxt;
        private TextView outcomeText;
        private TextView gamestateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCountrytxt(String cntxt){
            countrytxt = mView.findViewById(R.id.wlegueTennis1);
            countrytxt.setText(cntxt);
        }
        public void setClubtxt(String clubtxt){
            clubText = mView.findViewById(R.id.wclubTennis1);
            clubText.setText(clubtxt);
        }
        public void setOddText(String odd){
            oddText = mView.findViewById(R.id.woddTennis1);
            oddText.setText(odd);
        }
        public void setTimeTxt(String date){
            timeTxt = mView.findViewById(R.id.tennisGameDay1);
            timeTxt.setText(date);
        }
//        public void setGameDay(String day){
//            gameDay = mView.findViewById(R.id.daytennis1);
//            gameDay.setText(day);
//        }
        public void setKickoffText(String kickoff){
            kickoffText = mView.findViewById(R.id.kickoffTennis1);
            kickoffText.setText(kickoff);
        }
        public void setPredictionTxt(String predict){
            predictionTxt = mView.findViewById(R.id.wPredictionTennis1);
            predictionTxt.setText(predict);
        }

        public void setOutcomeText(String score){
            outcomeText = mView.findViewById(R.id.wscoreTennis1);
            outcomeText.setText(score);
        }
        public void setGamestateText(String state){
            gamestateText = mView.findViewById(R.id.wgamestateTennis1);
            gamestateText.setText(state);
        }

    }
}
