package com.dmitri.cardpair

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.onesignal.OneSignal

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initAF()
        initOneSignal()
    }

    private fun initAF() {
        val conversionDataListener  = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                Log.d("AppsFlyerApp", "onAttributionSuccess")
                AFConversionData.instance.conversionData = data
            }
            override fun onConversionDataFail(error: String?) {
                Log.e("AppsFlyerApp", "error onAttributionFailure :  $error")
            }
            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                // Must be overriden to satisfy the AppsFlyerConversionListener interface.
                // Business logic goes here when UDL is not implemented.
                data?.map {
                    Log.d("AppsFlyerApp", "onAppOpen_attribute: ${it.key} = ${it.value}")
                }
            }
            override fun onAttributionFailure(error: String?) {
                // Must be overriden to satisfy the AppsFlyerConversionListener interface.
                // Business logic goes here when UDL is not implemented.
                Log.e("AppsFlyerApp", "error onAttributionFailure :  $error")
            }
        }

        AppsFlyerLib.getInstance().init(resources.getString(R.string.af_dev_key), conversionDataListener, this)
        AppsFlyerLib.getInstance().setDebugLog(true);
        AppsFlyerLib.getInstance().start(this)
    }

    private fun initOneSignal() {
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(resources.getString(R.string.onesignal_app_id));

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        // FIXME ask about this
        OneSignal.promptForPushNotifications();
    }
}