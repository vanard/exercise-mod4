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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    TextView tvLogin;
    EditText etName, etEmail, etPass;
    Button btnRegist;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        bindView();

        String texuto = tvLogin.getText().toString();
        SpannableString spannable = new SpannableString(texuto);
        int lengthTexuto = texuto.length() - 5;
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

        tvLogin.setText(spannable);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    private void verify() {
        String mEmail = etEmail.getText().toString();
        final String mName = etName.getText().toString();
        String mPass = etPass.getText().toString();

        if (mEmail.isEmpty()){
            pesan("Email harus diisi");
            return;
        }
        if (mName.isEmpty()){
            pesan("Nama harus diisi");
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
        mAuth.createUserWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = mAuth.getCurrentUser();
                UserProfileChangeRequest prof = new UserProfileChangeRequest.Builder()
                        .setDisplayName(mName)
                        .build();
                if (user != null){
                    user.updateProfile(prof).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void bindView() {
        tvLogin = findViewById(R.id.login_tv_register);
        etEmail = findViewById(R.id.email_et_register);
        etName = findViewById(R.id.name_et_register);
        etPass = findViewById(R.id.password_et_register);
        btnRegist = findViewById(R.id.regist_btn_register);
    }

    private void pesan(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
