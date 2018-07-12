package com.utsman.kucingapes.mobilelearningprodisejarah.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpiniFragment extends Fragment {
    private List<ModelContentList> lists = new ArrayList<>();
    private AdapterOpiniList adapterOpiniList;
    private View view;

    public OpiniFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapterOpiniList = new AdapterOpiniList(lists);
        //preData();
        //adapterOpiniList.notifyDataSetChanged();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                preData();
            }
        }, 1000);*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //preData();

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_opini, container, false);
        preData();
        return view;
    }

    private void preData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        RecyclerView recyclerView = view.findViewById(R.id.opini_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MarginDecoration(15, MarginDecoration.VERTICAL));
        recyclerView.setAdapter(adapterOpiniList);
        final DatabaseReference myRef = database.getReference("opini");
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

                /*if (adapterOpiniList.getItemCount() == 0) {
                    adapterOpiniList.notifyDataSetChanged();
                }*/
                //for (adapterOpiniList.getItemCount() == 0) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterOpiniList.notifyDataSetChanged();
                    }
                }, 500);

                //adapterOpiniList.notifyDataSetChanged();
                //Toast.makeText(getContext(), String.valueOf(adapterOpiniList.getItemCount()), Toast.LENGTH_SHORT).show();
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addData(String title, String imgUrl, String cat, String body, Integer id) {
        ModelContentList contentList = new ModelContentList(title, imgUrl, cat, body, id);
        lists.add(contentList);
        //adapterOpiniList.removeData(lists.size());
        //adapterOpiniList.notifyDataSetChanged();
        //adapterOpiniList.notifyItemRangeInserted(0,0);
        //onActivityResult(1,RESULT_OK,);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //preData();
    }*/
}
