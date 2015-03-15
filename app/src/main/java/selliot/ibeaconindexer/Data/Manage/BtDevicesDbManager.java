package selliot.ibeaconindexer.Data.Manage;

import android.bluetooth.BluetoothClass;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public void UpdateBtDevice(BtDevice deviceToUpdate) {
        SQLiteDatabase db = btDeviceDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(BtDeviceContract.BtDevicesTable.COLUMN_NAME_NAME, deviceToUpdate.Name);

        // Which row to update, based on the ID
        String selection = BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID + " LIKE ?";
        String[] selectionArgs = {deviceToUpdate.UuidString};

        int count = db.update(
                BtDeviceContract.BtDevicesTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void GetBtDevice(String uuidToFind){
        SQLiteDatabase db = btDeviceDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BtDeviceContract.BtDevicesTable._ID,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_NAME,
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                BtDeviceContract.BtDevicesTable.COLUMN_NAME_TIMESSEEN + " DESC";

        String selection = BtDeviceContract.BtDevicesTable.COLUMN_NAME_UUID;
        String[] selectionArgs = new String[] {uuidToFind };
        Cursor c = db.query(
                BtDeviceContract.BtDevicesTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }


    public void DeleteBtDevice(int rowId){
        SQLiteDatabase db = btDeviceDbHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = BtDeviceContract.BtDevicesTable.COLUMN_NAME_ID + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(rowId) };
// Issue SQL statement.
        db.delete(BtDeviceContract.BtDevicesTable.TABLE_NAME, selection, selectionArgs);
    }

}
