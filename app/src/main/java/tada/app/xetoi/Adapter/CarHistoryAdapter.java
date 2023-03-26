package tada.app.xetoi.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import tada.app.xetoi.Model.History;
import tada.app.xetoi.R;
import java.util.List;

public class CarHistoryAdapter extends ArrayAdapter {
    private Context context;
    private int layout;
    private List<History> carList;

    public CarHistoryAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public static class ViewHolder{
        TextView tv_KM;
        TextView tv_type;
        TextView tv_time;
        LinearLayout itemHistory;

    }
    public CarHistoryAdapter(Context context, int layout, List<History> carList){
        super(context, R.layout.item_car, carList);
        this.context = context;
        this.layout = layout;
        this.carList = carList;

    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        History h = carList.get(pos);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_history,parent, false);
            viewHolder.tv_KM = convertView.findViewById(R.id.tv_KM);
            viewHolder.tv_type = convertView.findViewById(R.id.tv_type);
            viewHolder.tv_time = convertView.findViewById(R.id.tv_Time);
            viewHolder.itemHistory = convertView.findViewById(R.id.itemHistory);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //viewHolder.tv_carName.setText(h.getCar().getName());
        viewHolder.tv_KM.setText(String.valueOf(h.getKm()));
        viewHolder.tv_type.setText(h.getType());
        viewHolder.tv_time.setText(h.getDate().toString());

        return convertView;
    }
}

