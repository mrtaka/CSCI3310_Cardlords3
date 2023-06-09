package com.example.cardlords3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Toast;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CardCreationActivity extends AppCompatActivity {

    JSONObject itemJson;
    Integer position;
    Integer cardID = 0;
    String card_name = "Soldier1";
    String image_name = "image01.jpg";
    Integer cost = 5;
    Integer attack = 3;
    Integer health = 10;
    Integer rarity = 3;
    Integer typeID = 1;
    Integer skillID = 1;
    Integer raceID = 1;

    //default is create card, but true will become edit card
    boolean editCard = false;

    private EditText CardNameEditText;
    private EditText CostEditText;
    private EditText AttackEditText;
    private EditText HealthEditText;
    private TextView RarityTextItem;
    private TextView TypeTextItem;
    private TextView SkillTextItem;
    private TextView RaceTextItem;
    private Spinner image_selection;


    ImageView CardImageItemView;
    TextView CardName;
    TextView CardCost;
    TextView CardHealth;
    TextView CardAttack;
    TextView CardType;
    TextView CardRace;
    RatingBar CardRarityBar;

    Button FinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_creation);

        getIncomeIntent();

        //edit the button (create/edit)
        FinishButton = this.findViewById(R.id.finish_button);
        if(editCard==true){
            FinishButton.setText("Edit this Card");
        }

        //get all field from card
        CardImageItemView = this.findViewById(R.id.card_image);
        CardRarityBar = this.findViewById(R.id.starsBar);
        CardName = this.findViewById(R.id.card_name_textview);
        CardCost = this.findViewById(R.id.cost_textview);
        CardHealth = this.findViewById(R.id.health_textview);
        CardAttack = this.findViewById(R.id.attack_textview);
        CardType = this.findViewById(R.id.type_textview);
        CardRace = this.findViewById(R.id.race_textview);


        //get all field from editor
        CardNameEditText = findViewById(R.id.edit_name);
        changeCardName(card_name);
        CardNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                changeCardName(CardNameEditText.getText().toString());
            }});

        CostEditText = findViewById(R.id.editCost);
        changeCost(cost.toString());
        CostEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                changeCost(CostEditText.getText().toString());
        }});

        AttackEditText = findViewById(R.id.editAttack);
        changeAttack(attack.toString());
        AttackEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                changeAttack(AttackEditText.getText().toString());
            }});

        HealthEditText = findViewById(R.id.editHealth);
        changeHealth(health.toString());
        HealthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                changeHealth(HealthEditText.getText().toString());
            }});
        RarityTextItem = (TextView) findViewById(R.id.textRarity);
        changeRarity(rarity);
        TypeTextItem = (TextView) findViewById(R.id.textType);
        changeType(typeID);
        SkillTextItem = (TextView) findViewById(R.id.textSkill);
        changeSkill(skillID);
        RaceTextItem = (TextView) findViewById(R.id.textRace);
        changeRace(raceID);
        image_selection = findViewById(R.id.selection_images);

        image_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Do something when an item is selected
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                changeImage(selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        changeImage(image_name);

        //set default value of EditText and image
        CardNameEditText.setText(card_name);
        CostEditText.setText(String.valueOf(cost));
        AttackEditText.setText(String.valueOf(attack));
        HealthEditText.setText(String.valueOf(health));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.selection_images));
        image_selection.setAdapter(adapter);
        image_selection.setSelection(adapter.getPosition(image_name));
    }

    private void getIncomeIntent(){
        if(getIntent().hasExtra("editCard")){
            editCard = getIntent().getBooleanExtra("editCard", true);
            cardID = getIntent().getIntExtra("cardID", 1);
            card_name = getIntent().getStringExtra("name");
            //image_name = "image07.jpg";
            Log.d("tag", "The input image is : "+image_name);
            image_name = getIntent().getStringExtra("card_Image");
            Log.d("tag", "The input image is : "+image_name);
            cost = getIntent().getIntExtra("cost", 1);
            attack = getIntent().getIntExtra("attack", 1);
            health = getIntent().getIntExtra("health", 1);
            rarity = getIntent().getIntExtra("rarity", 1);
            typeID = getIntent().getIntExtra("typeID", 1);
            skillID = getIntent().getIntExtra("skillID", 1);
            raceID = getIntent().getIntExtra("raceID", 1);
        }
        else{
            Log.e("TAG", "No Intent passes! ");
        }
    }

    public void changeCardName(String input){
        CardName.setText(input);
        card_name = input;
        Log.e("TAG", "card_name set to: " + card_name);
    }

    public void changeCost(String input){
        Log.e("TAG", "input " + input);
        if(input.length() != 0){
            CardCost.setText(input);
            cost = Integer.parseInt(input);
            Log.e("TAG", "cost set to: " + cost);
        }
    }

    public void changeAttack(String input){
        if(input.length() != 0) {
            CardAttack.setText(input);
            attack = Integer.parseInt(input);
            Log.e("TAG", "attack set to: " + attack);
        }
    }

    public void changeHealth(String input){
        if(input.length() != 0) {
            CardHealth.setText(input);
            health = Integer.parseInt(input);
            Log.e("TAG", "health set to: " + health);
        }
    }

    public void changeImage(String mImagePath){
        image_name = mImagePath;
        if(mImagePath.length() != 0) {
            Log.e("TAG", "set the image : "+ mImagePath);
            mImagePath = mImagePath.replaceFirst("[.][^.]+$", "");
            Uri uri = Uri.parse("android.resource://com.example.cardlords3/drawable/" + mImagePath);
            CardImageItemView.setImageURI(uri);
        }
    }

    public void minusRarity(View view) {
        if(rarity>3){
            rarity = rarity - 1;
        }
        changeRarity(rarity);
        Log.e("TAG", "rarity minus set to: " + rarity);
    }

    public void plusRarity(View view) {
        if(rarity<5){
            rarity = rarity + 1;
        }
        changeRarity(rarity);
        Log.e("TAG", "rarity plus set to: " + rarity);
    }

    public void changeRarity(int input_rarity) {
        if(input_rarity == 3){
            RarityTextItem.setText("3-Stars");
            CardRarityBar.setRating(input_rarity);
        }else if(input_rarity == 4){
            RarityTextItem.setText("4-Stars");
            CardRarityBar.setRating(input_rarity);
        }else if(input_rarity == 5){
            RarityTextItem.setText("5-Stars");
            CardRarityBar.setRating(input_rarity);
        }else{
            RarityTextItem.setText("Error");
            CardRarityBar.setRating(1);
        }
    }

    public void minusType(View view) {
        if(typeID>1){
            typeID = typeID - 1;
        }
        changeType(typeID);
        Log.e("TAG", "typeID minus set to: " + typeID);
    }

    public void plusType(View view) {
        if(typeID<3){
            typeID = typeID + 1;
        }
        changeType(typeID);
        Log.e("TAG", "typeID plus set to: " + typeID);
    }

    public void changeType(int input) {
        if(input == 1){
            TypeTextItem.setText("士兵");
            CardType.setText("士");
        }else if(input == 2){
            TypeTextItem.setText("將軍");
            CardType.setText("將");
        }else if(input == 3){
            TypeTextItem.setText("魔法");
            CardType.setText("魔");
        }else{
            TypeTextItem.setText("Error");
            CardType.setText("E");
        }
    }

    public void minusSkill(View view) {
        if(skillID>1){
            skillID = skillID - 1;
        }
        changeSkill(skillID);
        Log.e("TAG", "skillID minus set to: " + skillID);
    }

    public void plusSkill(View view) {
        if(skillID<3){
            skillID = skillID + 1;
        }
        changeSkill(skillID);
        Log.e("TAG", "skillID plus set to: " + skillID);
    }

    public void changeSkill(int input) {
        if(input == 1){
            SkillTextItem.setText("Skill1");
        }else if(input == 2){
            SkillTextItem.setText("Skill2");
        }else if(input == 3){
            SkillTextItem.setText("Skill3");
        }else{
            SkillTextItem.setText("Error");
        }
    }

    public void minusRace(View view) {
        if(raceID>1){
            raceID = raceID - 1;
        }
        changeRace(raceID);
        Log.e("TAG", "raceID minus set to: " + raceID);
    }

    public void plusRace(View view) {
        if(raceID<3){
            raceID = raceID + 1;
        }
        changeRace(raceID);
        Log.e("TAG", "raceID plus set to: " + raceID);
    }

    public void changeRace(int input) {
        if(input == 1){
            RaceTextItem.setText("Race1");
            CardRace.setText("Race1");
        }else if(input == 2){
            RaceTextItem.setText("Race2");
            CardRace.setText("Race2");
        }else if(input == 3){
            RaceTextItem.setText("Race3");
            CardRace.setText("Race3");
        }else{
            RaceTextItem.setText("Error");
            CardRace.setText("Error");
        }
    }

    public void createCardToDB(View view) {

        if(editCard==true){//edit the info of this card to DB
            //Log.e("EditCard", cardID +", " + card_name +", " + typeID +", " + raceID +", " +
                   // + attack +", " + skillID +", " + image_name +", " + cost +", " + rarity);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("CardDB")
                    .whereEqualTo("cardID", cardID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Once we've found the document, update it with the new data
                                    Map<String, Object> card = new HashMap<>();
                                    card.put("cardID", cardID);
                                    card.put("card_name", card_name);
                                    card.put("typeID", typeID);
                                    card.put("raceID", raceID);
                                    card.put("health", health);
                                    card.put("attack", attack);
                                    card.put("skillID", skillID);
                                    card.put("card_Image", image_name);
                                    card.put("cost", cost);
                                    card.put("rarity", rarity);
                                    db.collection("CardDB").document(document.getId())
                                            .set(card, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("EditCard", "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("EditCard", "Error updating document", e);
                                                }
                                            });
                                }
                            } else {
                                Log.d("EditCard", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            Toast t = Toast.makeText(this, "Successful edit card (ID:" +cardID+ ")", Toast.LENGTH_SHORT);
            t.show();
            //do here to edit card to DB

        }else{//add this card to DB
            Toast t = Toast.makeText(this, "Successful create card", Toast.LENGTH_SHORT);
            t.show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("CardDB")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int maxCardID = Integer.MIN_VALUE;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    int cardID = document.getLong("cardID").intValue();
                                    if (cardID > maxCardID) {
                                        maxCardID = cardID;
                                    }
                                }
                                Log.d("MaxCardID: ", String.valueOf(maxCardID));
                                Integer newCardID = maxCardID + 1;
                                Map<String, Object> card = new HashMap<>();
                                card.put("cardID", newCardID);
                                card.put("card_name", card_name);
                                card.put("typeID", typeID);
                                card.put("raceID", raceID);
                                card.put("health", health);
                                card.put("attack", attack);
                                card.put("skillID", skillID);
                                card.put("card_Image", image_name);
                                card.put("cost", cost);
                                card.put("rarity", rarity);

                                db.collection("CardDB").document().set(card);
                                Toast.makeText(getApplicationContext(), "Successful create card " +
                                        card_name + " (ID: " + newCardID + ")", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("MaxCardID: ", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }
        //back to the card editor
        this.onBackPressed();
    }
}