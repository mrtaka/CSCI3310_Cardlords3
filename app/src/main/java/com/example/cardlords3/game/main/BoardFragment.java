package com.example.cardlords3.game.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardlords3.R;


public class BoardFragment extends Fragment {

    private GameViewModel mViewModel;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    private RecyclerView boardRecyclerView;
    private BoardAdapter boardAdapter;
    private int[][] boardData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // TODO: Use the ViewModel
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_fragment, container, false);

        boardRecyclerView = view.findViewById(R.id.board_recycler_view);
        boardRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        boardData = new int[5][5];
        boardAdapter = new BoardAdapter(boardData);
        boardRecyclerView.setAdapter(boardAdapter);

        // !!!!!!!!!!!!! Start a timer to periodically update the board data
        /*
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Update the board data
                // ...

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boardAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 0, 1000);
        */

        return view;
    }
}

/*
public class BoardFragment extends Fragment {

    private GameViewModel mViewModel;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.board_fragment, container, false);
    }

}
*/