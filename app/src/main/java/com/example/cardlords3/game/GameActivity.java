package com.example.cardlords3.game;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cardlords3.R;
import com.example.cardlords3.game.main.BoardFragment;

public class GameActivity extends AppCompatActivity {

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
    }
}