package com.example.tablayout_bluetooth_hc05;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPagerAdapter extends FragmentStateAdapter {
	public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
		super(fragmentActivity);
	}

	@NonNull
	@Override
	public Fragment createFragment(int position) {
		switch (position){
			case 0:
				return new HomeFragment();
			case 1:
				return  new LichteffekteFragment();
			case 2:
				return new SettingsFragment();
			default:
				return new HomeFragment();
		}
	}

	@Override
	public int getItemCount() {
		return 3;
	}
}
