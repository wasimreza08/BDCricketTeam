package com.example.asus.bdcricketteam.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by ASUS on 8/28/2016.
 */
public class UpComingTournamentFireBase extends FixtureBaseFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query recentPostsQuery = databaseReference.child("upcomingtournament");
        return recentPostsQuery;
    }
}
