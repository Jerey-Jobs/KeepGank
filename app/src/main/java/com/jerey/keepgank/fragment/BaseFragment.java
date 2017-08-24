package com.jerey.keepgank.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerey.loglib.LogTools;
import com.jerey.themelib.IDynamicNewView;
import com.jerey.themelib.attr.base.DynamicAttr;
import com.jerey.themelib.base.SkinBaseActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Xiamin on 2017/2/12.
 */

public abstract class BaseFragment extends RxFragment implements IDynamicNewView {


    protected View mContainView;
    private IDynamicNewView mIDynamicNewView;
    Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        LogTools.d("RxFragment onCreate : " + getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (mContainView == null) {
            mContainView = inflater.inflate(returnLayoutID(), container, false);
        }
        return mContainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        afterCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mIDynamicNewView = (IDynamicNewView) context;
        } catch (ClassCastException e) {
            LogTools.e("onAttach ClassCastException");
            e.printStackTrace();
            mIDynamicNewView = null;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTools.d("RxFragment onDestroy : " + getClass().getName());
    }

    protected abstract int returnLayoutID();

    protected abstract void afterCreate(Bundle savedInstanceState);

    public void showSnackbar(int stringRsId) {
        Snackbar.make(mContainView, stringRsId, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackbar(String string) {
        Snackbar.make(mContainView, string, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public final void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        if (mIDynamicNewView == null) {
            throw new RuntimeException("IDynamicNewView should be implements !");
        } else {
            mIDynamicNewView.dynamicAddView(view, pDAttrs);
        }
    }

    @Override
    public final void dynamicAddView(View view, String attrName, int attrValueResId) {
        mIDynamicNewView.dynamicAddView(view, attrName, attrValueResId);
    }

    @Override
    public final void dynamicAddFontView(TextView textView) {
        mIDynamicNewView.dynamicAddFontView(textView);
    }

    public final LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        LayoutInflater result = getActivity().getLayoutInflater();
        return result;
    }

    @Override
    public void onDestroyView() {
        removeAllView(getView());
        mUnbinder.unbind();
        super.onDestroyView();
    }

    public View getContainView() {
        return mContainView;
    }

    public void setContainView(View containView) {
        mContainView = containView;
    }

    private final void removeAllView(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                removeAllView(viewGroup.getChildAt(i));
            }
            removeViewInSkinInflaterFactory(v);
        } else {
            removeViewInSkinInflaterFactory(v);
        }
    }

    private final void removeViewInSkinInflaterFactory(View v) {
        if (getContext() instanceof SkinBaseActivity) {
            SkinBaseActivity skinBaseActivity = (SkinBaseActivity) getContext();
            //移除SkinInflaterFactory中的v
            skinBaseActivity.removeSkinView(v);
        }
    }

}
