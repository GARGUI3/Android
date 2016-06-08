package com.example.gargui3.faltanchelas;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/*
Ejemplo de login con conexion a server nodeJS
Almacenamiento de datos en SharedPreferences como token para peticiones
Y Manejo de Errores
*/

public class AccessActivity extends AppCompatActivity {

    String ip = "TU_IP_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("DatosChelas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ipaddress", ip);
        editor.commit();

        setContentView(R.layout.activity_access);

        String s = prefs.getString("token", "sintoken");

        if(!s.equals("sintoken")){
            Intent in = new Intent(this, MainActivity.class);
            finish();
            startActivity(in);
        }

    }

    //Vista para registrarte
    public void signup(View view) {
        Intent in = new Intent(this, RegisterActivity.class);
        startActivity(in);
    }

    public void signin(final View view) {

        EditText email = (EditText) findViewById(R.id.emailLogin);
        EditText password = (EditText) findViewById(R.id.passwordLogin);

        final Intent intent = new Intent(this, MainActivity.class);

        final View v = view;
        final String correo = email.getText().toString();

        JSONObject params = new JSONObject();
        StringEntity entity = null;
        try {
            params.put("email", email.getText().toString());
            params.put("password", password.getText().toString());
            entity = new StringEntity(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Context context = this.getApplicationContext();

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, ip + "/authenticate", entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray

                Object valor = null;
                Object msj = null;
                Object tkn = null;
                JSONObject user = null;
                try {
                    valor = response.get("success");
                    msj = response.get("message");
                    tkn = response.get("token");
                    user = response.getJSONObject("user");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // Do something with the response
                System.out.println(valor);

                if (valor == true) {
                    String idd = "";
                    try {
                        idd = user.getString("_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences prefs = getSharedPreferences("DatosChelas", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("token", tkn.toString());
                    editor.putString("correo", correo);
                    editor.putString("idUser", idd);
                    editor.commit();
                    finish();
                    startActivity(intent);

                } else {
                    Snackbar.make(v, msj.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }

        });
    }
}
