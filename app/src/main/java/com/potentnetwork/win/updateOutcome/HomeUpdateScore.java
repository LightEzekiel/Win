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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.potentnetwork.win.CustomProgressDialog;
import com.potentnetwork.win.MainMenu;
import com.potentnetwork.win.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class HomeUpdateScore extends AppCompatActivity  {
    TextView clubText,predictionText;
    EditText updateScore;
    Button wonBtn,lostBtn,cancldBtn;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    ProgressBar updateProgressBar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    DatabaseReference databaseReference;
    Task task1;
    Task task2;
    String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_update_score);

        clubText = findViewById(R.id.clubText);
        predictionText = findViewById(R.id.predictionText);
        wonBtn = findViewById(R.id.wonBtn);
        lostBtn = findViewById(R.id.lostBtn);
        cancldBtn = findViewById(R.id.cancldBtn);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
         firebaseDatabase= FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Predictions").push();
        String gameKey = databaseReference.getKey();


        updateScore = findViewById(R.id.updateScore);
//        updateProgressBar = findViewById(R.id.updateProgressBar);

        CustomProgressDialog customProgressDialog = new CustomProgressDialog(HomeUpdateScore.this);


        Intent intent = getIntent();
        String countryTv = intent.getStringExtra("Country");
        String clubTv = intent.getStringExtra("Club");
        String predictionTv = intent.getStringExtra("Prediction");
        String gameKeyTv = intent.getStringExtra("Gamekey");
        String OddTv = intent.getStringExtra("Odd");
        String TimestampTv = intent.getStringExtra("Timestamp");
        String gamedayTv = intent.getStringExtra("Gameday");



            clubText.setText(clubTv);
            predictionText.setText(predictionTv);

        wonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = updateScore.getText().toString().trim();
                if (!TextUtils.isEmpty(score)) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HomeUpdateScore.this);
                    builder.setTitle("Update Outcome");
                    builder.setMessage("Are you sure that "+ score + " is the correct outcome of "+clubTv);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            customProgressDialog.show();
                            wonBtn.setEnabled(false);
                             task1 = dbRef.child("Prediction1").child(gameKeyTv).child("score").setValue(score);
                            task1.addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    String gameState = "WON";
                                    task2 = dbRef.child("Prediction1").child(gameKeyTv).child("gamestate").setValue(gameState);
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
                                            hashMap.put("gamekey",gameKey);
                                            hashMap.put("daystamp", ServerValue.TIMESTAMP);
                                            hashMap.put("gamestate",gameState);
                                            hashMap.put("gameday",gamedayTv);
                                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful()){
                                                  customProgressDialog.dismiss();
                                                   Intent outComeIntent = new Intent(HomeUpdateScore.this, MainMenu.class);
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
                    Toast.makeText(HomeUpdateScore.this, "Update Match final Outcome", Toast.LENGTH_LONG).show();
                }
            }
        });

        lostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                score = updateScore.getText().toString().trim();
                if (!TextUtils.isEmpty(score)) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HomeUpdateScore.this);
                    builder.setTitle("Update Outcome");
                    builder.setMessage("Are you sure that "+ score + " is the correct outcome of "+clubTv);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           customProgressDialog.show();
                            lostBtn.setEnabled(false);
                            task1 = dbRef.child("Prediction1").child(gameKeyTv).child("score").setValue(score);
                            task1.addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    String gameState = "LOST";
                                    task2 = dbRef.child("Prediction1").child(gameKeyTv).child("gamestate").setValue(gameState);
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
                                            hashMap.put("gamekey",gameKey);
                                            hashMap.put("gamestate",gameState);
                                            hashMap.put("gameday",gamedayTv);
                                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        customProgressDialog.dismiss();
                                                        Intent outComeIntent = new Intent(HomeUpdateScore.this, MainMenu.class);
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
                    Toast.makeText(HomeUpdateScore.this, "Update Match final Outcome", Toast.LENGTH_LONG).show();
                }


            }
        });


        cancldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                score = "";
                if (TextUtils.isEmpty(score)) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HomeUpdateScore.this);
                    builder.setTitle("Update Outcome");
                    builder.setMessage("Are you sure that "+clubTv+" was CANCELLED");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            customProgressDialog.show();
                            lostBtn.setEnabled(false);
                            task1 = dbRef.child("Prediction1").child(gameKeyTv).child("score").setValue(score);
                            task1.addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    String gameState = "CANCLD";
                                    task2 = dbRef.child("Prediction1").child(gameKeyTv).child("gamestate").setValue(gameState);
                                    task2.addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("club",clubTv);
                                            hashMap.put("prediction",predictionTv);
                                            hashMap.put("odd",OddTv);
                                            hashMap.put("timestamp",TimestampTv);
                                            hashMap.put("daystamp", ServerValue.TIMESTAMP);
                                            hashMap.put("country",countryTv);
                                            hashMap.put("score",score);
                                            hashMap.put("gamekey",gameKey);
                                            hashMap.put("gamestate",gameState);
                                            hashMap.put("gameday",gamedayTv);
                                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        customProgressDialog.dismiss();
                                                        Intent outComeIntent = new Intent(HomeUpdateScore.this, MainMenu.class);
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
                    Toast.makeText(HomeUpdateScore.this, "Match Outcome Must be Empty", Toast.LENGTH_LONG).show();
                }


            }
        });
    }



}