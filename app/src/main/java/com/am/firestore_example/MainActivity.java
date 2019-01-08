package com.am.firestore_example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ttt";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference mDocRef = db.document("users/Abed_Murad");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Access a Cloud Firestore instance from your Activity


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            createNewFireStoreDocument(db);
            createNewFireStoreDocumentWithExtraFields(db);
            getUserCollectionFromFireStore(db);
            modifyDocumentData();
            convertServerDataToObject();

        });
    }

    private void convertServerDataToObject() {
        mDocRef.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
        });
    }

    private void modifyDocumentData() {
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("first", "Abdallah");
        dataToSave.put("last", "Muradd");
        mDocRef.set(dataToSave);
    }

    private void getUserCollectionFromFireStore(FirebaseFirestore db) {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void createNewFireStoreDocumentWithExtraFields(FirebaseFirestore db) {
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, " added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    private void createNewFireStoreDocument(FirebaseFirestore db) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, " added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
