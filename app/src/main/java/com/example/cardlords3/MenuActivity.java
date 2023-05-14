package com.example.cardlords3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cardlords3.data.model.Card;
import com.example.cardlords3.game.GameActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";
    private FirebaseUser user;
    private int userType;
    private Button cardEditorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        cardEditorButton = findViewById(R.id.card_editor_button);

        if (user != null) {
            db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userType = Math.toIntExact(document.getLong("userType"));
                            if (userType == 0)
                                cardEditorButton.setEnabled(true);
                            // Continue processing the user type...
                        } else {
                            Log.d(TAG, "No such document");
                            //New account
                            //Default: set userType as normal user and set default inventory.
                            Map<String, Object> userDoc = new HashMap<>();
                            userDoc.put("userType", 1); // 0 - admin, 1 - normal user, 2 - guest
                            userDoc.put("inventory", Arrays.asList(1, 2, 3)); // list of card IDs
                            db.collection("users").document(user.getUid()).set(userDoc);
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        cardEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cardEditorButton.isEnabled()) {
                    //not working as intended, to be fixed or abandoned.
                    Toast.makeText(getApplicationContext(), "Only accessible by admin", Toast.LENGTH_SHORT).show();
                }
                else{
                    GoCardEditor(v);
                }
            }
        });
    }

    //when click pve mode
    public void pve_launch(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, GameActivity.class); //Set target activity
        startActivity(intent);
    }

    //when click pvp mode
    public void pvp_launch(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, GameActivity.class); //Set target activity
        startActivity(intent);
    }

    //when click CardDeck
    public void GoCardDeck(View view) {
        Intent intent = new Intent(this, CardDeckActivity.class); //Set target activity
        startActivity(intent);
    }

    //when click CardEditor
    public void GoCardEditor(View view) {

        Intent intent = new Intent(this, CardEditorActivity.class); //Set target activity
        startActivity(intent);
    }


    //when click logout
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
        //super.onBackPressed();
    }

}