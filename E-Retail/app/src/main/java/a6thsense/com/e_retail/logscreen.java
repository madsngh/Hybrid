package a6thsense.com.e_retail;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class logscreen extends AppCompatActivity  {

    EditText emailid;
    EditText myownpassword;
    Button mylogin_button;
    TextView forgotpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        Backendless.Messaging.registerDevice(Defaults.SENDER_ID, "Default", new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Device Registered Sucessfully",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });


        setContentView(R.layout.activity_logscreen);
        emailid = (EditText) findViewById(R.id.email);
        myownpassword = (EditText) findViewById(R.id.mypassword);
        mylogin_button = (Button) findViewById(R.id.mylogin);
        forgotpassword = (TextView) findViewById(R.id.myforgotpassword);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //         startActivity(new Intent(logscreen.this,forgot_pass.class));
            }
        });
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            setActionbarTextColor(actionBar, Color.BLACK);
            actionBar.setElevation(0);
        } else {
            Toast.makeText(logscreen.this, "action bar null", Toast.LENGTH_SHORT).show();
        }
    }

    private void setActionbarTextColor(android.support.v7.app.ActionBar actBar, int color) {

        String title = actBar.getTitle().toString();
        Spannable spannablerTitle = new SpannableString(title);
        spannablerTitle.setSpan(new ForegroundColorSpan(color), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actBar.setTitle(spannablerTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setMenuBackground();
        return true;
    }

    private void setMenuBackground() {


        LayoutInflater layoutInflater = getLayoutInflater();
        final LayoutInflater.Factory existingFactory = layoutInflater.getFactory();
// use introspection to allow a new Factory to be set
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
            getLayoutInflater().setFactory(new LayoutInflater.Factory() {
                @Override
                public View onCreateView(String name, final Context context, AttributeSet attrs) {
                    View view = null;
                    // if a factory was already set, we use the returned view
                    if (existingFactory != null) {
                        view = existingFactory.onCreateView(name, context, attrs);
                    }
                    view.setBackgroundResource(android.R.color.black);
                    ((TextView) view).setTextColor(Color.BLACK);
                    ((TextView) view).setTextSize(18);
                    return view;
                }
            });
        } catch (NoSuchFieldException e) {
            // ...
        } catch (IllegalArgumentException e) {
            // ...
        } catch (IllegalAccessException e) {
            // ...
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (R.id.signup == id) {
            startActivity(new Intent(logscreen.this, Name.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void loginbutton(View v) {
        Backendless.UserService.login(emailid.getText().toString(), myownpassword.getText().toString(), new DefaultCallback<BackendlessUser>(logscreen.this) {
            @Override
            public void handleResponse(BackendlessUser response) {
                super.handleResponse(response);

                startActivity(new Intent(logscreen.this, MainActivity.class));
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                super.handleFault(fault);
            }
        }, true);
    }


}