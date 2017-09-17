package com.jerey.keepgank.modules.photopreview;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jerey.keepgank.R;
import com.jerey.keepgank.widget.imgpreview.SmoothImageView;
import com.jerey.loglib.LogTools;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author Xiamin
 * @date 2017/9/17
 */
public class PhotoPreviewFragment extends Fragment {
    public static final String KEY_START_RECT = "start_rect";
    public static final String KEY_TRANS_PHOTO = "is_trans_photo";
    public static final String KEY_SOING_FINLING = "isSingleFling";
    public static final String KEY_URL = "key_url";


    SmoothImageView mPhotoView;
    ProgressBar mLoading;
    RelativeLayout mRootView;

    protected View mContainView;
    private String url;

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
        mPhotoView = mContainView.findViewById(R.id.photoView);
        mRootView = mContainView.findViewById(R.id.rootView);
        mLoading = mContainView.findViewById(R.id.loading);
        afterCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected int returnLayoutID() {
        return R.layout.fragment_photo_preview;
    }

    protected void afterCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            url = bundle.getString(KEY_URL);
            LogTools.d("mPhotoView:" + mPhotoView);
            LogTools.d("url:" + url);
            Rect rect = bundle.getParcelable(KEY_START_RECT);
            if (rect != null) {
                mPhotoView.setThumbRect(rect);
            }
            mPhotoView.setTag(url);
            Glide.with(this)
                 .load(url)
                 .asBitmap()
                 .centerCrop()
                 .error(R.drawable.img_failed)
                 .into(new SimpleTarget<Bitmap>() {
                     @Override
                     public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                             glideAnimation) {
                         if (mPhotoView.getTag().toString().equals(url)) {
                             mLoading.setVisibility(View.GONE);
                             mPhotoView.setImageBitmap(resource);
                         }
                     }

                     @Override
                     public void onLoadFailed(Exception e, Drawable errorDrawable) {
                         super.onLoadFailed(e, errorDrawable);
                         if (errorDrawable != null) {
                             mPhotoView.setImageDrawable(errorDrawable);
                         }
                     }
                 });
            mPhotoView.setMinimumScale(1f);
            mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (mPhotoView.checkMinScale()) {
                        ((PhotoPreviewActivity) getActivity()).transformOut();
                    }
                }
            });
            /**
             * 设置背景色跟随拖动透明度变换
             */
            mPhotoView.setAlphaChangeListener(new SmoothImageView.OnAlphaChangeListener() {
                @Override
                public void onAlphaChange(int alpha) {
                    mRootView.setBackgroundColor(getColorWithAlpha(alpha / 255f, Color.BLACK));
                }
            });
            /**
             * 设置拖动结束监听器
             */
            mPhotoView.setTransformOutListener(new SmoothImageView.OnTransformOutListener() {
                @Override
                public void onTransformOut() {
                    if (mPhotoView.checkMinScale()) {
                        ((PhotoPreviewActivity) getActivity()).transformOut();
                    }
                }
            });
        }
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }


    public void transformIn() {
        mPhotoView.transformIn(new SmoothImageView.onTransformListener() {
            @Override
            public void onTransformCompleted(SmoothImageView.Status status) {
                mRootView.setBackgroundColor(Color.BLACK);
            }
        });
    }

    public void transformOut(SmoothImageView.onTransformListener listener) {
        mPhotoView.transformOut(listener);
    }

    public void changeBg(int color) {
        mRootView.setBackgroundColor(color);
    }

    public static PhotoPreviewFragment newInstance(String url, Rect rect) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        bundle.putParcelable(KEY_START_RECT, rect);
        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
