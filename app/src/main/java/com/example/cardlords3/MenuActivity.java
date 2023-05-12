package com.example.cardlords3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cardlords3.ui.login.GameActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    //when click pve mode
    public void pve_launch(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, GameActivity.class); //Set target activity
        startActivity(intent);
    }

    //when click pvp mode
    public void pvp_launch(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, GameActivity.class); //Set target activity
        startActivity(intent);
    }

    //when click CardDeck
    public void GoCardDeck(View view) {
        Intent intent = new Intent(this, CardDeckActivity.class); //Set target activity
        startActivity(intent);
    }

    //when click CardEditor
    public void GoCardEditor(View view) {
        Intent intent = new Intent(this, CardEditorActivity.class); //Set target activity
        startActivity(intent);
    }


    //when click logout
    public void logout(View view) {
        super.onBackPressed();
    }

}