package com.utsman.kucingapes.mobilelearningprodisejarah.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utsman.kucingapes.mobilelearningprodisejarah.Adapter.AdapterOpiniList;
import com.utsman.kucingapes.mobilelearningprodisejarah.Model.ModelContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.MarginDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpiniFragment extends Fragment {
    private List<ModelContentList> lists = new ArrayList<>();
    private AdapterOpiniList adapterOpiniList;

    public OpiniFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_opini, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.opini_list);
        adapterOpiniList = new AdapterOpiniList(lists);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MarginDecoration(15, MarginDecoration.VERTICAL));
        recyclerView.setAdapter(adapterOpiniList);
        preData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterOpiniList.notifyDataSetChanged();
            }
        }, 500);

        return view;
    }

    private void preData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("opini");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("title").getValue(String.class);
                    String imgUrl = ds.child("img").getValue(String.class);
                    String cat = ds.child("author").getValue(String.class);
                    String body = ds.child("author").getValue(String.class);
                    Integer id = ds.child("id").getValue(int.class);

                    if (body != null) {
                        body = body.replaceAll("[$]", "\n");
                    }
                    addData(title, imgUrl, cat, body, id);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addData(String title, String imgUrl, String cat, String body, Integer id) {
        ModelContentList contentList = new ModelContentList(title, imgUrl, cat, body, id);
        lists.add(contentList);
        //adapterOpiniList.notifyDataSetChanged();
    }

}
