package com.example.kobevervoort.breweryapp;

/**
 * Created by kobevervoort on 21/01/2018.
 */

public class UserFavorite {
    public boolean favorite;

    public UserFavorite(){

    }

    public UserFavorite(Boolean favorite){
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
