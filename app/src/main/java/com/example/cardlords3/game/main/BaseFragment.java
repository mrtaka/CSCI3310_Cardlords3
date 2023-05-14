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


public class BaseFragment extends Fragment {

    private GameViewModel mViewModel;
    private int baseSide;

    public BaseFragment(int side) {
        //side 1 = enemy
        //side 0 = own
        baseSide = side;
    }

    public static BaseFragment newInstance(int side) {
        BaseFragment sidedBaseFragment = new BaseFragment(side);
        return sidedBaseFragment;
    }

    private RecyclerView baseRecyclerView;
    private BaseAdapter baseAdapter;
    private int[] baseData;

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
        View view = inflater.inflate(R.layout.base_fragment, container, false);

        baseRecyclerView = view.findViewById(R.id.base_recycler_view);
        baseRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        baseData = new int[5];
        baseAdapter = new BaseAdapter(baseData, baseSide);
        baseRecyclerView.setAdapter(baseAdapter);

        // !!!!!!!!!!!!! Start a timer to periodically update the base data
        /*
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Update the base data
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
public class BaseFragment extends Fragment {

    private GameViewModel mViewModel;

    public static BaseFragment newInstance() {
        return new BaseFragment();
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
        return inflater.inflate(R.layout.base_fragment, container, false);
    }

}
*/