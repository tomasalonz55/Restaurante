package tk.tomasmendez.restaurante;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NuevaOrdenActivity extends AppCompatActivity {
    //Inicializacion De Variebles
    private EditText editTextNumeroMesa;
    private EditText editTextNumeroPersonas;
    private String JSON_STRING;
    private String usuario;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_orden);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        ID = intent.getStringExtra(Config.TAG_ID);
        System.out.println(ID);

        editTextNumeroMesa =(EditText) findViewById(R.id.editTextNuevaOrdenNumeroMesa);
        editTextNumeroPersonas = (EditText) findViewById(R.id.editTextNuevaOrdenNumeroPersonas);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        editTextNumeroMesa.setText(ID);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabOrden);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(NuevaOrdenActivity.this,platosActivity.class);
                startActivity(intent);
                finish();
            }
        });



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }





    private void getJSONIDEmpleado(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NuevaOrdenActivity.this,"Cargando Platos","Espere...",false,false);
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
                String s = rh.sendGetRequestParam(Config.URL_GET_EMPLEADO,usuario);
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
}
