package com.org.speakout.base

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.org.speakout.Constants.AppConstance
import com.org.speakout.RetrofitInterface
import com.org.speakout.Validator
import com.org.speakout.loginpage.LoginPage
import com.org.speakout.model.Tags
import com.wisdomrider.Utils.Preferences
import com.wisdomrider.sqliteclosedhelper.SqliteClosedHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

open class BaseActivity : com.wisdomrider.Activities.BaseActivity() {
    protected lateinit var preferences: Preferences
    lateinit var sqliteClosedHelper: SqliteClosedHelper
    protected var validator = Validator()
    lateinit var retrofit: Retrofit
    lateinit var api: RetrofitInterface
    var progess: ProgressDialog? = null
    lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = Preferences(this, AppConstance.APP_NAME, 0)
        storage= FirebaseStorage.getInstance()
        sqliteClosedHelper = SqliteClosedHelper(this, AppConstance.APP_NAME)
        disableKeyboardAtFirst()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
        validator = Validator()
        sqliteClosedHelper = SqliteClosedHelper(this, "NOTES")
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + preferences.getString(AppConstance.TOKEN, "notes"))
                    .method(original.method(), original.body())
                    .build()
            chain.proceed(request)
        }
        httpClient.addInterceptor(logging)
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        api = retrofit.create(RetrofitInterface::class.java)
    }


   public fun closeProgressBar() {
        if (progess != null)
            progess!!.cancel()
    }

    public fun showProgessBar(cont: String) {
        if (progess == null)
            progess = ProgressDialog(this)
        progess!!.setCancelable(false)
        progess!!.setMessage("Loading...")
        progess!!.setTitle(cont)
        progess!!.show()
    }

    public interface Do {
        fun <T> Do(body: T?)
    }

    fun showAlert(message: String) {
        val alert = AlertDialog.Builder(this)
        alert.setMessage(message)
        alert.show()
    }

    fun <T> Call<T>.get(
            what: String,
            after: Do? = null,
            messageFor406: String = "Authentication Error",
            messageFor400: String = "Unable to connect to server please try again !",
            onBack: Boolean = false,
            on406: Do? = null,
            onNetworkError: Do? = null
    ): Any {

        if (!what.isEmpty())
            showProgessBar(what)
        this.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                closeProgressBar()
                if (onNetworkError != null) {
                    onNetworkError.Do(null)
                    return
                }
                showAlert("Unable to connect to the server.")
                if (onBack) {
                    wisdom.toast("Please check your internet connection.")
                    onBackPressed()
                }

            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                closeProgressBar()
                when (response.code()) {
                    200 -> if (after != null) after.Do(response.body())
                    406 -> {
                        if (on406 == null) showAlert(messageFor406)
                        else on406.Do(null)
                    }
                    400 -> showAlert(messageFor400)
                    401 -> {
                        if (!what.isEmpty())
                            showAlert(messageFor406)
//                        else
//                            startActivity(Intent(applicationContext, TrackerPage::class.java))
                    }
                    500 -> showAlert(messageFor400)
                    123 -> showAlert(response.errorBody()!!.string())
                    else -> showAlert("Please check your internet connection.")
                }

            }
        })


        return this
    }

    protected fun disableKeyboardAtFirst() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    protected fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                AlertDialog.Builder(this)
                        .setTitle("location permision")
                        .setMessage("We need your location")
                        .setPositiveButton("OK") { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(this@BaseActivity,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    MY_PERMISSIONS_REQUEST_LOCATION)
                        }
                        .create()
                        .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }

    fun createTables() {
        sqliteClosedHelper.createTable(LoginPage.Tag(""))
        sqliteClosedHelper.createTable(LoginPage.Problem("","", "",""))
    }


    open fun openIssueActivity() {

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        openIssueActivity()
                        //Request location updates:

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }
        }
    }

    protected open fun insertintoTables(tags: ArrayList<Tags>) {


    }




    companion object {
        const val GALLERY_REQUEST = 256
        const val CAMARA_REQUEST = 109
        private var retrofit: Retrofit? = null
        private val BASE_URL = "https://speakout.herokuapp.com/api/"
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }


}
