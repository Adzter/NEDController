package ajwilson.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ajwilson on 10/4/15.
 */
public class Bluetooth extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter adapter;
    private boolean paired = false;

    public void Bluetooth() {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void requestEnableBluetooth() {
        if ( isCapable() ) {
            if (!adapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    public boolean isCapable() {
        return( adapter != null );
    }
}
