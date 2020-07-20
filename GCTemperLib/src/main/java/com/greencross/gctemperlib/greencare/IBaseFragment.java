package com.greencross.gctemperlib.greencare;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mrsohn on 2017. 3. 6..
 */

public interface IBaseFragment {
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    void onViewCreated(View view, @Nullable Bundle savedInstanceState);

    void onBackPressed();
}
