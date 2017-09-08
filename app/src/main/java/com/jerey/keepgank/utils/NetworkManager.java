package com.jerey.keepgank.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * NetworkManager
 * <p>
 * Please init NetworkManager in your Application
 * <pre>
 *      NetworkManager.init(this);
 *
 *
 * </pre>
 * </p>
 * @author xiamin
 */
public final class NetworkManager {
    public static final String TAG = NetworkManager.class.getSimpleName();

    private static Context app;

    private static boolean isWifiConnect;
    private static boolean isCellularConnect;

    public static enum NetworkType {
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    private NetworkManager() {
        throw new IllegalArgumentException("can't construct");
    }

    public static void init(Context context) {
        app = context;
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getApp().registerReceiver(NetworkChange.getInstance(), filter);
        isWifiConnect = getWifiConnected();
        isCellularConnect = getDataEnabled();
    }

    public static void shutdown() {
        getApp().unregisterReceiver(NetworkChange.getInstance());
    }

    public static boolean isWifiConnect() {
        return isWifiConnect;
    }

    public static boolean isCellularConnect() {
        return isCellularConnect;
    }

    public static boolean isNetWorkConnect() {
        return isWifiConnect || isCellularConnect;
    }


    public static Context getApp() {
        if (app == null) {
            throw new IllegalStateException("NetworkManager hasn't init");
        }
        return app;
    }

    /**
     * 打开网络设置界面
     */
    public static void openWirelessSettings() {
        getApp().startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                                       .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 获取活动网络信息
     * <p>需添加权限 {@code <uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>}</p>
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) getApp().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
    }

    /**
     * 判断网络是否连接
     * <p>需添加权限 {@code <uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>}</p>
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean getConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 判断网络是否可用
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需要异步ping，如果ping不通就说明网络不可用</p>
     * <p>ping的ip为阿里巴巴公共ip：223.5.5.5</p>
     * @return {@code true}: 可用<br>{@code false}: 不可用
     */
    public static boolean isAvailableByPing() {
        //   return isAvailableByPing(null);
        return true;
    }

    /**
     * 判断网络是否可用
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需要异步ping，如果ping不通就说明网络不可用</p>
     * @param ip ip地址（自己服务器ip），如果为空，ip为阿里巴巴公共ip
     * @return {@code true}: 可用<br>{@code false}: 不可用
     */
    public static boolean isAvailableByPing(String ip) {
        if (ip == null || ip.length() <= 0) {
            ip = "223.5.5.5";// 阿里巴巴公共ip
        }
        ShellUtils.CommandResult result = ShellUtils.execCmd(String.format("ping -c 1 %s",
                                                                           ip),
                                                             false);
        boolean ret = result.result == 0;
        if (result.errorMsg != null) {
            Log.d("NetworkManager", "isAvailableByPing() called" + result.errorMsg);
        }
        if (result.successMsg != null) {
            Log.d("NetworkManager", "isAvailableByPing() called" + result.successMsg);
        }
        return ret;
    }

    /**
     * 判断移动数据是否打开
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean getDataEnabled() {
        try {
            TelephonyManager tm = (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                return (boolean) getMobileDataEnabledMethod.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开或关闭移动数据
     * <p>需系统应用 需添加权限{@code <uses-permission android:name="android.permission
     * .MODIFY_PHONE_STATE"/>}</p>
     * @param enabled {@code true}: 打开<br>{@code false}: 关闭
     */
    public static void setDataEnabled(final boolean enabled) {
        try {
            TelephonyManager tm = (TelephonyManager) getApp()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = tm.getClass()
                                                  .getDeclaredMethod("setDataEnabled", boolean
                                                          .class);
            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(tm, enabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络是否是4G
     * <p>需添加权限 {@code <uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>}</p>
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean is4G() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager
                .NETWORK_TYPE_LTE;
    }

    /**
     * 判断wifi是否打开
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager) getApp()
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 打开或关闭wifi
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>}</p>
     * @param enabled {@code true}: 打开<br>{@code false}: 关闭
     */
    public static void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager) getApp()
                .getSystemService(Context.WIFI_SERVICE);
        if (enabled) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        }
    }

    /**
     * 判断wifi是否连接状态
     * <p>需添加权限 {@code <uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>}</p>
     * @return {@code true}: 连接<br>{@code false}: 未连接
     */
    public static boolean getWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断wifi数据是否可用
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isWifiAvailable() {
        return getWifiEnabled() && isAvailableByPing();
    }

    /**
     * 获取网络运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     * @return 运营商名称
     */
    public static String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN = 18;

    /**
     * 获取当前网络类型
     * <p>需添加权限 {@code <uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>}</p>
     * @return 网络类型
     * <ul>
     * <li>{@link NetworkManager.NetworkType#NETWORK_WIFI   } </li>
     * <li>{@link NetworkManager.NetworkType#NETWORK_4G     } </li>
     * <li>{@link NetworkManager.NetworkType#NETWORK_3G     } </li>
     * <li>{@link NetworkManager.NetworkType#NETWORK_2G     } </li>
     * <li>{@link NetworkManager.NetworkType#NETWORK_UNKNOWN} </li>
     * <li>{@link NetworkManager.NetworkType#NETWORK_NO     } </li>
     * </ul>
     */
    public static NetworkType getNetworkType() {
        NetworkType netType = NetworkType.NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {

                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NetworkType.NETWORK_2G;
                        break;

                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NetworkType.NETWORK_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NetworkType.NETWORK_4G;
                        break;
                    default:

                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NetworkType.NETWORK_3G;
                        } else {
                            netType = NetworkType.NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NetworkType.NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

    /**
     * 获取IP地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * @param useIPv4 是否用IPv4
     * @return IP地址
     */
    public static String getIPAddress(final boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis
                    .hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress
                                        .substring(0, index)
                                        .toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取域名ip地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * @param domain 域名
     * @return ip地址
     */
    public static String getDomainAddress(final String domain) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }


    public interface OnNetWorkChangeListener {
        void onNetWorkChanged(NetworkType currentstate);
    }

    public static class NetworkChange extends BroadcastReceiver {

        public List<OnNetWorkChangeListener> onNetWorkChange = new ArrayList<>();
        private static NetworkChange networkChange;

        public static NetworkChange getInstance() {
            if (networkChange == null) {
                networkChange = new NetworkChange();
            }
            return networkChange;
        }

        /**
         * 增加网络变化监听回调对象
         * 如果设置多个回调，请务必不要设置相同名字的OnNetWorkChange对象，否则会无效
         * @param onNetWorkChange 回调对象
         */
        public void registerOnNetWorkChange(OnNetWorkChangeListener onNetWorkChange) {
            if (this.onNetWorkChange.contains(onNetWorkChange)) {
                return;
            }
            this.onNetWorkChange.add(onNetWorkChange);
        }

        /**
         * 取消网络变化监听监听回调
         * @param onNetWorkChange 回调对象
         */
        public void unregisterOnNetWorkChange(OnNetWorkChangeListener onNetWorkChange) {
            if (this.onNetWorkChange.contains(onNetWorkChange)) {
                this.onNetWorkChange.remove(onNetWorkChange);
            }
        }

        /**
         * 触发网络状态监听回调
         * @param nowStatus 当前网络状态
         */
        private void setChange(NetworkType nowStatus) {
            for (OnNetWorkChangeListener change : onNetWorkChange) {
                change.onNetWorkChanged(nowStatus);
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager
                                                                                .TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager
                                                                                 .TYPE_WIFI);
            if (onNetWorkChange == null) {
                //当没有设置回调的时候，什么都不做
                return;
            }
            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                Log.i("通知", "网络不可以用");
                setChange(NetworkType.NETWORK_NO);
                isWifiConnect = false;
                isCellularConnect = false;
            } else if (wifiNetInfo.isConnected()) {
                Log.i("通知", "Wifi网络可用");
                isWifiConnect = true;
                setChange(NetworkType.NETWORK_WIFI);
            } else if (mobNetInfo.isConnected()) {
                Log.i("通知", "仅移动网络可用");
                isCellularConnect = true;
                setChange(NetworkType.NETWORK_4G);
            }
        }
    }
}