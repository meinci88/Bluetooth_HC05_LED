package com.example.tablayout_bluetooth_hc05;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.tablayout_bluetooth_hc05.databinding.FragmentLichtEffekteBinding;
import com.example.tablayout_bluetooth_hc05.databinding.FragmentLichteffekteBinding;

import java.io.IOException;

public class LichteffekteFragment extends Fragment {
	ItemViewModel viewModel;

	private FragmentLichteffekteBinding binding;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		binding = FragmentLichteffekteBinding.inflate(inflater, container, false);
		View view = binding.getRoot();

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

		binding.seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				TextView textView;

				String value = "A" + progress;
				binding.seekBar2.setProgress(0);
				binding.seekBar3.setProgress(0);
				binding.textViewSeekbarVal1.setText(value);
				viewModel.setData(value);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		binding.seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				String value = "B" + progress;
				binding.seekBar1.setProgress(0);
				binding.seekBar3.setProgress(0);
				binding.textViewSeekbarVal2.setText(value);
				viewModel.setData1(value);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		binding.seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				String value = "C" + progress;
				binding.seekBar1.setProgress(0);
				binding.seekBar2.setProgress(0);
				binding.textViewSeekbarVal3.setText(value);
				viewModel.setData2(value);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
	}
}