package project.remind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import project.remind.datos.RemindBD;

public class MainActivity extends AppCompatActivity {
    RemindBD baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent principal = new Intent(this, Principal.class);
        Intent acceso = new Intent(this, Acceso.class);
        boolean restringido; // Nuevo
        baseDatos = new RemindBD(getApplicationContext());

        restringido = baseDatos.seleccionarTContrasenas().getCount() > 0;
        String count = String.valueOf(baseDatos.seleccionarTContrasenas().getCount());

        /*Toast.makeText(
                getApplicationContext(),
                count,
                Toast.LENGTH_LONG
        ).show();*/

        if(restringido){
            acceso.putExtra("pantalla", 0);
            startActivity(acceso);
            return;
        }startActivity(principal);
    }
}