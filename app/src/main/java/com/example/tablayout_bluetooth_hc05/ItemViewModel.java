package com.example.tablayout_bluetooth_hc05;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
	private final MutableLiveData<String> selectedItem = new MutableLiveData<String>();
	private final MutableLiveData<String> selectedItem_1 = new MutableLiveData<String>();
	private final MutableLiveData<String> selectedItem_2 = new MutableLiveData<String>();
	private final MutableLiveData<String> nameInput = new MutableLiveData<String>();
	//region setData

	public void setName(String name){
		nameInput.setValue(name);
	}
	public LiveData<String> getName(){
		return nameInput;
	}


	public void setData(String item){
		selectedItem.setValue(item);
	}
	public LiveData<String> getSelectedItem(){
		return selectedItem;
	}


	public void setData1(String item){
		selectedItem_1.setValue(item);
	}
	public LiveData<String> getSelectedItem1(){
		return selectedItem_1;
	}


	public void setData2(String item){
		selectedItem_2.setValue(item);
	}
	public LiveData<String> getSelectedItem2(){
		return selectedItem_2;
	}
	//endregion
}
