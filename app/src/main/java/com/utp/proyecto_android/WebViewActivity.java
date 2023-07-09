package com.utp.proyecto_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.utp.proyecto_android.util.AndroidFormatUtils;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // Instancia de la clase de utilidad para aplicar estilo
        AndroidFormatUtils util = new AndroidFormatUtils(getResources());
        // Se establece la ToolBar | Referencia del MaterialToolBar implementada en la vista
        util.setCustomToolBar(this, findViewById(R.id.topAppBar_WebViewActivity));

        loadWebView("https://github.com/JStk-Dev/UniPlannerApp/tree/main");
    }

    /**
     * Carga contenido al WebVIew del activity
     * @param url "String" - Dirección URL del sitio web a mostrar en WebView.
     */
    private void loadWebView(@NonNull String url) {
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl(url);
    }

}