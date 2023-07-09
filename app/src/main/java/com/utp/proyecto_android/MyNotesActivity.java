package com.utp.proyecto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.utp.proyecto_android.util.AndroidFormatUtils;

public class MyNotesActivity extends AppCompatActivity {

    // Instancia de la clase de utilidad para aplicar formato
    private AndroidFormatUtils util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);

        util = new AndroidFormatUtils(getResources());
        util.setCustomToolBar(this, findViewById(R.id.topAppBar_MyNotesActivity));
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