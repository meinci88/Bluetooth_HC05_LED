package com.example.tablayout_bluetooth_hc05;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.tablayout_bluetooth_hc05.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
	ItemViewModel viewModel;
	private FragmentHomeBinding binding;

	ArrayList<String> stringArrayList = new ArrayList<String>();
	ArrayAdapter<String> arrayAdapter;
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	BluetoothDevice bondDevice;

	@Override
	public void onResume() {
		super.onResume();
		if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
		}
		if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 4);
		}
		if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
		}
		IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		requireActivity().registerReceiver(mBroadcastReceiver4, filter4);

		IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		requireActivity().registerReceiver(mBroadcastReceiver5, filter5);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		binding = FragmentHomeBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		BluetoothAdapter frAdapter = BluetoothAdapter.getDefaultAdapter();
		viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

		binding.ScanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
				}
				mBluetoothAdapter.startDiscovery();
				stringArrayList.clear();
				arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArrayList);
				binding.listView.setAdapter(arrayAdapter);
			}
		});

		binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				int index = position;
				//BluetoothDevice device = null;
				Toast.makeText(getContext(), "Device" + stringArrayList.get(index), Toast.LENGTH_SHORT).show();
				if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
				}
				mBluetoothAdapter.cancelDiscovery();
				try {
					bondDevice.createBond();
				}catch (Exception e){

				}

				return false;
			}
		});

	}

	BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				bondDevice  = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
				}
				stringArrayList.add("Addr: " + bondDevice.getAddress() + " Name: " + bondDevice.getName());
				arrayAdapter.notifyDataSetChanged();
			}
		}
	};
	BroadcastReceiver mBroadcastReceiver5 = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
				}
				if (device.getBondState() == BluetoothDevice.BOND_BONDED) {

				}
				if (device.getBondState() == BluetoothDevice.BOND_BONDING){

				}
				if (device.getBondState() == BluetoothDevice.BOND_NONE){

				}
			}
		}
	};
	public void onStop() {
		super.onStop();
		requireActivity().unregisterReceiver(mBroadcastReceiver4);
	}
}

