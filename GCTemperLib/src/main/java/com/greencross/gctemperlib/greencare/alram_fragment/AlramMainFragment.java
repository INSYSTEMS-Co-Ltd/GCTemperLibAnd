package com.greencross.gctemperlib.greencare.alram_fragment;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_asstb_kbtg_alimi;
import com.greencross.gctemperlib.Alram.AlramMainActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.greencare.base.CommonActionBar;
import com.greencross.gctemperlib.greencare.base.IBaseFragment;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.util.Util;

import java.util.ArrayList;
import java.util.List;

public class AlramMainFragment extends BaseFragment implements IBaseFragment {
    private final String TAG = AlramMainActivity.class.getSimpleName();

    List<Tr_asstb_kbtg_alimi.chlmReadern> items;

    private RecyclerView recyclerView;
    private MyAdapter Adapter;
    private LinearLayoutManager layoutManager;

    private int mPageNum = 0;
    private int mMaxPage = 0;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 10;
    private int firstVisibleItem=0, visibleItemCount=0, totalItemCount=0;

    private int mOldPos = 0;

    private AlramMainActivity activity;

    private String EVENT_POP = "";

    public class NotifierItem {
        String notiTitle;
        String notiContent;
        String notiDate;

        public String getNotiTitle() {
            return notiTitle;
        }
        public String getNotiContent() {
            return notiContent;
        }
        public String getNotiDate() {
            return notiDate;
        }

        public NotifierItem(String notiTitle, String notiContent, String notiDate){
            this.notiTitle = notiTitle;
            this.notiContent = notiContent;
            this.notiDate = notiDate;
        }
    }

    public static BaseFragment newInstance() {
        AlramMainFragment fragment = new AlramMainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifier_fragment, container, false);

        Util.setSharedPreference(getContext(), "new_check", "0");
        Logger.i(TAG,"newCheck:"+Util.getSharedPreference(getContext(),"new_check"));

        activity = (AlramMainActivity) getActivity();

        if(getArguments() != null) {
            EVENT_POP = getArguments().getString("EVENT_POP");
        }


        recyclerView = view.findViewById(R.id.noti_list);
        recyclerView.setHasFixedSize(true);

        items = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if(loading) {
                    if(totalItemCount > previousTotal) {
                        previousTotal = totalItemCount;
                        Log.i(TAG, "recycleView end called " + loading + " " + visibleItemCount+"/" + totalItemCount+"/" + firstVisibleItem);
                        loading = false;
                    }
                }
                if(!loading && totalItemCount <= firstVisibleItem + visibleItemCount){
                    Log.i(TAG, "recycleView end called " + loading + " " + visibleItemCount+"/" + totalItemCount+"/" + firstVisibleItem);


                    setNotifierData();
                    loading = true;
                }

                Log.i(TAG, "recycleView end called test " + visibleItemCount+"/" + totalItemCount+"/" + firstVisibleItem);
            }
        });

        if(mOldPos == 0){
            Adapter = new MyAdapter();

            mPageNum = 0;

            setNotifierData();

        }


        recyclerView.setAdapter(Adapter);

        recyclerView.scrollToPosition(mOldPos);
        Log.i(TAG, "NotifierFragment Test : " + mOldPos);


        return view;
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
//        getToolBar().setVisibility(View.GONE);
        actionBar.setWhiteTheme();
        actionBar.setActionBarTitle( getString(R.string.text_weight_input));
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public  class MyAdapter extends RecyclerView.Adapter{

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView notiTitleImage;
            TextView notiTitle;
            TextView notiContent;
            TextView notiDate;

            MyViewHolder(View view){
                super(view);
                notiTitleImage = view.findViewById(R.id.noti_title_img);
                notiTitle = view.findViewById(R.id.noti_title);
                notiContent = view.findViewById(R.id.noti_content);
                notiDate = view.findViewById(R.id.noti_date);
            }
        }

        private List<Tr_asstb_kbtg_alimi.chlmReadern> itemslist = new ArrayList<>();
        //        }
        public void setData(List<Tr_asstb_kbtg_alimi.chlmReadern> dataList){
            itemslist.addAll(dataList);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifier_list_item, parent, false);
            MyViewHolder holder = new MyViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final int Position = position;

            MyViewHolder myViewHolder = (MyViewHolder) holder;

            final String titleImgUrl = itemslist.get(position).ka_timg;
            final String title = itemslist.get(position).kbt;
            final String subtitle = itemslist.get(position).sub_tit;
            final String date = CDateUtil.getFormatYYYYMMDD(itemslist.get(position).kbvd);

            Logger.i(TAG,"titleImgUrl: "+ Uri.parse(titleImgUrl));

            if(!titleImgUrl.isEmpty())
//                Glide.with(getContext()).load(titleImgUrl)
//                        .apply(new RequestOptions()
//                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .skipMemoryCache(true)).into(myViewHolder.notiTitleImage);

            myViewHolder.notiTitle.setText(title);
            myViewHolder.notiContent.setText(subtitle);
            myViewHolder.notiDate.setText(date);

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                        Bundle bundle = new Bundle();
                        bundle.putString("IDX",itemslist.get(Position).kbta_idx);
                        activity.replaceFragment(new AlramContentFragment(),false, true, bundle);

                }
            });
        }

        @Override
        public int getItemCount() {
            return itemslist.size();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //알리미 리스트
    public void setNotifierData() {
        Tr_asstb_kbtg_alimi.RequestData requestData = new Tr_asstb_kbtg_alimi.RequestData();
        CommonData login = CommonData.getInstance(activity);
        requestData.PLN = "10";

        if(mPageNum == 0 || mMaxPage == StringUtil.getIntVal(requestData.PLN))
            requestData.PAGE = "" + (++mPageNum);
        else
            return;


        requestData.mber_sn = login.getMberSn();

        getData(getContext(), Tr_asstb_kbtg_alimi.class, requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_asstb_kbtg_alimi) {
                    Tr_asstb_kbtg_alimi data = (Tr_asstb_kbtg_alimi) obj;
                    if(data.data_yn.equals("Y")) {
                        Logger.i(TAG, "MSG : " + data.DATA_LENGTH);

                        if(!EVENT_POP.equals("")){
                            Bundle bundle = new Bundle();
                            bundle.putString("IDX",EVENT_POP);
                            activity.replaceFragment(new AlramContentFragment(),true, true, bundle);
                        } else {
                            mMaxPage = data.dataList.size();
                            Adapter.setData(data.dataList);
                            Adapter.notifyDataSetChanged();
                        }
                    }else{
                        Logger.i(TAG,"KA001 : 기타오류");
                    }

                }
            }
        }, null);
    }


    private String getAppname(String PK){
        String name ="";
        try {
            name = (String) getContext().getPackageManager().getApplicationLabel(getContext().getPackageManager().getApplicationInfo(PK, PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return name;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
