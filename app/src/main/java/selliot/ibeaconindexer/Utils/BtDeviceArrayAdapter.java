package selliot.ibeaconindexer.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public void Add(BtDevice item)
        {
        btDeviceList.add(item);
        }

public void AddList(List<BtDevice> list)
        {
        btDeviceList = list;
        }

public List<BtDevice> GetBlueToothList()
        {
        return btDeviceList;
        }

public int GetCount()
        {
        return btDeviceList.size();
        }

public BtDevice GetBlueToothListItem(int position)
        {
        return btDeviceList.get(position);
        }

public  View GetView(int position, View convertView, ViewGroup parent)
        {
        View view = convertView == null ? convertView : _activity.getLayoutInflater().inflate(R.layout.bluetooth_list_item_view, parent, false);
        TextView deviceName = (TextView)view.findViewById(R.id.mainBleList);
        TextView deviceType = (TextView)view.findViewById(R.id.btListItemType);
        TextView deviceStrength = (TextView)view.findViewById(R.id.btListItemStength);
        TextView deviceMacAddress = (TextView)view.findViewById(R.id.btListItemMacAddress);
        TextView deviceRawData = (TextView)view.findViewById(R.id.btListItemRaw);
        deviceName.setText(btDeviceList.get(position).Name);
        deviceType.setText("");
        deviceStrength.setText("");
        deviceMacAddress.setText("");
        BtDevice deviceInQuestion = btDeviceList.get(position);

        var dump = ObjectDumper.Dump(deviceInQuestion);

        deviceRawData.Text = dump;


        return view;
        }

public override void NotifyDataSetChanged()
        {
        super.NotifyDataSetChanged();
        }

public override void NotifyDataSetInvalidated()
        {
        super.NotifyDataSetInvalidated();
        }
}