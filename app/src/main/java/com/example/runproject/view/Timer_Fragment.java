package com.example.runproject.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.runproject.R;
import com.example.runproject.databinding.FragmentTimerBinding;
import com.example.runproject.model.TimerService;
import com.example.runproject.viewmodel.TimerViewmodel;

public class Timer_Fragment extends Fragment {
    FragmentTimerBinding timerBinding;
    TimerViewmodel timerViewmodel;
    Intent serviceIntent;

    private boolean timerStarted = false;
    private double time = 0.0;


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        timerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false);
        timerBinding.setLifecycleOwner(this);

        timerViewmodel = new ViewModelProvider(this).get(TimerViewmodel.class);
        timerBinding.setVm(timerViewmodel);


        serviceIntent = new Intent(requireContext(), TimerService.class);
        requireActivity().registerReceiver(updateTime, new IntentFilter(TimerService.TIMER_UPDATED));



        timerBinding.startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if(timerStarted)
                        stopTimer();
                    else
                        startTimer();
                }

            }
        });
        timerBinding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });


        return timerBinding.getRoot();
    }

    private void resetTimer() {
        stopTimer();
        time = 0.0;
        timerBinding.timeTV.setText(getTimeStringFromDouble(time));
    }

    private void startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time);
        requireContext().startService(serviceIntent);
        timerBinding.startStopButton.setText("stop");
        timerStarted = true;
    }

    private void stopTimer() {
        requireContext().stopService(serviceIntent);
        timerBinding.startStopButton.setText("Start");
        timerStarted = false;
    }


    private final BroadcastReceiver updateTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0);
            timerBinding.timeTV.setText(getTimeStringFromDouble(time));
        }
    };

    private String getTimeStringFromDouble(double time) {
        int resultInt = (int) Math.round(time);
        int hours = resultInt % 86400 / 3600;
        int minutes = resultInt % 86400 % 3600 / 60;
        int seconds = resultInt % 86400 % 3600 % 60;

        return makeTimeString(hours, minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    private String makeTimeString(int hours, int minutes, int seconds) {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}