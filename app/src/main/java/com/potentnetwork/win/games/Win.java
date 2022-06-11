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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Win extends AppCompatActivity {
    private TextInputLayout country,club,prediction,odd,kickOff,day;
    private Button summitButton;
    String Wcountry,Wclub,Wprediction,Wodd,WkickOff,Wday;
    DatabaseReference dbRef;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);


        country = findViewById(R.id.wcountry);
        club = findViewById(R.id.wclub);
        prediction = findViewById(R.id.wprediction);
        odd = findViewById(R.id.wodd);
        kickOff = findViewById(R.id.wgameTime);



        summitButton = findViewById(R.id.summitBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("Prediction1").push();
        String gameKey = dbRef.getKey();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        summitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Wcountry = country.getEditText().getText().toString().trim();
                Wclub = club.getEditText().getText().toString().trim();
                Wprediction = prediction.getEditText().getText().toString().trim();
                Wodd = odd.getEditText().getText().toString().trim();
                WkickOff = kickOff.getEditText().getText().toString().trim();
                Wday = "FOOTBALL";

                Date date = new Date();
                Date newDate = new Date(date.getTime());
                SimpleDateFormat dt = new SimpleDateFormat("EEE,dd MMM, yy");
                String stringdate = dt.format(newDate);

                    if (isValid()) {
                        ProgressDialog mProgressDiaglog = new ProgressDialog(Win.this);
                        mProgressDiaglog.setCancelable(false);
                        mProgressDiaglog.setCanceledOnTouchOutside(false);
                        mProgressDiaglog.setMessage("Updating your prediction...");
                        mProgressDiaglog.show();
                        final HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("country", Wcountry);
                        hashMap.put("club", Wclub);
                        hashMap.put("prediction", Wprediction);
                        hashMap.put("odd", Wodd);
                        hashMap.put("time", WkickOff);
                        hashMap.put("gameday", Wday);
                        hashMap.put("gamekey", gameKey);
                        hashMap.put("score","");
                        hashMap.put("gamestate","");
                        hashMap.put("daystamp", ServerValue.TIMESTAMP);
                        hashMap.put("timestamp", stringdate);
                        dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mProgressDiaglog.dismiss();
                                    Toast.makeText(Win.this, "Prediction updated", Toast.LENGTH_SHORT).show();
                                    Intent mainIntent = new Intent(Win.this, MainMenu.class);
                                    startActivity(mainIntent);
                                    finish();

//                                    hashMap1.put("prediction", Wprediction);
//                                    hashMap1.put("odd", Wodd);
//                                    hashMap1.put("time", WkickOff);
//                                    hashMap1.put("gameday", Wday);
//                                    hashMap1.put("gamekey", gameKey);
//                                    hashMap1.put("score","");
//                                    hashMap1.put("gamestate","");
//                                    hashMap1.put("timestamp", FieldValue.serverTimestamp());
                                }else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(Win.this, errorMessage, Toast.LENGTH_LONG).show();
                                }
                                mProgressDiaglog.dismiss();

                            }
                        });


                    }

                }

        });

    }

    public boolean isValid(){
        country.setErrorEnabled(false);
        country.setError("");
        club.setErrorEnabled(false);
        club.setError("");
        prediction.setErrorEnabled(false);
        prediction.setError("");
        odd.setErrorEnabled(false);
        odd.setError("");
        kickOff.setErrorEnabled(false);
        kickOff.setError("");
//        day.setErrorEnabled(false);
//        day.setError("");

        boolean isValid = false,isValidcountry = false,isValidclub = false,isValidprediction = false, isValidOdd = false,isValidKickOff = false;
        if (TextUtils.isEmpty(Wcountry)){
            country.setErrorEnabled(true);
            country.setError("Country/League Name is needed");
        }else{
            isValidcountry = true;
        }
        if (TextUtils.isEmpty(Wclub)){
            club.setErrorEnabled(true);
            club.setError("Club cannot be empty");
        }else{
            isValidclub = true;
        }
        if (TextUtils.isEmpty(Wprediction)){
            prediction.setErrorEnabled(true);
            prediction.setError("Prediction is needed");
        }else{
            isValidprediction = true;
        }
        if (TextUtils.isEmpty(Wodd)){
            odd.setErrorEnabled(true);
            odd.setError("Input game odd");
        }else{
            isValidOdd = true;
        }
        if (TextUtils.isEmpty(WkickOff)){
            kickOff.setErrorEnabled(true);
            kickOff.setError("Game kickOff time is needed");
        }else{
            isValidKickOff = true;
        }
//        if (TextUtils.isEmpty(Wday)){
//            day.setErrorEnabled(true);
//            day.setError("GameDay (Today or Tomorrow)");
//        }else{
//            isValidday = true;
//        }
        isValid = (isValidcountry && isValidclub && isValidprediction && isValidOdd && isValidKickOff ) ? true:false;
        return isValid;
    };
}