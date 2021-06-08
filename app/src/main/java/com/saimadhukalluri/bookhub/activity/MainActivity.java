package com.saimadhukalluri.bookhub.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.saimadhukalluri.bookhub.R;
import com.saimadhukalluri.bookhub.fragment.AboutFragment;
import com.saimadhukalluri.bookhub.fragment.DashboardFragment;
import com.saimadhukalluri.bookhub.fragment.FavouriteFragment;
import com.saimadhukalluri.bookhub.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    ImageView imageViewBook;
    String toolBarTitle = "Tool bar as Action Bar";
    MenuItem prevMenuItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        toolbar = findViewById(R.id.toolBar);
        frameLayout = findViewById(R.id.frameLayout);
        imageViewBook = findViewById(R.id.iv_book_logo);

        setUpToolbar();
        openDashboard();


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {

            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            }
            item.setChecked(true);
            prevMenuItem = item;

            switch (item.getItemId()) {
                case R.id.dashboard:
                    openDashboard();
                    break;
                case R.id.favourites:
                    openFavourite();
                    break;
                case R.id.profile:
                    openProfile();
                    break;
                case R.id.about:
                    openAbout();
                    break;
            }
            return true;
        });
    }

    //setting up the toolbar
    void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(toolBarTitle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //setting up the functionality of the hamburger icon on clicking
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    //The below are the function for opening Different Fragmentation's
    void openDashboard() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new DashboardFragment())
                .commit();
        drawerLayout.closeDrawers();
        getSupportActionBar().setTitle("Dashboard");
        navigationView.setCheckedItem(R.id.dashboard);
    }

    void openFavourite() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new FavouriteFragment())
                .commit();
        drawerLayout.closeDrawers();
        getSupportActionBar().setTitle("Favourites");
    }

    void openProfile() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new ProfileFragment())
                .commit();
        drawerLayout.closeDrawers();
        getSupportActionBar().setTitle("Profile");
    }

    void openAbout() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new AboutFragment())
                .commit();
        drawerLayout.closeDrawers();
        getSupportActionBar().setTitle("About");
    }

    /*the below is for checking if the current is not dashboard then moving back to dashboard
    or if it is dashboard then exiting the app this is existing method
     */
    @Override
    public void onBackPressed() {
        Fragment fragId = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragId instanceof DashboardFragment) {
            super.onBackPressed();
        } else {
            prevMenuItem.setChecked(false);
            openDashboard();
        }
    }
}