package com.example.cardlords3.game.main;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardlords3.R;
import com.example.cardlords3.game.GameActivity;

import org.json.JSONArray;
import org.json.JSONException;


public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.CellViewHolder> {
    private final int[][] boardData;
    private static GameActivity.Cell[][] BoardCells;
    private static GameActivity gameActivityParent;
    private Context context;

    private JSONArray CardJsonArray = new JSONArray();

    //=====================test==========================
    private BoardAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onBoardCellClick(int row, int col);
    }

    public void setOnItemClickListener(BoardAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    //=====================test==========================

    public BoardAdapter(int[][] boardData, GameActivity.Cell[][] cells, JSONArray inventoryJsonArray, GameActivity gameActivity) {
        this.boardData = boardData;
        this.BoardCells = cells;
        this.CardJsonArray = inventoryJsonArray;
        this.gameActivityParent = gameActivity;
    }

    public void setData(GameActivity.Cell[][] cells, JSONArray inventoryJsonArray) {
        this.BoardCells = cells;
        this.CardJsonArray = inventoryJsonArray;
    }






    class CellViewHolder extends RecyclerView.ViewHolder {

        CardView CellCardItemView;
        /*
        TextView CardName;
        TextView CardCost;
        TextView CardHealth;
        TextView CardAttack;
        TextView CardType;
        TextView CardRace;
        RatingBar CardRarityBar;
        TextView Glower;
        */

        final BoardAdapter bAdapter;

        public CellViewHolder(View itemView, BoardAdapter adapter) {
            super(itemView);

            CellCardItemView = itemView.findViewById(R.id.board_cardView);
            /*
            CardRarityBar = itemView.findViewById(R.id.starsBar);
            CardName = itemView.findViewById(R.id.card_name_textview);
            CardCost = itemView.findViewById(R.id.cost_textview);
            CardHealth = itemView.findViewById(R.id.health_textview);
            CardAttack = itemView.findViewById(R.id.attack_textview);
            CardType = itemView.findViewById(R.id.type_textview);
            CardRace = itemView.findViewById(R.id.race_textview);
            Glower = itemView.findViewById(R.id.hand_card_glow);
            */
            this.bAdapter = adapter;

            CellCardItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("CELL CLICKED", "clicked clicked clicked");
                    //=====================test========================
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        int row = position/5;
                        int col = position%5;
                        mListener.onBoardCellClick(row, col);
                    } else {

                        Log.e("Listener is null", "uh oh");
                    }

                }
            });
            // End of ViewHolder initialization
        }
    }




















    @NonNull
    @Override
    public CellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
        ImageView CardImageItemView;
        TextView CardName;
        TextView CardCost;
        TextView CardHealth;
        TextView CardAttack;
        TextView CardType;
        TextView CardRace;
        RatingBar CardRarityBar;

        final BoardAdapter boardAdapter;
        */

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlist_card_board, parent, false);


        // Set the height of the view to be divided by 5
        int height = parent.getHeight() / 5;
        //view.set
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));


        return new CellViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder holder, int position) {
        Log.e("BOARD LOADING", "LOADING");

        int col = position % boardData.length;
        int row = position / boardData.length;

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
            ((TextView)(holder.itemView.findViewById(R.id.cost_bg))).setBackgroundColor(Color.argb(200, 0, 153, 204));
            ((TextView)(holder.itemView.findViewById(R.id.type_bg))).setBackgroundColor(Color.argb(200, 0, 0, 0));
            ((TextView)(holder.itemView.findViewById(R.id.attack_bg))).setBackgroundColor(Color.argb(200, 255, 136, 0));
            ((TextView)(holder.itemView.findViewById(R.id.health_bg))).setBackgroundColor(Color.argb(200, 204, 0, 0));

        } catch (JSONException e) {
            mImagePath = "";
            rarity = 1;
            e.printStackTrace();
        }
        //check if the rarity has updated or not
        //rarity = 3;
        //edit the file path
        Uri uri;
        if (mImagePath == "") {
            uri = Uri.parse("");
        }
        else {
            mImagePath = mImagePath.replaceFirst("[.][^.]+$", "");
            uri = Uri.parse("android.resource://com.example.cardlords3/drawable/" + mImagePath);
        }
        // Set up View items for this row (position), modify to show correct information read from the CSV
        //holder.CardName.setText(name);
        if(typeID==1){
            ((TextView)(holder.itemView.findViewById(R.id.type_textview))).setText("士");
        }else if(typeID==2){
            ((TextView)(holder.itemView.findViewById(R.id.type_textview))).setText("將");
        }else if(typeID==3){
            ((TextView)(holder.itemView.findViewById(R.id.type_textview))).setText("魔");
        }else{
            ((TextView)(holder.itemView.findViewById(R.id.type_textview))).setText("");
        }
        if(rarity==-1){
            ((TextView)(holder.itemView.findViewById(R.id.cost_textview))).setText("");
            ((TextView)(holder.itemView.findViewById(R.id.health_textview))).setText("");
            ((TextView)(holder.itemView.findViewById(R.id.attack_textview))).setText("");
            //holder.CardRace.setText("");
        }else{
            ((TextView)(holder.itemView.findViewById(R.id.cost_textview))).setText(String.valueOf(cost));
            ((TextView)(holder.itemView.findViewById(R.id.health_textview))).setText(String.valueOf(health));
            ((TextView)(holder.itemView.findViewById(R.id.attack_textview))).setText(String.valueOf(attack));
            //holder.CardRace.setText(String.valueOf(raceID));
        }
        ((ImageView)(holder.itemView.findViewById(R.id.card_image))).setImageURI(uri);
        //holder.CardRarityBar.setRating(rarity);


        if(rarity==-1) {
            ((TextView) (holder.itemView.findViewById(R.id.cost_bg))).setBackgroundColor(Color.argb(0, 255, 255, 255));
            ((TextView) (holder.itemView.findViewById(R.id.type_bg))).setBackgroundColor(Color.argb(0, 255, 255, 255));
            ((TextView) (holder.itemView.findViewById(R.id.attack_bg))).setBackgroundColor(Color.argb(0, 255, 255, 255));
            ((TextView) (holder.itemView.findViewById(R.id.health_bg))).setBackgroundColor(Color.argb(0, 255, 255, 255));
        }

        //TODO Set Cell Data
        //To rotate 180
        if (BoardCells[row][col].owner == 1)
            holder.itemView.setRotation(180);


        if(GameActivity.clickable[row][col]) {
            //GLOW
            ((TextView)(holder.itemView.findViewById(R.id.board_card_glow))).setBackgroundColor(
                    Color.argb(50, 50, 255, 0));
            ((TextView)(holder.itemView.findViewById(R.id.board_card_glow))).setVisibility(View.VISIBLE);
        }
        else {
            ((TextView)(holder.itemView.findViewById(R.id.board_card_glow))).setBackgroundColor(
                    Color.argb(0, 255, 255, 255));
            ((TextView)(holder.itemView.findViewById(R.id.board_card_glow))).setVisibility(View.INVISIBLE);
        }


        //TODO Read Card Data
        //TODO END of Read Card Data

    }

    @Override
    public int getItemCount() {
        return boardData.length * boardData[0].length;
    }
}