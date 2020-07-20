package com.greencross.gctemperlib.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.YoutubeItem;

import java.util.ArrayList;

/**
 * Created by MobileDoctor on 2017-06-07.
 */

public class YoutubeListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<YoutubeItem> mYoutubeListItem;
    private DisplayMetrics dm;

    private OnLoadMoreListener onLoadMoreListener;

    int visibleThreshold = 1;
    int totalItemCount, lastVisibleItem;
    private boolean loading;

    public YoutubeListAdapter(Context _context, ArrayList<YoutubeItem> _mYoutubeListItem, RecyclerView recyclerView){
        mContext = _context;
        mYoutubeListItem = _mYoutubeListItem;
        dm = mContext.getResources().getDisplayMetrics();

        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                    if(!loading && totalItemCount <= (lastVisibleItem+visibleThreshold)){
                        if(onLoadMoreListener != null){
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_list_item, parent, false);
        vh = new YoutubeItemViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof YoutubeItemViewHolder){
            YoutubeItem curItem = mYoutubeListItem.get(position);

            ((YoutubeItemViewHolder)holder).group_tv.setText(curItem.getInfo_subject());

            ((YoutubeItemViewHolder)holder).group_layout.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(curItem.getInfo_title_url()));
                mContext.startActivity(i);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mYoutubeListItem.size();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class YoutubeItemViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout group_layout;
        public TextView group_tv;

        public YoutubeItemViewHolder(View itemView) {
            super(itemView);

            group_layout = (RelativeLayout)itemView.findViewById(R.id.group_layout);
            group_tv = (TextView)itemView.findViewById(R.id.group_tv);
        }
    }
}
