package com.jerey.keepgank.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.ClipboardManager;

import java.util.List;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class SystemUtils {
    private SystemUtils() {

    }

    public static void share(Context context, String text, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void copyText(Context context, String text) {
        ClipboardManager c = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        c.setText(text);
    }
    public static void openUrlByBrowser(Context context,String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}