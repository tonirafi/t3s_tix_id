package com.tes_tix_id.android.adapter

import android.text.TextUtils

class CardMapInitializer : ICardMapInitializer {
    override fun initRouterTable(cardNameList: MutableList<String>, providerNameList: MutableList<String>) {
        addCardProviderPair(cardNameList, providerNameList, "UserCard", "UserCardProvider")

    }


    fun addCardProviderPair(cardNameList: MutableList<String>, providerNameList: MutableList<String>, cardName: String, providerName: String) {
        if (TextUtils.isEmpty(cardName) || TextUtils.isEmpty(providerName)) return
        if (cardName.contains(".")) cardNameList.add(cardName) else cardNameList.add(CARD_DIR + cardName)
        if (providerName.contains(".")) providerNameList.add(providerName) else providerNameList.add(
            PROVIDER_DIR + providerName)
    }

    companion object {
        private const val CARD_DIR = "com.tes_tix_id.android.adapter.card."
        private const val PROVIDER_DIR = "com.tes_tix_id.android.adapter.providers."
    }


}