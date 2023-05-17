package com.example.yourvote;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yourvote.api.PersonApi;
import com.example.yourvote.model.Person;
import com.example.yourvote.retrofit.RetrofitService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    Button submitBtn;
    TextInputEditText getVoterId;
    String verificationId;
    ProgressBar progressBar;
    RetrofitService retrofitService = new RetrofitService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            if(!isDarkTheme(getApplicationContext())){
                actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
            }
        }

        submitBtn = findViewById(R.id.submitBtn);
        getVoterId = findViewById(R.id.editVoterId);
        progressBar = findViewById(R.id.progress_circular);

        submitBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), VerificationOTP.class);
            startActivity(intent);
        });
    }

    public boolean isDarkTheme(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    private void switchView() {
        progressBar.setVisibility(View.VISIBLE);
        if(getVoterId.length() == 10){
            /*PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);
            personApi.getPersonByEpic(Objects.requireNonNull(getVoterId.getText()).toString().trim())
                    .enqueue(new Callback<Person>() {
                        @Override
                        public void onResponse(@NotNull Call<Person> call,@NotNull Response<Person> response) {
                            if(response.isSuccessful()){
                                Person person = response.body();
                                String getPhoneNumber = Objects.requireNonNull(person).getPhoneNumber();
                                Toast.makeText(getApplicationContext(), getPhoneNumber, Toast.LENGTH_SHORT).show();
                                generateOTP(getPhoneNumber);
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<Person> call,@NotNull Throwable t) {
                            Log.i("Retro Error", t.getLocalizedMessage());
                        }
                    });*/
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Invalid Voter Id", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateOTP(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ phoneNumber,
                60,
                SECONDS,
                MainActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(getApplicationContext(), "Code sent", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), VerificationOTP.class);
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(), "Verification Failed : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCodeSent(@NonNull String mVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationId = mVerificationId;
                    }
                }
        );
    }
}