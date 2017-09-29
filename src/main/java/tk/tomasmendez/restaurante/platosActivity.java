package tk.tomasmendez.restaurante;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class platosActivity extends AppCompatActivity implements Serializable, ListView.OnItemClickListener{
    //Declaracion de variables

    private SwipeRefreshLayout refreshLayout;
    private String JSON_STRING;
    public Handler mHandler=new Handler();
    public ListView listView;
    public String ParametroPlato;
    public AutoCompleteTextView textViewPlatos;
    private String ID;

    List<String> platos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewPlatos = (AutoCompleteTextView) findViewById(R.id.TextViewPlatos);
        listView = (ListView) findViewById(R.id.listViewPlatos);
        //Listeners del Onclick y Onrefresh
        listView.setOnItemClickListener(this);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshPlatos);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getJSON();

            }
        });
        getJSON();

        //Intent que recibe los parametros desde MainActivity


        //arrar para el auto complete
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, platos);

        textViewPlatos.setAdapter(adapter);

        //Accion cuando se presiona el boton de buscar
        textViewPlatos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    ParametroPlato = textViewPlatos.getText().toString();
                    System.out.println(ParametroPlato);
                    getJSONPlato();
                    return true;
                }
                return false;
            }
        });

    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(platosActivity.this, NuevaOrdenActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String hid = map.get(Config.TAG_ID).toString();
        intent.putExtra(Config.TAG_ID,hid);
        startActivity(intent);
        finish();
    }



    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(platosActivity.this,"Cargando Platos","Espere...",false,false);
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
                String s = rh.sendGetRequest(Config.URL_GET_ALL_PLATOS);
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
                String id = jo.getString(Config.TAG_ID_PLATOS);
                String nombre = jo.getString(Config.TAG_NOMBRE_PLATOS);
                String precio = jo.getString(Config.TAG_PRECIO_PLATOS);
                String descripcion = jo.getString(Config.TAG_DESCRIPCION_PLATOS);
                String nombrecategoria = jo.getString(Config.TAG_NOMBRECATEGORIA_PLATOS);






                HashMap<String,String> comandas = new HashMap<>();
                comandas.put(Config.TAG_ID,id);
                comandas.put(Config.TAG_NOMBRE_PLATOS,nombre);
                comandas.put(Config.TAG_PRECIO_PLATOS,precio);
                comandas.put(Config.TAG_DESCRIPCION_PLATOS,descripcion);
                comandas.put(Config.TAG_NOMBRECATEGORIA_PLATOS,nombrecategoria);

                if(platos.contains(nombrecategoria))
                {}
                else
                {
                    platos.add(nombrecategoria);
                }


                platos.add(nombre);
                list.add(comandas);
                cont--;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(

                platosActivity.this, list, R.layout.list_item_plato,
                new String[]{Config.TAG_PRECIO_PLATOS,Config.TAG_NOMBRE_PLATOS},
                new int[]{R.id.id, R.id.name});

        listView.setAdapter(adapter);



    }

    private void getJSONPlato(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(platosActivity.this,"Cargando Platos","Espere...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                mostrarOrdenPlato();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_PLATO,ParametroPlato);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void mostrarOrdenPlato(){
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
                String id = jo.getString(Config.TAG_ID_PLATOS);
                String nombre = jo.getString(Config.TAG_NOMBRE_PLATOS);
                String precio = jo.getString(Config.TAG_PRECIO_PLATOS);
                String descripcion = jo.getString(Config.TAG_DESCRIPCION_PLATOS);
                String nombrecategoria = jo.getString(Config.TAG_NOMBRECATEGORIA_PLATOS);






                HashMap<String,String> comandas = new HashMap<>();
                comandas.put(Config.TAG_ID,id);
                comandas.put(Config.TAG_NOMBRE_PLATOS,nombre);
                comandas.put(Config.TAG_PRECIO_PLATOS,precio);
                comandas.put(Config.TAG_DESCRIPCION_PLATOS,descripcion);
                comandas.put(Config.TAG_NOMBRECATEGORIA_PLATOS,nombrecategoria);

                list.add(comandas);
                cont--;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(

                platosActivity.this, list, R.layout.list_item_plato,
                new String[]{Config.TAG_PRECIO_PLATOS,Config.TAG_NOMBRE_PLATOS},
                new int[]{R.id.id, R.id.name});

        listView.setAdapter(adapter);



    }


}
