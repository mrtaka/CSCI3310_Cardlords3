package com.example.cardlords3;

// TODO:
// Include your personal particular here
//
import android.app.Activity;
import android.content.Intent;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CardListAdapter extends Adapter<CardListAdapter.CardViewHolder>  {
    private Context context;
    private LayoutInflater mInflater;

    private JSONArray CardJsonArray = new JSONArray();

    //Fragment related
    CardEditorFragment EditFragment;
    CardDeckFragment InfoFragment;

    boolean FragmentExist = false;
    Integer FragmentType = 1;
    private FragmentManager mFragmentManager;

    class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView CardImageItemView;
        TextView CardName;
        TextView CardCost;
        TextView CardHealth;
        TextView CardAttack;
        TextView CardType;
        TextView CardRace;
        RatingBar CardRarityBar;

        final CardListAdapter mAdapter;

        public CardViewHolder(View itemView, CardListAdapter adapter) {
            super(itemView);

            CardImageItemView = itemView.findViewById(R.id.card_image);
            CardRarityBar = itemView.findViewById(R.id.starsBar);
            CardName = itemView.findViewById(R.id.card_name_textview);
            CardCost = itemView.findViewById(R.id.cost_textview);
            CardHealth = itemView.findViewById(R.id.health_textview);
            CardAttack = itemView.findViewById(R.id.attack_textview);
            CardType = itemView.findViewById(R.id.type_textview);
            CardRace = itemView.findViewById(R.id.race_textview);
            this.mAdapter = adapter;

            // Event handling registration, page navigation goes here
            // Event handling registration, page navigation goes here
            CardImageItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the item that was clicked.
                    int position = getLayoutPosition();
                    //Toast t = Toast.makeText(v.getContext(), "Position " + position + " is clicked", Toast.LENGTH_SHORT);
                    //t.show();

                    //Get this card detail info
                    Integer cardID = null;
                    String mImagePath = null;
                    String name = null;
                    Integer cost = null;
                    Integer typeID = null;
                    Integer health = null;
                    Integer attack = null;
                    String raceID = null;
                    int rarity = 0;
                    //get all detail info of a card
                    try {
                        cardID = CardJsonArray.getJSONObject(position).getInt("cardID");
                        mImagePath = CardJsonArray.getJSONObject(position).getString("card_Image");
                        name = CardJsonArray.getJSONObject(position).getString("card_name");
                        cost= CardJsonArray.getJSONObject(position).getInt("cost");
                        typeID= CardJsonArray.getJSONObject(position).getInt("typeID");
                        health = CardJsonArray.getJSONObject(position).getInt("health");
                        attack= CardJsonArray.getJSONObject(position).getInt("attack");
                        raceID = CardJsonArray.getJSONObject(position).getString("raceID");
                        rarity = CardJsonArray.getJSONObject(position).getInt("rarity");

                    } catch (JSONException e) {
                        mImagePath = "image01.png";
                        rarity = 1;
                        e.printStackTrace();
                    }

                    if(cardID == -1){//=====================go to CardShopActivity==========================
                        Intent intent = new Intent(v.getContext(), CardShopActivity.class); //set the target activity
                        intent.putExtra("position", position);//send position to new activity
                        ((Activity)context).startActivityForResult(intent,1); //launch new activity
                    }
                    else if(cardID == -2){//=====================go to CardCreationActivity==========================
                        Intent intent = new Intent(v.getContext(), CardCreationActivity.class); //set the target activity
                        intent.putExtra("position", position);//send position to new activity
                        ((Activity)context).startActivityForResult(intent,1); //launch new activity
                    }
                    else if(FragmentType == 3){//=====================add a card to card deck==========================
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // User is signed in
                            String uid = user.getUid();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            // Define the document reference
                            DocumentReference docRef = db.collection("users").document(uid);

                            // Get the document
                            Integer finalCardID = cardID;
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            // Get the inventory array
                                            List<Long> inventory = (List<Long>) document.get("inventory");
                                            // Add the new value
                                            inventory.add(Long.valueOf(finalCardID));
                                            // Write the entire array back to Firestore
                                            docRef.update("inventory", inventory)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("Firestore", "DocumentSnapshot successfully updated!");
                                                            // handle success here - update UI, etc.
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("Firestore", "Error updating document", e);
                                                            // handle failure here - show error message, etc.
                                                        }
                                                    });
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
                            // Handle this case
                        }


                        Toast t2 = Toast.makeText(v.getContext(), "Add card with ID:  " + cardID , Toast.LENGTH_SHORT);
                        t2.show();
                        //============ back to card deck activity======================
                        /*Intent replyIntent = new Intent();
                        ((Activity)context).setResult(RESULT_OK, replyIntent);
                        finish();*/
                        ((Activity)context).onBackPressed();
                    }
                    else {//=====================create fragment==========================
                        FragmentManager fragmentManager = mFragmentManager;
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        //use bundle to pass data to fragment
                        Bundle mBundle = new Bundle();
                        mBundle.putInt("cardID", cardID);
                        mBundle.putString("image", mImagePath);
                        mBundle.putString("name", name);
                        mBundle.putInt("cost", cost);
                        mBundle.putInt("typeID", typeID);
                        mBundle.putInt("health", health);
                        mBundle.putInt("attack", attack);
                        mBundle.putInt("rarity", rarity);
                        mBundle.putString("raceID", raceID);
                        mBundle.putInt("position", position);

                        //see which fragment is used
                        if (FragmentType == 1) { //create info fragment
                            InfoFragment = new CardDeckFragment();
                            InfoFragment.setArguments(mBundle);
                            //see if fragment exist or not
                            if (FragmentExist == false) {
                                fragmentTransaction.add(R.id.fragment_container, (Fragment) InfoFragment, "InfoCard");
                            } else {
                                fragmentTransaction.replace(R.id.fragment_container, (Fragment) InfoFragment, "InfoCard");
                            }
                        } else {//create Edit fragment
                            EditFragment = new CardEditorFragment();
                            EditFragment.setArguments(mBundle);
                            //see if fragment exist or not
                            if (FragmentExist == false) {
                                fragmentTransaction.add(R.id.fragment_container, (Fragment) EditFragment, "EditCard");
                            } else {
                                fragmentTransaction.replace(R.id.fragment_container, (Fragment) EditFragment, "EditCard");
                            }
                        }

                        //add new fragment to backstack
                        if (FragmentExist == false) {
                            FragmentExist = true;
                            fragmentTransaction.addToBackStack(null);
                        } else {
                            fragmentManager.popBackStack();
                            fragmentTransaction.addToBackStack(null);
                        }
                        fragmentTransaction.commit();
                    }

                }
            });
            // End of ViewHolder initialization
        }
    }

    //================changed here==========================
    public CardListAdapter(Context context, JSONArray CardJsonArray, FragmentManager fragmentManager, Integer FragmentType) {
        mInflater = LayoutInflater.from(context);
        this.CardJsonArray = CardJsonArray;
        this.context = context;
        this.mFragmentManager = fragmentManager;
        this.FragmentType = FragmentType; //1 is Info Fragment, 2 is EditFragment, 3 is buyCard
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("Tag", "onCreateViewHolder run");
        View mItemView = mInflater.inflate(R.layout.cardlist_card, parent, false);
        Log.e("Tag", "onCreateViewHolder run");
        return new CardViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String mImagePath = null;
        String name = null;
        Integer cost = null;
        Integer typeID = null;
        Integer health = null;
        Integer attack = null;
        String raceID = null;
        int rarity = 0;
        try {
            mImagePath = CardJsonArray.getJSONObject(position).getString("card_Image");
            name = CardJsonArray.getJSONObject(position).getString("card_name");
            cost= CardJsonArray.getJSONObject(position).getInt("cost");
            typeID= CardJsonArray.getJSONObject(position).getInt("typeID");
            health = CardJsonArray.getJSONObject(position).getInt("health");
            attack= CardJsonArray.getJSONObject(position).getInt("attack");
            raceID = CardJsonArray.getJSONObject(position).getString("raceID");
            rarity = CardJsonArray.getJSONObject(position).getInt("rarity");

        } catch (JSONException e) {
            mImagePath = "image01.png";
            rarity = 1;
            e.printStackTrace();
        }
        //check if the rarity has updated or not
        //rarity = 3;
        //edit the file path
        mImagePath = mImagePath.replaceFirst("[.][^.]+$", "");
        Uri uri = Uri.parse("android.resource://com.example.cardlords3/drawable/" + mImagePath);
        // Update the following to display correct information based on the given position

        // Set up View items for this row (position), modify to show correct information read from the CSV
        holder.CardName.setText(name);
        if(typeID==1){
            holder.CardType.setText("士");
        }else if(typeID==2){
            holder.CardType.setText("將");
        }else if(typeID==3){
            holder.CardType.setText("魔");
        }else{
            holder.CardType.setText("");
        }

        if(Integer.parseInt(raceID)==1){
            holder.CardRace.setText("Race1");
        }else if(Integer.parseInt(raceID)==2){
            holder.CardRace.setText("Race2");
        }else if(Integer.parseInt(raceID)==3){
            holder.CardRace.setText("Race3");
        }else{
            holder.CardRace.setText("");
        }

        if(rarity==-1){
            holder.CardCost.setText("");
            holder.CardHealth.setText("");
            holder.CardAttack.setText("");
        }else{
            holder.CardCost.setText(String.valueOf(cost));
            holder.CardHealth.setText(String.valueOf(health));
            holder.CardAttack.setText(String.valueOf(attack));
        }
        holder.CardImageItemView.setImageURI(uri);
        holder.CardRarityBar.setRating(rarity);

    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return CardJsonArray.length();
    }

}
