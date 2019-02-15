package com.example.mafiaapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mafiaapp.R;
import com.example.mafiaapp.adapter.PlayersRecyclerAdapter;
import com.example.mafiaapp.model.Player;
import com.example.mafiaapp.persistance.DAO.PlayerDao;
import com.example.mafiaapp.persistance.DbWrapper;
import com.example.mafiaapp.persistance.entity.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();

    private List<Player> playerList = new ArrayList<>();
    private List<Player> stateChecked;
    private FloatingActionButton addButton;
    private Button gameButton;

    private PlayersRecyclerAdapter mRecyclerAdapter;
    private RecyclerView recyclerView;

    private PlayerDao mToDoItemDao = DbWrapper.getAppDatabase().playerDao();

    private OnMainFragmentActionListener mOnMainFragmentActionListener;

    private View view;
    private Menu mMenu;
    private boolean showButtons;

    private boolean multiSelectedMode;
    private int selectedItemsCount;
    private boolean toRemove = false;

    private int playerN = 1; //this is the current number of player
    private boolean plusClicked;
    private boolean minusClicked;

    //private TextView reprimand;


    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateChecked = new ArrayList<>();
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate");
    }

    private boolean gotDatabase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container,
                false);
        addButton = (FloatingActionButton) view.findViewById(R.id.button_add);
        gameButton = view.findViewById(R.id.shuffle_cards);

        //Room Implementation

        if (!gotDatabase) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getDatabase();
                }
            }).start();
        }
        initRecyclerView(view);
        return view;
    }

    private void getDatabase() {
        // Query
        List<PlayerEntity> entities = mToDoItemDao.findAll();
        for (PlayerEntity entity : entities) {
            Player item = new Player();
            item.setId(entity.id);
            item.setName(entity.name);
            item.setReprimand(entity.reprimand);
            playerN = item.getId();
            playerN++;
            playerList.add(item);
        }
        gotDatabase = true;
    }

    public int getPlayerN() {
        return playerN;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnMainFragmentActionListener != null) {
                    mOnMainFragmentActionListener.onAddButtonClicked();
                }
            }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMainFragmentActionListener != null) {
                    mOnMainFragmentActionListener.onGameButtonClicked();
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hide_buttons:
                hideButtons(showButtons);
                showButtons = !showButtons;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideButtons(boolean show)
    {
        if(show)
        {
            addButton.setVisibility(View.VISIBLE);
            gameButton.setVisibility(View.VISIBLE);
        }
        else {
            addButton.setVisibility(View.INVISIBLE);
            gameButton.setVisibility(View.INVISIBLE);
        }
    }

    private ActionMode mActionMode;

    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(selectedItemsCount + " Ընտրված");
            mode.getMenuInflater().inflate(R.menu.menu_context, menu);
            addButton.setVisibility(View.INVISIBLE);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            selectedItemsCount = 0;
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_remove:
                    showInfoForRemove();
                    // mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mRecyclerAdapter.showAllCheckBoxes(false);
            if (!toRemove)
                stateChecked.clear();
            mRecyclerAdapter.clearCheckBoxState();
            selectedItemsCount = 0;
            multiSelectedMode = false;
            mActionMode = null;
        }

    };

    private void initRecyclerView(View view) {

        mRecyclerAdapter = new PlayersRecyclerAdapter();

        mRecyclerAdapter.setmOnRvItemClickListener(
                new PlayersRecyclerAdapter.OnRvItemClickListener() {
                    @Override
                    public void onItemClicked(Player item) {
                        if (mOnMainFragmentActionListener != null) {
                            mOnMainFragmentActionListener.onToDoItemClicked(item);
                        }
                    }

                    @Override
                    public void onItemLongClicked(Player item) {
                        if (mOnMainFragmentActionListener != null)
                            mOnMainFragmentActionListener.onToDoItemLongClicked(item);
                    }

                    @Override
                    public void onItemChecked(Player item, boolean isChecked) {
                        //todo add item in arraylist

                        Log.d(TAG, "onItemChecked");
                        if (isChecked) {
                            stateChecked.add(item);
                            mActionMode.setTitle(++selectedItemsCount + " Ընտրված");

                            Log.d(TAG, stateChecked.size() + "");
                        } else {

                            Log.d(TAG, stateChecked.size() + "");
                            if (multiSelectedMode) {
                                stateChecked.remove(item);
                            }
                            if (selectedItemsCount > 0)
                                mActionMode.setTitle(--selectedItemsCount + " Ընտրված");
                        }

                    }

                    @Override
                    public void onPlusClicked(final int pos) {
                        mRecyclerAdapter.plusClicked(pos);
                        plusClicked = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                updateDatabase(playerList.get(pos));
                            }
                        }).start();
                    }

                    @Override
                    public void onMinusClicked(final int pos) {
                        mRecyclerAdapter.minusClicked(pos);
                        minusClicked = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                updateDatabase(playerList.get(pos));
                            }
                        }).start();
                    }

//                    @Override
//                    public void onDeadButtonClicked(int index) {
//                        mRecyclerAdapter.deadButtonClicked(index);
//                    }
                }
        );
        recyclerView = view.findViewById(R.id.recycler_fragment_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        mRecyclerAdapter.addItems(playerList);
        //playerN = playerList.size();
    }


    public void CreateData(Player item) {

        final Player player = item;         //because we cannot pass non final object to inner class
        new Thread(new Runnable() {
            @Override
            public void run() {
                insertIntoDatabase(player);
                Log.d(TAG, player.toString());
            }
        }).start();

        playerList.add(item);
        playerN++;
    }

    private void insertIntoDatabase(Player item) {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.id = item.getId();
        playerEntity.name = item.getName();
        playerEntity.reprimand = item.getReprimand();

        // Insert
        mToDoItemDao.insert(playerEntity);
    }


    public void handleEditResult(Player player)  //getting changed todoItem
    {
        mRecyclerAdapter.updateItem(player);

        final Player item = player;
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateDatabase(item);
            }
        }).start();

    }

    private void updateDatabase(Player item) {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.id = item.getId();
        playerEntity.name = item.getName();
        if (plusClicked && item.getReprimand() < 4) {
            playerEntity.reprimand = item.getReprimand() + 1;
            plusClicked = false;
        } else if (minusClicked && item.getReprimand() > 0) {
            playerEntity.reprimand = item.getReprimand() - 1;
            minusClicked = false;
        } else playerEntity.reprimand = item.getReprimand();

        // Update
        mToDoItemDao.update(playerEntity);
    }

    public void handleLongClick() {
        multiSelectedMode = true;
//        toRemove = true;
        mActionMode = getActivity().startActionMode(modeCallBack);
//        isRemoveMode
        invalidateOptionsMenu(getActivity());
    }

    private void removeItems() {
        Log.d(TAG, "" + stateChecked.size());
        toRemove = true;

        for (Player item : stateChecked) {
            playerList.remove(item);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteFromDatabase();
            }
        }).start();

        //toRemove = false;                           //to hide remove button


        mActionMode.finish();
        addButton.setVisibility(View.VISIBLE);
        initRecyclerView(view);
        mRecyclerAdapter.showAllCheckBoxes(false);
        playerN--;
    }

    private void deleteFromDatabase() {

        Log.v(TAG, stateChecked.size() + " ");
        for (Player item : stateChecked) {
            PlayerEntity entity = new PlayerEntity();
            entity.id = item.getId();
            entity.name = item.getName();
            entity.reprimand = item.getReprimand();

            //Delete
            mToDoItemDao.delete(entity);
        }

        stateChecked.clear();
        mRecyclerAdapter.notifyDataSetChanged();

    }

    private void showInfoForRemove() {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity())
                .setTitle("Հաստատում")
                .setMessage("Դուք համոզվա՞ծ եք,որ ուզում եք ջնջել նշված մասնակիցներին")
                .setPositiveButton("ԱՅՈ", mOnRemoveClickListener)
                .setNegativeButton("Ոչ", mOnRemoveClickListener)
                .show();
    }

    public interface OnMainFragmentActionListener {
        void onAddButtonClicked();

        void onToDoItemClicked(Player item);

        void onToDoItemLongClicked(Player item);

        void onGameButtonClicked();

        //void onToDoItemChecked(ToDoItem item);
    }

    public void setOnFragmentActionListener(OnMainFragmentActionListener onMainFragmentActionListener) {
        mOnMainFragmentActionListener = onMainFragmentActionListener;
    }

    private DialogInterface.OnClickListener mOnRemoveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    removeItems();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;

            }
        }
    };

}
