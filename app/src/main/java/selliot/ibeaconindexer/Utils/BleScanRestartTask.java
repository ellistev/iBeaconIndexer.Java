package selliot.ibeaconindexer.Utils;

import android.os.Handler;
import android.os.Looper;

public class BleScanRestartTask {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable mStatusChecker;
    private int UPDATE_INTERVAL = 2000;

    public BleScanRestartTask(final Runnable uiUpdater) {
        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                // Run the passed runnable
                uiUpdater.run();
                // Re-run it after the update interval
                mHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
    }

    public BleScanRestartTask(Runnable uiUpdater, int interval){
        this(uiUpdater);
        UPDATE_INTERVAL = interval;
    }

    public synchronized void startUpdates(){
        mStatusChecker.run();
    }

    public synchronized void stopUpdates(){
        mHandler.removeCallbacks(mStatusChecker);
    }
}