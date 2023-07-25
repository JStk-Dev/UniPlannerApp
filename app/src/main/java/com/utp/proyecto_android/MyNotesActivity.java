package com.utp.proyecto_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utp.proyecto_android.util.AndroidFormatUtils;
import com.utp.proyecto_android.util.Note;

import java.util.ArrayList;
import java.util.List;

public class MyNotesActivity extends AppCompatActivity {

    private AndroidFormatUtils util;

    private final CollectionReference userCollection = FirebaseFirestore.getInstance().collection("User");
    private CollectionReference notesCollection;
    private ListView listView;
    private ArrayAdapter<Note> adapter;
    private List<Note> dataListView;
    private List<Note> listNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);

        util = new AndroidFormatUtils(getResources());
        util.setCustomToolBar(this, findViewById(R.id.topAppBar_MyNotesActivity));

        listView = findViewById(R.id.listView_activity_myNotes);
        dataListView = new ArrayList<>();
        listNotes = new ArrayList<>();

        setCustomListView();
        setupItemLongClickListView();
        if (getCurrentUserId() != null) {
            getNotesFirestoreAndShow();
        } else {
            Note tmp = new Note("Agrega una nota", "Haz clic en el botón flotante (+)");
            dataListView.add(tmp);
            Toast.makeText(this, "Usuario no autenticado. Esta función no esta disponible", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Setup para un diseño personalizado del ListView
     */
    private void setCustomListView() {
        // Se crea un nuevo adaptador para el listView
        adapter = new ArrayAdapter<Note>(this, R.layout.list_item_layout, R.id.textViewTitle, dataListView) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textViewTitle = view.findViewById(R.id.textViewTitle);
                TextView textViewDescription = view.findViewById(R.id.textViewDescription);

                // Obtener el dato para esta posicion
                Note nota = getItem(position);
                if (nota != null) {
                    // Establezca el título y descripción en los TextViews
                    textViewTitle.setText(nota.getTitleNote());
                    textViewDescription.setText(nota.getContentNote());
                }
                // Devuelve la vista personalizada
                return view;
            }
        };
        listView.setAdapter(adapter);
    }


    /**
     * Obtener las notas almacenadas en Firestore de acuerdo al usuario logeado y mostrarlos
     * en el ListView
     */
    private void getNotesFirestoreAndShow() {
        // Obtener una referencia al documento del usuario actual
        DocumentReference userDocument = userCollection.document(getCurrentUserId());
        // Obtener una referencia a la subcolección de notas del usuario
        notesCollection = userDocument.collection("Notes");

        notesCollection.get().addOnSuccessListener(querySnapshot -> {
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                Note nota = new Note();
                nota.setIdNote(document.getId());
                nota.setTitleNote(document.getString("title"));
                nota.setContentNote(document.getString("content"));
                listNotes.add(nota);
            }

            // Actualizar el ArrayAdapter con la nueva lista de notas
            adapter.clear();
            adapter.addAll(listNotes);
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Ocurrió un error al obtener los datos", Toast.LENGTH_SHORT).show();

        });
    }

    /**
     * Verifica que el usuario este autenticado en la app
     */
    private String getCurrentUserId() {
        SharedPreferences preferences = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        return preferences.getString("USER_ID", null);
    }

    /**
     * Metodo del FAB "FloatingActionButton"
     */
    public void addNewItem(View view) {
        startActivity(new Intent(this, NoteActivity.class));
        finish();
    }

    /**
     * Setup del listener de "pulsación prolongada" en un item del ListView
     */
    private void setupItemLongClickListView() {
        if (getCurrentUserId() != null) {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    // "position" indica el índice del elemento seleccionado en la lista
                    Note notaSeleccionada = listNotes.get(position);
                    String noteId = notaSeleccionada.getIdNote();
                    showDialog(noteId);

                    return true;
                }
            });
        }

    }

    /**
     * Ventana (dialog) de confirmación de "Eliminar una nota"
     */
    private void showDialog(final String noteId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Establece el título y el mensaje del diálogo
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta nota?");

        // Establece el botón de "Aceptar" y su evento de click
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getCurrentUserId() != null) {
                    // Realiza las acciones necesarias para el botón "Aceptar"
                    deleteNoteById(noteId);
                }

            }
        });

        // Establece el botón de "Cancelar" y su evento de click
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Realiza las acciones necesarias para el botón "Cancelar"
                Toast.makeText(MyNotesActivity.this, "Operación cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Borra una nota de Firestore de acuerdo a su "noteId" del documento
     */
    private void deleteNoteById(String noteId) {
        // Obtener referencia al documento de la nota a eliminar "{noteId}"
        DocumentReference noteToDeleteDocument = notesCollection.document(noteId);

        // Eliminar la nota de la subcolección de notas del usuario
        noteToDeleteDocument.delete()
                .addOnSuccessListener(aVoid -> {
                    // Buscar el índice del elemento con el noteId en la lista listNotes
                    int indexToRemove = -1;
                    for (int i = 0; i < listNotes.size(); i++) {
                        if (listNotes.get(i).getIdNote().equals(noteId)) {
                            indexToRemove = i;
                            break;
                        }
                    }

                    // Si se encontró el elemento en la lista, eliminarlo
                    if (indexToRemove != -1) {
                        listNotes.remove(indexToRemove);
                        adapter.clear();
                        adapter.addAll(listNotes);
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(this, "La nota se eliminó con éxito!", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ocurrió un error al intentar eliminar esta nota.", Toast.LENGTH_SHORT).show();
                });
    }

    /*
     Crea las opciones de menu en la vista
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_mynotes, menu);
        return true;
    }

    /*
     Establece las acciones por cada menu seleccionado
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se obtiene el valor del item seleccionado del menu
        int itemId = item.getItemId();
        return util.setActionsMenuMyNotes(this, itemId);
    }

}