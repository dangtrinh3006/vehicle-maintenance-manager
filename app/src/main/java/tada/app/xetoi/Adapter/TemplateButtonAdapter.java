package tada.app.xetoi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;

import tada.app.xetoi.ChangeOilActivity;
import tada.app.xetoi.FixPartActivity;
import tada.app.xetoi.MainActivity;
import tada.app.xetoi.MapsActivity;
import tada.app.xetoi.Model.TemplateButton;
import tada.app.xetoi.R;
import tada.app.xetoi.RefuelActivity;
import tada.app.xetoi.SOSActivity;


public class TemplateButtonAdapter extends RecyclerView.Adapter<TemplateButtonAdapter.ViewHolder> {

    private Context context;
    private List<TemplateButton> listButton;

    public TemplateButtonAdapter(Context context, List<TemplateButton> listButton) {
        this.context = context;
        this.listButton = listButton;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TemplateButton button = listButton.get(position);
        holder.tvButtonName.setText(button.getName());
        holder.ivButtonImage.setImageResource(button.getImage());
        holder.btnHomeMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            switch (position) {
                case 0:
                    intent = new Intent(context, RefuelActivity.class);
                    break;
                case 1:
                    intent = new Intent(context, ChangeOilActivity.class);
                    break;
                case 2:
                    intent = new Intent(context, FixPartActivity.class);
                    break;
                case 3:
                    intent = new Intent(context, MapsActivity.class);
                    break;
                case 4:
                    intent = new Intent(context, SOSActivity.class);
                    break;
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listButton.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvButtonName;
        ImageView ivButtonImage;
        RelativeLayout btnHomeMenuButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvButtonName = itemView.findViewById(R.id.tvButtonName);
            ivButtonImage = itemView.findViewById(R.id.ivButtonImage);
            btnHomeMenuButton = itemView.findViewById(R.id.btnHomeMenuButton);
        }
    }
}
