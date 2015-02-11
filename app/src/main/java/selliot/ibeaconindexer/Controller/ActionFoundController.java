package selliot.ibeaconindexer.Controller;

/**
 * Created by Steven on 2015-02-10.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import selliot.ibeaconindexer.Model.BluetoothObjects.BtDevice;
import selliot.ibeaconindexer.View.MainActivity;

public class ActionFoundController extends BroadcastReceiver, BluetoothAdapter.LeScanCallback
    {
    public List<BtDevice> btDeviceList = new ArrayList<BtDevice>();
    private List<String> btTextList;
    private Context context;
    private BtDeviceArrayAdapter adapter;
    public MainActivity mBlueToothDiscover;
    private List<BtDevice> newBtDeviceList;
    public TextView blueToothTextView;
    private Data eFunctions database;
    public ActionFoundController(){}

    public ActionFoundController(MainActivity activity)
    {
        mBlueToothDiscover = activity;
        context = context;
        //database = new DatabaseFunctions(activity.base);
    }

    public void ClearBlueToothList()
    {
        btDeviceList.clear();
        btTextList.clear();
    }

    public void PrintFullBlueToothList()
    {
        //TextView blueToothTextView = mBlueToothDiscover != null ? mBlueToothDiscover.FindViewById<TextView>(Resource.Id.BlueToothResults) : null;
        //blueToothTextView.Text = "";
        int count = 0;

        for(Iterator<BtDevice> i = btDeviceList.iterator(); i.hasNext(); ) {
            BtDevice btDevice = i.next();
            blueToothTextView.getText() += "\n" + count + ": " + btDevice.Name + ", " + btDevice.Type + ", " + btDevice.MacAddress +
                    ", " + btDevice.Strength;
            count++;
        }

    }

    public void OutputBlueToothList(Activity activity)
    {
        //TextView blueToothTextView = mBlueToothDiscover != null ? mBlueToothDiscover.FindViewById<TextView>(Resource.Id.BlueToothResults) : null;


        ListView blueToothListView = mBlueToothDiscover.FindViewById<ListView>(Resource.Id.BlueToothResultsListView);

        blueToothListView.ItemClick += blueToothListView_ItemClick;

        //newBtDeviceList = btDeviceList.OrderByDescending(o => o.Strength).GroupBy(i => i.MacAddress).Select(g => g.First()).ToList();

        adapter = new BtDeviceArrayAdapter(activity, activity.BaseContext, Android.Resource.Layout.SimpleListItem1);
        List<BtDevices> btDevicesListFromDatabase = database.GetAllBtDevices();
        adapter.AddList (btDevicesListFromDatabase);
        blueToothListView.Adapter = adapter;

        //blueToothTextView.Text = "";

    }

    void blueToothListView_ItemClick(object sender, AdapterView.ItemClickEventArgs e)
    {
        BtDevices device= adapter.GetBlueToothListItem(e.Position);

        device.MacAddress = "";

        adapter.NotifyDataSetChanged ();

    }

    public List<String> getPopulatedList(List<BtDevice> newBtDeviceList) {
        List<String> myList = new ArrayList<String>();
        foreach(var btDevice in newBtDeviceList){
            myList.add(btDevice.Name + ", " + btDevice.Type + ", " + btDevice.MacAddress + ", " + btDevice.Strength);
        }

        return myList;
    }

    public override void OnReceive(Context context, Intent intent)
    {
        String action = intent.Action;
        this.context = context;
        // blueToothTextView = mBlueToothDiscover != null ? mBlueToothDiscover.FindViewById<TextView>(Resource.Id.BlueToothResults) : null;

        if (BluetoothDevice.ActionFound == action && blueToothTextView != null)
        {
            BluetoothDevice device = (BluetoothDevice) intent.GetParcelableExtra(BluetoothDevice.ExtraDevice);

            int rssi = intent.GetShortExtra(BluetoothDevice.ExtraRssi, Short.MinValue);
            string uuid = (string)intent.GetParcelableExtra(BluetoothDevice.ExtraUuid);

            blueToothTextView.Text += "\n  Device: " + device.Name + ", " + device.Type + ", " + rssi + ", " + device;
            BtDevice btDevice = new BtDevice();
            btDevice.Name = device.Name;
            btDevice.Type = device.Type.ToString();
            btDevice.Strength = rssi;
            btDevice.MacAddress = device.ToString();
            btDevice.Uuid = uuid;
            btDeviceList.Add(btDevice);
        }
        else
        {
            if (BluetoothDevice.ActionPairingRequest == action)
            {
                BluetoothDevice device = (BluetoothDevice) intent.GetParcelableExtra(BluetoothDevice.ExtraDevice);
                IParcelable[] uuidExtra = intent.GetParcelableArrayExtra(BluetoothDevice.ExtraUuid);
                for (int i = 0; i < uuidExtra.Length; i++)
                {
                    blueToothTextView.Text += "\n  Device: " + device.Name + ", " + device + ", Service: " +
                            uuidExtra[i].ToString();
                }
            }
            else
            {
                if (BluetoothAdapter.ActionDiscoveryStarted == action)
                {
                    blueToothTextView.Text += "\nDiscovery Started...";
                }
            }
        }

        if (BluetoothAdapter.ActionDiscoveryFinished == action && blueToothTextView != null)
        {
            blueToothTextView.Text += "\nDiscovery Stopped For Some Reason...";

        }
    }

    public void OnLeScan (BluetoothDevice device, int rssi, byte[] scanRecord)
    {
        ScannedBleDevice parsedLEDevice = new BLEParser().ParseRawScanRecord (device, rssi, scanRecord, null);

        if (parsedLEDevice != null) {
            blueToothTextView = mBlueToothDiscover != null ? mBlueToothDiscover.FindViewById<TextView>(Resource.Id.BlueToothResults) : null;
            BtDevice btDevice = new BtDevice (parsedLEDevice);
            int newBtDeviceId;

            //get gps location and
            String location = mBlueToothDiscover.CheckLocation ();
            Locations locationName = database.GetLocationName (btDevice.UuidString, btDevice.MajorInt, btDevice.MinorInt);


            btDeviceList.add (btDevice);

            //only add if new, update if changed
            Location currentLocation = mBlueToothDiscover.GetCurrentLocationObject ();


            GPSLocation newGpsLocation = new GPSLocation();
            newGpsLocation.LatitudeLongitude = String.Format ("{0},{1}", currentLocation != null ? currentLocation.Latitude.ToString() : "", currentLocation != null ? currentLocation.Longitude.ToString() : "");
            newGpsLocation.Address = location;
            newGpsLocation.Altitude = currentLocation != null ? currentLocation.Altitude.ToString() : "";
            newBtDeviceId = database.AddUpdateBtDevice (btDevice, newGpsLocation);

            blueToothTextView.getText() += "\n Device found: " + btDevice.UuidString + ":" + btDevice.MajorInt + ":" + btDevice.MinorInt + " You are at " + locationName.Name + "(" + String.Format ("{0},{1}", currentLocation != null ? currentLocation.Latitude.ToString() : "", currentLocation != null ? currentLocation.Longitude.ToString() : "") + location + ")";

            OutputBlueToothList(mBlueToothDiscover);
        }

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
