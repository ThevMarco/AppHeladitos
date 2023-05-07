package com.Heladitos.forApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.Heladitos.forApp.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.Heladitos.forApp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    //View
    Button mButtonregister;
    TextInputEditText txtinputEmail;
    TextInputEditText txtinputPass;
    TextInputEditText txtinputName;

    public void volveratras(View view) {
        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        txtinputEmail = findViewById(R.id.txt_correo);
        txtinputName=findViewById(R.id.txt_namecompleto);
        txtinputPass=findViewById(R.id.txt_password);
        mButtonregister = findViewById(R.id.btn_register);




        mButtonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resgisterUser();
            }
        });
    }

void resgisterUser(){
        final String name = txtinputName.getText().toString();
        final String email= txtinputEmail.getText().toString();
        final String pass= txtinputPass.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtinputEmail.setError("Correo Inválido");
            txtinputEmail.setFocusable(true);
        }


        if(!name.isEmpty() && !email.isEmpty() && !pass.isEmpty()){
            if(pass.length()>=6){
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String id = mAuth.getCurrentUser().getUid();
                            saveUser(id,name,email);
                        }
                        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && !task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "¡ Correo ya se encuentra en Uso ! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            else{
                Toast.makeText(this, "La contraseña debe tener al menos 6 carácteres", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Complete los campos vacíos", Toast.LENGTH_SHORT).show();
        }

}


void saveUser(String id,String name, String email){
        Users users=new Users();
        users.setName(name);
        users.setEmail(email);

        mDatabase.child("Usuarios").child(id).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if(task.isSuccessful()){

               Toast.makeText(RegisterActivity.this, "Registro Éxitoso", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);

           }
           else{
               Toast.makeText(RegisterActivity.this, "Fallo el Registro, Problemas con su correo", Toast.LENGTH_SHORT).show();
           }
            }
        });
}
}