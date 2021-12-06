package com.tes_tix_id.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

abstract class ItemViewProvider<C : BaseCard?, V : CommonVh<C>?>(var mOnItemClickListener: CardAdapter.OnItemClickListener?) {
    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): V

    //    public void onBindViewHolder(@NonNull V holder, @NonNull C card) {
    //        holder.bind(card);
    //    }
    fun onBindViewHolder(holder: V, card: C, payloads: MutableList<out Any?>) {
        if (payloads == null || payloads.isEmpty()) {
            holder!!.bind(card)
        } else {
            holder!!.bind(card, payloads)
        }
    }
}