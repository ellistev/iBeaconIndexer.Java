package selliot.ibeaconindexer.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import selliot.ibeaconindexer.Model.BluetoothObjects.BtDevice;
import selliot.ibeaconindexer.R;

/**
 * Created by Steven on 2015-02-10.
 */
public class BtDeviceArrayAdapter extends ArrayAdapter<BtDevice>
{

public static List<BtDevice> btDeviceList;
private Activity _activity;


    public BtDeviceArrayAdapter(Activity activity, Context context,int resourceId)
            {
            super(context, resourceId);
            _activity = activity;
            btDeviceList = new ArrayList<BtDevice>();
            }

    public static List<BtDevice> getBtDeviceList()
            {
            return btDeviceList;
            }

    public void add(BtDevice item)
            {
            btDeviceList.add(item);
            }

    public void addList(List<BtDevice> list)
            {
            btDeviceList = list;
            }

    public List<BtDevice> getBlueToothList()
            {
            return btDeviceList;
            }

    public int getCount()
            {
            return btDeviceList.size();
            }

    public BtDevice getBlueToothListItem(int position)
            {
            return btDeviceList.get(position);
            }

    public  View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView != null ? convertView : _activity.getLayoutInflater().inflate(R.layout.bluetooth_list_item_view, parent, false);

        BtDevice currentDevice = btDeviceList.get(position);

        TextView nameView = (TextView)view.findViewById(R.id.btListItemName);
        TextView uuidView = (TextView)view.findViewById(R.id.btListItemUuid);
        TextView majorView = (TextView)view.findViewById(R.id.btListItemMajor);
        TextView minorView = (TextView)view.findViewById(R.id.btListItemMinor);
        TextView strengthView = (TextView)view.findViewById(R.id.btListStrength);

        String majorString = String.valueOf(currentDevice.MajorInt);
        String minorString = String.valueOf(currentDevice.MinorInt);
        String strengthString = String.valueOf(currentDevice.Strength);

        nameView.setText(currentDevice.Name);
        uuidView.setText(currentDevice.UuidString);
        majorView.setText(majorString);
        minorView.setText(minorString);
        strengthView.setText(strengthString);

        return view;
    }

    public void notifyDataSetChanged()
    {
    super.notifyDataSetChanged();
    }

    public void notifyDataSetInvalidated()
    {
    super.notifyDataSetInvalidated();
    }
}