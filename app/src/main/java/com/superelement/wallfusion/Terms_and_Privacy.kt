package com.superelement.wallfusion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.Toolbar
import com.superelement.wallfusion.databinding.ActivityTermsAndPrivacyBinding

class Terms_and_Privacy : AppCompatActivity() {
    private lateinit var webView: WebView

    private lateinit var binding: ActivityTermsAndPrivacyBinding
    private lateinit var mytoolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTermsAndPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = findViewById(R.id.privacyid)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://jjgbr0ufv7arga8rhvyvng.on.drv.tw/Privacy/Privacy_policy.html")
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)


        mytoolbar=findViewById<Toolbar>(R.id.mytoolbar)
        mytoolbar.title="        Terms & Privacy"
        setSupportActionBar(mytoolbar)
    }
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (webView.canGoBack())
            webView.goBack()// if your webview cannot go back
        // it will exit the application

        else
            super.onBackPressed()
    }
}