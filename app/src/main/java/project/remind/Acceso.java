package project.remind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import project.remind.datos.RemindBD;

public class Acceso extends AppCompatActivity {
    Acceso acceso;
    LinearLayout campos;
    EditText contrasena, rContrasena;
    TextView /*eRContrasena,*/ etVentana;
    Button aceptar, cancelar;
    RemindBD baseDatos;
    int pantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);

        acceso = this;
        etVentana = findViewById(R.id.etVentana);
        campos = findViewById(R.id.contenedor);
        contrasena = findViewById(R.id.contrasena);
        rContrasena = findViewById(R.id.rContrasena);
//        eRContrasena = findViewById(R.id.eRContrasena);
        aceptar = findViewById(R.id.aceptar);
        cancelar = findViewById(R.id.cancelar);
        pantalla = Integer.parseInt(getIntent().getExtras().get("pantalla").toString());
        baseDatos = new RemindBD(getApplicationContext());

        if(pantalla == 0 || pantalla == 2) sinNombre();
        else sinNombre2();

        aceptar.setOnClickListener(clicAceptar());
        cancelar.setOnClickListener(clicCancelar());
    }

    private View.OnClickListener clicAceptar(){
        return new View.OnClickListener(){
            @Override public void onClick(View view) {
                Intent principal = new Intent(acceso, Principal.class);
                Intent modificar = new Intent(acceso, Acceso.class);

                modificar.putExtra("pantalla", 1);

                if(pantalla == 1 && contrasenaAgregada()) acceso.startActivity(principal);
                else if(pantalla == 0 && contrasenaConfirmada()) acceso.startActivity(principal);
                else if(pantalla == 2 && contrasenaEliminada()) acceso.startActivity(modificar);
            }
        };
    }

    private View.OnClickListener clicCancelar(){
        return new View.OnClickListener(){
            @Override public void onClick(View view) {
                acceso.startActivity(
                        new Intent(acceso, Principal.class)
                );
            }
        };
    }

    private void sinNombre(){
        String ventana = getResources().getString(R.string.eliminarContrasena);

        rContrasena.setVisibility(EditText.GONE);
//        eRContrasena.setVisibility(EditText.GONE);

        if(pantalla != 2) cancelar.setVisibility(Button.GONE);
        else etVentana.setText(ventana);
    }

    private void sinNombre2(){
        String ventana = getResources().getString(R.string.registrarContrasena);
        etVentana.setText(ventana);
    }

    private boolean contrasenaAgregada(){
        String contrasena1 = contrasena.getText().toString(),
               contrasena2 = rContrasena.getText().toString();

        if(contrasena1.equals("") || contrasena2.equals("")) {
            Mensaje.camposVacios(getApplicationContext());
            return false;
        }

        if(contrasena1.equals(contrasena2)) {
            baseDatos.modificarContrasena(new String[]{contrasena1});
            Mensaje.contrasenaAgregada(getApplicationContext());
            return true;
        }
        Mensaje.camposDiferentes(getApplicationContext()); return false;
    }

    private boolean contrasenaConfirmada(){
        String [] contrasena = new String[]{this.contrasena.getText().toString()};

        return baseDatos.seleccionarTContrasenas(contrasena).getCount() == 1;
    }

    private boolean contrasenaEliminada(){
        String [] contrasena = new String[]{this.contrasena.getText().toString()};

        if(baseDatos.seleccionarTContrasenas(contrasena).getCount() == 0) {
            Mensaje.contrasenaIncorrecta(getApplicationContext());
            return false;
        }

        baseDatos.modificarContrasena(new String[]{""});
        Mensaje.contrasenaEliminada(getApplicationContext());
        return true;
    }
}