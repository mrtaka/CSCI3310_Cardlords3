package com.example.cardlords3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CardEditorActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CardListAdapter mAdapter;
    //JSONArray CardJsonArray2 = null;
    RecyclerView.LayoutManager layoutManager;

    public interface OnCardListLoadedCallback {
        void onCardListLoaded(JSONArray CardJsonArray);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_editor);

        //load default data from json
        loadJson(new OnCardListLoadedCallback() {
            @Override
            public void onCardListLoaded(JSONArray CardJsonArray) {
                Log.e("Tag", "the loaded CardJsonArray is " + CardJsonArray);

                //load a default creation card
                addCreateCard(CardJsonArray);
                Log.e("Tag", "the added creation card CardJsonArray is " + CardJsonArray);

                //create recyclerview
                mRecyclerView = findViewById(R.id.cardRecyclerView);
                mAdapter = new CardListAdapter(CardEditorActivity.this, CardJsonArray, getSupportFragmentManager(), 2);
                // Connect the adapter with the RecyclerView.
                mRecyclerView.setAdapter(mAdapter);
                // Give the RecyclerView a default layout manager.
                mRecyclerView.setLayoutManager(new LinearLayoutManager(CardEditorActivity.this, LinearLayoutManager.HORIZONTAL, false));
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
                        Log.d("test321", document.getId() + " => " + document.getData());

                        // Convert the Firestore Document to a JSONObject
                        JSONObject jsonObjectItem = new JSONObject(document.getData());
                        Log.d("test3212", String.valueOf(jsonObjectItem));
                        // Add the JSONObject to the JSONArray
                        CardJsonArray.put(jsonObjectItem);
                    }
                    Log.d("test3212", String.valueOf(CardJsonArray));
                    // Now that you have your JSONArray, you can continue with your logic here.
                    // Note: Remember to handle the JSONException that might be thrown when creating a JSONObject.
                    callback.onCardListLoaded(CardJsonArray);
                } else {
                    Log.d("test321", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /* parse from local json file
    private void loadJson2(){
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
            Log.e("STRINGJSON", json);
            JSONObject jsonObject = new JSONObject(json);
            //Log.e("Tag", "the json is " + jsonObject);
            CardJsonArray2 = new JSONArray(jsonObject.getString("Cards"));
            //Log.e("Tag", "the jsonArray is " + CardJsonArray);
            max = CardJsonArray2.length();

            //now fetch each json items
            for(int i=0; i<max; i++){
                JSONObject jsonObjectItem = CardJsonArray2.getJSONObject(i);
                //Log.e("Tag", "loadJson: file: " +filename + " rarity: " + rarity);
            }

        }catch (Exception e)
        {
            Log.e("Tag", "loadJson: error "+e);
        }
    }
    */
    private void addCreateCard(JSONArray CardJsonArray){
        //first add a default add_card on position 0
        try {
            JSONObject addCardJsonObject = new JSONObject();
            addCardJsonObject.put("cardID", -2);
            addCardJsonObject.put("card_name", "");
            addCardJsonObject.put("card_Image", "image_createcard");
            addCardJsonObject.put("typeID", -1);
            addCardJsonObject.put("raceID", -1);
            addCardJsonObject.put("health", -1);
            addCardJsonObject.put("attack", -1);
            addCardJsonObject.put("skillID", -1);
            addCardJsonObject.put("cost", -1);
            addCardJsonObject.put("rarity", -1);

            // Shift the existing elements to the right
            for (int i = CardJsonArray.length() - 1; i >= 0; i--) {
                CardJsonArray.put(i + 1, CardJsonArray.get(i));
            }

            // Insert the new object at the front of the array
            CardJsonArray.put(0, addCardJsonObject);

        }catch (Exception e)
        {
            Log.e("Tag", "from inventory Json: error "+e);
        }
    }
}