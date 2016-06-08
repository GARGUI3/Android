package com.example.gargui3.faltanchelas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Stack;

import layout.fragment_ayuda;
import layout.fragment_datospago;
import layout.fragment_historial;
import layout.fragment_pedir;
import layout.fragment_perfil;

/*
Control de menu lateral mediante el uso de fragmentos
Correcto regreso en onBackPressed entre fragmentos respetando titulos y itemChecked
*/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Pila para el manejo de los items del menu en retroceso
    Stack<MenuItem> listaMenu = new Stack<MenuItem>();
    //Pila para el manejo de los id menu para el retroceso
    Stack<Integer> indices = new Stack<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Inicializando primer fragmento
        Fragment f = new fragment_pedir();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, f)
                .addToBackStack("inicial")
                .commit();

        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportActionBar().setTitle(navigationView.getMenu().getItem(0).getTitle());
        indices.push(0);
        listaMenu.push(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            listaMenu.pop();
            indices.pop();
            navigationView.getMenu().getItem(indices.peek()).setChecked(true);
            getSupportActionBar().setTitle(listaMenu.peek().getTitle());
            System.out.println("entro a mas 0");
        }
        else {
            System.out.println("entro a menos 0");
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        int idd = 0;

        switch (item.getItemId()) {
            case R.id.pedir:
                fragment = new fragment_pedir();
                fragmentTransaction = true;
                idd = 0;
                break;
            case R.id.historial:
                fragment = new fragment_historial();
                fragmentTransaction = true;
                idd = 1;
                break;
            case R.id.perfil:
                fragment = new fragment_perfil();
                fragmentTransaction = true;
                idd = 2;
                break;
            case R.id.datosPago:
                fragment = new fragment_datospago();
                fragmentTransaction = true;
                idd = 3;
                break;
            case R.id.ayuda:
                fragment = new fragment_ayuda();
                fragmentTransaction = true;
                idd = 4;
                break;
        }

        if(fragmentTransaction) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(item.getItemId() +item.getTitle().toString())
                    .commit();

            listaMenu.push(item);
            indices.push(idd);

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());

        }else {

            SharedPreferences prefs = getSharedPreferences("DatosChelas", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("token", "sintoken");
            editor.commit();

            Intent intent = new Intent(this, AccessActivity.class);
            this.finish();
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
