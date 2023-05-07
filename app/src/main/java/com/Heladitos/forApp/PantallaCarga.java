package com.Heladitos.forApp;

import androidx.appcompat.app.AppCompatActivity;
import com.Heladitos.forApp.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.Heladitos.forApp.providers.GeoIceCream;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PantallaCarga extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    GeoIceCream geoIceCream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);


        mAuth=FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        geoIceCream = new GeoIceCream();
        savelohelados();

        //Esto se representa en segundos, que demorará la pantalla de carga.
        final int duration = 1500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    //Esto se ejecutara pasado los segundos que hemos establecido
                    Intent intent = new Intent(PantallaCarga.this, MainActivity.class);
                    startActivity(intent);
                    finish();

            }
        },duration);

    }

    private void savelohelados(){
        geoIceCream.dbheladerias();
    }

}