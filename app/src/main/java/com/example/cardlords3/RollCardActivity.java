package com.example.cardlords3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Random;

public class RollCardActivity extends AppCompatActivity implements ShakeDetector.OnShakeListener{

    private ShakeDetector mShakeDetector;
    int count = 0;
    int exist_card = 0;
    TextView TotalCardTextView;
    TextView AddCardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_card);

        if(getIntent().hasExtra("card_count")){
            exist_card = getIntent().getIntExtra("card_count", 0);
        }

        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(this);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mShakeDetector, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);

        TotalCardTextView = this.findViewById(R.id.card_number_text);
        TotalCardTextView.setText(Integer.toString(exist_card + count));
        AddCardTextView = this.findViewById(R.id.addCardNum);
    }
    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();

        SensorManager sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        sensorManager.unregisterListener(shakeDetector);
    }*/

    @Override
    public void onShake() {
        // do something in response to the shake event
        if((exist_card + count) < 50){
            addCard();
        }else{
            Toast t = Toast.makeText(this, "CardDeck is full", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void addCard(){
        Random random = new Random();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
// Get a random document from the "CardDB" collection
        db.collection("CardDB")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = task.getResult().size();
                            int randomIndex = random.nextInt(count);
                            DocumentSnapshot randomDocument = task.getResult().getDocuments().get(randomIndex);
                            long randomCardID = randomDocument.getLong("cardID");

                            // Then add randomCardID to the user's inventory as you have in your code

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // User is signed in
                                String uid = user.getUid();

                                // Define the document reference
                                DocumentReference docRef = db.collection("users").document(uid);

                                // Get the document
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                // Get the inventory array
                                                List<Long> inventory = (List<Long>) document.get("inventory");
                                                // Add the new value
                                                inventory.add(randomCardID);
                                                // Write the entire array back to Firestore
                                                docRef.update("inventory", inventory)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("RollCard", "DocumentSnapshot successfully updated!");
                                                                // handle success here - update UI, etc.
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("RollCard", "Error updating document", e);
                                                                // handle failure here - show error message, etc.
                                                            }
                                                        });
                                            } else {
                                                Log.d("RollCard", "No such document");
                                            }
                                        } else {
                                            Log.d("RollCard", "get failed with ", task.getException());
                                        }
                                    }
                                });
                            } else {
                                // No user is signed in
                                // Handle this case
                            }
                        } else {
                            Log.d("RollCard", "Error getting documents: ", task.getException());
                        }
                    }
                });

        count++;
        Toast t = Toast.makeText(this, "Shake added " + count + " cards!", Toast.LENGTH_SHORT);
        t.show();
        TotalCardTextView.setText(Integer.toString(exist_card + count));
        String input = "+"+ Integer.toString(count);
        AddCardTextView.setText(input);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(mShakeDetector);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mShakeDetector, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
    }
}