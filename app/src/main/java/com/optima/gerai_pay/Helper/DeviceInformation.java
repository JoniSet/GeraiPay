package com.optima.gerai_pay.Helper;

import static android.content.Context.WIFI_SERVICE;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;

public class DeviceInformation {
    public String getIP(Context context){
        String info = "";
        WifiManager wifiManager = (WifiManager)  context.getApplicationContext().getSystemService(WIFI_SERVICE);
        info        = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        return info;
    }

    public String getDevice(Context context){
        String info = "";
        info        = Build.BRAND;
        return info;
    }

    public String getType(Context context){
        String info = "";
        info        = Build.MODEL ;
        return info;
    }

    public String getVer(Context context){
        String info = "";
        WifiManager wifiManager = (WifiManager)  context.getApplicationContext().getSystemService(WIFI_SERVICE);
        info        =  Build.VERSION.RELEASE;
        return info;
    }
}
