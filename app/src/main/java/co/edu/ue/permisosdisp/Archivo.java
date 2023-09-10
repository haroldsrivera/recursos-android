package co.edu.ue.permisosdisp;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

import android.content.pm.PackageManager;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Archivo {
    private Context context;
    private Activity activity;

    public Archivo(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    private void crearDirectorio(File file){
        if (!file.exists()) file.mkdir();
    }

    public void guardarArchivo(String nombreArchivo, String informacion) {
        File directorio = determinarDirectorio();

        if (directorio != null) {
            Toast.makeText(context, "Archivo guardado en: " + directorio, Toast.LENGTH_LONG).show();

            try {
                File file = new File(directorio, nombreArchivo);
                FileWriter writer = new FileWriter(file);
                writer.append(informacion);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                Log.i("Archivo", e.getMessage());
                Toast.makeText(context, "Error al guardar el archivo", Toast.LENGTH_LONG).show();
            }
        }
    }

    private File determinarDirectorio() {
        File directorio;
        solicitarPermisoExternal();
        if (statusPermisoSD()) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                directorio = new File(Environment.getExternalStorageDirectory(), "ArchivoUE");
            } else {
                directorio = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "ArchivoUE");
            }
        } else {
            directorio = null;
        }

        crearDirectorio(directorio);
        return directorio;
    }

    private void solicitarPermisoExternal() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private boolean statusPermisoSD() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
