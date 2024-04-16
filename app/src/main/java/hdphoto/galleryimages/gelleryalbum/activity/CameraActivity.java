package hdphoto.galleryimages.gelleryalbum.activity;

import static hdphoto.galleryimages.gelleryalbum.utils.Utils.stringMutableLiveData;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.permissionx.guolindev.PermissionX;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityActCameraBinding;
import hdphoto.galleryimages.gelleryalbum.utils.Utils;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback {

    SurfaceHolder surfaceHolder;
    Camera camera;
    boolean lightOn = false;
    SurfaceView surfaceView;
    boolean rotate = false;
    ActivityActCameraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        clickListener();
        requestAllPermissions();
    }

    private void initView() {
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void clickListener() {
        binding.ivDone.setOnClickListener(view -> {
            stringMutableLiveData.setValue("1");
            finish();
        });

        binding.imgRotateCamera.setOnClickListener(view -> {
            if (!rotate) {
                rotate = true;
                binding.imgFlashon.setVisibility(View.GONE);
                startCamera(1);
            } else {
                rotate = false;
                binding.imgFlashon.setVisibility(View.VISIBLE);
                startCamera(0);
            }
        });

        binding.ivRestore.setOnClickListener(view -> {
            rotate = false;
            binding.rlPreview.setVisibility(View.GONE);
            binding.rlCamera.setVisibility(View.VISIBLE);
            binding.imgFlashon.setVisibility(View.VISIBLE);
            startCamera(0);
        });

        binding.imgFlashon.setOnClickListener(v -> {
            try {
                Camera.Parameters p = camera.getParameters();
                if (!lightOn) {
                    lightOn = true;
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    binding.imgFlashon.setImageDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_flash_off));
                } else {
                    lightOn = false;
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(p);
                    binding.imgFlashon.setImageDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_flash_on));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.startBtn.setOnClickListener(view -> captureImage(this));
    }

    private void requestAllPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionX.init(CameraActivity.this)
                    .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                    .explainReasonBeforeRequest()
                    .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                        scope.showRequestReasonDialog(deniedList, "PermissionX needs following permissions to continue", "Allow");
                    })
                    .onForwardToSettings((scope, deniedList) -> {
                        scope.showForwardToSettingsDialog(deniedList, "Please allow following permissions in settings", "Allow");
                    })
                    .request((allGranted, grantedList, deniedList) -> {
                        if (!allGranted) {
                            Toast.makeText(CameraActivity.this, "The following permissions are denied：" + deniedList, Toast.LENGTH_SHORT).show();
                        } else {
                            setupSurfaceHolder();
                        }
                    });
        } else {
            PermissionX.init(CameraActivity.this)
                    .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .explainReasonBeforeRequest()
                    .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                        scope.showRequestReasonDialog(deniedList, "PermissionX needs following permissions to continue", "Allow");
                    })
                    .onForwardToSettings((scope, deniedList) -> {
                        scope.showForwardToSettingsDialog(deniedList, "Please allow following permissions in settings", "Allow");
                    })
                    .request((allGranted, grantedList, deniedList) -> {
                        if (!allGranted) {
                            Toast.makeText(CameraActivity.this, "The following permissions are denied：" + deniedList, Toast.LENGTH_SHORT).show();
                        } else {
                            setupSurfaceHolder();
                        }
                    });
        }
    }

    private void setupSurfaceHolder() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    public void setCamFocusMode() {
        try {
            if (camera == null) {
                return;
            }

            Camera.Parameters parameters = camera.getParameters();
            java.util.List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        if (camera != null) {
            camera.stopPreview();
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }
    }

    public void captureImage(Camera.PictureCallback callback) {
        try {
            if (camera != null) {
                camera.takePicture(null, null, callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCamera(int i) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        camera = Camera.open(i);

        camera.setDisplayOrientation(90);
        try {
            Camera.Parameters params = camera.getParameters();
            camera.setPreviewDisplay(surfaceHolder);
            List<Camera.Size> supportedSizes = params.getSupportedPreviewSizes();
            Camera.Size optimalSize = getOptimalPreviewSize(supportedSizes, surfaceView.getWidth(), surfaceView.getHeight());
            params.setPreviewSize(optimalSize.width, optimalSize.height);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            params.set("orientation", "portrait");

            camera.setParameters(params);

            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stopPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rotate = false;
        binding.imgFlashon.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startCamera(0);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) height / width;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            double diff = Math.abs(ratio - targetRatio);

            if (diff < minDiff) {
                optimalSize = size;
                minDiff = diff;
            }
        }

        return optimalSize;
    }

    public void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public Uri getImageUri(Bitmap inImage) throws IOException {
        File tempFile = File.createTempFile("temprentpk", ".jpg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();

        FileOutputStream fileOutPut = new FileOutputStream(tempFile);
        fileOutPut.write(bitmapData);
        fileOutPut.flush();
        fileOutPut.close();
        return Uri.fromFile(tempFile);
    }

    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        int x = original.getWidth();
        int y = original.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        return Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
    }

    Bitmap imageBitmap;

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        try {
            imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            if (rotate) {
                imageBitmap = rotateBitmap(imageBitmap, 270f);
            } else {
                imageBitmap = rotateBitmap(imageBitmap, 90f);
            }

            Uri imageUrii = getImageUri(imageBitmap);
            Utils.imageUri = imageUrii;
            Utils.imageBitmap = imageBitmap;
            resetCamera();
            binding.ivPreview.setImageBitmap(imageBitmap);
            binding.rlCamera.setVisibility(View.GONE);
            binding.rlPreview.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        startCamera(0);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        resetCamera();
        setCamFocusMode();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        releaseCamera();
    }
}