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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.potentnetwork.win.MainMenu;
import com.potentnetwork.win.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Tennis extends AppCompatActivity {
    private TextInputLayout country,club,prediction,odd,kickOff,day;
    private Button summitButton;
    String Tcountry,Tclub,Tprediction,Todd,TkickOff,Tday;
    DatabaseReference dbRef;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tennis);

        country = findViewById(R.id.tcountry);
        club = findViewById(R.id.tclub);
        prediction = findViewById(R.id.tprediction);
        odd = findViewById(R.id.todd);
        kickOff = findViewById(R.id.tgameTime);

        summitButton = findViewById(R.id.tsummitBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("Tennis").push();
        String gameKey = dbRef.getKey();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        summitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tcountry = country.getEditText().getText().toString().trim();
                Tclub = club.getEditText().getText().toString().trim();
                Tprediction = prediction.getEditText().getText().toString().trim();
                Todd = odd.getEditText().getText().toString().trim();
                TkickOff = kickOff.getEditText().getText().toString().trim();
                Tday = "TENNIS";

                Date date = new Date();
                Date newDate = new Date(date.getTime());
                SimpleDateFormat dt = new SimpleDateFormat("EEE,dd MMM, yy");
                String stringdate = dt.format(newDate);

                if (isValid()) {
                    ProgressDialog mProgressDiaglog = new ProgressDialog(Tennis.this);
                    mProgressDiaglog.setCancelable(false);
                    mProgressDiaglog.setCanceledOnTouchOutside(false);
                    mProgressDiaglog.setMessage("Updating your prediction...");
                    mProgressDiaglog.show();
                    final HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("country", Tcountry);
                    hashMap.put("club", Tclub);
                    hashMap.put("prediction", Tprediction);
                    hashMap.put("odd", Todd);
                    hashMap.put("time", TkickOff);
                    hashMap.put("gameday", Tday);
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
                                Toast.makeText(Tennis.this, "Prediction updated", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(Tennis.this, MainMenu.class);
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
                                Toast.makeText(Tennis.this, errorMessage, Toast.LENGTH_LONG).show();
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
        if (TextUtils.isEmpty(Tcountry)){
            country.setErrorEnabled(true);
            country.setError("Country/League Name is needed");
        }else{
            isValidcountry = true;
        }
        if (TextUtils.isEmpty(Tclub)){
            club.setErrorEnabled(true);
            club.setError("Club cannot be empty");
        }else{
            isValidclub = true;
        }
        if (TextUtils.isEmpty(Tprediction)){
            prediction.setErrorEnabled(true);
            prediction.setError("Prediction is needed");
        }else{
            isValidprediction = true;
        }
        if (TextUtils.isEmpty(Todd)){
            odd.setErrorEnabled(true);
            odd.setError("Input game odd");
        }else{
            isValidOdd = true;
        }
        if (TextUtils.isEmpty(TkickOff)){
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