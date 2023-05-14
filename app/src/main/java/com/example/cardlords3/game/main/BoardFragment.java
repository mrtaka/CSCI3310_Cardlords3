package com.example.cardlords3.game.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardlords3.R;
import com.example.cardlords3.game.GameActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class BoardFragment extends Fragment {

    private GameViewModel mViewModel;
    private GameActivity.Cell[][] BoardCells;
    private GameActivity gameActivityParent;

    public BoardFragment(GameActivity.Cell[][] cells, GameActivity gameActivityParent) {
        BoardCells = cells;
        this.gameActivityParent = gameActivityParent;
    }

    public static BoardFragment newInstance(GameActivity.Cell[][] cells, GameActivity gameActivityParent) {
        BoardFragment celledBoardFragment = new BoardFragment(cells, gameActivityParent);
        return celledBoardFragment;
    }

    private RecyclerView boardRecyclerView;
    private BoardAdapter boardAdapter;
    private int[][] boardData;

    private JSONArray CardJsonArray = new JSONArray();
    private JSONArray InventoryJsonArray = new JSONArray();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // TODO: Use the ViewModel
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //load each type of cd data from json
        loadJson();
        Log.e("Tag", "the loaded CardJsonArray is " + CardJsonArray);

        int[] inventory = new int[25];
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                inventory[5*i+j] = BoardCells[i][j].cellID;
                //Log.e("inventory["+String.valueOf(5*i+j)+"]", String.valueOf(BoardCells[i][j].cellID));
            }
        }
        inventoryJson(inventory);
        Log.e("INV_JSON", "InventoryJsonArray: " + InventoryJsonArray);
        Log.e("INV_JSON_LENGTH", "InventoryJsonArray: " + InventoryJsonArray.length());

        View view = inflater.inflate(R.layout.board_fragment, container, false);

        boardRecyclerView = view.findViewById(R.id.board_recycler_view);
        boardRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        boardData = new int[5][5];
        boardAdapter = new BoardAdapter(boardData, BoardCells, InventoryJsonArray, gameActivityParent);
        boardRecyclerView.setAdapter(boardAdapter);

        return view;
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
            //Default Empty Card
            JSONObject addCardJsonObject = new JSONObject();
            addCardJsonObject.put("cardID", -1);
            addCardJsonObject.put("card_name", "");
            addCardJsonObject.put("card_Image", "");
            addCardJsonObject.put("typeID", -1);
            addCardJsonObject.put("raceID", -1);
            addCardJsonObject.put("health", -1);
            addCardJsonObject.put("attack", -1);
            addCardJsonObject.put("skillID", -1);
            addCardJsonObject.put("cost", -1);
            addCardJsonObject.put("rarity", -1);

            //now all the card object details to each card of the inventory
            for(int i=0; i<inventory.length; i++){

                //found the json object with that CardID
                boolean found = false;
                Log.e("CardJSONNN", String.valueOf(CardJsonArray.length()));
                for(int j=0; j<CardJsonArray.length(); j++){
                    JSONObject CardJson = CardJsonArray.getJSONObject(j);
                    //Log.e("Tag", "Get cardID "+ CardJson.getInt("cardID") + " found " + inventory[i]);
                    if(inventory[i] == CardJson.getInt("cardID")){
                        //append this card json to inventory json array
                        InventoryJsonArray.put(CardJson);
                        found = true;
                    }
                }
                if (!found) {
                    InventoryJsonArray.put(addCardJsonObject);
                }
            }

        }catch (Exception e)
        {
            Log.e("Tag", "from inventory Json: error "+e);
        }
    }

}