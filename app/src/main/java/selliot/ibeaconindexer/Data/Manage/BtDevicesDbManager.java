package selliot.ibeaconindexer.Data.Manage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import selliot.ibeaconindexer.Data.Contracts.BtDeviceContract;
import selliot.ibeaconindexer.Data.Helpers.BtDeviceDbHelper;
import selliot.ibeaconindexer.Model.BluetoothObjects.BtDevice;

/**
 * Created by Steven on 2015-03-15.
 */
public class BtDevicesDbManager {
    private Context context;
    private BtDeviceDbHelper btDeviceDbHelper;

    public BtDevicesDbManager(Context contextIn){
        context = contextIn;
        btDeviceDbHelper = new BtDeviceDbHelper(context);
    }

    public void addBtDevice(BtDevice deviceToAdd){
        // Gets the data repository in write mode
        SQLiteDatabase db = btDeviceDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID, deviceToAdd.UuidString);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_NAME, deviceToAdd.Name);
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_TYPE, deviceToAdd.Type);

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                BtDeviceContract.BtDevicesTable.TABLE_NAME,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_NAME, //nullable column, for strange sqlite purposes
                values);
    }
}
