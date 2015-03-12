package selliot.ibeaconindexer.View;

import android.app.Activity;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;


import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import selliot.ibeaconindexer.Controller.ActionFoundController;
import selliot.ibeaconindexer.Model.BluetoothObjects.BtDevice;
import selliot.ibeaconindexer.Model.BluetoothObjects.BtDeviceTimeFoundComparer;
import selliot.ibeaconindexer.Model.BluetoothObjects.DatabaseFunctions;
import selliot.ibeaconindexer.Model.BluetoothObjects.TaskFragment;
import selliot.ibeaconindexer.R;
import selliot.ibeaconindexer.Utils.BleListItemRefreshTask;
import selliot.ibeaconindexer.Utils.BleScanRestartTask;
import selliot.ibeaconindexer.Utils.BtDeviceArrayAdapter;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private static final int BLUETOOTH_RESET_TIME = 5000;
    private static final int LIST_REFRESH_TIME = 200;
    private static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    //private TextView blueToothTextView;
    private ListView blueToothListView;
    private ActionFoundController receiver = null;
    private Location _currentLocation;
    private LocationManager _locationManager;
    public String _locationText;
    public String _addressText;
    private String _locationProvider;

    public BleScanRestartTask bleRestartTask;
    public BleListItemRefreshTask bleListItemRefreshTask;

    public List<BtDevice> btDeviceList = new ArrayList<BtDevice>();
    long scanStartTime;

    private BtDeviceArrayAdapter adapter;
    public MainActivity mBlueToothDiscover;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupBlueToothListView();

//        FragmentManager fm = getFragmentManager();
//        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
//
//        // If the Fragment is non-null, then it is currently being
//        // retained across a configuration change.
//        if (mTaskFragment == null) {
//            mTaskFragment = new TaskFragment();
//            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
//        }

        InitializeLocationManager();

        blueToothListView = (ListView) findViewById(R.id.BlueToothResultsListView);

        receiver = new ActionFoundController(this, btDeviceList, adapter);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(receiver, filter); // Don't forget to unregister during onDestroy

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        //start and stop ble scan every so often, buggy on nexus devices
        bleRestartTask = GetBluetoothScanRestartTask();
        bleRestartTask.startUpdates();

        bleListItemRefreshTask = GetBleListItemRefreshTaskTask();
        bleListItemRefreshTask.startUpdates();

        scanStartTime = System.currentTimeMillis();


    }

    private BleScanRestartTask GetBluetoothScanRestartTask() {
        return new BleScanRestartTask(new Runnable() {
            @Override
            public void run() {
                if(btAdapter==null) {
                    return;
                } else {
                    if (btAdapter.isEnabled()) {
                        btAdapter.stopLeScan(receiver);
                        btAdapter.startLeScan(receiver);
                    }
                }
            }
        }, BLUETOOTH_RESET_TIME);
    }

    private BleListItemRefreshTask GetBleListItemRefreshTaskTask() {
        return new BleListItemRefreshTask(new Runnable() {
            @Override
            public void run() {

                TextView scanTimeTextView = (TextView) findViewById(R.id.ScanTimeTextView);

                long elapsedTimeScanning = System.currentTimeMillis() - scanStartTime;

                scanTimeTextView.setText(ConvertLongTimeToClockTime(elapsedTimeScanning));


                if(btDeviceList.size() == 0) return;

                adapter.notifyDataSetChanged();
                Collections.sort(btDeviceList, new BtDeviceTimeFoundComparer());
            }
        }, LIST_REFRESH_TIME);
    }

    public String ConvertLongTimeToClockTime(long timeToConvert){
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeToConvert),
                TimeUnit.MILLISECONDS.toMinutes(timeToConvert) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeToConvert)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(timeToConvert) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeToConvert)));
    }

    public void SetupBlueToothListView(){

        ListView blueToothListView = (ListView) findViewById(R.id.BlueToothResultsListView);

        blueToothListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BtDevice device= adapter.getBlueToothListItem(view.getId());// ? not sure if this is the correct way to get list item

                device.MacAddress = "";

                adapter.notifyDataSetChanged ();
            }
        });

        adapter = new BtDeviceArrayAdapter(this, this.getBaseContext(), android.R.layout.simple_list_item_1);

        adapter.addList(btDeviceList);

        blueToothListView.setAdapter(adapter);

    }

    public void onDestroy(){
        unregisterReceiver(receiver);
    }


    public void InitializeLocationManager()
    {
        _locationManager = (LocationManager) this.getBaseContext().getSystemService(LOCATION_SERVICE);
        Criteria criteriaForLocationService = new Criteria();
        criteriaForLocationService.setAccuracy(Criteria.NO_REQUIREMENT);
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
