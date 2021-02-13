package com.ns.quenfutsalbooking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    TextView txt_signup, txt_forget;
    TextInputLayout til_email, til_password;
    TextInputEditText edt_email, edt_password;
    ProgressBar pgbar;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        txt_signup = findViewById(R.id.txt_signup);
        txt_forget = findViewById(R.id.txt_forget);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        til_email = findViewById(R.id.ipt_email);
        til_password    = findViewById(R.id.ipt_password);
        pgbar = findViewById(R.id.pgbar_login);

        fAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(v -> login());

        txt_signup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });

        txt_forget.setOnClickListener(v -> {
            EditText resetEmail = new EditText(v.getContext());
            AlertDialog.Builder passDialog = new AlertDialog.Builder(v.getContext());
            passDialog.setTitle("Reset Password ?");
            passDialog.setMessage("Masukkan Email Untuk Mengirimkan Link Reset Password!");
            passDialog.setView(resetEmail);

            passDialog.setPositiveButton("Ya", (dialog, which) -> {
                String email = resetEmail.getText().toString();
                fAuth.sendPasswordResetEmail(email).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Link Reset Password Dikirim Ke Email Anda...", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e ->{
                    Toast.makeText(this, "Error ! Email Anda Belum Terdaftar. Silahkan daftar terlebih dahulu" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            });

            passDialog.setNegativeButton("Tidak", (dialog, which) -> {

            });

            passDialog.create().show();
        });

    }


    private void login() {
        String email = String.valueOf(edt_email.getText());
        String password = String.valueOf(edt_password.getText());

        pgbar.setVisibility(View.VISIBLE);

        if (!validasiEmail() | !validasiPassword()) {
            pgbar.setVisibility(View.INVISIBLE);
            btn_login.setEnabled(true);
            return;
        }

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Selamat Datang", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                Toast.makeText(this, "Sedang Ganguan Coba Beberapa Saat Lagi!!!", Toast.LENGTH_SHORT).show();
                pgbar.setVisibility(View.GONE);
            }
        });
    }


    private boolean validasiEmail() {
        String email = Objects.requireNonNull(til_email.getEditText()).getText().toString();
        if (email.isEmpty()) {
            til_email.setError("Email tidak boleh kosong");
            return false;
        } else {
            til_email.setError(null);
            til_email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validasiPassword() {
        String password = Objects.requireNonNull(til_password.getEditText()).getText().toString();
        if (password.isEmpty()) {
            til_password.setError("Password tidak boleh kosong");
            return false;
        } else {
            til_password.setError(null);
            til_password.setErrorEnabled(false);
            return true;
        }
    }
}