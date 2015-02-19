package selliot.ibeaconindexer.Model.BluetoothObjects;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Steven on 2015-02-10.
 */
public class BtDevice
{
    public String Name;
    public String Type;
    public String MacAddress;
    public int Strength;
    public String Uuid;
    public String UuidString;
    public String Major;
    public int MajorInt;
    public String Minor;
    public int MinorInt;
    public String TimeFound;

    public BtDevice(){}

    public BtDevice(BluetoothDevice device){
        Name = device.getName();
    }

    public BtDevice(String name, String type, String macAddress, int strength, String uuid)
    {
        this.Name = name;
        this.Type = type;
        this.MacAddress = macAddress;
        this.Strength = strength;
        this.Uuid = uuid;
    }

    public BtDevice(ScannedBleDevice device)
    {
        this.Name = !TextUtils.isEmpty(device.DeviceName) ? device.DeviceName : "";
        this.Type = device.getClass().toString();
        this.MacAddress = device.MacAddress;
        this.Strength = (int) device.RSSI;
        this.Uuid = device.IbeaconProximityUUID.toString();
        this.UuidString = device.IbeaconProximityUUIDString;
        this.Major = device.Major.toString();
        this.MajorInt = device.MajorInt;
        this.Minor = device.Minor.toString();
        this.MinorInt = device.MinorInt;
        this.TimeFound = String.valueOf(device.ScannedTime);

    }



}