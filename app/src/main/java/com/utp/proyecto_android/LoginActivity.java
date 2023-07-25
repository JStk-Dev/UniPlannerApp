package com.utp.proyecto_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utp.proyecto_android.util.AndroidFormatUtils;

public class LoginActivity extends AppCompatActivity {

    private AndroidFormatUtils util;

    private FirebaseAuth auth;
    private EditText txtClaveLogin, txtCorreoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        util = new AndroidFormatUtils(getResources());
        util.setCustomToolBar(this, findViewById(R.id.topAppBar_LoginActivity));

        txtClaveLogin = findViewById(R.id.txt_password_field_login_activity);
        txtCorreoLogin = findViewById(R.id.txt_email_field_login_activity);

        validarSesionActiva();
    }

    private void validarSesionActiva() {
        // Implementaciòn Firebase - Autenticaciòn de usuarios
        auth = FirebaseAuth.getInstance();
        SharedPreferences preferences = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String userID = preferences.getString("USER_ID", null);

        if (auth.getCurrentUser() != null  && userID != null) {
            // Si el ID está en SharedPreferences, significa que el usuario ha iniciado sesión previamente.
            // Redirige automáticamente a la actividad MyNotesActivity
            startActivity(new Intent(LoginActivity.this, MyNotesActivity.class));
            finish();
        }
    }

    /**
     * Metodo del botón "Iniciar Sesión"
     */
    public void iniciar(View view) {
        // Valida campos vacios del formulario
        if (!validateFields()) {
            String correo = txtCorreoLogin.getText().toString().trim();
            String clave = txtClaveLogin.getText().toString().trim();
            accederFirebase(correo,clave);
        }
    }

    private void accederFirebase(String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            auth = FirebaseAuth.getInstance();
                            // Inicio de sesión exitoso
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                // Guardar el ID del usuario en SharedPreferences
                                SharedPreferences preferences = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                String userID = user.getUid();
                                editor.putString("USER_ID", userID);
                                editor.apply();

                                Toast.makeText(LoginActivity.this, getString(R.string.toast_welcome_user), Toast.LENGTH_SHORT).show();
                                // Redirige al usuario al MyNotesActivity
                                startActivity(new Intent(LoginActivity.this, MyNotesActivity.class));
                                // Finaliza la activity actual (LoginActivity)
                                finish();
                            }

                        } else {
                            // Credenciales incorrectas - Usuario no existe
                            Toast.makeText(LoginActivity.this, getString(R.string.toast_user_not_exist), Toast.LENGTH_SHORT).show();
                            clearText(); // Limpia las entradas de texto
                        }
                    }
                });
    }

    private void clearText() {
        txtCorreoLogin.setText("");
        txtClaveLogin.setText("");
    }

    private boolean validateFields() {
        String email = txtCorreoLogin.getText().toString();
        String password = txtClaveLogin.getText().toString();

        if (email.isEmpty()) {
            txtCorreoLogin.setError(getString(R.string.txt_error_email));
            return true;
        } else if (password.isEmpty()) {
            txtClaveLogin.setError(getString(R.string.txt_error_password));
            return true;
        }
        // Si esta todo bien
        return false;
    }

    /*
     Crea las opciones de menu en la vista
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_toolbar, menu);
        return true;
    }

    /*
     Establece las acciones por cada menu seleccionado
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se obtiene el valor del item seleccionado del menu
        int itemId = item.getItemId();
        return util.setActionsMenuCustomToolBar(this, itemId);
    }

}