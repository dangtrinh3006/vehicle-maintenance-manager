package tada.app.xetoi;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import tada.app.xetoi.Model.JsonParser;
import tada.app.xetoi.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity {
    private static final String TAG = "MAP_ACTIVITY";

    private GoogleMap _googleMap;
    private ActivityMapsBinding binding;

    SupportMapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

    double latitude = 0;
    double longitude = 0;

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;
    Animation fromRight;
    Animation toLeft;

    private boolean isFABOpen = true;

    private static final String[] quickPlaceTypeList = {"atm", "parking", "gas_station", "hospital"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Prevent rotate screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set animation resource
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_open);
        rotateClose = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_rotate_close);
        fromBottom = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_from_bottom);
        toBottom = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_to_bottom);
        fromRight = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_from_right);
        toLeft = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_to_left);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        // Map zoom in
        binding.fabZoomIn.setOnClickListener(v -> {
            zoomIn();
        });

        // Map zoom out
        binding.fabZoomOut.setOnClickListener(v -> {
            zoomOut();
        });

        // Move to current location
        binding.fabGetCurrentLocation.setOnClickListener(v -> {
            _googleMap.clear();
            getCurrentLocation();
        });

        // Open quick search menu
        binding.fabSearch.setOnClickListener(v -> {
            handleFABMenu();
            if(isFABOpen) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        // Find nearby ATM
        binding.fabATM.setOnClickListener(v -> {
            String url = prepareURL(0);

            // Download json data
            new PlaceTask().execute(url);
        });

        // Find nearby Park lot
        binding.fabParking.setOnClickListener(v -> {
            String url = prepareURL(1);

            // Download json data
            new PlaceTask().execute(url);
        });

        // Find nearby Gas station
        binding.fabGasStation.setOnClickListener(v -> {
            String url = prepareURL(2);

            // Download json data
            new PlaceTask().execute(url);
        });

        // Find nearby Hospital
        binding.fabHospital.setOnClickListener(v -> {
            String url = prepareURL(3);

            // Download json data
            new PlaceTask().execute(url);
        });

        // Finish activity
        binding.ivMapFinishButton.setOnClickListener(v -> {
            finish();
        });

        // Search for place
        binding.svSearchPlace.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _googleMap.clear();

                String location = binding.svSearchPlace.getQuery().toString();
                List<Address> addresses = null;

                if(!location.matches("")) {
                    Geocoder geocoder = new Geocoder(getBaseContext());

                    try {
                        addresses = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        _googleMap.addMarker(new MarkerOptions().position(latLng).title(address.getFeatureName()));
                        _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private String prepareURL(int placeType) {
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + // Main url
                "?location=" + latitude + "," + longitude + // Location
                "&radius=5000" + // Nearby radius
                "&type=" + quickPlaceTypeList[placeType] + // Place type
                "&sensor=true" + // Sensor
                "&key=" + getResources().getString(R.string.google_maps_key);
    }

    private void getCurrentLocation() {
        Log.d(TAG, "getCurrentLocation: Begin get current location");
        if(ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getCurrentLocation: Check permission");
            Task<Location> task = fusedLocationProviderClient.getLastLocation();

            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    if(location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        Log.d(TAG, "getCurrentLocation: Get location succeeded: Location(lat: " + latitude + ",lng: " + longitude + ")");
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                LatLng current = new LatLng(latitude, longitude);

                                _googleMap = googleMap;
                                _googleMap.addMarker(new MarkerOptions().position(current).title("Vị trí của bạn"));
                                _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current,15));
                            }
                        });
                    }
                    else {
                        Log.d(TAG, "getCurrentLocation: Null location");
                    }
                }
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "getCurrentLocation: Exception: " + e.getMessage());
            });
        }
        else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    private void handleFABMenu() {
        setFABVisibility(isFABOpen);
        setFABAnimation(isFABOpen);
        isFABOpen = !isFABOpen;
    }

    private void setFABVisibility(boolean isOpen) {
        binding.fabGasStation.setVisibility(isOpen ? View.VISIBLE: View.INVISIBLE);
        binding.fabATM.setVisibility(isOpen ? View.VISIBLE: View.INVISIBLE);
        binding.fabParking.setVisibility(isOpen ? View.VISIBLE: View.INVISIBLE);
        binding.fabHospital.setVisibility(isOpen ? View.VISIBLE: View.INVISIBLE);
        binding.svSearchPlace.setVisibility(isOpen ? View.VISIBLE: View.INVISIBLE);
    }

    private void setFABAnimation(boolean isOpen) {
        binding.fabGasStation.startAnimation(isOpen? fromBottom : toBottom);
        binding.fabATM.startAnimation(isOpen? fromBottom : toBottom);
        binding.fabParking.startAnimation(isOpen? fromBottom : toBottom);
        binding.fabHospital.startAnimation(isOpen? fromBottom : toBottom);
        binding.fabSearch.startAnimation(isOpen ? rotateOpen : rotateClose);
        binding.svSearchPlace.startAnimation(isOpen ? fromRight : toLeft);
    }

    private void zoomIn() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                _googleMap = googleMap;
                _googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
    }

    private void zoomOut() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                _googleMap = googleMap;
                _googleMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                Log.d(TAG, "doInBackground: Begin background download: " + strings[0]);
                data = downloadWithUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }

        private String downloadWithUrl(String string) throws IOException {
            Log.d(TAG, "downloadWithUrl: Downloading");
            URL url = new URL(string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String data = builder.toString();

            reader.close();
            connection.disconnect();

            return data;
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();

            List<HashMap<String, String>> mapList = null;
            JSONObject object;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            _googleMap.clear();
            getCurrentLocation();
            Log.d(TAG, "onPostExecute: Finish download");
            Log.d(TAG, "onPostExecute: Data size: " + hashMaps.size());
            for (int i = 0; i < hashMaps.size(); i++) {
                HashMap<String,String> hashMapList = hashMaps.get(i);

                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));

                String name = hashMapList.get("name");

                LatLng latLng = new LatLng(lat,lng);

                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                _googleMap.addMarker(options);
            }
        }
    }
}