package com.greencross.gctemperlib.greencare.bluetooth.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.greencare.base.CommonActionBar;

/**
 * Created by jongrakmoon on 2017. 3. 2..
 */

public class BluetoothBaseFragment extends BaseFragment {
    protected static final String TAG = "####";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
        super.loadActionbar(actionBar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected void openBluetoothSetting() {
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);
    }
}
