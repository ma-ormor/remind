package project.remind.modulos;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.FileUriExposedException;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.File;

import project.remind.BuildConfig;
import project.remind.Mensaje;
import project.remind.Principal;
import project.remind.R;
import project.remind.datos.RemindBD;

public class Etiqueta extends LinearLayout{
    //LinearLayout etiquetas;
    Actividad actividad;
    Etiqueta etiqueta;
    TextView nombre;
    EditText campo;
    String datos[];
    RemindBD baseDatos;;
    Context context;

    public Etiqueta(Actividad actividad, String datos[], boolean existe, Context context){
        super(context);

        this.actividad = actividad;
        etiqueta = this;
        nombre = new TextView(context);
        campo = new EditText(context);
        baseDatos = new RemindBD(context);
        this.context = context;
        this.datos = new String[4];
        this.datos[3] = actividad.datos[0];

        this.configurar();
        //nombre.setMinimumHeight(campo.getMinimumHeight());
        nombre.setTextSize(15);
        nombre.setTypeface(null, Typeface.BOLD);
        nombre.setOnClickListener(clicNombre());
        campo.setMinimumWidth(200);
        campo.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
        campo.setMaxLines(1);
        nombre.setOnLongClickListener(sostenerNombre());
        campo.setOnEditorActionListener(enterCampo());

        if(!existe){
            principal().seleccionarEtiqueta(this);
            sinNombre();
            return;
        }

        this.datos = datos;
        campo.setVisibility(EditText.GONE);
        nombre.setText(datos[1]);
    }

    private void configurar(){
        this.setOrientation(LinearLayout.HORIZONTAL);
        super.setBackgroundResource(R.drawable.fondo_normal);
        //super.setPadding(30, 10, 30, 10);
        super.addView(nombre);
        super.addView(campo);
        //super.addView(etiquetas);
    }

//    @Override public void addView(View child){etiquetas.addView(child);}
//    @Override public void removeView(View child){etiquetas.removeView(child);}

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

    public void sinNombre3(){
        campo.setVisibility(TextView.VISIBLE);
        campo.setText(datos[2]);
        principal().ocultar();
    }

    public void sinNombre4(){
        datos[2] = campo.getText().toString();
        campo.setVisibility(TextView.GONE);
        Toast.makeText(principal().getApplicationContext(), datos[2], Toast.LENGTH_SHORT).show();
        principal().mostrar();
    }

    private void actualizar(){
        Cursor fila = baseDatos.seleccionarEtiqueta();
        if(!fila.moveToFirst()) return;
        datos[0] = fila.getString(0);
    }

    private void insertar(){baseDatos.insertarEtiqueta(datos);}
    // Mover a principal
    public void eliminar()
        {baseDatos.eliminarEtiqueta(datos); actividad.removeView(this);}

    public void modificar(){baseDatos.modificarEtiqueta(datos);}

    private TextView.OnEditorActionListener enterCampo(){
        return new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String texto = campo.getText().toString();
                boolean visible;

                campo.setText(texto.replace("\n", ""));

                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    visible = nombre.getVisibility() == View.VISIBLE;

                    if(campo.getText().toString().equals("") && !visible)
                    {Mensaje.camposVacios(context); return false;}

                    if(visible) sinNombre4();
                    else sinNombre2();

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
                if(principal().eSeleccionada() != etiqueta)
                    principal().seleccionarEtiqueta(etiqueta);
                else principal().seleccionarPrincipal();
            }
        };
    }

    private OnLongClickListener sostenerNombre(){
        return new OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override public boolean onLongClick(View view) {
                if(datos[2].equals("") || datos[2].equals("\n")) {
                    Toast.makeText(context, "Sin enlace", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if(datos[2].startsWith("https://") || datos[2].startsWith("http://"))
                    abrirEnlace();
                else Toast.makeText(context, "Solo enlaces web", Toast.LENGTH_SHORT).show();
                //else abrirArchivo();

                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            private void abrirEnlace(){
                Uri ruta = Uri.parse(datos[2]);
                Intent enlace = new Intent(Intent.ACTION_VIEW, ruta);

                try{principal().startActivity(enlace);}
                catch(FileUriExposedException e){}
            }

            private void abrirArchivo(){
                File file = new File(datos[2]);
                String extension = datos[2].split("\\.")[1],
                       mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".provider",file), mime);
                principal().startActivity(intent);
            }

            /**
             * filepaths.xml
             *
             * <paths xmlns:android="http://schemas.android.com/apk/res/android">
             *     <external-path name="external_files" path="."/>
             * </paths>
             * manifest
             *
             * <provider
             *         android:name="android.support.v4.content.FileProvider"
             *         android:authorities="${applicationId}.provider"
             *         android:exported="false"
             *         android:grantUriPermissions="true">
             *         <meta-data
             *             android:name="android.support.FILE_PROVIDER_PATHS"
             *             android:resource="@xml/filepaths"/>
             *     </provider>
             * </application>
             */
        };
    }

    public Principal principal(){return actividad.principal();}
    public boolean campoVisible(){return campo.getVisibility() == EditText.VISIBLE;}
}


