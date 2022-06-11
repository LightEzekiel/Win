package com.potentnetwork.win.modalClass;

import androidx.annotation.Keep;

@Keep
public class TennisPost {
    private String country,club,prediction,odd,time,gameday,gamekey,timestamp,score,gamestate;
    private long daystamp;

    public TennisPost(String country, String club, String prediction, String odd, String time, String gameday, String gamekey, String timestamp, String score, String gamestate, long daystamp) {
        this.country = country;
        this.club = club;
        this.prediction = prediction;
        this.odd = odd;
        this.time = time;
        this.gameday = gameday;
        this.gamekey = gamekey;
        this.timestamp = timestamp;
        this.score = score;
        this.gamestate = gamestate;
        this.daystamp = daystamp;
    }

    public TennisPost(){

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGameday() {
        return gameday;
    }

    public void setGameday(String gameday) {
        this.gameday = gameday;
    }

    public String getGamekey() {
        return gamekey;
    }

    public void setGamekey(String gamekey) {
        this.gamekey = gamekey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getGamestate() {
        return gamestate;
    }

    public void setGamestate(String gamestate) {
        this.gamestate = gamestate;
    }

    public long getDaystamp() {
        return daystamp;
    }

    public void setDaystamp(long daystamp) {
        this.daystamp = daystamp;
    }
}
