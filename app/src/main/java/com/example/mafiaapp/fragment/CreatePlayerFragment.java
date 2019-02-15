package com.example.mafiaapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mafiaapp.R;
import com.example.mafiaapp.model.Player;


public class CreatePlayerFragment extends Fragment {

    public static final String TAG = CreatePlayerFragment.class.getSimpleName();

    //private TextInputEditText inputId;
    private TextInputEditText inputName;

    private boolean passed1;
    private boolean passed2;

    private int playerN;

    OnCreatePlayerFragmentActionListener mOnCreatePlayerFragmentActionListener;

    private Player mPlayer;


    public CreatePlayerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_player_create, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //inputId = view.findViewById(R.id.set_id);
        inputName = view.findViewById(R.id.set_name);

        Bundle args = getArguments();
        if (args != null) {
            playerN = args.getInt(MainFragment.TAG);
            Log.d(TAG,"args"+playerN);
        }


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        Button saveButton = (Button) view.findViewById(R.id.button_save_create);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imitateCreate(view);
                if (mOnCreatePlayerFragmentActionListener != null && passed2) {
                    mOnCreatePlayerFragmentActionListener.onSaveButtonClicked(mPlayer);
                } else Log.d(TAG, "Yes it is null");
            }
        });

    }

    public void imitateCreate(View root) {

        mPlayer = new Player();
//        if (inputId.getText().length() == 0 || !isNumber(inputId.getText().toString())) {
//            inputId.setError("Դաշտը չի կարող դատարկ լինել,կամ պարունակել տառ։");
//            passed1 = false;
//        } else {
//            mPlayer.setId(Integer.parseInt(inputId.getText().toString()));
//            passed1 = true;
//        }
        mPlayer.setId(playerN);
        Log.d(TAG,""+playerN);

        if (inputName.getText().length() == 0) {
            inputName.setError("Դաշտը չի կարող դատարկ լինել։");
            passed2 = false;
        } else {
            mPlayer.setName(inputName.getText().toString());
            passed2 = true;
        }

    }

    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++)
            if (Character.isDigit(s.charAt(i)) == false)
                return false;

        return true;
    }

    public void setOnCreatePlayerFragmentActionListener(OnCreatePlayerFragmentActionListener onFragmentActionListener) {
        mOnCreatePlayerFragmentActionListener = onFragmentActionListener;
    }


    public interface OnCreatePlayerFragmentActionListener {
        void onSaveButtonClicked(Player player);
    }
}
