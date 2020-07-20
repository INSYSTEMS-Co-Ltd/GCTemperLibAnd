package com.greencross.gctemperlib.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.adapter.YoutubeListAdapter;
import com.greencross.gctemperlib.collection.YoutubeItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.MakeProgress;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.network.RequestApi;
import com.greencross.gctemperlib.push.FirebaseMessagingService;
import com.greencross.gctemperlib.util.GLog;
// import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class YoutubeActivity extends AppCompatActivity {

    YoutubeListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mYoutubeListLay;
    TextView mTxtNullData;

    ImageButton mCommonLeftBtn;

    ArrayList<YoutubeItem> mYoutubeList;

    int mPageNum;
    int mMaxPageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        initView();
        init();
        initEvent();

        requestYoutubeListApi(mPageNum);
    }



    protected void initView(){
        mCommonLeftBtn = (ImageButton)findViewById(R.id.common_left_btn);
        mYoutubeListLay = (RecyclerView)findViewById(R.id.youtube_list_lay);
        mYoutubeListLay.setHasFixedSize(true);
        mTxtNullData = (TextView)findViewById(R.id.txt_null_data);
    }

    protected void initEvent(){
        mCommonLeftBtn.setOnClickListener(v -> finish());
    }

    protected void init(){
        mPageNum = 1;
        mYoutubeList = new ArrayList<YoutubeItem>();

        mLayoutManager = new LinearLayoutManager(YoutubeActivity.this, LinearLayoutManager.VERTICAL, false);
        mYoutubeListLay.setLayoutManager(mLayoutManager);
        mAdapter = new YoutubeListAdapter(YoutubeActivity.this, mYoutubeList, mYoutubeListLay);
        mYoutubeListLay.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(() -> {

            if(mYoutubeList.size() < mMaxPageNum){
                mPageNum++;
                requestYoutubeListApi(mPageNum);
            }
        });
    }

    /**
     * 동영상 게시판 목록
     */
    public void requestYoutubeListApi(int pageNum) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_CONTENT_MOVIE_BBSLIST);    //  api 코드명
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);          //  insures 코드
            object.put(CommonData.JSON_MBER_SN,  CommonData.getInstance(YoutubeActivity.this).getMberSn());             //  회원고유값
            object.put(CommonData.JSON_PAGENUMBER, ""+pageNum);               //  페이지 번호

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(YoutubeActivity.this, NetworkConst.NET_GET_YOUTUBE_LIST, NetworkConst.getInstance().getDefDomain(), networkListener, params,new MakeProgress(this));
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub

            switch ( type ) {
                case NetworkConst.NET_GET_YOUTUBE_LIST:	// 유튜브 리스트 가져오기
                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_YOUTUBE_LIST", "dd");

                            try {
                                mMaxPageNum = resultData.getInt(CommonData.JSON_MAXPAGENUMBER);
                                if(mMaxPageNum > 0){
                                    int pageNum = resultData.getInt(CommonData.JSON_PAGENUMBER);
                                    JSONArray youtubeList = resultData.getJSONArray(CommonData.JSON_BBSLIST);

                                    if(youtubeList.length() > 0){
                                        if(pageNum == 1)
                                            mYoutubeList.clear();

                                        for (int i= 0; i < youtubeList.length(); i++){
                                            JSONObject bbsListJSONObject = youtubeList.getJSONObject(i);
                                            YoutubeItem newItem = new YoutubeItem(bbsListJSONObject.getString(CommonData.JSON_INFO_SUBJECT),
                                                    bbsListJSONObject.getString(CommonData.JSON_INFO_TITLE_URL), bbsListJSONObject.getString(CommonData.JSON_VIEW_DAY));

                                            mYoutubeList.add(newItem);
                                            mAdapter.notifyItemInserted(mYoutubeList.size());
                                        }
                                        mAdapter.setLoaded();

                                        mAdapter.notifyDataSetChanged();
                                        mTxtNullData.setVisibility(View.GONE);
                                        mYoutubeListLay.setVisibility(View.VISIBLE);
                                        //hsh start
                                        showPushLoad();
                                        //hsh end
                                    }
                                }else {
                                    mYoutubeList.clear();
                                    mAdapter.notifyDataSetChanged();
                                    mTxtNullData.setVisibility(View.VISIBLE);
                                    mYoutubeListLay.setVisibility(View.GONE);
                                }
                            }catch(Exception e){
                                GLog.e(e.toString());
                            }

                            break;

                        default:
                            GLog.i("JOIN FAIL", "dd");

                            break;
                    }
                    break;
            }
        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
            dialog.show();

        }
    };

    @Override protected void attachBaseContext(Context newBase) {
        // super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
        super.attachBaseContext(newBase);
    }

    //hsh start
    private void showPushLoad() {
        Intent i = getIntent();
        int push_type = i.getIntExtra(CommonData.EXTRA_PUSH_TYPE, 0);
        if(push_type!=0 && push_type == FirebaseMessagingService.FEVER_MOVIE){
            String info_sn = i.getStringExtra(CommonData.EXTRA_INFO_SN);

            i = new Intent(Intent.ACTION_VIEW, Uri.parse(info_sn));
            startActivity(i);
        }
        getIntent().removeExtra(CommonData.EXTRA_PUSH_TYPE);
        getIntent().removeExtra(CommonData.EXTRA_INFO_SN);
    }
    //hsh end
}
