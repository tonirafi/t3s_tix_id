package com.tes_tix_id.android.configapp.utils.widget.swap;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class GlobalEmptyAdapter extends RecyclerView.Adapter {

    private int emptyType;
    private String text;
    private @DrawableRes
    int itemViewBgResId;
    private View.OnClickListener campaignListHolderBtnOnClickListener;

    private RecyclerView.Adapter mAdapter;
    private static int Empty = 1000001;

    public GlobalEmptyAdapter(RecyclerView.Adapter adapter) {
        this(adapter, CommonEmptyFactory.COMMON_EMPTY);
    }

    public GlobalEmptyAdapter(RecyclerView.Adapter adapter, int emptyType) {
        this.mAdapter = adapter;
        this.emptyType = emptyType;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        if (this.hasStableIds() != hasStableIds) {
            super.setHasStableIds(hasStableIds);
            this.mAdapter.setHasStableIds(hasStableIds);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItemViewType(position) == Empty ? String.valueOf(position).hashCode() : this.mAdapter.getItemId(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Empty) {
            return CommonEmptyFactory.emptyHolder(parent, emptyType);
        }

        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
//        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.registerAdapterDataObserver(innerObserver);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
//        mAdapter.unregisterAdapterDataObserver(observer);
        mAdapter.unregisterAdapterDataObserver(innerObserver);
    }


    private final RecyclerView.AdapterDataObserver innerObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            GlobalEmptyAdapter.this.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            GlobalEmptyAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            GlobalEmptyAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            GlobalEmptyAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            GlobalEmptyAdapter.this.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            GlobalEmptyAdapter.this.notifyItemRangeChanged(positionStart, itemCount, payload);
        }
    };

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == Empty) {
            if (holder instanceof CommonEmptyFactory.CommonHolder) {
                CommonEmptyFactory.CommonHolder commonHolder = (CommonEmptyFactory.CommonHolder) holder;
                if (emptyType == CommonEmptyFactory.COMMON_BLANK) {
                    commonHolder.itemView.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(text)) {
                        commonHolder.tvText.setText(text);
                    }
                }
            } else if (holder instanceof CommonEmptyFactory.CampaignListHolder) {
                CommonEmptyFactory.CampaignListHolder campaignListHolder = (CommonEmptyFactory.CampaignListHolder) holder;
                if (!TextUtils.isEmpty(text)) {
                    campaignListHolder.tvText.setText(text);
                }

                campaignListHolder.btn.setOnClickListener(campaignListHolderBtnOnClickListener);
            }

            return;
        }

        holder.itemView.setBackgroundResource(itemViewBgResId);

        mAdapter.onBindViewHolder(holder, position);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (getItemViewType(position) == Empty) {
            onBindViewHolder(holder, position);
            return;
        }

        mAdapter.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdapter.getItemCount() == 0) {
            return Empty;
        }
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() == 0 ? 1 : mAdapter.getItemCount();
    }

    public RecyclerView.Adapter getRealAdapter() {
        return mAdapter;
    }

    public void updateText(String text) {
        this.text = text;
        notifyDataSetChanged();
    }

    public void setBackgroundResource(@DrawableRes int resid) {
        this.itemViewBgResId = resid;
        notifyDataSetChanged();
    }

    public void setCampaignListHolderBtnOnClickListener(View.OnClickListener campaignListHolderBtnOnClickListener) {
        this.campaignListHolderBtnOnClickListener = campaignListHolderBtnOnClickListener;
        notifyDataSetChanged();
    }


}
