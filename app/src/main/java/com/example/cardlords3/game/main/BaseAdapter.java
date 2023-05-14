package com.example.cardlords3.game.main;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardlords3.R;
import com.example.cardlords3.game.GameActivity;


public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.CellViewHolder> {
    private int[] BaseData;
    private int BaseSide;

    public BaseAdapter(int[] baseData, int baseSide) {
        this.BaseData = baseData;
        this.BaseSide = baseSide;
    }

    @NonNull
    @Override
    public CellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext())
        //        .inflate(R.layout.fragment_cell, parent, false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlist_card_base, parent, false);


        // Set the height of the view to be divided by 1
        int height = parent.getHeight();
        //view.set
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));


        return new CellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder holder, int position) {
        //TODO Set Cell Data
        //To rotate 180
        if (BaseSide == 1)
            holder.itemView.setRotation(180);

        if (BaseSide == 0) {
            //Own Side
            if (GameActivity.ownBase.size() > position) {
                if (GameActivity.ownBase.get(position) == -1) {
                    //Dead
                    ((TextView)holder.itemView.findViewById(R.id.cell_color_view)).setBackgroundColor(
                            Color.RED);
                }
                else {
                    //Has Card
                    ((TextView)holder.itemView.findViewById(R.id.cell_color_view)).setBackgroundColor(
                            Color.rgb(170, 200, 255));

                }
            }
            else {
                ((TextView)holder.itemView.findViewById(R.id.cell_color_view)).setBackgroundColor(
                        Color.WHITE);
            }
        }
        else {
            //Enemy Side
            if (GameActivity.enemyBase.size() > position) {
                if (GameActivity.enemyBase.get(position) == -1) {
                    //Dead
                    ((TextView)holder.itemView.findViewById(R.id.cell_color_view)).setBackgroundColor(
                            Color.RED);
                }
                else {
                    //Has Card
                    ((TextView)holder.itemView.findViewById(R.id.cell_color_view)).setBackgroundColor(
                            Color.rgb(170, 200, 255));

                }
            }
            else {
                ((TextView)holder.itemView.findViewById(R.id.cell_color_view)).setBackgroundColor(
                        Color.WHITE);
            }
        }

        if (GameActivity.enemyBase.size() > position) {
            ((TextView)holder.itemView.findViewById(R.id.cell_color_view)).setContentDescription(
                    String.valueOf(GameActivity.enemyBase.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return BaseData.length;
    }

    public static class CellViewHolder extends RecyclerView.ViewHolder {
        //public CellFragment cellFragment;

        public CellViewHolder(@NonNull View itemView) {
            super(itemView);
            //cellFragment = (CellFragment) getChildFragmentManager().findFragmentById(R.id.cell_fragment_container);
        }
    }
}