package com.org.speakout.splashscreen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.org.speakout.Constants.AppConstance
import com.org.speakout.MainActivity
import com.org.speakout.R
import com.org.speakout.base.BaseActivity
import com.org.speakout.loginpage.LoginPage

class SplashScreen : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        isUserLogin()
    }

    private fun isUserLogin() {
        // user is login
        if (preferences.getString(AppConstance.TOKEN, "") != "") {
            openHomePage()
        } else {
            openLoginPage()
        }
    }

    private fun openLoginPage() {
        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)
        finish()
    }

    private fun openHomePage() {
        createTables()
        api.getSplash().get("", object : Do {
            override fun <T> Do(body: T?) {
                val data = body as LoginPage.Response
                sqliteClosedHelper.removeAll(LoginPage.Tag(""))
                sqliteClosedHelper.removeAll(LoginPage.Problem("","", "", ""))
                sqliteClosedHelper.insertAll(data.data.tags)
                sqliteClosedHelper.insertAll(data.data.problems)
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }
        }, on406 = object : Do {
            override fun <T> Do(body: T?) {
                Toast.makeText(this@SplashScreen, "Sorry please try again", Toast.LENGTH_SHORT).show()
            }
        })


    }


}
