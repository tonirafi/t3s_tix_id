package com.tes_tix_id.android.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.aspsine.swipetoloadlayout.OnLoadMoreListener
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.tes_tix_id.android.adapter.BaseCard
import com.tes_tix_id.android.R
import com.tes_tix_id.android.adapter.CardAdapter
import com.tes_tix_id.android.adapter.card.UserCard
import com.tes_tix_id.android.configapp.utils.base.BaseActivity
import com.tes_tix_id.android.configapp.utils.AppUtil
import com.tes_tix_id.android.configapp.utils.StatusBarUtil
import kotlinx.android.synthetic.main.layout_card_list_common.*
import kotlinx.android.synthetic.main.layout_card_list_common_with_toolbar.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity :  BaseActivity(), OnRefreshListener, OnLoadMoreListener{


    private val homeViewModel: HomeViewModel by viewModel()
    private val cardAdapter: CardAdapter = CardAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_card_list_common_with_toolbar)
        initViews()
        setViewModel()
        StatusBarUtil.setDarkMode(this)
    }


    private fun initViews() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AppUtil.insertStatusBarHeight2TopPadding(toolbar)
        }
        toolbar.title = resources.getString(R.string.app_name)
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)

        globalSwapRecyclerView.setAdapter(cardAdapter)
        globalSwapRecyclerView.setAdapter(cardAdapter)
        globalSwapRecyclerView.setRefreshEnabled(true)
        globalSwapRecyclerView.setOnRefreshListener(this)
        globalSwapRecyclerView.isLoadMoreEnabled = false
        globalSwapRecyclerView.setOnLoadMoreListener(this)
        preloadData()
        getDataUsers()

    }


    private fun setViewModel() {

        homeViewModel.listBaseCardPre.observe(this, Observer {
            hideAnimationOrLoading()
            updateCards(it, true)
            globalSwapRecyclerView.onCompleteRefresh()
        })
        homeViewModel.listBaseCard.observe(this, Observer {
            hideAnimationOrLoading()
            updateCards(it, true)
            globalSwapRecyclerView.onCompleteRefresh()
        })
        homeViewModel.listBaseCardLoadMore.observe(this, Observer {
            hideAnimationOrLoading()
            updateCards(it,false)
            globalSwapRecyclerView.onCompleteRefresh()
        })

        homeViewModel.throwable.observe(this, Observer {
            hideAnimationOrLoading()
            netError(it)
            globalSwapRecyclerView.onCompleteRefresh()
            hideProgressBar()

        })
    }



    fun updateCards(list: List<BaseCard>, reloadAll: Boolean) {
         hideProgressBar()
        if (reloadAll) {
            val enable = list.lastOrNull()?.let { it is UserCard } ?: false
            globalSwapRecyclerView.isLoadMoreEnabled = enable

            cardAdapter.list.clear()
            cardAdapter.addAll(list)
            return
        }

        if (list.isEmpty()) {
            globalSwapRecyclerView.onCompleteRefresh()
            globalSwapRecyclerView.isLoadMoreEnabled = false
            return
        }

        val insertIndex = cardAdapter.list.size
        cardAdapter.list.addAll(insertIndex, list)
        cardAdapter.notifyItemRangeInserted(insertIndex, list.size)

    }


    override fun onRefresh() {
        getDataUsers()
    }

    override fun onLoadMore() {
        homeViewModel.loadMoreUsers()
    }

    fun getDataUsers() {
        progress.visibility=View.VISIBLE
        homeViewModel.loadDataUsers()
    }

    fun preloadData(){
        progress.visibility=View.VISIBLE
        homeViewModel.preloadCards()
    }


    fun hideProgressBar(){
        progress.visibility=View.GONE
    }

    override fun netErrorReload() {
        super.netErrorReload()
        getDataUsers()
    }


    override fun netForbiden() {

    }


}