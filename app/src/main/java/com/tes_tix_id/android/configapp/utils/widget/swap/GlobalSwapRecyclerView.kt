package com.tes_tix_id.android.configapp.utils.widget.swap

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.Recycler
import butterknife.ButterKnife
import com.aspsine.swipetoloadlayout.OnLoadMoreListener
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout
import com.tes_tix_id.android.R
import com.tes_tix_id.android.adapter.BaseCard
import com.tes_tix_id.android.adapter.CardAdapter
import com.tes_tix_id.android.adapter.FooterCard
import com.tes_tix_id.android.configapp.utils.LogUtil
import java.lang.ref.WeakReference


class GlobalSwapRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var headerView: GlobalRefreshHeaderView? = null

    var mSwipeLoadMoreFooter: GlobalLoadMoreFooterView? = null
    private val style: Int
    private val autoLoadMore: Boolean
    var emptyAdapter: GlobalEmptyAdapter? = null
        private set

    var customRecyclerView: RecyclerView? = null

    var mSwipeToLoadLayout: SwipeToLoadLayout? = null
    private var adjustFooterView = true
    private fun setUpView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.swap_refresh_layout, this)
        //        inflate(context,R.layout.qsc_swap_refresh_layout, this);
        ButterKnife.bind(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        headerView = findViewById(R.id.swipe_refresh_header)
        customRecyclerView = findViewById(R.id.swipe_target)
        mSwipeLoadMoreFooter = findViewById(R.id.swipe_load_more_footer)
        mSwipeToLoadLayout = findViewById(R.id.swipeToLoadLayout)
        when (style) {
            STYLE_ALL -> {
            }
            STYLE_NO_HEADER -> mSwipeToLoadLayout?.setRefreshEnabled(
                false
            )
            STYLE_NO_FOOTER -> mSwipeToLoadLayout?.setLoadMoreEnabled(
                false
            )
        }
        if (autoLoadMore) {

            customRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int
                ) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!recyclerView.canScrollVertically(1)) {
                            mSwipeToLoadLayout?.setLoadingMore(true)
                        }
                    }
                }
            })
        }
        customRecyclerView!!.layoutManager = InnerLinearLayoutManager(context)
        customRecyclerView!!.setHasFixedSize(true)
        customRecyclerView!!.itemAnimator = DefaultItemAnimator()
        mSwipeToLoadLayout?.setRefreshing(false)
    }

    fun setScrollingEnabled(enabled: Boolean) {
//        final RecyclerView.LayoutManager layoutManager = mSwipeTarget.getLayoutManager();
//        if (layoutManager instanceof InnerLinearLayoutManager) {
//            ((InnerLinearLayoutManager) layoutManager).setCanScrollVertically(canScrollVertically);

//        }
        mSwipeToLoadLayout?.setEnabled(enabled)
//        mSwipeToLoadLayout?.nested(enabled)
    }

    private class InnerLinearLayoutManager : LinearLayoutManager {
        private var canScrollVertically = true
        fun setCanScrollVertically(canScrollVertically: Boolean) {
            this.canScrollVertically = canScrollVertically
        }

        constructor(context: Context?) : super(context) {}
        constructor(
            context: Context?,
            orientation: Int,
            reverseLayout: Boolean
        ) : super(context, orientation, reverseLayout) {
        }

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes) {
        }

        override fun canScrollVertically(): Boolean {
            return canScrollVertically && super.canScrollVertically()
        }

        override fun smoothScrollToPosition(
            recyclerView: RecyclerView,
            state: RecyclerView.State,
            position: Int
        ) {
            val linearSmoothScroller =
                InnerLinearSmoothScroller(recyclerView.context)
            linearSmoothScroller.targetPosition = position
            startSmoothScroll(linearSmoothScroller)
        }

        override fun onLayoutChildren(
            recycler: Recycler,
            state: RecyclerView.State
        ) {
            /**
             * 可规避 java.lang.IndexOutOfBoundsException: 引起的崩溃
             */
            try {
                super.onLayoutChildren(recycler, state)
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }

        internal class InnerLinearSmoothScroller(context: Context?) :
            LinearSmoothScroller(context) {
            /**
             * @param viewStart      RecyclerView的top位置
             * @param viewEnd        RecyclerView的Bottom位置
             * @param boxStart       item的top位置
             * @param boxEnd         item的bottom位置
             * @param snapPreference 滑动方向的识别
             * @return
             */
            override fun calculateDtToFit(
                viewStart: Int,
                viewEnd: Int,
                boxStart: Int,
                boxEnd: Int,
                snapPreference: Int
            ): Int {
                return boxStart - viewStart //返回的就是我们item置顶需要的偏移量
            }

            /**
             * 此方法返回滚动每1px需要的时间,可以用来控制滚动速度
             * 即如果返回2ms，则每滚动1000px，需要2秒钟
             *
             * @param displayMetrics
             * @return
             */
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return super.calculateSpeedPerPixel(displayMetrics)
            }
        }
    }

    /**
     * 对外提供的方法
     */
    fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        mSwipeToLoadLayout?.setOnLoadMoreListener(listener)
    }

    fun setOnRefreshListener(listener: OnRefreshListener?) {
        mSwipeToLoadLayout?.setOnRefreshListener(listener)
    }

    fun beginRefreshing() {
        mSwipeToLoadLayout?.setRefreshing(true)
    }

    fun beginLoadingMore() {
        mSwipeToLoadLayout?.setLoadingMore(true)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        setAdapter(adapter, CommonEmptyFactory.COMMON_EMPTY)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>, emptyType: Int) {
        emptyAdapter = GlobalEmptyAdapter(adapter, emptyType)
        emptyAdapter!!.setHasStableIds(adapter.hasStableIds())
        customRecyclerView!!.adapter = emptyAdapter
    }

    fun setHasStableIds(hasStableIds: Boolean) {
        emptyAdapter!!.setHasStableIds(hasStableIds)
    }

    fun setDefaultColor(color: Int) {
        customRecyclerView!!.setBackgroundColor(color)
        //        mSwipeRefreshHeader.setBackgroundColor(color);
//        mSwipeLoadMoreFooter.setBackgroundColor(color);
    }

    fun setSwipeRefreshHeaderBackground(color: Int) {
        headerView!!.setBackgroundColor(color)
    }

    fun setAdjustFooterView(adjustFooterView: Boolean) {
        this.adjustFooterView = adjustFooterView
    }

    fun onCompleteRefresh() {
        when (style) {
            STYLE_ALL -> {
                mSwipeToLoadLayout?.setRefreshing(false)
                mSwipeToLoadLayout?.setLoadingMore(false)
            }
            STYLE_NO_HEADER -> mSwipeToLoadLayout?.setLoadingMore(false)
            STYLE_NO_FOOTER -> mSwipeToLoadLayout?.setRefreshing(false)
        }
        if (adjustFooterView) {
            if (observer == null || !observer!!.isAlive) {
                observer = customRecyclerView!!.viewTreeObserver // used for later cleanup
            }
            if (!observer!!.isAlive) {
                // If the observer’s not in a good state, skip the transition
                observer = null
                return
            }
            observer!!.addOnPreDrawListener(
                InnerOnPreDrawListener(
                    WeakReference(
                        this
                    )
                )
            )
        }
    }

    var observer: ViewTreeObserver? = null
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //        onRefreshComplete = true;
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    private class InnerOnPreDrawListener(var ref: WeakReference<GlobalSwapRecyclerView>) :
        ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            val swapRecyclerView = ref.get()
            if (swapRecyclerView != null && swapRecyclerView.observer != null) {
                if (swapRecyclerView.observer!!.isAlive) {
                    swapRecyclerView.observer!!.removeOnPreDrawListener(this)
                }
                swapRecyclerView.observer = null
                //                swapRecyclerView.mSwipeTarget.getViewTreeObserver().removeOnPreDrawListener(this);
                return !swapRecyclerView.fixFootView()
            }
            return true
        }

    }

    private var preSwipeTargetHeight = 0
    private fun fixFootView(): Boolean {
        if (emptyAdapter == null || emptyAdapter!!.realAdapter == null || emptyAdapter!!.realAdapter
                .itemCount <= 0
        ) return false
        if (customRecyclerView!!.layoutManager is LinearLayoutManager) {
            val lm = customRecyclerView!!.layoutManager as LinearLayoutManager?
            val lastCPos = lm!!.findLastCompletelyVisibleItemPosition()
            if (lastCPos == -1) return false
            if (emptyAdapter!!.realAdapter is CardAdapter) {
                val cardAdapter: CardAdapter = emptyAdapter!!.realAdapter as CardAdapter
                val size: Int = cardAdapter.list?.size!!
                if (lastCPos >= size) return false
                val footerCard: BaseCard = cardAdapter.list?.get(lastCPos) as BaseCard
                if (footerCard is FooterCard) {
                    val footerView = lm.getChildAt(lastCPos) ?: return false

                    //兼容某些android系统在onResume中调用时 mSwipeTarget容器或者整个根布局高度有一个状态栏高度的变化
                    LogUtil.logD(
                        "QSCSwapRecyclerView",
                        "===fixFootView  preSwipeTargetHeight=$preSwipeTargetHeight"
                    )
                    val swipeTargetHeightOffset =
                        customRecyclerView!!.height - preSwipeTargetHeight
                    LogUtil.logD(
                        "QSCSwapRecyclerView",
                        "===fixFootView  swipeTargetHeightOffset=$swipeTargetHeightOffset"
                    )
                    var flag = false
                    if ((footerCard as FooterCard).fillAsPadding) {
                        val delta = customRecyclerView!!.height - footerView.bottom
                        LogUtil.logD(
                            "QSCSwapRecyclerView",
                            "===fixFootView fillAsPadding delta=$delta"
                        )
                        if (delta > 0 && swipeTargetHeightOffset != delta) {
                            footerCard.padding?.top?.plus(delta)
                            preSwipeTargetHeight = customRecyclerView!!.height
                            if ((footerCard as FooterCard).useNotifyItemChanged) {
                                cardAdapter.notifyItemChanged(lastCPos)
                            } else {
                                cardAdapter.notifyDataSetChanged()
                            }
                            flag = true
                        }
                    } else {
                        if (footerCard.margin == null) return false
                        val delta: Int =
                            customRecyclerView!!.height - footerView.bottom - footerCard.margin!!.bottom
                        LogUtil.logD("QSCSwapRecyclerView", "===fixFootView  delta=$delta")
                        if (delta > 0 && swipeTargetHeightOffset != delta) {
                            footerCard.margin!!.top += delta
                            preSwipeTargetHeight = customRecyclerView!!.height
                            if ((footerCard as FooterCard).useNotifyItemChanged) {
                                cardAdapter.notifyItemChanged(lastCPos)
                            } else {
                                cardAdapter.notifyDataSetChanged()
                            }
                            flag = true
                        }
                    }
                    if (fixFootViewListener != null && flag) {
                        fixFootViewListener!!
                    }
                    return true
                }
            }
        }
        return false
    }

    var fixFootViewListener = null
    fun setFixFootViewListener(fixFootViewListener: OnFixFootViewListener?) {
        this.fixFootViewListener = fixFootViewListener as Nothing?
    }

    fun resetTopPadding(topPadding: Int) {
        customRecyclerView!!.setPadding(
            customRecyclerView!!.paddingLeft,
            topPadding,
            customRecyclerView!!.paddingRight,
            customRecyclerView!!.paddingBottom
        )
        customRecyclerView!!.clipToPadding = false
    }

    fun smoothScrollToPosition(position: Int) {
        customRecyclerView!!.smoothScrollToPosition(position)
    }

    fun addItemDecoration(decoration: ItemDecoration?) {
        customRecyclerView!!.addItemDecoration(decoration!!)
    }

    fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        customRecyclerView!!.addOnScrollListener(listener)
    }

    fun removeOnScrollListener(listener: RecyclerView.OnScrollListener) {
        customRecyclerView!!.removeOnScrollListener(listener)
    }

    fun clearOnScrollListeners() {
        customRecyclerView!!.clearOnScrollListeners()
    }

    val isLoadingMore: Boolean
        get() = mSwipeToLoadLayout!!.isLoadingMore()

    /**
     * 代码设置是否有Header、Footer
     */
    var isLoadMoreEnabled: Boolean
        get() = mSwipeToLoadLayout!!.isLoadMoreEnabled()
        set(loadMoreEnabled) {
            mSwipeToLoadLayout?.setLoadMoreEnabled(loadMoreEnabled)
        }

    fun setRefreshEnabled(refreshEnabled: Boolean) {
        mSwipeToLoadLayout?.setRefreshEnabled(refreshEnabled)
    }

    fun setListBackgroundResource(resId: Int) {
        if (customRecyclerView != null) {
            customRecyclerView!!.setBackgroundResource(resId)
            if (emptyAdapter != null && !customRecyclerView!!.clipToPadding) {
                emptyAdapter!!.setBackgroundResource(resId)
            }
        }
    }


    companion object {
        private const val STYLE_ALL = 0
        private const val STYLE_NO_HEADER = 1
        private const val STYLE_NO_FOOTER = 2
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.QSCSwapRecyclerView)
        style = a.getInt(R.styleable.QSCSwapRecyclerView_style, STYLE_ALL)
        autoLoadMore = a.getBoolean(R.styleable.QSCSwapRecyclerView_autoLoadMore, true)
        a.recycle()
        setUpView(context)
    }
}