package com.utp.proyecto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.utp.proyecto_android.util.AndroidFormatUtils;
import com.utp.proyecto_android.util.User;
import com.utp.proyecto_android.util.UserManager;

public class RegisterActivity extends AppCompatActivity {

    // Instancia de la clase de utilidad para aplicar formato
    private AndroidFormatUtils util;

    private EditText txt_name, txt_email, txt_password, txt_conf_password;
    private Spinner spinner_gender;
    private String name, email, password;
    private int itemPositionGender;

    // Instancia de la clase custom UserManager del paquete util
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userManager = UserManager.getInstance();

        // Se obtienen los recursos de la vista
        txt_name = findViewById(R.id.txt_name_field_activity_register);
        txt_email = findViewById(R.id.txt_email_field_activity_register);
        txt_password = findViewById(R.id.txt_password_field_activity_register);
        txt_conf_password = findViewById(R.id.txt_conf_password_field_activity_register);
        spinner_gender = findViewById(R.id.spinner_gender_field_activity_register);

        util = new AndroidFormatUtils(getResources());
        util.setCustomToolBar(this, findViewById(R.id.topAppBar_RegisterActivity));

        setDataSpinner();
        clearText();
    }

    public void setRegister(View view) {
        // Valida campos vacios del formulario
        if(!validateFields()) {
            // Crea nuevo usuario con los datos proporcionados
            User user = new User(name, itemPositionGender, email, password);
            userManager.addUser(user);
            // Muestra msj de registro exitoso
            Toast.makeText(this, getString(R.string.toast_user_registered), Toast.LENGTH_SHORT).show();
            // Redirige al usuario al login
            startActivity(new Intent(this, LoginActivity.class));
            // Finaliza la activity actual (RegisterActivity)
            finish();
        }
    }

    private void clearText() {
        txt_name.setText("");
        txt_email.setText("");
        txt_password.setText("");
        txt_conf_password.setText("");
        spinner_gender.setSelection(0);
    }

    private boolean validateFields() {
        name = txt_name.getText().toString();
        email = txt_email.getText().toString();
        password = txt_password.getText().toString();
        String conf_password = txt_conf_password.getText().toString();
        itemPositionGender = spinner_gender.getSelectedItemPosition();

        if (name.isEmpty()) {
            txt_name.setError(getString(R.string.txt_error_name));
            return true;
        } else if (email.isEmpty()) {
            txt_email.setError(getString(R.string.txt_error_email));
            return true;
        } else if (itemPositionGender == 0) {
            Toast.makeText(this, getString(R.string.txt_error_gender), Toast.LENGTH_SHORT).show();
            return true;
        } else if (password.isEmpty()) {
            txt_password.setError(getString(R.string.txt_error_password));
            return true;
        } else if (conf_password.isEmpty() || !conf_password.equals(password)) {
            txt_conf_password.setError(getString(R.string.txt_error_conf_password));
            Toast.makeText(this, getString(R.string.txt_error_conf_password), Toast.LENGTH_SHORT).show();
            return true;
        }
        // Si esta todo bien
        return false;
    }

    /**
     * Establece los valores de las opciones que tendrá el componente Spinner en la vista
     */
    private void setDataSpinner() {
        // Se obtienen los valores del Spinner declarados en el XML del recurso "String" (string-array)
        String[] mGender = getResources().getStringArray(R.array.array_options_spinner_gender);

        // Se establece un ArrayAdapter custom para el Spinner "Gender"
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, mGender);
        // Se establece el "Adapter" personalizado al Spinner
        spinner_gender.setAdapter(adapter);
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