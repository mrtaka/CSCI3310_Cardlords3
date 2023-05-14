package com.example.cardlords3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


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
    private int Param_cardID;
    private int Param_cost;
    private int Param_health;
    private int Param_attack;
    private int Param_type;
    private int Param_rarity;
    private int Param_skill;
    private int Param_race;
    private String Param_image;

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
            Param_cardID = getArguments().getInt("cardID");
            Param_cardName = getArguments().getString("name");
            Param_cost = getArguments().getInt("cost");
            Param_health = getArguments().getInt("health");
            Param_attack = getArguments().getInt("attack");
            Param_type = getArguments().getInt("typeID");
            Param_rarity = getArguments().getInt("rarity");
            Param_skill = getArguments().getInt("skillID");
            Param_race = getArguments().getInt("raceID");
            Param_image = getArguments().getString("image");
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
        TextView id = view.findViewById(R.id.card_id_text_view);
        id.setText(Integer.toString(Param_cardID));

        EditText name = view.findViewById(R.id.edit_name);
        name.setText(Param_cardName);

        EditText cost = view.findViewById(R.id.editCost);
        //cost.setInputType(InputType.TYPE_CLASS_NUMBER);
        cost.setText(String.valueOf(Param_cost));

        EditText health = view.findViewById(R.id.editHealth);
        health.setText(String.valueOf(Param_health));

        EditText attack = view.findViewById(R.id.editAttack);
        attack.setText(String.valueOf(Param_attack));

        Button editCardButton = view.findViewById(R.id.editCardFromCardListButton);
        editCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Edit card from cardlist
                if(Param_cardID > 0){
                    Toast.makeText(getActivity(), "Edit card with cardID" + Param_cardID, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), CardCreationActivity.class); //set the target activity
                    intent.putExtra("editCard", true);//send position to new activity
                    intent.putExtra("cardID", Param_cardID);//send position to new activity
                    intent.putExtra("name", Param_cardName);//send position to new activity
                    intent.putExtra("cost", Param_cost);//send position to new activity
                    intent.putExtra("health", Param_health);//send position to new activity
                    intent.putExtra("attack", Param_attack);//send position to new activity
                    intent.putExtra("typeID", Param_type);//send position to new activity
                    intent.putExtra("rarity", Param_rarity);//send position to new activity
                    intent.putExtra("skillID", Param_skill);//send position to new activity
                    intent.putExtra("raceID", Param_race);//send position to new activity
                    intent.putExtra("card_Image", Param_image);//send position to new activity
                    startActivity(intent); //launch new activity
                }
            }
        });

        Button deleteCardButton = view.findViewById(R.id.deleteCardFromCardListButton);
        deleteCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete card from cardlist

                if(Param_cardID > 0){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("CardDB")
                            .whereEqualTo("cardID", Param_cardID)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            db.collection("CardDB").document(document.getId())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("DeleteCard", "DocumentSnapshot successfully deleted!");
                                                            Toast.makeText(getActivity(), "Delete card with cardID " + Param_cardID, Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("DeleteCard", "Error deleting document", e);
                                                            Toast.makeText(getActivity(),
                                                                    "Delete failed",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Log.d("DeleteCard", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                }
            }
        });

    }
}