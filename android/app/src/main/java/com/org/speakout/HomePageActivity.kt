package com.org.speakout

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.org.speakout.Constants.AppConstance
import com.org.speakout.base.BaseActivity
import com.org.speakout.issueactivity.IssueActivity
import com.org.speakout.loginpage.LoginPage
import com.org.speakout.service.GetLocationClass
import com.org.speakout.service.MyLocationInterface

class HomePageActivity : BaseActivity() {
    private var floatingActionButton: FloatingActionButton? = null
    internal lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        floatingActionButton = findViewById(R.id.fab)
        floatingActionButton!!.setOnClickListener {
            if (checkLocationPermission()) {
                actualOpenIssueActivity()
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tool_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_logout) {
            logOut()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun logOut() {
        preferences.edit().putString(AppConstance.TOKEN, "").apply()
        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)
        finish()
    }


    internal fun actualOpenIssueActivity() {
        val getLocationClass = GetLocationClass()
        getLocationClass.start(this@HomePageActivity, MyLocationInterface { latitude, longitude -> openIssueActivity(latitude.toString(), longitude.toString()) })
    }

    private fun openIssueActivity(lats: String, longs: String) {

        val intent = Intent(this, IssueActivity::class.java)
        intent.putExtra(AppConstance.LATS, lats)
        intent.putExtra(AppConstance.LONG, longs)
        startActivity(intent)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun openIssueActivity() {
        actualOpenIssueActivity()
    }
}
