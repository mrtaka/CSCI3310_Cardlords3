package com.example.cardlords3;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardEditorFragment extends Fragment {
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

    public CardEditorFragment() {
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
    public static CardEditorFragment newInstance(String gender, String param2) {
        CardEditorFragment fragment = new CardEditorFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO:
        //  1. get reference of the views and assign with received value
        EditText name = view.findViewById(R.id.edit_name);
        name.setText(Param_cardName);

        EditText cost = view.findViewById(R.id.editCost);
        //cost.setInputType(InputType.TYPE_CLASS_NUMBER);
        cost.setText(String.valueOf(Param_cost));

        EditText health = view.findViewById(R.id.editHealth);
        health.setText(String.valueOf(Param_health));

        EditText attack = view.findViewById(R.id.editAttack);
        attack.setText(String.valueOf(Param_attack));

    }
}