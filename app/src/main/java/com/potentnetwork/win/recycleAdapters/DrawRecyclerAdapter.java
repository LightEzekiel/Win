package com.potentnetwork.win.recycleAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potentnetwork.win.R;
import com.potentnetwork.win.modalClass.DrawPost;

import java.util.List;

public class DrawRecyclerAdapter extends RecyclerView.Adapter<DrawRecyclerAdapter.ViewHolder> {
    Context context;
    public List<DrawPost> drawPosts;
    public DrawRecyclerAdapter(List<DrawPost> drawPosts){
        this.drawPosts = drawPosts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.winordraw,parent,false);
        context = parent.getContext();




        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        String countryVw = drawPosts.get(position).getCountry();
        holder.setCountrytxt(countryVw);

        String clubVw = drawPosts.get(position).getClub();
        holder.setClubtxt(clubVw);
        String oddVw = drawPosts.get(position).getOdd();
        holder.setOddText(oddVw);

        String dateView = drawPosts.get(position).getTimestamp();
//        long dayVw = drawPosts.get(position).getTimestamp().getTime();
//        String dateString = DateFormat.format("EEE,dd MMM, yy",new Date(dayVw)).toString();
        holder.setTimeTxt(dateView);
//        String gameVw = drawPosts.get(position).getGameday();
//        holder.setGameDay(gameVw);
        String kickVw = drawPosts.get(position).getGametime();
        holder.setKickoffText(kickVw);
        String predictVw = drawPosts.get(position).getPrediction();
        holder.setPredictionTxt(predictVw);
        String outcomeView = drawPosts.get(position).getScore();
        holder.setOutcomeText(outcomeView);

        //NotificationHelper.displayNotification2(context,gameVw,"KickOff: "+kickVw);

        String gamestateView = drawPosts.get(position).getGamestate();
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
        return drawPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;

        private TextView countrytxt;
        private TextView clubText;
        private TextView oddText;
        private TextView timeTxt;
        private TextView gameDay;
        private TextView kickoffText;
        private TextView predictionTxt;
        private TextView outcomeText;
        private TextView gamestateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCountrytxt(String cntxt){
            countrytxt = mView.findViewById(R.id.wlegueTextView1);
            countrytxt.setText(cntxt);
        }
        public void setClubtxt(String clubtxt){
            clubText = mView.findViewById(R.id.wclubTextView1);
            clubText.setText(clubtxt);
        }
        public void setOddText(String odd){
            oddText = mView.findViewById(R.id.woddTxtVw1);
            oddText.setText(odd);
        }
        public void setTimeTxt(String date){
            timeTxt = mView.findViewById(R.id.hdisplayDateTextView3);
            timeTxt.setText(date);
        }
//        public void setGameDay(String day){
//            gameDay = mView.findViewById(R.id.dayextviw1);
//            gameDay.setText(day);
//        }
        public void setKickoffText(String kickoff){
            kickoffText = mView.findViewById(R.id.wkickoff1);
            kickoffText.setText(kickoff);
        }
        public void setPredictionTxt(String predict){
            predictionTxt = mView.findViewById(R.id.wPredictionTxtVw1);
            predictionTxt.setText(predict);
        }

        public void setOutcomeText(String score){
            outcomeText = mView.findViewById(R.id.wscoreTextView1);
            outcomeText.setText(score);
        }
        public void setGamestateText(String state){
            gamestateText = mView.findViewById(R.id.wgamestateTextView1);
            gamestateText.setText(state);
        }
    }
}
