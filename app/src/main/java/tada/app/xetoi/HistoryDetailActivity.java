package tada.app.xetoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tada.app.xetoi.Model.History;
import tada.app.xetoi.Model.Vehicle;
import tada.app.xetoi.databinding.ActivityHistoryDetailBinding;

public class HistoryDetailActivity extends AppCompatActivity {
    private static final String TAG = "HISTORY DETAIL ACTIVITY";
    private ActivityHistoryDetailBinding binding;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private Vehicle vehicle;
    private String vehicleID;
    private String historyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        onStartUp();


    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();

        // Date picker
        binding.btnHistoryDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int lastSelectedYear = calendar.get(Calendar.YEAR);
            int lastSelectedMonth = calendar.get(Calendar.MONTH);
            int lastSelectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
                String time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                binding.etHistoryDate.setText(time);
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

            datePickerDialog.show();
        });

        // Save
        binding.btnHistorySave.setOnClickListener(v -> {
            addHistory();
            finish();
        });

        // Back button
        binding.ivHistoryFinishButton.setOnClickListener(v -> {
            finish();
        });

        // Export button
        binding.ivHistoryExportButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG,"Permission is granted");
                    exportPDF();
                } else {
                    Log.v(TAG,"Permission is revoked");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                }
            }
            else { //permission is automatically granted on sdk<23 upon installation
                Log.v(TAG,"Permission is granted");
            }
        });

    }

    private void onStartUp() {
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            startActivity(new Intent(HistoryDetailActivity.this, SplashScreenActivity.class));
        }
    }

    private void getData() {
        vehicleID = getIntent().getExtras().getString("vehicleID");
        historyID = getIntent().getExtras().getString("historyID");

        if ((vehicleID == null || vehicleID.matches("")) ||
                (historyID == null || historyID.matches(""))) {
            finish();
            return;
        }

        DatabaseReference vehicleRef = mDatabase.getReference("User")
                .child(firebaseUser.getUid())
                .child("ListVehicle")
                .child(vehicleID);

        DatabaseReference historyRef = mDatabase.getReference("User")
                .child(firebaseUser.getUid())
                .child("ListVehicle")
                .child(vehicleID)
                .child("ListHistory")
                .child(historyID);

        vehicleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vehicle = snapshot.getValue(Vehicle.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                History history = snapshot.getValue(History.class);
                if (history != null) {
                    binding.etHistoryVehicle.setText(vehicle.getName());
                    binding.etHistoryType.setText(history.getType());
                    binding.etHistoryStationName.setText(history.getLocation());
                    binding.etHistoryCurrentKm.setText(String.valueOf(history.getKm()));
                    binding.etHistoryDate.setText(history.getDate());
                    binding.etHistoryDetail.setText(history.getDetail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    private void addHistory() {
        String historyType = binding.etHistoryType.getText().toString();
        String historyDate = binding.etHistoryDate.getText().toString();
        String stationName = binding.etHistoryStationName.getText().toString();
        int currentKilometer = Integer.parseInt(binding.etHistoryCurrentKm.getText().toString());
        String historyDetails = binding.etHistoryDetail.getText().toString();

        DatabaseReference historyRef = mDatabase.getReference("User").child(firebaseUser.getUid()).child("ListVehicle").child(vehicleID).child("ListHistory");

        History history = new History(historyID, historyType, historyDate, stationName, currentKilometer, historyDetails);

        historyRef.child(historyID).setValue(history).addOnSuccessListener(s -> {
            Log.d(TAG, "addVehicle: Add history success");
            Toast.makeText(this, "Lịch sử đã được cập nhật", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(f -> {
            Toast.makeText(this, "Cập nhật thất bại vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "addVehicle: Add history fail with " + f.getMessage());
        });
    }

    private void exportPDF() {
        Bitmap bmp, scaleBmp;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.street);
        scaleBmp = Bitmap.createScaledBitmap(bmp, 1200, 518, false);

        Date date = new Date();
        DateFormat dateFormat;

        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo documentInfo = new PdfDocument.PageInfo.Builder(1200, 2100, 1).create();
        PdfDocument.Page page = document.startPage(documentInfo);
        Canvas canvas = page.getCanvas();

        canvas.drawBitmap(scaleBmp, 0, 0, paint);

        title.setTextAlign(Paint.Align.CENTER);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(65);
        title.setColor(Color.rgb(61, 219, 132));
        canvas.drawText("Xe Tôi", 600, 270, title);

        paint.setColor(Color.rgb(255,255,255));
        paint.setTextSize(45);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        canvas.drawText("Theo bạn trên mọi hành trình", 600, 310, paint);

        title.setTextAlign(Paint.Align.CENTER);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        title.setTextSize(45);
        title.setColor(Color.rgb(1, 22, 39));
        canvas.drawText("Lịch sử hoạt động", 600, 600, title);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(Color.rgb(1, 22, 39));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(20);
        canvas.drawText("User ID: " + firebaseUser.getUid(), 20, 650, paint);
        canvas.drawText("Username: " + firebaseUser.getDisplayName(), 20, 700, paint);
        canvas.drawText("Vehicle ID: " + vehicleID, 20, 750, paint);

        String dateRegex = "dd/mm/yyyy";
        String timeRegex = "HH:mm:ss";


        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("History ID: " + historyID, 1180, 630, paint);
        canvas.drawText("History type: " + binding.etHistoryType.getText().toString(), 1180, 680, paint);

        dateFormat = new SimpleDateFormat(dateRegex);
        canvas.drawText("Export date: " + dateFormat.format(date), 1180, 730, paint);

        dateFormat = new SimpleDateFormat(timeRegex);
        canvas.drawText("Export time: " + dateFormat.format(date), 1180, 780, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(20, 830, 1180, 880, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Date", 40, 860, paint);
        canvas.drawText("Station", 250, 860, paint);
        canvas.drawText("Km", 450, 860, paint);
        canvas.drawText("Detail", 650, 860, paint);

        canvas.drawLine(240, 830, 240, 880, paint);
        canvas.drawLine(440, 830, 440, 880, paint);
        canvas.drawLine(640, 830, 640, 880, paint);

        canvas.drawText(binding.etHistoryDate.getText().toString(), 25, 900, paint);
        canvas.drawText(binding.etHistoryStationName.getText().toString(), 245, 900, paint);
        canvas.drawText(binding.etHistoryCurrentKm.getText().toString(), 445, 900, paint);
        canvas.drawText(binding.etHistoryDetail.getText().toString(), 645, 900, paint);

        title.setTextAlign(Paint.Align.CENTER);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(35);
        title.setColor(Color.rgb(61, 219, 132));
        canvas.drawText("Cảm ơn vì đã sử dụng dịch vụ", 600, 2000, title);

        document.finishPage(page);

        File file = new File(Environment.getExternalStorageDirectory(), "/history.pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Exported", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Log.d(TAG, "exportPDF: " + e.getMessage());
        }
    }
}