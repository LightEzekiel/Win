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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.potentnetwork.win.R;
import com.potentnetwork.win.modalClass.DrawPost;
import com.potentnetwork.win.modalClass.HistoryPost;
import com.potentnetwork.win.modalClass.TennisPost;
import com.potentnetwork.win.recycleAdapters.HistoryRecycleAdapter;
import com.potentnetwork.win.recycleAdapters.TennisRecyclerAdapter;
import com.potentnetwork.win.updateOutcome.DrawupdateScore;
import com.potentnetwork.win.updateOutcome.TennisUpdateScore;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class TennisFragment extends Fragment {
    RecyclerView tennisRecycleView;
    private List<TennisPost> tennisPosts;
    TennisRecyclerAdapter tennisRecycleAdapter;
    DatabaseReference dbref;
    DatabaseReference predictRef;
    ValueEventListener valueEventListener;
    FirebaseAuth mAuth;


    public TennisFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbref = FirebaseDatabase.getInstance().getReference();
        predictRef = dbref.child("Tennis");
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tennis, container, false);

        tennisRecycleView = view.findViewById(R.id.tennisRecycleView);

        tennisPosts = new ArrayList<>();
        tennisRecycleAdapter = new TennisRecyclerAdapter(tennisPosts);


        tennisRecycleView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        tennisRecycleView.setAdapter(tennisRecycleAdapter);
        ProgressBar progressBar = view.findViewById(R.id.spin_kit3);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.exists()) {
//                        drawProgressBar.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
//                        gettingPost.setVisibility(View.INVISIBLE);
                        TennisPost tennisPost = ds.getValue(TennisPost.class);
                        tennisPosts.add(tennisPost);
                        tennisRecycleAdapter.notifyDataSetChanged();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
//                        gettingPost.setVisibility(View.INVISIBLE);
//                        drawProgressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(tennisRecycleView, "NO UPDATE YET, COME BACK LATER", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        predictRef.addValueEventListener(valueEventListener);

        if (mAuth.getCurrentUser() != null) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(tennisRecycleView);
        }

        return view;
    }

    TennisPost delectedPost = null;
    @Keep
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Keep
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Keep
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            String gamekey = tennisPosts.get(position).getGamekey();

            switch (direction) {
                case ItemTouchHelper.LEFT:

                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setTitle("THIS POST WILL BE PERMANENTLY DELETED");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProgressDialog deleteDialog2 = new ProgressDialog(getActivity());
                            deleteDialog2.setCancelable(false);
                            deleteDialog2.setCanceledOnTouchOutside(false);
                            deleteDialog2.setIcon(R.mipmap.ic_launcher);
                            deleteDialog2.setMessage("Deleting Prediction...");
                            deleteDialog2.show();
                            delectedPost = tennisPosts.get(position);
                            tennisPosts.remove(position);
                            tennisRecycleAdapter.notifyItemRemoved(position);
                            predictRef.child(gamekey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        deleteDialog2.dismiss();
//                                        delectedPost = drawPosts.get(position);
//                                        drawPosts.remove(position);
                                        tennisRecycleAdapter.notifyItemRemoved(position);
                                        Snackbar.make(tennisRecycleView, "Prediction successfully deleted", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            deleteDialog2.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    break;
                case ItemTouchHelper.RIGHT:
                    Intent updateIntent = new Intent(getContext(), TennisUpdateScore.class);
                    updateIntent.putExtra("Country", tennisPosts.get(position).getCountry());
                    updateIntent.putExtra("Club", tennisPosts.get(position).getClub());
                    updateIntent.putExtra("Prediction", tennisPosts.get(position).getPrediction());
                    updateIntent.putExtra("Odd", tennisPosts.get(position).getOdd());
                    updateIntent.putExtra("Timestamp", tennisPosts.get(position).getTimestamp());
                    updateIntent.putExtra("Gamekey", tennisPosts.get(position).getGamekey());
                    updateIntent.putExtra("Gameday", tennisPosts.get(position).getGameday());
                    startActivity(updateIntent);
                    break;
            }
        }

        @Keep
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray))
                    .addSwipeRightActionIcon(R.drawable.ic_update_score)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}