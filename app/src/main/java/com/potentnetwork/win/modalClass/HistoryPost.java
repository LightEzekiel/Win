package com.potentnetwork.win.modalClass;

import androidx.annotation.Keep;

import java.util.Date;
import java.util.Map;
@Keep
public class HistoryPost {
    private String country,club,prediction,odd,gametime,gameday,timestamp,score,gamestate,gamekey;
    private long daystamp;

    public HistoryPost(){

    }

    public HistoryPost(String country, String club, String prediction, String odd, String gametime, String gameday, String timestamp,String score,String gamestate,String gamekey,Long daystamp) {
        this.country = country;
        this.club = club;
        this.prediction = prediction;
        this.odd = odd;
        this.gametime = gametime;
        this.gameday = gameday;
        this.score = score;
        this.gamekey = gamekey;
        this.gamestate = gamestate;
        this.daystamp = daystamp;
        this.timestamp = timestamp;
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

    public String getGametime() {
        return gametime;
    }

    public void setGametime(String gametime) {
        this.gametime = gametime;
    }

    public String getGameday() {
        return gameday;
    }

    public void setGameday(String gameday) {
        this.gameday = gameday;
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

    public long getDaystamp() {
        return daystamp;
    }

    public void setDaystamp(long daystamp) {
        this.daystamp = daystamp;
    }
}
