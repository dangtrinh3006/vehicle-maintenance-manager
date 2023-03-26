package tada.app.xetoi.Adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import tada.app.xetoi.Model.SOS;
import tada.app.xetoi.R;

import java.util.ArrayList;

import tada.app.xetoi.UI.history.CarHistoryFragment;

public class SOSAdapter extends ArrayAdapter {
    private Context context;
    private int layout;
    private ArrayList<SOS> SOSList;

    public static class ViewHolder{
        TextView tv_SOSName;
        TextView tv_SOSNumber;
    }
    public SOSAdapter(Context context, int layout, ArrayList<SOS> SOSList){
        super(context, R.layout.sos_item, SOSList);
        this.context = context;
        this.layout = layout;
        this.SOSList = SOSList;
        Log.d("BBBB", "SOSAdapter: AA");
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        SOS c = SOSList.get(pos);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.sos_item,parent, false);
            viewHolder.tv_SOSName = convertView.findViewById(R.id.tv_SOSName);
            viewHolder.tv_SOSNumber = convertView.findViewById(R.id.tv_SOSNumber);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tv_SOSName.setText(c.getSOSname());
        viewHolder.tv_SOSNumber.setText(c.getSOSNumber());
        Log.d("BBBB", "SOSAdapter: AA");

        return convertView;
    }
}
