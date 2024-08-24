package com.example.theotokos;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    //private static final String USERS_NODE =;
    private static DatabaseReference HymnsDatabase, CopticDatabase, AgbyaDatabase, TaksDatabase;

    public FirebaseHelper() {
        HymnsDatabase = FirebaseDatabase.getInstance().getReference( "hymns");
        CopticDatabase = FirebaseDatabase.getInstance().getReference( "coptic");
        AgbyaDatabase = FirebaseDatabase.getInstance().getReference( "agbya");
        TaksDatabase = FirebaseDatabase.getInstance().getReference( "taks");

    }

//    public static DatabaseReference getUsersReference() {
//        return database;
//    }


}
