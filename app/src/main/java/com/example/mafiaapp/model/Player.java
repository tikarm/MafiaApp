package com.example.mafiaapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Player implements Parcelable {

    private int id;
    private String name;
    private int reprimand;      //նկատողություն

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReprimand() {
        return reprimand;
    }

    public void setReprimand(int reprimand) {
        this.reprimand = reprimand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeInt(getReprimand());
    }

    public static final Parcelable.Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel source) {
            return Player.createFromParcel(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }

    };

    public static Player createFromParcel(Parcel source) {
        Player player = new Player();
        Date dt = new Date();

        player.id = source.readInt();
        player.name = source.readString();
        player.reprimand = source.readInt();

        return player;
    }
}

