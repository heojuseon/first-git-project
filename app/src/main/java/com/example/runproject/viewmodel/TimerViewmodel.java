package com.example.runproject.viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.runproject.model.TimerService;


public class TimerViewmodel extends ViewModel {

    public MutableLiveData<String> time_count = new MutableLiveData<>("00:00:00");

}
