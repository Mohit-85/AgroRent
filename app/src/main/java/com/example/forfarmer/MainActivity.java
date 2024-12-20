package com.example.forfarmer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity {
    Button btnLogin;
    TextView btnRegister;
    EditText etEmail;
    EditText etPassword;
    TextView tvTitle;
    private FirebaseAuth mAuth;
    ImageView googleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
          if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.logintext);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        tvTitle=findViewById(R.id.tvConnectUsing);
        googleSignIn=findViewById(R.id.googleSignIn);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });




        mAuth = FirebaseAuth.getInstance();




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
                {
                    Toast.makeText(MainActivity.this, "All Field Are Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful())
                            {
                                startActivity(new Intent( MainActivity.this,home.class ));
                                finish();
                            }else {
                                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , regitratio.class));
            }
        });


    }
}