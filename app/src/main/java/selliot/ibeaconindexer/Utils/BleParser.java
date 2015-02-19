package selliot.ibeaconindexer.Utils;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import selliot.ibeaconindexer.Model.BluetoothObjects.ScannedBleDevice;

/**
 * Created by Steven on 2015-02-11.
 */
public class BleParser
{
    public BleParser ()
    {
    }

    static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // use this method to parse those bytes and turn to an object which defined proceeding.
    // the uuidMatcher works as a UUID filter, put null if you want parse any BLE advertising data around.
    public ScannedBleDevice ParseRawScanRecord(BluetoothDevice device, int rssi, byte[] advertisedData, byte[] uuidMatcher) {

        int startByte = 2;
        try {
            ScannedBleDevice parsedObj = new ScannedBleDevice();
            // parsedObj.BLEDevice = device;
            parsedObj.DeviceName = device.getName();
            parsedObj.MacAddress = device.getAddress();
            parsedObj.RSSI = rssi;
            List<UUID> uuids = new ArrayList<UUID>();
            int skippedByteCount = advertisedData[0];
            int magicStartIndex = skippedByteCount + 1;
            int magicEndIndex = magicStartIndex
                    + advertisedData[magicStartIndex] + 1;
            List<Byte> magic = new ArrayList<Byte>();
            for (int i = magicStartIndex; i < magicEndIndex; i++) {
                magic.add(advertisedData[i]);
            }

            byte[] companyId = new byte[2];
            companyId[0] = magic.get(2);
            companyId[1] = magic.get(3);
            parsedObj.CompanyId = companyId;

            byte[] ibeaconProximityUUID = new byte[16];
            for (int i = 0; i < 16; i++) {
                if(magic.size() > (i+6)){
                    ibeaconProximityUUID[i] = magic.get(i + 6);
                }
            }

            parsedObj.IbeaconProximityUUID = ibeaconProximityUUID;

            String hexString = bytesToHex(ibeaconProximityUUID);

            //Here is your UUID
            parsedObj.IbeaconProximityUUIDString =  hexString.substring(0, 8) + "-" +
                    hexString.substring(8, 12) + "-" +
                    hexString.substring(12, 16) + "-" +
                    hexString.substring(16, 20) + "-" +
                    hexString.substring(20, 32);

            byte[] major = new byte[2];
            byte[] minor = new byte[2];
            byte tx = 0;
            //try{

            major[0] = magic.get(22);
            major[1] = magic.get(23);
            parsedObj.Major = major;
            parsedObj.MajorInt  = (advertisedData[startByte+23] & 0xff) * 0x100 + (advertisedData[startByte+24] & 0xff);

            minor[0] = magic.get(24);
            minor[1] = magic.get(25);

            parsedObj.Minor = minor;
            parsedObj.MinorInt =  (advertisedData[startByte+25] & 0xff) * 0x100 + (advertisedData[startByte+26] & 0xff);

            tx = magic.get(26);
            parsedObj.Tx = tx;

            //} catch (System.Exception ex) {
            //	parsedObj.Major = major;
            //	parsedObj.MajorInt  = 0;
            //	parsedObj.Minor = minor;
            //	parsedObj.MinorInt =  0;
            //	parsedObj.Tx = 0;
            //}

            parsedObj.ScannedTime = new Date().getTime();
            return parsedObj;
        } catch (Exception ex) {

            // Log.e(LOG_TAG,
            // "Exception in ParseRawScanRecord with advertisedData: "
            // + Util.BytesToHexString(advertisedData, " ")
            // + ", detail: " + ex.getMessage());
            return null;
        }
    }
}
