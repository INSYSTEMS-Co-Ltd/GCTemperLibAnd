package com.greencross.gctemperlib.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.MakeProgress;


/**
 * Created by jihoon on 2016-03-21.
 * fragment 부모 클래스
 *
 * @since 0, 1
 */
public class BaseFragment extends Fragment {

    private MakeProgress mProgress = null;

    public LayoutInflater mLayoutInflater;

    public CommonData commonData;// = CommonData.getInstance();
    //    public CommonView commonView = CommonView.getInstance();
    public CustomAlertDialog mDialog;

    public BaseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        commonData = CommonData.getInstance(getContext());

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mProgress = new MakeProgress(getActivity());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * 프로그래스 활성화
     */
    public void showProgress() {

        if (mProgress == null)
            mProgress = new MakeProgress(getActivity());

        mProgress.show();
    }

    /**
     * 프로그래스 비활성화
     */
    public void hideProgress() {

        if (mProgress != null && mProgress.isShowing())
            mProgress.dismiss();
    }

}
