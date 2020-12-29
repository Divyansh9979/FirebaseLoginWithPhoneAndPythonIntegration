package com.example.pythonintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }
        Python python = Python.getInstance();
        PyObject pyObject = python.getModule("script");
        PyObject pyObject1 = pyObject.callAttr("test");

        textView = findViewById(R.id.text);
        textView.setText(pyObject1.toString());
    }
}