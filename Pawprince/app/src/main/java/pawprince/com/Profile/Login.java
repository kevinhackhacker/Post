package pawprince.com.Profile;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pawprince.com.Activities.Home;
import pawprince.com.Activities.MainActivity;
import pawprince.com.R;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView mPassword,mEmail;
    Button mLoginBtn;
    TextView mCreateBtn,mCreateBtn1;
    FirebaseAuth fAuth;
    CheckBox chckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.etEmail);
        mPassword = findViewById(R.id.etPassword);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginbtn);
        mCreateBtn = findViewById(R.id.CreateText);
        mCreateBtn1 = findViewById(R.id.CreateText1);
        chckBox = (CheckBox) findViewById(R.id.chckbox);





        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Home.class));
                /*String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    mPassword.setError("Password is required.");
                    return;
                }
                if(password.length() < 6)
                {
                    mPassword.setError("Password must be equal to 6 and above Characters");
                    return;
                }
                //login the user in firebase
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext() , MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
            }
        });


        chckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean btton) {

                if(btton)
                {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , RegisterSelection.class));
            }
        });
        mCreateBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , AdminLogin.class));
            }
        });
    }
}
