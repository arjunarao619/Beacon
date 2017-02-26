package com.example.arjunrao.beacon;

/**
 * Created by Arjun Rao on 1/14/2017.
 */

public class Card_data {

    private String name;
    private int imageResourceId;

    public Card_data(String name, int imageResourceId){
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public static final Card_data[] data = {
            new Card_data("Trusted Contacts",R.drawable.trusted_contacts),
            new Card_data("Shortcut Keys",R.drawable.shortcut_key),
            new Card_data("Email & SMS",R.drawable.email),
            new Card_data("Sound & Notifications",R.drawable.settings),
            new Card_data("Location Settings",R.drawable.location),
            new Card_data("About Developer",R.drawable.developer)
    };
            public String getName(){
                return name;
            }

    public int getImageResourceId(){
        return imageResourceId;
    }
}
