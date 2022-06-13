package com.example.uniboo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int itemID;
    BottomNavigationView bottomNavigationView;
    ViewPager vp;
    int pagerNumber = 2;
    MenuItem previewMenu;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationID);
        vp = findViewById(R.id.viewPagerID);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayoutID);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.openDrawer,R.string.closeDrawer );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationViewID);
        navigationView.setNavigationItemSelectedListener(this);

        vp.setAdapter(new Adapter(getSupportFragmentManager()));
        vp.setOffscreenPageLimit(pagerNumber);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(previewMenu != null){
                    previewMenu.setChecked(false);
                }else{
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                previewMenu = bottomNavigationView.getMenu().getItem(position);
                if(vp.getCurrentItem() == 0){
                    toolbar.setTitle("Uniboo");
                }else if(vp.getCurrentItem() == 1){
                    toolbar.setTitle((String) getResources().getText(R.string.help));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeID:
                        vp.setCurrentItem(0);
                        break;

                    case R.id.helpID:
                        vp.setCurrentItem(1);
                        break;

                }
                return true;
            }
        });
    }


    public class Adapter extends FragmentPagerAdapter{
        public Adapter(@NonNull FragmentManager fm) {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position){
            switch(position){
                case 0:
                    return new HomeFragment();
                case 1:
                    return new HelpFragment();
            }
            return null;
        }
        @Override
        public int getCount(){
            return pagerNumber;
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        itemID = menuItem.getItemId();
        Intent intent = null;
        Fragment fragment = null;

        if (itemID == R.id.homeNavID) {
            intent = new Intent(this, MainActivity.class);
        } else if (itemID == R.id.accountID) {
            fragment = new MyAccountFragment();
        } else if (itemID == R.id.basketID) {
            fragment = new BasketFrag();
        } else if (itemID == R.id.aboutID) {
            fragment = new AboutFragment();
        } else if (itemID == R.id.privacyID) {
            fragment = new PrivacyFragment();
        } else if (itemID == R.id.logoutID) {
            fragment = new LogoutFragment();
        }


        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutID, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else{
            startActivity(intent);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayoutID);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_logout,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.logoutID){
            System.exit(1);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayoutID);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if(vp.getCurrentItem() != 0){
            vp.setCurrentItem((0), true);
        }else {
            super.onBackPressed();
        }
    }
    private void exit(){
        if((System.currentTimeMillis()-exitTime >2000)){
            Toast.makeText(this, "Press again to Exit", Toast.LENGTH_SHORT);
            exitTime = System.currentTimeMillis();
        }else{
            finish();
        }
    }
}