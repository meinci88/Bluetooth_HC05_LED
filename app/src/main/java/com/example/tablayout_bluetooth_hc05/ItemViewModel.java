package com.example.tablayout_bluetooth_hc05;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
	private final MutableLiveData<String> seekbar1 = new MutableLiveData<String>();
	private final MutableLiveData<String> seekbar2 = new MutableLiveData<String>();
	private final MutableLiveData<String> seekbar3 = new MutableLiveData<String>();

	public void setData(String seekBar1Value){
		seekbar1.setValue(seekBar1Value);
	}
	public LiveData<String> seekbar1(){
		return seekbar1;
	}

	public void setData1(String seekbar2Value){
		seekbar2.setValue(seekbar2Value);
	}
	public LiveData<String> seekbar2(){
		return seekbar2;
	}

	public void setData2(String seekbar3Value){
		seekbar3.setValue(seekbar3Value);
	}
	public LiveData<String> seekbar3(){
		return seekbar3;
	}
}
