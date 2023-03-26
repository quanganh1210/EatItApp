package com.example.eatitapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.eatitapp.Adapter.OrderViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class OrderFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_order, container, false);
        mView = inflater.inflate(R.layout.fragment_order, container, false);
        tabLayout = mView.findViewById(R.id.tabLayout);
        viewPager = mView.findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new WaittingOrderFragment(), "Waitting");
        adapter.addFragment(new OnGoingFragment(), "On going");
        adapter.addFragment(new HistoryOrderFragment(), "History");
        viewPager.setAdapter(adapter);
        //viewPager.setSaveEnabled(false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
}