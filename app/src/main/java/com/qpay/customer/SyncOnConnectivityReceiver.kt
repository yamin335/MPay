package com.qpay.customer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.Nullable
import com.qpay.customer.util.CommonUtils
import com.qpay.customer.util.NetworkUtils
import timber.log.Timber


class SyncOnConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(@Nullable context: Context?, intent: Intent) {
        Timber.d("triggering on connectivity change")
        if (context != null) {
            if (!NetworkUtils.isNetworkConnected(context)) {
                CommonUtils.fireErrorMessageEvent(
                        error = context.getString(R.string.no_internet_error),
                        showInAlert = false
                )
            }
        }
    }
}
