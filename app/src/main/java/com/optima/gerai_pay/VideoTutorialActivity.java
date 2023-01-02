package com.optima.gerai_pay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class VideoTutorialActivity extends AppCompatActivity {

    public  static YouTubePlayerView youTubePlayerView;
    public static YouTubePlayer youTubePlayers;
    public static RelativeLayout relative_title;
    int one=0;
    ArrayList<Boolean> yes;
    public static String videoId="dpwBu-VullY",videoknow,awalku;

    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_tutorial);

        relative_title = findViewById(R.id.relative_title);

        img_back = findViewById(R.id.img_edit);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
    }

    private void initView() {
        youTubePlayerView   = findViewById(R.id.activity_main_youtubePlayerView);
        yes                 = new ArrayList<>();
        youTubePlayerView.getPlayerUiController().showYouTubeButton(true);
        youTubePlayerView.getPlayerUiController().showSeekBar(true);

        settingVIew();
    }

    private void settingVIew() {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayers = youTubePlayer;
                youTubePlayers.cueVideo(videoId,0);
            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);
            }
        });

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                relative_title.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
                one = 1;
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                relative_title.setVisibility(View.VISIBLE);
                one = 0;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            youTubePlayerView.release();
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initYouTubePlayerView() {
        float isi = Float.parseFloat(awalku);
        youTubePlayers.loadVideo(videoId,isi);
    }

    @Override
    public void onBackPressed() {
        if (one == 1){
            youTubePlayerView.exitFullScreen();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            youTubePlayerView.release();
            finish();
        }
    }
}