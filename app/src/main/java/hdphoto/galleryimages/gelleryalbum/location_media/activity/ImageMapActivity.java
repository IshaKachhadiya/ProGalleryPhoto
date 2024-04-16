package hdphoto.galleryimages.gelleryalbum.location_media.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityImageMapBinding;
import hdphoto.galleryimages.gelleryalbum.location_media.model.LocationImageData;

public class ImageMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {
    Location currentLocation;
    GoogleMap googleMap;
    private int imageCount;
    private double latitude;
    private boolean locationChanged;
    private ArrayList<LocationImageData> locationImageData;
    private LocationManager locationManager;
    private double longitude;
    private int position = -1;

    public static void lambda$initView$1() {
    }

    @Override
    public void onProviderDisabled(String str) {
    }

    @Override
    public void onProviderEnabled(String str) {
    }

    @Override
    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    ActivityImageMapBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityImageMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.locationImageData = (ArrayList) getIntent().getSerializableExtra("data");
        initView();
        getLocation();
    }


    private void initView() {
        if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && checkPermission("android.permission.ACCESS_FINE_LOCATION")) {
            ImageMapActivity.lambda$initView$1();

        }
        ((SupportMapFragment) Objects.requireNonNull((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))).getMapAsync(this);

        binding.icBack.setOnClickListener(v->{
            onBackPressed();
        });
    }

    public void getLocation() {
        if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && checkPermission("android.permission.ACCESS_FINE_LOCATION")) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            String bestProvider = locationManager.getBestProvider(new Criteria(), false);
            locationManager.requestLocationUpdates((String) Objects.requireNonNull(bestProvider), 30000L, 10.0f, this);
            Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
            Location lastKnownLocation2 = locationManager.getLastKnownLocation("network");
            if (lastKnownLocation != null) {
                currentLocation = lastKnownLocation;
                locationChanged = true;
                onLocationChanged(lastKnownLocation);
                return;
            } else if (lastKnownLocation2 != null) {
                currentLocation = lastKnownLocation2;
                locationChanged = true;
                onLocationChanged(lastKnownLocation2);
                return;
            } else {
                showLocationOnDialog();
                return;
            }
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 100);
    }

    public void showLocationOnDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.setContentView(R.layout.dialog_location_on);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.actionOnLocation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 100);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 100) {
            if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && checkPermission("android.permission.ACCESS_FINE_LOCATION")) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String bestProvider = locationManager.getBestProvider(new Criteria(), false);
                locationManager.requestLocationUpdates((String) Objects.requireNonNull(bestProvider), 30000L, 10.0f, this);
                Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                Location lastKnownLocation2 = locationManager.getLastKnownLocation("network");
                if (lastKnownLocation != null) {
                   currentLocation = lastKnownLocation;
                   locationChanged = true;
                    onLocationChanged(lastKnownLocation);
                    return;
                } else if (lastKnownLocation2 != null) {
                    currentLocation = lastKnownLocation2;
                    locationChanged = true;
                    onLocationChanged(lastKnownLocation2);
                    return;
                } else {
                    showLocationOnDialog();
                    return;
                }
            }
            Toast.makeText(this, getString(R.string.Permission_not_allowed), 0).show();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        int i3;
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            if (i == 100) {
                getLocation();
            } else if (i == 101 && (i3 = position) != -1) {
                locationImageData.set(i3, (LocationImageData) ((Intent) Objects.requireNonNull(intent)).getSerializableExtra("data"));
                googleMap.clear();
                if (locationImageData.size() > 0) {
                    imageCount = 0;
                    addMarkerImage();
                }
            }
        }
    }

    public boolean checkPermission(String str) {
        return ContextCompat.checkSelfPermission(this, str) == 0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        googleMap.setBuildingsEnabled(false);
        googleMap.setIndoorEnabled(false);
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.setOnMarkerClickListener(this);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        if (latitude != 0.0d && longitude != 0.0d) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
        }
        if (this.locationImageData.size() > 0) {
            this.imageCount = 0;
            addMarkerImage();
        }
    }

    private void addMarkerImage() {
        final LatLng latLng = new LatLng(Double.parseDouble(this.locationImageData.get(this.imageCount).getLat()), Double.parseDouble(this.locationImageData.get(this.imageCount).getLongs()));
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_location_image, (ViewGroup)binding.root, false);
        Glide.with((FragmentActivity) this).load(this.locationImageData.get(this.imageCount).getPictureData().get(0).getFilePath()).into((CircleImageView) inflate.findViewById(R.id.locationImage));
        binding.demoView.addView(inflate);
        try {
            Thread.sleep(150L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        binding.demoView.post(new Runnable() {
            @Override
            public void run() {
                Bitmap createBitmap = Bitmap.createBitmap(binding.demoView.getWidth(), binding.demoView.getHeight(), Bitmap.Config.ARGB_8888);
                binding.demoView.draw(new Canvas(createBitmap));
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(createBitmap)).anchor(0.5f, 1.0f)).setTag(Integer.valueOf(imageCount));
                int i = imageCount + 1;
                imageCount = i;
                if (i < locationImageData.size()) {
                    addMarkerImage();
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (locationChanged) {
            locationChanged = false;
            try {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                GoogleMap googleMap = this.googleMap;
                if (googleMap != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.latitude, this.longitude), 12.0f));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.position = ((Integer) marker.getTag()).intValue();
        startActivityForResult(new Intent(this, LocationImageListActivity.class).putExtra("data", this.locationImageData.get(this.position)), 101);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}