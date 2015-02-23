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
        TextView deviceName = (TextView)view.findViewById(R.id.btListItemName);
        TextView deviceType = (TextView)view.findViewById(R.id.btListItemType);
        TextView deviceStrength = (TextView)view.findViewById(R.id.btListItemStength);
        TextView deviceMacAddress = (TextView)view.findViewById(R.id.btListItemMacAddress);
        TextView uuid = (TextView)view.findViewById(R.id.btListItemUuid);
        TextView major = (TextView)view.findViewById(R.id.btListItemMajor);
        TextView minor = (TextView)view.findViewById(R.id.btListItemMinor);
        deviceName.setText(btDeviceList.get(position).Name);
        deviceType.setText(btDeviceList.get(position).Type);
        //deviceStrength.setText(btDeviceList.get(position).Strength.toString());
        deviceMacAddress.setText(btDeviceList.get(position).MacAddress);
        uuid.setText(btDeviceList.get(position).UuidString);
        major.setText(btDeviceList.get(position).Major);
        minor.setText(btDeviceList.get(position).Minor);
        BtDevice deviceInQuestion = btDeviceList.get(position);

        //var dump = ObjectDumper.Dump(deviceInQuestion);

        //deviceRawData.setText(dump);


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