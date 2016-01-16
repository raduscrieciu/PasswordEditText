package com.screechstuidos.passwordedittext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private PasswordEditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordEditText = (PasswordEditText) findViewById(R.id.password);

        /*
        //Change Password Edit Text attributes programmatically

        passwordEditText.setShowActionText("UNMASK");
        passwordEditText.setHideActionText("MASK");
        passwordEditText.setActionLabelColor(Color.RED);
        passwordEditText.setActionLabelDimension(45);
        */
    }
}
