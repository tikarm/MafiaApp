package com.example.mafiaapp.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.mafiaapp.R;
import com.example.mafiaapp.model.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PlayersRecyclerAdapter extends RecyclerView.Adapter<PlayersRecyclerAdapter.PlayersViewHolder> {

    private List<Player> mData = new ArrayList<>();
    private OnRvItemClickListener mOnRvItemClickListener;
    private boolean isDeleteMode = false;
    private boolean toClearCheckBoxState = false;
    private boolean deadClicked = false;

    private int reprimandCount = 0;
    private boolean plusChosen;
    private boolean minusChosen;

    private List<Player> stateChecked = new ArrayList<>();

    private int[] reprimandList;


    @NonNull
    @Override
    public PlayersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_player, parent, false);
        return new PlayersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayersViewHolder holder, final int position) {
        final Player player = mData.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!deadClicked)
                    mOnRvItemClickListener.onItemClicked(mData.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAllCheckBoxes(true);
                mOnRvItemClickListener.onItemLongClicked(mData.get(position));
                return true;
            }
        });
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("ADAPTER", "ItemCheck");
                mOnRvItemClickListener.onItemChecked(player, isChecked);
                //isItemChecked(todoItem,isChecked); //adds or removes item to/from stateChecked list
            }
        });
        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRvItemClickListener.onPlusClicked(position);
            }
        });
        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRvItemClickListener.onMinusClicked(position);
            }
        });
//        holder.deadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deadClicked = !deadClicked;
//                mOnRvItemClickListener.onDeadButtonClicked(position);
//                holder.minusButton.setEnabled(deadClicked);
//                holder.plusButton.setEnabled(deadClicked);
//                holder.playerReprimand.setEnabled(deadClicked);
//                holder.playerName.setEnabled(deadClicked);
//                holder.playerId.setEnabled(deadClicked);
//            }
//        });


        if (toClearCheckBoxState)
            holder.cb.setChecked(false);
        toClearCheckBoxState = false;

        holder.cb.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);
        holder.playerId.setText(String.valueOf(player.getId())+".");
        holder.playerName.setText(player.getName());

        reprimandList[position] = player.getReprimand();
        Log.d("ADAPTER",""+player.getReprimand());

        if (reprimandList.length > 0) {
            if (reprimandList[position] < 4 && plusChosen) {

                holder.playerReprimand.setText(String.valueOf(++reprimandList[position]));
                player.setReprimand(reprimandList[position]);
                Log.d("ADAPTER", "plusssss "+ reprimandList.length);

                plusChosen = false;
            }
            if (reprimandList[position] > 0 && minusChosen) {
                holder.playerReprimand.setText(String.valueOf(--reprimandList[position]));
                player.setReprimand(reprimandList[position]);
                Log.d("ADAPTER", "minussss "+ reprimandList.length);
                minusChosen = false;
            }
        }
        holder.playerReprimand.setText(String.valueOf(player.getReprimand()));

    }

    public void showAllCheckBoxes(boolean show) {
        isDeleteMode = show;
        notifyDataSetChanged();
    }

    public void clearCheckBoxState() {
        toClearCheckBoxState = true;
        notifyDataSetChanged();
    }

    public void plusClicked(int pos) {
        plusChosen = true;
        // notifyDataSetChanged();
        notifyItemChanged(pos);
    }

    public void minusClicked(int pos) {
        minusChosen = true;
//        notifyDataSetChanged();
        notifyItemChanged(pos);
    }

    public void deadButtonClicked(int pos)
    {
        notifyItemChanged(pos);
    }
//    public void isItemChecked(ToDoItem item,boolean isChecked)
//    {
//        if (isChecked) {
//            stateChecked.add(item);
//        } else {
//            stateChecked.remove(item);
//        }
//    }


    public class PlayersViewHolder extends RecyclerView.ViewHolder {
        public TextView playerId;
        public TextView playerName;
        public TextView playerReprimand;
        public Button plusButton;
        public Button minusButton;
        //public Button deadButton;
        public CheckBox cb;

        public PlayersViewHolder(final View itemView) {
            super(itemView);
            playerId = itemView.findViewById(R.id.player_id);
            playerName = itemView.findViewById(R.id.player_name);
            playerReprimand = itemView.findViewById(R.id.reprimand);
            plusButton = itemView.findViewById(R.id.button_plus);
            minusButton = itemView.findViewById(R.id.button_minus);
            //deadButton = itemView.findViewById(R.id.button_dead);
            cb = itemView.findViewById(R.id.checkBox);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItems(Collection<Player> players) {
        mData.addAll(players);
        reprimandList = new int[mData.size()];
        notifyDataSetChanged();
    }


    public void updateItem(Player player) {

        for (int i = 0; i < mData.size(); i++) {
            if (Objects.equals(player, mData.get(i))) {
                mData.set(i, player);
                notifyItemChanged(i);
            }
        }
    }

//    public List<ToDoItem> RemoveItems() {
//        for (ToDoItem item : stateChecked) {
//            mData.remove(item);
//        }
//        stateChecked.clear();
//        notifyDataSetChanged();
//        return mData;
//    }


    public void setmOnRvItemClickListener(OnRvItemClickListener mOnRvItemClickListener) {         //setter for mOnRvItemClickListener
        this.mOnRvItemClickListener = mOnRvItemClickListener;
    }

    public interface OnRvItemClickListener {
        void onItemClicked(Player item);

        void onItemLongClicked(Player item);

        void onItemChecked(Player item, boolean isChecked);

        void onPlusClicked(int index);

        void onMinusClicked(int index);

        //void onDeadButtonClicked(int index);
    }


}