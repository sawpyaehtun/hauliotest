package com.example.user.hauliotest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.user.hauliotest.MainActivity.MY_PREFS_NAME;
import static com.example.user.hauliotest.MainActivity.googleApiClient;

public class Mapactivity extends AppCompatActivity {

    private GoogleMap mMap;
    TextView jn,name;
    ImageButton back,signout;
    de.hdodenhof.circleimageview.CircleImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_job);

        Bundle b=getIntent().getExtras();
        String jntemp=(String)b.get("jn");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        name=(TextView)findViewById(R.id.name);
        profilepic=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_pic);
        jn=(TextView)findViewById(R.id.jobnumber);
        jn.setText(jntemp);
        back=(ImageButton)findViewById(R.id.back);
        signout=(ImageButton)findViewById(R.id.signout);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog adb = comboxforquit();
                adb.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        String resname = prefs.getString("name", "nothing");
        String img_url=prefs.getString("photo", "nothing");
        name.setText(resname);

        Glide.with(this)
                .load(img_url)
                .apply(
                        new RequestOptions().override(150,150)
                                .error(R.drawable.error)
                                .centerCrop()
                )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //on load failed
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .into(profilepic);

    }

    private AlertDialog comboxforquit() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setMessage("Are you sure want to Quit?")
                .setCancelable(false)
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        googleApiClient.connect();
                        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {
                                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        finishAffinity();
                                    }
                                });
                            }

                            @Override
                            public void onConnectionSuspended(int i) {

                            }
                        });

                    }
                });
        AlertDialog ad = adb.create();
        return ad;
    }

    public void finish()
    {
        this.finish();
    }
}
