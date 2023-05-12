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

        //==================== self-make =====================
        //load default data from json
        loadJson();
        Log.e("Tag", "the loaded CardJsonArray is " + CardJsonArray);
        //load updated data (rarity only) from share preference
        //loadSharePreference();
        //Log.e("Tag", "updated list is"+ updatedrarityList);
        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.cardRecyclerView);
        // Create an adapter and supply the data to be displayed,
        // initially just a list of image path
        // TODO: Update and pass more information as needed
        //================changed here==========================
        mAdapter = new CardListAdapter(this, CardJsonArray);

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        // TODO: Update the layout manager
        //  i.e. Set up Grid according to the orientation of phone
        //layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count));
        //mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Log.e("Tag", "On create run");

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
            //Log.e("Tag", "the jsonArray max is " + max);

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
}