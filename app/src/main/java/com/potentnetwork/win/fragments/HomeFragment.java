package com.potentnetwork.win.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.potentnetwork.win.updateOutcome.HomeUpdateScore;
import com.potentnetwork.win.R;
import com.potentnetwork.win.SignUp;
import com.potentnetwork.win.modalClass.WinPost;
import com.potentnetwork.win.recycleAdapters.WinRecyclerAdapter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class HomeFragment extends Fragment   {


    private static final int NONE = 0;
    private static final int SWIPE = 1;
    private int mode = NONE;
    private float startY, stopY;
    // We will only detect a swipe if the difference is at least 100 pixels
    private static final int TRESHOLD = 100;

    private RecyclerView blogView;
    private List<WinPost> winList;
    private WinRecyclerAdapter winRecyclerAdapter;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference dbRef;
    DatabaseReference predictionRef;
    ValueEventListener valueEventListener;
    ProgressBar homeProgressBar;
    String gamekey;
    FirebaseAuth mAuth;

    SwipeListener swipeListener;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbRef = FirebaseDatabase.getInstance().getReference();
        predictionRef = dbRef.child("Prediction1");
        mAuth = FirebaseAuth.getInstance();



        firebaseFirestore = FirebaseFirestore.getInstance();

        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        winList = new ArrayList<>();
        blogView = view.findViewById(R.id.list_view);
        winRecyclerAdapter = new WinRecyclerAdapter(winList);
        blogView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        blogView.setAdapter(winRecyclerAdapter);


        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.spin_kit1);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
        // Inflate the layout for this fragment






        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.exists()){
                        progressBar.setVisibility(View.INVISIBLE);
                        WinPost winPost = ds.getValue(WinPost.class);
                        winList.add(winPost);
                        winRecyclerAdapter.notifyDataSetChanged();
                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(blogView,"NO UPDATE YET, COME BACK LATER",Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        };


//        predictionRef.orderByChild("daystamp").endAt(cutoff)

//        long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
//        Query oldItems = dbRef.orderByChild("daystamp").endAt(cutoff);
//        oldItems.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
//                    itemSnapshot.getRef().child("Prediction1").child(gamekey).removeValue();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        predictionRef.addValueEventListener(valueEventListener);

//        firebaseFirestore.collection("Predictions").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for(DocumentChange doc : value.getDocumentChanges()){
//                    if (doc.getType() == DocumentChange.Type.ADDED){
//
//                    }
//                }
//            }
//        });
        swipeListener = new SwipeListener(blogView);
        if (mAuth.getCurrentUser() != null){
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(blogView);
        }



        return view;

            }
        WinPost delectedPost = null;
    @Keep
       ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
        @Keep
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
           @Keep
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            gamekey = winList.get(position).getGamekey();


            switch (direction){
                case ItemTouchHelper.LEFT:
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setTitle("THIS POST WILL BE PERMANENTLY DELETED");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ProgressDialog deleteDialog = new ProgressDialog(getActivity());
                            deleteDialog.setCancelable(false);
                            deleteDialog.setIcon(R.mipmap.ic_launcher);
                            deleteDialog.setCanceledOnTouchOutside(false);
                            deleteDialog.setMessage("Deleting Prediction...");
                            deleteDialog.show();
                            delectedPost = winList.get(position);
                            winList.remove(position);
                            winRecyclerAdapter.notifyItemRemoved(position);
                            predictionRef.child(gamekey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                    deleteDialog.dismiss();
//                                   delectedPost = winList.get(position);
//                                   winList.remove(position);
                                   winRecyclerAdapter.notifyItemRemoved(position);
                                   Snackbar.make(blogView,"Prediction successfully deleted",Snackbar.LENGTH_SHORT).show();
                               }

                                }
                            });
                           deleteDialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

//                    delectedPost = winList.get(position);
//                    winList.remove(position);
//                    winRecyclerAdapter.notifyItemRemoved(position);
//                    Snackbar.make(blogView, "Restore post", Snackbar.LENGTH_LONG)
//                            .setAction("Undo", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                               winList.add(position,delectedPost);
//                               winRecyclerAdapter.notifyItemInserted(position);
//                                }
//                            })
//                            .show();
                    break;
                case ItemTouchHelper.RIGHT:
                    Intent updateIntent = new Intent(getContext(), HomeUpdateScore.class);
                    updateIntent.putExtra("Country",winList.get(position).getCountry());
                    updateIntent.putExtra("Club",winList.get(position).getClub());
                    updateIntent.putExtra("Prediction",winList.get(position).getPrediction());
                    updateIntent.putExtra("Odd",winList.get(position).getOdd());
                    updateIntent.putExtra("Timestamp",winList.get(position).getTimestamp());
                    updateIntent.putExtra("Gamekey",winList.get(position).getGamekey());
                    updateIntent.putExtra("Gameday",winList.get(position).getGameday());
                    startActivity(updateIntent);
                    break;

            }
//            long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
//            Query oldItems = predictionRef.orderByChild("daystamp").endAt(cutoff);
//            oldItems.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot ds : snapshot.getChildren()){
//                        if (ds.exists()){
//                            predictionRef.child(gamekey).removeValue();
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }
        @Keep
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(),R.color.gray))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(),R.color.gray))
                    .addSwipeRightActionIcon(R.drawable.ic_update_score)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Keep
    private class SwipeListener implements View.OnTouchListener {
        GestureDetector gestureDetector;
        SwipeListener(View view){
            int threshold = 100;
            int velocity_threshold = 100;

            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }



//                @Override
//                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
////                    float xDiff = e2.getX() - e1.getX();
//                   float yDiff = e2.getY() - e1.getY();
//                   try {
//                           if (Math.abs(yDiff) > threshold && Math.abs(velocityY) > velocity_threshold){
//                               if (!(yDiff > 0)){
//                                   Intent intent = new Intent(getContext(), SignUp.class);
//                                   startActivity(intent);
//                               }
//                                return true;
//                           }
//
//
//                   }catch (Exception e){
//                       e.printStackTrace();
//                   }
//                   return false;
//                }
            };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }
        @Keep
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                // MotionEvent.ACTION_DOWN means one finger.
                // MotionEvent.ACTION_POINTER_DOWN is two fingers.
                case MotionEvent.ACTION_POINTER_DOWN:

                    // This happens when you touch the screen with two fingers
                    mode = SWIPE;
                    // You can also use event.getY(1) or the average of the two
                    startY = event.getY(1);
                    break;
                case MotionEvent.ACTION_POINTER_UP:

                    // This happens when you release the second finger
                    mode = NONE;
                    if (Math.abs(startY - stopY) > TRESHOLD) {

                        if (startY > stopY) {

                            // Swipe up.
                            Intent intent = new Intent(getContext(), SignUp.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    }

                    break;
                case MotionEvent.ACTION_MOVE:

                    if (mode == SWIPE) {

                        stopY = event.getY(1);
                    }

                    break;
            }
            return gestureDetector.onTouchEvent(event);
        }
    }
}