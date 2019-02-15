package com.example.mafiaapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mafiaapp.R;
import com.example.mafiaapp.model.Player;


import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

public class EditPlayerFragment extends Fragment
{

    public static final String TAG = EditPlayerFragment.class.getSimpleName();

    //private TextInputEditText editId;
    private TextInputEditText editName;
    private Button saveEditButton;

    private Menu mMenu;

    private boolean passed1;
    private boolean passed2;
    private boolean isEditEnabled = true;

    private Player currentPlayer;
    private Player newPlayer;

   // private int playerN;

    EditPlayerFragment.OnEditPlayerFragmentActionListener mOnEditPlayerFragmentActionListener;

    public EditPlayerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_edit, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //editId = view.findViewById(R.id.edit_id);
        editName = view.findViewById(R.id.edit_name);


        Bundle args = getArguments();
        if (args != null) {
            currentPlayer = args.getParcelable(MainFragment.TAG);
            //playerN = args.getInt(MainFragment.TAG);
        }

        //editId.setText(String.valueOf(currentPlayer.getId()));

        //editId.setEnabled(true);
        editName.setText(currentPlayer.getName());
        editName.setEnabled(true);


        saveEditButton = (Button) view.findViewById(R.id.button_save_edit);
        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imitateEdit();
                if (mOnEditPlayerFragmentActionListener != null  && passed2) {
                    mOnEditPlayerFragmentActionListener.onEditSaveButtonClicked(newPlayer);
                }
            }
        });
        SetView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mMenu.findItem(R.id.action_edit)
                .setVisible(isEditEnabled);
        mMenu.findItem(R.id.action_done)
                .setVisible(!isEditEnabled)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                toggleEditMenu();
                return true;
            case R.id.action_done:
                toggleEditMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleEditMenu() {
        isEditEnabled = !isEditEnabled;
        SetView();
        invalidateOptionsMenu(getActivity());
    }

    public void imitateEdit() {
        newPlayer = currentPlayer;
//        if (editId.getText().length() == 0 || !isNumber(editId.getText().toString())) {
//            editId.setError("Դաշտը չի կարող դատարկ լինել,կամ պարունակել տառ։");
//            passed1 = false;
//        } else {
//            newPlayer.setId(Integer.parseInt(editId.getText().toString()));
//            passed1 = true;
//        }
        //newPlayer.setId(playerN);
        if (editName.getText().length() == 0) {
            editName.setError("Դաշտը չի կարող դատարկ լինել։");
            passed2 = false;
        } else {
            newPlayer.setName(editName.getText().toString());
            passed2 = true;
        }

    }
    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++)
            if (Character.isDigit(s.charAt(i)) == false)
                return false;

        return true;
    }

    private void SetView()          //this method disables or enables view
    {
        //editId.setEnabled(!isEditEnabled);
        editName.setEnabled(!isEditEnabled);
        saveEditButton.setEnabled(isEditEnabled);

    }

    public void setOnEditPlayerFragmentActionListener(EditPlayerFragment.OnEditPlayerFragmentActionListener onFragmentActionListener) {
        mOnEditPlayerFragmentActionListener = onFragmentActionListener;
    }


    public interface OnEditPlayerFragmentActionListener {
        void onEditSaveButtonClicked(Player player);
    }

}
