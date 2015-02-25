package selliot.ibeaconindexer.Controller;

/**
 * Created by Steven on 2015-02-10.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import selliot.ibeaconindexer.Model.BluetoothObjects.BtDevice;
import selliot.ibeaconindexer.Model.BluetoothObjects.DatabaseFunctions;
import selliot.ibeaconindexer.Model.BluetoothObjects.ScannedBleDevice;
import selliot.ibeaconindexer.R;
import selliot.ibeaconindexer.Utils.BleParser;
import selliot.ibeaconindexer.Utils.BtDeviceArrayAdapter;
import selliot.ibeaconindexer.View.MainActivity;

public class ActionFoundController extends BroadcastReceiver implements android.bluetooth.BluetoothAdapter.LeScanCallback
    {
    public List<BtDevice> btDeviceList = new ArrayList<BtDevice>();
    private List<String> btTextList;
    private Context context;
    private BtDeviceArrayAdapter adapter;
    public MainActivity mBlueToothDiscover;
    private List<BtDevice> newBtDeviceList;
    public TextView BlueToothResults;
    private DatabaseFunctions database;

    public final class FoundStatus {
        public static final String NEW = "new";
        public static final String OLD = "old";
        public static final String NOT_BEACON = "not_beacon";

        private FoundStatus(){
        }
    }

    public ActionFoundController(){}

    public ActionFoundController(MainActivity activity)
    {
        mBlueToothDiscover = activity;
        context = context;
        SetupBlueToothListView(activity);
        //database = new DatabaseFunctions(activity.base);
    }

    public void ClearBlueToothList()
    {
        //btDeviceList.clear();
        //btTextList.clear();
    }

    public void PrintFullBlueToothList()
    {
        //TextView blueToothTextView = mBlueToothDiscover != null ? mBlueToothDiscover.FindViewById<TextView>(Resource.Id.BlueToothResults) : null;
        //blueToothTextView.Text = "";
        int count = 0;

        for(Iterator<BtDevice> i = btDeviceList.iterator(); i.hasNext(); ) {
            BtDevice btDevice = i.next();
            String currentBluetoothTextViewText = BlueToothResults.getText().toString();
            BlueToothResults.setText(currentBluetoothTextViewText += "\n" + count + ": " + btDevice.Name + ", " + btDevice.Type + ", " + btDevice.MacAddress +
                    ", " + btDevice.Strength);
            count++;
        }

    }

    public void SetupBlueToothListView(Activity activity){

        ListView blueToothListView = (ListView) mBlueToothDiscover.findViewById(R.id.BlueToothResultsListView);

        blueToothListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BtDevice device= adapter.getBlueToothListItem(view.getId());// ? not sure if this is the correct way to get list item

                device.MacAddress = "";

                adapter.notifyDataSetChanged ();
            }
        });

        adapter = new BtDeviceArrayAdapter(activity, activity.getBaseContext(), android.R.layout.simple_list_item_1);

        adapter.addList(btDeviceList);

        blueToothListView.setAdapter(adapter);

    }



    public List<String> getPopulatedList(List<BtDevice> newBtDeviceList) {
        List<String> myList = new ArrayList<String>();

        for(Iterator<BtDevice> i = newBtDeviceList.iterator(); i.hasNext(); ) {
            BtDevice btDevice = i.next();
            myList.add(btDevice.Name + ", " + btDevice.Type + ", " + btDevice.MacAddress + ", " + btDevice.Strength);
        }

        return myList;
    }

    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        this.context = context;
        // blueToothTextView = mBlueToothDiscover != null ? mBlueToothDiscover.FindViewById<TextView>(Resource.Id.BlueToothResults) : null;

        if (BluetoothDevice.ACTION_FOUND == action && BlueToothResults != null)
        {
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
            String uuid = intent.getParcelableExtra(BluetoothDevice.EXTRA_UUID).toString();

            BlueToothResults.setText(BlueToothResults.getText().toString() + "\n  Device: " + device.getName() + ", " + device.getType() + ", " + rssi + ", " + device);
            BtDevice btDevice = new BtDevice();
            btDevice.Name = device.getName();
            btDevice.Type = String.valueOf(device.getType());
            btDevice.Strength = rssi;
            btDevice.MacAddress = device.toString();
            btDevice.Uuid = uuid;
            btDeviceList.add(btDevice);
        }
        else
        {
            if (BluetoothDevice.ACTION_PAIRING_REQUEST == action)
            {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                for (int i = 0; i < uuidExtra.length; i++)
                {
                    BlueToothResults.setText(BlueToothResults.getText() +  "\n  Device: " + device.getName() + ", " + device + ", Service: " +
                            uuidExtra[i].toString());
                }
            }
            else
            {
                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action)
                {
                    BlueToothResults.append("\nDiscovery Started...");
                }
            }
        }

        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action && BlueToothResults != null)
        {
            BlueToothResults.append("\nDiscovery Stopped For Some Reason...");

        }
    }

    public void onLeScan (BluetoothDevice device, int rssi, byte[] scanRecord)
    {
        ScannedBleDevice parsedLEDevice = new BleParser().ParseRawScanRecord (device, rssi, scanRecord, null);

        if (parsedLEDevice != null) {
            BlueToothResults = mBlueToothDiscover != null ? (TextView)mBlueToothDiscover.findViewById(R.id.BlueToothResults) : null;
            BtDevice btDevice = new BtDevice (parsedLEDevice);
            int newBtDeviceId;

            //get gps location and
            String location = null;
            try {
                location = mBlueToothDiscover.CheckLocation ();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Locations locationName = database.GetLocationName (btDevice.UuidString, btDevice.MajorInt, btDevice.MinorInt);


            //check if device already exists in list
            int index = GetIndexOfBluetoothDevice(btDeviceList, btDevice);

            if(index == -1){ //if doesn't exist
                btDevice.FoundStatus = FoundStatus.NEW;
                btDeviceList.add (btDevice);
            }else{
                btDevice.FoundStatus = FoundStatus.OLD;
                btDeviceList.set(index, btDevice);
            }

            adapter.notifyDataSetChanged();

            //only add if new, update if changed
            Location currentLocation = mBlueToothDiscover.GetCurrentLocationObject ();


            //GPSLocation newGpsLocation = new GPSLocation();
            //newGpsLocation.LatitudeLongitude = String.Format ("{0},{1}", currentLocation != null ? currentLocation.Latitude.ToString() : "", currentLocation != null ? currentLocation.Longitude.ToString() : "");
            ////newGpsLocation.Address = location;
            //newGpsLocation.Altitude = currentLocation != null ? currentLocation.Altitude.ToString() : "";
            //newBtDeviceId = database.AddUpdateBtDevice (btDevice, newGpsLocation);

            BlueToothResults.setText(BlueToothResults.getText() + "\n Device found: " + btDevice.UuidString + ":" + btDevice.MajorInt + ":" + btDevice.MinorInt
                    + " You are at (" +
                    (currentLocation != null ? currentLocation.getLatitude() : "") + ":" +
                    (currentLocation != null ? currentLocation.getLongitude() : "")  + location + ")");

        }

    }

        private int GetIndexOfBluetoothDevice(List<BtDevice> btDeviceList, BtDevice deviceSearchingFor) {

            int counter = 0;

            for (BtDevice device : btDeviceList) {

                if(device.UuidString.equals(deviceSearchingFor.UuidString) &&
                   device.MajorInt == deviceSearchingFor.MajorInt &&
                   device.MinorInt == deviceSearchingFor.MinorInt){
                    return counter;
                }

                counter++;
            }
            return -1;
        }

        protected static double calculateDistance(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine distance, return -1.
        }

        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

    }
