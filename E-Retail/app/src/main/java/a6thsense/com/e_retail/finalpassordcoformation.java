package a6thsense.com.e_retail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class finalpassordcoformation extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    EditText yourpassword;
    EditText yourconformpassword;
    Button yourpasswordregister;
    public static String password = null;
    public static String cnfpassword = null;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();

        setContentView(R.layout.activity_finalpassordcoformation);
        yourpassword = (EditText) findViewById(R.id.yours);
        yourconformpassword = (EditText) findViewById(R.id.youconfo);
        yourpasswordregister = (Button) findViewById(R.id.yourregister);
        final BackendlessUser backendlessUser = new BackendlessUser();
        yourpasswordregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = yourpassword.getText().toString();
                cnfpassword = yourconformpassword.getText().toString();
                if (password.length() == 0) {
                    Toast.makeText(finalpassordcoformation.this, "password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (password.equals(cnfpassword) == false) {
                    Toast.makeText(finalpassordcoformation.this, "password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    backendlessUser.setEmail(MailId.email);
                    backendlessUser.setPassword(finalpassordcoformation.password);
                    backendlessUser.setProperty("NAME", Name.name);
                    Backendless.UserService.register(backendlessUser, new DefaultCallback<BackendlessUser>(finalpassordcoformation.this) {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            super.handleResponse(response);
                            addlocation(response);
                            startActivity(new Intent(finalpassordcoformation.this, logscreen.class));
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            super.handleFault(fault);
                        }
                    });
                }
            }
        });
    }


    private void addlocation(BackendlessUser backendlessUser) {
        List<String> category = new ArrayList<String>();
        category.add("shop");
        //user=Backendless.UserService.CurrentUser();
        if (backendlessUser == null)
            Toast.makeText(finalpassordcoformation.this, "current user is null", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(finalpassordcoformation.this, backendlessUser.getEmail(), Toast.LENGTH_SHORT).show();

        Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("UserId", backendlessUser.getUserId());
        while (mLastLocation == null) {

        }

        Backendless.Geo.savePoint(mLastLocation.getLatitude(), mLastLocation.getLongitude(), category, metadata, new AsyncCallback<GeoPoint>() {
            @Override
            public void handleResponse(GeoPoint geoPoint) {

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
            }
        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }


    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
