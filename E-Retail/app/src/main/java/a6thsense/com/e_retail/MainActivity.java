package a6thsense.com.e_retail;

import android.content.pm.PackageManager;

import android.location.Location;

import android.support.annotation.NonNull;

import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;

import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;

import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.Menu;

import android.view.MenuItem;

import android.view.View;

import android.view.ViewGroup;

import android.widget.TextView;

import android.widget.Toast;


import com.backendless.Backendless;

import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;

import com.backendless.exceptions.BackendlessFault;

import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoPoint;
import com.backendless.geo.geofence.IGeofenceCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.Status;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;
    BackendlessUser user;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    private static final long INTERVAL = 1000 * 1*60;
    private static final long FASTEST_INTERVAL = 1000 * 50;
    private GeoPoint mgeoPoint;



    @Override

    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        DataMine dataMine = new DataMine();
        dataMine.setItem_name("AirConditioning System");
        dataMine.setItem_quantity(5);


        Backendless.Persistence.save(dataMine, new DefaultCallback<DataMine>(this) {
            @Override
            public void handleResponse(DataMine response) {
                super.handleResponse(response);
                Toast.makeText(getApplicationContext(), "data saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                super.handleFault(fault);
            }
        });
    }
    private void getLocation(final Location location) {
        List<String> category = new ArrayList<String>();
        category.add("shop");
        user=Backendless.UserService.CurrentUser();
        if(user==null)
            Toast.makeText(MainActivity.this,"current user is null",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this,user.getUserId(),Toast.LENGTH_SHORT).show();

        BackendlessGeoQuery backendlessGeoQuery=new BackendlessGeoQuery();
        backendlessGeoQuery.addCategory("shop");
        backendlessGeoQuery.putMetadata("UserId",user.getUserId());
        backendlessGeoQuery.setIncludeMeta( true );


        Backendless.Geo.getPoints(backendlessGeoQuery, new AsyncCallback<BackendlessCollection<GeoPoint>>() {
            @Override
            public void handleResponse(BackendlessCollection<GeoPoint> geoPointBackendlessCollection) {
                updateLocation(geoPointBackendlessCollection.getCurrentPage(),location);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });




        /* Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("UserId",user.getUserId());

        Backendless.Geo.savePoint(location.getLatitude(), location.getLongitude(), category, metadata, new AsyncCallback<GeoPoint>() {

            @Override
            public void handleResponse(GeoPoint geoPoint) {
               Toast.makeText(MainActivity.this,"new Location saved",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(MainActivity.this,"error occured in saving the location",Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    public void updateLocation(List<GeoPoint> backendlessCollection,Location location){
        GeoPoint geoPoint=backendlessCollection.get(0);
        geoPoint.setLatitude(location.getLatitude()+0.000005);
        geoPoint.setLongitude(location.getLongitude()+0.00005);
        geoPoint.addMetadata("deviceId", Backendless.Messaging.DEVICE_ID);
        Backendless.Geo.savePoint(geoPoint, new AsyncCallback<GeoPoint>() {
            @Override
            public void handleResponse(GeoPoint geoPoint) {
                Toast.makeText(MainActivity.this,"geopoint Updated",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
            /*final AsyncCallback<GeoPoint> savecallback=new AsyncCallback<GeoPoint>() {
                @Override
                public void handleResponse(GeoPoint geoPoint) {

                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {

                }
            };
            IGeofenceCallback callback=new IGeofenceCallback() {
                @Override
                public void geoPointEntered(String s, String s1, double v, double v1) {
                    mgeoPoint.setLatitude(v);
                    mgeoPoint.setLongitude(v1);
                    Backendless.Geo.savePoint(mgeoPoint,savecallback);
                }
                @Override
                public void geoPointStayed(String s, String s1, double v, double v1) {
                    mgeoPoint.setLatitude(v);
                    mgeoPoint.setLongitude(v1);
                    Backendless.Geo.savePoint(mgeoPoint,savecallback);
                }

                @Override
                public void geoPointExited(String s, String s1, double v, double v1) {
                    mgeoPoint.setLatitude(v);
                    mgeoPoint.setLongitude(v1);
                    Backendless.Geo.savePoint(mgeoPoint,savecallback);

                }
            };*/

        });

    }

    /*private void startFenceMonitoring(GeoPoint geoPoint)
    {
            GeoPoint myLocation =geoPoint;

            myLocation.addMetadata( "deviceId", Backendless.Messaging.DEVICE_ID );

            AsyncCallback<Void> callback = new AsyncCallback<Void>()
            {
                @Override
                public void handleResponse( Void response )
                {
                    System.out.println( "Geofence monitoring has been started" );
                }

                @Override
                public void handleFault( BackendlessFault fault )
                {
                    System.out.println( "Error - " + fault.getMessage() );
                }
            };

            Backendless.Geo.startGeofenceMonitoring( myLocation, callback );

    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Toast.makeText(MainActivity.this,
                "connected",
                Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Toast.makeText(MainActivity.this, "Permission Not granted", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(MainActivity.this, "Permission Not granted", Toast.LENGTH_SHORT).show();
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Toast.makeText(MainActivity.this, "got Location", Toast.LENGTH_SHORT).show();

        if (mLastLocation != null) {

            Toast.makeText(MainActivity.this,
                    "your location is \n" + mLastLocation.getLatitude() + "\n" + mLastLocation.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            startLocationUpdates();
        }

    }

    protected void startLocationUpdates() {
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
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this,
                "connection failed",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "locatin changed", Toast.LENGTH_SHORT).show();
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        getLocation(location);
    }

    private void updateUI() {
        Toast.makeText(this, mCurrentLocation.getLatitude() + "", Toast.LENGTH_SHORT).show();
        Log.v("location", mCurrentLocation.getLongitude() + "");
        Log.v("time", mLastUpdateTime);
    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        Toast.makeText(MainActivity.this, "in start", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        Toast.makeText(MainActivity.this, "in disconnecting", Toast.LENGTH_SHORT).show();
        super.onStop();
    }
}