package selliot.ibeaconindexer.Model.BluetoothObjects;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteQuery;
import android.graphics.Path;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

import java.io.File;

import javax.xml.validation.Schema;

import selliot.ibeaconindexer.Utils.UtilClass;

/**
 * Created by Steven on 2015-02-10.
 */
public class DatabaseFunctions
{
    public SQLiteDatabase conn;
    private static String dbName = "MajorMinorIndex.s3db";
    private static String dbExternalFolder = "iBeacon_Indexer_Droid";
    private Context context;
    private DatabaseFunctions ()
    {
        //create new database connection


        File sdcard = Environment.getExternalStorageDirectory();
        String dbPath = sdcard.getAbsolutePath() + File.separator+ dbName;

        conn = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }

//    public DatabaseFunctions (Context icontext)
//    {
//        //creates and updates database to most recent version
//        //create new database connection, and create database location if doesn't already exist.
//        context = icontext;
//
//        // Check if your DB has already been extracted.
//        File sdcard = Environment.getExternalStorageDirectory();
//        String dbPath = sdcard.getAbsolutePath() + File.separator+ dbName;
//
//        conn = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
//
//        UpdateDatabaseToCurrentVersion ();
//    }
//
//    public void UpdateDatabaseToCurrentVersion(){
//        int lastSchemaVersion = 0;
//        int lastDataVersion = 0;
//        try{
//            Cursor lastSchemaVersionEntry = conn.rawQuery("select * from DbSchemaVersion;", null);
//            lastSchemaVersion = lastSchemaVersionEntry.OrderBy(x => x.Id).First().LastDBSchemaVersion;
//        }
//        catch(Exception e){
//            // doesn't exist
//        }
//        try{
//            var lastDbDataEntry = conn.Query<DbDataVersion> ("select * from DBDataVersion");
//            lastDataVersion = lastDbDataEntry.FirstOrDefault().LastDBDataVersion;
//        }
//        catch(Exception e){
//            // doesn't exist
//        }
//
//        UpdateSchema (lastSchemaVersion);
//
//        UpdateData (lastDataVersion);
//    }
//
//    private void UpdateSchema(int lastSchemaVersion)
//    {
//        //get list of files in schema folder
//        var schemaFolderFiles = context.Assets.List ("database/schema");
//        //List<String> schemaUpdateFiles = FileUtils.getFileNames (schemaFolderName);
//        foreach(string dataSqlFileName in schemaFolderFiles){
//        if(Integer.ParseInt(dataSqlFileName.Split('.')[0]) > lastSchemaVersion){
//            //if this file is greater than last updated, execute to update database schema
//            StringBuilder buf= new StringBuilder();
//            Stream sqlstreamin = context.Assets.Open ("database/schema/" + dataSqlFileName);
//            BufferedReader infile =	new BufferedReader(new Java.IO.InputStreamReader(sqlstreamin, "UTF-8"));
//            String str;
//
//            while ((str=infile.ReadLine()) != null) {
//                buf.Append(str);
//            }
//
//            infile.Close();
//            //execute all files not executed already
//            ExecuteScript (buf.ToString());
//        }
//    }
//
//    }
//
//    private void UpdateData(int lastDataVersion)
//    {
//        //get list of files in schema folder
//        var dataFolderFiles = context.Assets.List ("database/data");
//        //List<String> schemaUpdateFiles = FileUtils.getFileNames (schemaFolderName);
//        foreach(string dataSqlFileName in dataFolderFiles){
//        if(Integer.ParseInt(dataSqlFileName.Split('.')[0]) > lastDataVersion){
//            //if this file is greater than last updated, execute to update database schema
//            StringBuilder buf= new StringBuilder();
//            Stream sqlstreamin = context.Assets.Open ("database/data/" + dataSqlFileName);
//            BufferedReader infile =	new BufferedReader(new Java.IO.InputStreamReader(sqlstreamin, "UTF-8"));
//            String str;
//
//            while ((str=infile.ReadLine()) != null) {
//                buf.Append(str);
//            }
//
//            infile.Close();
//            //execute all files not executed already
//            ExecuteScript (buf.ToString());
//        }
//    }
//    }
//
//    protected void ExecuteScript(String script)
//    {
//        String[] commandTextArray = script.Split(new String[] { "GO" }, StringSplitOptions.RemoveEmptyEntries); // See EDIT below!
//
//        foreach (string commandText in commandTextArray)
//        {
//            if (commandText.Trim() == String.Empty) continue;
//            conn.Execute (commandText);
//        }
//    }
//
//    public void RefreshDatabase (Context context)
//    {
//        //wipes out database with fresh copy from assets folder
//        //todo find out how to update database with deltas if there are changes between versions
//
//        String dbPath = Path.Combine (Android.OS.Environment.ExternalStorageDirectory.ToString (), dbName);
//        // Check if your DB has already been extracted.
//
//        using (BinaryReader br = new BinaryReader(context.Assets.Open(dbName)))
//        {
//            using (BinaryWriter bw = new BinaryWriter(new FileStream(dbPath, FileMode.OpenOrCreate)))
//            {
//                byte[] buffer = new byte[2048];
//                int len = 0;
//                while ((len = br.Read(buffer, 0, buffer.Length)) > 0)
//                {
//                    bw.Write (buffer, 0, len);
//                }
//            }
//        }
//    }
//
//    public void CreateTables(){
//
//        conn.CreateTable<MMLocation> ();
//
//
//        conn.CreateTable<Locations> ();
//
//        conn.CreateTable<BtDevices> ();
//
//    }
//
//    public int AddNewBtDevice(BtDevice btdevice, GPSLocation newGpsLocation){
//
//        iBeacon_Indexer.BtDevices btDevicesIndividual = new BtDevices();
//        btDevicesIndividual.Name = btdevice.Name;
//        btDevicesIndividual.Type = btdevice.Type;
//        btDevicesIndividual.MacAddress = btdevice.MacAddress;
//        btDevicesIndividual.Strength = btdevice.Strength;
//        btDevicesIndividual.UUID = btdevice.UuidString;
//        btDevicesIndividual.Major = btdevice.MajorInt;
//        btDevicesIndividual.Minor = btdevice.MinorInt;
//
//        int newBTDeviceId = conn.Insert(btDevicesIndividual);;
//
//        newGpsLocation.BtDevicesId = newBTDeviceId;
//        conn.Insert (newGpsLocation);
//
//        return newBTDeviceId;
//    }
//
//    public int AddUpdateBtDevice(BtDevice btdevice, GPSLocation gpsLocation){
//
//
//        int btDeviceId = 0;
//
//        iBeacon_Indexer.BtDevices btDevicesIndividual = new BtDevices();
//
//        btDevicesIndividual.Name = btdevice.Name;
//        btDevicesIndividual.Type = btdevice.Type;
//        btDevicesIndividual.MacAddress = btdevice.MacAddress;
//        btDevicesIndividual.Strength = btdevice.Strength;
//        btDevicesIndividual.UUID = btdevice.UuidString;
//        btDevicesIndividual.Major = btdevice.MajorInt;
//        btDevicesIndividual.Minor = btdevice.MinorInt;
//        btDevicesIndividual.TimeFound = btdevice.TimeFound.ToString();
//
//        BtDevices deviceAlreadyExists = GetBtDevice (btdevice.UuidString, btdevice.MajorInt, btdevice.MinorInt);
//        if (deviceAlreadyExists == null) {
//            btDeviceId = conn.Insert (btDevicesIndividual);
//            gpsLocation.BtDevicesId = btDeviceId;
//            conn.Insert (gpsLocation);
//        } else {
//            btDevicesIndividual.Id = deviceAlreadyExists.Id;
//            conn.Update (btDevicesIndividual);
//
//            GPSLocation existingGPSLocation = GetGpsLocation(btDevicesIndividual.Id);
//            if (existingGPSLocation == null) {
//                existingGPSLocation = new GPSLocation ();
//                existingGPSLocation.BtDevicesId = btDeviceId;
//                existingGPSLocation.Address = gpsLocation.Address;
//                existingGPSLocation.Altitude = gpsLocation.Altitude;
//                existingGPSLocation.LatitudeLongitude = gpsLocation.LatitudeLongitude;
//                conn.Insert (gpsLocation);
//            } else {
//                existingGPSLocation.BtDevicesId = btDeviceId;
//                existingGPSLocation.Address = gpsLocation.Address;
//                existingGPSLocation.Altitude = gpsLocation.Altitude;
//                existingGPSLocation.LatitudeLongitude = gpsLocation.LatitudeLongitude;
//                conn.Update (existingGPSLocation);
//            }
//
//        }
//
//        return btDeviceId;
//    }
//
//
//    public GPSLocation GetGpsLocation(int btDeviceId){
//        var results =  conn.Table<GPSLocation>().Where(x => x.BtDevicesId == btDeviceId);
//
//        return results.FirstOrDefault();
//    }
//
//    public BtDevices GetBtDevice(string uuid, int major, int minor){
//        var results =  conn.Table<BtDevices>().Where(x => x.UUID == uuid && x.Major == major && x.Minor == minor);
//
//        return results.FirstOrDefault();
//    }
//
//    public List<BtDevices> GetAllBtDevices(){
//        var results =  conn.Table<BtDevices>();
//
//        return results.ToList();
//    }
//
//    public Locations GetLocationName(string uuid, int major, int minor){
//
//        var results = conn.Table<MMLocation>().Where (x => x.UUID == uuid && x.Major == major && x.Minor == minor);
//        MMLocation resultMMLocation = results.FirstOrDefault ();
//
//        int locationId = (resultMMLocation != null ? resultMMLocation.Id : 1);
//
//        TableQuery<Locations> locationNameResult = conn.Table<Locations>().Where (x => x.Id == locationId);
//
//        return locationNameResult.FirstOrDefault();
//
//    }

//    public void PopulateData(){
//
//        var s = new MMLocation { Id = 1,
//                Major = 1854,
//                Minor = 36039,
//                LocationId = 1
//        };
//        conn.Insert (s);
//        s = new MMLocation { Id = 2,
//                Major = 43942,
//                Minor = 1810,
//                LocationId = 2
//        };
//        conn.Insert (s);
//        s = new MMLocation { Id = 3,
//                Major = 62614,
//                Minor = 62288,
//                LocationId = 3
//        };
//        conn.Insert (s);
//
//        var l = new Locations { Id = 1,
//                Name = "Bedroom"
//        };
//        conn.Insert (l);
//        l = new Locations {
//            Id = 2,
//                    Name = "Couch"
//        };
//        conn.Insert (l);
//        l = new Locations {
//            Id = 3,
//                    Name = "Table"
//        };
//        conn.Insert (l);
//    }

    public SQLiteDatabase GetConnection(){
        return conn;
    }

//    public void ExportDatabaseToExternalStorage(){
//        String sdcardpath = System.IO.Path.Combine (Android.OS.Environment.ExternalStorageDirectory.Path, "MajorMinorIndex.s3dm");
//
//        String databaseLocation = System.IO.Path.Combine (System.Environment.GetFolderPath (System.Environment.SpecialFolder.Personal), "MajorMinorIndex.s3db");
//
//        if(System.IO.File.Exists(sdcardpath)){
//            System.IO.File.Copy (databaseLocation, sdcardpath, true);
//        }
//    }

}