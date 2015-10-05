package ajwilson.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button pairDevice;
    private Button sendTest;

    private final static int REQUEST_ENABLE_BT = 1;
    private boolean paired = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
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
        pairDevice = (Button) findViewById( R.id.pair_device);

        sendTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Send Test Button Pressed");
            }
        });

        pairDevice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Pair device button pressed");
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
