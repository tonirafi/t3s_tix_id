package com.tes_tix_id.android.configapp.utils.widget.swap

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.tes_tix_id.android.R
import butterknife.BindView
import android.widget.TextView
import butterknife.ButterKnife
import java.lang.RuntimeException

object CommonEmptyFactory {
    const val COMMON_EMPTY = 0
    const val CAMPAIGN_LIST_EMPTY = 1
    const val COMMON_BLANK = 2
    @JvmStatic
    fun emptyHolder(parent: ViewGroup, emptyType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View
        return when (emptyType) {
            COMMON_EMPTY, COMMON_BLANK -> {
                view = inflater.inflate(R.layout.common_empty_layout, parent, false)
                CommonHolder(view)
            }
            CAMPAIGN_LIST_EMPTY -> {
                view = inflater.inflate(R.layout.list_empty_layout, parent, false)
                CampaignListHolder(view)
            }
            else -> throw RuntimeException("there is no type that matches the type $emptyType + make sure your using types correctly")
        }
    }

    internal class CommonHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        @JvmField
        @BindView(R.id.text)
        var tvText: TextView? = null

        init {
            ButterKnife.bind(this, view!!)
        }
    }

    class CampaignListHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        @JvmField
        @BindView(R.id.text)
        var tvText: TextView? = null

        @JvmField
        @BindView(R.id.btn)
        var btn: Button? = null

        init {
            ButterKnife.bind(this, view!!)
        }
    }
}