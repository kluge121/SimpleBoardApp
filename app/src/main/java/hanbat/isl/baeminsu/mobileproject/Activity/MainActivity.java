package hanbat.isl.baeminsu.mobileproject.Activity;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import hanbat.isl.baeminsu.mobileproject.Entity.LoginEntity;
import hanbat.isl.baeminsu.mobileproject.Fragment.MainTab1;
import hanbat.isl.baeminsu.mobileproject.Fragment.MainTab2;
import hanbat.isl.baeminsu.mobileproject.Fragment.MainTabPagerAdapter;
import hanbat.isl.baeminsu.mobileproject.R;
import hanbat.isl.baeminsu.mobileproject.Set.PropertyManager;
import hanbat.isl.baeminsu.mobileproject.Set.UserInfo;

public class MainActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    DrawerLayout drawerLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    SearchView searchView;
    MainTabPagerAdapter mainTabPagerAdapter;
    Button mySell;
    Button myBuy;
    Button logout;
    TextView id;
    TextView name;

    RelativeLayout main_layout;


    public static FloatingActionButton fab;

    @Override
    public boolean onSupportNavigateUp() {

        drawerLayout.openDrawer(GravityCompat.START);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - backPressedTime;


        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else

        {
            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = currentTime;
                Toast.makeText(this, "한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent;
                if (tabLayout.getSelectedTabPosition() == 0) {
                    intent = new Intent(getApplicationContext(), SellSearchResultActivity.class);

                } else {
                    intent = new Intent(getApplicationContext(), BuySearchResultActivity.class);
                }

                intent.putExtra("keyword", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_main_drawer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name2);


        setWidget();


    }

    void setWidget() {
        mySell = findViewById(R.id.my_sell);
        myBuy = findViewById(R.id.my_buy);
        logout = findViewById(R.id.my_logout);
        main_layout = findViewById(R.id.main_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        fab = findViewById(R.id.main_fab);
        id = findViewById(R.id.drawer_id);
        name = findViewById(R.id.drawer_name);
        tabLayout = findViewById(R.id.main_tab);
        viewPager = findViewById(R.id.main_viewpager);
        tabLayout.setupWithViewPager(viewPager);
        setTabViewPager(viewPager);

        id.setText(UserInfo.getUserInfo().getId());
        name.setText(UserInfo.getUserInfo().getName());





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (tabLayout.getSelectedTabPosition() == 0) {
                    intent = new Intent(getApplicationContext(), WriteSellActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), WriteBuyActivity.class);
                    startActivity(intent);
                }
            }
        });
        mySell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellMyResultActivity.class);
                startActivity(intent);
            }
        });

        myBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuyMyResultActivity.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyManager.getInstance().setAutoLogin(false);
                UserInfo userInfo = UserInfo.getUserInfo();
                userInfo.initInfo();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    private void setTabViewPager(ViewPager viewPager) {
        mainTabPagerAdapter = new MainTabPagerAdapter(getSupportFragmentManager());

        mainTabPagerAdapter.addFragment(MainTab1.newInstance());
        mainTabPagerAdapter.addFragment(MainTab2.newInstance());


        viewPager.setAdapter(mainTabPagerAdapter);

    }


}
