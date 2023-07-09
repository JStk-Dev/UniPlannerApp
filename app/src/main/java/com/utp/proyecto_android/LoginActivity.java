package com.utp.proyecto_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utp.proyecto_android.util.AndroidFormatUtils;
import com.utp.proyecto_android.util.UserManager;

public class LoginActivity extends AppCompatActivity {

    // Instancia de la clase de utilidad para aplicar formato
    private AndroidFormatUtils util;

    private EditText txt_email, txt_password;

    private String email, password;

    // Instancia de la clase custom UserManager del paquete util
    private UserManager userManager;


    // Variables para autenticacion con Firebase
    private FirebaseAuth auth;
    private EditText txtClaveLogin, txtCorreoLogin;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userManager = UserManager.getInstance();

        // Se obtienen los recursos de la vista - Gestiòn local de usuarios
        txt_email = findViewById(R.id.txt_email_field_login_activity);
        txt_password = findViewById(R.id.txt_password_field_login_activity);

        util = new AndroidFormatUtils(getResources());
        util.setCustomToolBar(this, findViewById(R.id.topAppBar_LoginActivity));


        // Implementaciòn Firebase - Autenticaciòn de usuarios
        auth = FirebaseAuth.getInstance();
        // inicio
        SharedPreferences preferences =
                getApplication().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        editor = preferences.edit();
        txtClaveLogin = findViewById(R.id.txt_password_field_login_activity);
        txtCorreoLogin = findViewById(R.id.txt_email_field_login_activity);

    }




    public void iniciar(View view) {
        String correo =txtCorreoLogin.getText().toString().trim();
        String clave =txtClaveLogin.getText().toString().trim();
        if (TextUtils.isEmpty(correo)){
            txtCorreoLogin.setError("Ingresar correo");
            return;
        }
        if (TextUtils.isEmpty(clave)){
            txtClaveLogin.setError("Ingresar clave");
            return;
        }
        txtClaveLogin.setText(null);
       // StartActivity obj = new StartActivity();
        acceder(correo,clave);
    }

    private void acceder(String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            editor.putString("CORREO", user.getEmail());
                            editor.apply();
                            Toast.makeText(LoginActivity.this, "Autenticaciòn Exitosa!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MyNotesActivity.class));
                            //updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Autenticaciòn Fallida!", Toast.LENGTH_SHORT).show();
                            txtClaveLogin.setText(null);
                            //updateUI(null);
                        }
                    }
                });
    }


    private void crearCuenta(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }







    /*
        Implementaciòn de autenticaciòn local de usuarios
     */

    public void setLogin(View view) {
        // Valida campos del formulario
        if(!validateFields()) {
            // Verificar las credenciales utilizando UserManager
            // Valida que el usuario ingresado este registrado
            if (userManager.isValidCredentials(email, password)) {
                Toast.makeText(this, getString(R.string.toast_welcome_user), Toast.LENGTH_SHORT).show();
                // Redirige al usuario al MyNotesActivity
                startActivity(new Intent(this, MyNotesActivity.class));
                // Finaliza la activity actual (LoginActivity)
                finish();
            } else {
                // Credenciales incorrectas - Usuario no existe
                Toast.makeText(this, getString(R.string.toast_user_not_exist), Toast.LENGTH_SHORT).show();
                clearText(); // Limpia las entradas de texto
            }
        }
    }

    private void clearText() {
        txt_email.setText("");
        txt_password.setText("");
    }

    private boolean validateFields() {
        email = txt_email.getText().toString();
        password = txt_password.getText().toString();

        if (email.isEmpty()) {
            txt_email.setError(getString(R.string.txt_error_email));
            return true;
        } else if (password.isEmpty()) {
            txt_password.setError(getString(R.string.txt_error_password));
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