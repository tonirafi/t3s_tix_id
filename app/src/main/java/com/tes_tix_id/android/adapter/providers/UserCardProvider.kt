package com.tes_tix_id.android.adapter.providers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.tes_tix_id.android.adapter.card.UserCard
import com.tes_tix_id.android.R
import com.tes_tix_id.android.adapter.CardAdapter
import com.tes_tix_id.android.adapter.CardMap
import com.tes_tix_id.android.adapter.CommonVh
import com.tes_tix_id.android.adapter.ItemViewProvider
import com.tes_tix_id.android.configapp.utils.PicassoUtil
import com.tes_tix_id.android.configapp.utils.ToastUtil

@CardMap(UserCard::class)
class UserCardProvider(listener: CardAdapter.OnItemClickListener?) : ItemViewProvider<UserCard?, UserCardProvider.ViewHolder?>(listener) {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_user, parent, false), mOnItemClickListener)
    }

    class ViewHolder @JvmOverloads constructor(itemView: View?, onItemClickListener: CardAdapter.OnItemClickListener? = null) : CommonVh<UserCard?>(itemView, onItemClickListener) {
        @JvmField
        @BindView(R.id.tvName)
        var tvName: TextView? = null

        @JvmField
        @BindView(R.id.tvIdUser)
        var tvIdUser: TextView? = null

        @JvmField
        @BindView(R.id.tvUrlAvatar)
        var tvUrlAvatar: TextView? = null

        @JvmField
        @BindView(R.id.ic_profile)
        var ic_profile: ImageView? = null

        override fun bind(card: UserCard?) {
            super.bind(card)
            val user = card!!.user
            if (user == null) {
                if (card.loading) {
                    showPlaceholders()
                }
                return
            }
            tvName!!.text = user.login
            tvIdUser!!.text = user.id.toString()
            tvUrlAvatar!!.text = user.avatarUrl
            PicassoUtil.load(user.avatarUrl).centerCrop().fit().into(ic_profile)
            itemView!!.setOnClickListener { v ->
                ToastUtil.show(" ${user.login}  ${user.id}  ${user.avatarUrl}")
                }
        }
    }
}