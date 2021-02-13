package com.ns.quenfutsalbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    Button btn_singup;
    TextView txt_login;
    TextInputLayout til_fullname, til_password, til_email, til_nohp;
    TextInputEditText edt_fullname, edt_password, edt_email, edt_nohp;
    ProgressBar pgbar;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //deklarasi Firebase
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //Deklarasi item
        btn_singup      = findViewById(R.id.btn_signup);
        txt_login       = findViewById(R.id.txt_login);
        til_fullname    = findViewById(R.id.ipt_fullname2);
        til_password    = findViewById(R.id.ipt_password2);
        til_email       = findViewById(R.id.ipt_email2);
        til_nohp        = findViewById(R.id.ipt_nohp);
        edt_fullname    = findViewById(R.id.signup_fullname);
        edt_password    = findViewById(R.id.signup_password);
        edt_email       = findViewById(R.id.signup_email);
        edt_nohp        = findViewById(R.id.signup_nohp);
        pgbar           = findViewById(R.id.pgbar_signup);

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        btn_singup.setOnClickListener(v -> registerUser());

        txt_login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String fullname = String.valueOf(edt_fullname.getText());
        String password = String.valueOf(edt_password.getText());
        String email = String.valueOf(edt_email.getText());
        String no_hp = String.valueOf(edt_nohp.getText());

        pgbar.setVisibility(View.VISIBLE);

        if (!validasiFullname() | !validasiPassword() | !validasiEmail()| !validasiNoHP()) {
            pgbar.setVisibility(View.INVISIBLE);
            btn_singup.setEnabled(true);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                //Link Verifikasi

                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                user.sendEmailVerification().addOnSuccessListener(aVoid ->
                        Toast.makeText(SignUpActivity.this, "Link Verifikasi Email Terkirim...", Toast.LENGTH_SHORT).show());

                Toast.makeText(this, "Selamat, Akun Anda Sudah Terdaftar!", Toast.LENGTH_SHORT).show();
                UserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                DocumentReference documentReference = fStore.collection("users").document(UserID);
                Map<String,Object> users = new HashMap<>();
                users.put("fullname",fullname);
                users.put("password",password);
                users.put("email",email);
                users.put("no_hp",no_hp);
                documentReference.set(users);
                startActivity(new Intent(this,MainActivity.class));
//                Users users = new Users(fullname,username,password,email,no_hp);


            }
        });


    }

    //validasi
    private boolean validasiFullname() {
        String fullname = til_fullname.getEditText().getText().toString();
        if (fullname.isEmpty()) {
            til_fullname.setError("Nama tidak boleh kosong");
            return false;
        } else {
            til_fullname.setError(null);
            til_fullname.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validasiPassword() {
        String password = til_password.getEditText().getText().toString();
        if (password.isEmpty()) {
            til_password.setError("Password tidak boleh kosong");
            return false;
        } else {
            til_password.setError(null);
            til_password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validasiEmail() {
        String email = til_email.getEditText().getText().toString();
        if (email.isEmpty()) {
            til_email.setError("Email tidak boleh kosong");
            return false;
        } else {
            til_email.setError(null);
            til_email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validasiNoHP() {
        String noHp = til_nohp.getEditText().getText().toString();
        if (noHp.isEmpty()) {
            til_nohp.setError("No HP tidak boleh kosong");
            return false;
        } else {
            til_nohp.setError(null);
            til_nohp.setErrorEnabled(false);
            return true;
        }
    }


}