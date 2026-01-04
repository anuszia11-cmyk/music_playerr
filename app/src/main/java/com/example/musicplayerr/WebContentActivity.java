package com.example.musicplayerr;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class WebContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Section 1.4: Apply the saved theme
        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);

        // Section 7.1: Create and set WebView
        WebView webView = new WebView(this);
        setContentView(webView);

        // Section 7.3: Enable JavaScript and handle loading
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Section 7.4: Provide in-app browsing (prevents opening Chrome)
        webView.setWebViewClient(new WebViewClient());

        // Section 7.2: Load documentation URL
        webView.loadUrl("https://jsonplaceholder.typicode.com/");
    }
}