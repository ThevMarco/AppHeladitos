package com.Heladitos.forApp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import com.Heladitos.forApp.R;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Heladitos.forApp.providers.AuthProvider;
import com.Heladitos.forApp.providers.GeoIceCream;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapInvitado extends AppCompatActivity implements OnMapReadyCallback {

    FirebaseAuth mAuth;
    DatabaseReference ref;

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    Toolbar mToolbar;
    private AuthProvider mAuthProvider;
    com.google.android.gms.location.LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private GeoIceCream mGeoIceCream;
    private LatLng mCurrentLatLng;
    private List<Marker> mMarcadores = new ArrayList<>();
    private boolean mIsFirsTime=true;
    View mVisibilidad;


    private BottomSheetBehavior mBottomSheetBehavior1;
    LinearLayout tapactionlayout;
    View bottomSheet;
    TextView mTextNameIce, mTextDirectionIce, mTextDistanceIce;
    private LatLng latmarcador;
    Button mButtonIR;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private ProgressDialog progressDialog;




    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {



            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    //Obtener la localización del usuario en tiempo real.
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(16f)
                                    .build()
                    ));
                    mCurrentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
                    if(mIsFirsTime){
                        mIsFirsTime=false;
                        getActiveIceCream();
                    }
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Heladitos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        progressDialog=new ProgressDialog(this);




        mAuthProvider = new AuthProvider();
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        ref = FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();


        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mGeoIceCream=new GeoIceCream();

        mButtonIR=findViewById(R.id.buttonIR);
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);




        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
        startLocation();


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mVisibilidad = findViewById(R.id.bottomJsoft);
                mVisibilidad.setVisibility(View.INVISIBLE);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                mVisibilidad = findViewById(R.id.bottomJsoft);
                mVisibilidad.setVisibility(View.VISIBLE);


                MarcadorInforma();
                String LatMarker = String.valueOf(marker.getPosition().latitude);
                String LongMarker= String.valueOf(marker.getPosition().longitude);
                String data[] = {LatMarker,LongMarker};




                double markLati = marker.getPosition().latitude;
                double markLong = marker.getPosition().longitude;
                latmarcador= new LatLng(markLati,markLong);
                double distance = SphericalUtil.computeDistanceBetween(mCurrentLatLng,latmarcador);

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> directions = geocoder.getFromLocation(markLati,markLong,1);
                    mTextDirectionIce.setText(directions.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }




                //INFO FALTANTE. SABER COMO LA APP SABRA EL MARCADOR QUE APRETEMOS...
                //PARA OBTENER LOS DATOS DE FIREBASE REALTIME, EN NUESTRO DIALOG
                ref.child("HELADERÍAS").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot datosnapshot : snapshot.getChildren()){
                            String val = datosnapshot.toString();
                            String latitud =datosnapshot.child("l").child("0").getValue().toString();
                            String longitud = datosnapshot.child("l").child("1").getValue().toString();

                            if(LatMarker.equals(latitud)&&LongMarker.equals(longitud)){
                                if(val.contains(LatMarker)&&val.contains(LongMarker)) {
                                    String namesICE = datosnapshot.getKey();
                                    mTextNameIce.setText(namesICE);
                                    Log.i(TAG, "LA DISTANCIA ES : \n " + String.format("%.3f", distance / 1000) + "km aprox.");
                                    mTextDistanceIce.setText(String.format("%.3f", distance / 1000) + "km aprox.");
                                    mButtonIR.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+latitud+","+longitud));
                                            intent.setPackage("com.google.android.apps.maps");

                                            if(intent.setPackage("com.google.android.apps.maps")==null){
                                                Intent intentBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/"+latitud+","+longitud));
                                                startActivity(intentBrowser);
                                            }

                                            else if(intent.resolveActivity(getPackageManager())!=null){
                                                startActivity(intent);
                                            }

                                        }
                                    });
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                return false;
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==event.KEYCODE_BACK){

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);

        }
        return super.onKeyDown(keyCode, event);
    }






    private void getActiveIceCream(){
        mGeoIceCream.getActiveice(mCurrentLatLng).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                //Añadiremos Marcadores de las heladerías que se encuentren disponibles.

                for(Marker marker: mMarcadores){
                    if(marker.getTag()!=null) {
                        if (marker.getTag().equals(key)) {
                            return;
                        }
                    }
                }
                LatLng IceCreamLng=new LatLng(location.latitude,location.longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(IceCreamLng).title("Heladería").icon(BitmapDescriptorFactory.fromResource(R.drawable.iconito)));
                marker.setTag(key);
                mMarcadores.add(marker);


            }
            @Override
            public void onKeyExited(String key) {}
            @Override
            public void onKeyMoved(String key, GeoLocation location) {}
            @Override
            public void onGeoQueryReady() {}
            @Override
            public void onGeoQueryError(DatabaseError error) {}
        });

    }

    private void MarcadorInforma(){

        View headerLayout1 = findViewById(R.id.bottomJsoft);
        mTextNameIce = (TextView)findViewById(R.id.txtNombreIce);
        mTextDirectionIce = (TextView)findViewById(R.id.txtDireccionIce);
        mTextDistanceIce = (TextView)findViewById(R.id.txtdistanceIce);
        tapactionlayout =(LinearLayout)findViewById(R.id.tap_action_layout);
        bottomSheet =  findViewById(R.id.bottomJsoft);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setPeekHeight(120);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    tapactionlayout.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    tapactionlayout.setVisibility(View.GONE);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    tapactionlayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                tapactionlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mBottomSheetBehavior1.getState()==BottomSheetBehavior.STATE_COLLAPSED)
                        {
                            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }
                });
            }
        });
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                    else{
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermission();
                }
            } else {
                checkLocationPermission();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
        else{
            showAlertDialogNOGPS();
        }
    }

    private void showAlertDialogNOGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor Activa tu ubicación para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),SETTINGS_REQUEST_CODE );
                    }
                }).create().show();
    }









    private boolean gpsActived(){
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            isActive = true;
        }
        return isActive;
    }








    private void startLocation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(gpsActived()) {

                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
                else{
                    showAlertDialogNOGPS();
                }
            }
            else{
                checkLocationPermission();
            }
        }
        else{
            if(gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            }
            else{
                showAlertDialogNOGPS();
            }
        }
    }




    private void checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicación requiere de los permisos de ubicación para poder utilizarse.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MapInvitado.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(MapInvitado.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        getMenuInflater().inflate(R.menu.menu_invitado, menu);

        return super.onCreateOptionsMenu(menu);
    }


}

