package com.example.cardlords3;

// TODO:
// Include your personal particular here
//
import android.app.Activity;
import android.content.Intent;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CardListAdapter extends Adapter<CardListAdapter.CardViewHolder>  {
    private Context context;
    private LayoutInflater mInflater;

    //================changed here==========================
    private JSONArray CardJsonArray = new JSONArray();

    class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView CardImageItemView;
        RatingBar CardRarityBar;

        final CardListAdapter mAdapter;

        public CardViewHolder(View itemView, CardListAdapter adapter) {
            super(itemView);
            CardImageItemView = itemView.findViewById(R.id.image);
            CardRarityBar = itemView.findViewById(R.id.flowerBar);
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

                    /*
                    //=====================changed here==========================
                    String itemsString;
                    try {
                        JSONObject items = CardJsonArray.getJSONObject(position);
                        itemsString = items.toString();
                    } catch (JSONException e) {
                        JSONObject items = new JSONObject();
                        itemsString = "null";
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(v.getContext(), DetailActivity.class); //set the target activity
                    intent.putExtra("item", itemsString);//send item json to new activity
                    intent.putExtra("position", position);//send position to new activity
                    ((Activity)context).startActivityForResult(intent,1); //launch new activity
                    //v.getContext().startActivity(intent); //launch new activity

                     */
                }
            });
            // End of ViewHolder initialization
        }
    }

    //================changed here==========================
    public CardListAdapter(Context context, JSONArray CardJsonArray) {
        mInflater = LayoutInflater.from(context);
        this.CardJsonArray = CardJsonArray;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("Tag", "onCreateViewHolder run");
        View mItemView = mInflater.inflate(R.layout.cardlist_card, parent, false);
        Log.e("Tag", "onCreateViewHolder2 run");
        return new CardViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Log.e("Tag", "onBindViewHolder run");
        String mImagePath = null;
        int rarity = 0;
        try {
            mImagePath = CardJsonArray.getJSONObject(position).getString("card_Image");
            rarity = CardJsonArray.getJSONObject(position).getInt("rarity");
        } catch (JSONException e) {
            mImagePath = "image01.png";
            rarity = 1;
            e.printStackTrace();
        }
        //check if the rarity has updated or not
        rarity = 5;
        //edit the file path
        mImagePath = mImagePath.replaceFirst("[.][^.]+$", "");
        Uri uri = Uri.parse("android.resource://com.example.cardlords3/drawable/" + mImagePath);
        // Update the following to display correct information based on the given position

        // Set up View items for this row (position), modify to show correct information read from the CSV
        holder.CardImageItemView.setImageURI(uri);
        holder.CardRarityBar.setRating(rarity);

    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    //================changed here==========================
    public int getItemCount() {
        return CardJsonArray.length();
    }

}
