package com.greencross.gctemperlib.greencare.pressure;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.base.BaseFragment;

public class PressureInputMainFragment extends BaseFragment {
    private int mInputNum = -1;    // 인풋 타입


    private int mFragmentNum;
    private Intent mIntent;

    private RadioGroup mMenuRadioGroup;
    private View mActionbar;
    private ImageView mActionbarBtn;

    private Fragment[] fragments = new Fragment[2];

    public static BaseFragment newInstance() {
        PressureInputMainFragment fragment = new PressureInputMainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_input_main, container, false);
        return view;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragments[0] =  PressureInputFragment.newInstance();
        fragments[1] =  PressureMedInputFragment.newInstance();

        RadioButton btn_info = (RadioButton) view.findViewById(R.id.btn_info);
        RadioButton btn_medical = (RadioButton) view.findViewById(R.id.btn_medical);

        btn_info.setText(getString(R.string.text_pressure));
        btn_medical.setText(getString(R.string.text_medititle));

        mMenuRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup2);


        mMenuRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.indexOfChild(view.findViewById(checkedId));

                setChildFragment(fragments[id]);
            }
        });


//        selectTab(0);
        setChildFragment(fragments[0]);

    }


    private void setChildFragment(Fragment child) {
        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();

        if (!child.isAdded()) {
            childFt.replace(R.id.input_layout, child);
            childFt.addToBackStack(null);
            childFt.commit();
        }
    }

}
