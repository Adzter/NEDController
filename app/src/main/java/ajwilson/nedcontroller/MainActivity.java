package ajwilson.nedcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    private Button plane1;
    private Button plane2;
    private Button connect;
    private Button disconnect;

    private String btaddr = "";

    private boolean connected = false;

    private BluetoothDevice device;
    private BluetoothAdapter adapter;
    private BluetoothSocket socket;
    private ConnectedThread dataThread;
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
        disconnect = (Button) findViewById( R.id.disconnect);
        connect = (Button) findViewById( R.id.connect);
        plane1 = (Button) findViewById( R.id.plane1);
        plane2 = (Button) findViewById( R.id.plane2);

        connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btaddr.length() == 0) {
                    Dialogue dialogue = new Dialogue(context, "Error", "Please select a plane");
                    return;
                } else if( connected ) {
                    Dialogue dialogue = new Dialogue(context, "Error", "You are already connected");
                } else {
                    // If discovery is still enabled this will slow down the process
                    adapter.cancelDiscovery();

                    // Find the device that we want to connect
                    device = adapter.getRemoteDevice(btaddr);
                    System.out.println(device.getName());

                    // Use our unique ID
                    UUID uuid = UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");

                    // Attempt to open a socket
                    try {

                        // Make sure the unique ID on the server and client match
                        socket = device.createRfcommSocketToServiceRecord(uuid);

                        // Connect
                        socket.connect();
                        System.out.println("Bluetooth Socket: Connected");

                        // Show notification if successful
                        Toast toast = Toast.makeText(context, "Connected to Plane", Toast.LENGTH_SHORT);
                        toast.show();

                        dataThread = new ConnectedThread(socket);
                        dataThread.start();
                        dataThread.write("I'm sending data from an Android App".getBytes());

                        connected = true;
                    } catch (Exception e) {
                        // Show error message in case it fails
                        Dialogue dialogue = new Dialogue(context, "Error", "Failed to connect");
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

        disconnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if( socket != null ) {
                    if (socket.isConnected()) {
                        try {
                            socket.close();
                            Toast toast = Toast.makeText(context, "Disconnected from Plane", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        catch( Exception e ) {
                            
                        }
                    }
                }
                Toast toast = Toast.makeText(context, "Not connected to a Plane", Toast.LENGTH_SHORT);
                toast.show();
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
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity( intent );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
