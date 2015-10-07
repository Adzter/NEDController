package ajwilson.bluetoothtest;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
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
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    private Button plane1;
    private Button plane2;
    private Button sendTest;
    private String btaddr = "";

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter adapter;

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
                }
            }
        });

        plane1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Plane #1");
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
