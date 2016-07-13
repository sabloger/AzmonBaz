package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Adapters.MainActivityNavLVAdapter;
import com.kokabi.p.azmonbaz.Fragments.AboutFragment;
import com.kokabi.p.azmonbaz.Fragments.CoursesFragment;
import com.kokabi.p.azmonbaz.Fragments.FavoritesFragment;
import com.kokabi.p.azmonbaz.Fragments.HistoryFragment;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Help.CustomSnackBar;
import com.kokabi.p.azmonbaz.Objects.MainActivityNavObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    ActionBar actionbar;
    boolean doubleBackToExitPressedOnce = false;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public static CustomSnackBar snackBar;

    CoordinatorLayout mainContent;
    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    Toolbar toolBar;
    AppCompatImageButton menu_imgbtn;
    public static TextView title_tv;
    ListView lvNav;

    /*Activity Values*/
    public static FragmentManager fragmentManager;
    int selectedNavItem = 0;
    int lastSelectedItem = 0;
    boolean isNavItemSelected = false;
    MainActivityNavLVAdapter mainActivityNavLVAdapter;
    ArrayList<MainActivityNavObj> listMainActivityNavObj;
    ArrayList<Fragment> listFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        AppController.setActivityContext(MainActivity.this, this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        findViews();

        /*Make Lists show Scroll from left*/
        lvNav.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);

        /*Setting Custom ActionBar*/
        setSupportActionBar(toolBar);
        actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);

        /*Fill NavListView with Items*/
        listMainActivityNavObj = new ArrayList<>();
        listMainActivityNavObj.add(new MainActivityNavObj("آزمون", R.drawable.dr_test));
        listMainActivityNavObj.add(new MainActivityNavObj("آزمون‌های منتخب شما", R.drawable.dr_favorite));
        listMainActivityNavObj.add(new MainActivityNavObj("تاریخچه ی آزمون‌های شما", R.drawable.dr_history));
        listMainActivityNavObj.add(new MainActivityNavObj(true));
        listMainActivityNavObj.add(new MainActivityNavObj("درباره", R.drawable.dr_aboutus));
        listMainActivityNavObj.add(new MainActivityNavObj("معرفی به دوستان", R.drawable.dr_share));
        listMainActivityNavObj.add(new MainActivityNavObj("تنظیمات", R.drawable.dr_setting));

        mainActivityNavLVAdapter = new MainActivityNavLVAdapter(context, listMainActivityNavObj);
        lvNav.setAdapter(mainActivityNavLVAdapter);

        listFragments.add(new CoursesFragment());
        listFragments.add(new FavoritesFragment());
        listFragments.add(new HistoryFragment());
        listFragments.add(new AboutFragment());

        /*Load first fragment as default*/
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(0)).commit();
        drawerLayout.closeDrawer(drawerPane);

        /*Set Listener for navigation*/
        lvNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedNavItem = position;
                isNavItemSelected = true;
                changeNavSelectedColor(position);
                drawerLayout.closeDrawer(drawerPane);
            }
        });

        /*Create listener for drawer layout*/
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (isNavItemSelected) {
                    navItemController();
                }
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }

        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

    }

    @Override
    protected void onResume() {
        super.onResume();
        changeNavSelectedColor(lastSelectedItem);
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            if (selectedNavItem != 0) {
                drawerLayout.closeDrawer(drawerPane);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(0)).commit();
                lvNav.setItemChecked(0, true);
                selectedNavItem = 0;
                changeNavSelectedColor(selectedNavItem);
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
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                    Constants.hideKeyboard();
                }
                break;
        }
    }

    private void findViews() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerPane = (RelativeLayout) findViewById(R.id.drawerPane);

        lvNav = (ListView) findViewById(R.id.navList);

        menu_imgbtn = (AppCompatImageButton) findViewById(R.id.menu_imgbtn);

        title_tv = (TextView) findViewById(R.id.title_tv);

        setOnClick();
    }

    private void setOnClick() {
        menu_imgbtn.setOnClickListener(this);
    }

    /*Additional Methods==========================================================================*/
    public void navItemController() {
        isNavItemSelected = false;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (selectedNavItem) {
            /*Test Fragment*/
            case 0:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(0)).commit();
                lastSelectedItem = 0;
                break;
            /*Favorites Fragment*/
            case 1:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(1)).commit();
                lastSelectedItem = 1;
                break;
            /*History Fragment*/
            case 2:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(2)).commit();
                lastSelectedItem = 2;
                break;
            /*About Fragment*/
            case 4:
                fragmentManager.beginTransaction().replace(R.id.mainContent, listFragments.get(1)).commit();
                lastSelectedItem = 2;
                break;
            /*Sharing Application*/
            case 5:
                String shareBody = "آزمون باز";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "انتخاب برنامه"));
                break;
            /*Setting*/
            case 6:
//                Intent intent = new Intent(context, Setting.class);
//                startActivity(intent);
                break;
        }

    }

    /*get view of selected item in ListView*/
    public View getViewByPosition(int position, ListView listView) {

        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }

    }

    public void changeNavSelectedColor(int position) {
        View view1 = getViewByPosition(position, lvNav);
        TextView navTitle = (TextView) view1.findViewById(R.id.navTitle_tv);
        ImageView navIcon = (ImageView) view1.findViewById(R.id.navIcon_imgv);

        navTitle.setTextColor(context.getResources().getColor(R.color.accentColor));
        navIcon.setColorFilter(context.getResources().getColor(R.color.accentColor));

        for (int i = 0; i < mainActivityNavLVAdapter.getCount(); i++) {
            if (i != position) {
                View view2 = getViewByPosition(i, lvNav);
                TextView navTitle1 = (TextView) view2.findViewById(R.id.navTitle_tv);
                ImageView navIcon1 = (ImageView) view2.findViewById(R.id.navIcon_imgv);

                navTitle1.setTextColor(context.getResources().getColor(R.color.darkGray));
                navIcon1.setColorFilter(context.getResources().getColor(R.color.darkGray));
            }
        }
    }

}
