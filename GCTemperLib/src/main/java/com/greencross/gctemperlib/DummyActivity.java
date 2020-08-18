package com.greencross.gctemperlib;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.util.Log;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.webview.TipWebViewActivity;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * Created by mrsohn on 2017. 3. 6..
 */

public class DummyActivity extends BackBaseActivity {
    private static final String TAG = DummyActivity.class.getSimpleName();
    private static final String FRAGMENT_NAME = "fragment_name";
    private static final String FRAGMENT_BUNDLE = "fragment_bundle";

    public static int reqCode = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_activity);
        initFragment(getFragment());
    }

    public BaseFragment getFragment() {
        Intent intent = getIntent();
        BaseFragment fragment = null;
        try {
            String className = intent.getStringExtra(FRAGMENT_NAME);
            Class<?> cl = Class.forName(className);
            Constructor<?> co = cl.getConstructor();
            fragment = (BaseFragment) co.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "getFragment", e);
        }

        return fragment;
    }

    public void initFragment(BaseFragment fragment) {
        replaceFragment(fragment, true, false, getBundle());
    }

    public static void startActivity(Activity activity, Fragment fragment, Bundle bundle) {
        Intent intent = getIntentData(activity, fragment.getClass(), bundle);
        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.fragment_enter, R.anim.fragment_exit);
    }


    public static void startActivity(Activity activity, Class<?> cls, Bundle bundle) {
        Intent intent = getIntentData(activity, cls, bundle);
        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.fragment_enter, R.anim.fragment_exit);
    }

    public static void startActivityForResult(Activity activity, int reqCode, Class<?> cls, Bundle bundle) {
        Intent intent = getIntentData(activity, cls, bundle);
        activity.startActivityForResult(intent, reqCode);
//        activity.overridePendingTransition(R.anim.fragment_enter, R.anim.fragment_exit);
    }


//    public static void startActivity(BaseFragment fragment, Class<?> cls, Bundle bundle) {
//        Intent intent = getIntentData(fragment.getContext(), cls, bundle);
//        fragment.startActivity(intent);
////        fragment.getActivity().overridePendingTransition(R.anim.fragment_enter, R.anim.fragment_exit);
//    }


//    public static void startActivityForResult(BaseFragment fragment, int reqCode, Class<?> cls, Bundle bundle) {
//        Intent intent = getIntentData(fragment.getContext(), cls, bundle);
//        fragment.startActivityForResult(intent, reqCode, null);
////        fragment.getActivity().overridePendingTransition(R.anim.fragment_enter, R.anim.fragment_exit);
//    }

    public static void startActivity(Fragment fragment, Class<? extends Fragment> cls, Bundle bundle) {
        Intent intent = getIntentData(fragment.getContext(), cls, bundle);
        fragment.startActivity(intent);
//        fragment.getActivity().overridePendingTransition(R.anim.fragment_enter, R.anim.fragment_exit);
    }

    public static void startActivity2(Fragment fragment, Class<? extends Fragment> cls, Bundle bundle) {
        Intent intent = getIntentData(fragment.getContext(), cls, bundle);
        fragment.startActivity(intent);
//        fragment.getActivity().overridePendingTransition(R.anim.fragment_pop_up, R.anim.fragment_pop_stay);
    }

    public static void startActivityForResult(Fragment fragment, int reqCode, Class<? extends androidx.fragment.app.Fragment> cls, Bundle bundle) {
        Intent intent = getIntentData(fragment.getContext(), cls, bundle);
        fragment.startActivityForResult(intent, reqCode, null);
//        fragment.getActivity().overridePendingTransition(R.anim.fragment_enter, R.anim.fragment_exit);
    }

    private static Intent getIntentData(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, DummyActivity.class);
        intent.putExtra(FRAGMENT_NAME, cls.getName());
        if (bundle != null)
            intent.putExtra(FRAGMENT_BUNDLE, bundle);
        if (BuildConfig.DEBUG && bundle != null) {
            Log.i(TAG, "getIntentData.intent=" + intent);
            Set<String> keys = bundle.keySet();
            for (String key : keys)
                Log.i(TAG, "getIntentData.bundle.key=" + key + ", " + bundle.get(key));
        }
        return intent;
    }

    private Bundle getBundle() {
        Bundle bundle = getIntent().getBundleExtra(FRAGMENT_BUNDLE);
        return bundle;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
        if (fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) fragment;
            if (baseFragment.getBackPress() != null) {
                baseFragment.onBackPressed();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }


    public void replaceFragment(final androidx.fragment.app.Fragment fragment, final boolean isReplace, boolean isAnim, Bundle bundle) {
        BaseFragment.newInstance(this);
//        if (isReplace)
//            removeAllFragment();

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

//        if (isAnim)
//            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
        if (bundle != null)
            fragment.setArguments(bundle);


        transaction.replace(R.id.content_layout, fragment, fragment.getClass().getSimpleName());
        if (!isFinishing()) {
            if (isReplace == false)
                transaction.addToBackStack(null);

            transaction.commit();
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isReplace == false)
                        transaction.addToBackStack(null);

                    transaction.commitAllowingStateLoss();
                }
            }, 100);
        }
//        printFragmentLog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
        if (fragment instanceof Fragment)
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 액션바 관리팁 버튼 클릭 처리
//    public void actionTipBtnClick(Context context, int week) {
//        String Url = "http://www.higngkids.co.kr/auth/HL_TIP_contents_view.asp?wkey=";
//        Intent intent = intent = new Intent(context, TipWebViewActivity.class);
//        intent.putExtra("Pregmentkey", "1");
//
//        if (week <= 14) {
//            intent.putExtra("PregmentValue", "&C_Index=7417");
//        } else if (week > 14 && week <= 26) {
//            intent.putExtra("PregmentValue", "&C_Index=7418");
//        } else if (week > 26 && week <= 40) {
//            intent.putExtra("PregmentValue", "&C_Index=7419");
//        }
//
//
//        intent.putExtra("Title", getString(R.string.psy_tip));
//        intent.putExtra(CommonData.EXTRA_URL_POSITION, 2);
//
//        startActivity(intent);
//    }


    //    @Override
//    protected void onResume() {
//        super.onResume();
//        BaseFragment.newInstance(this);
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.i(TAG, TAG+".onStop()");
//    }


    @Override
    protected void reLoginComplete() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
        if (fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) fragment;
            baseFragment.reLoginComplete();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        Log.i(TAG, TAG + ".onDestroy()");
    }
//
//
//    @Override
//    public void finish() {
//        super.finish();
////        overridePendingTransition(R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
