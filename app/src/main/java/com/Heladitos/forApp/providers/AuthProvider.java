package com.Heladitos.forApp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthProvider {
    FirebaseAuth mAuth;

    public AuthProvider(){mAuth=FirebaseAuth.getInstance();}

    public Task<AuthResult> register(String email, String pass){
        return mAuth.createUserWithEmailAndPassword(email, pass);
    }

    public Task<AuthResult> login(String email, String pass){
        return mAuth.signInWithEmailAndPassword(email, pass);
    }

    public void logout(){
        mAuth.signOut();
    }
}
