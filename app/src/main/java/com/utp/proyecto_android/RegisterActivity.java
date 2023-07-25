package com.utp.proyecto_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utp.proyecto_android.util.AndroidFormatUtils;

import java.util.*;

public class RegisterActivity extends AppCompatActivity {

    // Instancia de la clase de utilidad para aplicar formato
    private AndroidFormatUtils util;

    private EditText txtName, txtEmail, txtPassword, txtConfPassword;
    private Spinner spinnerGender;

    private FirebaseAuth mAuth;

    public static final String TAG = "User";
    private final CollectionReference userCollection = FirebaseFirestore.getInstance().collection("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Se obtienen los recursos de la vista
        txtName = findViewById(R.id.txt_name_field_activity_register);
        txtEmail = findViewById(R.id.txt_email_field_activity_register);
        txtPassword = findViewById(R.id.txt_password_field_activity_register);
        txtConfPassword = findViewById(R.id.txt_conf_password_field_activity_register);
        spinnerGender = findViewById(R.id.spinner_gender_field_activity_register);

        util = new AndroidFormatUtils(getResources());
        util.setCustomToolBar(this, findViewById(R.id.topAppBar_RegisterActivity));

        setDataSpinner();
        clearText();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update instance.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    /**
     * Metodo del botón "Registrarse"
     */
    public void setRegister(View view) {
        // Valida campos vacios del formulario
        if(!validateFields()) {
            String nametxt = txtName.getText().toString().trim();
            String emailtxt = txtEmail.getText().toString().trim();
            String passwordtxt = txtPassword.getText().toString().trim();
            String gendertxt = String.valueOf(spinnerGender.getSelectedItemPosition());

            registrarFirebase(emailtxt, passwordtxt, nametxt, gendertxt);
        }
    }

    private void registrarFirebase(String email, String password, String name, String gender) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Creación exitosa de usuario, obtener el ID de usuario
                            String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                            Map<String, Object> dataToSave = new HashMap<>();
                            dataToSave.put("name", name);
                            dataToSave.put("email", email);
                            dataToSave.put("password", password);
                            dataToSave.put("gender", gender);

                            // Agregar el usuario a Firestore usando el ID de usuario (Auth) como el ID del documento
                            userCollection.document(userID).set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "El documento ha sido escrito con exito en Firebase");

                                    // Después de escribir con éxito en Firestore, continuar con el flujo de la aplicación
                                    Toast.makeText(RegisterActivity.this, "Registro exitoso en Firebase!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Error al guardar el documento: ", e);
                                }
                            });

                        } else {
                            // Si falla el registro, muestra un mensaje al usuario.
                            Toast.makeText(RegisterActivity.this, "Falló el registro, inténtelo nuevamente.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearText() {
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtConfPassword.setText("");
        spinnerGender.setSelection(0);
    }

    private boolean validateFields() {
        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String conf_password = txtConfPassword.getText().toString();
        int itemPositionGender = spinnerGender.getSelectedItemPosition();

        if (name.isEmpty()) {
            txtName.setError(getString(R.string.txt_error_name));
            return true;
        } else if (email.isEmpty()) {
            txtEmail.setError(getString(R.string.txt_error_email));
            return true;
        } else if (itemPositionGender == 0) {
            Toast.makeText(this, getString(R.string.txt_error_gender), Toast.LENGTH_SHORT).show();
            return true;
        } else if (password.isEmpty()) {
            txtPassword.setError(getString(R.string.txt_error_password));
            return true;
        } else if (conf_password.isEmpty() || !conf_password.equals(password)) {
            txtConfPassword.setError(getString(R.string.txt_error_conf_password));
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
        spinnerGender.setAdapter(adapter);
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