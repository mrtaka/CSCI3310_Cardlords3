package com.example.cardlords3.game.main;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardlords3.R;

import java.util.Random;


public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.CellViewHolder> {
    private int[][] boardData;

    public BoardAdapter(int[][] boardData) {
        this.boardData = boardData;
    }

    @NonNull
    @Override
    public CellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cell, parent, false);


        // Set the height of the view to be divided by 5
        int height = parent.getHeight() / 5;
        //view.set
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));


        return new CellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder holder, int position) {
        int row = position / boardData.length;
        int col = position % boardData.length;
        //TODO Set Cell Data
        Random rand = new Random();
        String mImagePath = "image_" + (char)(rand.nextInt(18)+97);
        Uri uri = Uri.parse("android.resource://com.example.cardlords3/drawable/" + mImagePath);
        ((ImageView)holder.itemView.findViewById(R.id.image_cell)).setImageURI(uri);
        //holder.cellFragment.setCellValue(boardData[row][col]);
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