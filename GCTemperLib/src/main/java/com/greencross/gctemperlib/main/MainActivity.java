package com.greencross.gctemperlib.main;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.greencross.gctemperlib.greencare.component.CPeriodDialog;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.greencare.database.DBHelper;
import com.greencross.gctemperlib.greencare.database.DBHelperLog;
import com.greencross.gctemperlib.Alram.AlramMainActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.ChildItem;
import com.greencross.gctemperlib.collection.EpidemicItem;
import com.greencross.gctemperlib.common.BackPressCloseHandler;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.CustomImageLoader;
import com.greencross.gctemperlib.common.CustomViewPager;
import com.greencross.gctemperlib.common.MakeProgress;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.fever.TemperMainActivity;
import com.greencross.gctemperlib.TemperActivity;
import com.greencross.gctemperlib.fever.RequestDiseaseProgramActivity;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.JsonLogPrint;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.greencare.weight.WeightBigDataInputFragment;
import com.greencross.gctemperlib.network.RequestApi;
import com.greencross.gctemperlib.push.FirebaseMessagingService;
import com.greencross.gctemperlib.setting.SettingActivity;
import com.greencross.gctemperlib.setting.SettingAddressActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.GpsInfo;
import com.greencross.gctemperlib.util.KakaoLinkUtil;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.webview.BackWebViewInfoActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


/**
 * Created by jihoon on 2016-03-21.
 * 메인 클래스 ( 로그인 후 최상위 클래스 )
 * @since 0, 1
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private final String TAG = MainActivity.class.getSimpleName();
    public static Activity MAIN_ACTIVITY;
    public LayoutInflater mLayoutInflater;

    private long period = 1;
    private long count = 1;
    private int num = 1;
    private String Fetus_BubbleId = "";
    private int resStringId = 0, diffday=0;
    private String url ="";
    private String[] dzname = new String[2];
    private String[] weekago_1_per = new String[2];

    public static ArrayList<ChildItem> mChildMenuItem;  // 자녀 데이터 ( 항상 동기화 )
    public static int mChildChoiceIndex = 0;    // 자녀 선택 index
    int chl_sn = 0;

    public static boolean mChangeChild = false;  // 임시 자녀변경 flag
    public static boolean mJunChangeChild = false;  // 정회원 전환 시 자녀변경 flag

    //hsh
    Intent inTentGo = null;
    private String sMainPopTitle ="";
    private String sMainPopTxt ="";
    private boolean bMainPop = false;
    boolean exsit_aft_baby;
    int typePush=0;

    public static ArrayList<EpidemicItem> mEpidemicList;            // 유행질병 카운트

    public static String mLastWeight = "";
    public static String mLastHeight = "";
    public static String mLastCmResult = "";
    public static String mLastKgResult = "";
    public static String mLastWeightDe = "";
    public static String mLastHeightDe = "";

    private MakeProgress mProgress			=	null;
    private BackPressCloseHandler backPressCloseHandler;
    private Calendar currentCal = Calendar.getInstance();

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationLeftView;
    private WebView webView;
    CustomAlertDialog mDialog;

    // 네비바
    private ImageButton mMenuBtn;
    private TextView mTitleTv,mHompageBtn, mFInFoBtn;
    private LinearLayout side_child_lv, side_fetus_lv;

    private FrameLayout mBackLayout;

    private ImageView mImgBabyPhoto, btn_picture,img_baby_line, mImgNew, mImgfetusphoto, mAlramBtn, mAlramNew;
    private ImageButton mBtnLeftBaby, mBtnRightBaby;
    private TextView mTxtSpeech_Bubble, text_period;
    private TextView txt_baby_birth, txt_baby_name, txt_baby_age, txt_baby_born_to_day;
    private LinearLayout baby_info_lv;

    // 왼쪽 메뉴 아이템
    private TextView  mNewMessage;//mBtnSide_07
    private RelativeLayout mBtnSide_04, mBtnSide_05, mBtnSide_06,mBtnSide_03, mBtnSide_10;
    private ImageView mBtnSide_08;
    private LinearLayout mBtnSide_01, mBtnSide_02, mBtnSide_98, mBtnSide_99,btn_side_100,btn_side_101, btn_side_200, btn_side_201,btn_side_202,btn_side_203,btn_side_204,btn_side_205;

    //하단 메뉴
    private LinearLayout mBabyMenu, mFetusMenu, name_lv;

    private View view;

    //swiper
//    private ViewPagerAdapter viewPager; //뷰페이지
    private ViewPagerAdapter ViewPagerAdapter; //뷰페이지 어댑터

    //Spinner
    private Spinner main_spinner;

    //Category
    private LinearLayout mlist_add_change;
    private RelativeLayout sub_main_category1, sub_main_category2, sub_main_category3, main_cata1_rl;
    private TextView main_cata1_title, main_cata1_view, main_cata1_content, main_cata1_content1, main_cata1_content2;
    private TextView main_cata2_title, main_cata2_rank1, main_cata2_rank_ratio1, main_cata2_view, main_cata2_rank2, main_cata2_rank_ratio2;
    private TextView main_cata3_title, main_cata3_fairytail, main_cata3_dance_txt;
    private LinearLayout main_cata3_dance;

    private boolean is_temp_setting = false;
    private boolean is_temp_menu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mProgress = new MakeProgress(this);

        backPressCloseHandler = new BackPressCloseHandler(this);
        MAIN_ACTIVITY  = MainActivity.this;
        mLayoutInflater	=	(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        NotificationManager mNM	= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNM.cancelAll();

        init();
        setEvent();

        GregorianCalendar mCalendar = new GregorianCalendar();
        mCalendar.setTime(new Date());
        mCalendar.add(Calendar.DAY_OF_MONTH, -7);
        requestEpidemicData(mCalendar.getTime(), new Date());

        GLog.i("기본 팝업"+CommonData.getInstance(MainActivity.this).getAfterBabyPopupShowCheck(), "dd");



        // 다시 안보기 체크 안하고,
        if(!CommonData.getInstance(MainActivity.this).getAfterBabyPopupShowCheck()){
            exsit_aft_baby = false;
            for (int i = 0; i < mChildMenuItem.size(); i++){
                try {
                    if(mChildMenuItem.get(i).getmChlExistYn().equals(CommonData.NO)){

                        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
                        Date today = new Date();

                        Date AftDate = format.parse(mChildMenuItem.get(i).getmChldrnAftLifyea());

                        if(today.compareTo(AftDate) > 0){
                            exsit_aft_baby = true;
                            break;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            GLog.i("기본 팝업2"+exsit_aft_baby, "dd");

            if(exsit_aft_baby && !CommonData.getInstance(MainActivity.this).getAfterBabyPopupShowCheck()) {
                if (CommonData.getInstance(MainActivity.this).getMberGrad().equals("10")) {
                    CustomAlertDialog mDialog1 = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_C);
                    mDialog1.setTitle(getString(R.string.app_name_kr));
                    mDialog1.setContent(getString(R.string.popup_msg_aft_lifyea));
                    mDialog1.setNegativeButton(getString(R.string.popup_dialog_button_cancel), (dialog, button) -> {
                        dialog.dismiss();
                        exsit_aft_baby = false;
                        showTempPop();
                    });
                    mDialog1.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                        String tel = "tel:" + getString(R.string.call_center_number_2);
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(tel));
                        startActivity(intent);
                        dialog.dismiss();
                        exsit_aft_baby = false;
                        showTempPop();
                    });
                    mDialog1.setCheckboxButton((dialog, button) -> {
                        dialog.setChangeCheckboxImg();
                        CommonData.getInstance(MainActivity.this).setAfterBabyPopupShowCheck(dialog.isChecked());
                        dialog.dismiss();
                        exsit_aft_baby = false;
                        showTempPop();
                    });
                    mDialog1.setCancelable(false);
                    mDialog1.show();
                } else{
                    CustomAlertDialog mDialog1 = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog1.setTitle(getString(R.string.app_name_kr));
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_dialog_baby_passover_jun, null);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    mDialog1.setContentView(view, params);
                    mDialog1.setPositiveButton(getString(R.string.popup_dialog_button_moveto), (dialog, button) -> {
                        dialog.dismiss();
                        Intent intent;
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        Util.BackAnimationStart(MainActivity.this);
                    });

                    mDialog1.show();
                }
            }
        }

        if(CommonData.getInstance(MainActivity.this).getMberGrad().equals("20")) {

            if (CommonData.getInstance(MainActivity.this).getMarketing_yn().equals("Y") && CommonData.getInstance(MainActivity.this).getMarketing_yn_de().equals("")) {
                CustomAlertDialog mDialog2 = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_F);
                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_dialog_certiview, null);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params1.gravity = Gravity.CENTER;
                mDialog2.setContentView(view1, params1);
                mDialog2.setTitle("마케팅 정보 앱 PUSH 수신 안내");
                mDialog2.setContent("마케팅 정보에 앱 PUSH가 추가되었습니다.\n다양한 정보와 이벤트 소식을 받아보세요.");
                mDialog2.setNegativeButtonVisible(false);
                mDialog2.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog1, button) -> {
                    requestAgreeAlarmSetting(dialog1.isMotherChecked()?CommonData.YES:CommonData.NO);
                    dialog1.dismiss();
                });

                mDialog2.setCheckboxButtonLv((dialog1, button) -> {
                    dialog1.setChangeCheckboxImg_motherBig();

                });

                mDialog2.setCancelable(false);
                mDialog2.show();
            } else {
                main_intro_popup();
            }
        } else {
            main_intro_popup();
        }


        //hsh
        requestTempPopApi();
    }

    /**
     * 초기화
     */
    public void init(){
        resStringId = getResources().getIdentifier(Fetus_BubbleId, "string", MAIN_ACTIVITY.getPackageName());

        mChildMenuItem = new ArrayList<ChildItem>();
        mEpidemicList = new ArrayList<EpidemicItem>();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationLeftView = (NavigationView) findViewById(R.id.nav_left_view);

        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_main);
        // start custom actionbar leftmargin remove
        View customView = getSupportActionBar().getCustomView();
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        mMenuBtn    =   (ImageButton)   getSupportActionBar().getCustomView().findViewById(R.id.menu_btn);
        mAlramBtn = (ImageButton) getSupportActionBar().getCustomView().findViewById(R.id.alram_btn);
        mAlramNew = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.alram_new);

        mImgNew     =   (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.img_new);

        mTitleTv    =   (TextView)      getSupportActionBar().getCustomView().findViewById(R.id.title_tv);

        mBabyMenu = (LinearLayout)findViewById(R.id.child);
        mFetusMenu = (LinearLayout)findViewById(R.id.fetus);

        main_spinner = (Spinner)findViewById(R.id.main_spinner);
        String[] main_catagory_array = getResources().getStringArray(R.array.main_category);

        ArrayAdapter<String> main_catagory_adapter = new ArrayAdapter<String>(
                this, R.layout.main_spinner_item, main_catagory_array );

        main_spinner.setAdapter(main_catagory_adapter);

        mlist_add_change = (LinearLayout) findViewById(R.id.list_add_change);

        //swiper
//        viewPager = (ViewPagerAdapter) findViewById(R.id.main_viewpager);

        mBtnLeftBaby = (ImageButton)findViewById(R.id.btn_left_baby);
        mBtnRightBaby = (ImageButton)findViewById(R.id.btn_right_baby);


        mBtnSide_01 = (LinearLayout) findViewById(R.id.btn_side_01);
        mBtnSide_02 = (LinearLayout)findViewById(R.id.btn_side_02);
        mBtnSide_03 = (RelativeLayout)findViewById(R.id.btn_side_03);
        mBtnSide_04 = (RelativeLayout)findViewById(R.id.btn_side_04);
        mBtnSide_05 = (RelativeLayout)findViewById(R.id.btn_side_05);
        mBtnSide_06 = (RelativeLayout)findViewById(R.id.btn_side_06);
        mBtnSide_08 = (ImageView)findViewById(R.id.btn_side_08);
        mBtnSide_10 = (RelativeLayout)findViewById(R.id.btn_side_10);

        btn_side_200 = (LinearLayout) findViewById(R.id.btn_side_200);
        btn_side_201 = (LinearLayout) findViewById(R.id.btn_side_201);
        btn_side_202 = (LinearLayout) findViewById(R.id.btn_side_202);
        btn_side_203 = (LinearLayout) findViewById(R.id.btn_side_203);
        btn_side_204 = (LinearLayout) findViewById(R.id.btn_side_204);
        btn_side_205 = (LinearLayout) findViewById(R.id.btn_side_205);

        /* 아이심리 테스트 버튼*/
        mBtnSide_99 = (LinearLayout)findViewById(R.id.btn_side_99);

        /* 엄마건강 버튼 */
        mBtnSide_98 = (LinearLayout)findViewById(R.id.btn_side_98);

        btn_side_101 = (LinearLayout)findViewById(R.id.btn_side_101);

        //아이학습
        btn_side_100 = (LinearLayout)findViewById(R.id.btn_side_100);

        //New 메시지 말풍선
        mNewMessage = (TextView) findViewById(R.id.new_message);

        mHompageBtn = (TextView) findViewById(R.id.hompage_btn);

        /**
         * 임신 여부에 따른 햄버거 메뉴
         */
        side_child_lv = (LinearLayout) findViewById(R.id.side_child_lv);
        side_fetus_lv = (LinearLayout) findViewById(R.id.side_fetus_lv);

        /**
         * 카테고리 메뉴
         */
        sub_main_category1 = (RelativeLayout) findViewById(R.id.sub_main_category1);
        sub_main_category2 = (RelativeLayout) findViewById(R.id.sub_main_category2);
        sub_main_category3 = (RelativeLayout) findViewById(R.id.sub_main_category3);

        main_cata1_title = (TextView) findViewById(R.id.main_cata1_title);
        main_cata1_view = (TextView) findViewById(R.id.main_cata1_view);
        main_cata1_rl = (RelativeLayout) findViewById(R.id.main_cata1_rl);
        main_cata1_content = (TextView) findViewById(R.id.main_cata1_content);
        main_cata1_content1 = (TextView) findViewById(R.id.main_cata1_content1);
//        main_cata1_content2 = (TextView) findViewById(R.id.main_cata1_content2);

        main_cata2_title = (TextView) findViewById(R.id.main_cata2_title);
        main_cata2_rank1 = (TextView) findViewById(R.id.main_cata2_rank1);
        main_cata2_rank_ratio1 = (TextView) findViewById(R.id.main_cata2_rank_ratio1);
        main_cata2_view = (TextView) findViewById(R.id.main_cata2_view);
        main_cata2_rank2 = (TextView) findViewById(R.id.main_cata2_rank2);
        main_cata2_rank_ratio2 = (TextView) findViewById(R.id.main_cata2_rank_ratio2);

        main_cata3_title = (TextView) findViewById(R.id.main_cata3_title);
        main_cata3_dance = (LinearLayout) findViewById(R.id.main_cata3_dance);
        main_cata3_fairytail = (TextView) findViewById(R.id.main_cata3_fairytail);
        main_cata3_dance_txt = findViewById(R.id.main_cata3_dance_txt);

        //click 저장
        view = (DrawerLayout) findViewById(R.id.drawer_layout);

        sub_main_category1.setVisibility(View.GONE);
        sub_main_category2.setVisibility(View.GONE);
        sub_main_category3.setVisibility(View.GONE);

        saveChild();

    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){

        findViewById(R.id.btn_weight_main).setOnClickListener(btnListener);
        findViewById(R.id.btn_play_main).setOnClickListener(btnListener);
        findViewById(R.id.mBtnMealMain).setOnClickListener(btnListener);
        findViewById(R.id.mBtnPressureMain).setOnClickListener(btnListener);
        findViewById(R.id.mBtnSugarMain).setOnClickListener(btnListener);
        findViewById(R.id.btn_fetus_comunity_main).setOnClickListener(btnListener);


        findViewById(R.id.btn_growth_main).setOnClickListener(btnListener);
        findViewById(R.id.btn_fever_main).setOnClickListener(btnListener);
        findViewById(R.id.mBtnPsyMain).setOnClickListener(btnListener);
        findViewById(R.id.btn_study_main).setOnClickListener(btnListener);
        findViewById(R.id.mBtnMomHealthMain).setOnClickListener(btnListener);
        findViewById(R.id.btn_child_comunity_main).setOnClickListener(btnListener);

        findViewById(R.id.btn_medycare).setOnClickListener(btnListener);
        findViewById(R.id.btn_HNews).setOnClickListener(btnListener);
        findViewById(R.id.mBtnCallMain).setOnClickListener(btnListener);

        mMenuBtn.setOnClickListener(btnListener);
        mAlramBtn.setOnClickListener(btnListener);

        mlist_add_change.setOnClickListener(btnListener);


        mBtnSide_01.setOnClickListener(btnListener);
        mBtnSide_02.setOnClickListener(btnListener);
        mBtnSide_03.setOnClickListener(btnListener);
        mBtnSide_04.setOnClickListener(btnListener);
        mBtnSide_05.setOnClickListener(btnListener);
        mBtnSide_06.setOnClickListener(btnListener);
        mBtnSide_08.setOnClickListener(btnListener);
        mBtnSide_10.setOnClickListener(btnListener);

        btn_side_200.setOnClickListener(btnListener);
        btn_side_201.setOnClickListener(btnListener);
        btn_side_202.setOnClickListener(btnListener);
        btn_side_203.setOnClickListener(btnListener);
        btn_side_204.setOnClickListener(btnListener);
        btn_side_205.setOnClickListener(btnListener);

        mHompageBtn.setOnClickListener(btnListener);


        mBtnSide_99.setOnClickListener(btnListener);
        mBtnSide_98.setOnClickListener(btnListener);
        btn_side_101.setOnClickListener(btnListener);
        btn_side_100.setOnClickListener(btnListener);

        navigationLeftView.setNavigationItemSelectedListener(this);

        //NEW말풍선
        mNewMessage.setOnClickListener(btnListener);

        mDrawerLayout.setDrawerListener(toggle);

        //카테고리
        main_cata1_rl.setOnClickListener(btnListener);
        main_cata2_view.setOnClickListener(btnListener);
        main_cata3_dance.setOnClickListener(btnListener);
        main_cata3_fairytail.setOnClickListener(btnListener);



        //click 저장
        OnClickListener mClickListener = new OnClickListener(btnListener,view, MainActivity.this);

        //홈
        findViewById(R.id.btn_growth_main).setOnTouchListener(mClickListener);
        findViewById(R.id.btn_fever_main).setOnTouchListener(mClickListener);
        findViewById(R.id.mBtnPsyMain).setOnTouchListener(mClickListener);
        findViewById(R.id.mBtnMomHealthMain).setOnTouchListener(mClickListener);
        findViewById(R.id.btn_study_main).setOnTouchListener(mClickListener);
        findViewById(R.id.btn_child_comunity_main).setOnTouchListener(mClickListener);

        findViewById(R.id.btn_medycare).setOnTouchListener(mClickListener);
        findViewById(R.id.btn_HNews).setOnTouchListener(mClickListener);
        findViewById(R.id.mBtnCallMain).setOnTouchListener(mClickListener);

        findViewById(R.id.btn_weight_main).setOnTouchListener(mClickListener);
        findViewById(R.id.btn_play_main).setOnTouchListener(mClickListener);
        findViewById(R.id.mBtnMealMain).setOnTouchListener(mClickListener);
        findViewById(R.id.mBtnPressureMain).setOnTouchListener(mClickListener);
        findViewById(R.id.mBtnSugarMain).setOnTouchListener(mClickListener);
        findViewById(R.id.btn_fetus_comunity_main).setOnTouchListener(mClickListener);


        //카테고리 크니댄스, 매일보는 동화
        main_cata3_dance.setOnTouchListener(mClickListener);
        main_cata3_fairytail.setOnTouchListener(mClickListener);


        //햄버거 메뉴
        mMenuBtn.setOnTouchListener(mClickListener);
        mBtnSide_01.setOnTouchListener(mClickListener);
        mBtnSide_02.setOnTouchListener(mClickListener);
        mBtnSide_99.setOnTouchListener(mClickListener);
        mBtnSide_98.setOnTouchListener(mClickListener);
        mBtnSide_03.setOnTouchListener(mClickListener);
        mBtnSide_04.setOnTouchListener(mClickListener);
        mBtnSide_06.setOnTouchListener(mClickListener);
        mBtnSide_05.setOnTouchListener(mClickListener);
        mHompageBtn.setOnTouchListener(mClickListener);
        mBtnSide_08.setOnTouchListener(mClickListener);
        btn_side_101.setOnTouchListener(mClickListener);
        btn_side_100.setOnTouchListener(mClickListener);

        main_cata1_rl.setOnTouchListener(mClickListener);
        main_cata2_view.setOnTouchListener(mClickListener);

        //알림
        mAlramBtn.setOnTouchListener(mClickListener);
        mNewMessage.setOnTouchListener(mClickListener);

        //코드 부여(홈)

        findViewById(R.id.btn_growth_main).setContentDescription(getString(R.string.BtnGrowthMain));
        findViewById(R.id.btn_fever_main).setContentDescription(getString(R.string.BtnFeverMain));
        findViewById(R.id.mBtnPsyMain).setContentDescription(getString(R.string.BtnPsyMain));
        findViewById(R.id.mBtnMomHealthMain).setContentDescription(getString(R.string.mBtnMomHealthMain));
        findViewById(R.id.btn_study_main).setContentDescription(getString(R.string.btn_study_main));
        findViewById(R.id.btn_child_comunity_main).setContentDescription(getString(R.string.btn_child_comunity_main));

        findViewById(R.id.btn_medycare).setContentDescription(getString(R.string.BtnMedycare));
        findViewById(R.id.btn_HNews).setContentDescription(getString(R.string.btn_HNews));
        findViewById(R.id.mBtnCallMain).setContentDescription(getString(R.string.BtnCallMain));

        findViewById(R.id.btn_weight_main).setContentDescription(getString(R.string.btn_weight_main));
        findViewById(R.id.btn_play_main).setContentDescription(getString(R.string.btn_play_main));
        findViewById(R.id.mBtnMealMain).setContentDescription(getString(R.string.mBtnMealMain));
        findViewById(R.id.mBtnPressureMain).setContentDescription(getString(R.string.mBtnPressureMain));
        findViewById(R.id.mBtnSugarMain).setContentDescription(getString(R.string.mBtnSugarMain));
        findViewById(R.id.btn_fetus_comunity_main).setContentDescription(getString(R.string.btn_fetus_comunity_main));


        //코드 부여(햄버거 메뉴)
        mMenuBtn.setContentDescription(getString(R.string.MenuBtn));
        mBtnSide_01.setContentDescription(getString(R.string.BtnSide_01));
        mBtnSide_02.setContentDescription(getString(R.string.BtnSide_02));
        mBtnSide_99.setContentDescription(getString(R.string.BtnSide_99));
        mBtnSide_98.setContentDescription(getString(R.string.BtnSide_98));
        mBtnSide_03.setContentDescription(getString(R.string.BtnSide_03));
        mBtnSide_04.setContentDescription(getString(R.string.BtnSide_04));
        mBtnSide_06.setContentDescription(getString(R.string.BtnSide_06));
        mBtnSide_05.setContentDescription(getString(R.string.BtnSide_05));
        mHompageBtn.setContentDescription(getString(R.string.HompageBtn));
        mBtnSide_08.setContentDescription(getString(R.string.BtnSide_08));
        btn_side_101.setContentDescription(getString(R.string.btn_side_101));
        btn_side_100.setContentDescription(getString(R.string.btn_side_100));

        //코드 부여(알림)
        mAlramBtn.setContentDescription(getString(R.string.AlramBtn));
        mNewMessage.setContentDescription(getString(R.string.NewMessage));

        main_cata3_dance.setContentDescription(getString(R.string.main_cata3_dance));
        main_cata3_fairytail.setContentDescription(getString(R.string.main_cata3_fairytail));

    }

    /**
     * 아이데이터 저장
     */

    public void saveChild(){
        try{
            if(!CommonData.getInstance(MainActivity.this).getChldrn().equals("")) {  // 자녀 데이터가 있는 경우
                JSONArray childArr = new JSONArray(CommonData.getInstance(MainActivity.this).getChldrn());
                if (childArr.length() > 0) {

                    mChildMenuItem.clear();


                    for (int i = 0; i < childArr.length(); i++) {

                        JSONObject data = childArr.getJSONObject(i);

                        ChildItem item = null;

                        if(data.getString(CommonData.JSON_CHLDRN_LIFYEA).equals("")){
                            GLog.i("자녀 생년월이이 없는 경우 없는 자식", "dd");
                            continue;
                        }

                        JsonLogPrint.printJson(data.getString(CommonData.JSON_CHLDRN_KGPER));

                        item = new ChildItem(data.getString(CommonData.JSON_CHL_SN),
                                data.getString(CommonData.JSON_CHLDRN_JOINSERIAL),
                                data.getString(CommonData.JSON_CHLDRN_NM),
                                data.getString(CommonData.JSON_CHLDRN_NCM),
                                data.getString(CommonData.JSON_CHLDRN_LIFYEA),
                                data.getString(CommonData.JSON_CHLDRN_AFT_LIFYEA),
                                data.getString(CommonData.JSON_CHL_EXIST_YN),
                                data.getString(CommonData.JSON_CHLDRN_SEX),
                                data.getString(CommonData.JSON_CHLDRN_HP),
                                data.getString(CommonData.JSON_CHLDRN_CI),
                                data.getString(CommonData.JSON_CHLDRN_HEIGHT),
                                data.getString(CommonData.JSON_CHLDRN_BDWGH),
                                data.getString(CommonData.JSON_CHLDRN_HEADCM),
                                data.getString(CommonData.JSON_CHLDRN_MAIN_IMAGE),
                                data.getString(CommonData.JSON_CHLDRN_ORG_IMAGE),
                                data.getString(CommonData.JSON_SAFE_AREA_X),
                                data.getString(CommonData.JSON_SAFE_AREA_Y),
                                data.getString(CommonData.JSON_SAFE_KM),
                                data.getString(CommonData.JSON_SAFE_ALARM_AT),
                                data.getString(CommonData.JSON_SAFE_HEDEXPLN),
                                data.getString(CommonData.JSON_SAFE_ADRES),
                                data.getString(CommonData.JSON_HP_USE_TIME),
                                data.getString(CommonData.JSON_HP_USE_ALARM_AT),
                                data.getString(CommonData.JSON_HP_USE_ESTBS_TIME),
                                data.getString(CommonData.JSON_CHLDRN_KGPER),
                                CommonData.NO

                        );

                        Log.d(TAG,"CommonData.getInstance(MainActivity.this).getSelect() :"+CommonData.getInstance(MainActivity.this).getSelect() );

                        if (CommonData.getInstance(MainActivity.this).getSelect()) {   // 자녀 선택한 데이터가 있다면

                            if (CommonData.getInstance(MainActivity.this).getSelectChildSn().equals(item.getmChlSn())) {    // 자녀선택한 고유키값이 같다면
                                mChildChoiceIndex = i;  // 선택중인 자녀 index 저장
                                item.setmSelect(CommonData.YES);
                                item.setmChldmKgper(data.getString(CommonData.JSON_CHLDRN_KGPER));

                            } else {
                                item.setmSelect(CommonData.NO);
                                item.setmChldmKgper(data.getString(CommonData.JSON_CHLDRN_KGPER));
                            }
                        } else {
                            if (i == 0) { // 선택한 자녀가 없다면 첫번째 데이터를 선택한 자녀로 저장
                                mChildChoiceIndex = i;
                                item.setmSelect(CommonData.YES);
                                CommonData.getInstance(MainActivity.this).setSelect(true);
                                CommonData.getInstance(MainActivity.this).setSelectChildSn(data.getString(CommonData.JSON_CHL_SN));
                            }
                        }

                        try{
                            SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_YYMMDD);
                            item.born_to_day = Util.sumDayCount(format.parse(item.getmChldrnLifyea()), new Date());
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        mChildMenuItem.add(item);
                    }

                    // start 선택한 자녀가 없는경우 첫번째 자녀 선택
                    boolean isSelect = false;
                    for(int j=0; j<mChildMenuItem.size(); j++){
                        if(mChildMenuItem.get(j).getmSelect().equals(CommonData.YES)){
                            isSelect = true;
                        }
                    }

                    if(!isSelect){
                        mChildChoiceIndex   =   0;
                        mChildMenuItem.get(mChildChoiceIndex).setmSelect(CommonData.YES);
                    }
                    // end 선택한 자녀가 없는경우 첫번째 자녀 선택


                    Intent intent = getIntent();
                    chl_sn = intent.getIntExtra("chl_sn", 0);
                    if(chl_sn != 0){
                        for (int z = 0; z < mChildMenuItem.size(); z++){
                            if(mChildMenuItem.get(z).getmChlSn().equals(chl_sn)){
                                mChildChoiceIndex = z;
                                CommonData.getInstance(MainActivity.this).setSelectChildSn(mChildMenuItem.get(mChildChoiceIndex).getmChlSn());
                            }
                        }
                    }

                } else {
                    GLog.i("child length = 0", "dd");
                    mChildMenuItem.clear();
                }
            }

        }catch(Exception e){
            GLog.e(e.toString());
        }
    }

    /**
     * 아이 정보 화면 설정
     */
    public void setBabyData(){
        // 자녀 데이터가 있는경우 UI 세팅
        try{
            if(mChildChoiceIndex == 0){
                mBtnLeftBaby.setEnabled(false);

                if(mChildMenuItem.size()-1 == 0)
                    mBtnRightBaby.setEnabled(false);
                else
                    mBtnRightBaby.setEnabled(true);

            }else{
                if(mChildMenuItem.size()-1 == mChildChoiceIndex){
                    mBtnLeftBaby.setEnabled(true);
                    mBtnRightBaby.setEnabled(false);
                }else{
                    mBtnLeftBaby.setEnabled(true);
                    mBtnRightBaby.setEnabled(true);
                }
            }

            if(!mChildMenuItem.get(mChildChoiceIndex).getmChlSn().equals("")) {
                Logger.i(TAG,"mChildChoiceIndex : "+mChildChoiceIndex + "||"+mChildMenuItem.get(mChildChoiceIndex).getmChldrnNcm() );

                if(mChildMenuItem.get(mChildChoiceIndex).getmChlExistYn().equals(CommonData.NO)){

//                    mBabyInfoMain_1.setVisibility(View.VISIBLE);
                    mBabyMenu.setVisibility(View.GONE);
                    mFetusMenu.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.img_fetus_photo);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.RIGHT_OF,R.id.middle_view);
                    params.leftMargin = (int)getResources().getDimension(R.dimen._20_dp);
                    baby_info_lv.setLayoutParams(params);

                    mBtnLeftBaby.setContentDescription("HL02_002_001_!");
                    mBtnRightBaby.setContentDescription("HL02_002_001_!");


                    //기본 셋팅
//                    txt_baby_name.setText(mChildMenuItem.get(mChildChoiceIndex).getmChldrnNcm().length() > 0 ? mChildMenuItem.get(mChildChoiceIndex).getmChldrnNcm() :
//                            mChildMenuItem.get(mChildChoiceIndex).getmChldrnNm().length() > 0?mChildMenuItem.get(mChildChoiceIndex).getmChldrnNm():"태아" );
                    Util.setSharedPreference(MainActivity.this, "age", "0");
                }else {
                    ChildItem childItem = mChildMenuItem.get(mChildChoiceIndex);
                    // 아이 사진 세팅

//                    mBabyInfoMain_1.setVisibility(View.VISIBLE);

                    mBabyMenu.setVisibility(View.VISIBLE);
                    mFetusMenu.setVisibility(View.GONE);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.RIGHT_OF,R.id.middle_view);
                    params.leftMargin = (int)getResources().getDimension(R.dimen._20_dp);
                    baby_info_lv.setLayoutParams(params);

                    mBtnLeftBaby.setContentDescription("HL01_002_001_!");
                    mBtnRightBaby.setContentDescription("HL01_002_001_!");

                    final Date mDate = Util.getDateFormat(childItem.getmChldrnLifyea(), CommonData.PATTERN_YYMMDD);
                    SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE_DOT);
                    int year = mDate.getYear() + 1900;
                    int month = mDate.getMonth() + 1;
                    int day = mDate.getDate();
                    int age = 0;

                    diffday = Util.GetDifferenceOfDate(currentCal.get(Calendar.YEAR), (currentCal.get(Calendar.MONTH) + 1), currentCal.get(Calendar.DATE),
                            year, month, day);

                    GLog.i("diffDay = " + diffday, "dd");

                    age = diffday / CommonData.YEAR;

                    Util.setSharedPreference(MainActivity.this, "age", String.valueOf(age));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setfetus_img(long P_days) {

        //mBabyInfoMain_2.setVisibility(View.GONE);

        mBackLayout.setBackgroundResource(R.color.color_E7F1FE);

        if (period <= 2) {
            mImgfetusphoto.setImageResource(R.drawable.main_fetus01);
        } else if (period >= 3 && period <= 6) {
            mImgfetusphoto.setImageResource(R.drawable.main_fetus02);
        } else if (period >= 7 && period <= 8) {
            mImgfetusphoto.setImageResource(R.drawable.main_fetus03);
        } else if (period >= 9 && period <= 10) {
            mImgfetusphoto.setImageResource(R.drawable.main_fetus04);
        } else if (period >= 11 && period <= 15) {
            mImgfetusphoto.setImageResource(R.drawable.main_fetus05);
        } else if (period >= 16 && period <= 40) {
            //16주차 이후 2틀마다 이미지 변경
            int flag = (((int) P_days - CommonData.AFTER_BIRTH_16) / 2) % 3;
            switch (flag) {
                case 0:
                    mImgfetusphoto.setImageResource(R.drawable.main_fetus06);
                    break;
                case 1:
                    mImgfetusphoto.setImageResource(R.drawable.main_fetus06b);
                    break;
                case 2:
                    mImgfetusphoto.setImageResource(R.drawable.main_fetus06c);
                    break;

            }

        }
    }

    /**
     * 아이 정보 화면 설정
     */
    public void setBabyData_adapter(int position){
        ChildItem childItem = mChildMenuItem.get(position);
        // 자녀 데이터가 있는경우 UI 세팅
        try{

            if(!childItem.getmChlSn().equals("")) {
                Logger.i(TAG,"mChildChoiceIndex_adapter : "+position + "||"+mChildMenuItem.get(position).getmChldrnOrgImage() );

                if(childItem.getmChlExistYn().equals(CommonData.NO)){

                    long D_days;
                    long P_days = 280;
                    final Date mDate = Util.getDateFormat(mChildMenuItem.get(position).getmChldrnAftLifyea(), CommonData.PATTERN_YYMMDD);
                    SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE_DOT);

                    D_days = Util.getTwoDateCompare(mChildMenuItem.get(position).getmChldrnAftLifyea(), CommonData.PATTERN_YYMMDD);
                    if (D_days > 0) {
//                        txt_baby_birth.setText(format.format(mDate));
                        txt_baby_birth.setVisibility(View.INVISIBLE);
                        txt_baby_born_to_day.setText(getString(R.string.fetus_txt));
                        mFInFoBtn.setVisibility(View.GONE);
                        period = 40;
                    } else {
                        txt_baby_birth.setVisibility(View.VISIBLE);
                        mFInFoBtn.setVisibility(View.VISIBLE);

                        txt_baby_born_to_day.setText("출산까지" + CommonData.STRING_SPACE + Math.abs(D_days) + getString(R.string.day));

                        //임신일짜
                        P_days = CommonData.AFTER_BIRTH_PERIOD - Math.abs(D_days);
                        //ssshin 임시주기 수정  //계산방법 : 40 – { (출산예정일과 오늘 사이의 일 수)/7 }    중괄호 안은 소수점 첫째 자리에서 올림 처리한다
                        double tempDdays = (double) Math.abs(D_days)/7;
                        period = (long) (40 - Math.ceil(tempDdays));
                        if(period == 0 ) period =1;
                        txt_baby_birth.setText("임신" + CommonData.STRING_SPACE + P_days + "일 (" + period + "주)");

                    }

                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.topMargin = (int)getResources().getDimension(R.dimen._5_dp);
                    params1.bottomMargin = (int)getResources().getDimension(R.dimen._5_dp);
                    name_lv.setLayoutParams(params1);


                    txt_baby_age.setVisibility(View.GONE);


                    //임신주기에 따른 말풍선 문구
                    Fetus_BubbleId = "fetus" + period + "_" + num;
                    resStringId = getResources().getIdentifier(Fetus_BubbleId, "string", MAIN_ACTIVITY.getPackageName());
                    mTxtSpeech_Bubble.setText(getString(resStringId));

                    mTxtSpeech_Bubble.setVisibility(View.VISIBLE);

                    mImgBabyPhoto.setVisibility(View.GONE);
                    img_baby_line.setVisibility(View.GONE);
                    btn_picture.setVisibility(View.GONE);
                    mImgfetusphoto.setVisibility(View.VISIBLE);

                    //기본 셋팅
                    setfetus_img(P_days);
                    txt_baby_name.setText(childItem.getmChldrnNcm().length() > 0 ? childItem.getmChldrnNcm() :
                            childItem.getmChldrnNm().length() > 0?childItem.getmChldrnNm():"태아" );
                    Util.setSharedPreference(MainActivity.this, "age", "0");

                }else {
                    // 아이 사진 세팅
                    CustomImageLoader.clearCache();
                    int   height = mImgBabyPhoto.getDrawable().getIntrinsicHeight();
                    CustomImageLoader.displayImageMainNew(MainActivity.this, childItem.getmChldrnOrgImage(), mImgBabyPhoto,height);
                    Log.d(TAG, "Height:"+height);

                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.topMargin = (int)getResources().getDimension(R.dimen._10_dp);
                    params1.bottomMargin = (int)getResources().getDimension(R.dimen._10_dp);
                    name_lv.setLayoutParams(params1);

                    mImgfetusphoto.setVisibility(View.GONE);
                    mImgBabyPhoto.setVisibility(View.VISIBLE);
                    img_baby_line.setVisibility(View.VISIBLE);
                    btn_picture.setVisibility(View.VISIBLE);
                    txt_baby_birth.setVisibility(View.VISIBLE);

                    mFInFoBtn.setVisibility(View.GONE);
                    txt_baby_age.setVisibility(View.VISIBLE);

                    mTxtSpeech_Bubble.setVisibility(View.GONE);
                    mBackLayout.setBackgroundResource(R.color.color_E7F1FE);

                    final Date mDate = Util.getDateFormat(childItem.getmChldrnLifyea(), CommonData.PATTERN_YYMMDD);
                    SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE_DOT);
                    int year = mDate.getYear() + 1900;
                    int month = mDate.getMonth() + 1;
                    int day = mDate.getDate();
                    int age = 0;

                    diffday = Util.GetDifferenceOfDate(currentCal.get(Calendar.YEAR), (currentCal.get(Calendar.MONTH) + 1), currentCal.get(Calendar.DATE),
                            year, month, day);

                    GLog.i("diffDay = " + diffday, "dd");

                    age = diffday / CommonData.YEAR;

                    txt_baby_born_to_day.setText(Util.getAfterBirth_New(MainActivity.this, currentCal.get(Calendar.YEAR), (currentCal.get(Calendar.MONTH) + 1), currentCal.get(Calendar.DATE),
                            year, month, day));

                    android.util.Log.i(TAG, "Lifyea: "+mDate + currentCal.get(Calendar.YEAR)+ (currentCal.get(Calendar.MONTH) + 1) + currentCal.get(Calendar.DATE) + year + month + day );


                    txt_baby_name.setText(childItem.getmChldrnNcm().length() > 0 ?childItem.getmChldrnNcm() : childItem.getmChldrnNm());
                    txt_baby_birth.setText(format.format(mDate));

                    txt_baby_age.setText(getString(R.string.man) + CommonData.STRING_SPACE + age + getString(R.string.age));
                    Util.setSharedPreference(MainActivity.this, "age", String.valueOf(age));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 컨텐츠 변경
     */
    public void main_category() {
        main_spinner.performClick();
        main_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Logger.i(TAG,"main_spinner :" + view);
                // _!: 카운트만 되는 버튼 끝에 붙임
                int j = position +1;
                view.setContentDescription("HL01_004_00"+j+"_!");
                if (view.getContentDescription().toString().contains("_!")) {
                    String temp = view.getContentDescription().toString().replace("_!", "");
                    String cod[] = temp.split("_");

                    DBHelper helper = new DBHelper(MainActivity.this);
                    DBHelperLog logdb = helper.getLogDb();

                    if (cod.length == 1) {
                        logdb.insert(cod[0], "", "", 0, 1);
                        Log.i(TAG, "view.contentDescription : " + cod[0] + "count : 1");
                    } else if (cod.length == 2) {
                        logdb.insert(cod[0], cod[1], "", 0, 1);
                        Log.i(TAG, "view.contentDescription : " + cod[0] + cod[1] + "count : 1");
                    } else {
                        logdb.insert(cod[0], cod[1], cod[2], 0, 1);
                        Log.i(TAG, "view.contentDescription : " + cod[0] + cod[1] + cod[2] + "count : 1");
                    }

                }
                Log.i(TAG,"ACTION_UP");

                CommonData.getInstance(MainActivity.this).setMain_Category(Integer.toString(position));
                setCategory(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setDefaultCategory(){
        android.util.Log.i("Main_category", "setDefaultCategory: "+CommonData.getInstance(MainActivity.this).getMain_Category());
        hideProgress();

        //임신여부
        CommonData common = CommonData.getInstance(MainActivity.this);
        String materPregency = common.getMotherIsPregnancy();
        String iamChild = common.getIamChild();

        if("".equals(CommonData.getInstance(MainActivity.this).getMain_Category())) {
            if("Y".equals(iamChild)) {
                main_spinner.setSelection(4); //교육영상
                setCategory(4);
            }
            else if ("Y".equals(materPregency))  { //임신중일경우
                if ("Y".equals(CommonData.getInstance(MainActivity.this).getTempDivices())) { //체온계여부
                    main_spinner.setSelection(0); //지역 체온
                    setCategory(0);
                } else {
                    main_spinner.setSelection(2); //엄마체중
                    setCategory(2);
                }
            } else {
                if ("Y".equals(CommonData.getInstance(MainActivity.this).getWeighingchk())) { //체중계 여부

                    if (StringUtil.getAfterBirth3Year(CommonData.getInstance(MainActivity.this).getLastChlBirth())) { //3년 경과시
                        main_spinner.setSelection(3); //아이체중
                        setCategory(3);
                    }
                    else{
                        main_spinner.setSelection(2); //엄마체중
                        setCategory(2);
                    }
                }else{
                    main_spinner.setSelection(1); //유의질환
                    setCategory(1);
                }
            }
        }
        else{
            main_spinner.setSelection(StringUtil.getIntger(CommonData.getInstance(MainActivity.this).getMain_Category()));
            setCategory(StringUtil.getIntger(CommonData.getInstance(MainActivity.this).getMain_Category()));
            android.util.Log.i("Main_category", "setDefaultCategory: "+CommonData.getInstance(MainActivity.this).getMain_Category());
        }
    }


    public void setCategory(int poistion){
        CommonData.getInstance(MainActivity.this).setMain_Category(Integer.toString(poistion));
        Logger.i(TAG, "setCategory["+poistion+"]["+mChildChoiceIndex+"]");
        switch (poistion){
            case 0:
                is_temp_menu = false;
                requestDiseaseDataApi();
                break;
            case 1:
                is_temp_menu = true;
                requestDiseaseDataApi();
                break;
            case 2:
                sub_main_category1.setVisibility(View.VISIBLE);
                sub_main_category2.setVisibility(View.GONE);
                sub_main_category3.setVisibility(View.GONE);

                main_cata1_title.setText(getString(R.string.main_category_2));

                String momKg = CommonData.getInstance(MainActivity.this).getMotherWeight();
                if(TextUtils.isEmpty(momKg) == false && StringUtil.getIntVal(momKg) > 0) {
                    main_cata1_content.setText("현재 체중 : "+CommonData.getInstance(MainActivity.this).getMotherWeight() + "kg");
                    main_cata1_content1.setText(CommonData.getInstance(MainActivity.this).getKg_Kind());
                    setColorchange(CommonData.getInstance(MainActivity.this).getKg_Kind());
                }
                else{
                    main_cata1_content.setText("체중을 입력해 주세요.");
                    main_cata1_content1.setText("--");
                    main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_797979));
                }
                main_cata1_view.setText(getString(R.string.main_bottom_sub_title));
                main_cata1_rl.setContentDescription("HL01_007_001_!");
                break;
            case 3:
                requestGrowthLastDataApi(mChildMenuItem.get(mChildChoiceIndex).getmChlSn());



                break;
            case 4:
                sub_main_category1.setVisibility(View.GONE);
                sub_main_category2.setVisibility(View.GONE);
                sub_main_category3.setVisibility(View.VISIBLE);

                main_cata3_title.setText(getString(R.string.main_category_4));
                main_cata3_dance_txt.setText("크니 댄스");
                main_cata3_fairytail.setText("매일 보는 동화");
                break;
        }
    }

    public void setColorchange(String type){
        if(type.contains("저체중군")) {
            main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_f3ab49));
        }else if(type.contains("정상체중군")) {
            main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_8dc349));
        } else if(type.contains("과체중군")) {
            main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.color_e65739));
        } else if(type.contains("비만군")) {
            main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.color_a49bc8));
        } else if(type.contains("고도비만군")) {
            main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.color_717173));
        }else if(type.contains("부족")) {
            main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_f3ab49));
        }else if(type.contains("권장")) {
            main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_8dc349));
        }else if(type.contains("초과")) {
            main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.color_e65739));
        }
    }

    /**
     * 시기별 정보 Dialog
     */
    private void showFetusInfo() {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.fetus_period_webview, null);
        final CPeriodDialog dlg = CPeriodDialog.showDlg(MainActivity.this, view, new CPeriodDialog.DismissListener() {
            @Override
            public void onDissmiss() {

            }
        });
        text_period = (TextView) view.findViewById(R.id.period_date_textview);
        ImageButton pre_btn = (ImageButton) view.findViewById(R.id.pre_btn);
        ImageButton next_btn = (ImageButton) view.findViewById(R.id.next_btn);

        pre_btn.setOnClickListener(mDialogBtn);
        next_btn.setOnClickListener(mDialogBtn);
        url = "http://www.higngkids.co.kr/auth/HL_moms_contents_view.asp?Wkey=";

        text_period.setText(period + "주");
        webView =(WebView) view.findViewById(R.id.web_view);
        count = period;

        webView.setWebViewClient(new TermsWebViewClinet());
        webView.setWebChromeClient(new TermsWebViewChromeClient());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setSavePassword(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setBackgroundColor(Color.TRANSPARENT);

        if ( Build.VERSION.SDK_INT >= 11 )
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        webView.loadUrl(url+period);


    }

    private class TermsWebViewChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            // TODO Auto-generated method stub
            return super.onJsAlert(view, url, message, result);
        }
    }

    private class TermsWebViewClinet extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//            handler.proceed(); // SSL 에러가 발생해도 계속 진행!
            GLog.i("onReceivedSslError()", "dd");
            mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_B);
            mDialog.setTitle(getResources().getString(R.string.popup_dialog_a_type_title));
            mDialog.setContent(getResources().getString(R.string.popup_dialog_serucity_content));
            mDialog.setPositiveButton(getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    handler.proceed(); // SSL 에러가 발생해도 계속 진행!
                    dialog.dismiss();
                }
            });
            mDialog.setNegativeButton(null, new CustomAlertDialogInterface.OnClickListener() {

                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    handler.cancel();    // 취소
                    dialog.dismiss();
                }
            });
            mDialog.show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showProgress();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideProgress();
            super.onPageFinished(view, url);
        }

    }

    View.OnClickListener mDialogBtn = new View.OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.pre_btn) {
                if (count > 1) {
                    --count;
                    text_period.setText(count + "주");
                    webView.loadUrl(url + count);
                }
            } else if (id == R.id.next_btn) {
                if (count < 40) {
                    ++count;
                    text_period.setText(count + "주");
                    webView.loadUrl(url + count);
                }
            }
        }

    };

    public void onCloseMenu(View view){

        mDrawerLayout.closeDrawers();
    }

    private void openDialogRegisterNick() {

//        CommonFunction.openDialogRegisterNick(MainActivity.this, new DialogCommon.UpdateProfile() {
//            @Override
//            public void updateProfile(String NICK, String DISEASE_OPEN, String DISEASE_NM) {
//                if(!NICK.equals("")&&!DISEASE_OPEN.equals("")) {
//                    CDialog dig = CDialog.showDlg(MainActivity.this, NICK+" 님 환영합니다. \n\n굿앤굿어린이케어서비스 회원님들과 \n" +
//                            "즐거운 소통을 시작해보세요.\n\n\n\n※ 미풍양속에 어긋나는 내용 및 사진은 \n" +
//                            " 관리자에의해 삭제될 수 있습니다." );
//                    dig.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            DummyActivity.startActivity(MainActivity.this, CommunityMainFragment.class, null);
//
//                        }
//                    });
//
//                } else {
//                    openDialogRegisterNick();
//                }
//
//            }
//        });
    }


    public class ViewPagerAdapter extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View view = inflater.inflate(R.layout.main_adapter, null);
            container.addView(view);

            mBackLayout = (FrameLayout)view.findViewById(R.id.framebackgroud);
            mImgfetusphoto = (ImageView) view.findViewById(R.id.img_fetus_photo);
            mImgBabyPhoto = (ImageView) view.findViewById(R.id.img_baby_photo);
            img_baby_line = (ImageView) view.findViewById(R.id.img_baby_line);
            btn_picture = (ImageView) view.findViewById(R.id.btn_picture);
            mTxtSpeech_Bubble = (TextView)view.findViewById(R.id.speech_bubble);
            mFInFoBtn     =   (TextView) view.findViewById(R.id.btn_main_fetus_info);
            name_lv  =   (LinearLayout) view.findViewById(R.id.name_lv);

            //main_baby_photo area
            txt_baby_birth = (TextView) view.findViewById(R.id.txt_baby_birth);
            txt_baby_name = (TextView) view.findViewById(R.id.txt_baby_name);
            txt_baby_age = (TextView) view.findViewById(R.id.txt_baby_age);
            txt_baby_born_to_day = (TextView) view.findViewById(R.id.txt_baby_born_to_day);

            baby_info_lv = (LinearLayout) view.findViewById(R.id.baby_info_lv);


            mTxtSpeech_Bubble.setTag("mTxtSpeech_Bubble"+position);

            mImgfetusphoto.setOnClickListener(btnListener);
            btn_picture.setOnClickListener(btnListener);
            mBtnLeftBaby.setOnClickListener(btnListener);
            mBtnRightBaby.setOnClickListener(btnListener);
            mFInFoBtn.setOnClickListener(btnListener);

            //click 저장
            OnClickListener mClickListener = new OnClickListener(btnListener,view, MainActivity.this);

            //홈
            mImgfetusphoto.setOnTouchListener(mClickListener);
            btn_picture.setOnTouchListener(mClickListener);
            mBtnLeftBaby.setOnTouchListener(mClickListener);
            mBtnRightBaby.setOnTouchListener(mClickListener);
            mFInFoBtn.setOnTouchListener(mClickListener);

            //코드 부여(홈)
            mImgfetusphoto.setContentDescription(getString(R.string.Imgfetusphoto));
            btn_picture.setContentDescription(getString(R.string.ImgBabyPhoto));
            mFInFoBtn.setContentDescription(getString(R.string.FInFoBtn));

            Logger.i(TAG,"instantiateItem : "+position );
            setBabyData_adapter(position);


            return view;
        }

        @Override
        public int getCount() {
            return mChildMenuItem.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(View container, int position, Object object) {
            ((CustomViewPager) container).removeView((View) object);
        }

        @Override
        public void notifyDataSetChanged() {
//            TextView temp1 = viewPager.findViewWithTag("mTxtSpeech_Bubble"+mChildChoiceIndex);
//            temp1.setText(resStringId);
        }
    }

    /**
     * ViewPager 전환시 호출되는 메서드
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mChildChoiceIndex = position;
            setBabyData();
            CommonData.getInstance(MainActivity.this).setSelectChildSn(mChildMenuItem.get(mChildChoiceIndex).getmChlSn());
            setCategory(StringUtil.getIntVal(CommonData.getInstance(MainActivity.this).getMain_Category()));
            Logger.i(TAG,"onPageSelected:"+position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /**
     * main_intro_popup
     */
    private void main_intro_popup(){
//        if(!CommonData.getInstance(MainActivity.this).getMainItro_yn()) {
//            Intent intent = new Intent(MainActivity.this, MainIntroMainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
//
//        }
//        requestNoticMainPopYn();
    }

    /**
     * 버튼 클릭 리스너
     */
    View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {

            Intent intent = null;
            String str = "";
            Fragment fragment = null;

//            intent = new Intent(MainActivity.this, EduVideoActivity.class);

            GLog.i("v.getId() = " +v.getId(), "dd");
            showProgress();
            int id = v.getId();
//            if (id == R.id.main_cata3_dance) {
//                intent = new Intent(MainActivity.this, ChunjaeMainActivity.class);
//                intent.putExtra(CommonData.EXTRA_VIDEOTITLE, getString(R.string.grow_dance));
//                startActivity(intent);
//            } else if (id == R.id.main_cata3_fairytail) {
//                intent = new Intent(MainActivity.this, ChunjaeMainActivity.class);
//                intent.putExtra(CommonData.EXTRA_VIMEOCAT, "cat2");
//                intent.putExtra(CommonData.EXTRA_VIDEOTITLE, getString(R.string.daily_fairytail));
//                startActivity(intent);
//            } else
                if (id == R.id.main_cata1_rl || id == R.id.main_cata2_view) {
                GLog.i("v.getId() = " + main_spinner.getSelectedItemPosition(), "dd");
                switch (StringUtil.getIntger(CommonData.getInstance(MainActivity.this).getMain_Category())) {
                    case 0: //열지도 이동
                        if (is_temp_setting) {
                            GpsInfo gps = new GpsInfo(MainActivity.this);
                            if (gps.isGetLocation()) {
                                intent = new Intent(MainActivity.this, TemperActivity.class);
                                startActivity(intent);

                            } else {
                                gps.showSettingsAlert();
                                hideProgress();
                            }
                        } else {
                            intent = new Intent(MainActivity.this, SettingAddressActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 1: //유의질환 이동
                        intent = new Intent(MainActivity.this, TemperActivity.class);
                        intent.putExtra("tabNum", 1);
                        intent.putExtra(CommonData.EXTRA_MAIN_TYPE, 1);
                        startActivity(intent);
                        break;
                    case 2: //엄마건강 이동
                        if (CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                            mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                            mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                            mDialog.setContent(getString(R.string.i_am_child));
                            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                            mDialog.show();

                        } else {
//                            if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                                intent = new Intent(MainActivity.this, MotherHealthMainActivity.class);
//                            } else {
//                                intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                            }
//                            startActivity(intent);
                        }
                        break;
                    case 3: //아이성장 메인이동
//                        startActivity(new Intent(MainActivity.this, GrowthMainActivity.class));
                        break;

                }
                hideProgress();
            } else if (id == R.id.btn_main_fetus_info) {
                showFetusInfo();
            } else if (id == R.id.img_fetus_photo) {
                Logger.i("img_fetus_photo", "img_fetus_photo :" + num);
                hideProgress();
                if (num == 10) {
                    num = 0;
                }
                num++;
                Fetus_BubbleId = "fetus" + period + "_" + num;
                resStringId = getResources().getIdentifier(Fetus_BubbleId, "string", MAIN_ACTIVITY.getPackageName());
//                    mTxtSpeech_Bubble.setText(getString(resStringId));
                ViewPagerAdapter.notifyDataSetChanged();
//            } else if (id == R.id.btn_picture || id == R.id.btn_side_03) {
//                mDrawerLayout.closeDrawers();
//                intent = new Intent(MainActivity.this, BabyInfoActivity.class);
//                startActivity(intent);
//                Util.BackAnimationStart(MainActivity.this);
            } else if (id == R.id.btn_left_baby) {
                hideProgress();
                if (mChildChoiceIndex > 0) {

                    mChildChoiceIndex--;
//                    viewPager.setCurrentItem(mChildChoiceIndex);
                    setBabyData();
                    CommonData.getInstance(MainActivity.this).setSelectChildSn(mChildMenuItem.get(mChildChoiceIndex).getmChlSn());
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.first_baby), Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.btn_right_baby) {
                hideProgress();
                if (mChildChoiceIndex < mChildMenuItem.size() - 1) {

                    mChildChoiceIndex++;
//                    viewPager.setCurrentItem(mChildChoiceIndex);
                    setBabyData();
                    CommonData.getInstance(MainActivity.this).setSelectChildSn(mChildMenuItem.get(mChildChoiceIndex).getmChlSn());
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.last_baby), Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.menu_btn) { // 우측메뉴
                hideProgress();
                Log.i(TAG, "onDrawerClosed mChildMenuItem.get(mChildChoiceIndex).getmChlExistYn() :" + mChildMenuItem.get(mChildChoiceIndex).getmChlExistYn());
                if (mChildMenuItem.get(mChildChoiceIndex).getmChlExistYn().equals("N")) {
                    side_child_lv.setVisibility(View.GONE);
                    side_fetus_lv.setVisibility(View.VISIBLE);
                } else {
                    side_child_lv.setVisibility(View.VISIBLE);
                    side_fetus_lv.setVisibility(View.GONE);
                }
                mDrawerLayout.openDrawer(GravityCompat.END);
            } else if (id == R.id.alram_btn) { // 알림메뉴
                hideProgress();
                intent = new Intent(MainActivity.this, AlramMainActivity.class);
                startActivity(intent);
            } else if (id == R.id.btn_side_04) {
                mDrawerLayout.closeDrawers();
                GpsInfo gps = new GpsInfo(MainActivity.this);
                if (gps.isGetLocation()) {
                    intent = new Intent(MainActivity.this, TemperActivity.class);
                    startActivity(intent);
                    Util.BackAnimationStart(MainActivity.this);
                } else {
                    gps.showSettingsAlert();
                    hideProgress();
                }
            } else if (id == R.id.btn_side_05) {
                mDrawerLayout.closeDrawers();
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                Util.BackAnimationStart(MainActivity.this);
            } else if (id == R.id.btn_weight_main || id == R.id.btn_side_200) {
                mDrawerLayout.closeDrawers();
                if (CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                    hideProgress();
                    mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.i_am_child));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                    mDialog.show();

                } else {
//                    if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                        intent = new Intent(MainActivity.this, MotherHealthMainActivity.class);
//                        intent.putExtra("Num", 0);
//                    } else {
//                        intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                    }
                }
                startActivity(intent);
            } else if (id == R.id.btn_play_main || id == R.id.btn_side_201) {
                mDrawerLayout.closeDrawers();
                if (CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                    hideProgress();
                    mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.i_am_child));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                    mDialog.show();

                } else {
//                    if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                        intent = new Intent(MainActivity.this, MotherHealthMainActivity.class);
//                        intent.putExtra("Num", 1);
//                    } else {
//                        intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                    }
                }
                startActivity(intent);
            } else if (id == R.id.mBtnMealMain || id == R.id.btn_side_202) {
                mDrawerLayout.closeDrawers();
                if (CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                    mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.i_am_child));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                    mDialog.show();

                } else {
//                    if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                        intent = new Intent(MainActivity.this, MotherHealthMainActivity.class);
//                        intent.putExtra("Num", 2);
//                    } else {
//                        intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                    }
                }
                startActivity(intent);
            } else if (id == R.id.mBtnPressureMain || id == R.id.btn_side_203) {
                mDrawerLayout.closeDrawers();
                if (CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                    hideProgress();
                    mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.i_am_child));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                    mDialog.show();

                } else {
//                    if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                        intent = new Intent(MainActivity.this, MotherHealthMainActivity.class);
//                        intent.putExtra("Num", 3);
//                    } else {
//                        intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                    }
                }
                startActivity(intent);
            } else if (id == R.id.mBtnSugarMain || id == R.id.btn_side_204) {
                mDrawerLayout.closeDrawers();
                if (CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                    hideProgress();
                    mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.i_am_child));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                    mDialog.show();

                } else {
//                    if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                        intent = new Intent(MainActivity.this, MotherHealthMainActivity.class);
//                        intent.putExtra("Num", 4);
//                    } else {
//                        intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                    }
                }
//                startActivity(intent);
//            } else if (id == R.id.btn_fetus_comunity_main || id == R.id.btn_child_comunity_main || id == R.id.btn_side_205 || id == R.id.btn_side_101) {
//                hideProgress();
//                mDrawerLayout.closeDrawers();
//                if (TextUtils.isEmpty(CommonData.getInstance(MainActivity.this).getMberNick())) {
//                    openDialogRegisterNick();
//                    return;
//                }
//                DummyActivity.startActivity(MainActivity.this, CommunityMainFragment.class, null);
//            } else if (id == R.id.btn_growth_main || id == R.id.btn_side_01) {
//                mDrawerLayout.closeDrawers();
//                startActivity(new Intent(MainActivity.this, GrowthMainActivity.class));
//                Util.BackAnimationStart(MainActivity.this);
            } else if (id == R.id.btn_fever_main || id == R.id.btn_side_02) {
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this, TemperMainActivity.class));
                Util.BackAnimationStart(MainActivity.this);
//            } else if (id == R.id.mBtnPsyMain || id == R.id.btn_side_99) {
//                mDrawerLayout.closeDrawers();
//                intent = new Intent(MainActivity.this, PsyMainActivity.class);
//                startActivity(intent);
//                Util.BackAnimationStart(MainActivity.this);
//            } else if (id == R.id.btn_study_main || id == R.id.btn_side_100) {
//                mDrawerLayout.closeDrawers();
//                intent = new Intent(MainActivity.this, ChunjaeIntroActivity.class);
//                startActivity(intent);
//                Util.BackAnimationStart(MainActivity.this);
            } else if (id == R.id.mBtnMomHealthMain || id == R.id.btn_side_98) {
                mDrawerLayout.closeDrawers();
                if (CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                    hideProgress();
                    mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.i_am_child));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                    mDialog.show();

                } else {
                    Boolean temp = CommonData.getInstance(MainActivity.this).getHpMjYnJun();
                    Log.i(TAG, "getHpMjYnJun : " + temp);
//                    if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                        intent = new Intent(MainActivity.this, MotherHealthMainActivity.class);
//                    } else {
//                        intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                    }
//                    startActivity(intent);
                }
            } else if (id == R.id.btn_medycare) {
                intent = new Intent(MainActivity.this, BackWebViewInfoActivity.class);
                intent.putExtra(CommonData.EXTRA_URL, "https://wkd.walkie.co.kr/HL_FV/INFO/m_info_and.asp");
                intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.title_medicare1));
                startActivity(intent);
//            } else if (id == R.id.btn_side_06 || id == R.id.btn_HNews) {
//                mDrawerLayout.closeDrawers();
//                intent = new Intent(MainActivity.this, BBSActivity.class);
//                startActivity(intent);
//                Util.BackAnimationStart(MainActivity.this);
            } else if (id == R.id.mBtnCallMain) {
                hideProgress();
                if (CommonData.getInstance(MainActivity.this).getMberGrad().equals("10")) {
                    mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_D);
                    mDialog.setCall(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_B);
                            mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                            mDialog.setContent(getString(R.string.do_call_center));
                            mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), null);
                            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                                String tel = "tel:" + getString(R.string.call_center_number);
                                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                                intentCall.setData(Uri.parse(tel));
                                startActivity(intentCall);
                                dialog.dismiss();
                            });
                            mDialog.show();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CommonData.getInstance(MainActivity.this).getHiPlannerHp().length() > 0) {
                                mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_B);
                                mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                                mDialog.setContent(getString(R.string.do_call_hiplanner).replace("[tel]", CommonData.getInstance(MainActivity.this).getHiPlannerHp()));
                                mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), null);
                                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                                    String tel = "tel:" + CommonData.getInstance(MainActivity.this).getHiPlannerHp();
                                    Intent intentTel = new Intent(Intent.ACTION_DIAL);
                                    intentTel.setData(Uri.parse(tel));
                                    startActivity(intentTel);
                                    dialog.dismiss();
                                });
                                mDialog.show();
                            } else {
                                mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                                mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                                mDialog.setContent(getString(R.string.empty_hiplaaner));
                                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                                mDialog.show();
                            }

                        }
                    });
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.show();

                } else {
                    mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_B);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.call_center2));
                    mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.setPositiveButton(getString(R.string.do_call), (dialog, button) -> {
                        String tel = "tel:" + getString(R.string.call_center_number2);
//                        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                        Intent intentCall = new Intent(Intent.ACTION_DIAL);
                        intentCall.setData(Uri.parse(tel));
                        startActivity(intentCall);
                        dialog.dismiss();
                    });
                    mDialog.show();
                }
            } else if (id == R.id.btn_side_10) {
                mDrawerLayout.closeDrawers();
                if (CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                    hideProgress();
                    mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.i_am_child));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                    mDialog.show();

                } else {
                    if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
                        DummyActivity.startActivityForResult(((Activity) MainActivity.this), WeightBigDataInputFragment.REQ_WEIGHT_PREDICT, WeightBigDataInputFragment.class, new Bundle());
//                    } else {
//                        intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                        startActivity(intent);
                    }

                }
            } else if (id == R.id.btn_side_08) {
                hideProgress();
                mDrawerLayout.closeDrawers();
                KakaoLinkUtil.kakaoAddFriends(MainActivity.this);
            } else if (id == R.id.hompage_btn) {
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_homepage))));
            } else if (id == R.id.list_add_change) {
                hideProgress();
                main_category();
            }

            if (!str.equals("")) {
                mTitleTv.setText(str);
            }

        }
    };

    /**
     * 최근 게시판 여부
     */
    public void requestLastAlramDataApi() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // json={"api_code":"asstb_kbtg_alimi_view_on","insures_code":"108","mber_sn":"115232","pushk":"0"}
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_ASSTB_KBTG_ALIMI_VIEW_ON);    //  api 코드명
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);          //  insures 코드
            object.put(CommonData.JSON_MBER_SN,  CommonData.getInstance(MainActivity.this).getMberSn());             //  회원고유값
            object.put(CommonData.JSON_PUSH_K, "0");               //

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(this, NetworkConst.NET_ASSTB_KBTG_ALIMI_VIEW_ON, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgress(),false);
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    /**
     * 최근 키 몸무게 가져오기
     */
    public void requestGrowthLastDataApi(String chl_sn) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // {"api_code":"chldrn_growth_last_cm_height","insures_code":"108","app_code":"android","mber_sn":"18622","chl_sn":"1312"}
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_CHLDRN_GROWTH_LAST_CM_HEIGHT);    //  api 코드명
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);          //  insures 코드
            object.put(CommonData.JSON_APP_CODE, CommonData.APP_CODE_ANDROID);          //  os
            object.put(CommonData.JSON_MBER_SN,  CommonData.getInstance(MainActivity.this).getMberSn());             //  회원고유값
            object.put(CommonData.JSON_CHL_SN, chl_sn);               //  자녀키값

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(this, NetworkConst.NET_GROWTH_LAST_DATA, NetworkConst.getInstance().getDefDomain(), networkListener, params, null,false);
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    /**
     * 메인 유해질환 및 지역가져오기
     */
    public void requestDiseaseDataApi() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // { "API_CODE": "HX001", "LOC_NM_1": "경기도", "LOC_NM_2": "안양시", "AVG_FEVER": "37.8", "LOC_1": "37.3942527", "LOC_2": "126.9568209",
        // "DATA": [ { "DZNUM": "1", "DZNAME": "기관지염", "WEEKAGO_1": "25", "WEEKAGO_2": "31" }, { "DZNUM": "2", "DZNAME": "(모)세기관지염", "WEEKAGO_1": "6", "WEEKAGO_2": "6" } ], "DATA_LENGTH": "2" }
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.METHOD_DISEASE_HX001);    //  api 코드명
            object.put(CommonData.JSON_MBER_SN_F,  CommonData.getInstance(MainActivity.this).getMberSn());             //  회원고유값

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            JsonLogPrint.printJson(params.toString());

            RequestApi.requestApi(this, NetworkConst.NET_DISEASE_INFO, NetworkConst.getInstance().getFeverDomain(), networkListener, params, null,false);
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    /**
     * 유의질환 가져오기
     * @param startDate  시작일
     * @param ednDate  종료일
     */
    public void requestEpidemicData(Date startDate, Date ednDate){
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE);

            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HB002);

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(this, NetworkConst.NET_GET_EPIDEMIC, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgress(),true);
        }catch(Exception e){
            GLog.i(e.toString(), "dd");
        }
    }


    /**
     * 최근 게시판 목록
     */
    public void requestBBSListApi() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_CONTENT_SPECIAL_BBSLIST);    //  api 코드명
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);          //  insures 코드
            object.put(CommonData.JSON_MBER_SN,  CommonData.getInstance(MainActivity.this).getMberSn());             //  회원고유값
            object.put(CommonData.JSON_PAGENUMBER, "1");               //  페이지 번호
            object.put(CommonData.JSON_CONTENT_TYP, "0");               //  게시판 분류 번호 0: 전체  1: 질병예방 2:영양 3:운동 4: 심리

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(this, NetworkConst.NET_GET_BBS_LIST, NetworkConst.getInstance().getDefDomain(), networkListener, params,getProgress(),true);
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    /**
     * hsh 메인 체온 알람
     */
    public void requestTempPopApi() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_ALARM_POP);    //  api 코드명
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);          //  insures 코드
            object.put(CommonData.JSON_APP_CODE, CommonData.APP_CODE_ANDROID);          //  insures 코드

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(MainActivity.this, NetworkConst.NET_GET_ALARM_POP, NetworkConst.getInstance().getDefDomain(), networkListener, params,getProgress(),true);
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }


    /**
     * 마케팅 정보 및 위치정보 동의
     */
    public void requestAgreeAlarmSetting(String YN){
        GLog.i("requestAgreeAlarmSetting", "dd");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_MBER_CHECK_AGREE_YN);
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
            object.put(CommonData.JSON_MBER_SN, CommonData.getInstance(MainActivity.this).getMberSn());

            object.put(CommonData.JSON_MARKETING_YN, YN);
            object.put(CommonData.JSON_LOCATION_YN, "");

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(MainActivity.this, NetworkConst.NET_MBER_CHECK_AGREE_YN, NetworkConst.getInstance().getDefDomain(), networkListener, params ,getProgress(),true);
        }catch(Exception e){
            GLog.i(e.toString(), "dd");
        }
    }

    /**
     * 공지사항 팝업
     */
    public void requestNoticMainPopYn(){
        GLog.i("requestNoticMainPopYn", "dd");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_NOTICE_MAIN_POP_YN);
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
            object.put(CommonData.JSON_APP_CODE, CommonData.APP_CODE_ANDROID);
            object.put(CommonData.JSON_MBER_SN, CommonData.getInstance(MainActivity.this).getMberSn());

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(MainActivity.this, NetworkConst.NET_NOTICE_MAIN_POP_YN, NetworkConst.getInstance().getDefDomain(), networkListener, params ,getProgress(),true);
        }catch(Exception e){
            GLog.i(e.toString(), "dd");
        }
    }







    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            hideProgress();
            switch ( type ) {
                case NetworkConst.NET_GET_EPIDEMIC:             // 유행 질병 데이터 가져오기
                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            try {
                                JsonLogPrint.printJson(resultData.toString());
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);

                                if (data_yn.equals(CommonData.YES)) {
                                    JSONArray resultArr = resultData.getJSONArray(CommonData.JSON_DATA_F);
                                    ArrayList<EpidemicItem> epidemicItems = new ArrayList<>();

                                    for(int i = 0; i < resultArr.length(); i++){
                                        JSONObject resultObject = resultArr.getJSONObject(i);

                                        EpidemicItem curItem = new EpidemicItem();
                                        curItem.setDzNum(resultObject.getInt(CommonData.JSON_DZNUM));
                                        curItem.setDzName(resultObject.getString(CommonData.JSON_DZNAME));
                                        curItem.setWeekago_1(resultObject.getInt(CommonData.JSON_WEEKAGO_1));
                                        curItem.setWeekago_2(resultObject.getInt(CommonData.JSON_WEEKAGO_2));
                                        epidemicItems.add(curItem);
                                    }

                                    epidemicItems.remove(epidemicItems.get(epidemicItems.size()-2));
                                    epidemicItems.remove(epidemicItems.get(epidemicItems.size()-2));

                                    for (int i = 0; i < epidemicItems.size(); i++){
                                        epidemicItems.get(i).setRatio(getRatio(epidemicItems.get(i).getWeekago_1(), epidemicItems.get(epidemicItems.size()-1).getWeekago_1(), 100));
                                    }

                                    epidemicItems.remove(epidemicItems.get(epidemicItems.size()-1));

                                    Collections.sort(epidemicItems, (a, b) -> a.getRatio() > b.getRatio() ? -1: a.getRatio() < b.getRatio() ? 1:0);

                                    //Collections.sort(epidemicItems, comRatio);
                                    //Collections.reverse(epidemicItems);

                                    mEpidemicList = epidemicItems;


                                    //mTxtEpidemic.setText(getString(R.string.epidemic_main) + " " + mEpidemicList.get(0).getDzName());
                                    //hsh start
                                    if(inTentGo !=null && (typePush== FirebaseMessagingService.FEVER || typePush==FirebaseMessagingService.FEVER_MOVIE||typePush==FirebaseMessagingService.DIESEASE)){
                                        startActivity(inTentGo);
                                        Util.BackAnimationStart(MainActivity.this);
                                        inTentGo = null;
                                    }
                                    //hsh end
                                }else{
                                    //mTxtEpidemic.setText(getString(R.string.epidemic_main) + " " + getString(R.string.empty_epidemic_main));
                                }
                            }catch(Exception e){
                                GLog.e(e.toString());
                                //mTxtEpidemic.setText(getString(R.string.epidemic_main) + " " + getString(R.string.empty_epidemic_main));
                            }
                    }
                    break;
                case NetworkConst.NET_GROWTH_LAST_DATA:

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN);
                                if(data_yn.equals(CommonData.YES)){
                                    mLastHeight = resultData.getString(CommonData.JSON_LAST_HRIGTH);
                                    mLastWeight = resultData.getString(CommonData.JSON_LAST_BDWGH);
                                    mLastCmResult = resultData.getString(CommonData.JSON_CM_REUSLT);
                                    mLastKgResult = resultData.getString(CommonData.JSON_KG_REUSLT);
                                    mLastHeightDe = resultData.getString(CommonData.JSON_LAST_HRIGTH_DE);
                                    mLastWeightDe = resultData.getString(CommonData.JSON_LAST_BDWGH_DE);


                                }else{
                                    mLastHeight = "0";
                                    mLastWeight = "0";
                                    mLastCmResult = "";
                                    mLastKgResult = "";
                                    mLastHeightDe = "";
                                    mLastWeightDe = "";
                                }

                                if((mLastWeight.length() > 0 && !"0".equals(mLastWeight)) && (!"0".equals(mLastKgResult) && mLastKgResult.length() > 0)){
                                    main_cata1_content.setText("현재 체중 : " + mLastWeight+"kg");
                                    main_cata1_content1.setText(mLastKgResult + "%");


                                    android.util.Log.i("MainActivity", "setCategory: "+ mChildMenuItem.get(mChildChoiceIndex).getmChldrnBdwgh());

                                }
                                else{
                                    main_cata1_content.setText("체중을 입력해 주세요.");
                                    main_cata1_content1.setText("--");
                                }

                                main_cata1_title.setText(getString(R.string.main_category_3));
                                main_cata1_content1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_797979));

                                main_cata1_rl.setContentDescription("HL01_008_001_!");
                                main_cata1_view.setText(getString(R.string.main_bottom_sub_title));

                                sub_main_category1.setVisibility(View.VISIBLE);
                                sub_main_category2.setVisibility(View.GONE);
                                sub_main_category3.setVisibility(View.GONE);

                            } catch (Exception e) {
                                GLog.e(e.toString());
                                mLastHeight = "0";
                                mLastWeight = "0";
                                mLastCmResult = "";
                                mLastKgResult = "";
                                mLastHeightDe = "";
                                mLastWeightDe = "";
                            } finally {
                                sub_main_category1.setVisibility(View.VISIBLE);
                                sub_main_category2.setVisibility(View.GONE);
                                sub_main_category3.setVisibility(View.GONE);
                            }

                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            mLastHeight = "0";
                            mLastWeight = "0";
                            mLastCmResult = "";
                            mLastKgResult = "";
                            mLastHeightDe = "";
                            mLastWeightDe = "";
                            break;


                    }
                    break;
                case NetworkConst.NET_GET_BBS_LIST:
                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            try {
                                JSONArray bbsList = resultData.getJSONArray(CommonData.JSON_BBSLIST);
                                mImgNew.setVisibility(View.GONE);
                                if(bbsList.length() > 0){
                                    for (int i= 0; i < bbsList.length(); i++){
                                        JSONObject bbsListJSONObject = bbsList.getJSONObject(i);
                                        if(!CommonData.getInstance(MainActivity.this).getBBSUrlShowCheck(bbsListJSONObject.getString(CommonData.JSON_INFO_TITLE_URL))){
                                            // new 처리
                                            //mImgNew.setVisibility(View.VISIBLE);
                                            break;
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            break;
                    }
                    break;
                case NetworkConst.NET_GET_ALARM_POP:	// 게시판 리스트 가져오기
                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_ALARM_POP API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_DATE_YN);
                                if(data_yn.equals(CommonData.YES)){
                                    GLog.i("NET_GET_ALARM_POP YYYY", "dd");
                                    JSONArray bbsList = resultData.getJSONArray(CommonData.JSON_MAIN_POP_LIST);
                                    for(int i = 0; i < bbsList.length(); i++){
                                        JSONObject resultObject = bbsList.getJSONObject(i);

                                        sMainPopTitle = resultObject.getString(CommonData.JSON_POP_TITLE);
                                        sMainPopTxt = resultObject.getString(CommonData.JSON_POP_TXT);
                                    }
                                    bMainPop = true;
                                    showTempPop();
                                }else{
                                    GLog.i("NET_GET_ALARM_POP NNNN", "dd");
                                    sMainPopTitle="";
                                    sMainPopTxt="";
                                    bMainPop = false;
                                    if(false){
                                        sMainPopTitle="체온 측정 안내";
                                        sMainPopTxt="체온 항상 똑같지 않아요.\r\n언제, 어디에 따라 달라져요.\r\n우리 아이 체온 미리 측정해요.";
                                        showTempPop();
                                    }
                                }
                            } catch (Exception e) {
                                GLog.e(e.toString());
                                sMainPopTitle="";
                                sMainPopTxt="";
                            }

                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            sMainPopTitle="";
                            sMainPopTxt="";
                            break;
                    }
                    break;
                case NetworkConst.NET_DISEASE_INFO:
                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            try {
                                JsonLogPrint.printJson(resultData.toString());

                                JSONArray Disease = resultData.getJSONArray("DATA");

                                for (int i = 0; i < Disease.length(); i++) {
                                    JSONObject resultObject = Disease.getJSONObject(i);

                                    dzname[i] = resultObject.getString("DZNAME");
                                    weekago_1_per[i] = resultObject.getString("WEEKAGO_1_PER");
                                }
                                main_cata2_title.setText(getString(R.string.main_category_1));
                                main_cata2_rank1.setText("1. "+dzname[0]);
                                main_cata2_rank_ratio1.setText(weekago_1_per[0] + "%");

                                main_cata2_rank2.setText("2. "+dzname[1]);
                                main_cata2_rank_ratio2.setText(weekago_1_per[1] + "%");

                                main_cata2_view.setText(getString(R.string.main_bottom_sub_title));
                                main_cata2_view.setContentDescription("HL01_006_001_!");


                                if (TextUtils.isEmpty(resultData.getString("LOC_NM_2")) == false && TextUtils.isEmpty(resultData.getString("AVG_FEVER")) == false) {
                                    main_cata1_title.setText(getString(R.string.main_category_0));
                                    main_cata1_content.setText(resultData.getString("LOC_NM_2"));


                                    if ("0".equals(resultData.getString("AVG_FEVER"))) {
                                        main_cata1_content1.setText("체온정보없음");
                                    } else {
                                        main_cata1_content1.setText("평균 " + resultData.getString("AVG_FEVER") + "℃");

                                    }
                                    main_cata1_view.setText(getString(R.string.main_bottom_sub_title1));

                                    is_temp_setting = true;
                                    main_cata1_rl.setContentDescription("HL01_005_002_!");


                                } else {
                                    main_cata1_title.setText(getString(R.string.main_category_0));
                                    main_cata1_content.setText("-");
                                    main_cata1_content1.setText("-");
                                    main_cata1_view.setText(getString(R.string.main_bottom_sub_title2));
                                    is_temp_setting = false;
                                    main_cata1_rl.setContentDescription("HL01_005_001_!");

                                }
                                main_cata1_content1.setTextColor(getResources().getColor(R.color.color_EE8C2A));

                                if(!is_temp_menu) {
                                    sub_main_category1.setVisibility(View.VISIBLE);
                                    sub_main_category2.setVisibility(View.GONE);
                                    sub_main_category3.setVisibility(View.GONE);
                                } else {
                                    sub_main_category1.setVisibility(View.GONE);
                                    sub_main_category2.setVisibility(View.VISIBLE);
                                    sub_main_category3.setVisibility(View.GONE);
                                }
//


                            } catch (Exception e) {
                                GLog.e(e.toString());

                            } finally {
                                if(!is_temp_menu) {
                                    sub_main_category1.setVisibility(View.VISIBLE);
                                    sub_main_category2.setVisibility(View.GONE);
                                    sub_main_category3.setVisibility(View.GONE);
                                } else {
                                    sub_main_category1.setVisibility(View.GONE);
                                    sub_main_category2.setVisibility(View.VISIBLE);
                                    sub_main_category3.setVisibility(View.GONE);
                                }
                            }


                            break;

                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            sMainPopTitle="";
                            sMainPopTxt="";
                            break;
                    }
                    break;

                case NetworkConst.NET_ASSTB_KBTG_ALIMI_VIEW_ON:
                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            try {
                                JsonLogPrint.printJson(resultData.toString());

                                String data_yn = resultData.getString(CommonData.JSON_DATA_YN);
                                JSONArray chlmReadern = resultData.getJSONArray("chlmReadern");
                                String result_code = chlmReadern.getJSONObject(0).getString("result_code");
                                String idx = chlmReadern.getJSONObject(0).getString("kbta_idx");
                                String title = chlmReadern.getJSONObject(0).getString("kbt");
                                if(data_yn.equals(CommonData.YES)) {   // 알림 최신
                                    if(result_code.equals("8888")){
                                        Util.setSharedPreference(MainActivity.this, "kbta_idx", idx);
                                        setNewIcon(idx,title);
                                    }else{
                                        if(!Util.getSharedPreference(MainActivity.this,"kbta_idx").equals(idx)){
                                            Util.setSharedPreference(MainActivity.this, "new_check", "1"); // 1: true 0 :false
                                            Util.setSharedPreference(MainActivity.this, "kbta_idx", idx);
                                        }
                                        setNewIcon(idx,title);
                                    }

                                }
                            } catch (Exception e) {
                                GLog.e(e.toString());
                            }
                            break;

                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            break;
                    }
                    break;
                case NetworkConst.NET_MBER_CHECK_AGREE_YN :

                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");

                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN);
                                if(data_yn.equals(CommonData.YES)){

                                    mDialog =   new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_join_complete_1, null);
                                    TextView dialog_content2 = view.findViewById(R.id.dialog_content2);
                                    TextView dialog_content1 = view.findViewById(R.id.dialog_content1);
                                    TextView dialog_content = view.findViewById(R.id.dialog_content);
                                    TextView dialog_title = view.findViewById(R.id.dialog_title);


                                    dialog_title.setVisibility(View.GONE);
                                    dialog_content2.setVisibility(View.GONE);
                                    dialog_content1.setVisibility(View.GONE);

                                    if(resultData.getString(CommonData.JSON_MARKETING_YN).equals(CommonData.YES)) {
                                        dialog_content.setText(String.format(getResources().getString(R.string.popup_dialog_contactor_complete1_4), CDateUtil.getToday_year_month_day(),"동의"));
                                    } else {
                                        dialog_content.setText(String.format(getResources().getString(R.string.popup_dialog_contactor_complete1_4),CDateUtil.getToday_year_month_day(),"거부"));
                                    }

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                    params.gravity = Gravity.CENTER;
                                    mDialog.setContentView(view, params);
                                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(CustomAlertDialog dialog, Button button) {
                                            dialog.dismiss();

                                            try {
                                                CommonData.getInstance(MainActivity.this).setMarketing_yn(resultData.getString(CommonData.JSON_MARKETING_YN));
                                                CommonData.getInstance(MainActivity.this).setLocation_yn(resultData.getString(CommonData.JSON_LOCATION_YN));
                                                CommonData.getInstance(MainActivity.this).setEvent_alert_yn(resultData.getString(CommonData.JSON_EVENT_ALERT_YN).equals(CommonData.YES) ? true : false);
                                                CommonData.getInstance(MainActivity.this).setMapPushAlarm(resultData.getString(CommonData.JSON_HEAT_YN).equals(CommonData.YES) ? true : false);
                                                CommonData.getInstance(MainActivity.this).setDietPushAlarm(resultData.getString(CommonData.JSON_DIET_YN).equals(CommonData.YES) ? true : false);
                                                CommonData.getInstance(MainActivity.this).setDisease_alert_yn(resultData.getString(CommonData.JSON_DISEASE_ALERT_YN).equals(CommonData.YES) ? true : false);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            main_intro_popup();

                                        }
                                    });
                                    mDialog.show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:	// 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:	// 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            break;
                    }
                    break;

                case NetworkConst.NET_NOTICE_MAIN_POP_YN :

                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");

                            try {
                                String data_yn = resultData.getString("date_yn");
                                if(data_yn.equals(CommonData.YES)){

                                    if(!CommonData.getInstance(MainActivity.this).getEventNotSee().equals(CDateUtil.getToday_yyyy_MM_dd())) {
                                        try {
                                            mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_B);
                                            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_dialog_main_event, null);
                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                            params.gravity = Gravity.CENTER;
                                            mDialog.setContentView(view, params);

                                            TextView tv1 = view.findViewById(R.id.dialog_title);
                                            ImageView iv1 = view.findViewById(R.id.iv1);
                                            Button b1 = view.findViewById(R.id.detail_btn);

                                            JSONArray noticeArr = new JSONArray(resultData.getString("main_pop_list"));
                                            JSONObject noticeData = noticeArr.getJSONObject(0);

                                            String seq = noticeData.getString("seq");

                                            tv1.setText(noticeData.getString("title"));

//                                            Glide.with(MainActivity.this).load(noticeData.getString("notice_title_img_url"))
//                                                    .apply(new RequestOptions()
//                                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                                            .placeholder(R.drawable.event_img)
//                                                            .skipMemoryCache(true)).into(iv1);

                                            b1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(MainActivity.this, AlramMainActivity.class);
                                                    intent.putExtra("EVENT_POP", seq);
                                                    startActivity(intent);
                                                    mDialog.dismiss();
                                                }
                                            });

                                            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(CustomAlertDialog dialog, Button button) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            mDialog.setNegativeButton("오늘은 그만보기", new CustomAlertDialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(CustomAlertDialog dialog, Button button) {
                                                    CommonData.getInstance(MainActivity.this).setEventNotSee(CDateUtil.getToday_yyyy_MM_dd());
                                                    dialog.dismiss();
                                                }
                                            });

                                            mDialog.show();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:	// 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:	// 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            break;
                    }
                    break;
            }

        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
            dialog.show();
        }
    };

    private void clearCache() {
        final File cacheDirFile = this.getCacheDir();
        if (null != cacheDirFile && cacheDirFile.isDirectory()) {
            clearSubCacheFiles(cacheDirFile);
        }
    }

    private void clearSubCacheFiles(File cacheDirFile) {
        if (null == cacheDirFile || cacheDirFile.isFile()) {
            return;
        }
        for (File cacheFile : cacheDirFile.listFiles()) {
            if (cacheFile.isFile()) {
                if (cacheFile.exists()) {
                    cacheFile.delete();
                }
            } else {
                clearSubCacheFiles(cacheFile);
            }
        }
    }

    public double getRatio(int num_1, int num_2, int num_3){

        BigDecimal bd_1 = null;
        BigDecimal bd_2 = null;
        BigDecimal bd_3 = new BigDecimal(""+num_3);

        if(num_1 > 0){
            bd_1 = new BigDecimal(""+num_1);
            bd_2 = new BigDecimal(""+num_2);

            BigDecimal ratio = bd_1.divide(bd_2, 3, BigDecimal.ROUND_HALF_UP).multiply(bd_3) ;

            return ratio.doubleValue();
        }else{
            return 0d;
        }
    }


    @Override protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    private void showTempPop(){
        GLog.i("showTempPop t: "+sMainPopTitle+" : "+sMainPopTxt, "dd");
        if(mDialog!=null){
            GLog.i("showTempPop t dialog : "+mDialog.isShowing(), "dd");
        }

        if(!bMainPop || sMainPopTitle.equals("") || sMainPopTxt.equals("") || (mDialog!=null && mDialog.isShowing()))
            return;


        String sNowTime = Util.getNowDateFormat();
        GLog.i("showTempPop t now : "+sNowTime+" before : "+CommonData.getInstance(MainActivity.this).getAfterMainPopupShowCheck(), "dd");
        if(CommonData.getInstance(MainActivity.this).getAfterMainPopupShowCheck().equals(sNowTime)){

        }else{
            Intent intent = new Intent(MainActivity.this, TemperActivity.class);
            intent.putExtra("title",sMainPopTitle);
            intent.putExtra("txt",sMainPopTxt);
            startActivity(intent);
        }

    }

    public void setNewIcon(String idx,String text){
        if(text.length() > 15){
            text = text.substring(0, 15) + "    \n" + text.substring(15);
        }
        mNewMessage.setText(text);
        if (Util.getSharedPreference(MainActivity.this,"kbta_idx").equals(idx) && !Util.getSharedPreference(MainActivity.this,"kbta_idx").equals("")) {
            if(Util.getSharedPreference(MainActivity.this,"new_check").equals("1")) {
                mAlramNew.setVisibility(View.VISIBLE);
                mNewMessage.setVisibility(View.VISIBLE);
            }else{
                mAlramNew.setVisibility(View.GONE);
                mNewMessage.setVisibility(View.GONE);
            }
        } else if(!Util.getSharedPreference(MainActivity.this,"kbta_idx").equals(idx) && !Util.getSharedPreference(MainActivity.this,"kbta_idx").equals("")){
            mAlramNew.setVisibility(View.VISIBLE);
            mNewMessage.setVisibility(View.VISIBLE);
            Util.setSharedPreference(MainActivity.this,"new_check","1");
        }else{
            Util.setSharedPreference(MainActivity.this,"new_check","0");
            mAlramNew.setVisibility(View.GONE);
            mNewMessage.setVisibility(View.GONE);
        }
    }


    public MakeProgress getProgress(){
//        /* 테스트 후 주석 해제
        if(mProgress != null) {
            return mProgress;
        }else {
            return mProgress = new MakeProgress(this);
        }
//        */
    }

    /**
     * 프로그래스 활성화
     */
    public void showProgress() {

        if ( mProgress == null )
            mProgress = new MakeProgress(this);

        mProgress.show();
    }

    /**
     * 프로그래스 비활성화
     */
    public void hideProgress() {

        if (mProgress != null) {
            mProgress.dismiss();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        GLog.i("onResume", "dd");
        if(mJunChangeChild){
            saveChild();
            mJunChangeChild = false;
        }

        /**
         * 만삭체중예측 버튼 문진했고 임신중이면 보이게
         */

        if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
            if(CommonData.getInstance(MainActivity.this).getbirth_chl_yn().equals("N")){
                mBtnSide_10.setVisibility(View.VISIBLE);
            } else {
                mBtnSide_10.setVisibility(View.GONE);
            }
        }else {
            mBtnSide_10.setVisibility(View.GONE);
        }


        /**
         * 만삭체중예측 버튼 문진했고 임신중이면 보이게
         */

//        if(!CommonData.getInstance(MainActivity.this).getMotherBigPopupShowCheck()) {
//            if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                if (CommonData.getInstance(MainActivity.this).getbirth_chl_yn().equals("N")) {
//                    CustomAlertDialog mDialog2 = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_F);
//                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_dialog_noview, null);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                    params.gravity = Gravity.CENTER;
//                    mDialog2.setContentView(view, params);
//                    mDialog2.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
//                        dialog.dismiss();
//                    });
//
//                    mDialog2.setNegativeButton(getString(R.string.popup_dialog_button_cancel), (dialog, button) -> {
//                        dialog.dismiss();
//
//                        Intent intent;
//                        if (CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
//                            hideProgress();
//                            CustomAlertDialog mDialog1 = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
//                            mDialog1.setTitle(getString(R.string.popup_dialog_a_type_title));
//                            mDialog1.setContent(getString(R.string.i_am_child));
//                            mDialog1.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
//                            mDialog1.show();
//
//                        } else {
//                            if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                                DummyActivity.startActivityForResult(((Activity) MainActivity.this), WeightBigDataInputFragment.REQ_WEIGHT_PREDICT, WeightBigDataInputFragment.class, new Bundle());
//                            } else {
//                                intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                                startActivity(intent);
//                            }
//
//                        }
//
//                    });
//
//                    mDialog2.setCheckboxButtonLv((dialog, button) -> {
//                        mDialog2.setChangeCheckboxImg_motherBig();
//                        CommonData.getInstance(MainActivity.this).setMotherBigPopupShowCheck(mDialog2.isMotherChecked());
//                    });
//
//                    mDialog2.setCancelable(false);
//                    mDialog2.show();
//                }
//            }
//        }


        requestLastAlramDataApi();

        setDefaultCategory();

        ViewPagerAdapter = new ViewPagerAdapter();
//        viewPager.setAdapter(ViewPagerAdapter);
//        viewPager.setOffscreenPageLimit(mChildMenuItem.size());
//        viewPager.setCurrentItem(mChildChoiceIndex);
//        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        setBabyData();
        requestBBSListApi();

        int push_type = getIntent().getIntExtra(CommonData.EXTRA_PUSH_TYPE, 0);
        GLog.i("onResume = "+push_type, "dd");
        if(push_type > 0){
            Intent intent;
            typePush = push_type;
            switch (push_type){
                case FirebaseMessagingService.NOTICE:
                    break;
//                case FirebaseMessagingService.NEWS:
//                    String info_sn = getIntent().getStringExtra(CommonData.EXTRA_INFO_SN);
//                    intent = new Intent(MainActivity.this, BBSActivity.class);
//                    intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
//                    intent.putExtra(CommonData.EXTRA_INFO_SN, info_sn);
//                    startActivity(intent);
//                    Util.BackAnimationStart(MainActivity.this);
//                    break;
                case FirebaseMessagingService.FEVER:
                    intent = new Intent(MainActivity.this, TemperActivity.class);
                    inTentGo = intent;
                    /* hsh old
                    startActivity(intent);
                    BleUtil.BackAnimationStart(MainActivity.this);*/
                    break;
                //hsh start
//                case FirebaseMessagingService.FEVER_MOVIE:
//                    GLog.i("FirebaseMessagingService.FEVER_MOVIE", "dd");
//                    String info = getIntent().getStringExtra(CommonData.EXTRA_INFO_SN);
//                    intent = new Intent(MainActivity.this, YoutubeActivity.class);
//                    intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
//                    intent.putExtra(CommonData.EXTRA_INFO_SN, info);
//                    inTentGo = intent;
//                    break;
                case FirebaseMessagingService.DIESEASE:
                    GLog.i("FirebaseMessagingService.DIESEASE", "dd");
                    intent = new Intent(MainActivity.this, TemperActivity.class);
                    intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
                    inTentGo = intent;
                    break;
                case FirebaseMessagingService.DIET: //다이어트 독려
                    GLog.i("FirebaseMessagingService.DIET", "dd");
                    if(CommonData.getInstance(MainActivity.this).getMberGrad().equals("10")) {
                        if(CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                            mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                            mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                            mDialog.setContent(getString(R.string.i_am_child));
                            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                            mDialog.show();

//                        }else{
//                            if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                                if(CommonData.getInstance(MainActivity.this).getbirth_chl_yn().equals("Y")) {
//                                    intent = new Intent(MainActivity.this, RequestDietProgramActivity.class);
//                                    intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
//                                    Util.BackAnimationStart(MainActivity.this);
//                                    startActivity(intent);
//                                }
////                            } else {
////                                intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
////                                startActivity(intent);
//                            }
                        }
                    }
                    break;
                case FirebaseMessagingService.ALIMI: //알리미 공지
                    GLog.i("FirebaseMessagingService.ALIMI", "dd");
                    intent = new Intent(MainActivity.this, AlramMainActivity.class);
                    intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
                    String push_idx = getIntent().getStringExtra("EVENT_POP");
                    if(push_idx !=null && !push_idx.equals(""))
                        intent.putExtra("EVENT_POP", push_idx);
                    startActivity(intent);
                    Util.BackAnimationStart(MainActivity.this);
                    break;
                case 8: //엄마 건강
                    GLog.i("8:엄마 건강", "dd");
                    if(CommonData.getInstance(MainActivity.this).getIamChild().compareTo("Y") == 0) {
                        mDialog = new CustomAlertDialog(MainActivity.this, CustomAlertDialog.TYPE_A);
                        mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                        mDialog.setContent(getString(R.string.i_am_child));
                        mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                        mDialog.show();

//                    }else{
//                        if (CommonData.getInstance(MainActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(MainActivity.this).getHpMjYnJun()) {
//                            intent = new Intent(MainActivity.this, MotherHealthMainActivity.class);
//                        } else {
//                            intent = new Intent(MainActivity.this, MotherHealthRegActivity.class);
//                        }
//                        Util.BackAnimationStart(MainActivity.this);
//                        startActivity(intent);
                    }

                    break;
//                case 9: //아이심리
//                    GLog.i("9:아이심리", "dd");
//                    intent = new Intent(MainActivity.this, PsyMainActivity.class);
//                    intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
//                    startActivity(intent);
//                    Util.BackAnimationStart(MainActivity.this);
//                    break;
//                case 10: //아이성장
//                    GLog.i("10:아이성장", "dd");
//                    intent = new Intent(MainActivity.this, GrowthMainActivity.class);
//                    intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
//                    startActivity(intent);
//                    Util.BackAnimationStart(MainActivity.this);
//                    break;
//                case 11: //심리음원
//                    GLog.i("11:심리음원", "dd");
//                    intent = new Intent(MainActivity.this, PsyMainActivity.class);
//                    intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
//                    startActivity(intent);
//                    Util.BackAnimationStart(MainActivity.this);
//                    break;
                //hsh end
                case FirebaseMessagingService.DIESEASE_PROGRAM: //유행성질환프로그램 신청
                    GLog.i("FirebaseMessagingService.DIESEASE_PROGRAM", "dd");
                    if(CommonData.getInstance(MainActivity.this).getMberGrad().equals("10")) {
                        intent = new Intent(MainActivity.this, RequestDiseaseProgramActivity.class);
                        intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
                        startActivity(intent);
                        Util.BackAnimationStart(MainActivity.this);
                    }
                    break;
//                case FirebaseMessagingService.COMMUNITY_COMMENT: //커뮤니티 댓글,좋아요
//                case FirebaseMessagingService.COUMMUNITY_MENTION:
//                case FirebaseMessagingService.COUMMUNITY_LIKE:
//                    GLog.i("FirebaseMessagingService.COMMUNITY", "dd");
//                    DummyActivity.startActivityForResult(this, CommunityNoticeFragment.REQUEST_CODE_NOTICE, CommunityNoticeFragment.class, null);
//                    Util.BackAnimationStart(MainActivity.this);
//                    break;
            }
        }

        getIntent().removeExtra(CommonData.EXTRA_PUSH_TYPE);
        getIntent().removeExtra(CommonData.EXTRA_INFO_SN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GLog.i("onStop", "dd");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgress();
        clearCache();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        } else {
//            super.onBackPressed();
            backPressCloseHandler.onBackPressed();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        GLog.i("onNewIntent", "dd");

        if ( CommonData.getInstance(MainActivity.this).getMemberId() == 0 ) {
            GLog.i("CommonData.getInstance(MainActivity.this).getMemberId() == 0", "dd");
            Intent introIntent = new Intent(getApplicationContext(), TemperActivity.class);
            startActivity(introIntent);
            finish();
        }

    }



}