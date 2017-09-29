package tk.tomasmendez.restaurante;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements  Serializable, ListView.OnItemClickListener {
    //Inicialización de variables
    private TextView textView;
    private SwipeRefreshLayout refreshLayout;
    private String JSON_STRING;
    public Handler mHandler=new Handler();
    public ListView listView;
    public String empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//Initializing textview
        textView = (TextView) findViewById(R.id.textView);

        //Recibiendo la informacion del inicio de sesion
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        empleado = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        //Inicializacion de variables
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        listView = (ListView) findViewById(R.id.listView);
        //Listeners del Onclick y Onrefresh
        listView.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getJSON();

            }
        });
        getJSON();
        //Showing the current logged in email to textview
        String cap = empleado.substring(0, 1).toUpperCase() + empleado.substring(1).toLowerCase();
        textView.setText("Bienvenido: " + cap);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //con este boton se creara una nueva orden

                Intent intent = new Intent(MainActivity.this,NuevaOrdenActivity.class);

                startActivity(intent);
                finish();



            }
        });

        //doTheAutoRefresh();
    }

//Se encarga de refrescar la pagina y hacer el query de nuevo para verificar si hay datos nuevos
    private void doTheAutoRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                getJSON();
                doTheAutoRefresh();

            }
        }, 25000);
    }
//Empieza el proceso de query para recibir los datos en los archivos php

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Cargando Órdenes","Espere...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                mostrarOrden();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }



    private void mostrarOrden(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            //El cont se utiliza para que el primer elemento de la lista sea el mas nuevo
            int cont =result.length();
            for(int i = 0; i<result.length(); i++){
                System.out.println(result.length()-1+" "+cont);
                JSONObject jo = result.getJSONObject(cont-1);
                String id = jo.getString(Config.TAG_ID);
                String mesa = jo.getString(Config.TAG_MESA);
                String personas = jo.getString(Config.TAG_PERSONAS);
                String nota = jo.getString(Config.TAG_NOTA);
                String visto = jo.getString(Config.TAG_VISTO);






                HashMap<String,String> comandas = new HashMap<>();
                comandas.put(Config.TAG_ID,id);
                comandas.put(Config.TAG_MESA,mesa);
                comandas.put(Config.TAG_PERSONAS,personas);
                comandas.put(Config.TAG_NOTA,nota);
                comandas.put(Config.TAG_VISTO,visto);


                list.add(comandas);
                cont--;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(

                MainActivity.this, list, R.layout.list_item,
                new String[]{Config.TAG_ID,Config.TAG_MESA},
                new int[]{R.id.id, R.id.name});

        listView.setAdapter(adapter);



    }

    private void getJSONIDEmpleado(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Cargando Platos","Espere...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                obtenerIDEmpleado();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_EMPLEADO, empleado);

                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void obtenerIDEmpleado(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            //El cont se utiliza para que el primer elemento de la lista sea el mas nuevo
            int cont =result.length();
            for(int i = 0; i<result.length(); i++){
                System.out.println(result.length()-1+" "+cont);
                JSONObject jo = result.getJSONObject(cont-1);
                String id = jo.getString(Config.TAG_ID_EMPLEADO);



                HashMap<String,String> comandas = new HashMap<>();
                comandas.put(Config.TAG_ID_EMPLEADO,id);


                list.add(comandas);
                cont--;


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }












    //crear orden

    private void crearEmpleado(){





        class CrearEmpleado extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Creando...","Espere...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.TAG_ID_EMPLEADO,empleado);


                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_CREAR_ORDEN, params);
                return res;
            }
        }

        CrearEmpleado ae = new CrearEmpleado();
        ae.execute();
    }








    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Desea cerrar sesión?");
        alertDialogBuilder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            //calling logout method when the logout button is clicked
            logout();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(MainActivity.this, OrdenActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String hid = map.get(Config.TAG_ID).toString();
        intent.putExtra(Config.TAG_ID,hid);
        startActivity(intent);
        finish();

    }


}
