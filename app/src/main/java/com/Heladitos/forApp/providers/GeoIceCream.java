package com.Heladitos.forApp.providers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.Heladitos.forApp.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoIceCream{

   private DatabaseReference mDatabase;
   private GeoFire mGeofire;
   GoogleMap mMap;

   FirebaseFirestore db = FirebaseFirestore.getInstance();
   FirebaseAuth mAuth;


    public GeoIceCream(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("HELADERÍAS");
        mGeofire = new GeoFire(mDatabase);
    }


    public void saveLocation(String name,LatLng latLng){
        mGeofire.setLocation(name, new GeoLocation(latLng.latitude, latLng.longitude));
    }


//Creamos este metodo para que almacene Data de los nombres y coordenadas de las HEladerias que se vayan agregando
 public void dbheladerias(){

        /*FORMATO...
      String id ="";
      LatLng heladeria = new LatLng();*/


     //________________LA GRANJA____________________
     String id1 = "Helados Artesanales de la maria";
     LatLng heladeria1 = new LatLng(-33.5237504,-70.6173798);
     String id2 = "Helados Artesanales Maricris";
     LatLng heladeria2 = new LatLng(-33.519880, -70.623834);
     String id3 = "Helados Art";
     LatLng heladeria3 = new LatLng(-33.517291, -70.625994);


     //__________MACUL___________________
     String id46 ="Heladería y Cafetería Arcoíris";
     LatLng heladeria46 = new LatLng(-33.494277, -70.601865);
     String id47 ="Gran gelato";
     LatLng heladeria47 = new LatLng(-33.491546, -70.599775);
     String id48 ="Las Delicias de Macul Heladería";
     LatLng heladeria48 = new LatLng(-33.485617, -70.596819);


//________________ÑUÑOA__________________________
     String id49 ="Palettas";
     LatLng heladeria49 = new LatLng(-33.465128, -70.598242);
     String id51 ="Emporio La Rosa";
     LatLng heladeria51 = new LatLng( -33.455238, -70.593047);
     String id52 ="Filippo";
     LatLng heladeria52 = new LatLng(-33.455195, -70.594296);
     String id53 ="Grido Helado";
     LatLng heladeria53 = new LatLng(-33.451717, -70.600339);








     //________________LA FLORIDA____________________
     String id14 = "Cremi Helados";
     LatLng heladeria14 = new LatLng(-33.524076, -70.577339);
     String id15 = "Delicia del Sur";
     LatLng heladeria15 = new LatLng(-33.535762, -70.593710);
     String id17 ="Rock & Blu Galactic Ice Cream";
     LatLng heladeria17 = new LatLng(-33.522884, -70.579555);
     String id18 ="Grido Helado";
     LatLng heladeria18 = new LatLng(-33.521733, -70.558464);
     String id19 ="Vive Gelatto";
     LatLng heladeria19 = new LatLng(-33.553571, -70.590951);
     String id20 ="Grido";
     LatLng heladeria20 = new LatLng(-33.550721, -70.578893);
     String id22 ="Churrería Heladería LA DULZURA DE AGUSTINA";
     LatLng heladeria22 = new LatLng(-33.553221, -70.583315);
     String id36 ="Grido Los Quillayes";
     LatLng heladeria36 = new LatLng(-33.561795, -70.593811);
     String id39 ="Todo Helados glotones";
     LatLng heladeria39 = new LatLng(-33.520827, -70.600193);
     String id40 ="Palettas";
     LatLng heladeria40 = new LatLng( -33.518434, -70.600343);
     String id45 ="YouServ";
     LatLng heladeria45 = new LatLng(  -33.510805, -70.608272);




//__________________PUENTE ALTO_________________________
     String id21 ="Grido Diego Portales";
     LatLng heladeria21 = new LatLng( -33.559069, -70.552162);
     String id37 ="Heladería Youserv Mall Tobalaba";
     LatLng heladeria37 = new LatLng(-33.567867, -70.557013);
     String id38 ="Palettas";
     LatLng heladeria38 = new LatLng(-33.5694312,-70.5572966);


//_________________SAN JOAQUÍN_____________________
     String id16 = "Helados La Ventisca";
     LatLng heladeria16 = new LatLng(-33.514196, -70.627144);
     String id8 = "Gelateria Dolce Pistacchio";
     LatLng heladeria8 = new LatLng(-33.478089, -70.634163);
     String id44 ="Mc Marces";
     LatLng heladeria44 = new LatLng(-33.513508, -70.627692);
     String id50 ="Angela Caro e Hijos Ltda";
     LatLng heladeria50 = new LatLng(-33.492094, -70.633666);









//_________________SAN RAMÓN_____________________
     String id4 = "Helados Madel";
     LatLng heladeria4 = new LatLng(-33.535263, -70.634692);
     String id5 = "Heladería JA";
     LatLng heladeria5 = new LatLng(-33.547915, -70.636759);

 //_________________SAN MIGUEL___________________
     String id7 = "Heladeria Mimi Gelato";
     LatLng heladeria7 = new LatLng(-33.502180, -70.656180);

 //____________________QUINTA NORMAL________________
     String id6 = "Heladería Deleite’s";
     LatLng heladeria6 = new LatLng(-33.440836, -70.704877);
     String id11 = "Heladería Deleite’s";
     LatLng heladeria11 = new LatLng(-33.434350, -70.680439);

 //___________________SANTIAGO___________________________
     String id9 = "LA HELADERIA DEL BARRIO";
     LatLng heladeria9 = new LatLng(-33.467542, -70.648730);
     String id10 = "Frigeri Helados Artesanales";
     LatLng heladeria10 = new LatLng(-33.457795, -70.649403);

   //_______________VITACURA_____________________________
     String id12 = "Heladeria Lärrs Vitacura";
     LatLng heladeria12 = new LatLng(-33.382999, -70.572652);

     //_________________MAIPÚ____________
     String id13 = "La Foca";
     LatLng heladeria13 = new LatLng(-33.5282574, -70.6349149);

     //_______________VIÑA DEL MAR_________________
     String id23 = "Heladerias Ice Cream Chile Limitada";
     LatLng heladeria23 = new LatLng(-33.025741, -71.547474);
     String id24 = "Santogelato";
     LatLng heladeria24 = new LatLng(-33.024942, -71.552814);
     String id25 = "Timbao";
     LatLng heladeria25 = new LatLng(-33.024967, -71.552923);
     String id26 = "Grido Helado";
     LatLng heladeria26 = new LatLng(-33.024194, -71.555509);
     String id27 = "Santogelato";
     LatLng heladeria27 = new LatLng(  -33.024421, -71.556315);
     String id28 = "Rockola Diner";
     LatLng heladeria28 = new LatLng(-33.024184, -71.558026);
     String id29 = "Helados Fresia Parra";
     LatLng heladeria29 = new LatLng( -33.028345, -71.575328 );
     String id30 = "Emporio La Rosa";
     LatLng heladeria30 = new LatLng( -33.0486729,-71.6036414 );
     String id31 = "Helados Artesanales Sabores que Hablan";
     LatLng heladeria31 = new LatLng( -33.048678, -71.613677);
     String id32 = "Yogufree";
     LatLng heladeria32 = new LatLng(-33.046427, -71.620557);
     String id33 = "Rosas Heladería";
     LatLng heladeria33 = new LatLng(-33.017930, -71.554272 );
     String id34 = "Affeto Heladeria y Cafetería Italiana";
     LatLng heladeria34 = new LatLng(-33.017467, -71.558033);
     String id35 = "Heladería Alicia";
     LatLng heladeria35 = new LatLng(-33.016972, -71.557787 );


     //________________SAN ANTONIO__________________________
     String id41 ="Enrohelados Aldo";
     LatLng heladeria41 = new LatLng(-33.611188, -71.610328);
     String id42 ="Heladeria Kofmanc";
     LatLng heladeria42 = new LatLng(-33.596637, -71.606528);
     String id43 ="Yogen Früz";
     LatLng heladeria43 = new LatLng(-33.5809322,-71.6140758);





/*FORMATO ...
 saveLocation(id, heladeria);
 */

     saveLocation(id1, heladeria1);
     saveLocation(id2, heladeria2);
     saveLocation(id4, heladeria4);
     saveLocation(id3, heladeria3);
     saveLocation(id5, heladeria5);
     saveLocation(id6, heladeria6);
     saveLocation(id7, heladeria7);
     saveLocation(id8, heladeria8);
     saveLocation(id9, heladeria9);
     saveLocation(id10, heladeria10);
     saveLocation(id11, heladeria11);
     saveLocation(id12, heladeria12);
     saveLocation(id13, heladeria13);
     saveLocation(id14, heladeria14);
     saveLocation(id15, heladeria15);
     saveLocation(id16, heladeria16);
     saveLocation(id17, heladeria17);
     saveLocation(id18, heladeria18);
     saveLocation(id19, heladeria19);
     saveLocation(id20, heladeria20);
     saveLocation(id21, heladeria21);
     saveLocation(id22, heladeria22);
     saveLocation(id23, heladeria23);
     saveLocation(id24, heladeria24);
     saveLocation(id25, heladeria25);
     saveLocation(id26, heladeria26);
     saveLocation(id27, heladeria27);
     saveLocation(id28, heladeria28);
     saveLocation(id29, heladeria29);
     saveLocation(id30, heladeria30);
     saveLocation(id31, heladeria31);
     saveLocation(id32, heladeria32);
     saveLocation(id33, heladeria33);
     saveLocation(id34, heladeria34);
     saveLocation(id35, heladeria35);
     saveLocation(id36, heladeria36);
     saveLocation(id37, heladeria37);
     saveLocation(id38, heladeria38);
     saveLocation(id39, heladeria39);
     saveLocation(id40, heladeria40);
     saveLocation(id41, heladeria41);
     saveLocation(id42, heladeria42);
     saveLocation(id43, heladeria43);
     saveLocation(id44, heladeria44);
     saveLocation(id45, heladeria45);
     saveLocation(id46, heladeria46);
     saveLocation(id47, heladeria47);
     saveLocation(id48, heladeria48);
     saveLocation(id49, heladeria49);
     saveLocation(id50, heladeria50);
     saveLocation(id51, heladeria51);
     saveLocation(id52, heladeria52);
     saveLocation(id53, heladeria53);

 }




    public GeoQuery getActiveice(LatLng latLng){
    GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude),5); //-> ,5
    geoQuery.removeAllListeners();
    return geoQuery;
}
}



