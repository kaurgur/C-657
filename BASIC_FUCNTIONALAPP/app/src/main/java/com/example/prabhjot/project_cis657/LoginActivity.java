package com.example.prabhjot.project_cis657;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = (EditText) findViewById(R.id.email);
        EditText passwd = (EditText) findViewById(R.id.password);
        Button signin = (Button) findViewById(R.id.signin);


        signin.setOnClickListener(v -> {
            String emailStr = email.getText().toString();
            if (emailStr.length() == 0) {
                Snackbar.make(email, R.string.email_required, Snackbar.LENGTH_LONG).show();
                return;
            }
            if (!EMAIL_REGEX.matcher(emailStr).find()) {
                Snackbar.make(email, R.string.incorrect_email, Snackbar.LENGTH_LONG).show();
                return;
            }


                Intent main = new Intent(LoginActivity.this, MainScreenActivity.class);
                startActivity(main);


        });
    }
}