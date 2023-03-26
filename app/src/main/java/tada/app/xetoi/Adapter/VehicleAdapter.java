package tada.app.xetoi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tada.app.xetoi.Model.Vehicle;
import tada.app.xetoi.R;
import tada.app.xetoi.VehicleDetailActivity;
import tada.app.xetoi.VehicleFormActivity;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {
    private List<Vehicle> lstVehicle;
    private Context mContext;


    public VehicleAdapter(Context context,List<Vehicle> lstVehicle) {
        this.mContext = context;
        this.lstVehicle = lstVehicle;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        mContext = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = lstVehicle.get(position);
        if (vehicle == null){
            return;
        }
        if (vehicle.getType() == "Car" ){

            holder.imgVehicle.setImageResource(R.drawable.car);
        } else {
            holder.imgVehicle.setImageResource(R.drawable.bike);
        }
        holder.tvVehicleName.setText(vehicle.getName());
        holder.tvVehicleNumber.setText(vehicle.getNumber());
        holder.vehicle_item.setOnClickListener(view -> {
            onClickDetail(vehicle);
        });

    }
    private void onClickDetail(Vehicle v) {
        Intent i =new Intent(mContext, VehicleFormActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_vehicle", v);
        i.putExtras(bundle);
        mContext.startActivity(i);
    }
    @Override
    public int getItemCount() {
        if (lstVehicle != null){
            return lstVehicle.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout vehicle_item;
        ImageView imgVehicle;
        TextView tvVehicleName, tvVehicleNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            vehicle_item =  itemView.findViewById(R.id.vehicle_item);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            tvVehicleNumber = itemView.findViewById(R.id.tvVehicleNumber);
        }
    }
}
