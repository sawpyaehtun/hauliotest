package com.example.user.hauliotest;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.hauliotest.MainActivity.googleApiClient;

public class Joblist extends AppCompatActivity {
    RequestQueue requestQueue;
    ArrayList<JobBean> list;
    ListView listView;
    ImageButton signout;
    private static final String RES_URL="https://api.myjson.com/bins/8d195.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joblist);

        signout=(ImageButton)findViewById(R.id.signout);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog adb = comboxforquit();
                adb.show();
            }
        });

        requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest request=new StringRequest(Request.Method.GET, RES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray arr = new JSONArray(response);
                    list = new ArrayList<JobBean>();
                    for(int i = 0; i < arr.length(); i++){
                        JSONObject lastarr=arr.getJSONObject(i).getJSONObject("geolocation");
                         list.add(new JobBean(arr.getJSONObject(i).getInt("id"),
                                arr.getJSONObject(i).getInt("job-id"),
                                arr.getJSONObject(i).getInt("priority"),
                                arr.getJSONObject(i).getString("company"),
                                arr.getJSONObject(i).getString("address"),
                                lastarr.getDouble("latitude"),
                                lastarr.getDouble("longitude")));

                    }
                    listView=(ListView)findViewById(R.id.list_view);
                    listView.setAdapter(new CustomListviewAdapter(Joblist.this));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Please Check Internet Connection",Toast.LENGTH_LONG).show();
            }
        })
        {

        };
        request.setShouldCache(false);
        requestQueue.add(request);


    }

    public class CustomListviewAdapter extends ArrayAdapter
    {
        Context context;
        public CustomListviewAdapter(Context context)
        {
            super(context,R.layout.lv,list);
            this.context=context;
        }

        @Override
        public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {

            LayoutInflater inflate=((Activity)context).getLayoutInflater();
            android.view.View listview=inflate.inflate(R.layout.lv,null);
            final TextView jobnumber=(TextView)listview.findViewById(R.id.job_number);
            TextView company=(TextView) listview.findViewById(R.id.company);
            TextView address=(TextView)listview.findViewById(R.id.address);
            Button btn=(Button)listview.findViewById(R.id.accept);

            jobnumber.setText(Integer.toString(list.get(position).getJob_id()
            ));
            company.setText(list.get(position).getCompany());
            address.setText(list.get(position).getAddress());

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Joblist.this,Mapactivity.class);
                    intent.putExtra("jn",jobnumber.getText().toString());
                    startActivity(intent);
                }
            });
            return listview;
        }
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
}
