package com.example.cardlords3.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardlords3.R;
import com.example.cardlords3.game.main.BaseFragment;
import com.example.cardlords3.game.main.BoardAdapter;
import com.example.cardlords3.game.main.BoardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameActivity extends AppCompatActivity implements CardListAdapterBoard.OnItemClickListener, BoardAdapter.OnItemClickListener {
    private int hand_Position = -1;
    private int hand_side = -1;
    private int hand_id = -1;
    private int hand_cost = 0;
    private int hand_placeType = -1;

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

    private boolean menuOpen = false;
    private boolean movable = false;
    private boolean attackable = false;
    private boolean skillable = false;

    public class Cell {
        public int owner;  //0=Own, 1=Enemy
        public int cellID;
        public int movable;
        public int skill_usable;
        public int effect1;
        public int effect2;
        public int effect3;

        public Cell() {
            owner = -1;  //0=Own, 1=Enemy
            cellID = -1;    //-1 means EMPTY
            movable = 0;    //Number of Moves left
            skill_usable = 0;   //Is skill still Active
            effect1 = 0;
            effect2 = 0;
            effect3 = 0;
        }
    }

    Cell[][] BoardCells = new Cell[5][5];
    public static boolean[][] clickable = new boolean[5][5];

    Fragment newEnemyBaseFragment1 = new BaseFragment(1);
    Fragment newBoardFragment = new BoardFragment(BoardCells, this);
    Fragment newOwnBaseFragment2 = new BaseFragment(0);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //TODO: Create Data
        fetchAndSaveFirestoreData();
        turn = 1;
        int[] enemyDeckDummy = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
        int[] ownDeckDummy = new int[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4};

        enemyDeck = new ArrayList<>();
        ownDeck = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                BoardCells[i][j] = new Cell();
                BoardCells[i][j].cellID = -1;
            }
        }

        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                clickable[i][j] = false;
            }
        }
        //Log.e("BoardCells[2][3]", String.valueOf(BoardCells[2][3].cellID));
        //Array to List & Vice Versa
        /*
        List<Integer> intList = Arrays.asList(intArray);
        intList.toArray(intArray);
        */
        loadOwnDeck(new OnDeckLoadedCallback() {
            @Override
            public void onDeckLoaded(List<Long> deck) {
                for (Long i : deck) {
                    ownDeck.add(i.intValue());
                    enemyDeck.add(i.intValue());
                }

                // Shuffle the deck
                Collections.shuffle(ownDeck);
                Collections.shuffle(enemyDeck);

                Log.e("OwnDeck2", ownDeck.toString());
                Log.e("EnemyDeck", enemyDeck.toString());

                enemyHand = new ArrayList<>();
                enemyBase = new ArrayList<>();

                ownHand = new ArrayList<>();
                ownBase = new ArrayList<>();

                // Rest of the dependent code...

                //TODO: Board Fragments
        /*
        Fragment newEnemyBaseFragment1 = new BaseFragment(1);
        Fragment newBoardFragment = new BoardFragment(BoardCells, this);
        Fragment newOwnBaseFragment2 = new BaseFragment(0);
        */

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

                //TODO: Get a references to Views
                View enemyDeckView = findViewById(R.id.enemy_deck);
                View ownDeckView = findViewById(R.id.own_deck);
                TextView enemyDeckCountView = findViewById(R.id.enemy_deck_count);
                TextView ownDeckCountView = findViewById(R.id.own_deck_count);
                Button turnEndButton = findViewById(R.id.turnEndButton);

                TextView closeCardInfoButton = findViewById(R.id.close_button);
                Button moveButton = findViewById(R.id.move_button);
                Button attackButton = findViewById(R.id.attack_button);
                Button skillButton = findViewById(R.id.skill_button);
                Button informationButton = findViewById(R.id.info_button);

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
                            EnemyDrawDeck(1, enemyHand, true);
                            loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand), true, 1, hand_Position, hand_side, 1);
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
                            OwnDrawDeck(1, ownHand, true);
                            loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand), true, 0, hand_Position, hand_side, 1);
                        }
                    }
                });
                //TODO -  Set a click listener on turnEndButton
                turnEndButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TurnEnd();
                        loadBoard();
                    }
                });
                //TODO -  Set a click listener on CloseCardInfoButton
                closeCardInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (menuOpen) {
                            CloseOptions();
                        }
                    }
                });
                //TODO -  Set a click listener on MoveButton
                moveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (movable) {
                            CloseOptions();
                        }
                    }
                });
                //TODO -  Set a click listener on AttackButton
                attackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (attackable) {
                            CloseOptions();
                        }
                    }
                });
                //TODO -  Set a click listener on SkillButton
                skillButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (skillable) {
                            CloseOptions();
                        }
                    }
                });
                //TODO -  Set a click listener on InformationButton
                informationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (menuOpen) {
                            CloseOptions();
                        }
                    }
                });

                TextView enemyDataTextView = findViewById(R.id.enemyDataView);
                TextView ownDataTextView = findViewById(R.id.ownDataView);

                loadData(enemyMaxMana, enemyMaxMoves, enemyHand.size(), enemyGraveCount, enemyDataTextView);
                loadData(ownMaxMana, ownMaxMoves, ownHand.size(), ownGraveCount, ownDataTextView);

                //TODO: TURN 1
                //TODO: Draw 5 to Hand
                EnemyDrawDeck(5, enemyHand, false);
                OwnDrawDeck(5, ownHand, false);
                loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand), false, 1, hand_Position, hand_side, 1);
                loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand), false, 0, hand_Position, hand_side, 1);

                //TODO: Draw 5 to Base
                EnemyDrawDeck(5, enemyBase, false);
                OwnDrawDeck(5, ownBase, false);
                loadBase(ownBase, findViewById(R.id.own_base_fragment_container));
                loadBase(enemyBase, findViewById(R.id.enemy_base_fragment_container));

                //EndZone and enemy draw
                //EndZone(1, enemyBase, findViewById(R.id.enemy_base_fragment_container));
            }
        });

    }

//TODO: Functions

    //TODO:
    private void loadBoard() {
        //BoardFragment boardFragment = (BoardFragment) getSupportFragmentManager().findFragmentById(R.id.board_fragment_container);
        ((BoardFragment)newBoardFragment).reloadData(BoardCells);
        /*
        View boardRecyclerView = newBoardFragment.getView().findViewById(R.id.board_recycler_view);
        boardRecyclerView.invalidate();
        */

        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                Log.e("BoardCells["+String.valueOf(i)+"]["+String.valueOf(j)+"].cellID", String.valueOf(BoardCells[i][j].cellID));
            }
        }

        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                Log.e("clickable["+String.valueOf(i)+"]["+String.valueOf(j)+"]", String.valueOf(clickable[i][j]));
            }
        }
    }
    /*
    //TODO:
    public static void loadBoardGlobal(GameActivity gameActivity) {
        View boardRecyclerView = gameActivity.findViewById(R.id.board_fragment_container);
        boardRecyclerView.invalidate();
    }
    */

    //TODO: Auto Move Up one
    private void AutoUp(int side) {
        if (side == 0) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    Log.e("TESTTEST",String.valueOf(i)+","+String.valueOf(j));
                    if (BoardCells[i][j].owner == side) {
                        if (i>0) {
                            if (BoardCells[i-1][j].cellID == -1)
                                //Move 1 up
                                Move(i,j, i-1,j);
                        } else {
                            //Move to Base
                            //if (enemyBase.get(j) != -1) {
                            Destroy(i, j);
                            EndZone(j, enemyBase, findViewById(R.id.enemy_base_fragment_container));
                            //}
                        }
                    }
                }
            }
        }
        else {
            for (int i = 4; i >= 0; i--) {
                for (int j = 0; j < 5; j++) {
                    Log.e("TESTTEST",String.valueOf(i)+","+String.valueOf(j));
                    if (BoardCells[i][j].owner == side) {
                        if (i<4) {
                            if (BoardCells[i+1][j].cellID == -1)
                                //Move 1 down
                                Move(i,j, i+1,j);
                        } else {
                            //Move to Base
                            //if (enemyBase.get(j) != -1) {
                                Destroy(i,j);
                                EndZone(j, ownBase, findViewById(R.id.own_base_fragment_container));
                            //}
                        }
                    }
                }
            }
        }
    }

    private void Destroy(int i1, int j1) {
        //Log.e("Game Over", "");
        BoardCells[i1][j1] = new Cell();
        BoardCells[i1][j1].cellID = -1;
    }

    private void Move(int i1, int j1, int i2, int j2) {
        BoardCells[i2][j2] = BoardCells[i1][j1];
        BoardCells[i1][j1] = new Cell();
        BoardCells[i1][i1].cellID = -1;
    }

    private void TurnEnd() {
        turn++;
        AutoUp(turn%2);
        loadTurn(findViewById(R.id.turnDataView));
        Button turnEndButton = findViewById(R.id.turnEndButton);
        turnEndButton.setRotation((turn+1)%2*180);

        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                clickable[i][j] = false;
            }
        }

        CellsToJSON();
        int type = -1;
        String checkTypes = "";
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                BoardCells[i][j].movable = 0;
                type = -1;
                if ((turn+1)%2 == BoardCells[i][j].owner) {
                    try {
                        type = CardJsonArray.getJSONObject(5 * i + j).getInt("typeID");
                        Log.e("boardCellType", String.valueOf(type));
                        if (type == 1) {
                            BoardCells[i][j].movable = 1;
                        } else if (type == 2) {
                            BoardCells[i][j].movable = 2;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                checkTypes += String.valueOf(type);
            }
        }
        //TODO - DEBUG THIS
        Log.e("CheckTypes", checkTypes);


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

        //Reset selected
        ResetSelected(true);
    }

    private void ResetSelected(boolean loadBoardFlag) {
        hand_Position = -1;
        hand_side = -1;
        hand_id = -1;
        hand_cost = 0;
        hand_placeType = -1;
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                clickable[i][j] = false;
            }
        }
        if (loadBoardFlag)
            loadBoard();
        loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand), false, 0, hand_Position, hand_side, 1);
        loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand), false, 1, hand_Position, hand_side, 1);
    }

    //TODO: Reach End Zone
    private void EndZone(int position, List<Integer> base, View baseView) {
        //Set Variables
            if (base == enemyBase) {
                ownConquers++;
                if(ownConquers >= 3) {
                    OwnWin();
                }
                enemyMaxMana++;
                enemyManaCount = enemyMaxMana;
            }
            else {
                enemyConquers++;
                if(enemyConquers >= 3) {
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
                loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand), true, 1, hand_Position, hand_side, 1);
                loadData(enemyMaxMana, enemyMaxMoves, enemyHand.size(), enemyGraveCount, enemyDataTextView);
            }
            else {
                ownHand.add(baseCardID);
                loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand), true, 0, hand_Position, hand_side, 1);
                loadData(ownMaxMana, ownMaxMoves, ownHand.size(), ownGraveCount, ownDataTextView);
            }

        //Set Base stuff
        base.set(position, -1);
        loadBase(base, baseView);
    }

    //TODO: Win, DO STUFF
    private void EnemyWin() {
        Toast toast = Toast.makeText(this, "Player 2 Wins!", Toast.LENGTH_LONG);
        toast.show();
    }

    private void OwnWin() {
        Toast toast = Toast.makeText(this, "Player 1 Wins!", Toast.LENGTH_LONG);
        toast.show();
    }

    //TODO: Enemy draw card to hand or base
    private void EnemyDrawDeck(int num, List<Integer> handOrBase, boolean loadBoardAfter) {
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

        //Reset selected
        ResetSelected(loadBoardAfter);
    }

    //TODO: Own draw card to hand or base
    private void OwnDrawDeck(int num, List<Integer> handOrBase, boolean loadBoardAfter) {
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

        //Reset selected
        ResetSelected(loadBoardAfter);
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
    private void loadHand(List<Integer> handInventory, RecyclerView handView, boolean scrollToLastPosBool, int side, int hand_Position, int hand_side, int playable) {
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
        mAdapter = new CardListAdapterBoard(this, InventoryJsonArray, side, this, hand_Position, hand_side, playable); //, getSupportFragmentManager(), 1
        // Connect the adapter with the RecyclerView

        mAdapter.setOnItemClickListener(this);
        //mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (scrollToLastPosBool) {
            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        }
        else {
            if (hand_Position != -1)
                mRecyclerView.scrollToPosition(hand_Position);
        }
        Log.e("Tag", "On create run");

        TextView enemyDataTextView = findViewById(R.id.enemyDataView);
        TextView ownDataTextView = findViewById(R.id.ownDataView);
        loadData(enemyManaCount, enemyMovesCount, enemyHand.size(), enemyGraveCount, enemyDataTextView);
        loadData(ownManaCount, ownMovesCount, ownHand.size(), ownGraveCount, ownDataTextView);
    }

    //=================test===================
    @Override
    public void onHandCardClick(int position, int side, int id, int cost, int placeType) {
        int currentMana = 0;
        if (turn%2 == 1) { //Own turn
            currentMana = ownManaCount;
        }
        else {
            currentMana = enemyManaCount;
        }

        if ((turn+1)%2 == side) {   //Make sure it's the correct player clicking
            // Do something with the data
            Toast t = Toast.makeText(this, "Position: " + position + ",\nid is: " + id + ",\nCost is: " + cost + ",\nSide is: " + side + ",\nPlaceType is: " + placeType, Toast.LENGTH_SHORT);
            t.show();

            int playable = 2;

            if (cost <= currentMana) {
                //Playable
                hand_Position = position;
                hand_side = side;
                hand_id = id;
                hand_cost = cost;
                hand_placeType = placeType;
                //TODO - Do something on board
                PreparePlace();

                playable = 1;
            }
            else {
                //Not playable
                hand_Position = position;
                hand_side = side;
                hand_id = -1;
                hand_cost = 99;
                hand_placeType = -1;
                for (int i=0; i<5; i++) {
                    for (int j=0; j<5; j++) {
                        clickable[i][j] = false;
                    }
                }
                loadBoard();
                playable = 0;
            }

            if (side == 0)
                loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand), false, 0, hand_Position, hand_side, playable);
            else
                loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand), false, 1, hand_Position, hand_side, playable);
        }
    }
    //=================test===================
    @Override
    public void onBoardCellClick(int row, int col) {
        // Do something with the data
        Toast t = Toast.makeText(this, "row: " + row + ",\ncol: " + col, Toast.LENGTH_SHORT);
        t.show();

        if (clickable[row][col]) {  //To place or use a card
            Log.e("THIS IS CLICKABLE", "true");
            if (hand_id != -1) {    //Card selected

                //TODO - Use card
                if (hand_placeType == 0 || hand_placeType == 6) {
                    //It's a Soldier!
                    //TODO - Place card to board
                    BoardCells[row][col].owner = hand_side;
                    BoardCells[row][col].cellID = hand_id;
                    CellsToJSON();
                    int skill = -1;
                    try {
                        skill = CardJsonArray.getJSONObject(5 * row + col).getInt("skillID");
                        if (skill > 0) {
                            if(CheckIfSkillIs_ActiveSkill(skill))
                                BoardCells[row][col].skill_usable = 1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    //It's a Magic!
                    //TODO - Do Magic Stuff
                    switch(hand_id) {
                        default:
                            break;
                    }
                }


                if (hand_side == 0) {
                    //OWN
                    //TODO - Subtract mana
                    ownManaCount -= hand_cost;
                    //TODO - Remove card from hand
                    ownHand.remove(hand_Position);
                    //TODO - Load board and hand and data
                    loadHand(ownHand, findViewById(R.id.cardRecyclerViewOwnHand), false, 0, hand_Position, hand_side, 2);
                    ResetSelected(true);

                    TextView ownDataTextView = findViewById(R.id.ownDataView);
                    loadData(ownManaCount, ownMovesCount, ownHand.size(), ownGraveCount, ownDataTextView);

                } else {
                    //ENEMY
                    //TODO - Subtract mana
                    enemyManaCount -= hand_cost;
                    //TODO - Remove card from hand
                    enemyHand.remove(hand_Position);
                    //TODO - Load board and hand and data
                    loadHand(enemyHand, findViewById(R.id.cardRecyclerViewEnemyHand), false, 1, hand_Position, hand_side, 2);
                    ResetSelected(true);

                    TextView enemyDataTextView = findViewById(R.id.enemyDataView);
                    loadData(enemyManaCount, enemyMovesCount, enemyHand.size(), enemyGraveCount, enemyDataTextView);
                }
            }
        } else {
            //TODO: Trying to tap on a card
            if (BoardCells[row][col].cellID != -1) {    //Check if card exists
                menuOpen = true;
                movable = false;
                attackable = false;
                skillable = false;

                if((turn+1)%2 == BoardCells[row][col].owner) {
                    if (BoardCells[row][col].movable > 0) {
                        if (row > 0 && BoardCells[row - 1][col].cellID == -1)
                            movable = true;
                        if (col > 0 && BoardCells[row][col - 1].cellID == -1)
                            movable = true;
                        if (row < 4 && BoardCells[row + 1][col].cellID == -1)
                            movable = true;
                        if (col < 4 && BoardCells[row][col + 1].cellID == -1)
                            movable = true;

                        if (row > 0 && BoardCells[row - 1][col].cellID != -1 && BoardCells[row][col].owner != BoardCells[row - 1][col].owner)
                            movable = true;
                        if (col > 0 && BoardCells[row][col - 1].cellID != -1 && BoardCells[row][col].owner != BoardCells[row][col - 1].owner)
                            movable = true;
                        if (row < 4 && BoardCells[row + 1][col].cellID != -1 && BoardCells[row][col].owner != BoardCells[row + 1][col].owner)
                            movable = true;
                        if (col < 4 && BoardCells[row][col + 1].cellID != -1 && BoardCells[row][col].owner != BoardCells[row][col + 1].owner)
                            movable = true;
                    }
                    if (BoardCells[row][col].skill_usable > 0)
                        skillable = true;
                }

                if(movable)
                    ((Button) findViewById(R.id.move_button)).setEnabled(true);
                if(attackable)
                    ((Button) findViewById(R.id.attack_button)).setEnabled(true);
                if(skillable)
                    ((Button) findViewById(R.id.skill_button)).setEnabled(true);
                ((Button) findViewById(R.id.info_button)).setEnabled(true);

                ((Button) findViewById(R.id.move_button)).setBackgroundColor(Color.rgb(0, 180, 30));
                ((Button) findViewById(R.id.attack_button)).setBackgroundColor(Color.rgb(230, 0, 0));
                ((Button) findViewById(R.id.skill_button)).setBackgroundColor(Color.rgb(130, 0, 130));
                ((Button) findViewById(R.id.info_button)).setBackgroundColor(Color.rgb(30, 30, 30));
                ((TextView) findViewById(R.id.close_button)).setBackgroundColor(Color.rgb(180, 0, 0));

                ((LinearLayout) findViewById(R.id.cell_choices)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.cell_choices)).invalidate();
                //TODO: Move/Attack/Skill
            }
        }

    }

    private boolean CheckIfSkillIs_ActiveSkill(int skill) {
        //If is an active skill, set to true
        return false;   //passive skill
    }
    //=================test===================

    private void CloseOptions() {
        menuOpen = false;
        movable = false;
        attackable = false;
        skillable = false;
        ((LinearLayout) findViewById(R.id.cell_choices)).setVisibility(View.INVISIBLE);
        ((Button) findViewById(R.id.move_button)).setEnabled(false);
        ((Button) findViewById(R.id.attack_button)).setEnabled(false);
        ((Button) findViewById(R.id.skill_button)).setEnabled(false);
        ((Button) findViewById(R.id.info_button)).setEnabled(false);

        ((LinearLayout) findViewById(R.id.cell_choices)).invalidate();
    }

    private void PreparePlace() {
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                clickable[i][j] = false;
            }
        }
        switch(hand_placeType) {
            case 0:
                // 0 normal_soldier
                //TODO: Make all empty back-rank glow and clickable
                if (hand_side == 0) {    //own
                    for(int i=0; i<5; i++) {
                        if (BoardCells[4][i].cellID == -1) {
                            GameActivity.clickable[4][i] = true;
                            Log.e("Clickable Pos", String.valueOf(i));
                        }
                    }
                }
                else {    //enemy
                    for(int i=0; i<5; i++) {
                        if (BoardCells[0][i].cellID == -1) {
                            GameActivity.clickable[0][i] = true;
                            Log.e("Clickable Pos", String.valueOf(i));
                        }
                    }
                }
                break;
            case 1:
                // 1 magic_self_card
                //TODO: Make all owner's cards glow and clickable
                for(int i=0; i<5; i++) {
                    for(int j=0; j<5; j++) {
                        if (BoardCells[i][j].owner == hand_side)
                            GameActivity.clickable[i][j] = true;
                    }
                }
                break;
            case 2:
                // 2 magic_anywhere
                //TODO: Make all cells glow and clickable
                for(int i=0; i<5; i++) {
                    for(int j=0; j<5; j++) {
                        GameActivity.clickable[i][j] = true;
                    }
                }
                break;
            case 3:
                // 3 magic_enemy_card
                //TODO: Make all enemy's glow and clickable
                for(int i=0; i<5; i++) {
                    for(int j=0; j<5; j++) {
                        if (BoardCells[i][j].owner + hand_side == 1)
                            GameActivity.clickable[i][j] = true;
                    }
                }
                break;
            case 4:
                // 4 magic_all_empty
                //TODO: Make all EMPTY cells glow and clickable
                for(int i=0; i<5; i++) {
                    for(int j=0; j<5; j++) {
                        if (BoardCells[i][j].cellID == -1)
                            GameActivity.clickable[i][j] = true;
                    }
                }
                break;
            case 5:
            case 6:
                // 6 anywhere_soldier_except_last_line
                // 5 magic_all_empty_except_last_line
                //TODO: Make all EMPTY cells glow and clickable except last line
                if (hand_side == 0) {   //own
                    for (int i = 1; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (BoardCells[i][j].cellID == -1)
                                GameActivity.clickable[i][j] = true;
                        }
                    }
                }
                else {  //enemy
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (BoardCells[i][j].cellID == -1)
                                GameActivity.clickable[i][j] = true;
                        }
                    }
                }
                break;
            default:
                break;
        }
        loadBoard();
    }

    //TODO: Base info
    private void loadBase(List<Integer> Base, View baseView) {
        /*
        ((BaseFragment)newEnemyBaseFragment1).SendBase(Base);
        ((BaseFragment)newOwnBaseFragment2).SendBase(Base);
        */
        baseView.invalidate();
    }

    private void loadJson(){
        try{
            //load json
            InputStream inputStream = openFileInput("cardlist.json");
            int size = inputStream.available();
            byte[] buffer=new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            //now we will fetch json file
            String json;
            int max;

            json = new String(buffer, StandardCharsets.UTF_8);
            //JSONObject jsonObject = new JSONObject(json);
            //Log.e("Tag", "the json is " + jsonObject);
            CardJsonArray = new JSONArray(json);
            Log.e("Tag", "the jsonArray is " + CardJsonArray);
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

    private void CellsToJSON() {
        InventoryJsonArray = new JSONArray();
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
    }

    private void fetchAndSaveFirestoreData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CardDB").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    JSONArray jsonArray = new JSONArray();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        JSONObject jsonObjectItem = new JSONObject(document.getData());
                        jsonArray.put(jsonObjectItem);
                    }
                    String jsonString = jsonArray.toString();
                    saveDataToLocalFile(jsonString);
                    Log.e("SaveData", jsonString);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void saveDataToLocalFile(String data) {
        String filename = "cardlist.json";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadOwnDeck(OnDeckLoadedCallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(uid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<Long> deck = (List<Long>) document.get("inventory");
                            callback.onDeckLoaded(deck);
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

    private interface OnDeckLoadedCallback {
        void onDeckLoaded(List<Long> deck);
    }
}