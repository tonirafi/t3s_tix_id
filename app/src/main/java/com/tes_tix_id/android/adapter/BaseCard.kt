package com.tes_tix_id.android.adapter

import android.os.Parcel
import android.os.Parcelable
import com.tes_tix_id.android.configapp.utils.bean.BaseBean
import com.tes_tix_id.android.configapp.utils.DensityUtil

open class BaseCard : BaseBean, Comparable<BaseCard>, Parcelable {
    @JvmField
    @Transient
    var sort = 0
    @JvmField
    var uri: String? = null

    @JvmField
    @Transient
    var uriRequestCode = 1000

    @JvmField
    @Transient
    var useUriRequestCodeAsItemId = false

    @JvmField
    @Transient
    var cardId = 0
    @JvmField
    var padding: Padding? = null
    @JvmField
    var margin: Margin? = null
    @JvmField
    var loading = false
    @JvmField
    var paddingFromLayout //标记padding来源是否从布局中提取
            = false

    override fun toString(): String {
        return String.format("%s@%s", this.javaClass.name, if (paddingFromLayout) null else padding)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        sort = `in`.readInt()
        uri = `in`.readString()
        cardId = `in`.readInt()
        padding = `in`.readParcelable(Padding::class.java.classLoader)
        margin = `in`.readParcelable(Margin::class.java.classLoader)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(sort)
        dest.writeString(uri)
        dest.writeInt(cardId)
        dest.writeParcelable(padding, flags)
        dest.writeParcelable(margin, flags)
    }

    /**
     * 用于列表排序
     *
     * @param another
     * @return
     */
    override fun compareTo(another: BaseCard): Int {
        if (sort == another.sort) {
            return 0
        }
        return if (sort > another.sort) 1 else -1
    }

    fun marginTop(`val`: Int): BaseCard {
        margin = newMarginTop(`val`)
        return this
    }

    class Padding : BaseBean, Parcelable {
        @JvmField
        var left: Int
        @JvmField
        var top: Int
        @JvmField
        var right: Int
        @JvmField
        var bottom: Int

        constructor(left: Int, top: Int, right: Int, bottom: Int) {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }

        override fun toString(): String {
            return String.format("Padding-%s-%s-%s-%s", left, top, right, bottom)
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(left)
            dest.writeInt(top)
            dest.writeInt(right)
            dest.writeInt(bottom)
        }

        protected constructor(`in`: Parcel) {
            left = `in`.readInt()
            top = `in`.readInt()
            right = `in`.readInt()
            bottom = `in`.readInt()
        }

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<Padding?> = object : Parcelable.Creator<Padding?> {
                override fun createFromParcel(source: Parcel): Padding? {
                    return Padding(source)
                }

                override fun newArray(size: Int): Array<Padding?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    class Margin : BaseBean, Parcelable {
        @JvmField
        var left: Int
        @JvmField
        var top: Int
        @JvmField
        var right: Int
        @JvmField
        var bottom: Int
        override fun toString(): String {
            return String.format("Margin-%s-%s-%s-%s", left, top, right, bottom)
        }

        constructor(left: Int, top: Int, right: Int, bottom: Int) {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }

        constructor(left: Int, right: Int) {
            this.left = left
            top = 0
            this.right = right
            bottom = 0
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(left)
            dest.writeInt(top)
            dest.writeInt(right)
            dest.writeInt(bottom)
        }

        protected constructor(`in`: Parcel) {
            left = `in`.readInt()
            top = `in`.readInt()
            right = `in`.readInt()
            bottom = `in`.readInt()
        }

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<Margin?> = object : Parcelable.Creator<Margin?> {
                override fun createFromParcel(source: Parcel): Margin? {
                    return Margin(source)
                }

                override fun newArray(size: Int): Array<Margin?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        //默认水平/垂直 方向边距
        val defaultPadding = DensityUtil.dp2px(16f)
        @JvmField val CREATOR: Parcelable.Creator<BaseCard?> = object : Parcelable.Creator<BaseCard?> {
            override fun createFromParcel(`in`: Parcel): BaseCard? {
                return BaseCard(`in`)
            }

            override fun newArray(size: Int): Array<BaseCard?> {
                return arrayOfNulls(size)
            }
        }

        fun newMarginTop(top: Int): Margin {
            return Margin(0, top, 0, 0)
        }

        fun newMarginBottom(bottom: Int): Margin {
            return Margin(0, 0, 0, bottom)
        }

        @JvmStatic
        @JvmOverloads
        fun newMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): Margin {
            return Margin(left, top, right, bottom)
        }

        fun defaultHorizontalMargin(): Margin {
            return Margin(defaultPadding, 0, defaultPadding, 0)
        }

        fun newPaddingRaw(): Padding {
            return newPadding(0, 0, 0, 0)
        }

        fun newPaddingHorizontal(horizontal: Int): Padding {
            return newPadding(horizontal, defaultPadding, horizontal, defaultPadding)
        }

        fun newPaddingHorizontalRaw(horizontal: Int): Padding {
            return newPadding(horizontal, 0, horizontal, 0)
        }

        fun newPaddingVertical(top: Int, bottom: Int): Padding {
            return newPadding(defaultPadding, top, defaultPadding, bottom)
        }

        fun newPaddingVerticalRaw(top: Int, bottom: Int): Padding {
            return newPadding(0, top, 0, bottom)
        }

        fun newPaddingVertical(vertical: Int): Padding {
            return newPaddingVertical(vertical, vertical)
        }

        fun newPaddingVerticalRaw(vertical: Int): Padding {
            return newPaddingVerticalRaw(vertical, vertical)
        }

        @JvmStatic
        @JvmOverloads
        fun newPadding(left: Int = defaultPadding, top: Int = defaultPadding, right: Int = defaultPadding, bottom: Int = defaultPadding): Padding {
            return Padding(left, top, right, bottom)
        }
    }
}