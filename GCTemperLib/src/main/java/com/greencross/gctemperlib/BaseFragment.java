package com.greencross.gctemperlib;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.base.BaseActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.greencare.base.CommonActionBar;
import com.greencross.gctemperlib.greencare.base.IBackPress;
import com.greencross.gctemperlib.greencare.base.IBaseFragment;
import com.greencross.gctemperlib.greencare.base.value.Define;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.network.tr.CConnAsyncTask;
import com.greencross.gctemperlib.greencare.network.tr.HNApiData;
import com.greencross.gctemperlib.greencare.network.tr.HNCConnAsyncTask;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.NetworkUtil;
import com.greencross.gctemperlib.greencare.util.StringUtil;

import java.io.BufferedInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrsWin on 2017-02-16.
 */

public class BaseFragment extends Fragment implements IBaseFragment {
    protected final String TAG = getClass().getSimpleName();

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 33;
    private int mRequestCode = 1111;

    private IPermission mIpermission = null;
    private IBackPress mIBackPress;
    public CommonData commonData;// = CommonData.getInstance();


//    private static BaseActivity mBaseActivity;
//    private CommonActionBar mActionBar;

    public static Fragment newInstance(Context activity) {
        BaseFragment fragment = new BaseFragment();
//        mBaseActivity = activity;
        return fragment;
    }



    public void movePage(Fragment fragment) {
        movePage(fragment, null);
    }

    public void movePage(Fragment fragment, Bundle bundle) {
//        mActionBar = mBaseActivity.getCommonActionBar();
//        mBaseActivity.replaceFragment(fragment, bundle);
    }

    /**
     * 사용할 레이아웃 또는 View 지정
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        commonData = CommonData.getInstance(getContext());
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    /**
     * 뷰가 생성된 후 세팅
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        doAutoLogin();
    }

    protected void setBackPress(IBackPress iBackPress) {
        mIBackPress = iBackPress;
    }

    protected IBackPress getBackPress() {
        return mIBackPress;
    }

    @Override
    public void onBackPressed() {
        Logger.i(TAG, "BaseFragment.onBackPressed().getActivity()=" + getActivity());
//        if (getActivity() instanceof MotherHealthMainActivity) {
//            MotherHealthMainActivity activity = (MotherHealthMainActivity) getActivity();
//            activity.superBackPressed();
            getActivity().onBackPressed();
//        } else
            if (getActivity() instanceof DummyActivity) {
            if (mIBackPress != null) {
                mIBackPress.onBackPressed();
            } else {
                getActivity().onBackPressed();
            }
        } else {
//            mBaseActivity.onBackPressed();
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GCTemperLib gcHeatLib = new GCTemperLib(getContext());
        if (gcHeatLib.isAvailableGCToken() == false) {
            CDialog.showDlg(getContext(), "인증 후 이용 가능합니다.")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            getActivity().finish();
                        }
                    });
//            return null;
        }

//        if (getActivity() instanceof BaseActivity)
//            loadActionbar(((BaseActivity)getActivity()).getCommonActionBar((BaseActivity) getActivity()));

//        doAutoLogin();
    }


//    private ActionBar getActionBar() {
//        return mBaseActivity.getSupportActionBar();
//    }


    public CommonActionBar getCommonActionBar() {
        return null;//mBaseActivity.getCommonActionBar((BaseActivity) getContext());
    }

//    private CommonToolBar mToolBar;
//    protected CommonToolBar getToolBar(View view) {
//        mToolBar = view.findViewById(R.id.medi_common_toolbar);
//        return mToolBar;
//    }

//    protected void setTitle(int title) {
//        if (mToolBar != null)
//            mToolBar.setTitle(title);
//    }
//
//    protected void setTitle(String title) {
//        if (mToolBar != null)
//            mToolBar.setTitle(title);
//    }

    /**
     * Back 이동시 데이터 전달
     */
    private static Bundle mBackDataBundle;

    protected static void setBackData(Bundle bundle) {
        mBackDataBundle = bundle;
    }

    public static Bundle getBackData() {
        Bundle bundle = mBackDataBundle;
        if (mBackDataBundle != null) {
            mBackDataBundle = new Bundle(); // 초기화
            return bundle;
        } else {
            return new Bundle();
        }
    }


//    public MakeProgress getProgress(){
////        /* 테스트 후 주석 해제
//        if(mProgress != null) {
//            return mProgress;
//        }else {
//            return null;
//        }
////        */
//    }

    /**
     * 프로그래스 활성화
     */
    public void showProgress() {
        if (getActivity() instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) getActivity();
            if (activity != null)
                activity.showProgress();
        }
    }

    /**
     * 프로그래스 비활성화
     */

    public void hideProgress() {
        if (getActivity() instanceof BaseActivity) {
//            BaseActivity activity = (BaseActivity)getActivity();
//            activity.hideProgress();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseActivity activity = (BaseActivity) getActivity();
                    if (activity != null)
                        activity.hideProgress();
                }
            }, 1 * 500);
        }
    }

    /*public void showProgress() {
        if (mBaseActivity != null)
            if (getActivity() instanceof BaseActivity){
                ((BaseActivity)getActivity()).showProgress();
            }
    }

    public void hideProgress() {
        if (getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).hideProgress();
        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mBaseActivity.hideProgress();
//            }
//        }, 1 * 500);
    }

    public void hideProgressForce() {
        //mBaseActivity.hideProgressForce();
    }*/

    /**
     * @param type           // 액션바 타입
     * @param title          // 타이틀
     * @param clickListener1 // 맨 오른쪽 버튼 처리
     * @param clickListener2 // 맨 오른쪽 옆 버튼 처리
     */
    public void setActionBar(Define.ACTION_BAR type, String title, View.OnClickListener clickListener1, View.OnClickListener clickListener2) {
        getCommonActionBar().setActionBar(type, title, clickListener1, clickListener2);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent, null);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode, null);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    public void startActivityForResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
//        if (getActivity() instanceof BaseActivity) {
//            actionBar.setActionBackBtnVisible(View.VISIBLE);
//            actionBar.goneActionBarFunctionBtn();
////            actionBar.showActionBar(true);            // 액션바 띄울지 여부
//
//            actionBar.setActionBarTitle("");
//            Bundle bundle = getArguments();
//            if (bundle != null) {
//                String title = getArguments().getString(CommonActionBar.ACTION_BAR_TITLE);
//                if (TextUtils.isEmpty(title) == false)
//                    actionBar.setActionBarTitle(title);
//
//                Logger.i(TAG, "loadActionbar.title=" + title);
//            }
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i(TAG, TAG + ".onActivityResult");
    }

    protected void getData(Class<? extends BaseData> cls, final Object obj, final IGCResult iGCResult) {
        if (NetworkUtil.getConnectivityStatus(getContext()) == false) {
            CDialog.showDlg(getContext(), "네트워크 연결 상태를 확인해주세요.");
            return;
        }
        showProgress();
        HNCConnAsyncTask.CConnectorListener queryListener = new HNCConnAsyncTask.CConnectorListener() {
            @Override
            public Object run() throws Exception {
                HNApiData data = new HNApiData();
                try {
                    Object recv = data.getData(getContext(), cls, obj);
                    return recv;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void view(HNCConnAsyncTask.CQueryResult result) {
//                Log.i(TAG, "result1="+result);
                hideProgress();
                if (result.result == CConnAsyncTask.CQueryResult.SUCCESS && result.data != null) {
//                    Log.e(TAG, "데이터 수신 성공::"+result);
                    if (iGCResult != null) {
                        iGCResult.onResult(true, "데이터 수신 성공", result.data);
                    }
                } else {
//                    Log.e(TAG, "데이터 수신 실패");
                    if (iGCResult != null) {
                        iGCResult.onResult(false, "데이터 수신 실패", null);
                    }
                }
            }
        };

        HNCConnAsyncTask asyncTask = new HNCConnAsyncTask();
        asyncTask.execute(queryListener);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public BaseFragment getFragment(Class<?> cls) {
        BaseFragment fragment = null;
        try {
            Constructor<?> co = cls.getConstructor();
            fragment = (BaseFragment) co.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "getFragment", e);
        }
        return fragment;
    }

    public void reqPermissions(String[] perms, IPermission iPermission) {
        mIpermission = iPermission;
        final String[] permissions = getGrandtedPermissions(perms);
        if (permissions.length > 0) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            CDialog.showDlg(getContext(), "권한 설정 후 이용 가능합니다.", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(getActivity()
                            , permissions
                            , REQUEST_PERMISSIONS_REQUEST_CODE);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {
            if (iPermission != null) {
                iPermission.result(true);
                mIpermission = null;
            }

        }
    }

    /**
     * 설정이 되지 않은 권한들 가져옴
     *
     * @return
     */
    private String[] getGrandtedPermissions(String... permissions) {
        List<String> list = new ArrayList<>();
        for (String perm : permissions) {
            int isGrandted = ActivityCompat.checkSelfPermission(getContext(), perm);

            if (isGrandted != PackageManager.PERMISSION_GRANTED)
                list.add(perm);
        }

        String[] permissionArr = new String[list.size()];
        permissionArr = list.toArray(permissionArr);
        return permissionArr;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                if (mIpermission != null) {
                    mIpermission.result(true);
                    mIpermission = null;
                }
            } else if (isPermissionGransteds(grantResults)) {
                if (mIpermission != null) {
                    mIpermission.result(true);
                    mIpermission = null;
                }
            } else {
                if (mIpermission != null) {
                    mIpermission.result(false);
                    mIpermission = null;
                } else {
                    CDialog.showDlg(getContext(), "권한 설정 후 이용 가능합니다.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reqPermissions(permissions, mIpermission);
                        }
                    }, null);
                }
            }
        }
    }

    /**
     * 권한이 정상적으로 설정 되었는지 확인
     *
     * @param grantResults
     * @return
     */
    private boolean isPermissionGransteds(int[] grantResults) {
        for (int isGranted : grantResults) {
            return isGranted == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public interface IPermission {
        void result(boolean isGranted);
    }

    // 화면 회전시 초기화 방지
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
