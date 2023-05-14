package com.example.cardlords3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CardShopActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CardListAdapter mAdapter;
    private JSONArray CardJsonArray = new JSONArray();

    private interface OnCardListLoadedCallback {
        void onCardListLoaded(JSONArray CardJsonArray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_shop);

        //load each type of cd data from json
        loadJson(new OnCardListLoadedCallback() {
            @Override
            public void onCardListLoaded(JSONArray CardJsonArray) {
                Log.e("Tag", "the loaded CardJsonArray is " + CardJsonArray);


                //create recyclerview
                mRecyclerView = findViewById(R.id.cardRecyclerView);
                mAdapter = new CardListAdapter(CardShopActivity.this, CardJsonArray, getSupportFragmentManager(), 3);
                // Connect the adapter with the RecyclerView.
                mRecyclerView.setAdapter(mAdapter);
                // Give the RecyclerView a default layout manager.
                //layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count));
                mRecyclerView.setLayoutManager(new GridLayoutManager(CardShopActivity.this, getResources().getInteger(R.integer.grid_column_count)));
                //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            }
        });


    }

    private void loadJson(OnCardListLoadedCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CardDB").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    JSONArray CardJsonArray = new JSONArray();
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
    /* parse local json
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
}