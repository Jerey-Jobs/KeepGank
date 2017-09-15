package com.jerey.keepgank.modules.welcome;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jerey.keepgank.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Xiamin
 * @date 2017/9/14
 */
@Route(path = "/activity/WelcomeActivity")
public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.path_iv)
    ImageView mPathIv;
    @BindView(R.id.enter_btn)
    Button mEnterBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        Drawable drawable = mPathIv.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    @OnClick(R.id.enter_btn)
    public void onViewClicked() {
        ARouter.getInstance()
               .build("/activity/MainActivity")
               .navigation(this);
    }
}
