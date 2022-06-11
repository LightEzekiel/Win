package com.potentnetwork.win.updateOutcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.potentnetwork.win.CustomProgressDialog;
import com.potentnetwork.win.MainMenu;
import com.potentnetwork.win.R;

import java.util.HashMap;

public class TennisUpdateScore extends AppCompatActivity {
    TextView clubText,predictionText;
    EditText updateScore;
    Button wonBtn,lostBtn,cancldBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    DatabaseReference databaseReference;
    Task task1;
    Task task2;
    String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tennis_update_score);

        updateScore = findViewById(R.id.tupdateScore);
        wonBtn = findViewById(R.id.twonBtn);
        lostBtn = findViewById(R.id.tlostBtn);
        cancldBtn = findViewById(R.id.tcancldBtn);
//        updateProgressBar = findViewById(R.id.xupdateProgressBar);
        clubText = findViewById(R.id.tclubText);
        predictionText = findViewById(R.id.tpredictionText);

        CustomProgressDialog customProgressDialog = new CustomProgressDialog(TennisUpdateScore.this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference();
        databaseReference = firebaseDatabase.getInstance().getReference("Predictions").push();
        String gameKey = databaseReference.getKey();
        Intent intent = getIntent();
        String countryTv = intent.getStringExtra("Country");
        String clubTv = intent.getStringExtra("Club");
        String predictionTv = intent.getStringExtra("Prediction");
        String gameKeyTv = intent.getStringExtra("Gamekey");
        String OddTv = intent.getStringExtra("Odd");
        String TimestampTv = intent.getStringExtra("Timestamp");
        String gameDayTv = intent.getStringExtra("Gameday");

        clubText.setText(clubTv);
        predictionText.setText(predictionTv);

        wonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = updateScore.getText().toString().trim();
                if (!TextUtils.isEmpty(score)) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(TennisUpdateScore.this);
                    builder.setTitle("Update Outcome");
                    builder.setMessage("Are you sure that "+ score + " is the correct outcome of "+clubTv);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wonBtn.setEnabled(false);
                            customProgressDialog.show();
                            task1 = dbRef.child("Tennis").child(gameKeyTv).child("score").setValue(score);
                            task1.addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    String gameState = "WON";
                                    task2 = dbRef.child("Tennis").child(gameKeyTv).child("gamestate").setValue(gameState);
                                    task2.addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("club",clubTv);
                                            hashMap.put("prediction",predictionTv);
                                            hashMap.put("odd",OddTv);
                                            hashMap.put("timestamp",TimestampTv);
                                            hashMap.put("country",countryTv);
                                            hashMap.put("score",score);
                                            hashMap.put("daystamp", ServerValue.TIMESTAMP);
                                            hashMap.put("gamestate",gameState);
                                            hashMap.put("gamekey",gameKey);
                                            hashMap.put("gameday",gameDayTv);
                                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        customProgressDialog.dismiss();
                                                        Intent outComeIntent = new Intent(TennisUpdateScore.this, MainMenu.class);
                                                        startActivity(outComeIntent);
                                                        finish();
                                                    }
                                                }
                                            });

                                        }
                                    });
                                }
                            });


                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }else {
                    Toast.makeText(TennisUpdateScore.this, "Update Match final Outcome", Toast.LENGTH_LONG).show();
                }
            }
        });

        lostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = updateScore.getText().toString().trim();
                if (!TextUtils.isEmpty(score)) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(TennisUpdateScore.this);
                    builder.setTitle("Update Outcome");
                    builder.setMessage("Are you sure that "+ score + " is the correct outcome of "+clubTv);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wonBtn.setEnabled(false);
                            customProgressDialog.show();
                            task1 = dbRef.child("Tennis").child(gameKeyTv).child("score").setValue(score);
                            task1.addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    String gameState = "LOST";
                                    task2 = dbRef.child("Tennis").child(gameKeyTv).child("gamestate").setValue(gameState);
                                    task2.addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("club",clubTv);
                                            hashMap.put("prediction",predictionTv);
                                            hashMap.put("odd",OddTv);
                                            hashMap.put("timestamp",TimestampTv);
                                            hashMap.put("country",countryTv);
                                            hashMap.put("score",score);
                                            hashMap.put("daystamp", ServerValue.TIMESTAMP);
                                            hashMap.put("gamestate",gameState);
                                            hashMap.put("gamekey",gameKey);
                                            hashMap.put("gameday",gameDayTv);
                                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        customProgressDialog.dismiss();
                                                        Intent outComeIntent = new Intent(TennisUpdateScore.this, MainMenu.class);
                                                        startActivity(outComeIntent);
                                                        finish();
                                                    }
                                                }
                                            });

                                        }
                                    });
                                }
                            });


                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }else {
                    Toast.makeText(TennisUpdateScore.this, "Update Match final Outcome", Toast.LENGTH_LONG).show();
                }
            }
        });
        cancldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = updateScore.getText().toString().trim();
                if (TextUtils.isEmpty(score)) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(TennisUpdateScore.this);
                    builder.setTitle("Update Outcome");
                    builder.setMessage("Are you sure that "+ score + " is the correct outcome of "+clubTv);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wonBtn.setEnabled(false);
                            customProgressDialog.show();
                            task1 = dbRef.child("Tennis").child(gameKeyTv).child("score").setValue(score);
                            task1.addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    String gameState = "CANCLD";
                                    task2 = dbRef.child("Tennis").child(gameKeyTv).child("gamestate").setValue(gameState);
                                    task2.addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("club",clubTv);
                                            hashMap.put("prediction",predictionTv);
                                            hashMap.put("odd",OddTv);
                                            hashMap.put("timestamp",TimestampTv);
                                            hashMap.put("country",countryTv);
                                            hashMap.put("score",score);
                                            hashMap.put("daystamp", ServerValue.TIMESTAMP);
                                            hashMap.put("gamestate",gameState);
                                            hashMap.put("gamekey",gameKey);
                                            hashMap.put("gameday",gameDayTv);
                                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        customProgressDialog.dismiss();
                                                        Intent outComeIntent = new Intent(TennisUpdateScore.this, MainMenu.class);
                                                        startActivity(outComeIntent);
                                                        finish();
                                                    }
                                                }
                                            });

                                        }
                                    });
                                }
                            });


                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }else {
                    Toast.makeText(TennisUpdateScore.this, "Match Outcome Must be Empty", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}