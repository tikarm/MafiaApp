package com.example.mafiaapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mafiaapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardsFragment extends Fragment {
    public static final String TAG = CardsFragment.class.getSimpleName();
    public static final int CARDS_COUNT = 10;

    private ImageView card;
    private Button nextButton;
    private TextView playerCount;

    List<Integer> cards = new ArrayList<>();
    Random random = new Random();


    public CardsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        //Bundle bundle = this.getArguments();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        card = view.findViewById(R.id.card);
        nextButton = view.findViewById(R.id.button_next);
        playerCount = view.findViewById(R.id.player_count);

        //1-red,2-sheriff,3-black,4-don
        cards.add(1);
        cards.add(1);
        cards.add(1);
        cards.add(1);
        cards.add(1);
        cards.add(1);
        cards.add(2);
        cards.add(3);
        cards.add(3);
        cards.add(4);



        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShuffle();
            }
        });

    }

    private int i = 0;

    public void startShuffle() {
        if(cards.size()==0 || i>9)
        {
            card.setImageResource(R.drawable.empty);
        }
        else {
            int currentCard = random.nextInt(CARDS_COUNT - i);
            if (cards.get(currentCard) == 1) {
                card.setImageResource(R.drawable.red);
            }
            if (cards.get(currentCard) == 2) {
                card.setImageResource(R.drawable.sheriff);
            }
            if (cards.get(currentCard) == 3) {
                card.setImageResource(R.drawable.black);
            }
            if (cards.get(currentCard) == 4) {
                card.setImageResource(R.drawable.don);
            }
            cards.remove(currentCard);
            i++;
            playerCount.setText(String.valueOf(i));
        }
    }

}
