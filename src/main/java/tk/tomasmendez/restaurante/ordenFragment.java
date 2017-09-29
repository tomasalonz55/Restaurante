package tk.tomasmendez.restaurante;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ordenFragment extends Fragment {



    private TextView texto;
    public Button boton;
    private String ID;

    public ordenFragment() {
        // Required empty public constructor
    }

    //El fragment se ha adjuntado al Activity

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_orden,
                container, false);

        //Recibimos los agrumentos mandados en el Fragmento Anterior y los Guardamos en Variables.
        String texto2 = getArguments().getString(Config.TAG_ID);
        texto = (TextView) view.findViewById(R.id.textView4);
        texto.setText(texto2);
        /*boton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                platosFragment s2 = new platosFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container,s2); // f1_container is your FrameLayout container
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            }
        });*/
        return view;
    }


}
