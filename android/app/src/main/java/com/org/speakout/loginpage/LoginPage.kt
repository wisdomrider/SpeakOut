package com.org.speakout.loginpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import com.org.speakout.Constants.AppConstance
import com.org.speakout.MainActivity
import com.org.speakout.R
import com.org.speakout.base.BaseActivity
import com.org.speakout.model.RegistrationModel
import com.org.speakout.registration.RegistrationActivity
import com.wisdomrider.sqliteclosedhelper.SqliteAnnotations

class LoginPage : BaseActivity() {
    internal var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        validator.add(wisdom.editText(R.id.userName))
        validator.add(wisdom.editText(R.id.password))
        wisdom.button(R.id.login_registration).setOnClickListener {
            val intent = Intent(this@LoginPage, RegistrationActivity::class.java)
            startActivity(intent)
        }
        wisdom.button(R.id.login_button).setOnClickListener(View.OnClickListener {
            if (!validator.validate()) return@OnClickListener
            login()
        })
    }

    class Response(var data: Data)
    class Data(var token: String?, var tags: ArrayList<Tag>, var problems: ArrayList<Problem>)
    class Tag(@SqliteAnnotations.Primary var name: String)
    class Problem( var title:String, var desc: String, var timeStamp: String, var photoUrl: String)


    private fun login() {
        val registrationModel = RegistrationModel()
        registrationModel.password = wisdom.editText(R.id.password).text.toString()
        registrationModel.userName = wisdom.editText(R.id.userName).text.toString()
        createTables()
        api.login(registrationModel).get("Logging In !", object : Do {
            override fun <T> Do(body: T?) {
                val data = body as Response
                sqliteClosedHelper.removeAll(Tag(""))
                sqliteClosedHelper.removeAll(Problem("","", "", ""))
                preferences.putString(AppConstance.TOKEN, data.data.token).apply()
                sqliteClosedHelper.insertAll(data.data.tags)
                sqliteClosedHelper.insertAll(data.data.problems)
                startActivity(Intent(this@LoginPage, MainActivity::class.java))
            }
        })
    }
}
