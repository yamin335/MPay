package com.qpay.customer.ui

import android.annotation.SuppressLint
import android.content.*
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.security.ProviderInstaller
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.qpay.customer.BuildConfig
import com.qpay.customer.R
import com.qpay.customer.SyncOnConnectivityReceiver
import com.qpay.customer.databinding.MainActivityBinding
import com.qpay.customer.util.CommonUtils
import com.qpay.customer.util.NetworkUtils
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import java.io.File

class MainActivity  : DaggerAppCompatActivity(),
    ProviderInstaller.ProviderInstallListener,
    NavigationHost {

    lateinit var binding: MainActivityBinding

    private val navController: NavController
        get() = findNavController(R.id.container)

    private var currentNavController: LiveData<NavController>? = null

    private val connectivityReceiver by lazy {
        SyncOnConnectivityReceiver()
    }

    private val filter = IntentFilter()

    private var retryProviderInstall: Boolean = false

    lateinit var appUpdateManager: AppUpdateManager

    /*fragment ids which doesn't need bottom navigation or drawer*/
    private val fragmentArrayWithoutBottomNav = intArrayOf(
        R.id.splashFragment,
        R.id.viewPagerFragment,
        R.id.signInFragment,
        R.id.otpSignInFragment,
        R.id.pinNumberFragment,
        R.id.profileSignInFragment,
        R.id.preOnBoardFragment,
        R.id.howWorksFragment,
        R.id.registrationFragment,
        R.id.otpFragment,
        R.id.touFragment,
        R.id.setupFragment,
        R.id.setupCompleteFragment,
        R.id.chapterListFragment,
        R.id.videoPlayFragment,
        R.id.cameraFragment,
        R.id.NIDScanCameraXFragment
    )

    private val topLevelDestinations = setOf(
        R.id.homeFragment,
        R.id.profilesFragment,
        R.id.settingsFragment
    )

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(topLevelDestinations)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //EventBus.getDefault().register(this)
        ProviderInstaller.installIfNeededAsync(this, this)
        setTheme(R.style.AppTheme)
        appUpdaterSetUp()
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.lifecycleOwner = this


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            CommonUtils.hideKeyboard(this@MainActivity)
            val hasBottomNav = destination.id !in fragmentArrayWithoutBottomNav
            binding.showBottomNav = hasBottomNav
            if (hasBottomNav) {
                setupBottomNav(destination.id)
                /*set title explicitly to avoid title error on locale changes*/
                val label = destination.label
                if (!label.isNullOrBlank()) title = label
            }
            if (destination.id !in fragmentArrayWithoutBottomNav) {

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Timber.d("using registerNetworkCallback")
            createChangeConnectivityMonitor()
            filter.addAction(BuildConfig.APPLICATION_ID + ".CONNECTIVITY_CHANGE")
        } else {
            Timber.d("using old broadcast receiver")
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }
    }


    private fun setupBottomNav(id: Int) {
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Setting Up ActionBar with Navigation Controller
        //Prevent Back arrow in action bar of top level destination we can use appbar configuration for back arrow issue
        //setupActionBarWithNavController method use for change the action bar title when bottom nav item changed
        // setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() ?: false
    }

    private fun appUpdaterSetUp() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            // Checks that the platform will allow the specified type of update.
            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        REQUEST_APP_UPDATE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createChangeConnectivityMonitor() {
        val intent = Intent(BuildConfig.APPLICATION_ID + ".CONNECTIVITY_CHANGE")
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                /**
                 * @param network
                 */
                override fun onAvailable(network: Network) {
                    Timber.d("On available network")
                    sendBroadcast(intent)
                }

                /**
                 * @param network
                 */
                override fun onLost(network: Network) {
                    Timber.d("On not available network")
                    sendBroadcast(intent)
                }
            })

    }


    var isPaused = false
    override fun onPause() {
        super.onPause()
        isPaused = true
        unregisterReceiver(connectivityReceiver)
        //unregisterReceiver(callAnsweredBroadCastReceiver)
    }

    override fun onDestroy() {
        //EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        isPaused = false
        if (!NetworkUtils.isNetworkConnected(this@MainActivity)) {
            //CommonUtils.showErrorSnack(findViewById(R.id.container), R.string.no_internet_error)
        }
        registerReceiver(connectivityReceiver, filter)
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            REQUEST_APP_UPDATE
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }
    }

    /**
     * On resume, check to see if we flagged that we need to reinstall the
     * provider.
     */
    override fun onPostResume() {
        super.onPostResume()
        if (retryProviderInstall) {
            // We can now safely retry installation.
            ProviderInstaller.installIfNeededAsync(this, this)
        }
        retryProviderInstall = false
    }

    /**
     * This method is called if updating fails; the error code indicates
     * whether the error is recoverable.
     */
    override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
        GoogleApiAvailability.getInstance().apply {
            if (isUserResolvableError(errorCode)) {
                // Recoverable error. Show a dialog prompting the user to
                // install/update/enable Google Play services.
                showErrorDialogFragment(this@MainActivity, errorCode, ERROR_DIALOG_REQUEST_CODE) {
                    // The user chose not to take the recovery action
                    onProviderInstallerNotAvailable()
                }
            } else {
                onProviderInstallerNotAvailable()
            }
        }
    }

    private fun onProviderInstallerNotAvailable() {
        // This is reached if the provider cannot be updated for some reason.
        // App should consider all HTTP com munication to be vulnerable, and take
        // appropriate action.
    }

    /**
     * This method is only called if the provider is successfully updated
     * (or is already up-to-date).
     */

    override fun onProviderInstalled() {
        // Provider is up-to-date, app can make secure network calls.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ERROR_DIALOG_REQUEST_CODE -> // Adding a fragment via GoogleApiAvailability.showErrorDialogFragment
                // before the instance state is restored throws an error. So instead,
                // set a flag here, which will cause the fragment to delay until
                // onPostResume.
                retryProviderInstall = true
        }
    }

    companion object {
        const val REQUEST_APP_UPDATE = 557
        private const val ERROR_DIALOG_REQUEST_CODE = 1

        private const val NAV_ID_NONE = -1

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}