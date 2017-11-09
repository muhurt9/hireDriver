package com.coder.hiredriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;

public class DriverSignUp extends AppCompatActivity {
    private EditText confirmPass, password, email, age, contact, name;
    private Button signupBtn;
    private RadioGroup gender;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isNum(String s) {
        try {
            new BigInteger(s);
            //   Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_up);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.emailAdd);
        confirmPass = (EditText) findViewById(R.id.confirmpassword);
        age = (EditText) findViewById(R.id.age);
        contact = (EditText) findViewById(R.id.contactNumber);
        name = (EditText) findViewById(R.id.Name);
        signupBtn = (Button) findViewById(R.id.signupbtn);
        gender = (RadioGroup) findViewById(R.id.gender);
    }

    public void signUp(View v) {

        if (name.getText().toString().length() == 0) {
            name.setError("name not entered");
            name.requestFocus();
        } else if (!isValidEmail(email.getText().toString())) {
            email.setError("invalid Email/");
            email.requestFocus();
        } else if (age.getText().toString().length() == 0 || !isNum(age.getText().toString())) {
            age.setError("Invalid");
            age.requestFocus();
        } else if (contact.getText().toString().length() != 10 || !isNum(contact.getText().toString())) {
            contact.setError("invalid");
            contact.requestFocus();
        } else if (password.getText().toString().length() < 8) {
            password.setError("Minimum length 8");
            password.requestFocus();
        } else if (!password.getText().toString().equals(confirmPass.getText().toString())) {
            confirmPass.setError("Password Not matched");
            confirmPass.requestFocus();
        } else {
            final user u = new user();
            u.email = email.getText().toString();
            u.age = Integer.parseInt(age.getText().toString());
            u.contact = (Long.parseLong(contact.getText().toString()));
            u.gender = (String) ((RadioButton) findViewById(gender.getCheckedRadioButtonId())).getText();
            u.name = name.getText().toString();

            //   Toast toast=Toast.makeText(getApplicationContext(),u.email+" "+u.age+" "+u.contact+" "+u.gender+" "+u.name+" "+temp,Toast.LENGTH_LONG);
            //toast.show();
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(u.email, password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user1 = mAuth.getCurrentUser();

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                u.userId = user1.getUid();
                                mDatabase.child("driver").child(u.userId).setValue(u);
                                Log.d("success", "createUserWithEmail:success");
                                startActivity(new Intent(DriverSignUp.this, DriverDashboard.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Failure", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(DriverSignUp.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }


    }
}
