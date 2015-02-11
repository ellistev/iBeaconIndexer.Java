package selliot.ibeaconindexer.View;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import java.util.List;
import selliot.ibeaconindexer.Controller.ActionFoundController;
import selliot.ibeaconindexer.R;


public class MainActivity extends ActionBarActivity {

    private static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    //private TextView blueToothTextView;
    private ListView blueToothListView;
    private List<BluetoothDevice> btDeviceList;
    private ActionFoundController receiver = null;
    private Location _currentLocation;
    private LocationManager _locationManager;
    public String _locationText;
    public String _addressText;
    private String _locationProvider;
    boolean locationUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeLocationManager();

        //blueToothTextView = FindViewById<TextView>(Resource.Id.BlueToothResults);
        blueToothListView = (ListView) findViewById(R.id.mainBleListView);

        //blueToothTextView.MovementMethod = new ScrollingMovementMethod();
        //Register the BroadcastReceiver
        receiver = new ActionFoundReceiver(this);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STRTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        RegisterReceiver(receiver, filter); // Don't forget to unregister during onDestroy

        // Getting the Bluetooth adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //blueToothTextView.Text += "\nAdapter: " + btAdapter;

        Button startButton = findViewById(R.id.startBlueToothButton);
        startButton.callOnClick() += (object sender, EventArgs e) =>
        {
            //blueToothTextView.SetHeight(1200);
            blueToothListView.SetMinimumHeight(0);
            CheckBTState();
            receiver.OutputBlueToothList(this);
            //btAdapter.StartDiscovery();
        };

        Button stopButton = findViewById(R.id.stopBlueToothButton);
        stopButton.Click += (object sender, EventArgs e) =>
        {
            btAdapter.stopLeScan(receiver);
            receiver.OutputBlueToothList(this);
        };
    }

    public void InitializeLocationManager()
    {
        _locationManager = (LocationManager) this.getBaseContext().getSystemService(LOCATION_SERVICE);
        Criteria criteriaForLocationService = new Criteria
        {
            Accuracy = Accuracy.NoRequirement
        };
        List<String> acceptableLocationProviders = _locationManager.getProviders(criteriaForLocationService, true);

        if (acceptableLocationProviders.Any())
        {
            _locationProvider = acceptableLocationProviders.First();
        }
        else
        {
            _locationProvider = "";
        }
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
