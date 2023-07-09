package com.utp.proyecto_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utp.proyecto_android.util.AndroidFormatUtils;

public class StartActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        // Instancia de la clase de utilidad para aplicar estilo
        AndroidFormatUtils util = new AndroidFormatUtils(getResources());
        util.setUnderlinedText(findViewById(R.id.txt_continue_activity_start), R.string.txt_continue_activity_start);

        // Carga el Logo de la app en el imageView utilizando la librería Picasso
        util.loadIconApp(this, findViewById(R.id.img_activity_start));
    }





    public void goActivtyLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void goActivityRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void goActivityMyNotes(View view) {
        startActivity(new Intent(this, MyNotesActivity.class));
    }
}