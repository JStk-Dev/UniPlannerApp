package com.utp.proyecto_android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utp.proyecto_android.util.AndroidFormatUtils;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        // Instancia de la clase de utilidad para aplicar estilo
        AndroidFormatUtils util = new AndroidFormatUtils(getResources());
        util.setUnderlinedText(findViewById(R.id.txt4_activity_main), R.string.txt4_activity_main);
        // Carga el Logo de la app en el imageView utilizando la librería Picasso
        util.loadIconApp(this, findViewById(R.id.img_activity_main));
    }

    public void goWebView(View view){
        startActivity(new Intent(this, WebViewActivity.class));
    }

    /**
     * Metodo del boton "Comenzar"
     */
    public void goStartActivity(View view) {
        FirebaseUser currentUser = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            // Si el usuario ya ha iniciado sesión
            String userID = currentUser.getUid();
            startActivity(new Intent(this, MyNotesActivity.class));
            Toast.makeText(MainActivity.this, "Su ID de usuario es: "+userID, Toast.LENGTH_SHORT).show();

        } else {
            // Si el usuario no ha iniciado sesión, ir a StartActivity
            startActivity(new Intent(this, StartActivity.class));
        }
    }

}
