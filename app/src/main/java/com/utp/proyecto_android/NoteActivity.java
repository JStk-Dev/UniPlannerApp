package com.utp.proyecto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utp.proyecto_android.util.AndroidFormatUtils;

import java.util.HashMap;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

    private final CollectionReference userCollection = FirebaseFirestore.getInstance().collection("User");
    private CollectionReference notesCollection;
    private String userID;
    private EditText txtTitleNote, txtContentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        AndroidFormatUtils util = new AndroidFormatUtils(getResources());
        util.setCustomToolBar(this, findViewById(R.id.topAppBar_NoteActivity));

        txtTitleNote = findViewById(R.id.txt_title_activity_note);
        txtContentNote = findViewById(R.id.txt_content_activity_note);

    }

    private void validarSesionActiva() {
        // Validar sesión Firebase - usuario
        FirebaseAuth auth = FirebaseAuth.getInstance();
        SharedPreferences preferences = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        userID = preferences.getString("USER_ID", null);

        if (auth.getCurrentUser() != null  && userID != null) {
            // El usuario ha iniciado sesión previamente.
            setupFirestore();
        } else {
            // El usuario NO esta registrado en la app - Almacenamiento local de notas
            Toast.makeText(this, "Usuario no autenticado. Esta función no esta disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupFirestore() {
        // Obtener una referencia al documento del usuario actual
        DocumentReference userDocument = userCollection.document(userID);
        // Obtener una referencia a la subcolección de notas del usuario
        notesCollection = userDocument.collection("Notes");
        if (!validateFields()) {
            String titleNote = txtTitleNote.getText().toString().trim();
            String contentNote = txtContentNote.getText().toString().trim();
            sendDataFirestore(titleNote, contentNote);
        }
    }

    /**
     * Enviar la data a Firestore (Coll -> User {Note -> "data"})
     */
    private void sendDataFirestore(String titleNote, String contentNote) {
        // Crear un nuevo documento para la nueva nota con un ID automático
        DocumentReference newNoteDocument = notesCollection.document();
        // guardar idNote en SharedPreferences - temp
        saveIdNote(newNoteDocument.getId());

        // Crear un mapa con los datos de la nueva nota
        Map<String, Object> nuevaNota = new HashMap<>();
        nuevaNota.put("title", titleNote);
        nuevaNota.put("content", contentNote);

        // Agregar la nueva nota a la subcolección de notas del usuario
        newNoteDocument.set(nuevaNota)
                .addOnSuccessListener(aVoid -> {
                    // La nota se agregó exitosamente a la subcolección
                    Log.d("Note", "El documento ha sido escrito con exito en Firebase");
                    Toast.makeText(this, "Nota guardada con éxito!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MyNotesActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Ocurrió un error al agregar la nota
                    Log.d("Note", "Error al guardar el documento: ", e);
                    Toast.makeText(this, "Ocurrió un error al intentar guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Guarda el ID de la nota (documento) de la subcolección "Notes" en SharedPreferences
     */
    private void saveIdNote(String idNote) {
        SharedPreferences preferences = getSharedPreferences("NOTE_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NOTE_ID", idNote);
        editor.apply();
    }

    private boolean validateFields() {
        String titleNote = txtTitleNote.getText().toString();
        String contentNote = txtContentNote.getText().toString();

        if (titleNote.isEmpty()) {
            txtTitleNote.setError(getString(R.string.txt_error_title_note));
            return true;
        } else if (contentNote.isEmpty()) {
            txtContentNote.setError(getString(R.string.txt_error_content_note));
            return true;
        }
        // Si esta todo bien
        return false;
    }

    public void saveNoteButton(View view) {
        validarSesionActiva();
    }

    public void btnCancel(View view) {
        Toast.makeText(this, "Operación Cancelada", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MyNotesActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MyNotesActivity.class));
        finish();
    }

}