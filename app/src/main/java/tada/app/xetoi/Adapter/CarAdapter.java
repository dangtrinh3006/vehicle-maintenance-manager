package tada.app.xetoi.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import tada.app.xetoi.Model.Vehicle;
import tada.app.xetoi.R;

import java.util.ArrayList;

import tada.app.xetoi.UI.history.CarHistoryFragment;

public class CarAdapter extends ArrayAdapter {
    private Context context;
    private int layout;
    private ArrayList<Vehicle> carList;

    public static class ViewHolder{
        TextView tv_Car_Name;
        TextView tv_Car_Number;
        LinearLayout itemCar;
    }
    public CarAdapter(Context context, int layout, ArrayList<Vehicle> carList){
        super(context, R.layout.item_car, carList);
        this.context = context;
        this.layout = layout;
        this.carList = carList;

    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        Vehicle c = carList.get(pos);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_car,parent, false);
            viewHolder.tv_Car_Name = convertView.findViewById(R.id.tvVehicleName);
            viewHolder.tv_Car_Number = convertView.findViewById(R.id.tvVehicleNumber);
            viewHolder.itemCar = convertView.findViewById(R.id.item_car);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tv_Car_Name.setText(c.getName());
        viewHolder.tv_Car_Number.setText(c.getNumber());

        return convertView;
    }
}
