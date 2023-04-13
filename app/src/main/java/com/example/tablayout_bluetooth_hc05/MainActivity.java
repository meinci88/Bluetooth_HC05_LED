package com.example.tablayout_bluetooth_hc05;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
	Spinner spinner;
	TabLayout tabLayout;
	ViewPager2 viewPager2;
	MyViewPagerAdapter myViewPagerAdapter;
	BluetoothAdapter btAdapter;
	private ArrayList<String> arrayList;
	private ArrayAdapter<String> adapter;
	Dialog dialog;
	String item;
	TextView textView;


	@SuppressLint("MissingInflatedId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(false);


		Button okey = dialog.findViewById(R.id.btn_okay);
		Button cancel = dialog.findViewById(R.id.btn_cancel);

		okey.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Mit dem Bluetoothgerät verbinden
				Toast.makeText(MainActivity.this, "wird mit: " + item +" verbunden", Toast.LENGTH_SHORT).show();

				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Mit dem Bluetoothgerät nicht verbinden
				Toast.makeText(MainActivity.this, "wurde nicht verbunden", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});

		spinner = findViewById(R.id.spinner);
		tabLayout = findViewById(R.id.tabLayout);
		viewPager2 = findViewById(R.id.view_pager);
		myViewPagerAdapter = new MyViewPagerAdapter(this);
		viewPager2.setAdapter(myViewPagerAdapter);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		String[] permissions = {"android.permission.BLUETOOTH_CONNECT"};

		arrayList = new ArrayList<>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

		if (ActivityCompat.checkSelfPermission(getApplicationContext(),
				android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
			//Hier sollte eine user Warnung stehen!
		}
		Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
		arrayList.clear();
		//Listet die gebundenen BL-Geräte
		for (BluetoothDevice device:devices){
			spinner.setAdapter(adapter);
			arrayList.add(device.getName() + ":  " + String.valueOf(device));
			adapter.notifyDataSetChanged();
		}
		arrayList.add(0,"Bitte Bluetoothgerät wählen");
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				item = arrayList.get(position).toString();
				Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
				if (item != "Bitte Bluetoothgerät wählen"){
					TextView textView_dialog = dialog.findViewById(R.id.textView2);
					textView_dialog.setText("Möchten Sie mit : " + item + " verbinden?");//Text im Dialogfenster
					dialog.show();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager2.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});

		viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				tabLayout.getTabAt(position).select();
			}
		});

		if (btAdapter.isEnabled()){
			requestPermissions(permissions, 80);
		}

	}
}