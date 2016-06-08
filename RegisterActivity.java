package com.example.gargui3.faltanchelas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/*
Ejemplo de registro de usuarios en un server nodeJS
Validacion de email
*/

public class RegisterActivity extends AppCompatActivity {

    String ip;

    private static final String emailValido = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("DatosChelas", Context.MODE_PRIVATE);
        this.ip = prefs.getString("ipaddress", "sinip");

        setContentView(R.layout.activity_register);
    }

    public void cancel(View view){
        Intent in = new Intent(this, AccessActivity.class);
        this.finish();
        startActivity(in);
    }

    //Valida email de email
    public static boolean validarEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(emailValido);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public void signup(View view){

        EditText phone = (EditText) findViewById(R.id.phone);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText repeatpassword = (EditText) findViewById(R.id.repeatpassword);

        final Intent intent = new Intent(this, AccessActivity.class);

        final View v = view;

        if(validarEmail(email.getText().toString())) {

            if(password.getText().toString().equals(repeatpassword.getText().toString())){

                JSONObject params = new JSONObject();
                StringEntity entity = null;
                try {
                    params.put("email", email.getText().toString());
                    params.put("password", password.getText().toString());
                    params.put("phone", phone.getText().toString());
                    entity = new StringEntity(params.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Context context = this.getApplicationContext();

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(context, ip + "/user", entity, "application/json", new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response is JSONObject instead of expected JSONArray

                        Object valor = null;
                        Object msj = null;
                        try {
                            valor = response.get("success");
                            msj = response.get("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // Do something with the response
                        System.out.println(valor);

                        if (valor == true) {
                            startActivity(intent);
                        } else {
                            Snackbar.make(v, msj.toString(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    }

                });
            }else{
                Snackbar.make(v, "Las contraseñas no coinciden", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


        }else{
            Snackbar.make(v, "Formato de correo invalido", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

}
