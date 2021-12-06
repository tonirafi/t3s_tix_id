package com.tes_tix_id.android.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tes_tix_id.android.adapter.BaseCard
import com.tes_tix_id.android.adapter.card.UserCard
import io.reactivex.schedulers.Schedulers


class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private var pageIndex = 1
    private var limit = 10




    val listBaseCard by lazy {
        MutableLiveData<ArrayList<BaseCard>>()
    }
    val listBaseCardLoadMore by lazy {
        MutableLiveData<ArrayList<BaseCard>>()
    }

    val listBaseCardPre by lazy {
        MutableLiveData<ArrayList<BaseCard>>()
    }


    val throwable by lazy {
        MutableLiveData<Throwable>()
    }


    @SuppressLint("CheckResult")
    fun loadDataUsers() {
        pageIndex=1
        homeRepository.loadDataUsers(limit,pageIndex)
            ?.map {

                var baseCards = ArrayList<BaseCard>()

                for (data in it!!) {
                    baseCards.add( UserCard(data))
                }

                baseCards
            }
            ?.subscribe({
                listBaseCard.postValue(it)

            }, {
                throwable.postValue(it)
            })
    }


    @SuppressLint("CheckResult")
    fun loadMoreUsers(){
        pageIndex++

        homeRepository?.loadDataUsers(limit,pageIndex)
            ?.subscribeOn(Schedulers.newThread())
            ?.map { it ->
                var baseCards = ArrayList<BaseCard>()

                    for (data in it!!) {
                        baseCards.add( UserCard(data))
                    }

                baseCards
            }
            ?.subscribe(
                { data ->
                    listBaseCardLoadMore.postValue(data)
                    if (pageIndex > 1 && data.isEmpty()) {
                        pageIndex--
                    }
                },
                { error ->
                    if (pageIndex > 1) {
                        pageIndex--
                    }
                    throwable.postValue(error)
                })

    }




    fun preloadCards() {
        val baseCards = ArrayList<BaseCard>()

        for (i in 0..7) {
            baseCards.add(UserCard(null).apply {
                loading = true
            })
        }

        listBaseCardPre.postValue(baseCards)

    }


}