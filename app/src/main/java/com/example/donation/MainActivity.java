package com.example.donation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int DEFAULT_DESCRIPTION_LIMIT = 500;
    public static final int RC_SIGN_IN = 1;

    private String pUsername;
    private ListView projectListView;
    private ItemAdapter projectAdapter;

    private FirebaseDatabase dataBase;
    private DatabaseReference dataBaseReference;
    private ChildEventListener childEventListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        dataBase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        dataBaseReference = dataBase.getReference().child("progetti");

        projectListView = (ListView) findViewById(R.id.insertionList);

        List<ItemProject> homeProjects = new ArrayList<>();
        projectAdapter = new ItemAdapter(this, R.layout.item_insertion , homeProjects); //,
        projectListView.setAdapter(projectAdapter);

       authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null)
                {
                    onSignedInInitialize(user.getDisplayName());
                }
                else
                {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build()))
                    .build(),
                            RC_SIGN_IN );
                }
            }
        };

       //projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //       Intent appInfo = new Intent(MainActivity.this, Donation.class);
        //       startActivity(appInfo);
        //   }
        //});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(authStateListener != null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        detachDatabaseReadListener();
        projectAdapter.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Signed Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void onSignedInInitialize(String username)
    {
        pUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup()
    {
        pUsername = "anonymous";
        projectAdapter.clear();
    }


    private void attachDatabaseReadListener()
    {
        if(childEventListener == null)
        {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                {
                    //ItemProject proj = new ItemProject( dataSnapshot.child("titolo").getValue(String.class),
                    //    dataSnapshot.child("descrizione").getValue(String.class),
                    //    dataSnapshot.child("onlus").getValue(String.class), dataSnapshot.child("email").getValue(String.class));//getValue(ItemProject.class);
                    ItemProject proj = dataSnapshot.getValue(ItemProject.class);
                    projectAdapter.add(proj);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            dataBaseReference.addChildEventListener(childEventListener);
        }
    }


    private void detachDatabaseReadListener()
    {
        if(childEventListener != null)
        {
            dataBaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

}
