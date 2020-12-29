package com.example.pythonintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.DataInput;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText editText;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        editText = findViewById(R.id.editText);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editText.getText().toString();
                if(phoneNumber.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please Enter Phone Number.",Toast.LENGTH_LONG).show();
                }
                else
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91"+phoneNumber, 60, TimeUnit.SECONDS, LoginActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    signInUser(phoneAuthCredential);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Log.d(TAG,"onVerificationFailed:"+e.getLocalizedMessage());
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);

                                    Dialog dialog = new Dialog(LoginActivity.this);
                                    dialog.setContentView(R.layout.verify_popup);
                                    EditText editText1 = dialog.findViewById(R.id.editText1);
                                    Button btn_verify = dialog.findViewById(R.id.btn_verify);
                                    btn_verify.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String verifyCode = editText1.getText().toString();
                                            if(verifyCode.isEmpty()) return;
                                            //create credential
                                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s,verifyCode);
                                            signInUser(credential);
                                        }
                                    });
                                    dialog.show();
                                }
                            }
                    );
            }
        });
    }

    private void signInUser(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }else {
                            Log.d(TAG,"onComplete:"+task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
}