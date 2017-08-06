package com.payinekereg.treelogy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.tabs.ListTreesTAB;
import com.payinekereg.treelogy.tabs.MyObservationsTAB;
import com.payinekereg.treelogy.tabs.NewObservationTAB;

import static com.payinekereg.treelogy.constants.MyConstants.EXTRA;

import static com.payinekereg.treelogy.constants.Constants_English.eNEW_OBSERVATION  ;
import static com.payinekereg.treelogy.constants.Constants_English.eMY_OBSERVATIONS  ;
import static com.payinekereg.treelogy.constants.Constants_English.eTREES_AND_LEAVES ;
import static com.payinekereg.treelogy.constants.MyConstants.LANGUAGE;
import static com.payinekereg.treelogy.constants.MyConstants.MY_CONSTANTS;

public class MainActivity extends AppCompatActivity {

    private boolean lang = EntranceActivity.lang;
    private DrawerLayout mDrawerLayout;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
        {
            if(lang)
                navigationView.inflateMenu(R.menu.drawer_view_en);
            else
                navigationView.inflateMenu(R.menu.drawer_view_tr);

            setupDrawerContent(navigationView);
        }


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final int extra = getIntent().getIntExtra(EXTRA, 1);
        tabLayout.getTabAt(extra).select();
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        if(lang)
        {
            adapter.addFragment(new NewObservationTAB()     , eNEW_OBSERVATION  );
            adapter.addFragment(new ListTreesTAB()          , eTREES_AND_LEAVES );
            adapter.addFragment(new MyObservationsTAB()     , eMY_OBSERVATIONS  );
        }
        else
        {
            adapter.addFragment(new NewObservationTAB()     , "YENİ GÖZLEM"         );
            adapter.addFragment(new ListTreesTAB()          , "AĞAÇ VE YAPRAKLAR"   );
            adapter.addFragment(new MyObservationsTAB()     , "GÖZLEMLERİM"         );
        }
        viewPager.setAdapter(adapter);
    }

    private class Adapter extends FragmentPagerAdapter {
        private final List<Fragment>    mFragments      = new ArrayList<>();
        private final List<String>      mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.nav_back)
                    finish();
                else
                {
                    if(menuItem.getItemId() == R.id.nav_new_observation)
                        tabLayout.getTabAt(0).select();
                    else if(menuItem.getItemId() == R.id.nav_trees_and_leaves)
                        tabLayout.getTabAt(1).select();
                    else if(menuItem.getItemId() == R.id.nav_my_observations)
                        tabLayout.getTabAt(2).select();

                    else if(menuItem.getItemId() == R.id.nav_lang1)
                    {
                        if(lang)
                            changeLanguage();
                    }
                    else if(menuItem.getItemId() == R.id.nav_lang2)
                    {
                        if(!lang)
                            changeLanguage();
                    }
                    else if(menuItem.getItemId() == R.id.nav_tutorial)
                    {
                        Intent intent = new Intent(MainActivity.this, Tutorial.class);
                        startActivity(intent);
                    }
                    else if(menuItem.getItemId() == R.id.nav_contribute)
                    {
                        Intent intent = new Intent(MainActivity.this, Contribute.class);
                        startActivity(intent);
                    }

                    mDrawerLayout.closeDrawers();
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage()
    {
        SharedPreferences prefs = getSharedPreferences(MY_CONSTANTS, MODE_PRIVATE   )   ;
        lang = !prefs.getBoolean(LANGUAGE, false)                                       ;

        EntranceActivity.lang = lang                                                    ;

        SharedPreferences.Editor editor = prefs.edit()                                  ;
        editor.putBoolean(LANGUAGE          , lang  )                                   ;
        editor.apply()                                                                  ;

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
