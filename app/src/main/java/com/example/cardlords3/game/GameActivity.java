package com.example.cardlords3.game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardlords3.R;
import com.example.cardlords3.game.main.BaseFragment;
import com.example.cardlords3.game.main.BoardFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CardListAdapterBoard mAdapter;

    private JSONArray CardJsonArray = new JSONArray();
    private JSONArray InventoryJsonArray;

    public int turn = 1;
    public List<Integer> enemyDeck;
    public List<Integer> enemyHand;
    public static List<Integer> enemyBase;
    public List<Integer> enemyGrave;

    public List<Integer> ownDeck;
    public List<Integer> ownHand;
    public static List<Integer> ownBase;
    public List<Integer> ownGrave;

    public int enemyManaCount = 10;
    public int enemyMovesCount = 3;
    public int enemyGraveCount = 0;
    public int ownManaCount = 10;
    public int ownMovesCount = 3;
    public int ownGraveCount = 0;

    public int enemyMaxMana = 10;
    public int enemyMaxMoves = 3;
    public int enemyConquers = 0;
    public int ownMaxMana = 10;
    public int ownMaxMoves = 3;
    public int ownConquers = 0;

    private int enemyDeckDrawable = 0;
    private int ownDeckDrawable = 0;

    public class Cell {
        public int owner;  //0=Own, 1=Enemy
        public int cellID;
        public int movable;
        public int skill_usable;
        public int effect1;
        public int effect2;
        public int effect3;

        public Cell() {
            owner = 0;  //0=Own, 1=Enemy
            cellID = -1;    //-1 means EMPTY
            movable = 0;    //Number of Moves left
            skill_usable = 0;   //Is skill still Active
            effect1 = 0;
            effect2 = 0;
            effect3 = 0;
        }
    }

    Cell[][] BoardCells = new Cell[5][5];

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //TODO: Create Data
        turn = 1;
        int[] enemyDeckDummy = new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
        int[] ownDeckDummy = new int[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4};

        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                BoardCells[i][j] = new Cell();
            }
        }
        BoardCells[3][0].cellID = 3;
        BoardCells[0][2].cellID = 4;
        BoardCells[0][2].owner = 1;
        //Log.e("BoardCells[2][3]", String.valueOf(BoardCells[2][3].cellID));
        //Array to List & Vice Versa
        /*
        List<Integer> intList = Arrays.asList(intArray);
        intList.toArray(intArray);
        */
        enemyDeck = new ArrayList<>();
        for (int i : enemyDeckDummy) {
            enemyDeck.add(i);
        }

        ownDeck = new ArrayList<>();
        for (int i : ownDeckDummy) {
            ownDeck.add(i);
        }

        enemyHand = new ArrayList<>();
        enemyBase = new ArrayList<>();

        ownHand = new ArrayList<>();
        ownBase = new ArrayList<>();

        //TODO: Manipulate Data
        Collections.shuffle(enemyDeck);
        Collections.shuffle(ownDeck);

        //TODO: Board Fragments
        Fragment newEnemyBaseFragment1 = new BaseFragment(1);
        Fragment newBoardFragment = new BoardFragment(BoardCells);
        Fragment newOwnBaseFragment2 = new BaseFragment(0);

        //TopFragment topFragment = new TopFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.enemy_base_fragment_container, newEnemyBaseFragment1)
                .commit();

        //MiddleFragment middleFragment = new MiddleFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.board_fragment_container, newBoardFragment)
                .commit();

        //BottomFragment bottomFragment = new BottomFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.own_base_fragment_container, newOwnBaseFragment2)
                .commit();

        loadJson();
        Log.e("Tag", "the loaded CardJsonArray is " + CardJsonArray);

        //TODO: Hand RecyclerViews
        //get user inventory, and link then with each cards detail object
        //Load Enemy Hand
        //loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand));
        //Load Own Hand
        //loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand));


        // Get a reference to the View
        View enemyDeckView = findViewById(R.id.enemy_deck);
        View ownDeckView = findViewById(R.id.own_deck);
        TextView enemyDeckCountView = findViewById(R.id.enemy_deck_count);
        TextView ownDeckCountView = findViewById(R.id.own_deck_count);
        Button turnEndButton = findViewById(R.id.turnEndButton);

        loadDeck(enemyDeck.size(), enemyDeckView, enemyDeckCountView);
        loadDeck(ownDeck.size(), ownDeckView, ownDeckCountView);


        //TODO: Deck Views

        // Set a click listener on enemyDeckView
        enemyDeckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enemyDeckDrawable > 0) {
                    enemyDeckDrawable--;
                    if (enemyDeckDrawable == 0) {
                        CardView enemyDeckColorView = findViewById(R.id.enemy_deck_color);
                        enemyDeckColorView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.teal_700));
                        enemyDeckColorView.invalidate();
                    }
                    EnemyDrawDeck(1, enemyHand);
                    loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand), true);
                }
            }
        });
        // Set a click listener on ownDeckView
        ownDeckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ownDeckDrawable > 0) {
                    ownDeckDrawable--;
                    if (ownDeckDrawable == 0) {
                        CardView ownDeckColorView = findViewById(R.id.own_deck_color);
                        ownDeckColorView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.teal_700));
                        ownDeckColorView.invalidate();
                    }
                    OwnDrawDeck(1, ownHand);
                    loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand), true);
                }
            }
        });
        // Set a click listener on turnEndButton
        turnEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnEnd();
            }
        });

        TextView enemyDataTextView = findViewById(R.id.enemyDataView);
        TextView ownDataTextView = findViewById(R.id.ownDataView);

        loadData(enemyMaxMana, enemyMaxMoves, enemyHand.size(), enemyGraveCount, enemyDataTextView);
        loadData(ownMaxMana, ownMaxMoves, ownHand.size(), ownGraveCount, ownDataTextView);

        //TODO: TURN 1
            //TODO: Draw 5 to Hand
                EnemyDrawDeck(5, enemyHand);
                OwnDrawDeck(5, ownHand);
                loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand), false);
                loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand), false);

            //TODO: Draw 5 to Base
                EnemyDrawDeck(5, enemyBase);
                OwnDrawDeck(5, ownBase);
                loadBase(ownBase, findViewById(R.id.own_base_fragment_container));
                loadBase(enemyBase, findViewById(R.id.enemy_base_fragment_container));

            //EndZone and enemy draw
            EndZone(1, enemyBase, findViewById(R.id.enemy_base_fragment_container));
    }

//TODO: Functions

    private void TurnEnd() {
        turn++;
        loadTurn(findViewById(R.id.turnDataView));
        Button turnEndButton = findViewById(R.id.turnEndButton);
        turnEndButton.setRotation((turn+1)%2*180);

        if (turn%2 == 1) {
            if (ownDeck.size() > 0) {
                ownDeckDrawable = 1;
                CardView ownDeckColorView = findViewById(R.id.own_deck_color);
                ownDeckColorView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
                ownDeckColorView.invalidate();
            }
        }
        else {
            if (enemyDeck.size() > 0) {
                enemyDeckDrawable = 1;
                CardView enemyDeckColorView = findViewById(R.id.enemy_deck_color);
                enemyDeckColorView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
                enemyDeckColorView.invalidate();
            }
        }
        enemyManaCount = enemyMaxMana;
        enemyMovesCount = enemyMaxMoves;
        ownManaCount = ownMaxMana;
        ownMovesCount = ownMaxMoves;
    }

    //TODO: Reach End Zone
    private void EndZone(int position, List<Integer> base, View baseView) {
        //Set Variables
            if (base == enemyBase) {
                ownConquers++;
                if(ownConquers == 3) {
                    OwnWin();
                }
                enemyMaxMana++;
                enemyManaCount = enemyMaxMana;
            }
            else {
                enemyConquers++;
                if(enemyConquers == 3) {
                    EnemyWin();
                }
                ownMaxMana++;
                ownManaCount = ownMaxMana;
            }

        //Opposing side Draw
            //Load Views
                View enemyDeckView = findViewById(R.id.enemy_deck);
                TextView enemyDeckCountView = findViewById(R.id.enemy_deck_count);
                View ownDeckView = findViewById(R.id.own_deck);
                TextView ownDeckCountView = findViewById(R.id.own_deck_count);
                TextView enemyDataTextView = findViewById(R.id.enemyDataView);
                TextView ownDataTextView = findViewById(R.id.ownDataView);
            int baseCardID = base.get(position);

            if (base == enemyBase) {
                enemyHand.add(baseCardID);
                loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand), true);
                loadData(enemyMaxMana, enemyMaxMoves, enemyHand.size(), enemyGraveCount, enemyDataTextView);
            }
            else {
                ownHand.add(baseCardID);
                loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand), true);
                loadData(ownMaxMana, ownMaxMoves, ownHand.size(), ownGraveCount, ownDataTextView);
            }

        //Set Base stuff
        base.set(position, -1);
        loadBase(base, baseView);
    }

    //TODO: Win, DO STUFF
    private void EnemyWin() {
    }

    private void OwnWin() {
    }

    //TODO: Enemy draw card to hand or base
    private void EnemyDrawDeck(int num, List<Integer> handOrBase) {
        View enemyDeckView = findViewById(R.id.enemy_deck);
        TextView enemyDeckCountView = findViewById(R.id.enemy_deck_count);
        for (int i=0; i<num; i++) {
            if (enemyDeck.size() > 0) {
                int topCardID = enemyDeck.remove(0).intValue();
                handOrBase.add(topCardID);
                loadDeck(enemyDeck.size(), enemyDeckView, enemyDeckCountView);
                //Toast.makeText(getApplicationContext(), "Card " + String.valueOf(topCardID), Toast.LENGTH_SHORT).show();
            }
        }
        if (enemyDeck.size()<=0) {
            CardView enemyDeckColorView = findViewById(R.id.enemy_deck_color);
            enemyDeckColorView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.Grey));
            enemyDeckColorView.invalidate();
        }
    }

    //TODO: Own draw card to hand or base
    private void OwnDrawDeck(int num, List<Integer> handOrBase) {
        View ownDeckView = findViewById(R.id.own_deck);
        TextView ownDeckCountView = findViewById(R.id.own_deck_count);
        for (int i=0; i<num; i++) {
            if (ownDeck.size() > 0) {
                int topCardID = ownDeck.remove(0).intValue();
                handOrBase.add(topCardID);
                loadDeck(ownDeck.size(), ownDeckView, ownDeckCountView);
                //Toast.makeText(getApplicationContext(), "Card "+ String.valueOf(topCardID), Toast.LENGTH_SHORT).show();
            }
        }
        if (ownDeck.size()<=0) {
            CardView ownDeckColorView = findViewById(R.id.own_deck_color);
            ownDeckColorView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.Grey));
            ownDeckColorView.invalidate();
        }
    }

    //TODO: Turn Number
    private void loadTurn(TextView turnTextView) {
        turnTextView.setText("\nTurn\n"+String.valueOf(turn));
        turnTextView.invalidate();
    }

    //TODO: Deck size and Deck Image
    private void loadDeck(int deckSize, View deckView, TextView deckTextView) {
        deckTextView.setText(String.valueOf(deckSize));
        deckView.invalidate();
    }

    //TODO: Player text data
    private void loadData(int manaCount, int movesCount, int handCount, int graveCount, TextView dataTextView) {
        dataTextView.setText("Mana:   "+String.valueOf(manaCount)+"\nMoves:  "+String.valueOf(movesCount)+"\nHand:   "+String.valueOf(handCount)+"\nGrave:  "+String.valueOf(graveCount));
        dataTextView.invalidate();
    }

    //TODO: Hand info
    private void loadHand(List<Integer> handInventory, RecyclerView handView, boolean scrollToLastPosBool) {
        InventoryJsonArray = new JSONArray();
        int[] handIntArray = new int[handInventory.size()];
        for (int i = 0; i < handInventory.size(); i++) {
            handIntArray[i] = handInventory.get(i);
        }
        inventoryJson(handIntArray);
        Log.e("Tag", "the combined InventoryJsonArray is " + InventoryJsonArray);

        //create recyclerview
        mRecyclerView = handView;
        //To create fragment
        mAdapter = new CardListAdapterBoard(this, InventoryJsonArray); //, getSupportFragmentManager(), 1
        // Connect the adapter with the RecyclerView
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (scrollToLastPosBool) {
            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        }
        Log.e("Tag", "On create run");

        TextView enemyDataTextView = findViewById(R.id.enemyDataView);
        TextView ownDataTextView = findViewById(R.id.ownDataView);
        loadData(enemyManaCount, enemyMovesCount, enemyHand.size(), enemyGraveCount, enemyDataTextView);
        loadData(ownManaCount, ownMovesCount, ownHand.size(), ownGraveCount, ownDataTextView);
    }

    //TODO: Base info
    private void loadBase(List<Integer> Base, View baseView) {
        baseView.invalidate();
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