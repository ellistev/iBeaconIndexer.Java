package selliot.ibeaconindexer.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        ImageView image = (ImageView)view.findViewById(R.id.newOldDeviceImage);
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

        image.setAdjustViewBounds(true);

        Date timeNow = new Date();

        long timeSinceLastSeen = timeNow.getTime() - currentDevice.TimeFound.getTime();
        TextView timeSinceLastSeenView = (TextView)view.findViewById(R.id.btTimeSinceLastSeen);
        timeSinceLastSeenView.setText((String.valueOf(timeSinceLastSeen/1000.00)));


        if(timeSinceLastSeen >= 0 && timeSinceLastSeen < 5000){
            image.setImageResource(R.drawable.green_icon);
        }else if(timeSinceLastSeen >= 5000 && timeSinceLastSeen < 15000){
            image.setImageResource(R.drawable.blue_icon);
        }else if(timeSinceLastSeen >= 15000){
            image.setImageResource(R.drawable.red_icon);
        }else{
            image.setImageResource(0);
        }


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