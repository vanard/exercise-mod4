package com.vanard.vian_1202154186_si4008_pab_modul4;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView tvRegister;
    Button btnLogin;
    EditText etMail, etPass;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        bindView();

        String texuto = tvRegister.getText().toString();
        SpannableString spannable = new SpannableString(texuto);
        int lengthTexuto = texuto.length() - 11;
        StyleSpan text = new StyleSpan(Typeface.BOLD);

        spannable.setSpan(
                text,
                lengthTexuto,
                texuto.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannable.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                lengthTexuto,
                texuto.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        tvRegister.setText(spannable);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    private void verify() {
        String mEmail = etMail.getText().toString();
        String mPass = etPass.getText().toString();

        if (mEmail.isEmpty()){
            pesan("Email harus diisi");
            return;
        }
        if (mPass.isEmpty()){
            pesan("Password harus diisi");
            return;
        }
        if (mPass.length() < 6){
            pesan("Password minimal 6 karakter");
            return;
        }

        mAuth.signInWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    pesan(task.getResult().toString());
                }
            }
        });
    }

    private void bindView() {
        tvRegister = findViewById(R.id.regist_tv_login);
        btnLogin = findViewById(R.id.login_btn_login);
        etMail = findViewById(R.id.email_et_login);
        etPass = findViewById(R.id.password_et_login);
    }

    private void pesan(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
