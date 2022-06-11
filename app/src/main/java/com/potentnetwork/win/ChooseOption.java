package com.potentnetwork.win;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.potentnetwork.win.games.Draw;
import com.potentnetwork.win.games.Tennis;
import com.potentnetwork.win.games.Win;
import com.potentnetwork.win.updateOutcome.BookingCodeActivity;

public class ChooseOption extends AppCompatActivity {
    private ImageView winBtn,winOrDrawBtn,bookingCode,tennis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);
        winBtn = findViewById(R.id.winBtn);
        winOrDrawBtn = findViewById(R.id.winOrDrawBtn);
        bookingCode = findViewById(R.id.bookingCode);
        tennis = findViewById(R.id.tennis);


        winOrDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToAdmin1();
            }
        });

        winBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToAdmin2();
            }
        });

        bookingCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAdmin3();
            }
        });

        tennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              sendToAdmin4();
            }
        });
    }

    private void sendToAdmin4() {
        Intent sendToAdmin = new Intent(ChooseOption.this, Tennis.class);
        startActivity(sendToAdmin);
    }

    private void sendToAdmin3() {
        Intent sendToAdmin = new Intent(ChooseOption.this, BookingCodeActivity.class);
        startActivity(sendToAdmin);
//        Toast.makeText(ChooseOption.this, "Coming soon...", Toast.LENGTH_LONG).show();
    }

    private void sendToAdmin2() {
        Intent sendToAdmins = new Intent(ChooseOption.this, Win.class);
        startActivity(sendToAdmins);
    }


    private void sendToAdmin1() {
        Intent sendToAdmin = new Intent(ChooseOption.this, Draw.class);
        startActivity(sendToAdmin);
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code here
            Intent intent = new Intent(ChooseOption.this,MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}