package com.allan.adbtool.wifiAdb;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiIp
{
  
  public static String getWifiIp(Context content)
  {
    WifiManager wm = (WifiManager) content.getSystemService("wifi");
    if (!wm.isWifiEnabled()) {
        wm.setWifiEnabled(true);
    }
    return intToIp(wm.getConnectionInfo().getIpAddress());
  }
  
  private static String intToIp(int paramInt)
  {
      return (paramInt & 0xFF) + "." + (paramInt >> 8 & 0xFF) + "." + (paramInt >> 16 & 0xFF) + "." + (paramInt >> 24 & 0xFF);
  }
}
