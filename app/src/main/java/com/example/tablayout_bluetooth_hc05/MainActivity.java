package com.example.tablayout_bluetooth_hc05;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tablayout_bluetooth_hc05.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
	static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	BluetoothSocket btSocket;

	Spinner spinner;
	TabLayout tabLayout;
	ViewPager2 viewPager2;
	MyViewPagerAdapter myViewPagerAdapter;
	BluetoothAdapter btAdapter;

	private ArrayList<String> arrayList;
	private ArrayList<String> arrayListname;
	private ArrayList<String> newarrayList;
	private ArrayAdapter<String> adapter;
	Dialog dialog;
	String item;
	String newitem;
	String itemname;
	TextView textView_Status;
	TextView textView_BoundedDev;
	FrameLayout frameLayout;

	String[] permissions = {"android.permission.BLUETOOTH_CONNECT"};
	private ItemViewModel viewModel;
	private ActivityMainBinding binding;

	private void DevList() {
		//*********************************************************************************************************
		if (ActivityCompat.checkSelfPermission(getApplicationContext(),
				android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
			//Hier sollte eine user Warnung stehen!
		}
		Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
		newarrayList.clear();
		//Listet die gebundenen BL-Geräte
		for (BluetoothDevice device : devices) {
			spinner.setAdapter(adapter);
			newarrayList.add(device.getAddress() + " ->" + (device.getName()));
			adapter.notifyDataSetChanged();
		}
		newarrayList.add(0, "Bitte Bluetoothgerät wählen");
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int positionAddressName, long id) {
				//itemAdr = arrayListName.get(position);//In Spinner ausgewählte Position(itemAdr)
				item = newarrayList.get(positionAddressName);
				//itemname = arrayListname.get(positionName);

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
	}
//*******************************************************************************************************************

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver1);
		unregisterReceiver(mBroadcastReceiver2);
		unregisterReceiver(mBroadcastReceiver3);
	}

	@Override
	protected void onStart() {
		super.onStart();
		ImageView imageView = findViewById(R.id.BL_Icon);
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		imageView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(MainActivity.this, "kein Bluetooth, soll es eingeschaltet werden?", Toast.LENGTH_LONG).show();
				if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {}
				mBluetoothAdapter.enable();
				return false;
				}
			});

			if (mBluetoothAdapter == null) {
				// Device does not support Bluetooth
			} else if (!mBluetoothAdapter.isEnabled()) {
				imageView.setBackgroundResource(R.drawable.bl_off_icon);
				//spinner.setEnabled(false);
				spinner.setVisibility(View.INVISIBLE);
			} else {
				imageView.setBackgroundResource(R.drawable.bl_on_icon);
				DevList();
			}
		}

		@Override
		protected void onCreate (Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			//setContentView(R.layout.activity_main);
			requestPermissions(permissions, 80);
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			binding = ActivityMainBinding.inflate(getLayoutInflater());
			View view = binding.getRoot();
			setContentView(view);

			btAdapter = BluetoothAdapter.getDefaultAdapter();

			if (!mBluetoothAdapter.isEnabled()){
				if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {}
				mBluetoothAdapter.enable();
				DevList();
			}

//*******Intentfilter1*****************************************************************
			IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			registerReceiver(mBroadcastReceiver1, filter1);
//*******Intentfilter1*****************************************************************

//*******Intentfilter2*****************************************************************
			IntentFilter filter2 = new IntentFilter();
			filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
			filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			filter2.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
			registerReceiver(mBroadcastReceiver2, filter2);
//*******Intentfilter2*****************************************************************

//*******Intentfilter3*****************************************************************
			IntentFilter filter3 = new IntentFilter();
			filter3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
			filter3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
			registerReceiver(mBroadcastReceiver3, filter3);
//*******Intentfilter3*****************************************************************

			textView_Status = findViewById(R.id.tv_Status);
			textView_BoundedDev = findViewById(R.id.tv_BoundedDevice);

			dialog = new Dialog(MainActivity.this);
			dialog.setContentView(R.layout.custom_dialog);
			dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
			dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			dialog.setCancelable(false);

			Button okey = dialog.findViewById(R.id.btn_okay);
			Button cancel = dialog.findViewById(R.id.btn_cancel);
			viewModel = new ViewModelProvider(this).get(ItemViewModel.class);


			viewModel.getSelectedItem().observe(this, item ->{
				try {
					OutputStream outputStream = btSocket.getOutputStream();
					outputStream.write(item.getBytes(StandardCharsets.UTF_8));
				} catch (Exception e) {}
			});

			viewModel.getSelectedItem1().observe(this, item1 ->{
				try {
					OutputStream outputStream = btSocket.getOutputStream();
					outputStream.write(item1.getBytes(StandardCharsets.UTF_8));
				} catch (Exception e) {}
			});

			viewModel.getSelectedItem2().observe(this, item2 ->{
				try {
					OutputStream outputStream = btSocket.getOutputStream();
					outputStream.write(item2.getBytes(StandardCharsets.UTF_8));
				} catch (Exception e) {}
			});

			myViewPagerAdapter = new MyViewPagerAdapter(this);
			binding.viewPager.setAdapter(myViewPagerAdapter);
			frameLayout = findViewById(R.id.frameLayout);

			//region Tablayout addOnTabSelectedListener
			binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

				@Override
				public void onTabSelected(TabLayout.Tab tab) {
					binding.viewPager.setVisibility(View.VISIBLE);
					binding.frameLayout.setVisibility(View.GONE);
					binding.viewPager.setCurrentItem(tab.getPosition());
				}

				@Override
				public void onTabUnselected(TabLayout.Tab tab) {
				}

				@Override
				public void onTabReselected(TabLayout.Tab tab) {
					binding.viewPager.setVisibility(View.VISIBLE);
					binding.frameLayout.setVisibility(View.GONE);
				}
			});

			//endregion

			okey.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Mit dem Bluetoothgerät verbinden
					newitem = item.substring(0,17);//item beinhaltet Devive adresse und name. newitem beinhaltet nur die Device adresse.
					BluetoothDevice BT_Device = btAdapter.getRemoteDevice(newitem);
					if (ActivityCompat.checkSelfPermission(getApplicationContext(),
							Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
					}
					try {
						btSocket = BT_Device.createRfcommSocketToServiceRecord(MY_UUID);
						btSocket.connect();
					} catch (IOException e) {
						Toast.makeText(MainActivity.this, "konnte nicht verbinden", Toast.LENGTH_SHORT).show();
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

			newarrayList = new ArrayList<>();
			//arrayListname = new ArrayList<>();
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newarrayList);


			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
				//Hier sollte eine user Warnung stehen!

			}
			//(value = "android.permission.BLUETOOTH_CONNECT")

			Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
			newarrayList.clear();
			//Listet die gebundenen BL-Geräte
			for (BluetoothDevice device : devices) {
				spinner.setAdapter(adapter);
				//arrayListname.add(device.getName() + ":  " + String.valueOf(device));
				newarrayList.add(device.getAddress());
				adapter.notifyDataSetChanged();
			}
			newarrayList.add(0, "Bitte Bluetoothgerät wählen");
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					item = newarrayList.get(position);//In Spinner ausgewählte Position(item)
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
					switch (state) {
						case BluetoothAdapter.STATE_OFF:
							Toast.makeText(MainActivity.this, "Bluetooth ausgeschaltet", Toast.LENGTH_SHORT).show();
							textView_BoundedDev.setText("");
							imageView.setBackgroundResource(R.drawable.bl_off_icon);
							//spinner.setEnabled(false);
							spinner.setVisibility(View.INVISIBLE);
							break;
						case BluetoothAdapter.STATE_TURNING_OFF:
							Toast.makeText(MainActivity.this, "Bluetooth wird im moment ausgeschaltet", Toast.LENGTH_SHORT).show();
							break;
						case BluetoothAdapter.STATE_ON:
							Toast.makeText(MainActivity.this, "Bluetooth eingeschaltet", Toast.LENGTH_SHORT).show();
							imageView.setBackgroundResource(R.drawable.bl_on_icon);
							//spinner.setEnabled(true);
							spinner.setVisibility(View.VISIBLE);
							break;
						case BluetoothAdapter.STATE_TURNING_ON:
							Toast.makeText(MainActivity.this, "Bluetooth wird im moment eingeschaltet", Toast.LENGTH_LONG).show();
							break;
					}
				}
			}
		};

		private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();

				if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

					int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

					switch (mode) {
						case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
							Toast.makeText(MainActivity.this, "SCAN_MODE_CONNECTABLE_DISCOVERABLE", Toast.LENGTH_LONG).show();
							break;
						case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
							Toast.makeText(MainActivity.this, "SCAN_MODE_CONNECTABLE", Toast.LENGTH_LONG).show();
							break;
						case BluetoothAdapter.SCAN_MODE_NONE:
							Toast.makeText(MainActivity.this, "SCAN_MODE_NONE", Toast.LENGTH_LONG).show();
							break;
					}
				}
			}
		};

		private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				switch (action) {
					case BluetoothDevice.ACTION_ACL_CONNECTED:
						Toast.makeText(MainActivity.this, "ACTION_ACL_CONNECTED", Toast.LENGTH_LONG).show();
						textView_BoundedDev.setText(item);
						break;
					case BluetoothDevice.ACTION_ACL_DISCONNECTED:
						Toast.makeText(MainActivity.this, "ACTION_ACL_DISCONNECTED", Toast.LENGTH_LONG).show();
						textView_BoundedDev.setText("");
						break;
				}
			}
		};
	}




