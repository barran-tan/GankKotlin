package com.barran.gank.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.util.Log

import com.barran.gank.app.App

/**
 * network
 *
 * Created by tanwei on 2017/9/27.
 */

object NetUtils {

    /**
     * Returns whether the network is available
     *
     * @return 网络是否可用
     * @see [类、类.方法、类.成员]
     */
    val isNetworkAvailable: Boolean
        get() = connectedNetworkInfo != null

    /**
     * 获取网络类型
     *
     * @return 网络类型
     * @see [类、类.方法、类.成员]
     */
    private val networkType: Int
        get() {
            val networkInfo = connectedNetworkInfo
            return networkInfo?.type ?: -1

        }

    private val connectedNetworkInfo: NetworkInfo?
        get() {
            try {
                val connectivity = App.appContext
                        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val info = connectivity.allNetworkInfo
                info?.filter { it.state == NetworkInfo.State.CONNECTED }?.forEach { return it }

            } catch (e: Exception) {
                Log.w("network", e.toString(), e)
            }

            return null
        }

    val isMobileConnected: Boolean
        get() = ConnectivityManager.TYPE_MOBILE == networkType

    val isWifiConnected: Boolean
        get() = ConnectivityManager.TYPE_WIFI == networkType

    val isEthernetConnected: Boolean
        get() = ConnectivityManager.TYPE_ETHERNET == networkType

    // 三种3G制式
    val connectionType: ConnectionType
        get() {

            if (!isNetworkAvailable)
                return ConnectionType.NONE

            if (isWifiConnected) {
                return ConnectionType.WIFI
            } else {
                val networkInfo = connectedNetworkInfo ?: return ConnectionType.UNKNOWN
                val nType = networkInfo.type

                when (nType) {
                    ConnectivityManager.TYPE_WIFI -> return ConnectionType.WIFI
                    ConnectivityManager.TYPE_ETHERNET -> return ConnectionType.ETHERNET
                    ConnectivityManager.TYPE_MOBILE -> {
                        val subType = networkInfo.subtype
                        when (subType) {
                            TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> return ConnectionType.G2
                            TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> return ConnectionType.G3
                            TelephonyManager.NETWORK_TYPE_LTE -> return ConnectionType.G4
                            else -> {
                                val subTypeName = networkInfo.subtypeName
                                return if (subTypeName.equals("TD-SCDMA", ignoreCase = true)
                                        || subTypeName.equals("WCDMA", ignoreCase = true)
                                        || subTypeName.equals("CDMA2000", ignoreCase = true)) {
                                    ConnectionType.G3
                                } else {
                                    ConnectionType.UNKNOWN
                                }
                            }
                        }
                    }
                    else -> return ConnectionType.UNKNOWN
                }
            }
        }

    enum class ConnectionType {
        NONE, UNKNOWN, ETHERNET, WIFI, G_UNKNOWN, G2, G3, G4
    }

}
