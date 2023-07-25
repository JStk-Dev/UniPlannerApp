package com.utp.proyecto_android.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.utp.proyecto_android.LoginActivity;
import com.utp.proyecto_android.MainActivity;
import com.utp.proyecto_android.MyNotesActivity;
import com.utp.proyecto_android.ProfileActivity;
import com.utp.proyecto_android.R;
import com.utp.proyecto_android.RegisterActivity;
import com.utp.proyecto_android.AboutActivity;
import com.utp.proyecto_android.StartActivity;
import com.utp.proyecto_android.WebViewActivity;

import java.util.Objects;

public class AndroidFormatUtils {

    /**
     * Instancia de la clase Resources para acceder a los recursos de la vista a aplicar el formato
     */
    private final Resources res;

    public AndroidFormatUtils(Resources res) {
        this.res = res;
    }

    /**
     * Establece un estilo subrayado al texto de un TextView
     * @param textview "TextView" - Componente TextView a aplicar el estilo
     * @param id_res_string "int" - Valor del ID del recurso String que usa el TextView
     */
    public void setUnderlinedText(@NonNull TextView textview, int id_res_string) {

        // Obtener texto del TextView accediendo a los recursos
        String txt = res.getString(id_res_string);

        // Formatear el texto agregando el subrayado
        SpannableString texto = new SpannableString(txt);
        texto.setSpan(new UnderlineSpan(),0,texto.length(),0);

        textview.setText(texto);
    }

    /**
     * Establece y configura una barra de herramientas (ToolBar) custom en una "Activity".
     * En este caso aplica un botón de navegación "back"
     * @param activity "AppCompatActivity" - Activity desde donde se aplicará la ToolBar
     * @param topAppBar "MaterialToolBar" - Objeto de referencia tipo MaterialToolBar que se implementa en la vista
     */
    public void setCustomToolBar(@NonNull AppCompatActivity activity, @NonNull MaterialToolbar topAppBar) {

        //Se establece "topAppBar" como la ToolBar principal del activity
        activity.setSupportActionBar(topAppBar);
        //Se obtiene una instancia de la "ActionBar" principal y se agrega un botón de navegación
        //hacia atrás en la ToolBar. La instancia no debe ser null.
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Establece un listener al botón de navegación del "topAppBar" creado en la vista
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ejecuta la acción de "volver atrás"
                activity.onBackPressed();
            }
        });
    }

    /**
     * Establece las acciones por cada opción del menu de la ToolBar personalizada en una activity
     * @param activity "AppCompatActivity" - Activity desde donde se sobrecargará el metodo "OnOptionsItemSelected(item)"
     *                 para poder establecer las acciones por cada opción del menu.
     * @param itemId "int" - Referencia ID del menu seleccionado en la vista
     * @return "boolean" - False permite que continue el proceso normal del menu (sin accion). True cuando se consume
     * una acción en el menu.
     */
    public boolean setActionsMenuCustomToolBar(@NonNull AppCompatActivity activity, int itemId) {
        if (itemId == R.id.action_submenu_0_go_to_main_activity) {
            // Ir a main_activity
            activity.startActivity(new Intent(activity, MainActivity.class));
            return true;
        } else if (itemId == R.id.action_submenu_1_go_to_start_activity) {
            // Ir a start_activity
            activity.startActivity(new Intent(activity, StartActivity.class));
            return true;
        } else if (itemId == R.id.action_submenu_2_go_to_login_activity) {
            // Ir a login_activity
            activity.startActivity(new Intent(activity, LoginActivity.class));
            return true;
        } else if (itemId == R.id.action_submenu_3_go_to_register_activity) {
            // Ir a register_activity
            activity.startActivity(new Intent(activity, RegisterActivity.class));
            return  true;
        } else if (itemId == R.id.action_submenu_4_go_to_webview_activity) {
            // Ir a webview_activity
            activity.startActivity(new Intent(activity, WebViewActivity.class));
            return true;
        } else if (itemId == R.id.action_submenu_5_go_to_mynotes_activity) {
            // Ir a mynotes_activity
            activity.startActivity(new Intent(activity, MyNotesActivity.class));
            return true;
        } else if (itemId == R.id.action_menu_about) {
            // Ir a activity_about
            activity.startActivity(new Intent(activity, AboutActivity.class));
            return true;
        }

        return false;
    }

    /**
     * Asigna las acciones del menu del activity "MyNotes"
     */
    public boolean setActionsMenuMyNotes(@NonNull AppCompatActivity activity, int itemId) {
        if (itemId == R.id.action_menu_mynotes_profile) {
            // Ir a activity profile
            activity.startActivity(new Intent(activity, ProfileActivity.class));
            SharedPreferences preferences = activity.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            String userID = preferences.getString("USER_ID", null);
            if (userID == null) {
                // Si el usuario no ha iniciado sesión
                activity.finish();
            }
            return true;

        } else if (itemId == R.id.action_menu_mynotes_about) {
            // Ir a activity_settings
            activity.startActivity(new Intent(activity, AboutActivity.class));
            return true;

        } else if (itemId == R.id.action_menu_mynotes_logout) {
            // Cerrar sesión - redirige al main_activity
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences =
                    activity.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            SharedPreferences pref = activity.getSharedPreferences("NOTE_DATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.clear();
            edit.apply();
            activity.startActivity(new Intent(activity, MainActivity.class));
            Toast.makeText(activity, "Se cerró su sesión de usuario", Toast.LENGTH_SHORT).show();
            activity.finish();
            return true;

        }
        return false;
    }

    /**
     * Carga una imagen de internet en un ImageView de una Activity
     * @param activity "AppCompatActivity" - Activity en donde se encuentra el ImageView
     * @param imageView "ImageView" - Componente de la vista en donde se mostrará la imagen
     */
    public void loadIconApp(@NonNull AppCompatActivity activity, @NonNull ImageView imageView) {
        // URL del logo de la app alojado en internet
        String urlImage = "https://i.imgur.com/jHJXx2C.png";
        Picasso.with(activity).load(urlImage).into(imageView);
    }

}
