package com.gaocheng.baselibrary.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;

import java.util.ArrayList;
import java.util.List;



public class ProgressLayout extends RelativeLayout {

    private static final String LOADING_TAG = "ProgressLayout.LOADING_TAG";
    private static final String ERROR_TAG = "ProgressLayout.ERROR_TAG";
    private static final String EMPTY_TAG = "ProgressLayout.EMPTY_TAG";

    private LayoutInflater inflater;
    private LayoutParams layoutParams;
    private View loadingGroup;
    private View errorGroup;
    private View emptyGroup;

    private TextView errorTextView;
    private TextView errorButton;

    private List<View> contentViews = new ArrayList<>();

    private enum State {
        LOADING, CONTENT, ERROR, EMPTY
    }

    private State currentState = State.LOADING;

    public ProgressLayout(Context context) {
        super(context);
    }

    public ProgressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (child.getTag() == null || (!child.getTag().equals(LOADING_TAG) && !child.getTag()
                .equals(ERROR_TAG) && !child.getTag().equals(EMPTY_TAG))) {
            contentViews.add(child);
        }
    }

    public void showLoading() {

        ProgressLayout.this.showLoadingView();
        ProgressLayout.this.hideErrorView();
        ProgressLayout.this.hideEmptyView();
        ProgressLayout.this.setContentVisibility(false);
        currentState = State.LOADING;
    }

    public void showContent() {

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideErrorView();
        ProgressLayout.this.hideEmptyView();
        ProgressLayout.this.setContentVisibility(true);
        currentState = State.CONTENT;
    }

    public void showEmpty() {

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideErrorView();
        ProgressLayout.this.setContentVisibility(false);
        ProgressLayout.this.showEmptyView();
        currentState = State.EMPTY;
    }

    public void showError(@StringRes int stringId, @NonNull OnClickListener onClickListener) {

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideEmptyView();
        ProgressLayout.this.showErrorView();

        errorTextView.setText(getResources().getString(stringId));
        errorButton.setOnClickListener(onClickListener);
        ProgressLayout.this.setContentVisibility(false);
        currentState = State.ERROR;
    }

    public void showError(String str, @NonNull OnClickListener onClickListener) {

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideEmptyView();
        ProgressLayout.this.showErrorView();

        errorTextView.setText(str);
        errorButton.setOnClickListener(onClickListener);
        ProgressLayout.this.setContentVisibility(false);
        currentState = State.ERROR;
    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (errorButton != null) errorButton.setOnClickListener(null);
//    }


    public State getCurrentState() {
        return currentState;
    }

    public boolean isContent() {
        return currentState == State.CONTENT;
    }

    public boolean isLoading() {
        return currentState == State.LOADING;
    }

    public boolean isError() {
        return currentState == State.ERROR;
    }

    public boolean isEmpty() {
        return currentState == State.EMPTY;
    }

    private void showLoadingView() {

        if (loadingGroup == null) {
            loadingGroup = inflater.inflate(R.layout.layout_progress, null);
            loadingGroup.setTag(LOADING_TAG);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            ProgressLayout.this.addView(loadingGroup, layoutParams);
        } else {
            loadingGroup.setVisibility(VISIBLE);
        }
    }

    private void showErrorView() {

        if (errorGroup == null) {
            errorGroup = inflater.inflate(R.layout.layout_error, null);
            errorGroup.setTag(ERROR_TAG);

            errorTextView =  errorGroup.findViewById(R.id.progress_error_tv);
            errorButton = errorGroup.findViewById(R.id.progress_error_btn);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            ProgressLayout.this.addView(errorGroup, layoutParams);
        } else {
            errorGroup.setVisibility(VISIBLE);
        }
    }

    private void hideLoadingView() {
        if (loadingGroup != null && loadingGroup.getVisibility() != GONE) {
            loadingGroup.setVisibility(GONE);
        }
    }

    private void hideErrorView() {
        if (errorGroup != null && errorGroup.getVisibility() != GONE) {
            errorGroup.setVisibility(GONE);
        }
    }


    public void setEmptyViewRes(View emptyView) {
        this.emptyGroup = emptyView;
    }

    public void showEmptyView() {
        if (emptyGroup == null) {
            return;
        }
        if (emptyGroup.getTag() == null || !emptyGroup.getTag().equals(EMPTY_TAG)) {
            emptyGroup.setTag(EMPTY_TAG);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);
            ProgressLayout.this.addView(emptyGroup, layoutParams);
            emptyGroup.setVisibility(VISIBLE);
        } else {
            emptyGroup.setVisibility(VISIBLE);
        }
        currentState = State.EMPTY;

    }

    public void hideEmptyView() {
        if (emptyGroup != null && emptyGroup.getVisibility() != GONE) {
            emptyGroup.setVisibility(GONE);
        }

    }

    private void setContentVisibility(boolean visible) {
        for (View contentView : contentViews) {
            contentView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
}