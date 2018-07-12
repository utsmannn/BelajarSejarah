package com.utsman.kucingapes.mobilelearningprodisejarah.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.utsman.kucingapes.mobilelearningprodisejarah.Adapter.AdapterCategory;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.MarginDecoration;
import com.utsman.kucingapes.mobilelearningprodisejarah.Model.ModelCategory;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MateriFragment extends Fragment {

    private List<ModelCategory> modelCategoryList = new ArrayList<>();
    private AdapterCategory adapterCategory;

    public MateriFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        RecyclerView recyclerMain = view.findViewById(R.id.main_list);
        adapterCategory = new AdapterCategory(modelCategoryList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerMain.setLayoutManager(layoutManager);
        recyclerMain.setItemAnimator(new DefaultItemAnimator());
        recyclerMain.addItemDecoration(new MarginDecoration(15, MarginDecoration.VERTICAL));
        recyclerMain.setAdapter(adapterCategory);
        preData();
        return view;
    }

    private void preData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("kat");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    String title = ds.child("titleCat").getValue(String.class);
                    String imgUrl = ds.child("imgCat").getValue(String.class);
                    String name = ds.child("nameCat").getValue(String.class);
                    Integer id = ds.child("idCat").getValue(Integer.class);
                    addData(title, imgUrl, id, name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addData(String title, String imgUrl, Integer id, String name) {
        ModelCategory modelCategory = new ModelCategory(title, imgUrl, id, name);
        modelCategoryList.add(modelCategory);
        adapterCategory.notifyDataSetChanged();
    }

}
