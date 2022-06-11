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
import com.potentnetwork.win.modalClass.WinPost;

import java.util.List;

public class WinRecyclerAdapter extends RecyclerView.Adapter<WinRecyclerAdapter.ViewHolder> {

    public List<WinPost> winList;
    public Context context;


    public WinRecyclerAdapter(List<WinPost> winList){
        this.winList = winList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homewin,parent,false);
       context = parent.getContext();
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(true);



        String countryView = winList.get(position).getCountry();
        holder.setCountryTextView(countryView);

        String clubView = winList.get(position).getClub();
        holder.setClubTextView(clubView);



        String kickoffView = winList.get(position).getTime();
        holder.setKickoffTextView(kickoffView);

        String predictionView = winList.get(position).getPrediction();
        holder.setPredictionTextView(predictionView);


        String dateView = winList.get(position).getTimestamp();
//        holder.setDateTextView(dateView);
//        long time = winList.get(position).getTimestamp().getTime();
//        String dateString = DateFormat.format("EEE, dd MMM, yy",new Date(time)).toString();
        holder.setDateTextView(dateView);

//        String day = winList.get(position).getGameday();
//        holder.setDayTextView(day);

        String gameKeyId = winList.get(position).getGamekey();
        holder.setGameKeyTextVw(gameKeyId);

       String gameScore = winList.get(position).getScore();
       holder.setGameScoreTextView(gameScore);

       //NotificationHelper.displayNotification2(context,countryView,"KickOff: "+kickoffView);


        String gameState = winList.get(position).getGamestate();
       if (gameState.equals("WON")){
           holder.setGameStateTextView(gameState);
           holder.gameStateTextView.setTextColor(Color.BLUE);
       }else if (gameState.equals("LOST")){
           holder.setGameStateTextView(gameState);
           holder.gameStateTextView.setTextColor(Color.RED);
       }else{
           holder.setGameStateTextView(gameState);
           holder.gameStateTextView.setTextColor(Color.GRAY);
        }



       String gameOdd = winList.get(position).getOdd();
       holder.setGameOddTextView(gameOdd);
    }


    @Override
    public int getItemCount() {
        return winList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView countryTextView;
        private TextView clubTextview;
        private TextView kickoffTextView;
        private TextView predictionTextView;
        private TextView dateTextView;
        private TextView dayTextVw;
        private TextView gameKeyTextVw;
        private TextView gameScoreTextView;
        private TextView gameStateTextView;
        private TextView gameOddTextView;



        private View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;




//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    recyclerViewClickInterface.OnlongItemClick(getAdapterPosition());
//                    return false;
//                }
//            });

        }

        public void setCountryTextView(String countryText){
            countryTextView = mView.findViewById(R.id.wlegueTextView1);
            countryTextView.setText(countryText);
        }

        public void setClubTextView(String clubText){
            clubTextview = mView.findViewById(R.id.wclubTextView1);
            clubTextview.setText(clubText);
        }

        public void setKickoffTextView(String KickOff){
            kickoffTextView = mView.findViewById(R.id.wkickoff1);
            kickoffTextView.setText(KickOff);
        }

        public void setPredictionTextView(String prediction){
            predictionTextView = mView.findViewById(R.id.homewin1);
            predictionTextView.setText(prediction);
        }

        public void setDateTextView(String date){
            dateTextView = mView.findViewById(R.id.hdisplayDateTextView3);
            dateTextView.setText(date);
        }

//        public void setDayTextView(String day){
//            dayTextVw = mView.findViewById(R.id.dayextviw1);
//            dayTextVw.setText(day);
//        }

        public  void setGameKeyTextVw(String key){
            gameKeyTextVw = mView.findViewById(R.id.gameKey);
            gameKeyTextVw.setText(key);
        }

        public void setGameScoreTextView(String score){
            gameScoreTextView = mView.findViewById(R.id.wscoreTextView1);
            gameScoreTextView.setText(score);
        }

        public void setGameStateTextView(String state){
            gameStateTextView = mView.findViewById(R.id.wgamestateTextView1);
            gameStateTextView.setText(state);
        }

        public void setGameOddTextView(String odd){
            gameOddTextView = mView.findViewById(R.id.woddTxtVw1);
            gameOddTextView.setText(odd);
        }

    }


}
