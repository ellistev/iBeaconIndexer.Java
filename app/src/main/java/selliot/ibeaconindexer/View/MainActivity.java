package selliot.ibeaconindexer.View;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;


import java.io.IOException;
import java.util.List;
import selliot.ibeaconindexer.Controller.ActionFoundController;
import selliot.ibeaconindexer.R;


public class MainActivity extends ActionBarActivity implements LocationListener {

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
        blueToothListView = (ListView) findViewById(R.id.BlueToothResultsListView);

        //blueToothTextView.MovementMethod = new ScrollingMovementMethod();
        //Register the BroadcastReceiver
        receiver = new ActionFoundController(this);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(receiver, filter); // Don't forget to unregister during onDestroy

        // Getting the Bluetooth adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //blueToothTextView.Text += "\nAdapter: " + btAdapter;

        //Button startButton = findViewById(R.id.startBlueToothButton);
        //startButton.callOnClick() += (object sender, EventArgs e) =>
        //{
            //blueToothTextView.SetHeight(1200);
            blueToothListView.setMinimumHeight(0);
            CheckBTState();
            receiver.OutputBlueToothList(this);
            //btAdapter.StartDiscovery();
        //};

        //Button stopButton = findViewById(R.id.stopBlueToothButton);
        //stopButton.Click += (object sender, EventArgs e) =>
        //{
        //    btAdapter.stopLeScan(receiver);
        //    receiver.OutputBlueToothList(this);
        //};
    }

    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // If it isn't request to turn it on
        // List paired devices
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            //blueToothTextView.Text += "\nBluetooth NOT supported. Aborting.";
            return;
        } else {
            if (btAdapter.isEnabled()) {
                //blueToothTextView.Text += "\nBluetooth is enabled...";

                //btAdapter.StartDiscovery ();

                btAdapter.startLeScan(receiver);
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    public void InitializeLocationManager()
    {
        _locationManager = (LocationManager) this.getBaseContext().getSystemService(LOCATION_SERVICE);
        Criteria criteriaForLocationService = new Criteria()
        {

        };
        List<String> acceptableLocationProviders = _locationManager.getProviders(criteriaForLocationService, true);

        if (acceptableLocationProviders.size() > 0)
        {
            _locationProvider = acceptableLocationProviders.get(0);
        }
        else
        {
            _locationProvider = "";
        }
    }

    public void UpdateLocation() throws IOException {
        if (_currentLocation == null)
        {
            _addressText = "Can't determine the current address.";
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = geocoder.getFromLocation(_currentLocation.getLatitude(), _currentLocation.getLongitude(), 10);

        Address address = addressList.get(0);
        if (address != null)
        {
            String deviceAddress = "";
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
            {
                deviceAddress += (address.getAddressLine(i)) + ", ";
            }
            _addressText = deviceAddress;
        }
        else
        {
            _addressText = "Unable to determine the address.";
        }
    }

    public String CheckLocation() throws IOException {
        UpdateLocation ();
        return _addressText;
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

    public Location GetCurrentLocationObject(){
        return _currentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
