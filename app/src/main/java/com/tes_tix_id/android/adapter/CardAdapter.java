package com.tes_tix_id.android.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CardAdapter extends RecyclerView.Adapter {
    protected List<BaseCard> mList;
    protected Context mContext;
    protected SparseArray<ItemViewProvider> mProviders;
    protected static final List<String> mCardPool = new ArrayList<>();
    protected static final List<String> mProviderPool = new ArrayList<>();
    protected static final Map<String, Integer> mViewTypeVal = new HashMap<>();
    protected final SparseIntArray mViewTypePos = new SparseIntArray();

    public CardAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.mContext = context;
        this.mProviders = new SparseArray<>();
    }

    static {
        CardMapInitializer initializer = new CardMapInitializer();
        initializer.initRouterTable(mCardPool, mProviderPool);
    }

    public void add(BaseCard card, boolean needSort) {
        if (card != null) {
            mList.add(card);
            if (needSort) {
                Collections.sort(mList);
            }
            notifyDataSetChanged();
        }
    }

    public void add(BaseCard card) {
        add(card, false);
    }

    public void add(int location, BaseCard card) {
        if (card != null) {
            mList.add(location, card);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemOnclick(int position);
    }


    protected OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public long getItemId(int position) {
        BaseCard card = getItem(position);
        if (card.useUriRequestCodeAsItemId)
            return card.uriRequestCode;

        return position;
    }

    @Override
    public int getItemViewType(int position) {
        int keyHash = -1;

        BaseCard baseCard = getItem(position);
        if (baseCard != null) {
            String key = baseCard.toString();

            Integer obj = mViewTypeVal.get(key);
            if (obj == null) {
                keyHash = key.hashCode();
                mViewTypeVal.put(key, keyHash);
            } else {
                keyHash = obj;
            }

            mViewTypePos.put(keyHash, mCardPool.indexOf(baseCard.getClass().getName()));
        }

        return keyHash;

//        return mCardPool.indexOf(mList.get(position).getClass().getName());
    }

    @Nullable
    public BaseCard getItem(int position) {
        if (position >= 0 && position < mList.size()) {
            return mList.get(position);
        }
        return null;
    }

    public void clear() {
        if (!mList.isEmpty()) {
            mList.clear();
            notifyDataSetChanged();
        }

    }

    public void replaceAllWithAnim(List<BaseCard> list) {
        int size = mList.size();
        mList.clear();
        mList.addAll(list);
        notifyItemRangeChanged(0, size > list.size() ? size : list.size());
    }

    public void addAll(List list, boolean needSort) {
        if (list == null) return;
        mList.addAll(list);
        if (needSort) {
            Collections.sort(mList);
        }
        notifyDataSetChanged();
    }

    public void addAll(List list) {
        addAll(list, false);
    }

    public void clearList() {
        mList.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemViewProvider provider = getProviderByViewType(viewType);
        if (provider == null) {
            provider = getWithoutProvider();
        }
        if (provider == null) {
            return null;
        }
        return provider.onCreateViewHolder(inflater, parent);
    }

    private ItemViewProvider getWithoutProvider() {
        return withoutProvider;
    }

    public void setWithoutProvider(ItemViewProvider withoutProvider) {
        this.withoutProvider = withoutProvider;
    }

    private ItemViewProvider withoutProvider;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewProvider provider = getProvider(holder, position);
        if (provider == null) {
            return;
        }
        provider.onBindViewHolder((CommonVh) holder, getItem(position), null);
    }

    /**
     * 不管是 RV 在初始化时，还是在因为上下滑动而刷新时，都会走该方法（此时 payloads 为空），而不走 onBindViewHolder(ViewHolder holder, int position) 方法，
     * 因此，需要完善逻辑，在 payloads 为空时手动调用 onBindViewHolder(holder, position); 或者将原本 onBindViewHolder(holder, position); 的逻辑写到这里。
     * ---------------------
     *
     * @param holder
     * @param position
     * @param payloads
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        ItemViewProvider provider = getProvider(holder, position);
        if (provider == null) {
            return;
        }

        provider.onBindViewHolder((CommonVh) holder, getItem(position), payloads);
    }

    private ItemViewProvider getProvider(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewProvider provider = null;
        if (holder instanceof CommonVh) {
            provider = getProviderByViewType(getItemViewType(position));
            if (provider == null) {
                provider = getWithoutProvider();
            }
        }

        return provider;
    }

    @Deprecated
    public void updateItemViewByCardId(BaseCard baseCard) {
        int index = -1;
        Iterator<BaseCard> iterator = mList.iterator();
        while (iterator.hasNext()) {BaseCard card = iterator.next();
            if (card.cardId == baseCard.cardId) {
                index = mList.indexOf(card);
                iterator.remove();
                break;
            }
        }
        if (index >= 0) {
            mList.add(index, baseCard);
            notifyItemChanged(index);
        }

    }

    public ItemViewProvider getProviderByViewType(int viewType) {

        int providerPos = mViewTypePos.get(viewType, -1);
        if (providerPos == -1)
            return null;

        ItemViewProvider provider = mProviders.get(providerPos);
        if (provider == null) {
            try {
                Constructor c = Class.forName(mProviderPool.get(providerPos)).getConstructor(OnItemClickListener.class);
                provider = (ItemViewProvider) c.newInstance(mOnItemClickListener);
                if (provider != null) {
                    mProviders.put(providerPos, provider);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                return null;
            }
        }
        return provider;
    }

    public List<BaseCard> getList() {
        return mList;
    }

    public boolean contains(int cardId) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).cardId == cardId) {
                return true;
            }
        }
        return false;
    }

    public void removeAll(List list) {
        mList.removeAll(list);
//        if (needSort) {
//            Collections.sort(mList);
//        }
        notifyDataSetChanged();
    }

    public void removeFirstItemViewByCardId(int cardId) {
        Iterator<BaseCard> iterator = mList.iterator();
        while (iterator.hasNext()) { BaseCard card = iterator.next();
            if (card.cardId == cardId) {
                iterator.remove();
                break;
            }
        }
        notifyDataSetChanged();
    }

    synchronized public BaseCard findBaseCardIndexByClazz(Class<?> clazz) {

        for (BaseCard baseCard : mList) {
            if (clazz.isAssignableFrom(baseCard.getClass())) {
                return baseCard;
            }
        }
        return null;
    }

    synchronized public List<BaseCard> findBaseCardsIndexByClazz(Class<?> clz) {
        List<BaseCard> result = new LinkedList<>();

        for (BaseCard baseCard : mList) {
            if (clz.isAssignableFrom(baseCard.getClass())) {
                result.add(baseCard);
            }
        }
        return result;
    }


}
