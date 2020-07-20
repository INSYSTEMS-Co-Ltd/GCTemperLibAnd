package com.greencross.gctemperlib.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.fever.FeverHxActivity;
import com.greencross.gctemperlib.fever.FeverInputActivity;
import com.greencross.gctemperlib.fever.FeverResultActivity;
import com.greencross.gctemperlib.fever.MemoInputActivity;
import com.greencross.gctemperlib.fever.RemedyInputActivity;
import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.AllDataItem;
import com.greencross.gctemperlib.network.RequestApi;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by MobileDoctor on 2017-02-27.
 */

public class FeverHistoryListAdapter extends RecyclerSwipeAdapter<FeverHistoryListAdapter.FeverAllDataListItemViewHolder> {

    private int itemLayout;
    private Context mContext;
    ArrayList<AllDataItem> allDataList;
    CustomAlertDialog mDialog;


    public FeverHistoryListAdapter(Context _context, ArrayList<AllDataItem> _allDataList, int _itemLayout){
        mContext = _context;
        allDataList = _allDataList;
        itemLayout = _itemLayout;
    }

    @Override
    public FeverAllDataListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new FeverAllDataListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FeverAllDataListItemViewHolder holder, final int position) {

        final AllDataItem curItem = allDataList.get(position);

        if(!curItem.getmIsDate()){
            holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        }

        holder.buttonDelete.setOnClickListener(view -> {
            mDialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_B);
            mDialog.setTitle(mContext.getString(R.string.popup_dialog_a_type_title));
            mDialog.setContent(mContext.getString(R.string.really_delete));
            mDialog.setNegativeButton(mContext.getString(R.string.popup_dialog_button_cancel), null);
            mDialog.setPositiveButton(mContext.getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                requestRecordDeleteApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), holder.filter, curItem.getmDataSn());
                dialog.dismiss();
            });
            mDialog.show();
        });


        holder.date = curItem.getmInputDe();
        holder.filter = curItem.getmFilter();

        if(curItem.getmIsDate()){
            holder.txt_day_date.setText(curItem.getmInputDe());
            holder.swipeLayout.setVisibility(View.GONE);
            holder.date_lay.setVisibility(View.VISIBLE);
        }else{
            holder.swipeLayout.setVisibility(View.VISIBLE);
            holder.date_lay.setVisibility(View.GONE);
            holder.txt_date_item.setText(curItem.getmInputDe().substring(11,16));

            switch (holder.filter){
                case "0":       // 체온
                    holder.img_history_mark.setImageResource(R.drawable.icon_temp);
                    holder.txt_type_item.setText(mContext.getString(R.string.filter_0));
                    holder.txt_content_item.setText(curItem.getmFever()+" ℃");
                    break;
                case "1":       // 해열제
                    holder.img_history_mark.setImageResource(R.drawable.icon_med);
                    holder.txt_type_item.setText(mContext.getString(R.string.filter_1));
                    String str = "";
                    if(curItem.getmInputType().equals("3")){
                        if(curItem.getmInputKind().equals(CommonData.JSON_INPUT_KIND_0))
                            str += mContext.getString(R.string.suppository_1) + " / " + curItem.getmInputVolume();
                        else if(curItem.getmInputKind().equals(CommonData.JSON_INPUT_KIND_1))
                            str += mContext.getString(R.string.suppository_2) + " / " + curItem.getmInputVolume();
                    }else{
                        if(curItem.getmInputKind().equals(CommonData.JSON_INPUT_KIND_0))
                            str += mContext.getString(R.string.remedy_kind_0) + " / " + curItem.getmInputVolume();
                        else if(curItem.getmInputKind().equals(CommonData.JSON_INPUT_KIND_1))
                            str += mContext.getString(R.string.remedy_kind_1) + " / " + curItem.getmInputVolume();
                        else
                            str += mContext.getString(R.string.remedy_kind_2) + " / " + curItem.getmInputVolume();
                    }

                    if(curItem.getmInputType().equals("0"))
                        str += mContext.getString(R.string.ml);
                    else
                        str += mContext.getString(R.string.mg);

                    holder.txt_content_item.setText(str);
                    break;
                case "2":
                    holder.img_history_mark.setImageResource(R.drawable.icon_result);
                    holder.txt_content_item.setText(mContext.getString(R.string.fever_report));
                    holder.txt_type_item.setText(mContext.getString(R.string.filter_report));
                    break;
                case "3":
                    holder.img_history_mark.setImageResource(R.drawable.icon_symtom);
                    holder.txt_type_item.setText(mContext.getString(R.string.filter_3));
                    holder.txt_content_item.setText(mContext.getResources().getIdentifier("symptom_" + curItem.getmInputNum(), "string", mContext.getPackageName()));
                    break;
                case "4":
                    holder.img_history_mark.setImageResource(R.drawable.icon_ill);
                    holder.txt_type_item.setText(mContext.getString(R.string.filter_4));
                    holder.txt_content_item.setText(mContext.getResources().getIdentifier("diagnosis_" + curItem.getmInputNum(), "string", mContext.getPackageName()));
                    break;
                case "5":
                    holder.img_history_mark.setImageResource(R.drawable.icon_memo);
                    holder.txt_type_item.setText(mContext.getString(R.string.filter_5));
                    break;
                case "6":
                    holder.img_history_mark.setImageResource(R.drawable.icon_memo);
                    holder.txt_type_item.setText(mContext.getString(R.string.filter_5));
                    holder.txt_content_item.setText(mContext.getResources().getIdentifier("memo_" + curItem.getmInputNum(), "string", mContext.getPackageName()));
                    break;
            }

            holder.linear_history_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    switch (holder.filter){
                        case "0":       // 체온
                            intent = new Intent(mContext, FeverInputActivity.class);
                            intent.putExtra(CommonData.EXTRA_SN, curItem.getmDataSn());
                            intent.putExtra(CommonData.EXTRA_DATE, curItem.getmInputDe());
                            intent.putExtra(CommonData.EXTRA_FEVER, curItem.getmFever());
                            intent.putExtra(CommonData.EXTRA_IS_EDIT, CommonData.YES);
                            mContext.startActivity(intent);
                            Util.BackAnimationStart(((Activity) mContext));
                            break;
                        case "1":       //  해열제
                            intent = new Intent(mContext, RemedyInputActivity.class);
                            intent.putExtra(CommonData.EXTRA_SN, curItem.getmDataSn());
                            intent.putExtra(CommonData.EXTRA_DATE, curItem.getmInputDe());
                            intent.putExtra(CommonData.EXTRA_KIND, curItem.getmInputKind());
                            intent.putExtra(CommonData.EXTRA_TYPE, curItem.getmInputType());
                            intent.putExtra(CommonData.EXTRA_VOLUME, curItem.getmInputVolume());
                            intent.putExtra(CommonData.EXTRA_IS_EDIT, CommonData.YES);
                            mContext.startActivity(intent);
                            Util.BackAnimationStart(((Activity) mContext));
                            break;
                        case "2":       //  체온 결과
                            intent = new Intent(mContext, FeverResultActivity.class);
                            intent.putExtra(CommonData.EXTRA_SN, curItem.getmDataSn());
                            intent.putExtra(CommonData.EXTRA_DATE, curItem.getmInputDe());
                            intent.putExtra(CommonData.EXTRA_CODE, curItem.getmInputCode());
                            intent.putExtra(CommonData.EXTRA_IS_EDIT, CommonData.YES);
                            mContext.startActivity(intent);
                            Util.BackAnimationStart(((Activity) mContext));
                            break;
                        default:        //  메모
                            intent = new Intent(mContext, MemoInputActivity.class);
                            intent.putExtra(CommonData.EXTRA_SN, curItem.getmDataSn());
                            intent.putExtra(CommonData.EXTRA_DATE, curItem.getmInputDe());
                            intent.putExtra(CommonData.EXTRA_NUM, curItem.getmInputNum());
                            intent.putExtra(CommonData.EXTRA_MEMO, curItem.getmInputMemo());
                            intent.putExtra(CommonData.EXTRA_IS_EDIT, CommonData.YES);
                            if(holder.filter.equals("3"))   //  증상
                                intent.putExtra(CommonData.EXTRA_MEMO_TYPE, 0);
                            else if(holder.filter.equals("4"))   //  진단
                                intent.putExtra(CommonData.EXTRA_MEMO_TYPE, 1);
                            else                                // 메모
                                intent.putExtra(CommonData.EXTRA_MEMO_TYPE, 2);
                            mContext.startActivity(intent);
                            Util.BackAnimationStart(((Activity) mContext));
                            break;
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return allDataList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class FeverAllDataListItemViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linear_history_item, date_lay;
        public ImageView img_history_mark;
        public TextView txt_type_item, txt_date_item, txt_content_item, txt_day_date;
        public String filter;
        public String date;

        public SwipeLayout swipeLayout;
        public ImageButton buttonDelete;

        public FeverAllDataListItemViewHolder(View itemView) {
            super(itemView);
            linear_history_item = (LinearLayout) itemView.findViewById(R.id.linear_history_item);
            img_history_mark = (ImageView)itemView.findViewById(R.id.img_history_mark);
            txt_type_item = (TextView)itemView.findViewById(R.id.txt_type_item);
            txt_date_item = (TextView)itemView.findViewById(R.id.txt_date_item);
            txt_content_item = (TextView)itemView.findViewById(R.id.txt_content_item);
            txt_day_date = (TextView)itemView.findViewById(R.id.txt_day_date);
            date_lay = (LinearLayout)itemView.findViewById(R.id.date_lay);

            swipeLayout = (SwipeLayout)itemView.findViewById(R.id.swipe);
            buttonDelete = (ImageButton)itemView.findViewById(R.id.trash);
        }
    }

    /**
     * 해열제 입력 수정 삭제
     */
    public void requestRecordDeleteApi(String chl_sn, String filter, String data_sn) {
//        GLog.i("requestAppInfo");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject object = new JSONObject();
            switch (filter){
                case "0":   // 체온
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HF004);    //  api 코드명
                    object.put(CommonData.JSON_FEVER_SN_F, data_sn);
                    break;
                case "1":   // 해열제
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HR004);    //  api 코드명
                    object.put(CommonData.JSON_REMEDY_SN_F, data_sn);
                    break;
                case "2":   // 레포트
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HP002);    //  api 코드명
                    object.put(CommonData.JSON_FEVER_RE_SN_F, data_sn);
                    break;
                case "3":   // 증상
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HS002);    //  api 코드명
                    object.put(CommonData.JSON_SYM_SN_F, data_sn);
                    break;
                case "4":   // 진단
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HD002);    //  api 코드명
                    object.put(CommonData.JSON_DISE_SN_F, data_sn);
                    break;
                case "5":   // 예방접종
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HV002);    //  api 코드명
                    object.put(CommonData.JSON_VAC_SN_F, data_sn);
                    break;
                case "6":   // 메모
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HM002);    //  api 코드명
                    object.put(CommonData.JSON_MEMO_SN_F, data_sn);
                    break;
            }
            object.put(CommonData.JSON_CHL_SN_F, chl_sn);               //  자녀키값
            object.put(CommonData.JSON_TYPE_F, CommonData.JSON_DELETE_F);

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(mContext, NetworkConst.NET_REMEDY_INPUT, NetworkConst.getInstance().getFeverDomain(), networkListener, params, ((FeverHxActivity) mContext).getProgressLayout());
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
                case NetworkConst.NET_REMEDY_INPUT:

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);

                                if (data_yn.equals(CommonData.YES)) {
                                    ((FeverHxActivity) mContext).setTab(1);
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
            }
            ((FeverHxActivity) mContext).hideProgress();
        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            ((FeverHxActivity) mContext).hideProgress();
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
            ((FeverHxActivity) mContext).hideProgress();
            dialog.show();

        }
    };
}
