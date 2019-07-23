package com.org.speakout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.org.speakout.Constants.AppConstance
import com.org.speakout.HomeRecyclerView
import com.org.speakout.MainActivity
import com.org.speakout.R
import com.org.speakout.base.BaseFragment
import com.org.speakout.loginpage.LoginPage
import com.wisdomrider.sqliteclosedhelper.SqliteClosedHelper

class HomePageFragment(): BaseFragment() {
    lateinit  var mainActivity: MainActivity
    lateinit var recyclerView: RecyclerView
    var arrayList = ArrayList<LoginPage.Problem>()
    lateinit  var sqliteClosedHelper: SqliteClosedHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_page_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sqliteClosedHelper = SqliteClosedHelper(activity, AppConstance.APP_NAME)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = HomeRecyclerView(this)
        recyclerView.adapter = adapter
        arrayList = sqliteClosedHelper.getAll(LoginPage.Problem("", "", "",""))
        }


     fun getInstance(mainActivity: MainActivity):HomePageFragment  {
        this.mainActivity = mainActivity
        return HomePageFragment();
    }
}