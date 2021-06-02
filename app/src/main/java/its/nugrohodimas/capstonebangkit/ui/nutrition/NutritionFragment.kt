package its.nugrohodimas.capstonebangkit.ui.nutrition

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import its.nugrohodimas.capstonebangkit.BuildConfig
import its.nugrohodimas.capstonebangkit.databinding.FragmentNutritionBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger


@Suppress("DEPRECATION")
class NutritionFragment : Fragment() {

    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_PICK_PHOTO = 2
        private val CAMERA_PIC_REQUEST = 1111

        private val TAG = "Nutrition Fragment"

        private val CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100

        val MEDIA_TYPE_IMAGE = 1
        val IMAGE_DIRECTORY_NAME = "Android File Upload"

        /**
         * returning image / video
         */
        private fun getOutputMediaFile(type: Int): File? {

            // External sdcard location
            val mediaStorageDir = File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME
            )

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(
                        TAG, "Oops! Failed create "
                                + IMAGE_DIRECTORY_NAME + " directory"
                    )
                    return null
                }
            }

            // Create a media file name
            val timeStamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
            ).format(Date())
            val mediaFile: File
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = File(
                    mediaStorageDir.path + File.separator
                            + "IMG_" + ".jpg"
                )
            } else {
                return null
            }

            return mediaFile
        }
    }

    private lateinit var nutritionViewModel: NutritionViewModel
    private var _binding: FragmentNutritionBinding? = null

    private val mMediaUri: Uri? = null
    private var fileUri: Uri? = null
    private var mediaPath: String? = null
    private val btnCapturePicture: Button? = null
    private var mImageFileLocation = ""
    private lateinit var pDialog: ProgressDialog
    private var postPath: String? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nutritionViewModel =
            ViewModelProvider(this).get(NutritionViewModel::class.java)

        _binding = FragmentNutritionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.btnRmvImageDetection.isEnabled = false
            binding.btnTakePhotoDetection.isEnabled = false
            binding.btnUploadPhotoDetection.isEnabled = false
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0
            )
        } else {
            binding.btnTakePhotoDetection.isEnabled = true
            binding.btnRmvImageDetection.isEnabled = true
            binding.btnUploadPhotoDetection.isEnabled = true
        }

        binding.btnTakePhotoDetection.setOnClickListener {
            if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
                val callCameraApplicationIntent = Intent()
                callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE

                // We give some instruction to the intent to save the image
                var photoFile: File? = null

                try {
                    // If the createImageFile will be successful, the photo file will have the address of the file
                    photoFile = createImageFile()
                    // Here we call the function that will try to catch the exception made by the throw function
                } catch (e: IOException) {
                    Logger.getAnonymousLogger().info("Exception error in generating the file")
                    e.printStackTrace()
                }

                // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
                val outputUri = FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile!!
                )
                callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

                // The following is a new line with a trying attempt
                callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)

                Logger.getAnonymousLogger().info("Calling the camera App by intent")
                // The following strings calls the camera app and wait for his file in return.
                startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST)
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

                // start the image capture Intent
                startActivityForResult(intent, CAMERA_PIC_REQUEST)

            }
        }

        binding.btnUploadPhotoDetection.setOnClickListener {
//            if (postPath == null || postPath == "") {
//                Toast.makeText(context, "please select an image ", Toast.LENGTH_LONG).show()
//                return
//            } else {
//                showDialog()
//
//                // Map is used to multipart the file using okhttp3.RequestBody
//                val map = HashMap<String, RequestBody>()
//                val file = File(postPath!!)
//
//                // Parsing any Media type file
//                val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
//                map.put("file\"; filename=\"" + file.name + "\"", requestBody)
//                val getResponse = AppConfig.getRetrofit().create(ApiConfig::class.java)
//                val call = getResponse.upload("token", map)
//                call.enqueue(object : Callback<ServerResponse> {
//                    override fun onResponse(
//                        call: Call<ServerResponse>,
//                        response: Response<ServerResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            if (response.body() != null) {
//                                hidepDialog()
//                                val serverResponse = response.body()
//                                Toast.makeText(
//                                    applicationContext,
//                                    serverResponse.message,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//
//                            }
//                        } else {
//                            hidepDialog()
//                            Toast.makeText(
//                                applicationContext,
//                                "problem uploading image",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
//                        hidepDialog()
//                        Log.v("Response gotten is", t.message)
//                    }
//                })
//            }
        }

        binding.btnRmvImageDetection.setOnClickListener {
            binding.imgDetectionNutrition.setImageResource(android.R.color.transparent)
        }

        Log.d("Test", mImageFileLocation)

        return root
    }

    private fun showDialog() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == CAMERA_PIC_REQUEST) {
//            var pic: Bitmap? = data?.getParcelableExtra<Bitmap>("data")
//            binding.imgDetectionNutrition.setImageBitmap(pic)
//        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Get the Image from data
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = activity?.contentResolver?.query(
                        selectedImage!!,
                        filePathColumn,
                        null,
                        null,
                        null
                    )
                    assert(cursor != null)
                    cursor!!.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    // Set the Image in ImageView for Previewing the Media
                    binding.imgDetectionNutrition.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()


                    postPath = mediaPath
                }


            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {

                    Glide.with(this).load(mImageFileLocation).into(binding.imgDetectionNutrition)
                    postPath = mImageFileLocation

                } else {
                    Glide.with(this).load(fileUri).into(binding.imgDetectionNutrition)
                    postPath = fileUri!!.path

                }

            }

        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(context, "Sorry, there was an error!", Toast.LENGTH_LONG).show()
        }
    }

    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    private fun createImageFile(): File {
        Logger.getAnonymousLogger().info("Generating the image - method started")

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
        val imageFileName = "IMAGE_" + timeStamp
        // Here we specify the environment location and the exact path where we want to save the so-created file
        val storageDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app")
        Logger.getAnonymousLogger().info("Storage directory set")

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir()

        // Here we create the file using a prefix, a suffix and a directory
        val image = File(storageDirectory, imageFileName + ".jpg")
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set")

        mImageFileLocation = image.absolutePath
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                binding.btnTakePhotoDetection.isEnabled = true
                binding.btnRmvImageDetection.isEnabled = true
                binding.btnUploadPhotoDetection.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}