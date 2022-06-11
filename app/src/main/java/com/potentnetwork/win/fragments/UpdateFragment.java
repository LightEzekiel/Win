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

import com.potentnetwork.win.updateOutcome.DrawupdateScore;
import com.potentnetwork.win.modalClass.DrawPost;
import com.potentnetwork.win.recycleAdapters.DrawRecyclerAdapter;
import com.potentnetwork.win.R;
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


public class UpdateFragment extends Fragment {
    private RecyclerView drawRecyclerView;
    private List<DrawPost> drawPosts;
    private DrawRecyclerAdapter drawRecyclerAdapter;
    DatabaseReference dbref;
    DatabaseReference predictRef;
    ValueEventListener valueEventListener;
    ProgressBar drawProgressBar;
    TextView gettingPost;
    FirebaseAuth mAuth;

    FirebaseFirestore firebaseFirestore;



    public UpdateFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbref = FirebaseDatabase.getInstance().getReference();
        predictRef = dbref.child("Prediction2");
        mAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        drawPosts = new ArrayList<>();
        drawRecyclerView = view.findViewById(R.id.drawRecycleView);
        drawRecyclerAdapter = new DrawRecyclerAdapter(drawPosts);
//        drawProgressBar = view.findViewById(R.id.drawProgressBar);
        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.spin_kit2);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
//        gettingPost = view.findViewById(R.id.gettingPost);

        drawRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        drawRecyclerView.setAdapter(drawRecyclerAdapter);

//        drawProgressBar.setVisibility(View.VISIBLE);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.exists()){
//                        drawProgressBar.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
//                        gettingPost.setVisibility(View.INVISIBLE);
                        DrawPost drawPost = ds.getValue(DrawPost.class);
                        drawPosts.add(drawPost);
                        drawRecyclerAdapter.notifyDataSetChanged();
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
//                        gettingPost.setVisibility(View.INVISIBLE);
//                        drawProgressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(drawRecyclerView,"NO UPDATE YET, COME BACK LATER",Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        predictRef.addValueEventListener(valueEventListener);

//        firebaseFirestore.collection("Prediction1").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for (DocumentChange doc : value.getDocumentChanges()){
//                    if (doc.getType() == DocumentChange.Type.ADDED){
//                        DrawPost drawPost = doc.getDocument().toObject(DrawPost.class);
//                        drawPosts.add(drawPost);
//                        drawRecyclerAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });
        if (mAuth.getCurrentUser() != null){
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(drawRecyclerView);
        }

        // Inflate the layout for this fragment
        return view;
    }
    DrawPost delectedPost = null;
    @Keep
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Keep
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Keep
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            String gamekey = drawPosts.get(position).getGamekey();

            switch (direction){
                case ItemTouchHelper.LEFT:

                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setTitle("THIS POST WILL BE PERMANENTLY DELETED");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProgressDialog deleteDialog2 = new ProgressDialog(getActivity());
                            deleteDialog2.setCancelable(false);
                            deleteDialog2.setIcon(R.mipmap.ic_launcher);
                            deleteDialog2.setCanceledOnTouchOutside(false);
                            deleteDialog2.setMessage("Deleting Prediction...");
                            deleteDialog2.show();
                            delectedPost = drawPosts.get(position);
                            drawPosts.remove(position);
                            drawRecyclerAdapter.notifyItemRemoved(position);
                            predictRef.child(gamekey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        deleteDialog2.dismiss();
//                                        delectedPost = drawPosts.get(position);
//                                        drawPosts.remove(position);
                                        drawRecyclerAdapter.notifyItemRemoved(position);
                                        Snackbar.make(drawRecyclerView,"Prediction successfully deleted",Snackbar.LENGTH_SHORT).show();
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
                    
//                    delectedPost = drawPosts.get(position);
//                    drawPosts.remove(position);
//                    drawRecyclerAdapter.notifyItemRemoved(position);
//                    Snackbar.make(drawRecyclerView, "Restore post", Snackbar.LENGTH_LONG)
//                            .setAction("Undo", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    drawPosts.add(position,delectedPost);
//                                    drawRecyclerAdapter.notifyItemInserted(position);
//                                }
//                            })
//                            .show();
                    break;
                    case ItemTouchHelper.RIGHT:
                        Intent updateIntent = new Intent(getContext(), DrawupdateScore.class);
                        updateIntent.putExtra("Country",drawPosts.get(position).getCountry());
                        updateIntent.putExtra("Club",drawPosts.get(position).getClub());
                        updateIntent.putExtra("Prediction",drawPosts.get(position).getPrediction());
                        updateIntent.putExtra("Odd",drawPosts.get(position).getOdd());
                        updateIntent.putExtra("Timestamp",drawPosts.get(position).getTimestamp());
                        updateIntent.putExtra("Gamekey",drawPosts.get(position).getGamekey());
                        updateIntent.putExtra("Gameday",drawPosts.get(position).getGameday());
                        startActivity(updateIntent);
                        break;
            }
//            long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
//            Query oldItems = predictRef.orderByChild("daystamp").endAt(cutoff);
//            oldItems.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot ds : snapshot.getChildren()){
//                        if (ds.exists()){
//                            predictRef.child(gamekey).removeValue();
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

}