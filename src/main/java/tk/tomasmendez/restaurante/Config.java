package tk.tomasmendez.restaurante;

/**
 * Created by tomasmendez on 29/07/17.
 */

public class Config {
    public static final String ip="192.168.1.6/";
    //URL to our login.php file
    public static final String LOGIN_URL = "http://"+ip+"restaurante/php/login.php";
    public static final String URL_GET_ALL = "http://"+ip+"restaurante/php/verTodasOrdenesActivas.php";
    public static final String URL_GET_PLATO = "http://"+ip+"restaurante/php/verPlato.php?NOMBRE=";
    public static final String URL_GET_ALL_PLATOS = "http://"+ip+"restaurante/php/verPlatos.php";
    public static final String URL_GET_EMPLEADO = "http://"+ip+"restaurante/php/verEmpleado.php?NOMBRE=";
    public static final String URL_CREAR_ORDEN = "http://"+ip+"restaurante/php/crearOrden.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "NICK";
    public static final String KEY_PASSWORD = "PASS";
    public static String id_empleado;

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    //ORDEN
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "ID_COMANDA";
    public static final String TAG_MESA = "MESA";
    public static final String TAG_PERSONAS = "PERSONAS";
    public static final String TAG_NOTA = "NOTA";
    public static final String TAG_VISTO = "VISTO";
    public static final String TAG_CANTIDAD = "CANTIDAD";
    public static final String TAG_ENTREGADO = "ENTREGADO";
    public static final String TAG_NOMBRE = "NOMBRE";
    public static final String TAG_PRECIO = "PRECIO";

    //Platos
    public static final String TAG_ID_PLATOS = "ID_PLATO";
    public static final String TAG_NOMBRE_PLATOS = "NOMBRE";
    public static final String TAG_PRECIO_PLATOS = "PRECIO";
    public static final String TAG_DESCRIPCION_PLATOS = "DESCRIPCION";
    public static final String TAG_NOMBRECATEGORIA_PLATOS ="NOMBRECATEGORIA";

    //Empleado
    public static final String TAG_ID_EMPLEADO="ID_EMPLEADO";

}
