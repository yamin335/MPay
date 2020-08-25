package com.qpay.customer.ui.profile_signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.qpay.customer.BR
import com.qpay.customer.R
import com.qpay.customer.databinding.ProfileSignInBinding
import com.qpay.customer.ui.NavigationHost
import com.qpay.customer.ui.common.BaseFragment
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.*

private const val REQUEST_TAKE_PHOTO_NID_FRONT = 1

class ProfileSignInFragment : BaseFragment<ProfileSignInBinding, ProfileSignInViewModel>() {


    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_profile_sign_in

    override val viewModel: ProfileSignInViewModel by viewModels { viewModelFactory }


    var navigationHost: NavigationHost? = null
    var rivNidFrontCaptureImage: String = ""
    var rivNidBackCaptureImage: String = ""
    private lateinit var cntx: Context

    val args: ProfileSignInFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationHost) {
            navigationHost = context
        }
        cntx = context
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateStatusBarBackgroundColor("#1E4356")

        val nidFrontData = args.NIDData.frontData
        val nidBackData = args.NIDData.backData

        viewDataBinding.nameField.setText(nidFrontData.name)
        viewDataBinding.birthDayField.setText(nidFrontData.birthDate)
        viewDataBinding.nidField.setText(nidFrontData.nidNo)
        viewDataBinding.addressField.setText(nidBackData.birthPlace)

        val host = navigationHost ?: return
        viewDataBinding.toolbar.apply {
            host.registerToolbarWithNavigation(this)
        }
        viewDataBinding.btnSubmit.setOnClickListener {
//            viewModel.registerNewUser(viewDataBinding.nameField.text.toString(), "",
//                "", "", "", "",
//                null, null, null).observe(viewLifecycleOwner, androidx.lifecycle.Observer {response ->
//                if (response != null) {
//                    // Just a primary navigation. Security check should have to implement later.
//                    navController().navigate(ProfileSignInFragmentDirections.actionProfileSignInFragmentToHome())
//                }
//            })
            navController.navigate(ProfileSignInFragmentDirections.actionProfileSignInFragmentToHome())
        }
        viewDataBinding.rivNidFrontImage.setOnClickListener {
            dispatchTakePictureIntent("rivNidFrontImage")
        }
        viewDataBinding.rivNidBackImage.setOnClickListener {
            dispatchTakePictureIntent("rivNidBackImage")
        }
    }

    private fun dispatchTakePictureIntent(captureFor: String) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(cntx.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(captureFor)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        cntx,
                        "com.qpay.customer.fileprovider",
                        it
                    )
                    Timber.d("received file uri : $photoURI")

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_NID_FRONT)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(captureFor: String): File {
        // Create an image file name
        // val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val timeStamp: String = Date().time.toString()
        val storageDir: File = cntx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        Timber.d("file directory : $storageDir")

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            if (captureFor == "rivNidFrontImage") {
                rivNidFrontCaptureImage = absolutePath
            } else if (captureFor == "rivNidBackImage") {
                rivNidBackCaptureImage = absolutePath
            }
            //galleryAddPic(absolutePath)
        }
    }

    // need this when file is saved in external storage public directory
   /* private fun galleryAddPic(absolutePath: String) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(absolutePath)
            mediaScanIntent.data = Uri.fromFile(f)
            cntx.sendBroadcast(mediaScanIntent)
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TAKE_PHOTO_NID_FRONT && resultCode == Activity.RESULT_OK) {


            //Intent data is returning empty

            /*data?.data?.let { uri ->
                viewDataBinding.rivNidFrontImage.setImageURI(uri)
            } ?: run {
                Timber.e("uri is null")
            }*/

           /* val imageBitmap = data?.extras?.get("data") as? Bitmap
            viewDataBinding.rivNidFrontImage.setImageBitmap(imageBitmap)*/


        }
    }

}