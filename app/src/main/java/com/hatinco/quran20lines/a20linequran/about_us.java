package com.hatinco.quran20lines.a20linequran;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import static com.hatinco.quran20lines.a20linequran.R.id.progressBar;

public class about_us extends AppCompatActivity {
    private WebView info_web;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        info_web = (WebView) findViewById(R.id.webview_company_info);
        info_web.setBackgroundColor(Color.TRANSPARENT);
        info_web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        info_web.setWebViewClient(new myWebClient());
        info_web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        info_web.getSettings().setJavaScriptEnabled(true);
        String infoText = "<center>\n" +
                "\t<h2>Developed By HAT INC.</h2>\n" +
                "\t<h3>About Us</h3>\n" +
                "\t<p>HAT INCORPORATION is high end inovative software solutions providing firm, working with professional and genius softawre, web developers and inovative designers. We believe quality over quntity. client satisfaction is our primary goal, achiving and enhancing that goal will give more opportunity to make goodwill.</p>\n" +
                "\t<h3>Contact Us</h3>\n" +
                "\t<ul>\n" +
                "\t\t<li>Call : +92-332-2982223</li>\n" +
                "\t\t<li>Website : <a href=\"http://hatinco.com\">www.hatinco.com</a></li>\n" +
                "\t\t<li>Email : info@hatinco.com</li>\n" +
                "\t\t<li>Address : <address>Sector 14/B Shadman Town karachi Pakistan</address></li>\n" +
                "\t</ul>\n" +
                "</br>\n" +
                "\t<h3>Disclaimer</h3>\n" +
                "\t<p>HAT INC. does not take any credits and responsibility of Image Assests used. All the credits goes to there actual owners. If you find any mistakes or issues kindly report to info@hatinco.com</p>\n" +
                "</center>";
        info_web.loadDataWithBaseURL("file:///android_asset/fonts/", getWebViewText(infoText), "text/html", "utf-8", null);
    }



    private String getWebViewText(String text) {

        return String.format("<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: arial;\n" +
                "}\n" +
                "body {\n" +
                "    font-family: arial;\n" +
                "    font-size: medium;\n" +
                "    text-align: justify;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body dir=\"rtl\" style=\"color:black\">\n" +
                " %s " +
                "</body>\n" +
                "</html>", text);
    }

    private class myWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }



        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
