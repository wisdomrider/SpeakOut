package com.org.speakout.issueactivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.org.speakout.R;
import com.org.speakout.base.BaseActivity;
import com.org.speakout.constance.AppConstance;
import com.org.speakout.model.RegistrationModel;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class IssueActivity extends BaseActivity {
    Toolbar toolbar;
    ImageView imageView;
    Button galaryButton;
    String lats, longs;
    Spinner spinner;
    Button camarabutton;
    List<String> issueList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        camarabutton = findViewById(R.id.button_upload_camara);
        /*for (Tags tags : sqliteClosedHelper.getAll(new Tags())) {
            issueList.add(tags.getName());
         }*/
        lats = getIntent().getStringExtra(AppConstance.LATS);
        longs = getIntent().getStringExtra(AppConstance.LONG);
        spinner = findViewById(R.id.issue_list);
      //  spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_spinner, issueList));
        galaryButton = findViewById(R.id.btn_upload_galary);
        toolbar=  findViewById(R.id.toolbar);
        imageView = findViewById(R.id.problem_image);
        camarabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IssueActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                checkCamaraPermision();
            }
        });
        wisdom.button(R.id.btn_upload_galary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGalaryPermision();

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        wisdom.button(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationModel registrationModel = new RegistrationModel();
                registrationModel.setLocation(lats+":"+longs);
                registrationModel.setTitle(wisdom.editText(R.id.editTextTitle).getText().toString());
                registrationModel.setDesc(wisdom.editText(R.id.editTextDescription).toString());
                registrationModel.setPhoto("");
                registrationModel.setTag("");
            }
        });
    }

    protected  void checkGalaryPermision() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_REQUEST);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void checkCamaraPermision() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMARA_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMARA_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case GALLERY_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                } else {
                    Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show();
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;

            case CAMARA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMARA_REQUEST);
                } else {
                    Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show();

                }
                break;


                }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    InputStream inputStream = null;
                    try {
                        inputStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap mySelectedImage = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageURI(selectedImage);

                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                }
                break;
            case CAMARA_REQUEST:
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    //imageView.setImageURI(bitmap);

                }catch (Exception e) {

                }
        }

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void openIssueActivity() {
        super.openIssueActivity();
        disableKeyboardAtFirst();
    }
}
