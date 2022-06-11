package com.potentnetwork.win;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.potentnetwork.win.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

public class MainActivity extends AppCompatActivity {
    View parentLayout;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentLayout = findViewById(android.R.id.content);

//        CustomProgressDialog dialog = new CustomProgressDialog(MainActivity.this);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);



     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {

             Intent intent = new Intent(MainActivity.this,MainMenu.class);
             startActivity(intent);
             finish();

         }
     },3000);

    }

}