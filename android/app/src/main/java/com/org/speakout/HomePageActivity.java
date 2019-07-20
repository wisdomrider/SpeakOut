package com.org.speakout;
import android.os.Bundle;
import android.widget.Toast;
import com.org.speakout.base.BaseActivity;
import com.org.speakout.service.GetLocationClass;
import com.org.speakout.service.MyLocationInterface;

public class HomePageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkLocationPermission()) {
            GetLocationClass getLocationClass = new GetLocationClass();
            getLocationClass.start(this, new MyLocationInterface() {
                @Override
                public void getLocation(double latitude, double longitude) {
                    Toast.makeText(HomePageActivity.this, String.valueOf(latitude), Toast.LENGTH_SHORT).show();
                    Toast.makeText(HomePageActivity.this, String.valueOf(longitude), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No location permision", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
