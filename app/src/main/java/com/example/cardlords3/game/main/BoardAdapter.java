package com.example.cardlords3.game.main;

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

    private JSONArray CardJsonArray = new JSONArray();

    public BoardAdapter(int[][] boardData, GameActivity.Cell[][] cells, JSONArray inventoryJsonArray, GameActivity gameActivity) {
        this.boardData = boardData;
        this.BoardCells = cells;
        this.CardJsonArray = inventoryJsonArray;
        this.gameActivityParent = gameActivity;
    }

    public static void PreparePlace(int handCardPlaceType, int side, int position, int intValue) {
        switch(handCardPlaceType) {
            case 0:
                // 0 normal_soldier
                //TODO: Make all empty back-rank glow and clickable
                if (side == 0) {    //own
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
                break;
            case 2:
                // 2 magic_anywhere
                break;
            case 3:
                // 3 magic_enemy_card
                break;
            case 4:
                // 4 magic_all_empty
                break;
            case 5:
                // 5 magic_all_empty_except_last_line
                break;
            case 6:
                // 6 anywhere_soldier_except_last_line
                break;
            default:
                break;
        }

        GameActivity.loadBoardGlobal(gameActivityParent);
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


        return new CellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder holder, int position) {
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
            if(GameActivity.clickable[row][col]) {
                ((CardView)(holder.itemView.findViewById(R.id.board_cardView))).setCardBackgroundColor(
                        Color.argb(255, 230, 255, 230));
            }
            else {
                ((CardView)(holder.itemView.findViewById(R.id.board_cardView))).setCardBackgroundColor(
                        Color.argb(255, 255, 255, 255));
            }
        }
        else {
            mImagePath = mImagePath.replaceFirst("[.][^.]+$", "");
            uri = Uri.parse("android.resource://com.example.cardlords3/drawable/" + mImagePath);
        }

        if(GameActivity.clickable[row][col]) {
            ((CardView)(holder.itemView.findViewById(R.id.board_cardView))).setCardBackgroundColor(
                    Color.argb(255, 230, 255, 230));
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

        //TODO Read Card Data
        //TODO END of Read Card Data

    }

    @Override
    public int getItemCount() {
        return boardData.length * boardData[0].length;
    }

    public static class CellViewHolder extends RecyclerView.ViewHolder {
        //public CellFragment cellFragment;

        public CellViewHolder(@NonNull View itemView) {
            super(itemView);
            //cellFragment = (CellFragment) getChildFragmentManager().findFragmentById(R.id.cell_fragment_container);
        }
    }
}