package com.utp.proyecto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void goGitHub(View view) {
        startActivity(new Intent(this, WebViewActivity.class));
    }

    public void back(View view) {
        finish();
    }

}