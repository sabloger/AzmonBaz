package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Fragments.AboutFragment;
import com.kokabi.p.azmonbaz.Fragments.CoursesFragment;
import com.kokabi.p.azmonbaz.Fragments.FavoredQuestionFragment;
import com.kokabi.p.azmonbaz.Fragments.FavoritesFragment;
import com.kokabi.p.azmonbaz.Fragments.HistoryFragment;
import com.kokabi.p.azmonbaz.Fragments.SavedTestFragment;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Help.CustomSnackBar;
import com.kokabi.p.azmonbaz.Help.CustomTypefaceSpan;
import com.kokabi.p.azmonbaz.Help.FontChange;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    ActionBar actionbar;
    boolean doubleBackToExitPressedOnce = false;
    public static CustomSnackBar snackBar;

    CoordinatorLayout mainContent;
    DrawerLayout drawerLayout;
    NavigationView navDrawer;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolBar;
    AppCompatImageButton menu_imgbtn;
    public static TextView title_tv;

    /*Activity Values*/
    ArrayList<Fragment> listFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        AppController.setActivityContext(MainActivity.this, this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        findViews();
        setupDrawer();
        setupDrawerContent(navDrawer);
        setMenuFont();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.freeMemory();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!navDrawer.getMenu().findItem(R.id.dr_test).isChecked()) {
                drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(0)).commit();
                navDrawer.getMenu().findItem(R.id.dr_test).setChecked(true);
                title_tv.setText(navDrawer.getMenu().findItem(R.id.dr_test).getTitle());
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;

                snackBar = new CustomSnackBar(mainContent, "لطفا برای خروج  مجددا دکمه بازگشت را فشار دهید.", Constants.SNACK.WARNING);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_imgbtn:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }

    private void findViews() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        navDrawer = (NavigationView) findViewById(R.id.navDrawer);

        menu_imgbtn = (AppCompatImageButton) findViewById(R.id.menu_imgbtn);

        title_tv = (TextView) findViewById(R.id.title_tv);

        setOnClick();
    }

    private void setOnClick() {
        menu_imgbtn.setOnClickListener(this);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.drawer_opened, R.string.drawer_closed);
    }

    private void setupDrawer() {
        /*Setting Custom ActionBar*/
        setSupportActionBar(toolBar);
        actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);

        // Find our drawer view
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        drawerLayout.addDrawerListener(drawerToggle);

        listFragments.add(new CoursesFragment());
        listFragments.add(new FavoritesFragment());
        listFragments.add(new FavoredQuestionFragment());
        listFragments.add(new HistoryFragment());
        listFragments.add(new SavedTestFragment());
        listFragments.add(new AboutFragment());

        /*Load first fragment as default*/
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContent, listFragments.get(0)).commit();
        title_tv.setText(navDrawer.getMenu().findItem(R.id.dr_test).getTitle());
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (menuItem.getItemId()) {
            /*Test Fragment*/
            case R.id.dr_test:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(0)).commit();
                break;
            /*Favorites Fragment*/
            case R.id.dr_favorite:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(1)).commit();
                break;
            /*FavoredQuestion Fragment*/
            case R.id.dr_favored_question:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(2)).commit();
                break;
            /*History Fragment*/
            case R.id.dr_history:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(3)).commit();
                break;
            /*SavedTest Fragment*/
            case R.id.dr_saved_test:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(4)).commit();
                break;
            /*About Fragment*/
            case R.id.dr_about:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(5)).commit();
                break;
            case R.id.dr_share:
                String shareBody = "آزمون باز";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری ازطریق"));
                break;
            default:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(0)).commit();
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        title_tv.setText(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = FontChange.getTypeface(Constants.font.SANS, this);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void setMenuFont() {
        Menu m = navDrawer.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            applyFontToMenuItem(mi);
        }
    }
}
