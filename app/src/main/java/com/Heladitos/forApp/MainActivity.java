package com.Heladitos.forApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.Heladitos.forApp.models.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.Heladitos.forApp.R;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignClient;
    private final static int RC_SIGN_IN = 123;

    TextInputEditText mtextInputEmail;
    TextInputEditText mtextInputPass;
    Button mButtonLogin;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference mDatabase;
    Button mloginGoogle, mButtonFLASH;
    private Dialog dialog;
    private ProgressDialog progressDialog;



    public void olvideclave(View view){
        Intent intent = new Intent(MainActivity.this,ResetPassword.class);
        startActivity(intent);
    }

    public void registrarme(View view) {
        Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        verificacionLogin();
    }


    private void verificacionLogin(){
        if(firebaseUser != null){
            startActivity(new Intent(MainActivity.this,MapActivity.class));
            finish();
        }
    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextInputEmail = findViewById(R.id.textInputEmail);
        mtextInputPass = findViewById(R.id.textInputPassword);
        mButtonLogin = findViewById(R.id.iniciosesion);
        mloginGoogle = findViewById(R.id.iniciar_con_google);
        mButtonFLASH = findViewById(R.id.ingresoFash);
        dialog = new Dialog(this);
        progressDialog =new ProgressDialog(this);


        mAuth=FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        crearSolicitud();

        mButtonFLASH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Buscando Heladerías ...");
                progressDialog.show();

                Intent intent=new Intent(MainActivity.this,MapInvitado.class);
                startActivity(intent);

            }
        });


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mloginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }









    //SOLICITUD
    private void crearSolicitud() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Creamos un googlesigninclient con las opciones especificadas por GSO
        mGoogleSignClient= GoogleSignIn.getClient(this,gso);
    }


    //Creamos la pantalla de Google
    private void signIn(){
        Intent signIntent = mGoogleSignClient.getSignInIntent();
        startActivityForResult(signIntent,RC_SIGN_IN);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Resultado devuelto al iniciar la intencion desde GoogleSignInApi.getSignIntent
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //El inicio de sesion fue exitoso
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AutenticationFirebase(account); //-----> Aqui se ejecuta el método para logearnos con Google


            }catch (ApiException e){
                Toast.makeText(this,"Google signin is Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Método para autenticar con FIREBASE->Google
    private void AutenticationFirebase(GoogleSignInAccount account) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Iniciando Sesíon ...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful()){
                            //Si inició correctamente
                            FirebaseUser user = mAuth.getCurrentUser(); //--> Obtenemos al usuario que quiere iniciar sesión
                            if(user != null){
                                String usuarioName = user.getDisplayName();
                                Toast.makeText(MainActivity.this, "Bienvenido "+usuarioName+" :)", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            //Si el usuario inicia sesion por primera vez
                            if(task.getResult().getAdditionalUserInfo().isNewUser()){

                                String id = user.getUid();
                                String name = user.getDisplayName();
                                String email = user.getEmail();

                                Users users=new Users();
                                users.setName(name);
                                users.setEmail(email);

                                mDatabase.child("Usuarios").child(id).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MainActivity.this, "Registro Éxitoso :)", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this,MapActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this, "Fallo el Registro, Problemas con su correo", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }





    private void login() {
        String email= mtextInputEmail.getText().toString();
        String pass=mtextInputPass.getText().toString();


        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mtextInputEmail.setError("Correo Inválido");
            mtextInputEmail.setFocusable(true);
        }

        if(!email.isEmpty() && !pass.isEmpty()){
            if(pass.length()>=6){

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Ingreso Exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,MapActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "El correo Eléctronico o la contraseña, son Incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else{
                Toast.makeText(this, "La contraseña debe tener como mínimo 6 carácteres", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "El correo eléctronico y la contraseña son Obligatorios", Toast.LENGTH_SHORT).show();
        }

    }


}