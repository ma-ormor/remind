package project.remind.modulos;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import project.remind.Mensaje;
import project.remind.Principal;
import project.remind.R;
import project.remind.datos.RemindBD;

public class Categoria extends LinearLayout{
    LinearLayout actividades, horizontal;
    Principal principal;
    Categoria categoria;
    TextView dibujo, nombre;
    EditText campo;
    String [] datos;
    Context context;
    RemindBD baseDatos;

    public Categoria(Principal principal, String [] datos, boolean existe, Context context){
        super(context);

        this.principal = principal;
        this.context = context;
        categoria = this;
        horizontal = new LinearLayout(context);
        dibujo = new TextView(context);
        nombre = new TextView(context);
        campo = new EditText(context);
        actividades = new LinearLayout(context);
        baseDatos = new RemindBD(context);
        this.datos = new String[2];

        this.configurar();
        //dibujo.setText("•");
        //dibujo.setTextSize(25);
        //dibujo.setPadding(0, 0, 10, 0);
        nombre.setTextSize(25);
        nombre.setTypeface(null, Typeface.NORMAL);
        campo.setMaxLines(1);
        actividades.setOrientation(LinearLayout.VERTICAL);
        actividades.setPadding(50,0,0,0);
        nombre.setOnClickListener(clicNombre());
        campo.setOnEditorActionListener(enterCampo());

        if(!existe){
            principal.seleccionarCategoria(this);
            sinNombre();
            return;
        }

        this.datos = datos;
        campo.setVisibility(EditText.GONE);
        nombre.setText(datos[1]);
    }

    private void configurar(){
        this.setOrientation(LinearLayout.VERTICAL);
        super.setBackgroundResource(R.drawable.fondo_normal);
        //super.setPadding(10, 10, 10, 10);
        super.addView(nombre);
        super.addView(campo);
        super.addView(actividades);
    }

    @Override public void addView(View child){actividades.addView(child);}
    @Override public void removeView(View child){actividades.removeView(child);}

    public void agregarActividad(){agregarActividad(null);}

    public void agregarActividad(Actividad actividad){
        Actividad nueva;
        if(actividad != null){this.addView(actividad); return;}
        nueva = new Actividad(this,null, false, context);
        this.addView(nueva);
    }

    public void sinNombre(){
        nombre.setVisibility(TextView.GONE);
        campo.setText(nombre.getText().toString());
        campo.setVisibility(TextView.VISIBLE);
        campo.requestFocus();
        principal().ocultar();
    }

    public void sinNombre2(){
        campo.setVisibility(TextView.GONE);
        nombre.setText(datos[1] = campo.getText().toString());
        nombre.setVisibility(TextView.VISIBLE);
        principal().mostrar();
    }

    private void actualizar(){
        Cursor fila = baseDatos.seleccionarCategoria();
        if(!fila.moveToFirst()) return;
        datos[0] = fila.getString(0);
    }

    private void insertar(){baseDatos.insertarCategoria(datos);}
// Mover a principal
    public void eliminar()
        {baseDatos.eliminarCategoria(datos); principal().categorias().removeView(this);}

    public void modificar(){baseDatos.modificarCategoria(datos);}

    private TextView.OnEditorActionListener enterCampo(){
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String texto = campo.getText().toString();

                campo.setText(texto.replace("\n", ""));

                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if(campo.getText().toString().equals(""))
                        {Mensaje.camposVacios(context); return false;}

                    sinNombre2();

                    if(datos[0] == null){insertar(); actualizar();}
                    else modificar();
                    return true;
                }return false;
            }
        };
    }

    private OnClickListener clicNombre(){
        return new OnClickListener() {
            @Override public void onClick(View view) {
                if (principal().cSeleccionada() != categoria)
                    principal().seleccionarCategoria(categoria);
                else principal().seleccionarPrincipal();
            }
        };
    }

    public Principal principal(){return principal;}

    public boolean campoVisible(){return campo.getVisibility() == EditText.VISIBLE;}
}


