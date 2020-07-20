package com.greencross.gctemperlib.greencare.component;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.base.BaseFragment;
import com.greencross.gctemperlib.greencare.base.CommonActionBar;
import com.greencross.gctemperlib.greencare.base.DummyActivity;
import com.greencross.gctemperlib.greencare.util.Logger;

/**
 * Created by MrsWin on 2017-03-01.
 */

public class FullImageFragment extends BaseFragment implements View.OnClickListener{
    private final String TAG = FullImageFragment.class.getSimpleName();
    public static String SAMPLE_BACK_DATA = "SAMPLE_BACK_DATA";
    public static String DRAWABLE_IMG = "drawable_img";
    private String title;
    private int drawImg, type;
    private ImageButton mBtnSearch, mBtnMedicare, mBtnWeight;
    private LinearLayout mBottomLay;


    /**
     * 액션바 세팅
     */
    @Override
    public void loadActionbar(CommonActionBar actionBar) {
    }


    public static Fragment newInstance() {
        FullImageFragment fragment = new FullImageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_layout, container, false);
        return view;
    }

    private ImageView mIv;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            drawImg = getArguments().getInt(DRAWABLE_IMG);
            title = getArguments().getString("Title");
            type = getArguments().getInt("Type");
        }

        mBottomLay = (LinearLayout) view.findViewById(R.id.bottom_lay);
        mBtnMedicare = (ImageButton) view.findViewById(R.id.btn_medicare);
        mBtnSearch = (ImageButton) view.findViewById(R.id.btn_search);
        mBtnWeight = (ImageButton) view.findViewById(R.id.btn_weight);

        mIv = (ImageView) view.findViewById(R.id.full_content_imageview);
        Logger.i(TAG, "drawImg" + drawImg);
//        new IvLoadAsyctask().execute(drawImg);
        mIv.setImageResource(drawImg);

        mBtnMedicare.setOnClickListener(this);
        mBtnSearch.setOnClickListener(this);
        mBtnWeight.setOnClickListener(this);

        setActionBar();
        setBottomLayout();
    }

    private void setActionBar() {
        // CommonActionBar actionBar 는 안 씀 한화꺼
        if (getActivity() instanceof DummyActivity) {
            DummyActivity activity = (DummyActivity) getActivity();

            TextView titleTv = (TextView) activity.findViewById(R.id.common_title_tv);
            ImageView leftbtn = (ImageView) activity.findViewById(R.id.common_left_btn);
            leftbtn.setImageResource(R.drawable.btn_back_gray);
            titleTv.setText(title);
            titleTv.setTextColor(ContextCompat.getColor(getContext(),R.color.txt_dark_bold));

            RelativeLayout titleLayout = (RelativeLayout) activity.findViewById(R.id.common_bg_layout);
            titleLayout.setBackgroundResource(R.color.bg_gray);


        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_search) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_higngkids))));
        } else if (id == R.id.btn_medicare) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_medicare))));
        } else if (id == R.id.btn_weight) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_weight))));
        }
    }



    public void setBottomLayout() {
        if (type == 1) {
            mBottomLay.setVisibility(View.VISIBLE);
            mBtnSearch.setVisibility(View.GONE);
            mBtnMedicare.setVisibility(View.VISIBLE);
            mBtnWeight.setVisibility(View.GONE);
        } else if (type == 4) {
            mBottomLay.setVisibility(View.VISIBLE);
            mBtnSearch.setVisibility(View.VISIBLE);
            mBtnMedicare.setVisibility(View.GONE);
            mBtnWeight.setVisibility(View.GONE);
        } else if (type == 6){
            mBottomLay.setVisibility(View.VISIBLE);
            mBtnSearch.setVisibility(View.GONE);
            mBtnMedicare.setVisibility(View.GONE);
            mBtnWeight.setVisibility(View.VISIBLE);
        }
        else
            mBottomLay.setVisibility(View.GONE);
    }

    class IvLoadAsyctask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
            showProgress();
        }

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            if (integers.length > 0) {
                BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(integers[0]);
                Bitmap bitmap = drawable.getBitmap();
                return bitmap;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null)
                mIv.setImageBitmap(bitmap);

            //hideProgressForce();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 이전 플래그먼트에서 데이터 받기
        Bundle bundle = BaseFragment.getBackData();
        String backString = bundle.getString(SAMPLE_BACK_DATA);
        Logger.i("", "backString=" + backString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
