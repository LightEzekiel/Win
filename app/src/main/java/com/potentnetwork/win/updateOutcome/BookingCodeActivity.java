package com.potentnetwork.win.updateOutcome;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class BookingCodeActivity extends AppCompatActivity {
    private TextInputLayout Bet9ja,BetKing,Text_ChanceMixCode;
    private Button BtnBet9ja,BtnBetKing,BtnChanceMix;
    private String bet9ja,betKing,text_chancemix;
    DatabaseReference dbReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_code);
        Bet9ja = findViewById(R.id.Text_Bet9jaCode);
        BetKing = findViewById(R.id.Text_BetKingCode);

        Text_ChanceMixCode = findViewById(R.id.Text_ChanceMixCode);
        BtnBet9ja = findViewById(R.id.BtnBet9ja);
        BtnBetKing = findViewById(R.id.BtnBetKing);

        BtnChanceMix = findViewById(R.id.BtnChanceMix);
        dbReference = FirebaseDatabase.getInstance().getReference().child("Booking Codes");



        BtnBet9ja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bet9ja = Bet9ja.getEditText().getText().toString().trim();
//                Date date = new Date();
//                Date newDate = new Date(date.getTime());
//                SimpleDateFormat dt = new SimpleDateFormat("EEE,dd MMM, yy");
//                String stringdate = dt.format(newDate);

                if (isValid1()){
                    ProgressDialog progressDialog = new ProgressDialog(BookingCodeActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Updating Football Booking Code...");
                    progressDialog.show();

                    final HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("Bet9jaCode",bet9ja);
                    hashMap.put("BookingState","");

                    dbReference.child("Bet9ja").setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(BookingCodeActivity.this, "Football code uploaded successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent drawIntent = new Intent(BookingCodeActivity.this, MainMenu.class);
                                startActivity(drawIntent);
                                finish();

                            }
                        }
                    });

                }

            }
        });

        BtnBetKing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                betKing = BetKing.getEditText().getText().toString().trim();
//                Date date = new Date();
//                Date newDate = new Date(date.getTime());
//                SimpleDateFormat dt = new SimpleDateFormat("EEE,dd MMM, yy");
//                String stringdate = dt.format(newDate);
                if (isValid2()){
                    ProgressDialog progressDialog = new ProgressDialog(BookingCodeActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Updating Basketball Booking Code..");
                    progressDialog.show();

                    final HashMap<String,Object> hashMap2 = new HashMap<>();
                    hashMap2.put("BetKingCode",betKing);
                    hashMap2.put("BookingState","");

                    dbReference.child("BetKing").setValue(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(BookingCodeActivity.this, "Basketball code uploaded successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent drawIntent = new Intent(BookingCodeActivity.this, MainMenu.class);
                                startActivity(drawIntent);
                                finish();

                            }
                        }
                    });
                }

            }
        });


        BtnChanceMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_chancemix = Text_ChanceMixCode.getEditText().getText().toString().trim();
//                Date date = new Date();
//                Date newDate = new Date(date.getTime());
//                SimpleDateFormat dt = new SimpleDateFormat("EEE,dd MMM, yy");
//                String stringdate = dt.format(newDate);
                if (isValid4()){
                    ProgressDialog progressDialog = new ProgressDialog(BookingCodeActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Updating Mix Booking Code..");
                    progressDialog.show();

                    final HashMap<String,Object> hashMap4 = new HashMap<>();
                    hashMap4.put("ChanceMixCode",text_chancemix);
                    hashMap4.put("BookingState","");

                    dbReference.child("ChanceMix").setValue(hashMap4).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(BookingCodeActivity.this, "ChanceMix code uploaded successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent drawIntent = new Intent(BookingCodeActivity.this, MainMenu.class);
                                startActivity(drawIntent);
                                finish();

                            }
                        }
                    });
                }

            }
        });





    }

    public boolean isValid1(){
        Bet9ja.setErrorEnabled(false);
        Bet9ja.setError("");


        boolean isvalid = false,isValidBet9ja = false;

        if (TextUtils.isEmpty(bet9ja)){
            Bet9ja.setErrorEnabled(true);
            Bet9ja.setError("Field Can't Be Empty");
        }else {
            isValidBet9ja = true;
        }

        isvalid = isValidBet9ja;
        return isvalid;
    }
    public boolean isValid2(){
        BetKing.setErrorEnabled(false);
        BetKing.setError("");


        boolean isvalid2 = false,isValidBetKing = false;
        if (TextUtils.isEmpty(betKing)){
            BetKing.setErrorEnabled(true);
            BetKing.setError("Field Can't Be Empty");
        }else {
            isValidBetKing = true;
        }
        isvalid2 =  isValidBetKing;
        return isvalid2;

    }


    public boolean isValid4(){
        Text_ChanceMixCode.setErrorEnabled(false);
        Text_ChanceMixCode.setError("");


        boolean isvalid4 = false,isValidChanceMix = false;
        if (TextUtils.isEmpty(text_chancemix)){
            Text_ChanceMixCode.setErrorEnabled(true);
            Text_ChanceMixCode.setError("Field Can't Be Empty");
        }else {
            isValidChanceMix = true;
        }
        isvalid4 =  isValidChanceMix;
        return isvalid4;

    }

}