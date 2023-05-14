package com.example.cardlords3.game;

// TODO:
// Include your personal particular here
//

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.cardlords3.CardDeckFragment;
import com.example.cardlords3.CardEditorFragment;
import com.example.cardlords3.R;

import org.json.JSONArray;
import org.json.JSONException;

public class CardListAdapterBoard extends Adapter<CardListAdapterBoard.CardViewHolder>  {
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

        final CardListAdapterBoard mAdapter;

        public CardViewHolder(View itemView, CardListAdapterBoard adapter) {
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
                    Toast t = Toast.makeText(v.getContext(), "Position " + position + " is clicked", Toast.LENGTH_SHORT);
                    t.show();

                    //=====================go to fragment==========================
                    /*
                            FragmentManager fragmentManager = mFragmentManager;
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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

                            //use bundle to pass data to fragment
                            Bundle mBundle = new Bundle();
                            mBundle.putString("image", mImagePath);
                            mBundle.putString("name", name);
                            mBundle.putInt("cost", cost);
                            mBundle.putInt("typeID", typeID);
                            mBundle.putInt("health", health);
                            mBundle.putInt("attack", attack);
                            mBundle.putInt("rarity", rarity);
                            mBundle.putString("raceID", raceID);

                            //see which fragment is used
                            if(FragmentType == 1){
                                InfoFragment = new CardDeckFragment();
                                InfoFragment.setArguments(mBundle);
                                //see if fragment exist or not
                                if(FragmentExist == false){
                                    fragmentTransaction.add(R.id.fragment_container, (Fragment) InfoFragment, "InfoCard");
                                }else{
                                    fragmentTransaction.replace(R.id.fragment_container, (Fragment) InfoFragment, "InfoCard");
                                }
                            }else{
                                EditFragment = new CardEditorFragment();
                                EditFragment.setArguments(mBundle);
                                //see if fragment exist or not
                                if(FragmentExist == false){
                                    fragmentTransaction.add(R.id.fragment_container, (Fragment) EditFragment, "EditCard");
                                }else{
                                    fragmentTransaction.replace(R.id.fragment_container, (Fragment) EditFragment, "EditCard");
                                }
                            }

                            //add new fragment to backstack
                            if(FragmentExist == false){
                                FragmentExist = true;
                                fragmentTransaction.addToBackStack(null);
                            }else{
                                fragmentManager.popBackStack();
                                fragmentTransaction.addToBackStack(null);
                            }
                            fragmentTransaction.commit();
                    */
                    //===========================================================
                }
            });
            // End of ViewHolder initialization
        }
    }

    //================changed here==========================
    public CardListAdapterBoard(Context context, JSONArray CardJsonArray) { /*, FragmentManager fragmentManager, Integer FragmentType*/
        mInflater = LayoutInflater.from(context);
        this.CardJsonArray = CardJsonArray;
        this.context = context;
        /*
        this.mFragmentManager = fragmentManager;
        this.FragmentType = FragmentType; //1 is Info Fragment, 2 is EditFragment
        */
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("Tag", "onCreateViewHolder run");
        View mItemView = mInflater.inflate(R.layout.cardlist_card_hand, parent, false);
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
        holder.CardCost.setText(String.valueOf(cost));
        if(typeID==1){
            holder.CardType.setText("士");
        }else if(typeID==2){
            holder.CardType.setText("將");
        }else{
            holder.CardType.setText("魔");
        }
        holder.CardHealth.setText(String.valueOf(health));
        holder.CardAttack.setText(String.valueOf(attack));
        holder.CardRace.setText(String.valueOf(raceID));
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
