package com.utsman.kucingapes.mobilelearningprodisejarah.Content;

import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

public class OpiniActivity extends BaseContent {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        fav = menu.findItem(R.id.action_favorite);
        favFill = menu.findItem(R.id.action_favorite_fill);

        fav.setVisible(false);
        favFill.setVisible(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("opini").child(String.valueOf(id));
        mRef.keepSynced(true);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("userfavorit/"+nameUser)) {
                    String statusFav = dataSnapshot.child("userfavorit/"+nameUser).getValue(String.class);

                    if (statusFav != null) {
                        if (statusFav.equals("false")) {
                            fav.setVisible(true);
                            favFill.setVisible(false);
                        } if (statusFav.equals("true")) {
                            fav.setVisible(false);
                            favFill.setVisible(true);
                        }
                    }
                } else {
                    fav.setVisible(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String y = "true";
        String n = "false";

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("data-md").child(String.valueOf(id));

        switch (item.getItemId()) {
            case R.id.action_favorite:
                Snackbar favSnack = Snackbar.make(coordinatorLayout, "Ditambahkan ke Favorit", Snackbar.LENGTH_LONG);
                favSnack.show();
                mRef.child("userfavorit/"+nameUser).setValue(y);
                return true;
            case R.id.action_favorite_fill:
                Snackbar favSnackRem = Snackbar.make(coordinatorLayout, "Dihapus dari Favorit", Snackbar.LENGTH_LONG);
                favSnackRem.show();
                mRef.child("userfavorit/"+nameUser).setValue(n);
                return true;
            case R.id.text_kecil:
                settings.setDefaultFontSize(small);
                return true;
            case R.id.text_sedang:
                settings.setDefaultFontSize(medium);
                return true;
            case R.id.text_besar:
                settings.setDefaultFontSize(large);
                return true;
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}