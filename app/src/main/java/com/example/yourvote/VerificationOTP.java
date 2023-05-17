package com.example.yourvote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;

public class VerificationOTP extends AppCompatActivity {

    Button submitBtnOtp;
    TextInputEditText getVerificationCode;
    Intent intent = getIntent();
    String verificationId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_otp);
        
        submitBtnOtp = findViewById(R.id.submitBtnOtp);
        getVerificationCode = findViewById(R.id.editOtp);

        submitBtnOtp.setOnClickListener(view -> {
            updateUser();
        });
    }

    private void verifyCode(String code) {
        if(code.length() == 6){
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                    verificationId,
                    code
            );
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            updateUser();
                        } else {
                            Toast.makeText(getApplicationContext(), "Verification Code entered was invalid", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Verification Failed : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser() {
        startActivity(new Intent(getApplicationContext(), BallotActivity.class));
    }

}