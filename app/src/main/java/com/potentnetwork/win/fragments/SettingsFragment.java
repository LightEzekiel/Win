package com.potentnetwork.win.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.potentnetwork.win.modalClass.HistoryPost;
import com.potentnetwork.win.R;
import com.potentnetwork.win.recycleAdapters.HistoryRecycleAdapter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SettingsFragment extends Fragment {
    RecyclerView historyRecycleView;
    private List<HistoryPost> historyPosts;
    DatabaseReference dbRef;
    DatabaseReference predictionRef;
    ValueEventListener valueEventListener;
    HistoryRecycleAdapter historyRecycleAdapter;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbRef = FirebaseDatabase.getInstance().getReference();
        predictionRef = dbRef.child("Predictions");
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        historyRecycleView = view.findViewById(R.id.historyRecycleView);

        historyPosts = new ArrayList<>();
        historyRecycleAdapter = new HistoryRecycleAdapter(historyPosts);


        historyRecycleView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        historyRecycleView.setAdapter(historyRecycleAdapter);
        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.spin_kit3);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.exists()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        HistoryPost historyPost = ds.getValue(HistoryPost.class);
                        historyPosts.add(historyPost);
                        historyRecycleAdapter.notifyDataSetChanged();

                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(historyRecycleView,"NO HISTORY YET",Snackbar.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        predictionRef.addValueEventListener(valueEventListener);
        if (mAuth.getCurrentUser() != null){
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(historyRecycleView);
        }


        return view;
    }

    HistoryPost delectedPost = null;
    @Keep
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Keep
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Keep
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            String gamekey = historyPosts.get(position).getGamekey();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setTitle("THIS PREDICTION WILL BE PERMANENTLY DELETED");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ProgressDialog deleteDialog3 = new ProgressDialog(getActivity());
                            deleteDialog3.setCancelable(false);
                            deleteDialog3.setCanceledOnTouchOutside(false);
                            deleteDialog3.setIcon(R.mipmap.ic_launcher);
                            deleteDialog3.setMessage("Deleting Prediction...");
                            deleteDialog3.show();

                            delectedPost = historyPosts.get(position);
                            historyPosts.remove(position);
                            historyRecycleAdapter.notifyItemRemoved(position);
                            predictionRef.child(gamekey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        deleteDialog3.dismiss();
//                                        delectedPost = historyPosts.get(position);
//                                        historyPosts.remove(position);
                                        historyRecycleAdapter.notifyItemRemoved(position);
                                        Snackbar.make(historyRecycleView,"Prediction successfully deleted",Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            deleteDialog3.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

//                    Snackbar.make(historyRecycleView, "Restore post", Snackbar.LENGTH_LONG)
//                            .setAction("Undo", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    historyPosts.add(position, delectedPost);
//                                    historyRecycleAdapter.notifyItemInserted(position);
//                                }
//                            })
//                            .show();
                    break;
            }
//            long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(4, TimeUnit.DAYS);
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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}