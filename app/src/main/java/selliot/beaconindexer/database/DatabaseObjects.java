package selliot.beaconindexer.database;

import android.;
/**
 * Created by selliot on 2015-01-29.
 */
public class DatabaseObjects {
    public class MMLocation : IBusinessEntity
    {
        [PrimaryKey, AutoIncrement]
        public int ID { get; set; }
        [Indexed]
        public string UUID { get; set; }
        public int Major { get; set; }
        public int Minor { get; set; }
        public int LocationId { get; set; }

    }

    public class Locations : IBusinessEntity
    {
        [PrimaryKey, AutoIncrement]
        public int ID { get; set; }
        [Indexed]
        public string Name { get; set; }
    }

    public class GPSLocation : IBusinessEntity
    {
        [PrimaryKey, AutoIncrement]
        public int ID { get; set;}
        [Indexed]
        public int BtDevicesId { get; set;}
        public string Address { get; set;}
        public string LatitudeLongitude { get; set;}
        public string Altitude {get; set;}
    }

    public class BtDevices : IBusinessEntity
    {
        [PrimaryKey, AutoIncrement]
        public int ID { get; set; }
        [Indexed]
        public string Name{ get; set; }
        public string Type{ get; set; }
        public string MacAddress{ get; set; }
        public int Strength{ get; set; }
        public string UUID{ get; set; }
        public int Major{ get; set; }
        public int Minor{ get; set; }
        public string TimeFound {get;set;}


    }

    public class DBSchemaVersion : IBusinessEntity
    {
        [PrimaryKey, AutoIncrement]
        public int ID { get; set; }
        [Indexed]
        public int LastDBSchemaVersion { get; set; }
    }

    public class DbDataVersion : IBusinessEntity
    {
        [PrimaryKey, AutoIncrement]
        public int ID { get; set; }
        [Indexed]
        public int LastDBDataVersion { get; set; }
    }
}
