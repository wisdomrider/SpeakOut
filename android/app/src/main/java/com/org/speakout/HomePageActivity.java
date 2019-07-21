package com.org.speakout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.org.speakout.base.BaseActivity;
import com.org.speakout.constance.AppConstance;
import com.org.speakout.issueactivity.IssueActivity;
import com.org.speakout.service.GetLocationClass;
import com.org.speakout.service.MyLocationInterface;

public class HomePageActivity extends BaseActivity {
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
