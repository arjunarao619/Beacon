package com.example.arjunrao.beacon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HelpLines extends AppCompatActivity {
Bundle country;
    int country_choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        country = intent.getExtras();
        country_choice = Integer.valueOf(country.getString("COUNTRY",""));

       switch (country_choice){
           case 0:
               //india
               setContentView(R.layout.india);
               break;
           case 1:
               setContentView(R.layout.usa);
               //usa
               break;
           case 3:
               setContentView(R.layout.france);
               break;
           case 4:
               setContentView(R.layout.uk);
               break;
           case 5:
               break;
           case 6:
               break;
           case 7:
               break;

       }

    }
}
