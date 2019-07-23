package com.org.speakout.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.org.speakout.Constants.AppConstance
import com.org.speakout.MainActivity
import com.org.speakout.R
import com.org.speakout.base.BaseActivity
import com.org.speakout.loginpage.LoginPage
import com.org.speakout.model.RegistrationModel
import java.util.*

class RegistrationActivity : BaseActivity() {
    internal lateinit var registerButton: Button
    internal var nameTextView: EditText? = null
    internal var emailTextView: EditText? = null
    internal var passwordTextView: EditText? = null
    internal var phoneNumberTextView: EditText? = null
    internal var genderList: MutableList<String> = ArrayList()
    internal lateinit var gender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        registerButton = findViewById(R.id.button_register)
        genderList.add("Select Gender")
        genderList.add("Male")
        genderList.add("Female")
        wisdom.spinner(R.id.gender_spinner).adapter = ArrayAdapter(this, R.layout.custom_spinner, genderList)
        wisdom.spinner(R.id.gender_spinner).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    gender = ""
                } else if (position == 1) {
                    gender = AppConstance.MALE

                } else {
                    gender = AppConstance.FEMALE
                }
                Toast.makeText(this@RegistrationActivity, gender, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        validator.add(wisdom.editText(R.id.fullNameText))
        validator.add(wisdom.editText(R.id.emailText))
        validator.add(wisdom.editText(R.id.phoneNumberText))
        validator.add(wisdom.editText(R.id.password))
        validator.add(wisdom.editText(R.id.confrimPasswordText))
        registerButton.setOnClickListener(View.OnClickListener {
            if (!validator.validate())
                return@OnClickListener
            val registrationModel = RegistrationModel()
            registrationModel.gender = gender
            registrationModel.name = wisdom.editText(R.id.fullNameText).text.toString()
            registrationModel.email = wisdom.editText(R.id.emailText).text.toString()
            registrationModel.phoneNumber = wisdom.editText(R.id.phoneNumberText).text.toString()
            registrationModel.password = wisdom.editText(R.id.password).text.toString()
            createTables()
            api.registerUser(registrationModel).get("Registering User !", object : Do {
                override fun <T> Do(body: T?) {
                    val data = body as LoginPage.Response
                    sqliteClosedHelper.removeAll(LoginPage.Tag(""))
                    sqliteClosedHelper.removeAll(LoginPage.Problem("", "","",""))
                    sqliteClosedHelper.insertAll(data.data.tags)
                    sqliteClosedHelper.insertAll(data.data.problems)
                    preferences.putString(AppConstance.TOKEN, data.data.token).apply()
                    val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            })
        })


    }
}
