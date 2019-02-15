package com.example.mafiaapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mafiaapp.R;
import com.example.mafiaapp.fragment.CardsFragment;
import com.example.mafiaapp.fragment.CreatePlayerFragment;
import com.example.mafiaapp.fragment.EditPlayerFragment;
import com.example.mafiaapp.fragment.MainFragment;
import com.example.mafiaapp.model.Player;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainFragment mainFragment;
    private CreatePlayerFragment createPlayerFragment;
    private EditPlayerFragment editPlayerFragment;
    private CardsFragment cardsFragment;

    private int playerN;

    private MainFragment.OnMainFragmentActionListener mOnMainFragmentActionListener = new MainFragment.OnMainFragmentActionListener() {
        @Override
        public void onAddButtonClicked() {
            playerN = mainFragment.getPlayerN();
            Bundle args = new Bundle();
            args.putInt(MainFragment.TAG,playerN);
            createPlayerFragment = new CreatePlayerFragment();
            createPlayerFragment.setOnCreatePlayerFragmentActionListener(mOnCreatePlayerFragmentActionListener);
            createPlayerFragment.setArguments(args);
            replaceFragment(createPlayerFragment);
        }

        @Override
        public void onToDoItemClicked(Player player) {
            editPlayerFragment = new EditPlayerFragment();
            editPlayerFragment.setOnEditPlayerFragmentActionListener(mOnEditPlayerFragmentActionListener);
            Bundle args = new Bundle();
            args.putParcelable(MainFragment.TAG,player);
            editPlayerFragment.setArguments(args);
            replaceFragment(editPlayerFragment);
        }

        @Override
        public void onToDoItemLongClicked(Player player) {
            mainFragment.handleLongClick();
        }

        @Override
        public void onGameButtonClicked() {
            cardsFragment = new CardsFragment();
            replaceFragment(cardsFragment);
        }

    };
    private CreatePlayerFragment.OnCreatePlayerFragmentActionListener mOnCreatePlayerFragmentActionListener = new CreatePlayerFragment.OnCreatePlayerFragmentActionListener(){
        @Override
        public void onSaveButtonClicked(Player player) {
            mainFragment.CreateData(player);
            replaceFragment(mainFragment);
        }
    };
    private EditPlayerFragment.OnEditPlayerFragmentActionListener mOnEditPlayerFragmentActionListener = new EditPlayerFragment.OnEditPlayerFragmentActionListener() {
        @Override
        public void onEditSaveButtonClicked(Player player) {
            Log.d("MAIN","editSave");
            mainFragment.handleEditResult(player);
            replaceFragment(mainFragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainFragment = new MainFragment();
        mainFragment.setOnFragmentActionListener(mOnMainFragmentActionListener);
        addFragment(mainFragment);
    }

    private void addFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_main_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void replaceFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_main_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
