package com.example.cardlords3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CardEditorActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CardListAdapter mAdapter;

    private JSONArray CardJsonArray = new JSONArray();
    RecyclerView.LayoutManager layoutManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_editor);

        //load default data from json
        loadJson();

        Log.e("Tag", "the loaded CardJsonArray is " + CardJsonArray);

        //load a default creation card
        addCreateCard();
        Log.e("Tag", "the added creation card CardJsonArray is " + CardJsonArray);

        //create recyclerview
        mRecyclerView = findViewById(R.id.cardRecyclerView);
        mAdapter = new CardListAdapter(this, CardJsonArray, getSupportFragmentManager(), 2);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

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

    private void addCreateCard(){
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