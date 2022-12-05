package project.remind.modulos;

import android.content.Context;
import android.database.Cursor;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import project.remind.Mensaje;
import project.remind.Principal;
import project.remind.R;
import project.remind.datos.RemindBD;

public class Actividad extends LinearLayout{
    HorizontalScrollView contenedor;
    LinearLayout etiquetas;
    Categoria categoria;
    Actividad actividad;
    TextView nombre;
    EditText campo;
    String datos[];
    Context context;
    RemindBD baseDatos;

    public Actividad(Categoria categoria, String datos[], boolean existe, Context context){
        super(context);

        this.categoria = categoria;
        this.context = context;
        actividad = this;
        nombre = new TextView(context);
        campo = new EditText(context);
        contenedor = new HorizontalScrollView(context);
        etiquetas = new LinearLayout(context);
        baseDatos = new RemindBD(context);
        this.datos = new String[3];
        this.datos[2] = categoria.datos[0];

        this.configurar();
        nombre.setTextSize(20);
        campo.setMaxLines(1);
        contenedor.addView(etiquetas);
        etiquetas.setOrientation(LinearLayout.HORIZONTAL);
        etiquetas.setPadding(50,0,0,0);
        nombre.setOnClickListener(clicNombre());
        campo.setOnEditorActionListener(enterCampo());

        if(!existe){
            principal().seleccionarActividad(this);
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
        super.addView(contenedor);
    }

    @Override public void addView(View child){etiquetas.addView(child);}
    @Override public void removeView(View child){etiquetas.removeView(child);}

    public void agregarEtiqueta(){agregarEtiqueta(null);}

    public void agregarEtiqueta(Etiqueta etiqueta){
        Etiqueta nueva;
        if(etiqueta != null){this.addView(etiqueta); return;}
        nueva = new Etiqueta(this,null, false, context);
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
        Cursor fila = baseDatos.seleccionarActividad();
        if(!fila.moveToFirst()) return;
        datos[0] = fila.getString(0);
    }

    private void insertar(){baseDatos.insertarActividad(datos);}
    // Mover a principal
    public void eliminar(){baseDatos.eliminarActividad(datos); categoria.removeView(this);}

    public void modificar(){baseDatos.modificarActividad(datos);}

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
                    //Toast.makeText(context, "Llegao"+datos[1]+" "+datos[2], Toast.LENGTH_SHORT).show();

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
                if(principal().aSeleccionada() != actividad)
                    principal().seleccionarActividad(actividad);
                else principal().seleccionarPrincipal();
            }
        };
    }

    public Principal principal(){return categoria.principal();}
    public boolean campoVisible(){return campo.getVisibility() == EditText.VISIBLE;}
}


