package com.utp.proyecto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utp.proyecto_android.util.AndroidFormatUtils;
import com.utp.proyecto_android.util.User;
import com.utp.proyecto_android.util.UserManager;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        // Instancia de la clase custom UserManager del paquete util
        UserManager userManager = UserManager.getInstance();
        // Se agrega usuario de prueba
        userManager.addUser(new User("USER", 1, "USER", "10101"));

        // Instancia de la clase de utilidad para aplicar estilo
        AndroidFormatUtils util = new AndroidFormatUtils(getResources());
        util.setUnderlinedText(findViewById(R.id.txt4_activity_main), R.string.txt4_activity_main);

        // Carga el Logo de la app en el imageView utilizando la librería Picasso
        util.loadIconApp(this, findViewById(R.id.img_activity_main));
    }

    public void goWebView(View view){
        startActivity(new Intent(this, WebViewActivity.class));
    }

    // Metodo del boton
    public void goStartActivity(View view) {

        FirebaseUser currentUser = auth.getCurrentUser();
        SharedPreferences preferences =
                getApplication().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        editor = preferences.edit();
        if (currentUser != null) {
            editor.putString("CORREO", currentUser.getEmail());
            editor.apply();
            startActivity(new Intent(this, MyNotesActivity.class));
        }else {
            startActivity(new Intent(this, StartActivity.class));
        }

    }

}