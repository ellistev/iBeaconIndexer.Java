package selliot.ibeaconindexer.Utils;

import java.io.File;

/**
 * Created by Steven on 2015-02-10.
 */
public class UtilClass {

    public static String combine(String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }
}
