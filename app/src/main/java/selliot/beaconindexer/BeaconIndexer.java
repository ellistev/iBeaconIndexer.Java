package selliot.beaconindexer;

import selliot.beaconindexer.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.sql.Connection;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class BeaconIndexer extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);


        String sqliteFilename = "MajorMinorIndex.3db";
        String libraryPath = System.Environment.GetFolderPath(system.SpecialFolder.Personal);
        //var path = Path.Combine(libraryPath, sqliteFilename);
        String pathToDatabase = getDatabasePath ("MajorMinorIndex3db.3db").getPath();
        conn = new Connection(libraryPath + "/" + sqliteFilename);

        DeviceCollectionManager BLEMgr = new DeviceCollectionManager(conn);
        db = BLEMgr.repository.db.database;
        db.CreateTable<MMLocation>();
        db.CreateTable<Locations>();
        db.CreateTable<GPSLocation>();
        db.CreateTable<BtDevices>();
        db.CreateTable<DBSchemaVersion>();
        db.CreateTable<DbDataVersion>();

        Button mainBlueToothButton = findViewById()<Button>(R.id.MainBlueToothButton);
        mainBlueToothButton.Click += (sender, e) =>
        {
            var intent = new Intent(this, typeof(BlueToothDiscover));
            StartActivity(intent);
        };

        Button mainSettingsButton = findViewById()<Button>(R.id.MainSettingsButton);
        mainSettingsButton.Click += (sender, e) =>
        {
            var intent = new Intent(this, typeof(SettingsActivity));
            StartActivity(intent);
        };
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
