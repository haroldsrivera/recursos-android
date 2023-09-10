package co.edu.ue.permisosdisp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Activity activity;

    // Version Android
    private TextView versionAndroid;
    private int versionSDK;
    // Bateria
    private ProgressBar pbLevelBaterry;
    private TextView tvLevelBaterry;
    IntentFilter batteryFilter;
    // Camara
    CameraManager cameraManager;
    String cameraId;
    private Button btnOnLight;
    private Button btnOffLight;
    // Archivo
    private EditText nameFile;
    private Archivo archivo;
    // Conexion
    private TextView tvConexion;
    ConnectivityManager conexion;

    // Bluetooth
    private BluetoothAdapter BluetoothAdapter;
    private Button btnOnBluetooth;
    private Button btnOffBluetooth;
    private Button btnSaveFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        begin();

        batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, batteryFilter);

        btnOnLight.setOnClickListener(this::onLight);
        btnOffLight.setOnClickListener(this::offLight);

        this.archivo = new Archivo(context, activity);
        btnSaveFile.setOnClickListener(this::saveFile);

        this.BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnOnBluetooth.setOnClickListener(this::onBluetooth);
        btnOffBluetooth.setOnClickListener(this::offBluetooth);
    }

    @SuppressLint("WrongViewCast")
    private void begin() {
        this.context = getApplicationContext();
        this.activity = this;
        this.versionAndroid = findViewById(R.id.tvVersionAndroid);
        this.pbLevelBaterry = findViewById(R.id.pbLevelBattery);
        this.tvLevelBaterry = findViewById(R.id.tvLevelBatteryLB);
        this.nameFile = findViewById(R.id.etNameFile);
        this.tvConexion = findViewById(R.id.tvConexion);
        this.btnOnLight = findViewById(R.id.btnOn);
        this.btnOffLight = findViewById(R.id.btnOff);
        this.btnOnBluetooth = findViewById(R.id.btnOnBluetooth);
        this.btnOffBluetooth = findViewById(R.id.btnOffBluetooth);
        this.btnSaveFile = findViewById(R.id.btnSaveFile);
    }

    // SO version
    @Override
    protected void onResume() {
        super.onResume();
        String versionSO = Build.VERSION.RELEASE;
        versionSDK = Build.VERSION.SDK_INT;
        versionAndroid.setText("Version Android: " + versionSO + " /SDK: " + versionSDK);
        checkConnection();
    }

    // Linterna
    private void onLight(View view) {
        try {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void offLight(View view) {
        try {
            cameraManager.setTorchMode(cameraId, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bateria
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            pbLevelBaterry.setProgress(levelBattery);
            tvLevelBaterry.setText("3. Nivel de la bateria: " + levelBattery + "%");
        }
    };

    private void checkConnection() {
        conexion = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conexion.getActiveNetworkInfo();
        boolean stateNet = network != null && network.isConnectedOrConnecting();
        if (stateNet) tvConexion.setText("Conexion a internet: " + network.getTypeName());
        else tvConexion.setText("Conexion a internet: No hay conexion");
    }


    // Storage
    private void saveFile(View view) {
        String nombreArchivo = nameFile.getText().toString().trim();

        if (nombreArchivo.isEmpty()) {
            Toast.makeText(context, "Por favor, ingrese un nombre de archivo", Toast.LENGTH_SHORT).show();
        } else {
            String informacion = "Información del archivo";
            archivo.guardarArchivo(nombreArchivo, informacion);
        }
    }


    // Bluetooth
    private void offBluetooth(View view) {
        if (BluetoothAdapter == null) {
            Toast.makeText(context, "No se reconoce el Bluetooth del dispositivo", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (BluetoothAdapter.isEnabled()) {
                    BluetoothAdapter.disable();
                    Toast.makeText(context, "Bluetooth Desactivado", Toast.LENGTH_SHORT).show();
                }
            } catch (SecurityException e) {
                Toast.makeText(context, "Sin permisos para desactivar el Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onBluetooth(View view) {
        if (BluetoothAdapter == null) {
            Toast.makeText(context, "No se reconoce el Bluetooth del dispositivo", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (!BluetoothAdapter.isEnabled()) {
                    BluetoothAdapter.enable();
                    Toast.makeText(context, "Bluetooth Activado", Toast.LENGTH_SHORT).show();
                }
            } catch (SecurityException e) {
                Toast.makeText(context, "Sin permisos para desactivar el Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }


}