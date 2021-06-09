package com.eflexsoft.larrysnews.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.eflexsoft.larrysnews.R;

public class AboutActivity extends AppCompatActivity {

    String dot = "•";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView t2 = findViewById(R.id.text2);
        t2.setText("https://newsapi.org/terms");
        Linkify.addLinks(t2, Linkify.ALL);

    }
}