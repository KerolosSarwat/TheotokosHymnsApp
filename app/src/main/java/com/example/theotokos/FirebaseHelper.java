package com.example.theotokos;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    //private static final String USERS_NODE =;
    private static CollectionReference HymnsDatabase, CopticDatabase, AgbyaDatabase, TaksDatabase, bannerDatabase;

    public FirebaseHelper() {
        HymnsDatabase = FirebaseFirestore.getInstance().collection( "hymns");
        CopticDatabase = FirebaseFirestore.getInstance().collection( "coptic");
        AgbyaDatabase = FirebaseFirestore.getInstance().collection( "agbya");
        TaksDatabase = FirebaseFirestore.getInstance().collection( "taks");
        bannerDatabase = FirebaseFirestore.getInstance().collection( "banner");
    }

    public static CollectionReference getHymnsDatabase() {
        return HymnsDatabase;
    }

    public static CollectionReference getCopticDatabase() {
        return CopticDatabase;
    }

    public static CollectionReference getAgbyaDatabase() {
        return AgbyaDatabase;
    }

    public static CollectionReference getTaksDatabase() {
        return TaksDatabase;
    }

    public static CollectionReference getBannerDatabase() {
        return bannerDatabase;
    }
}
