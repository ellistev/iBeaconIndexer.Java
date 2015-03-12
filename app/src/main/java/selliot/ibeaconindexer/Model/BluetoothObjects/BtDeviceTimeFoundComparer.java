package selliot.ibeaconindexer.Model.BluetoothObjects;

import java.util.Comparator;

public class BtDeviceTimeFoundComparer implements Comparator<BtDevice> {
    @Override
    public int compare(BtDevice x, BtDevice y) {
        // TODO: Handle null x or y values
        int startComparison = compare(y.TimeFound.getTime(), x.TimeFound.getTime());
        return startComparison != 0 ? startComparison
                : compare(y.TimeFound.getTime(), x.TimeFound.getTime());
    }

    // I don't know why this isn't in Long...
    private static int compare(long a, long b) {
        return a < b ? -1
                : a > b ? 1
                : 0;
    }
}
