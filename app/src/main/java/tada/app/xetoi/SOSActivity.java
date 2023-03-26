package tada.app.xetoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import tada.app.xetoi.Adapter.CarAdapter;
import tada.app.xetoi.Adapter.SOSAdapter;
import tada.app.xetoi.Model.SOS;
import tada.app.xetoi.Model.Vehicle;

public class SOSActivity extends AppCompatActivity {
    SOSAdapter sosAdapter;
    ArrayList<SOS> listSOS =new ArrayList<>();
    ListView lvSOS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosactivity);
        getdata();
        lvSOS = findViewById(R.id.lvSOS);
        sosAdapter = new SOSAdapter(this,R.layout.sos_item,listSOS);
        lvSOS.setAdapter(sosAdapter);
        lvSOS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getIntent().ACTION_CALL);
                intent.setData(Uri.parse("tel:"+listSOS.get(i).getSOSNumber()));
                startActivity(intent);
            }
        });
    }
    public void getdata(){

        listSOS.add(new SOS("Đội sửa chữa quận 1","0965412293"));
        listSOS.add(new SOS("Đội sửa chữa quận 2","0965412292"));
        listSOS.add(new SOS("Đội sửa chữa quận 3","0965412663"));
        listSOS.add(new SOS("Đội sửa chữa quận 4","0965412463"));
        listSOS.add(new SOS("Đội sửa chữa quận 5","0965412053"));
        listSOS.add(new SOS("Đội sửa chữa quận 6","0965412133"));
        listSOS.add(new SOS("Đội sửa chữa quận 7","0965412233"));
        listSOS.add(new SOS("Đội sửa chữa quận 8","0965412663"));
        listSOS.add(new SOS("Đội sửa chữa quận 9","0965412153"));
        listSOS.add(new SOS("Đội sửa chữa quận 10","0965411493"));
        listSOS.add(new SOS("Đội sửa chữa quận 11","0965612293"));
        listSOS.add(new SOS("Đội sửa chữa quận 12","0968912293"));
        listSOS.add(new SOS("Đội sửa chữa Thành phố Thủ Đức","0965411233"));
        listSOS.add(new SOS("Đội sửa chữa quận Phú Nhuận","0965295623"));
        listSOS.add(new SOS("Đội sửa chữa quận Gò Vấp","0964442293"));
        listSOS.add(new SOS("Đội sửa chữa quận Tân Bình","0965411233"));
        listSOS.add(new SOS("Đội sửa chữa quận Bình Tân","0965654293"));
        listSOS.add(new SOS("Đội sửa chữa quận Tân Phú","0965412223"));
        listSOS.add(new SOS("Đội sửa chữa quận Bình Thạnh","0969992293"));
        listSOS.add(new SOS("Đội sửa chữa Huyện Nhà Bè","0961234293"));
        listSOS.add(new SOS("Đội sửa chữa Huyện Bình Chánh","0965612293"));
        listSOS.add(new SOS("Đội sửa chữa Huyện Củ Chi","0965962293"));
        listSOS.add(new SOS("Đội sửa chữa Huyện Hóc Môn","0956412293"));
        listSOS.add(new SOS("Đội sửa chữa Huyện Cần Giờ","0965789293"));
    }

}