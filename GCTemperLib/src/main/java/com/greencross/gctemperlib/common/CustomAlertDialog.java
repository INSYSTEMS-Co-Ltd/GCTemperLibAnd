package com.greencross.gctemperlib.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.R;


/**
 * Created by jihoon on 2016-03-21.
 * 커스텀 팝업 다이얼로그
 * @since 0, 1
 */
public class CustomAlertDialog extends Dialog {

    public final static int TYPE_A	= 1;    //  버튼1개 팝업
    public final static int TYPE_B	= 2;    // 버튼 2개 팝업
    public final static int TYPE_C	= 3;    // 버튼 2개 팝업
    public final static int TYPE_D	= 4;    // 버튼 1개 선택팝업
    public final static int TYPE_E	= 5;    // 버튼 2개 선택팝업
    public final static int TYPE_F	= 6;    // 버튼 2개 팝업

    public static final int TYPE_RATING =   10; // 별점평가
    public static final int TYPE_PHOTO = 11;    // 사진 업로드

    public final static int TYPE_EDIT_PROFILE = 94;
    public final static int TYPE_HOME_DATE = 95;


    public final static int TYPE_NEW_1 = 101;
    private String tempHid;
    private String tempAid;
    private boolean mIsAutoDismiss = true;


    public CustomAlertDialogInterface.OnClickListener positiveButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener negativeButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener thirdButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener fourthButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener fifthButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener sixthButtonClickListener = null;
    public CustomAlertDialogInterface.OnCheckedChangeListener checkedChangeListner = null;
    public CustomAlertDialogInterface.OnRatingBarChangeListener ratingChangeListener = null;

    public CustomAlertDialogInterface.OnImgClickListener imgCheckedChangeListner = null;
    public CustomAlertDialogInterface.OnImgClickListener imgMotherCheckedChangeListner = null;

    public int mType = 1;

    private int id = -1;
    private int memberId = -1;
    private String mToast;

    private String mCoffeeItemCode;
    private String mBeanType;

    RelativeLayout mLayout;
    TextView mContentTv;
    Button button;
    Button negaButton;
    CheckBox checkbox;
    Context mContext;
    EditText mContextEt;
    ImageView mImgCheckBoc;
    LinearLayout mImgCheckBocLv;
    boolean isChecked = false;
    boolean isMotherChecked = false;

    RelativeLayout mPhotoLayout = null;
    ImageView ContentImageView = null;
    ImageView ItemImageView = null;
    TextView mVoiceContentView = null;

    ImageView mPhotoBlur = null;
    ImageView mPhotoBlurWho = null;
    Bitmap mPhotoBlurBmp = null;
    private LinearLayout mCall1,mCall2;
    private TextView mHompageId, mAppId;
    private TextView mCheck_Box_big_mother;


    private boolean mOutTouchCancel = false;
    private boolean mWIndowFocusChange = false; // 윈도우 포커스 호출유무

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GLog.i("onCreate()", "dd");

        WindowManager.LayoutParams lpWindow = getWindow().getAttributes();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        mLayout = (RelativeLayout) getWindow().findViewById(R.id.dialog_layout);

        setCanceledOnTouchOutside(true);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        mOutTouchCancel = cancel;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (mLayout == null || button == null)
            return;

        switch ( mType ) {
            case TYPE_A :
                GLog.i("Type_a mLayout = " +mLayout.getWidth(), "dd");
                GLog.i("Type_a button = " + button.getWidth(), "dd");
                if(!mWIndowFocusChange) {
                    button.setWidth(mLayout.getWidth());
                }
                GLog.i("Type_a mLayout = " +mLayout.getWidth(), "dd");
                GLog.i("Type_a button = " + button.getWidth(), "dd");
                break;
            case TYPE_B :
            case TYPE_C :
            case TYPE_E :
            case TYPE_F :
                if(!mWIndowFocusChange) {
                    int maxWidth = mLayout.getWidth();
                    button.setWidth((int) maxWidth / 2);
                    negaButton.setWidth((int) maxWidth / 2);

                    GLog.i("maxWidth = " + maxWidth, "dd");
                    GLog.i("maxWidth /2 = " + (int) maxWidth / 2, "dd");
                }
                break;
            case TYPE_D :
                GLog.i("Type_a mLayout = " +mLayout.getWidth(), "dd");
                GLog.i("Type_a button = " + button.getWidth(), "dd");
                if(!mWIndowFocusChange) {
                    button.setWidth(mLayout.getWidth());
                }
                GLog.i("Type_a mLayout = " +mLayout.getWidth(), "dd");
                GLog.i("Type_a button = " + button.getWidth(), "dd");
                break;

        }

        mWIndowFocusChange = true;

    }

    /**
     * 다이얼로그 외 영억 터치시 다이얼로그를 닫는다
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        int action = event.getAction();

        switch ( action ) {

            case MotionEvent.ACTION_UP :

                float touchX = event.getX();
                float touchY = event.getY();

                float layoutX = mLayout.getLeft();
                float layoutY = mLayout.getTop();

                float layoutWidth 	= mLayout.getWidth();
                float layoutHeight 	= mLayout.getHeight();

                if ( !(touchX >= layoutX && touchX <= layoutX+layoutWidth
                        && touchY >= layoutY && touchY <= layoutY+layoutHeight)
                        ) {
                    if ( mOutTouchCancel ) {
                        try{
                            // popup type TYPE_A or TYPE_NEW_1 은 onclick 처리
                            if(mType == TYPE_A || mType == TYPE_NEW_1 || mType == TYPE_D){	// 버튼 1개 dialog는 클릭 처리
                                GLog.i("onBackPressed() Type_A ", "dd");
                                button.performClick();
                            }else if(mType == TYPE_HOME_DATE){			// 홈에서 데이트 신청 응답 팝업도 클릭 처리
                                GLog.i("onBackPressed() TYPE_HOME_DATE ", "dd");
                                negaButton.performClick();
                            }else{
                                GLog.v("close window");
                                dismiss();
                            }
                        }catch(Exception e){
                            GLog.e(e.toString());
                            dismiss();
                        }
                    }
                }

                break;

        }
        //GLog.v("Layout getX : " + mLayout.getX() + "Layout getY : " + mLayout.getY());
        //GLog.v("touche getX : " + event.getX() + " touch getY : " + event.getY());

        return super.onTouchEvent(event);


    }

    /**
     * 뒤로가기 단, 버튼이 1개인 다이얼로그는 button click으로 인지한다.
     */
    @Override
    public void onBackPressed() {
        if(mType == TYPE_A || mType == TYPE_NEW_1 || mType == TYPE_D){
            GLog.i("onBackPressed() Type_A ", "dd");
            if (button != null)
                button.performClick();
            else
                dismiss();
        }else if(mType == TYPE_HOME_DATE){
            GLog.i("onBackPressed() TYPE_HOME_DATE ", "dd");
            negaButton.performClick();
        }else{
            super.onBackPressed();
        }
    }

    /**
     * 커스텀 팝업 다이얼로그
     * @param context   context
     * @param type  타입 ( 1 ~ 3 )
     */
    public CustomAlertDialog(Context context, int type) {
        super(context, R.style.popup_custom_theme);
        mContext = context;
        setSelectView(type);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setToastMsg(String msg){
        this.mToast = msg;
    }

    public String getToastMsg(){
        return this.mToast;
    }

    public void setMemberId(int memberId){
        this.memberId = memberId;
    }

    public int getMemberId(){
        return this.memberId;
    }

    public Button getPosiBtn(){
        return button;
    }

    /**
     * 버틴 클릭 기본 설정
     */
    public View.OnClickListener clickListener = v -> {
        // TODO Auto-generated method stub
        dismiss();
    };


    public void setSelectView(int type) {

        mType = type;

        switch ( type ) {
            case TYPE_A :   // 버튼 1개
                setContentView(R.layout.popup_dialog_a_type);
                break;
            case TYPE_B :   // 버튼 2개
                setContentView(R.layout.popup_dialog_b_type);
                break;
            case TYPE_C:   // 다신 안보기 체크 박스
                setContentView(R.layout.popup_dialog_c_type);
                break;
            case TYPE_D :   // 버튼 1개
                setContentView(R.layout.popup_dialog_f_type);
                break;
            case TYPE_E : //버튼 2개 선택 팝업
                setContentView(R.layout.popup_dialog_g_type);
                break;
            case TYPE_F :
                setContentView(R.layout.popup_dialog_h_type);
                break;
             /* 2016-03-21 주석
            case TYPE_RATING:   // 별점평가
                setContentView(R.layout.popup_dialog_rating_type);
                break;
            */
        }
//        setFont();

    }

    /**
     * 별점평가 버튼 활성 유무 설정
     * @param bool ( true - 활성화 , false - 비활성홯 )
     */
    public void setButtonColor(boolean bool){
        // 활성화
        if(bool){
            button.setBackgroundResource(R.drawable.btn_right_xml);
            button.setTextColor(ContextCompat.getColorStateList(mContext, R.color.color_ffffff_btn));
            button.setEnabled(true);
        }
        // 비활성화
        else{
            button.setBackgroundResource(R.drawable.background_2_5_rightbottom_50f39c89);
            button.setTextColor(ContextCompat.getColor(mContext, R.color.color_50ffffff));
            button.setEnabled(false);
        }

    }



    /*
     * (non-Javadoc)
     * @see android.app.Dialog#setTitle(java.lang.CharSequence)
     */
    @Override
    public void setTitle(CharSequence title) {
        // TODO Auto-generated method stub
        TextView titleTextView = null;
//        switch ( mType ) {
//            case TYPE_A :
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//            case TYPE_B :
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//            case TYPE_SEARCH_PASSWORD:
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//            case TYPE_PHOTO:
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//            case TYPE_STARMARK:
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//
//        }

        titleTextView = (TextView) findViewById(R.id.dialog_title);
        titleTextView.setVisibility(View.VISIBLE);
        if ( titleTextView != null ){
            titleTextView.setText(title);
        }
    }

    /*
     * (non-Javadoc)
     * @see android.app.Dialog#setTitle(int)
     */
    @Override
    public void setTitle(int titleId) {
        // TODO Auto-generated method stu
        this.setTitle(titleId);
        String title = getContext().getResources().getString(titleId);
        setTitle(title);
    }

    /**
     * 팝업 내용
     * @param content   내용
     */
    public void setContent(CharSequence content) {
        // TODO Auto-generated method stub
        mContentTv = null;
//        switch ( mType ) {
//            case TYPE_A :
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//            case TYPE_B :
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//            case TYPE_SEARCH_PASSWORD:
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//            case TYPE_STARMARK:
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//
//        }

        mContentTv = (TextView) findViewById(R.id.dialog_message_textview);

        if ( mContentTv != null ){
            mContentTv.setText(content);
            setToastMsg(String.valueOf(content));
        }
    }


    /**
     * 팝업 내용
     * @param content   내용
     */
    public void setHtmlContent(String content) {
        // TODO Auto-generated method stub
        mContentTv = null;
//        switch ( mType ) {
//            case TYPE_A :
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//            case TYPE_B :
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//            case TYPE_SEARCH_PASSWORD:
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//            case TYPE_STARMARK:
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//
//        }

        mContentTv = (TextView) findViewById(R.id.dialog_message_textview);

        if ( mContentTv != null ){
            Spanned htmlText ;
            if (Build.VERSION.SDK_INT >= 24) {
                htmlText = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
            }else{
                htmlText = Html.fromHtml(content);

            }
            mContentTv.setText(htmlText);
            setToastMsg(String.valueOf(content));
        }
    }


    /**
     * 팝업 콜뷰
     *
     */
    public void setCall(View.OnClickListener okListener,View.OnClickListener noListener) {
        // TODO Auto-generated method stub
        mCall1 = findViewById(R.id.dialog_call1);
        mCall2 = findViewById(R.id.dialog_call2);
        View view = findViewById(R.id.dialog_layout);

        //click 저장
        com.greencross.gctemperlib.greencare.component.OnClickListener mClickListener = new com.greencross.gctemperlib.greencare.component.OnClickListener(null,view, getContext());

        //홈
        mCall1.setOnTouchListener(mClickListener);
        mCall2.setOnTouchListener(mClickListener);

        //코드 부여(홈)

        mCall1.setContentDescription(getContext().getString(R.string.Call1));
        mCall2.setContentDescription(getContext().getString(R.string.Call2));

        setButton(okListener,noListener);

    }

    /**
     * 팝업 id
     *
     */
    public void setSelectId(String homepageId, String AppId, String text, CustomAlertDialogInterface.OnClickListener listener) {
        // TODO Auto-generated method stub
        button = null;
        mHompageId = findViewById(R.id.homepage_id);
        mAppId = findViewById(R.id.app_id);
        button = (Button) findViewById(R.id.confirm_btn);

        this.positiveButtonClickListener = listener;

        if ( text != null )	button.setText(text);


        if ( button != null && positiveButtonClickListener != null) {

            button.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                positiveButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

            });
        }
        else {
            button.setOnClickListener(clickListener);
        }


        mHompageId.setText(homepageId);
        mAppId.setText(AppId);

        button.setEnabled(false);
        button.setBackgroundResource(R.drawable.background_5_e6e7e8_right);
        button.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.color_ffffff_btn));

        mHompageId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHompageId.setSelected(true);
                mAppId.setSelected(false);
                setHomepageId("Y");
                setAppId("N");

                button.setEnabled(true);
                button.setBackgroundResource(R.drawable.btn_right_blue);
                button.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.color_f8f5f0_btn));
            }
        });

        mAppId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHompageId.setSelected(false);
                mAppId.setSelected(true);
                setHomepageId("N");
                setAppId("Y");

                button.setEnabled(true);
                button.setBackgroundResource(R.drawable.btn_right_blue);
                button.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.color_f8f5f0_btn));
            }
        });


    }


    public void setHomepageId(String id){       tempHid = id;}
    public String getHomepageId(){      return tempHid;}

    public void setAppId(String id){       tempAid = id;}
    public String getAppId(){      return tempAid;}







    /**
     * 오른쪽 버튼 세팅
     *
     * @param okClickListener
     */
    public void setButton(View.OnClickListener okClickListener,View.OnClickListener noClickListener) {
        setAlertButtonClickListener(mCall1, okClickListener);
        setAlertNoButtonClickListener(mCall2,noClickListener);
    }

    private void setAlertButtonClickListener(LinearLayout button, final View.OnClickListener clickListener) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v);

                    if (mIsAutoDismiss) {
                        CustomAlertDialog.this.dismiss();
                    }
                } else {
                    CustomAlertDialog.this.dismiss();
                }
            }
        });
    }

    private void setAlertNoButtonClickListener(LinearLayout button, final View.OnClickListener clickListener) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v);

                    if (mIsAutoDismiss) {
                        CustomAlertDialog.this.dismiss();
                    }
                } else {
                    CustomAlertDialog.this.dismiss();
                }
            }
        });
    }


    /**
     *  Type_C Content가 직접 입력하는 EditText가 추가될 때.
     */
    public void makeContent(){
//        mContextEt = (EditText) findViewById(R.id.dialog_econtent);
    }

    public String getmakeContent(){
        if(mContextEt != null){
            return mContextEt.getText().toString();
        }else{
            return null;
        }

    }

    /**
     * 확인 버튼 설정 ( 버튼이 2개일 경우 오른쪽 )
     * @param text 버튼명
     * @param listener button click event
     */
    public void setPositiveButton(CharSequence text, CustomAlertDialogInterface.OnClickListener listener) {

        this.positiveButtonClickListener = listener;

        button = null;

        switch ( mType ) {
            case TYPE_PHOTO:
//                button = (Button) findViewById(R.id.gallery_btn);
                if ( text != null )	button.setText(text);
                break;
            case TYPE_E:
                break;
            default:
                button = (Button) findViewById(R.id.confirm_btn);
                if (button != null && text != null )	button.setText(text);
                break;
        }


        if ( button != null && positiveButtonClickListener != null) {

            button.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                positiveButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

    /**
     * 취소 버튼 설정 ( 버튼이 2개일 경우 왼쪽 )
     * @param text 버튼명
     * @param listener button click event
     */
    public void setNegativeButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.negativeButtonClickListener = listener;

        negaButton = null;

//        switch ( mType ) {
//            case TYPE_A :
//                break;
//            case TYPE_B :
//                negaButton = (Button) findViewById(R.id.cancel_btn);
////				/* 2015-01-14 네비바 팝업 -> X 팝업 스타일 개선
//				if ( text != null )	negaButton.setText(text);
////				*/
//                break;
//            case TYPE_SEARCH_PASSWORD:
//                negaButton = (Button) findViewById(R.id.cancel_btn);
//                if ( text != null )	negaButton.setText(text);
//                break;
//            case TYPE_PHOTO:
//                negaButton = (Button) findViewById(R.id.cancel_btn);
//                if ( text != null )	negaButton.setText(text);
//                break;
//            case TYPE_STARMARK:
//                negaButton = (Button) findViewById(R.id.cancel_btn);
//                if ( text != null )	negaButton.setText(text);
//                break;
//
//        }


        negaButton = (Button) findViewById(R.id.cancel_btn);


        if ( negaButton != null && negativeButtonClickListener != null) {

            negaButton.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                negativeButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

            });
        }
        else {
            negaButton.setOnClickListener(clickListener);
        }

    }

    public void setNegativeButtonVisible(Boolean visible){
        negaButton = null;
        negaButton = (Button) findViewById(R.id.cancel_btn);
        if(visible)
            negaButton.setVisibility(View.VISIBLE);
        else
            negaButton.setVisibility(View.GONE);
    }


    /**
     * 체크 박스 설정 ( 버튼이 2개일 경우 오른쪽 )
     * @param listener button click event
     */
    public void setCheckboxButton(CustomAlertDialogInterface.OnImgClickListener listener) {

        this.imgCheckedChangeListner = listener;

        mImgCheckBoc = null;

        mImgCheckBoc = (ImageView)findViewById(R.id.check_box);

        if ( mImgCheckBoc != null && imgCheckedChangeListner != null) {

            mImgCheckBoc.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                imgCheckedChangeListner.onClick(CustomAlertDialog.this, v);
            });
        }
        else {
            mImgCheckBoc.setOnClickListener(clickListener);
        }


    }


    /**
     * 체크 박스 설정 ( 버튼이 2개일 경우 오른쪽 )
     * @param listener button click event
     */
    public void setCheckboxButtonLv(CustomAlertDialogInterface.OnImgClickListener listener) {

        this.imgMotherCheckedChangeListner = listener;

        mImgCheckBocLv = null;

        mImgCheckBocLv = (LinearLayout) findViewById(R.id.mother_check_box);

        if ( mImgCheckBocLv != null && imgMotherCheckedChangeListner != null) {

            mImgCheckBocLv.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                imgMotherCheckedChangeListner.onClick(CustomAlertDialog.this, v);
            });
        }
        else {
            mImgCheckBocLv.setOnClickListener(clickListener);
        }


    }


    public void setChangeCheckboxImg(){
        if(isChecked){
            isChecked = false;
            mImgCheckBoc.setImageResource(R.drawable.btn_checkbox_black);
        }else {
            isChecked = true;
            mImgCheckBoc.setImageResource(R.drawable.btn_checkbox_black_sel);
        }
    }

    public void setChangeCheckboxImg_motherBig(){
        mCheck_Box_big_mother = findViewById(R.id.check_box1);
        if(isMotherChecked){
            isMotherChecked = false;
            mCheck_Box_big_mother.setSelected(false);
        }else {
            isMotherChecked = true;
            mCheck_Box_big_mother.setSelected(true);
        }
    }

    /**
     * 프로필 정보 수정 버튼 설정
     * @param text 버튼명
     * @param listener button click event
     */
    public void setThirdButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.thirdButtonClickListener = listener;

        Button button = null;

        /* 2016-01-04 작업 후 주석제거
        switch ( mType ) {
            case TYPE_A :
                break;
            case TYPE_B :
                break;
            case TYPE_PHOTO:
                button = (Button) findViewById(R.id.camera_btn);
                if ( text != null )	button.setText(text);
                break;
            case TYPE_FAVORITE:
                button = (Button) findViewById(R.id.third_btn);
                if ( text != null )	button.setText(text);
                break;

        }
        */

        if ( button != null && thirdButtonClickListener != null) {

            button.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                thirdButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

    /**
     * 다음에 하기 버튼 설정
     * @param text 버튼명
     * @param listener button click event
     */
    public void setFourthButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.fourthButtonClickListener = listener;

        Button button = null;

        switch ( mType ) {
            case TYPE_A :
                break;
            case TYPE_B :
                break;
            case TYPE_C :
                break;
        }

        if ( button != null && fourthButtonClickListener != null ) {

            button.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                fourthButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isMotherChecked() {
        return isMotherChecked;
    }

    public void setMotherChecked(boolean checked) {
        isMotherChecked = checked;
    }

    /**
     * 다음에 하기 버튼 설정
     * @param text 버튼명
     * @param listener button click event
     */
    public void setFifthButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.fifthButtonClickListener = listener;

        Button button = null;

        switch ( mType ) {
            case TYPE_A :
                break;
            case TYPE_B :
                break;
            case TYPE_C :
                break;
        }

        if ( button != null && fifthButtonClickListener != null ) {

            button.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                fifthButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

    /**
     * 여섯번째 버튼
     * @param text 버튼명
     * @param listener button click event
     */
    public void setSixthButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.sixthButtonClickListener = listener;

        Button button = null;

        switch ( mType ) {
            case TYPE_EDIT_PROFILE:	// 키워드 수정

                break;
        }

        if ( button != null && sixthButtonClickListener != null ) {

            button.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                sixthButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

    /**
     * 별점평가 리스너
     * @param listener
     */
    public void setRatingChange(CustomAlertDialogInterface.OnRatingBarChangeListener listener){
        this.ratingChangeListener = listener;

        RatingBar ratingbar = null;

        switch( mType ) {
            case TYPE_RATING:   // 별점평가
//                ratingbar   =   (RatingBar) findViewById(R.id.popup_star_rating_bar);
                break;
        }

        if( ratingbar != null && ratingChangeListener != null){
            ratingbar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingChangeListener.onRatingChanged(CustomAlertDialog.this, ratingBar, rating, fromUser));
        }

    }

}
