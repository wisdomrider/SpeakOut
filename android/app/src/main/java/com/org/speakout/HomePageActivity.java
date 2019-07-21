package com.org.speakout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.org.speakout.base.BaseActivity;
import com.org.speakout.constance.AppConstance;
import com.org.speakout.issueactivity.IssueActivity;
import com.org.speakout.loginpage.LoginPage;
import com.org.speakout.service.GetLocationClass;
import com.org.speakout.service.MyLocationInterface;

import androidx.appcompat.widget.Toolbar;

public class HomePageActivity extends BaseActivity {
    private FloatingActionButton floatingActionButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocationPermission()) {
                    actualOpenIssueActivity();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
       preferences.edit().putString(AppConstance.TOKEN, "").apply();
       Intent intent = new Intent(this, LoginPage.class);
       startActivity(intent);
       finish();
    }


    void actualOpenIssueActivity() {
        showProgressBar();
        GetLocationClass getLocationClass = new GetLocationClass();
        getLocationClass.start(HomePageActivity.this, new MyLocationInterface() {
            @Override
            public void getLocation(double latitude, double longitude) {

                openIssueActivity(String.valueOf(latitude), String.valueOf(longitude));

            }
        });
    }

    private void openIssueActivity(String lats, String longs) {
        hideProgressBar();
        Intent intent = new Intent(this, IssueActivity.class);
        intent.putExtra(AppConstance.LATS, lats);
        intent.putExtra(AppConstance.LONG, longs);
        startActivity(intent);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void openIssueActivity() {
        actualOpenIssueActivity();
    }
}
