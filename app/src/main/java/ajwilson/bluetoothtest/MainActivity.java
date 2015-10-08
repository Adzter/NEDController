package ajwilson.bluetoothtest;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    private Button plane1;
    private Button plane2;
    private Button sendTest;
    private String btaddr = "";

    private boolean connected = false;

    private BluetoothDevice device;
    private BluetoothAdapter adapter;
    private BluetoothSocket socket;
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = BluetoothAdapter.getDefaultAdapter();
        if ( adapter != null ) {
            if (!adapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        addListenerOnButtons();
    }

    private void addListenerOnButtons() {
        sendTest = (Button) findViewById( R.id.send_test);
        plane1 = (Button) findViewById( R.id.plane1);
        plane2 = (Button) findViewById( R.id.plane2);

        sendTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Send Test Button Pressed");
                if( btaddr.length() == 0 ) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( context );

                    // set title
                    alertDialogBuilder.setTitle("Error");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("You must select a plane to control")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                    return;
                } else {
                    // If discovery is still enabled this will slow down the process
                    adapter.cancelDiscovery();

                    // Find the device that we want to connect
                    device = adapter.getRemoteDevice( btaddr );
                    System.out.println( device.getName() );

                    // Use our unique ID
                    UUID uuid = UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");

                    // Attempt to open a socket
                    try {

                        // Make sure the unique ID on the server and client match
                        socket = device.createRfcommSocketToServiceRecord(uuid);

                        // Connect
                        socket.connect();
                        System.out.println("Bluetooth Socket Method 1: Connected");
                    }
                    catch ( Exception e ) {
                        System.out.println("Bluetooth Socket Error: " + e );
                    }

                }
            }
        });

        plane1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Plane #1");
                btaddr = "00:15:83:3D:0A:57";
            }
        });

        plane2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Plane #2");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
