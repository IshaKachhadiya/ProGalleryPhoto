package hdphoto.galleryimages.gelleryalbum.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import hdphoto.galleryimages.gelleryalbum.R;

public class VideoPlayerActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_player);
        getWindow().setFlags(1024, 1024);

        VideoView videoView = (VideoView) findViewById(R.id.videoView1);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        ((ImageView) findViewById(R.id.backbutton)).setOnClickListener(view -> onBackPressed());

        ((TextView) findViewById(R.id.titlename)).setText(String.valueOf(new File(new File(PreviewActivity.ImgPath).getName())));

        Uri parse = Uri.parse(PreviewActivity.ImgPath);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(parse);
        videoView.requestFocus();
        videoView.start();
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
    }
}
