package com.Heladitos.forApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.Heladitos.forApp.R;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetPassword extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Dialog dialog;
    private TextInputEditText correoRecup;
    private String correo;
    boolean flag;
    Button mButtonreecuperar;
    FirebaseAuth mAuth;
    DatabaseReference reference;



    public void volveratras(View view) {
        Intent intent = new Intent(ResetPassword.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(ResetPassword.this);
        dialog = new Dialog(ResetPassword.this);

        correoRecup = findViewById(R.id.textInputEmail);
        mButtonreecuperar = findViewById(R.id.reestar);

        mButtonreecuperar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                correo = correoRecup.getText().toString().trim();
                flag=false;

                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    correoRecup.setError("Correo Inválido");
                    correoRecup.setFocusable(true);
                }

                if(!correo.isEmpty()){
                    correo = correoRecup.getText().toString().trim();

                    reference.child("Usuarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot datasnapshot : snapshot.getChildren()){
                                String emailData = datasnapshot.child("email").getValue().toString();

                                if(correo.equals(emailData)){
                                   flag=true;
                                }
                                }
                            if(!flag && Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                                ErrorData();
                            }
                            else{
                                getEnviarCorreo();
                            }
                            }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ResetPassword.this," Error al Recuperar Info DATA = >"+error+" .",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(ResetPassword.this,"Ingrese un Correo Eléctronico",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        Dialogo();
    }

    private void Dialogo() {
        Button okey;
        dialog.setContentView(R.layout.infor_reestablece_pass);
        okey = dialog.findViewById(R.id.oka);

        okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }


    private void getEnviarCorreo(){

        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Button okey;
                    dialog.setContentView(R.layout.revise_link);
                    okey = dialog.findViewById(R.id.oka_exitoso);

                    okey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(ResetPassword.this,MainActivity.class);
                            startActivity(intent);

                        }

                    });
                    dialog.setCancelable(false);
                    dialog.show();

                }
                /*
                else{
                    Toast.makeText(ResetPassword.this,"El LINK no se pudo Enviar",Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }


    private void ErrorData(){
        Button okey;
        dialog.setContentView(R.layout.error_info);
        okey = dialog.findViewById(R.id.oka_error);

        okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}





