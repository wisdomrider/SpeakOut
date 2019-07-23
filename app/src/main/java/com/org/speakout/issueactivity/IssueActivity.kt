package com.org.speakout.issueactivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.org.speakout.Constants.AppConstance
import com.org.speakout.HomePageActivity
import com.org.speakout.R
import com.org.speakout.base.BaseActivity
import com.org.speakout.loginpage.LoginPage
import com.org.speakout.model.RegistrationModel
import java.io.ByteArrayOutputStream
import java.io.InputStream

class IssueActivity : BaseActivity() {
    internal lateinit var toolbar: Toolbar
    internal lateinit var imageView: ImageView
    internal lateinit var galaryButton: Button
    internal lateinit var lats: String
    internal lateinit var longs: String
    internal lateinit var spinner: Spinner
    internal lateinit var camarabutton: Button
    internal lateinit var urlString: String;
    internal var issueList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue)
        camarabutton = findViewById(R.id.button_upload_camara)
        val r = sqliteClosedHelper.getAll(LoginPage.Tag(""))
        for (x in r) {
            issueList.add(x.name);
        }
        lats = intent.getStringExtra(AppConstance.LATS)
        longs = intent.getStringExtra(AppConstance.LONG)
        spinner = findViewById(R.id.issue_list)
        spinner.adapter = ArrayAdapter<String>(this, R.layout.custom_spinner, issueList)
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
            registrationModel.desc = wisdom.editText(R.id.editTextDescription).text.toString()
            registrationModel.photo = ""
            registrationModel.tag = spinner.selectedItem.toString()
            val mytoken = preferences.getString(AppConstance.TOKEN, "notes")
            Log.e("MyToken", mytoken)
            api.postProblem(registrationModel).get("Upding Problem on Server", object : Do {
                override fun <T> Do(body: T?) {
                   /* val intent = Intent(this@IssueActivity, HomePageActivity::class.java)
                    startActivity(intent)
                   */
                    onBackPressed()
                    wisdom.toast("Sucessfully update your problem")
                }
            });
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
                    val baos = ByteArrayOutputStream()
                    mySelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    var uploadTask = storage.getReference("${System.currentTimeMillis()}.png").putBytes(data)
                    showProgessBar("uploading")
                    uploadTask.addOnFailureListener {
                    }.addOnSuccessListener { s ->
                        imageView.visibility
                        closeProgressBar()
                        Log.e("slog", s.toString())
                        //urlString = s.metadata.u
                        //generatedFilePath = downloadUri.toString()
                        //val downloadUri = s.getMetadata().getDownloadUrl();
                        imageView.setImageURI(selectedImage)
                        imageView.visibility = View.VISIBLE
                    }


                } catch (e: Exception) {
                    Log.e("Exception", e.message)
                }
            }
            CAMARA_REQUEST -> try {
                val bitmap = data!!.extras!!.get("data") as Bitmap
                val baos = ByteArrayOutputStream()

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                val data = baos.toByteArray()
                var uploadTask = storage.getReference("${System.currentTimeMillis()}.png").putBytes(data)
                showProgessBar("Uploading Image")
                uploadTask.addOnFailureListener {
                }.addOnSuccessListener { s ->
                    imageView.visibility = View.VISIBLE
                    Log.e("slog", s.toString())
                    imageView.setImageBitmap(bitmap)
                    closeProgressBar()
                }

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
