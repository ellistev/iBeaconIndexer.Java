package selliot.ibeaconindexer.Data.Contracts;

import android.provider.BaseColumns;

/**
 * Created by Steven on 2015-03-15.
 */
public final class BtDeviceContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public BtDeviceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class BtDevicesTable implements BaseColumns {
        public static final String TABLE_NAME = "BtDevices";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_MACADDRESS = "macaddress";
        public static final String COLUMN_NAME_STRENGTH = "strength";
        public static final String COLUMN_NAME_UUID = "uuid";
        public static final String COLUMN_NAME_MAJOR = "major";
        public static final String COLUMN_NAME_MINOR = "minor";
        public static final String COLUMN_NAME_TIMEFOUND = "timefound";
        public static final String COLUMN_NAME_FOUNDSTATUS = "foundstatus";
        public static final String COLUMN_NAME_TIMESSEEN = "timesseen";
        public static final String COLUMN_NAME_DISCOVERER = "discoverer";

        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MACADDRESS + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_STRENGTH + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_UUID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MAJOR + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MINOR + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_TIMEFOUND + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_FOUNDSTATUS + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_TIMESSEEN + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DISCOVERER + TEXT_TYPE +
                " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }


}