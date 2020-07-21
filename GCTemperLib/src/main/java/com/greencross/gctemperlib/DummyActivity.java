package com.greencross.gctemperlib;

import android.app.Activity;
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
            Log.i(TAG, "getIntentData.intent="+intent);
            Set<String> keys = bundle.keySet();
            for (String key : keys)
                Log.i(TAG, "getIntentData.bundle.key="+key+", "+bundle.get(key));
        }
        return intent;
    }

    private Bundle getBundle() {
        Bundle bundle = getIntent().getBundleExtra(FRAGMENT_BUNDLE);
        return bundle;
    }

    @Override
    public void onBackPressed() {
        androidx.fragment.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
        if (fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment)fragment;
            if (baseFragment.getBackPress() != null) {
                baseFragment.onBackPressed();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        BaseFragment.newInstance(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, TAG+".onStop()");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, TAG+".onDestroy()");
    }


    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
    }


//    public static void moveToCommentPage(Fragment fragment, CommunityListViewData data, boolean isKeyPad, String PRE_PAGE, String CM_GUBUN){
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(CommunityCommentFragment.KEYPAD_STATUS, isKeyPad);
//        bundle.putSerializable(CommunityCommentFragment.POSTS_INFO,data);
//        bundle.putString(CommunityCommentFragment.PRE_PAGE, PRE_PAGE);
//        bundle.putString(CommunityCommentFragment.KEY_CMGUBUN, CM_GUBUN);
//        startActivityForResult(fragment, CommunityCommentFragment.REQUEST_CODE_COMMENTINFO,CommunityCommentFragment.class,bundle);
//    }

//    public static void moveToProfilePage(String PRE_PAGE, CommunityUserData userData, Activity activity){
//
//        if(userData.MBER_SN==null|| userData.MBER_SN.equals(""))
//            return;
//
//        Bundle bundle = new Bundle();
//        bundle.putString(CommunityProfileFragment.KEY_PRE_PAGE, PRE_PAGE);
//        bundle.putSerializable(CommunityProfileFragment.KEY_PROFILE_INFO,userData);
//        DummyActivity.startActivity(activity, CommunityProfileFragment.class, bundle); //닉네임 변경에 따라 추가 데이터 변경필요할지도..
//    }

    public static void moveToTagPage(Activity activity, String title, String pre_page){
//        Bundle bundle = new Bundle();
//        bundle.putString(CommunityTagFragment.TAG_TITLE, title);
//        bundle.putString(CommunityTagFragment.PRE_PAGE, pre_page);
//        DummyActivity.startActivity(activity, CommunityTagFragment.class, bundle);
    }

    public static void moveToImagePage(Fragment fragement, String CM_IMG1, String NICK){
//        Bundle bundle = new Bundle();
//        bundle.putString(CommunityImageFragment.KEY_IMAGE_PATH,CM_IMG1);
//        bundle.putString(CommunityImageFragment.KEY_NICKNAME,NICK);
//        DummyActivity.startActivity2(fragement, CommunityImageFragment.class,bundle);
    }


    /**
     *
     * @param fragment
     *
     * 선택입력사항
     * @param data data.CM_COMMENT 글내용 전달 시 포함 / 선택
     * @param data data.CM_MEAL 글쓰기 시 칼로리 식단 등을 표현 할 경우 사용
     * @param ImagePath 이미지의 절대 경로 / 로컬이미지 사용 시 지정. 없을 경우 빈값
     * 필수입력사항
     * @param data data.CM_SEQ 값 에 따라 글쓰기/글수정이 결정됨 / 신규 글쓰기 시 0, 수정일 경우 1 이상의 값 입력
     * @param data data.ISSHARE 공유하기로 글 작성 시 반드시 true
     *
     */

//    public static void moveToWritePage(final Fragment fragment, final CommunityListViewData data, final String ImagePath){
//
//        if(TextUtils.isEmpty(CommonData.getInstance(fragment.getContext()).getMberNick())){
//
//            CommonFunction.openDialogRegisterNick(fragment.getActivity(), new DialogCommon.UpdateProfile() {
//                @Override
//                public void updateProfile(String NICK, String DISEASE_OPEN,String DISEASE_NM) {
//                    if(!NICK.equals("")&&!DISEASE_OPEN.equals("")) {
//                        final DialogCommon dialogIntro = DialogCommon.showDialogIntro(fragment.getContext(), NICK);
//                        dialogIntro.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialog) {
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable(CommunityWriteFragment.KEY_CONTENTS,data);
//                                bundle.putString(CommunityWriteFragment.KEY_IMAGEPATH,ImagePath);
//                                DummyActivity.startActivityForResult(fragment, CommunityWriteFragment.REQUEST_CODE_WRITEINFO,CommunityWriteFragment.class,bundle);
//                            }
//                        });
//                    }else{
//                        moveToWritePage(fragment,data,ImagePath);
//                    }
//                }
//            });
//        }else{
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(CommunityWriteFragment.KEY_CONTENTS,data);
//            bundle.putString(CommunityWriteFragment.KEY_IMAGEPATH,ImagePath);
//            DummyActivity.startActivityForResult(fragment, CommunityWriteFragment.REQUEST_CODE_WRITEINFO,CommunityWriteFragment.class,bundle);
//        }
//
//
//
//    }


    public void replaceFragment(final androidx.fragment.app.Fragment fragment, final boolean isReplace, boolean isAnim, Bundle bundle) {

        BaseFragment.newInstance(this);
//        if (isReplace)
//            removeAllFragment();

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

//        if (isAnim)
//            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
        if (bundle != null)
            fragment.setArguments(bundle);


        setContentView(R.layout.mother_health_sub_main_activity);
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
        androidx.fragment.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
        if (fragment instanceof androidx.fragment.app.Fragment)
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 액션바 관리팁 버튼 클릭 처리
    public void actionTipBtnClick(Context context, int week) {
        String Url = "http://www.higngkids.co.kr/auth/HL_TIP_contents_view.asp?wkey=";
        Intent intent = intent = new Intent(context, TipWebViewActivity.class);
        intent.putExtra("Pregmentkey","1");

        if(week <= 14){
            intent.putExtra("PregmentValue","&C_Index=7417");
        } else if(week > 14 && week <= 26){
            intent.putExtra("PregmentValue","&C_Index=7418");
        } else if(week > 26 && week <= 40){
            intent.putExtra("PregmentValue","&C_Index=7419");
        }



        intent.putExtra("Title", getString(R.string.psy_tip));
        intent.putExtra(CommonData.EXTRA_URL_POSITION , 2);

        startActivity(intent);
    }

}
