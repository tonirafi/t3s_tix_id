package com.tes_tix_id.android.configapp.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tes_tix_id.android.R
import com.tes_tix_id.android.configapp.utils.bean.Act
import com.tes_tix_id.android.configapp.utils.DensityUtil
import com.tes_tix_id.android.configapp.utils.PicassoUtil
import kotlinx.android.synthetic.main.frag_act_d.*

class ActDialogFragment : BaseDialogFragment(), View.OnClickListener {

    private lateinit var act: Act

    override fun bottomSheetMode(): Boolean {
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_act_d, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (!bottomSheetMode()) {
            dialog?.findViewById<View>(android.R.id.content)?.let {
                val dp = DensityUtil.dp2px(16f)
                it.setPadding(dp, 0, dp, 0)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (act.act_des.isNullOrEmpty()) {
            actDesc.visibility = View.GONE
        } else {
            actDesc.text = act.act_des
            actDesc.visibility = View.VISIBLE
        }

        PicassoUtil.load(act.act_cover)
            .centerCrop()
            .fit()
            .into(ic_topup)

        closeButton.setOnClickListener(this)
        viewDetail.setOnClickListener(this)

    }



    override fun onClick(view: View?) {
        if (view?.id == R.id.viewDetail) {
//            IntentUtil.intentToH5(context, act.act_link)
        }

        dismissAllowingStateLoss()
    }

    fun show(manager: FragmentManager, act: Act) {
        super.show(manager)
        this.act = act
    }
}