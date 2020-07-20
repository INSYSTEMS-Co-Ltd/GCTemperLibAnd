package com.greencross.gctemperlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.FeverFaqItem;

import java.util.ArrayList;
 


public class FeverFaqAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mInflater;
    private ArrayList<FeverFaqItem> mData;
    private Context mContext;

    public FeverFaqAdapter(Context context, ArrayList<FeverFaqItem> items){
        super();
        this.mContext   =   context;
        this.mInflater  =   LayoutInflater.from(context);
        this.mData      =   items;

        mInflater       =   (LayoutInflater)    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition).getmFaqAnswer();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){
            convertView =   mInflater.inflate(R.layout.news_list_group_item, null);

            holder = new ViewHolder();

            holder.mGroupTv     =   (TextView)  convertView.findViewById(R.id.group_tv);
            holder.mGroupImg    =   (ImageView) convertView.findViewById(R.id.group_img);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(isExpanded){
            holder.mGroupImg.setImageResource(R.drawable.btn_faq_answer_on);
        }else{
            holder.mGroupImg.setImageResource(R.drawable.btn_faq_answer);
        }

        holder.mGroupTv.setText(mData.get(groupPosition).getmFaqQestn());   // 제목



        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getmFaqSn();
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){
            convertView =   mInflater.inflate(R.layout.news_list_child_item, null);

            holder = new ViewHolder();

            holder.mChildTv     =   (TextView)  convertView.findViewById(R.id.group_child_tv);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mChildTv.setText(mData.get(groupPosition).getmFaqAnswer());   // 내용

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolder {
        public TextView mGroupTv;
        public ImageView mGroupImg;
        public TextView mChildTv;
    }
}
