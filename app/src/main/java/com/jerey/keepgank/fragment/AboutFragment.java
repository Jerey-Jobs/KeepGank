package com.jerey.keepgank.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.jerey.keepgank.R;

/**
 * @author Xiamin
 * @date 2017/9/1
 */
public class AboutFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_preference);

    }

}
