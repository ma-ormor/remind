package project.remind;

import android.content.Context;
import android.widget.Toast;

public class Mensaje {
    public static void camposVacios(Context context){
        Toast.makeText(context, "Campos vacíos", Toast.LENGTH_SHORT).show();
    }

    public static void camposDiferentes(Context context){
        Toast.makeText(context, "Los campos no coinciden", Toast.LENGTH_SHORT).show();
    }

    public static void contrasenaIncorrecta(Context context){
        Toast.makeText(context, "Intentelo de nuevo", Toast.LENGTH_SHORT).show();
    }

    public static void contrasenaAgregada(Context context){
        Toast.makeText(context, "Contraseña registrada", Toast.LENGTH_SHORT).show();
    }

    public static void contrasenaEliminada(Context context){
        Toast.makeText(context, "Contraseña eliminada", Toast.LENGTH_SHORT).show();
    }
}
