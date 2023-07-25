package com.utp.proyecto_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private EditText txtName, txtEmail, txtGender;
    private Button btnEditProfile, btnSaveDataProfile;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentSnapshot document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.txt_name_field_activity_profile);
        txtEmail = findViewById(R.id.txt_email_field_activity_profile);
        txtGender = findViewById(R.id.txt_gender_field_activity_profile);

        btnEditProfile = findViewById(R.id.btn_edit_data_activity_profile);
        btnSaveDataProfile = findViewById(R.id.btn_save_activity_profile);
        btnSaveDataProfile.setEnabled(false);

        // Obtener el ID del usuario desde SharedPreferences
        String userID = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("USER_ID", null);

        if (userID != null) {
            // Si el ID del usuario no es nulo, obtener los datos del usuario desde Firestore
            db.collection("User").document(userID).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                document = task.getResult();
                                if (document.exists()) {
                                    // Muestra los datos del usuario en la vista
                                    setDataUser();
                                } else {
                                    // Documento no existe en Firestore
                                    Toast.makeText(ProfileActivity.this, "No se encontraron datos del perfil en Firestore", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ProfileActivity.this, MyNotesActivity.class));
                                    finish();
                                }
                            } else {
                                // Manejar errores al obtener datos de Firestore
                                Toast.makeText(ProfileActivity.this, "Error al obtener datos del perfil: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ProfileActivity.this, MyNotesActivity.class));
                                finish();
                            }
                        }
                    });
        } else {
            // Si el ID del usuario es nulo, redirigir a MainActivity
            Toast.makeText(ProfileActivity.this, "ID del usuario no encontrado. No se puede mostrar el perfil", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, MyNotesActivity.class));
            finish();
        }
    }

    private void setDataUser() {
        txtName.setText(document.getString("name"));
        txtEmail.setText(document.getString("email"));
        String opcGender = Objects.requireNonNull(document.getString("gender"));

        int temp = Integer.parseInt(opcGender);
        String[] mGenders = getResources().getStringArray(R.array.array_options_spinner_gender);
        if (temp == 1) {
            this.txtGender.setText(mGenders[1]);
        } else if (temp == 2) {
            this.txtGender.setText(mGenders[2]);
        }
    }

    /**
     * Método del botón "Editar Perfil"
     */
    public void enableEditText(View view) {
        txtName.setEnabled(true);
        txtEmail.setEnabled(true);
        btnSaveDataProfile.setEnabled(true);
        btnEditProfile.setEnabled(false);
        txtName.requestFocus();
    }

    /**
     * Método del botón "Guardar"
     */
    public void saveDataProfile(View view) {
        if (!validateFields()) {
            String name = txtName.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            updateDataFirebase(name, email);
        }
    }

    private void updateDataFirebase(String name, String email) {
        String userID = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("USER_ID", null);

        if (userID != null) {
            // Crear un objeto Map con los nuevos datos del usuario
            Map<String, Object> dataUser = new HashMap<>();
            dataUser.put("name", name);
            dataUser.put("email", email);

            // Realizar la actualización en Firestore
            db.collection("User").document(userID)
                    .update(dataUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Datos actualizados exitosamente!", Toast.LENGTH_SHORT).show();
                                // Deshabilitar la edición y habilitar el botón de "Editar Perfil"
                                txtName.setEnabled(false);
                                txtEmail.setEnabled(false);
                                btnSaveDataProfile.setEnabled(false);
                                btnEditProfile.setEnabled(true);
                            } else {
                                Toast.makeText(ProfileActivity.this, "Error al actualizar los datos: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(ProfileActivity.this, "ID del usuario no encontrado. No se puede actualizar el perfil", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validateFields() {
        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();

        if (name.isEmpty()) {
            txtName.setError(getString(R.string.txt_error_name));
            return true;
        } else if (email.isEmpty()) {
            txtEmail.setError(getString(R.string.txt_error_email));
            return true;
        }
        // Si esta todo bien
        return false;
    }
}