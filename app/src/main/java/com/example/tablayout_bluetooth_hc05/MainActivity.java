package com.example.tablayout_bluetooth_hc05;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
	static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


	Spinner spinner;
	TabLayout tabLayout;
	ViewPager2 viewPager2;
	MyViewPagerAdapter myViewPagerAdapter;

	private ArrayList<String> arrayList;
	private ArrayList<String> arrayListname;
	private ArrayAdapter<String> adapter;
	Dialog dialog;
	String item;
	TextView textView_Status;
	TextView textView_BoundedDev;



	String connectedDeviceName;
	String connectedDeviceAddress;

	BluetoothSocket btSocket = null;
	BluetoothAdapter btAdapter;
	String[] permissions = {"android.permission.BLUETOOTH_CONNECT"};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mBroadcastReceiver1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(mBroadcastReceiver1, filter1);


		textView_Status = findViewById(R.id.tv_Status);
		textView_BoundedDev = findViewById(R.id.tv_BoundedDevice);



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
				BluetoothDevice BT_Device = btAdapter.getRemoteDevice(item);
				if (ActivityCompat.checkSelfPermission(getApplicationContext(),
						Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
				}
				try {
					btSocket = BT_Device.createRfcommSocketToServiceRecord(MY_UUID);

					if (btSocket.isConnected() == false) {
						textView_BoundedDev.setText("");
						btSocket.connect();
						if (btSocket.isConnected() == true) {
							connectedDeviceName = btSocket.getRemoteDevice().getName();
							connectedDeviceAddress = btSocket.getRemoteDevice().getAddress();
							textView_BoundedDev.setText(connectedDeviceName);
						} else {
							textView_BoundedDev.setText("");
						}
					} else {
						textView_BoundedDev.setText("");
						connectedDeviceName = btSocket.getRemoteDevice().getName();
						textView_BoundedDev.setText(connectedDeviceName);
					}
				} catch (IOException e) {
					//Toast.makeText(MainActivity.this, "leider nicht gebunden!", Toast.LENGTH_SHORT).show();
					textView_BoundedDev.setText("");
				}
				dialog.dismiss();
				spinner.setSelection(0);
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Mit dem Bluetoothgerät nicht verbinden
				Toast.makeText(MainActivity.this, "wurde nicht verbunden", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				spinner.setSelection(0);
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
		for (BluetoothDevice device : devices) {
			spinner.setAdapter(adapter);
			//arrayListname.add(device.getName() + ":  " + String.valueOf(device));
			arrayList.add(device.getAddress());
			adapter.notifyDataSetChanged();
		}
		arrayList.add(0, "Bitte Bluetoothgerät wählen");
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				item = arrayList.get(position);//In Spinner ausgewählte Position(item)
				if (item != "Bitte Bluetoothgerät wählen") {
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

		if (btAdapter.isEnabled()) {
			requestPermissions(permissions, 80);
		}
	}

	private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			ImageView imageView = findViewById(R.id.BL_Icon);
			final String action = intent.getAction();

			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				switch(state) {
					case BluetoothAdapter.STATE_OFF:
						Toast.makeText(MainActivity.this, "Bluetooth ausgeschaltet", Toast.LENGTH_SHORT).show();
						imageView.setBackgroundResource(R.drawable.bl_off_icon);
						break;
					case BluetoothAdapter.STATE_TURNING_OFF:
						Toast.makeText(MainActivity.this, "Bluetooth wird im moment ausgeschaltet", Toast.LENGTH_SHORT).show();
						break;
					case BluetoothAdapter.STATE_ON:
						Toast.makeText(MainActivity.this, "Bluetooth eingeschaltet", Toast.LENGTH_SHORT).show();
						imageView.setBackgroundResource(R.drawable.bl_on_icon);
						break;
					case BluetoothAdapter.STATE_TURNING_ON:
						Toast.makeText(MainActivity.this, "Bluetooth wird im moment eingeschaltet", Toast.LENGTH_LONG).show();
						break;
				}
			}
		}
	};
}




