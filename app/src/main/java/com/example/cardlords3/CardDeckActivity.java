package com.example.cardlords3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CardDeckActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CardListAdapter mAdapter;

    private JSONArray CardJsonArray = new JSONArray();
    private JSONArray InventoryJsonArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_deck);

        //load each type of cd data from json
        loadJson();
        Log.e("Tag", "the loaded CardJsonArray is " + CardJsonArray);

        //get user inventory, and link then with each cards detail object
        int[] inventory = {3, 2, 2, 1, 4, 1, 1};
        inventoryJson(inventory);
        Log.e("Tag", "the combined InventoryJsonArray is " + InventoryJsonArray);

        //create recyclerview
        mRecyclerView = findViewById(R.id.cardRecyclerView);
        mAdapter = new CardListAdapter(this, InventoryJsonArray, getSupportFragmentManager(), 1);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        //layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count));
        //mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Log.e("Tag", "On create run");
    }

    public FragmentManager getMyFragmentManager() {
        return getSupportFragmentManager();
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

    private void inventoryJson(int[] inventory){
        try{
            //now all the card object details to each card of the inventory
            for(int i=0; i<inventory.length; i++){

                //found the json object with that CardID
                for(int j=0; j<CardJsonArray.length(); j++){
                    JSONObject CardJson = CardJsonArray.getJSONObject(j);
                    //Log.e("Tag", "Get cardID "+ CardJson.getInt("cardID") + " found " + inventory[i]);
                    if(inventory[i] == CardJson.getInt("cardID")){
                        //append this card json to inventory json array
                        InventoryJsonArray.put(CardJson);
                    }
                }
            }

        }catch (Exception e)
        {
            Log.e("Tag", "from inventory Json: error "+e);
        }
    }

}