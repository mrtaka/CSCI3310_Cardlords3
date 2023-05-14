package com.example.cardlords3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardDeckActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CardListAdapter mAdapter;
    private TextView CardNumView;

    private JSONArray CardJsonArray = new JSONArray();
    private JSONArray InventoryJsonArray = new JSONArray();
    private int[] inventory;

    private interface OnCardListLoadedCallback {
        void onCardListLoaded(JSONArray CardJsonArray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_deck);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        inventory = new int[0]; // Initialize to empty array, will be filled later
        CardNumView = this.findViewById(R.id.card_number_text);
        //load each type of cd data from json
        loadJson(new OnCardListLoadedCallback() {
            @Override
            public void onCardListLoaded(JSONArray CardJsonArray) {
                Log.e("Tag", "the loaded CardJsonArray is " + CardJsonArray);

                //get user inventory, and link then with each cards detail object
                if (user != null) {
                    db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    List<Long> inventoryList = (List<Long>) document.get("inventory");
                                    if (inventoryList != null) {
                                        inventory = new int[inventoryList.size()];
                                        for (int i = 0; i < inventoryList.size(); i++) {
                                            inventory[i] = inventoryList.get(i).intValue();
                                        }
                                    }
                                    //set the num of card textview
                                    CardNumView.setText(String.valueOf(inventory.length));

                                    Log.d("Tag1", Arrays.toString(inventory));
                                    inventoryJson(inventory);
                                    Log.e("Tag", "the combined InventoryJsonArray is " + InventoryJsonArray);

                                    //create recyclerview
                                    mRecyclerView = findViewById(R.id.cardRecyclerView);
                                    mAdapter = new CardListAdapter(CardDeckActivity.this, InventoryJsonArray, getSupportFragmentManager(), 1);
                                    // Connect the adapter with the RecyclerView.
                                    mRecyclerView.setAdapter(mAdapter);
                                    // Give the RecyclerView a default layout manager.
                                    //layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count));
                                    //mRecyclerView.setLayoutManager(layoutManager);
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(CardDeckActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                }
                            }
                        }
                    });
/*
                    db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    List<Long> inventoryList = (List<Long>) document.get("inventory");
                                    if (inventoryList != null) {
                                        inventory = new int[inventoryList.size()];
                                        for (int i = 0; i < inventoryList.size(); i++) {
                                            inventory[i] = inventoryList.get(i).intValue();
                                        }
                                    }
                                    Log.d("Tag1", Arrays.toString(inventory));
                                }
                            }
                        }
                    });*/
                }

            }
        });
    }

    public FragmentManager getMyFragmentManager() {
        return getSupportFragmentManager();
    }
    private void loadJson(OnCardListLoadedCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CardDB").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    //JSONArray CardJsonArray = new JSONArray();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        // Convert the Firestore Document to a JSONObject
                        JSONObject jsonObjectItem = new JSONObject(document.getData());
                        // Add the JSONObject to the JSONArray
                        CardJsonArray.put(jsonObjectItem);
                    }
                    // Now that you have your JSONArray, you can continue with your logic here.
                    // Note: Remember to handle the JSONException that might be thrown when creating a JSONObject.
                    callback.onCardListLoaded(CardJsonArray);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    /* parse from local json
    private void loadJson(){
        try{
            //load json
            InputStream inputStream = getResources().openRawResource(R.raw.cardlist);
            int size = inputStream.available();
            byte[] buffer=new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            //now we will fetch json file
            String json;
            int max;

            json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            //Log.e("Tag", "the json is " + jsonObject);
            CardJsonArray = new JSONArray(jsonObject.getString("Cards"));
            //Log.e("Tag", "the jsonArray is " + CardJsonArray);
            max = CardJsonArray.length();

            //now fetch each json items
            for(int i=0; i<max; i++){
                JSONObject jsonObjectItem = CardJsonArray.getJSONObject(i);
                //Log.e("Tag", "loadJson: file: " +filename + " rarity: " + rarity);
            }

        }catch (Exception e)
        {
            Log.e("Tag", "loadJson: error "+e);
        }
    }
    */

    private void inventoryJson(int[] inventory){
        try{
            //first add a default add_card on position 0
            JSONObject addCardJsonObject = new JSONObject();
            addCardJsonObject.put("cardID", -1);
            addCardJsonObject.put("card_name", "");
            addCardJsonObject.put("card_Image", "image_addcard");
            addCardJsonObject.put("typeID", -1);
            addCardJsonObject.put("raceID", -1);
            addCardJsonObject.put("health", -1);
            addCardJsonObject.put("attack", -1);
            addCardJsonObject.put("skillID", -1);
            addCardJsonObject.put("cost", -1);
            addCardJsonObject.put("rarity", -1);

            Log.e("TAG2", Arrays.toString(inventory));
            Log.e("TAG3", String.valueOf(inventory.length));
            InventoryJsonArray.put(addCardJsonObject);

            Log.d("Tag", "Inventory length: " + inventory.length);
            Log.d("Tag", "CardJsonArray length: " + CardJsonArray.length());


            //now all the card object details to each card of the inventory
            int inventory_length = inventory.length;
            for(int i = 0,k=0; k<inventory_length; i++, k++){

                //found the json object with that CardID
                for(int j=0; j<CardJsonArray.length(); j++){
                    JSONObject CardJson = CardJsonArray.getJSONObject(j);
                    Log.d("Tag", "Checking cardID "+ CardJson.getInt("cardID") + " against " + inventory[i]);
                    //Log.e("Tag", "Get cardID "+ CardJson.getInt("cardID") + " found " + inventory[i]);
                    if(inventory[i] == CardJson.getInt("cardID")){
                        //append this card json to inventory json array
                        InventoryJsonArray.put(CardJson);
                        Log.d("Tag", "Match found, added to InventoryJsonArray");
                        break;
                    }
                    else if(j == CardJsonArray.length()-1){
                        inventory = removeElement(inventory, i);
                        i--;
                    }
                }
            }
            if(inventory.length != inventory_length){
                Log.d("InventoryUpdated", Arrays.toString(inventory));
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String uid = user.getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Define the document reference
                    DocumentReference docRef = db.collection("users").document(uid);

                    // Get the document
                    int[] finalInventory = inventory;
                    List<Integer> inventoryList = new ArrayList<>();
                    for (int item : finalInventory) {
                        inventoryList.add(item);
                    }
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Write the entire array back to Firestore
                                    docRef.update("inventory", inventoryList)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Firestore", "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@androidx.annotation.NonNull Exception e) {
                                                    Log.w("Firestore", "Error updating document", e);
                                                }
                                            });
                                } else {
                                    Log.d("Firestore", "No such document");
                                }
                            } else {
                                Log.d("Firestore", "get failed with ", task.getException());
                            }
                        }
                    });
                } else {
                    // No user is signed in
                }
            }

        }catch (Exception e)
        {
            Log.e("Tag", "from inventory Json: error "+e);
        }
    }
    public static int[] removeElement(int[] array, int index) {
        if (index < 0 || index >= array.length) {
            // Invalid index, return the original array
            return array;
        }

        int[] newArray = new int[array.length - 1];
        for (int i = 0, j = 0; i < array.length; i++) {
            if (i != index) {
                newArray[j++] = array[i];
            }
        }

        return newArray;
    }
}