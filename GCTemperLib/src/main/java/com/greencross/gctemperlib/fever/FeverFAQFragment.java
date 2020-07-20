package com.greencross.gctemperlib.fever;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.greencross.gctemperlib.base.BaseFragment;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.adapter.FeverFaqAdapter;
import com.greencross.gctemperlib.collection.FeverFaqItem;
import com.greencross.gctemperlib.common.CommonView;

import java.util.ArrayList;

/**
 * Created by jihoon on 2016-04-18.
 *
 * @since 0, 1
 */
public class FeverFAQFragment extends BaseFragment implements  TemperMainActivity.onKeyBackPressedListener, View.OnClickListener, View.OnFocusChangeListener {

    private View view;
    private ListView mRefreshListView;
    private ExpandableListView mListView;
    private FeverFaqAdapter mAdapter;
    private ArrayList<FeverFaqItem> mItem;

    // header view
    private View mHeaderView;
    private TextView mHeaderTitleTv, mHeaderEmptyTv;
    private LinearLayout mHeadBg;

    private EditText mSearchEdit;
    private ImageButton mSearchDelBtn, mSearchBtn;

    private String mSearchStr;    // 검색 문자열

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fever_faq_fragment, null);
        init(view);
        setEvent();

        // 헤더뷰
        addHeaderView();
        mListView.addHeaderView(mHeaderView);

        setListAdapter();
        return view;
    }

    /**
     * 객체 초기화
     * @param view view
     */
    public void init(View view){

        mRefreshListView   =       (ListView) view.findViewById(R.id.list_view);
//        mListView = mRefreshListView.getRefreshableView();
        mItem   =   new ArrayList<FeverFaqItem>();

        mSearchEdit         =       (EditText)      view.findViewById(R.id.search_edit);
        mSearchDelBtn       =       (ImageButton)   view.findViewById(R.id.search_del_btn);
        mSearchBtn          =       (ImageButton)   view.findViewById(R.id.search_btn);

        mSearchEdit.setOnFocusChangeListener(this);
        mSearchEdit.addTextChangedListener(new MyTextWatcher());

        mSearchDelBtn.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);

        String[] titles = getResources().getStringArray(R.array.fever_faq_title_list);
        String[] contents = getResources().getStringArray(R.array.fever_faq_contents_list);

        for(int i = 0 ; i < titles.length; i ++){
            FeverFaqItem item = new FeverFaqItem(Integer.toString(i), titles[i], contents[i]);
            mItem.add(item);
        }
    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){

        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                GLog.i("g Expand = " + groupPosition, "dd");

                int groupCount = mAdapter.getGroupCount();

                // 한 그룹을 클릭하면 나머지 그룹들은 닫힌다.
                for (int i = 0; i < groupCount; i++) {
                    if (!(i == groupPosition))
                        mListView.collapseGroup(i);
                }
            }
        });
    }

    /**
     * 리스트뷰 연결
     */
    public void setListAdapter(){

        mAdapter = new FeverFaqAdapter(getActivity(), mItem);
//        mRefreshListView.setAdapter(mAdapter);
//        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener((parent, view1, position, id) -> {
            //mListView.smoothScrollToPosition(position);
            int h = parent.getHeight();
            //mListView.smoothScrollToPositionFromTop(position, 20);
            int sum = 0;
            for(int i =0; i < position; i++){
                sum += mListView.getChildAt(i).getHeight();
            }
            mListView.scrollTo(0, h-sum);
        });
    }

    /**
     * 성장 FAQ headerView
     */
    public void addHeaderView(){

        mHeaderView     =   mLayoutInflater.inflate(R.layout.child_care_note_header_view, null, false);
        mHeaderTitleTv  =   (TextView)  mHeaderView.findViewById(R.id.title_tv);
        mHeaderEmptyTv  =   (TextView)  mHeaderView.findViewById(R.id.empty_tv);
        mHeadBg          =   (LinearLayout) mHeaderView.findViewById(R.id.head_bg);

        mHeadBg.setBackgroundResource(R.color.bg_yellow_light);
        mHeaderTitleTv.setText(getString(R.string.faq_desc_fever));
        mHeaderEmptyTv.setVisibility(View.GONE);

    }


    @Override
    public void onStart() {
        super.onStart();
        ((TemperMainActivity) getContext()).setOnKeyBackPressedListener(this);
    }

    @Override
    public void onBack() {
        TemperMainActivity activity = (TemperMainActivity)getActivity();
        activity.switchActionBarTheme(TemperMainActivity.THEME_ORANGE);
        switchFragment(new FeverMainFragment());
        activity.switchActionBarTitle(getString(R.string.title_fever));
    }

    public void search(String faq_kwrd){
        ArrayList<FeverFaqItem> tmpList = new ArrayList<FeverFaqItem>();
        String[] titles = getResources().getStringArray(R.array.fever_faq_title_list);
        String[] contents = getResources().getStringArray(R.array.fever_faq_contents_list);
        for (int i = 0; i < titles.length; i++){
            if( titles[i].replace(" ", "").indexOf(faq_kwrd.replace(" ", "")) > -1 || contents[i].replace(" ", "").indexOf(faq_kwrd.replace(" ", "")) > -1){
                FeverFaqItem tmpItem = new FeverFaqItem(Integer.toString(i), titles[i], contents[i]);
                tmpList.add(tmpItem);
            }
        }
        if(tmpList.size() > 0){
            mHeaderTitleTv.setText(String.format(getString(R.string.child_care_note_search_desc), mSearchEdit.getText().toString()));
            mItem.clear();
            mItem = tmpList;
        }else {
            mHeaderTitleTv.setText(getString(R.string.faq_search_empty));
        }
        setListAdapter();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        int id = v.getId();
        if (id == R.id.search_del_btn) {   // 검색 삭제
            GLog.i("search_del_btn", "dd");
            CommonView.getInstance().setClearEditText(mSearchEdit);
        } else if (id == R.id.search_btn) {   // 검색
            mSearchStr = mSearchEdit.getText().toString().trim();
            if (!mSearchStr.equals("")) {  // 검색 내용이 있다면
                Util.hideKeyboard(getActivity(), mSearchEdit);
                search(mSearchStr);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        if (id == R.id.search_edit) {    // 검색
            CommonView.getInstance().setClearImageBt(mSearchDelBtn, hasFocus);
        }
    }

    /**
     * 입력창 리스너
     */
    class MyTextWatcher implements TextWatcher
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

            GLog.i("s = " +s.toString() +"\n start = " +start +"\n before = " +before +"\n count = " +count, "dd");

            if(mSearchEdit.getText().toString().length() == 0){  // 검색어 입력 후, 텍스트 삭제시 기본 화면으로 원복
                String[] titles = getResources().getStringArray(R.array.fever_faq_title_list);
                String[] contents = getResources().getStringArray(R.array.fever_faq_contents_list);

                for(int i = 0 ; i < titles.length; i ++){
                    FeverFaqItem item = new FeverFaqItem(Integer.toString(i), titles[i], contents[i]);
                    mItem.add(item);
                }
                mHeaderTitleTv.setText(getString(R.string.faq_desc_fever));
                setListAdapter();
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void afterTextChanged(Editable s)
        {
        }
    }
}
