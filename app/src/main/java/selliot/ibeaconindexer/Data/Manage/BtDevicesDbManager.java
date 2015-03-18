package selliot.ibeaconindexer.Data.Manage;

import android.bluetooth.BluetoothClass;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import selliot.ibeaconindexer.Data.Contracts.BtDeviceContract;
import selliot.ibeaconindexer.Data.Helpers.BtDeviceDbHelper;
import selliot.ibeaconindexer.Model.BluetoothObjects.BtDevice;

/**
 * Created by Steven on 2015-03-15.
 */
public class BtDevicesDbManager {
    private Context context;
    private BtDeviceDbHelper btDeviceDbHelper;
    private SQLiteDatabase db;

    public BtDevicesDbManager(Context contextIn){
        context = contextIn;
        btDeviceDbHelper = new BtDeviceDbHelper(context);
    }

    public void open() {
        db = btDeviceDbHelper.getWritableDatabase();
    }

    public void close() {
        btDeviceDbHelper.close();
    }

    public SQLiteDatabase getBd() {
        return db;
    }

    public long addBtDevice(BtDevice deviceToAdd){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID, deviceToAdd.UuidString);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_MAJOR, deviceToAdd.MajorInt);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_MINOR, deviceToAdd.MinorInt);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_MACADDRESS, deviceToAdd.MacAddress);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_NAME, deviceToAdd.Name);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TYPE, deviceToAdd.Type);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMEFOUND, deviceToAdd.TimeFound.toString());
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_DISCOVERER, deviceToAdd.Discoverer);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMESSEEN, deviceToAdd.TimesSeen);

        // Insert the new row, returning the primary key value of the new row
        return  db.insert(
                BtDeviceContract.BtDevicesTable.TABLE_NAME,
                null, //will insert nothing if there are no values to set
                values);
    }

    public int updateBtDevice(BtDevice deviceToUpdate) {

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID, deviceToUpdate.UuidString);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_MAJOR, deviceToUpdate.MajorInt);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_MINOR, deviceToUpdate.MinorInt);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_MACADDRESS, deviceToUpdate.MacAddress);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_NAME, deviceToUpdate.Name);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TYPE, deviceToUpdate.Type);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMEFOUND, deviceToUpdate.TimeFound.toString());
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_DISCOVERER, deviceToUpdate.Discoverer);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMESSEEN, deviceToUpdate.TimesSeen);

        // Which row to update, based on the ID
        String selection = BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID + " = '" + deviceToUpdate.UuidString
                + "' AND " + BtDeviceContract.BtDevicesTable.COLUMN_NAME_MAJOR + " = '" + deviceToUpdate.MajorInt
                + "' AND " + BtDeviceContract.BtDevicesTable.COLUMN_NAME_MINOR + " = '" + deviceToUpdate.MinorInt + "'";

        return  db.update(
                BtDeviceContract.BtDevicesTable.TABLE_NAME,
                values,
                selection,
                null);
    }

    public Cursor getBtDeviceByUuidMajorMinor(String uuid, String major, String minor){

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BtDeviceContract.BtDevicesTable._ID,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_ID,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_NAME,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_TYPE,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_MACADDRESS,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_STRENGTH,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_MAJOR,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_MINOR,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMEFOUND,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_FOUNDSTATUS,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMESSEEN,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_DISCOVERER
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_ID + " ASC";

        String selection = BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID + " = '" + uuid
                + "' AND " + BtDeviceContract.BtDevicesTable.COLUMN_NAME_MAJOR + " = '" + major
                + "' AND " + BtDeviceContract.BtDevicesTable.COLUMN_NAME_MINOR + " = '" + minor + "'";

        return db.query(
                BtDeviceContract.BtDevicesTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }

    public void deleteBtDevice(int rowId){
        SQLiteDatabase db = btDeviceDbHelper.getWritableDatabase();
        String selection = BtDeviceContract.BtDevicesTable.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rowId) };
        db.delete(BtDeviceContract.BtDevicesTable.TABLE_NAME, selection, selectionArgs);
    }

    public void deleteAllBtDevices(){
        SQLiteDatabase db = btDeviceDbHelper.getWritableDatabase();
        String selection = "";
        db.delete(BtDeviceContract.BtDevicesTable.TABLE_NAME, selection, null);
    }

    public List<BtDevice> getAllBtDevices() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_ID,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_NAME,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_TYPE,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_MACADDRESS,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_STRENGTH,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_MAJOR,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_MINOR,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMEFOUND,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_FOUNDSTATUS,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMESSEEN,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_DISCOVERER
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_ID + " ASC";

        String selection = "";

        Cursor btDevices = db.query(
                BtDeviceContract.BtDevicesTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<BtDevice> listOfBtDevicesFromDatabase = new ArrayList<BtDevice>();

        while(btDevices.moveToNext()){
            BtDevice nextDevice = new BtDevice();

            nextDevice.UuidString = btDevices.getString(btDevices.getColumnIndexOrThrow(BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID));
            nextDevice.MajorInt = btDevices.getInt(btDevices.getColumnIndexOrThrow(BtDeviceContract.BtDevicesTable.COLUMN_NAME_MAJOR));
            nextDevice.MinorInt = btDevices.getInt(btDevices.getColumnIndexOrThrow(BtDeviceContract.BtDevicesTable.COLUMN_NAME_MINOR));
            nextDevice.MacAddress = btDevices.getString(btDevices.getColumnIndexOrThrow(BtDeviceContract.BtDevicesTable.COLUMN_NAME_MACADDRESS));
            nextDevice.Name = btDevices.getString(btDevices.getColumnIndexOrThrow(BtDeviceContract.BtDevicesTable.COLUMN_NAME_NAME));
            nextDevice.Type = btDevices.getString(btDevices.getColumnIndexOrThrow(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TYPE));

            String timeString = btDevices.getString(btDevices.getColumnIndexOrThrow(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMEFOUND));
            Date date = new Date();
            try {
                date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            nextDevice.TimeFound = date;
            nextDevice.Discoverer = btDevices.getString(btDevices.getColumnIndexOrThrow(BtDeviceContract.BtDevicesTable.COLUMN_NAME_DISCOVERER));
            nextDevice.TimesSeen = btDevices.getInt(btDevices.getColumnIndexOrThrow(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMESSEEN));

            listOfBtDevicesFromDatabase.add(nextDevice);
        }

        return listOfBtDevicesFromDatabase;
    }
}
