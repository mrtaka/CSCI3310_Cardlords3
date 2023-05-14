package com.example.cardlords3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardDeckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardDeckFragment extends Fragment {
    // TODO: Create member attribute for UI display

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM0 = "param0";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String Param_cardName;
    private int Param_cost;
    private int Param_health;
    private int Param_attack;
    private int Param_type;
    private int Param_rarity;
    private int Param_position;

    public CardDeckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragmentD.
     */
    // TODO: Rename and change types and number of parameters
    public static CardDeckFragment newInstance(String gender, String param2) {
        CardDeckFragment fragment = new CardDeckFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM0, gender);
        args.putString(ARG_PARAM1, gender);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Param_cardName = getArguments().getString("name");
            Param_cost = getArguments().getInt("cost");
            Param_health = getArguments().getInt("health");
            Param_attack = getArguments().getInt("attack");
            Param_type = getArguments().getInt("typeID");
            Param_rarity = getArguments().getInt("rarity");
            Param_position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO:
        //  1. get reference of the views and assign with received value
        TextView name = view.findViewById(R.id.showName);
        name.setText(Param_cardName);

        TextView cost = view.findViewById(R.id.showCost);
        //cost.setInputType(InputType.TYPE_CLASS_NUMBER);
        cost.setText(String.valueOf(Param_cost));

        TextView health = view.findViewById(R.id.showHealth);
        health.setText(String.valueOf(Param_health));

        TextView attack = view.findViewById(R.id.showAttack);
        attack.setText(String.valueOf(Param_attack));

        TextView type = view.findViewById(R.id.showType);
        if(Param_type==1){
            type.setText("士兵");
        }else if(Param_type==2){
            type.setText("將軍");
        }else{
            type.setText("魔法");
        }

        Button deleteCardButton = view.findViewById(R.id.deleteCardFromUserCardDeckButton);
        deleteCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete card from deck
                if(Param_position > 0){
                    Toast.makeText(getActivity(), "Remove card in position " + Param_position, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}