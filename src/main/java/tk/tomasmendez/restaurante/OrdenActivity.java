package tk.tomasmendez.restaurante;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.Serializable;

public class OrdenActivity extends AppCompatActivity implements Serializable{
private String ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_orden);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Intent que recibe los parametros desde MainActivity
        Intent intent = getIntent();
        ID = intent.getStringExtra(Config.TAG_ID);
        System.out.println(ID);

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }


            Bundle args = new Bundle();

            // Colocamos el String
            args.putString(Config.TAG_ID, ID);

            //Colocamos este nuevo Bundle como argumento en el fragmento.
            Fragment platosFragment = new ordenFragment();
            platosFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, platosFragment).commit();

        }



    }
}