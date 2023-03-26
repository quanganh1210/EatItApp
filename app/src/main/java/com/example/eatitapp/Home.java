package com.example.eatitapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    CartFragment cartFragment = new CartFragment();
    OrderFragment orderFragment = new OrderFragment();
    MeFragment meFragment = new MeFragment();

    //    private ViewPager viewPager;
//    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

                        return true;
                    case R.id.cart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, cartFragment).commit();
                        return true;
                    case R.id.order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, orderFragment).commit();
                        return true;
                    case R.id.me:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, meFragment).commit();
                        return true;
                }
                return false;
            }
        });


//        viewPager = findViewById(R.id.viewPager);
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//
//        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        viewPager.setAdapter(adapter);
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
//                        break;
//                    case 1:
//                        bottomNavigationView.getMenu().findItem(R.id.cart).setChecked(true);
//                        break;
//                    case 2:
//                        bottomNavigationView.getMenu().findItem(R.id.order).setChecked(true);
//                        break;
//                    case 3:
//                        bottomNavigationView.getMenu().findItem(R.id.me).setChecked(true);
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.home:
//                        viewPager.setCurrentItem(0);
//                        break;
//                    case R.id.cart:
//                        viewPager.setCurrentItem(1);
//                        break;
//                    case R.id.order:
//                        viewPager.setCurrentItem(2);
//                        break;
//                    case R.id.me:
//                        viewPager.setCurrentItem(3);
//                        break;
//                }
//                return true;
//            }
//        });
//    }
    }
}