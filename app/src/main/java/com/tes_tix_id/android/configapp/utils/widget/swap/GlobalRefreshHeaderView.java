package com.tes_tix_id.android.configapp.utils.widget.swap;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.tes_tix_id.android.R;

import butterknife.BindView;


public class GlobalRefreshHeaderView extends RelativeLayout implements SwipeRefreshTrigger, SwipeTrigger {

    @BindView(R.id.ivRefresh)
    ImageView mIvRefresh;
    @BindView(R.id.tv_status)
    TextView mTextView;

    public final String refreshing;
    public final String pull_to_refresh;
    public final String release_to_refresh;
    public final String refresh_completed;

    private int mHeaderHeight;

    ObjectAnimator animator;

    public GlobalRefreshHeaderView(Context context) {
        this(context, null);
    }

    public GlobalRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlobalRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        refreshing = getResources().getString(R.string.refreshing);
        pull_to_refresh = getResources().getString(R.string.pull_to_refresh);
        release_to_refresh = getResources().getString(R.string.release_to_refresh);
        refresh_completed = getResources().getString(R.string.refresh_completed);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (dealNull()) {
            mIvRefresh = findViewById(R.id.ivRefresh);
            mTextView = findViewById(R.id.tv_status);
//            mTextView.setText(refreshing);
//            mIvRefresh.setImageResource(R.drawable.header_animation);
//            mAnimDrawable = (AnimationDrawable) mIvRefresh.getDrawable();
//            ObjectAnimator leftDown = ObjectAnimator.ofFloat(mIvRefresh, "rotation", 0f, -90f,-180f,-90f,0);
            animator = ObjectAnimator.ofFloat(mIvRefresh, "rotation", 0f, 360f);
            animator.setDuration(800);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());

        }
    }

    private boolean dealNull() {
        return mIvRefresh == null || mTextView == null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeaderHeight = h;
    }

    @Override
    public void onRefresh() {
        if (dealNull()) return;
        mTextView.setText(refreshing);
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    @Override
    public void onPrepare() {
        if (dealNull() && pull_to_refresh.equals(mTextView.getText().toString()))
            return;
        mTextView.setText(pull_to_refresh);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (dealNull() || isComplete) return;

        if (animator.isRunning()) {
            animator.cancel();
        }

        if (y < mHeaderHeight && !pull_to_refresh.equals(mTextView.getText().toString())) {
            mTextView.setText(pull_to_refresh);
        } else if (y > mHeaderHeight && !release_to_refresh.equals(mTextView.getText().toString())) {
            mTextView.setText(release_to_refresh);
        }
    }

    @Override
    public void onRelease() {
        if (dealNull()) return;
        mTextView.setText(refreshing);
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    @Override
    public void onComplete() {
        if (dealNull()) return;
        mTextView.setText(refresh_completed);
    }

    @Override
    public void onReset() {
        if (dealNull()) return;
        mTextView.setText(pull_to_refresh);
        if (animator.isRunning()) {
            animator.cancel();
        }
    }

}
