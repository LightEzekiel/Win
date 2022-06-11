
package com.potentnetwork.win;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.potentnetwork.win.R;
import com.potentnetwork.win.fragments.HomeFragment;
import com.potentnetwork.win.fragments.SettingsFragment;
import com.potentnetwork.win.fragments.TennisFragment;
import com.potentnetwork.win.fragments.UpdateFragment;
import com.potentnetwork.win.fragments.VipFragment;
import com.potentnetwork.win.updateOutcome.BookingCodeActivity;
import com.potentnetwork.win.utility.NetworkChangeStateListener;

import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String CHANNEL_ID = "Win_bet";
    public static final String CHANNEL_ID_2 = "Win_bet2";
    private static final String CHANNEL_NAME = "Win";
    private static final String CHANNEL_NAME2 = "Win2";
    private static final String CHANNEL_DESC = "Win notification";
    private static final String CHANNEL_DESC2 = "Win notification2";


    private BottomNavigationView mBottomNavigation;
    private HomeFragment homeFragment;
    private UpdateFragment updateFragment;
    private SettingsFragment settingsFragment;
    private TennisFragment tennisFragment;
    private VipFragment vipFragment;

    private Toolbar mainToolbar;
    BubbleNavigationConstraintView bubbleNavigationConstraintView;
    FragmentTransaction fragmentTransaction;
    DatabaseReference dbReference;
    ImageView bet9jaCode;
    ImageView betKingCode;

    ImageView chanceMixCode;
    FirebaseAuth mAuth;
    MenuItem bookingCode;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    NetworkChangeStateListener networkChangeStateListener = new NetworkChangeStateListener();



    InterstitialAd mInterstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-6532546341392825/5871515454", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(getParent());
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        },2600);



//
//          if (mInterstitialAd != null) {
//            mInterstitialAd.show(MainMenu.this);
//        } else {
//            Log.d("TAG", "The interstitial ad wasn't ready yet.");
//        }




//        mBottomNavigation = findViewById(R.id.mainBottomNav);
        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
//        bubbleNavigationConstraintView = findViewById(R.id.bubbleNavigationConstraintView);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        homeFragment = new HomeFragment();
        updateFragment = new UpdateFragment();
        settingsFragment = new SettingsFragment();
        tennisFragment = new TennisFragment();
        vipFragment = new VipFragment();




        if (savedInstanceState == null){
            replaceFragment(homeFragment);
            navigationView.setCheckedItem(R.id.football);
        }


//        bookingCodeReference = dbReference.child("Booking Codes");
        dbReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


//        final Calendar now = GregorianCalendar.getInstance();
//        final int dayNumber = now.get(Calendar.DAY_OF_MONTH);
//        if (dayNumber == 28 || dayNumber == 30){
//
//        }


       checkUp();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,new HomeFragment());
        fragmentTransaction.commit();

        FirebaseMessaging.getInstance().subscribeToTopic("Predictions")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                mainToolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//        bubbleNavigationConstraintView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
//            @Keep
//            @Override
//            public void onNavigationChanged(View view, int position) {
//
//                switch (position){
//                    case 0:
////                        replaceFragment(homeFragment);
//                        view = bubbleNavigationConstraintView.getChildAt(R.id.homeview);
//                        getSupportActionBar().setTitle("Home");
//                        getSupportActionBar().show();
//                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.main_container,new HomeFragment());
//                        fragmentTransaction.commit();
//                        break;
//                    case 1:
//
////                        replaceFragment(updateFragment);
//                        view = bubbleNavigationConstraintView.getChildAt(R.id.chanceMixView);
//                        getSupportActionBar().setTitle("Chance Mix");
//                        getSupportActionBar().show();
//                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.main_container,new UpdateFragment());
//                        fragmentTransaction.commit();
//                        break;
//                    case 2:
////                        replaceFragment(settingsFragment);
//                        view = bubbleNavigationConstraintView.getChildAt(R.id.settingsView);
//                        getSupportActionBar().setTitle("History");
//                        getSupportActionBar().show();
//                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.main_container,new SettingsFragment());
//                        fragmentTransaction.commit();
//                        break;
//
//                }
//
//
//            }
//        });

//        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.home:
//                        replaceFragment(homeFragment);
//                        getSupportActionBar().setTitle("Home");
//
//                        return true;
//                    case R.id.update:
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (mInterstitialAd != null) {
//                                    mInterstitialAd.show(getParent());
//                                } else {
//                                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
//                                }
//                            }
//                        },2000);
//                        replaceFragment(updateFragment);
//                        getSupportActionBar().setTitle("Chance Mix");
//                        return true;
//                    case R.id.settings:
//                       replaceFragment(settingsFragment);
//                        getSupportActionBar().setTitle("History");
//                        return true;
//                    default:
//                        return false;
//                }
//
//            }
//        });
        checkPhoneVersion();



    }


    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,fragment);
        fragmentTransaction.commit();

    }
    @Keep
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.whatsapp_layout:
                String wUrl = "https://wa.me/+2349077667154?text=Hello! my name is..";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(wUrl));
                startActivity(intent);
                return true;

            case R.id.booking_code:
                showBottomDialog();
                return true;
            default:
                return false;
        }

        

    }



    @Keep
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        bookingCode = menu.findItem(R.id.booking_code);
        bookingCode.setVisible(false);

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
               bookingCode.setVisible(true);
            }

        }.start();

        return true;
    }

    @Keep
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeStateListener,filter);
        super.onStart();
    }

    @Keep
    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeStateListener);
        super.onStop();
    }

    public void showWarning(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layoutDialog = LayoutInflater.from(context).inflate(R.layout.warning_notes,null);
        builder.setView(layoutDialog);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);

    }


    public void aboutUs(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layoutDialog = LayoutInflater.from(context).inflate(R.layout.about_us_layout,null);
        builder.setView(layoutDialog);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
    }



    private void showBottomDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);
        bet9jaCode = dialog.findViewById(R.id.copy_bet9ja_code);
        betKingCode = dialog.findViewById(R.id.copy_betking_code);
        chanceMixCode = dialog.findViewById(R.id.copy_chancemix_code);

        TextView bet9jaText = dialog.findViewById(R.id.bet9ja_code);
        TextView betKingText = dialog.findViewById(R.id.betKing_code);

        TextView chanceMixText = dialog.findViewById(R.id.chancemix_code);

        ImageView deleteBet9jaCode = dialog.findViewById(R.id.delete_bet9ja_code);
        ImageView deleteBetKingCode = dialog.findViewById(R.id.delete_betking_code);

        ImageView deleteChanceMixCode = dialog.findViewById(R.id.delete_chancemix_code);

        ImageView winBookingCodeBet9ja = dialog.findViewById(R.id.winBookingCodeBet9ja);
        ImageView winBookingCodeBasketball = dialog.findViewById(R.id.winBookingCodeBasketball);

        ImageView winBookingCodeMix = dialog.findViewById(R.id.winBookingCodeMix);


        final String[] betKingBookingCode = {""};
        final String[] bet9jaBookingCode = {""};

        final String[] chanceMixBookingCode = {""};

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);



        bet9jaText.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                final EditText txtUrl = new EditText(MainMenu.this);


                dbReference.child("Booking Codes").child("Bet9ja").child("Bet9jaCode").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            txtUrl.setText(String.valueOf(task.getResult().getValue()));
                        }else{
                           txtUrl.setText("null");
                        }
                    }
                });
                new AlertDialog.Builder(MainMenu.this)
                                        .setTitle("Verify Football Code")
                                        .setView(txtUrl)
                                        .setPositiveButton("WON", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                if ((txtUrl.length() == 7)){
                                                    progressDialog.setMessage("Updating Booking state...");
                                                    progressDialog.show();
                                                    dbReference.child("Booking Codes").child("Bet9ja").child("BookingState").setValue("WON").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(MainMenu.this, "Booking state Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }

                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(MainMenu.this, "INVALID!.\nBooking Code must not be Above/Below 7 char long", Toast.LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                }


                                            }
                                        })
                                        .setNegativeButton("LOST", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                if ((txtUrl.length() == 7)){
                                                    progressDialog.setMessage("Updating Booking state...");
                                                    progressDialog.show();
                                                    dbReference.child("Booking Codes").child("Bet9ja").child("BookingState").setValue("LOST").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(MainMenu.this, "Booking state Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(MainMenu.this, "INVALID!.\nBooking Code must not be Above/Below 7 char long", Toast.LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                }

                                            }
                                        })
                                        .show();
                return true;
            }
        });
        dbReference.child("Booking Codes").child("Bet9ja").child("BookingState").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful() && task.getResult().getValue().equals(null)){
                    winBookingCodeBet9ja.setVisibility(View.INVISIBLE);
                }else if (task.isSuccessful() && task.getResult().getValue().equals("WON")){
                    winBookingCodeBet9ja.setVisibility(View.VISIBLE);
//                    txtUrl.setText(String.valueOf(task.getResult().getValue()));
                }else if (task.isSuccessful() && task.getResult().getValue().equals("LOST")){
                    winBookingCodeBet9ja.setVisibility(View.VISIBLE);
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_booking_fail);
                    winBookingCodeBet9ja.setImageDrawable(drawable);
                }else if (task.getResult().getValue().equals("null")){

                    winBookingCodeBet9ja.setVisibility(View.INVISIBLE);
                }
//                else{
//                    winBookingCodeBet9ja.setVisibility(View.INVISIBLE);
//                }
            }
        });


        betKingText.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                final EditText txtUrl = new EditText(MainMenu.this);


                dbReference.child("Booking Codes").child("BetKing").child("BetKingCode").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            txtUrl.setText(String.valueOf(task.getResult().getValue()));
                        }else{
                            txtUrl.setText("null");
                        }
                    }
                });
                new AlertDialog.Builder(MainMenu.this)
                        .setTitle("Verify Basketball Code")
                        .setView(txtUrl)
                        .setPositiveButton("WON", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if ((txtUrl.length() == 7)){
                                    progressDialog.setMessage("Updating Booking state...");
                                    progressDialog.show();
                                    dbReference.child("Booking Codes").child("BetKing").child("BookingState").setValue("WON").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MainMenu.this, "Booking state Updated Successfully", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }

                                        }
                                    });
                                }else{
                                    Toast.makeText(MainMenu.this, "INVALID!.\nBooking Code must not be Above/Below 7 char long", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }


                            }
                        })
                        .setNegativeButton("LOST", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if ((txtUrl.length() == 7)){
                                    progressDialog.setMessage("Updating Booking state...");
                                    progressDialog.show();
                                    dbReference.child("Booking Codes").child("BetKing").child("BookingState").setValue("LOST").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MainMenu.this, "Booking state Updated Successfully", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(MainMenu.this, "INVALID!.\nBooking Code must not be Above/Below 7 char long", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }

                            }
                        })
                        .show();
                return true;
            }
        });
        dbReference.child("Booking Codes").child("BetKing").child("BookingState").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful() && task.getResult().getValue().equals(null)){
                    winBookingCodeBasketball.setVisibility(View.INVISIBLE);
                }else if (task.isSuccessful() && task.getResult().getValue().equals("WON")){
                    winBookingCodeBasketball.setVisibility(View.VISIBLE);
//                    txtUrl.setText(String.valueOf(task.getResult().getValue()));
                }else if (task.isSuccessful() && task.getResult().getValue().equals("LOST")){
                    winBookingCodeBasketball.setVisibility(View.VISIBLE);
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_booking_fail);
                    winBookingCodeBasketball.setImageDrawable(drawable);
                }else if (task.getResult().getValue().equals("null")){

                    winBookingCodeBasketball.setVisibility(View.INVISIBLE);
                }
//                else{
//                    winBookingCodeBet9ja.setVisibility(View.INVISIBLE);
//                }
            }
        });


        chanceMixText.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                final EditText txtUrl = new EditText(MainMenu.this);


                dbReference.child("Booking Codes").child("ChanceMix").child("ChanceMixCode").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            txtUrl.setText(String.valueOf(task.getResult().getValue()));
                        }else{
                            txtUrl.setText("null");
                        }
                    }
                });
                new AlertDialog.Builder(MainMenu.this)
                        .setTitle("Verify Mix Code")
                        .setView(txtUrl)
                        .setPositiveButton("WON", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if ((txtUrl.length() == 7)){
                                    progressDialog.setMessage("Updating Booking state...");
                                    progressDialog.show();
                                    dbReference.child("Booking Codes").child("ChanceMix").child("BookingState").setValue("WON").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MainMenu.this, "Booking state Updated Successfully", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }

                                        }
                                    });
                                }else{
                                    Toast.makeText(MainMenu.this, "INVALID!.\nBooking Code must not be Above/Below 7 char long", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }


                            }
                        })
                        .setNegativeButton("LOST", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if ((txtUrl.length() == 7)){
                                    progressDialog.setMessage("Updating Booking state...");
                                    progressDialog.show();
                                    dbReference.child("Booking Codes").child("ChanceMix").child("BookingState").setValue("LOST").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MainMenu.this, "Booking state Updated Successfully", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(MainMenu.this, "INVALID!.\nBooking Code must not be Above/Below 7 char long", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }

                            }
                        })
                        .show();
                return true;
            }
        });


        dbReference.child("Booking Codes").child("ChanceMix").child("BookingState").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful() && task.getResult().getValue().equals(null)){
                    winBookingCodeMix.setVisibility(View.INVISIBLE);
                }else if (task.isSuccessful() && task.getResult().getValue().equals("WON")){
                    winBookingCodeMix.setVisibility(View.VISIBLE);
//                    txtUrl.setText(String.valueOf(task.getResult().getValue()));
                }else if (task.isSuccessful() && task.getResult().getValue().equals("LOST")){
                    winBookingCodeMix.setVisibility(View.VISIBLE);
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_booking_fail);
                    winBookingCodeMix.setImageDrawable(drawable);
                }else if (task.getResult().getValue().equals("null")){

                    winBookingCodeMix.setVisibility(View.INVISIBLE);
                }
//                else{
//                    winBookingCodeBet9ja.setVisibility(View.INVISIBLE);
//                }
            }
        });





        dbReference.child("Booking Codes").child("Bet9ja").child("Bet9jaCode").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    bet9jaText.setText(String.valueOf(task.getResult().getValue()));
                    bet9jaBookingCode[0] = String.valueOf(task.getResult().getValue());
                    if (!bet9jaBookingCode[0].equals("null")){
                        bet9jaCode.setVisibility(View.VISIBLE);
                        if (mAuth.getCurrentUser() != null){
                            deleteBet9jaCode.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }
        });
        dbReference.child("Booking Codes").child("BetKing").child("BetKingCode").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    betKingText.setText(String.valueOf(task.getResult().getValue()));
                    betKingBookingCode[0] = String.valueOf(task.getResult().getValue());
                    if (!betKingBookingCode[0].equals("null")){
                        betKingCode.setVisibility(View.VISIBLE);
                        if (mAuth.getCurrentUser() != null){
                            deleteBetKingCode.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });


        dbReference.child("Booking Codes").child("ChanceMix").child("ChanceMixCode").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    chanceMixText.setText(String.valueOf(task.getResult().getValue()));
                    chanceMixBookingCode[0] = String.valueOf(task.getResult().getValue());
                    if (!chanceMixBookingCode[0].equals("null")){
                        chanceMixCode.setVisibility(View.VISIBLE);
                        if (mAuth.getCurrentUser() != null){
                            deleteChanceMixCode.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });


        bet9jaCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(MainMenu.this,bet9jaBookingCode[0]);
                Toast.makeText(MainMenu.this, "Football code copied", Toast.LENGTH_SHORT).show();
            }
        });
        betKingCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(MainMenu.this, betKingBookingCode[0]);
                Toast.makeText(MainMenu.this, "Basketball code copied", Toast.LENGTH_SHORT).show();
            }
        });


        chanceMixCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(MainMenu.this, chanceMixBookingCode[0]);
                Toast.makeText(MainMenu.this, "Mix code copied", Toast.LENGTH_SHORT).show();
            }
        });


        deleteBet9jaCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainMenu.this);
                builder.setTitle("FOOTBALL CODE");
                builder.setMessage("Do you want to delete Football booking code?");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbReference.child("Booking Codes").child("Bet9ja").child("Bet9jaCode").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dbReference.child("Booking Codes").child("Bet9ja").child("BookingState").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(MainMenu.this,"Football Code Deleted",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

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

            }
        });
        deleteBetKingCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainMenu.this);
                builder.setTitle("BASKETBALL CODE");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Do you want to delete Basketball booking code?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbReference.child("Booking Codes").child("BetKing").child("BetKingCode").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dbReference.child("Booking Codes").child("BetKing").child("BookingState").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(MainMenu.this,"Basketball Code Deleted",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

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

            }
        });


        deleteChanceMixCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainMenu.this);
                builder.setTitle("Mix CODE");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Do you want to delete Mix booking code?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbReference.child("Booking Codes").child("ChanceMix").child("ChanceMixCode").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dbReference.child("Booking Codes").child("ChanceMix").child("BookingState").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(MainMenu.this,"Mix Code Deleted",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

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

            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();



    }
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);

        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
    private void checkUp() {
        AppUpdater appUpdater = new AppUpdater(MainMenu.this)
                .setDisplay(Display.DIALOG)
                .setCancelable(true)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .showEvery(3)
                .setTitleOnUpdateAvailable("WIN")
                .setContentOnUpdateAvailable("Check out the latest version available")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available yet!")
                .setButtonUpdate("Update!")
//                .setButtonUpdateClickListener(new UpdateClickListener(this,))
//                .setButtonUpdateClickListener(...)
                .setButtonDismiss("later")
//                .setButtonDismissClickListener(...)
                .setButtonDoNotShowAgain(null)
                .setIcon(R.mipmap.ic_launcher) // Notification icon


                ;
        appUpdater.start();
    }
    private void checkPhoneVersion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID_2,CHANNEL_NAME2, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC2);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.createNotificationChannel(channel2);

        }
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.football:
            replaceFragment(homeFragment);
                    getSupportActionBar().setTitle("Football");
                    break;
            case R.id.basketball:
                replaceFragment(updateFragment);
                        getSupportActionBar().setTitle("Basketball");
                         break;
            case R.id.tennis:
                replaceFragment(tennisFragment);
                getSupportActionBar().setTitle("Tennis");
                break;
            case R.id.history:
                replaceFragment(settingsFragment);
                getSupportActionBar().setTitle("History");
                break;

            case R.id.vip:
                replaceFragment(vipFragment);
                getSupportActionBar().setTitle("Vip");
                break;
            case R.id.warning:
                showWarning(MainMenu.this);
                break;
            case R.id.about_us:
                aboutUs(MainMenu.this);
                break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}