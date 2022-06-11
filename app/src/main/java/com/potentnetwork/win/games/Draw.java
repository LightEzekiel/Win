package com.potentnetwork.win.games;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.potentnetwork.win.MainMenu;
import com.potentnetwork.win.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Draw extends AppCompatActivity {
    private TextInputLayout Country, Club, Prediction, Odd, GameTime, GameDay;
    private String country, club, prediction, odd, gametime, gameday;
    private Button summitbtn2;
    DatabaseReference dbRef;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        Country = findViewById(R.id.hcountry);
        Club = findViewById(R.id.hclub);
        Prediction = findViewById(R.id.hprediction);
        Odd = findViewById(R.id.hodd);
        GameTime = findViewById(R.id.hgameTime);

        summitbtn2 = findViewById(R.id.summitButton2);


        dbRef = FirebaseDatabase.getInstance().getReference("Prediction2").push();
        String gameKey = dbRef.getKey();
        firestore = FirebaseFirestore.getInstance();
        summitbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country = Country.getEditText().getText().toString().trim();
                club = Club.getEditText().getText().toString().trim();
                prediction = Prediction.getEditText().getText().toString().trim();
                odd = Odd.getEditText().getText().toString().trim();
                gametime = GameTime.getEditText().getText().toString().trim();
                gameday = "BASKETBALL";

                Date date = new Date();
                Date newDate = new Date(date.getTime());
                SimpleDateFormat dt = new SimpleDateFormat("EEE,dd MMM, yy");
                String stringdate = dt.format(newDate);

                if (isValid()) {
                    ProgressDialog progressDialog = new ProgressDialog(Draw.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Updating your prediction....");
                    progressDialog.show();

                    final HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("country", country);
                    hashMap.put("club", club);
                    hashMap.put("prediction", prediction);
                    hashMap.put("odd", odd);
                    hashMap.put("gametime", gametime);
                    hashMap.put("gameday", gameday);
                    hashMap.put("gamekey", gameKey);
                    hashMap.put("score","");
                    hashMap.put("gamestate","");
                    hashMap.put("daystamp",ServerValue.TIMESTAMP);
                    hashMap.put("timestamp", stringdate);
                    dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent drawIntent = new Intent(Draw.this, MainMenu.class);
                                startActivity(drawIntent);
                                finish();

//
//                                hashMap1.put("prediction", prediction);
//                                hashMap1.put("odd", odd);
//                                hashMap1.put("gametime", gametime);
//                                hashMap1.put("gameday", gameday);
//                                hashMap1.put("gamekey", gameKey);
//                                hashMap1.put("score","");
//                                hashMap1.put("gamestate","");
//                                hashMap1.put("timestamp", FieldValue.serverTimestamp());




                            }else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(Draw.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();

                        }
                    });

                }

            }
        });


    }

    public boolean isValid() {
        Country.setErrorEnabled(false);
        Country.setError("");
        Club.setErrorEnabled(false);
        Club.setError("");
        Prediction.setErrorEnabled(false);
        Prediction.setError("");
        Odd.setErrorEnabled(false);
        Odd.setError("");
        GameTime.setErrorEnabled(false);
        GameTime.setError("");

        boolean isValid = false, isValidCountry = false, isValidClub = false, isValidPrediction = false, isValidOdd = false, isValidGametime = false;

        if (TextUtils.isEmpty(country)) {
            Country.setErrorEnabled(true);
            Country.setError("Country/League is needed");
        } else {
            isValidCountry = true;
        }
        if (TextUtils.isEmpty(club)) {
            Club.setErrorEnabled(true);
            Club.setError("Club cannot be empty");
        } else {
            isValidClub = true;
        }
        if (TextUtils.isEmpty(prediction)) {
            Prediction.setErrorEnabled(true);
            Prediction.setError("Prediction is needed");
        } else {
            isValidPrediction = true;
        }
        if (TextUtils.isEmpty(odd)) {
            Odd.setErrorEnabled(true);
            Odd.setError("Input game odd");
        } else {
            isValidOdd = true;
        }
        if (TextUtils.isEmpty(gametime)) {
            GameTime.setErrorEnabled(true);
            GameTime.setError("GameDay kickOff time is needed");
        } else {
            isValidGametime = true;
        }


        isValid = isValidCountry && isValidClub && isValidPrediction && isValidOdd  && isValidGametime;
        return isValid;
    }


}