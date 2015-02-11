package selliot.ibeaconindexer.Model.BluetoothObjects;

/**
 * Created by Steven on 2015-02-10.
 */
public class ScannedBleDevice{

    public String MacAddress;

    public String DeviceName;
    public double RSSI;
    public double Distance;

    public byte[] CompanyId;
    public byte[] IbeaconProximityUUID;
    public String IbeaconProximityUUIDString;
    public byte[] Major;
    public int MajorInt;
    public byte[] Minor;
    public int MinorInt;
    public byte Tx;

    public long ScannedTime;

}