package com.org.speakout.issueactivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.org.speakout.Constants.AppConstance
import com.org.speakout.R
import com.org.speakout.base.BaseActivity
import com.org.speakout.model.RegistrationModel
import java.io.InputStream
import java.util.*

class IssueActivity : BaseActivity() {
    internal lateinit var toolbar: Toolbar
    internal lateinit var imageView: ImageView
    internal lateinit var galaryButton: Button
    internal lateinit var lats: String
    internal lateinit var longs: String
    internal lateinit var spinner: Spinner
    internal lateinit var camarabutton: Button
    internal var issueList: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue)
        camarabutton = findViewById(R.id.button_upload_camara)
        /*for (Tags tags : sqliteClosedHelper.getAll(new Tags())) {
            issueList.add(tags.getName());
         }*/
        lats = intent.getStringExtra(AppConstance.LATS)
        longs = intent.getStringExtra(AppConstance.LONG)
        spinner = findViewById(R.id.issue_list)
        //  spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_spinner, issueList));
        galaryButton = findViewById(R.id.btn_upload_galary)
        toolbar = findViewById(R.id.toolbar)
        imageView = findViewById(R.id.problem_image)
        camarabutton.setOnClickListener {
            Toast.makeText(this@IssueActivity, "Hello", Toast.LENGTH_SHORT).show()
            checkCamaraPermision()
        }
        wisdom.button(R.id.btn_upload_galary).setOnClickListener { checkGalaryPermision() }

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        wisdom.button(R.id.btn_submit).setOnClickListener {
            val registrationModel = RegistrationModel()
            registrationModel.location = "$lats:$longs"
            registrationModel.title = wisdom.editText(R.id.editTextTitle).text.toString()
            registrationModel.desc = wisdom.editText(R.id.editTextDescription).toString()
            registrationModel.photo = ""
            registrationModel.tag = ""
        }
    }

    protected fun checkGalaryPermision() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), BaseActivity.Companion.GALLERY_REQUEST)
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, BaseActivity.Companion.GALLERY_REQUEST)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    protected fun checkCamaraPermision() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMARA_REQUEST)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMARA_REQUEST)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            GALLERY_REQUEST ->
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent = Intent(Intent.ACTION_PICK)
                    galleryIntent.type = "image/*"
                    startActivityForResult(galleryIntent, BaseActivity.Companion.GALLERY_REQUEST)
                } else {
                    Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show()
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }

            CAMARA_REQUEST -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMARA_REQUEST)
            } else {
                Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            BaseActivity.Companion.GALLERY_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data!!.data
                var inputStream: InputStream? = null
                try {
                    inputStream = contentResolver.openInputStream(selectedImage!!)
                    val mySelectedImage = BitmapFactory.decodeStream(inputStream)
                    imageView.setImageURI(selectedImage)

                } catch (e: Exception) {
                    Log.e("Exception", e.message)
                }

            }
            CAMARA_REQUEST -> try {
                val bitmap = data!!.extras!!.get("data") as Bitmap
                imageView.setImageBitmap(bitmap)
                //imageView.setImageURI(bitmap);

            } catch (e: Exception) {

            }

        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun openIssueActivity() {
        super.openIssueActivity()
        disableKeyboardAtFirst()
    }
}
