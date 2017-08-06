package com.payinekereg.treelogy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.payinekereg.treelogy.R;

/**
 * Created by Emre on 4/16/2016.
 */
public class Tutorial extends AppCompatActivity{

    private boolean lang = EntranceActivity.lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

        setLang();
    }

    private void setLang()
    {
        Button btn = (Button) findViewById(R.id.tutorial_btn);

        if(lang)
        {
            TextView tv1 = (TextView) findViewById(R.id.tutorial_tv1);
            TextView tv20 = (TextView) findViewById(R.id.tutorial_tv20);
            TextView tv22 = (TextView) findViewById(R.id.tutorial_tv22);

            tv1.setText("Valid Observation");
            tv20.setText("Just put the leaf on a white paper");
            tv22.setText("Take a photo");
            btn.setText("Got it");
        }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
    }
}
