package com.payinekereg.treelogy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.tabs.ListTreesTAB;
import com.payinekereg.treelogy.tabs.MyObservationsTAB;
import com.payinekereg.treelogy.tabs.NewObservationTAB;

import static com.payinekereg.treelogy.constants.MyConstants.MY_CONSTANTS            ;
import static com.payinekereg.treelogy.constants.MyConstants.IS_FIRST_ENTRANCE       ;
import static com.payinekereg.treelogy.constants.MyConstants.LANGUAGE                ;

import static com.payinekereg.treelogy.constants.MyConstants.NEW_OBSERVATION         ;
import static com.payinekereg.treelogy.constants.MyConstants.MY_OBSERVATIONS         ;
import static com.payinekereg.treelogy.constants.MyConstants.TREES_AND_LEAVES        ;
import static com.payinekereg.treelogy.constants.MyConstants.EXTRA                   ;

import static com.payinekereg.treelogy.constants.Constants_English.eNEW_OBSERVATION  ;
import static com.payinekereg.treelogy.constants.Constants_English.eMY_OBSERVATIONS  ;
import static com.payinekereg.treelogy.constants.Constants_English.eTREES_AND_LEAVES ;


public class EntranceActivity extends AppCompatActivity {

    public static boolean lang;

    private Button new_observation  ;
    private Button my_observations  ;
    private Button tree_and_leaves  ;
    private Button how_to_use       ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLang();

        new_observation = (Button) findViewById(R.id.entrance_new_observation);
        my_observations = (Button) findViewById(R.id.entrance_my_observations);
        tree_and_leaves = (Button) findViewById(R.id.entrance_leaves);
        how_to_use      = (Button) findViewById(R.id.entrance_how_to_use);

        if(lang)
        {
            new_observation.setText(eNEW_OBSERVATION);
            my_observations.setText(eMY_OBSERVATIONS);
            tree_and_leaves.setText(eTREES_AND_LEAVES);
            how_to_use.setText("HOW TO USE");
        }

        new_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newObservationMethod();
            }
        });

        my_observations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myObservationsMethod();
            }
        });

        tree_and_leaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leavesMethod();
            }
        });

        how_to_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                howToUseMethod();
            }
        });
    }

    void initLang()
    {
        SharedPreferences prefs = getSharedPreferences(MY_CONSTANTS, MODE_PRIVATE   )   ;
        boolean isFirstEntrance = prefs.getBoolean      (IS_FIRST_ENTRANCE, true    )   ;

        if(isFirstEntrance)
        {
            lang = !Locale.getDefault().getLanguage().equals("tr");

            SharedPreferences.Editor editor = prefs.edit()                          ;
            editor.putBoolean(LANGUAGE          , lang  )                           ;
            editor.putBoolean(IS_FIRST_ENTRANCE , false )                           ;
            editor.apply();

            Intent intent = new Intent(EntranceActivity.this, Tutorial.class);
            startActivity(intent);
        }
        else
            lang = prefs.getBoolean(LANGUAGE, false);

        setContentView(R.layout.entrance);
    }

    private void newObservationMethod()
    {
        Intent intent = new Intent(EntranceActivity.this, MainActivity.class);
        intent.putExtra(EXTRA, NEW_OBSERVATION);
        startActivity(intent);
    }
    private void myObservationsMethod()
    {
        Intent intent = new Intent(EntranceActivity.this, MainActivity.class);
        intent.putExtra(EXTRA, MY_OBSERVATIONS);
        startActivity(intent);
    }
    private void leavesMethod()
    {
        Intent intent = new Intent(EntranceActivity.this, MainActivity.class);
        intent.putExtra(EXTRA, TREES_AND_LEAVES);
        startActivity(intent);
    }
    private void howToUseMethod()
    {
        Intent intent = new Intent(EntranceActivity.this, Tutorial.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();

        if(lang)
        {
            new_observation.setText(eNEW_OBSERVATION);
            my_observations.setText(eMY_OBSERVATIONS);
            tree_and_leaves.setText(eTREES_AND_LEAVES);
            how_to_use.setText("HOW TO USE");
        }
        else
        {
            new_observation.setText("YENİ GÖZLEM");
            my_observations.setText("GÖZLEMLERİM");
            tree_and_leaves.setText("AĞAÇ VE YAPRAKLAR");
            how_to_use.setText("NASIL KULLANILIR");
        }
    }
}
