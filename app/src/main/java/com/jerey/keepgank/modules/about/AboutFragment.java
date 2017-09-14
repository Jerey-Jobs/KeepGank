package com.jerey.keepgank.modules.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jerey.keepgank.R;

/**
 * @author Xiamin
 * @date 2017/9/1
 */
public class AboutFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_preference);

        /** 跳转Github */
        findPreference("go_github").setOnPreferenceClickListener(new Preference
                .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_github)));
                getActivity().startActivity(intent);
                return true;
            }
        });

        /** 跳转到我的博客 */
        findPreference("author_blog").setOnPreferenceClickListener(new Preference
                .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.my_blog)));
                getActivity().startActivity(intent);
                return true;
            }
        });

        findPreference("follow_me_on_github").setOnPreferenceClickListener(new Preference
                .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.my_github)));
                getActivity().startActivity(intent);
                return true;
            }
        });

        findPreference("follow_me_on_jianshu").setOnPreferenceClickListener(new Preference
                .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.my_jianshu)));
                getActivity().startActivity(intent);
                return true;
            }
        });

        findPreference("suggestions").setOnPreferenceClickListener(new Preference
                .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Uri uri = Uri.parse(getString(R.string.sendto));
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic));
                    intent.putExtra(Intent.EXTRA_TEXT,
                                    getString(R.string.device_model) + Build.MODEL + "\n"
                                            + getString(R.string.sdk_version) + Build.VERSION
                                            .RELEASE + "\n"
                                            + getString(R.string.version));
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
                return true;
            }
        });

        findPreference("open_source_license").setOnPreferenceClickListener(new Preference
                .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ARouter.getInstance()
                       .build("/activity/MyWebActivity")
                       .withString("title", "开源许可")
                       .withString("url", "file:///android_asset/html/license.html")
                       .withTransition(R.anim.in_from_right, 0)
                       .navigation(getActivity());
                return true;
            }
        });
    }


}
