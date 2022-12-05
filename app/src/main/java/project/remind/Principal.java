package project.remind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import project.remind.modulos.*;

public class Principal extends AppCompatActivity {
    Principal principal;
    ScrollView scroll;
    LinearLayout categorias;
    Button nueva, modificar, eliminar, enlazar, rContrasena;
    Categoria cSeleccionada;
    Actividad aSeleccionada;
    Etiqueta eSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        principal = this;
        scroll = findViewById(R.id.scroll);
        categorias = findViewById(R.id.contenedor);
        nueva = findViewById(R.id.agregar);
        modificar = findViewById(R.id.modificar);
        eliminar = findViewById(R.id.eliminar);
        enlazar = findViewById(R.id.enlace);
        rContrasena = findViewById(R.id.rContrasena);

        nueva.setOnClickListener(clicAgregar());
        modificar.setOnClickListener(clicModificar());
        eliminar.setOnClickListener(clicEliminar());
        enlazar.setOnClickListener(clicEnlazar());
        rContrasena.setOnClickListener(clicRContrasena());
        mostrar(Button.VISIBLE, Button.GONE, Button.GONE, Button.GONE);

        new Controlador(this).sinNombre();
    }

    private View.OnClickListener clicPrincipal(){
        return new View.OnClickListener(){
            @Override public void onClick(View view){seleccionarPrincipal();}
        };
    }

    private View.OnClickListener clicAgregar(){
        return new View.OnClickListener(){
            @Override public void onClick(View view) {
                if(cSeleccionada != null) cSeleccionada.agregarActividad();
                else if(aSeleccionada != null) aSeleccionada.agregarEtiqueta();
                else agregarCategoria();
            }
        };
    }

    private View.OnClickListener clicModificar(){
        return new View.OnClickListener(){
            @Override public void onClick(View view) {
                if(cSeleccionada != null) cSeleccionada.sinNombre();
                else if(aSeleccionada != null) aSeleccionada.sinNombre();
                else eSeleccionada.sinNombre();
            }
        };
    }

    private View.OnClickListener clicEliminar(){
        return new View.OnClickListener(){
            @Override public void onClick(View view) {
                if(cSeleccionada != null) cSeleccionada.eliminar();
                else if(aSeleccionada != null) aSeleccionada.eliminar();
                else eSeleccionada.eliminar();
                seleccionarPrincipal();
            }
        };
    }

    private View.OnClickListener clicEnlazar(){
        return new View.OnClickListener() {
            @Override public void onClick(View view) {
                eSeleccionada.sinNombre3();
            }
        };
    }

    private View.OnClickListener clicRContrasena(){
        return new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent acceso = new Intent(principal, Acceso.class);

                if(new Controlador(principal).hayContrasena())
                    acceso.putExtra("pantalla", 2);
                else acceso.putExtra("pantalla", 1);
                principal.startActivity(acceso);
            }
        };
    }

    public void seleccionarPrincipal(){
        if(!desmarcar()) return;
        cSeleccionada = null;
        aSeleccionada = null;
        eSeleccionada = null;
        mostrar(Button.VISIBLE, Button.GONE, Button.GONE, Button.GONE);
    }

    public void seleccionarCategoria(Categoria categoria){
        if(!desmarcar()) return;
        cSeleccionada = categoria;
        aSeleccionada = null;
        eSeleccionada = null;
        mostrar(Button.VISIBLE, Button.VISIBLE, Button.VISIBLE, Button.GONE);
        resaltar(categoria);
    }

    public void seleccionarActividad(Actividad actividad){
        if(!desmarcar()) return;
        cSeleccionada = null;
        aSeleccionada = actividad;
        eSeleccionada = null;
        mostrar(Button.VISIBLE, Button.VISIBLE, Button.VISIBLE, Button.GONE);
        resaltar(actividad);
    }

    public void seleccionarEtiqueta(Etiqueta etiqueta){
        if(!desmarcar()) return;
        cSeleccionada = null;
        aSeleccionada = null;
        eSeleccionada = etiqueta;
        mostrar(Button.GONE, Button.VISIBLE, Button.VISIBLE, Button.VISIBLE);
        resaltar(etiqueta);
    }

    private void resaltar(View view){
        // Antes seleccionar
        if(cSeleccionada != null) cSeleccionada.setBackgroundResource(R.drawable.resaltar);
        else if(aSeleccionada != null) aSeleccionada.setBackgroundResource(R.drawable.resaltar);
        else if(eSeleccionada != null) eSeleccionada.setBackgroundResource(R.drawable.resaltar);
    }

    private boolean desmarcar(){
        Drawable fondo = categorias.getBackground();

        if(cSeleccionada != null){
            if(cSeleccionada.campoVisible()) return false;
            cSeleccionada.setBackgroundResource(R.drawable.fondo_normal); return true;
        }else if(aSeleccionada != null){
            if(aSeleccionada.campoVisible()) return false;
            aSeleccionada.setBackgroundResource(R.drawable.fondo_normal); return true;
        }else if(eSeleccionada != null){
            if(eSeleccionada.campoVisible()) return false;
            eSeleccionada.setBackgroundResource(R.drawable.fondo_normal); return true;
        }return true;
    }

    public void agregarCategoria(){agregarCategoria(null);}

    public void agregarCategoria(Categoria categoria){
        Categoria nueva;
        if(categoria != null){categorias.addView(categoria); return;}
        nueva = new Categoria(this,null, false, getApplicationContext());
        categorias.addView(nueva);
    }

    public LinearLayout categorias(){return categorias;}
    public Categoria cSeleccionada(){return cSeleccionada;}
    public Actividad aSeleccionada(){return aSeleccionada;}
    public Etiqueta eSeleccionada(){return eSeleccionada;}

    public void mostrar(int aVisible, int mVisible, int eVisible, int enVisible){
        nueva.setVisibility(aVisible);
        modificar.setVisibility(mVisible);
        eliminar.setVisibility(eVisible);
        enlazar.setVisibility(enVisible);
    }

    public void mostrar(){
        if(cSeleccionada != null)
            mostrar(Button.VISIBLE, Button.VISIBLE, Button.VISIBLE, Button.GONE);
        else if(aSeleccionada != null)
            mostrar(Button.VISIBLE, Button.VISIBLE, Button.VISIBLE, Button.GONE);
        else if(eSeleccionada != null)
            mostrar(Button.GONE, Button.VISIBLE, Button.VISIBLE, Button.VISIBLE);
    }

    public void ocultar(){
        mostrar(Button.GONE, Button.GONE, Button.GONE, Button.GONE);
    }
}