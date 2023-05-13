package com.example.cardlords3.game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardlords3.R;
import com.example.cardlords3.game.main.BoardFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GameActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CardListAdapterBoard mAdapter;

    private JSONArray CardJsonArray = new JSONArray();
    private JSONArray InventoryJsonArray;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Fragment newBoardFragment1 = new BoardFragment();
        Fragment newBoardFragment2 = new BoardFragment();
        Fragment newBoardFragment3 = new BoardFragment();

        /*
        //TopFragment topFragment = new TopFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.p2_hand_fragment_container, newBoardFragment1)
                .commit();
        */

        //MiddleFragment middleFragment = new MiddleFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.board_fragment_container, newBoardFragment2)
                .commit();

        /*
        //BottomFragment bottomFragment = new BottomFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.p1_hand_fragment_container, newBoardFragment3)
                .commit();
        */

        loadJson();
        Log.e("Tag", "the loaded CardJsonArray is " + CardJsonArray);

        //Create Own Hand
        //get user inventory, and link then with each cards detail object
        int[] handOwnInventory = {3, 2, 2, 1, 4, 1, 1};
        loadHand(handOwnInventory, findViewById(R.id.cardRecyclerViewOwnHand));

        //Create Enemy Hand
        int[] handEnemyInventory = {4, 4, 4, 3};
        loadHand(handEnemyInventory, findViewById(R.id.cardRecyclerViewEnemyHand));

    }

    private void loadHand(int[] handInventory, RecyclerView handView) {
        InventoryJsonArray = new JSONArray();
        inventoryJson(handInventory);
        Log.e("Tag", "the combined InventoryJsonArray is " + InventoryJsonArray);

        //create recyclerview
        mRecyclerView = handView;
        //To create fragment
        mAdapter = new CardListAdapterBoard(this, InventoryJsonArray); //, getSupportFragmentManager(), 1
        // Connect the adapter with the RecyclerView
        mRecyclerView.setAdapter(mAdapter);
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