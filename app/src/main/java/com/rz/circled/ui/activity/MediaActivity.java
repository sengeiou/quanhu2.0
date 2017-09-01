package com.rz.circled.ui.activity;

import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.toasty.Toasty;

import butterknife.BindView;


public class MediaActivity extends BaseActivity implements SimpleExoPlayer.VideoListener, ExoPlayer.EventListener {

    @BindView(R.id.playview_exo)
    SimpleExoPlayerView playView;
    private SimpleExoPlayer player;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_media, null);
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    public void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        playView.requestFocus();
    }

    @Override
    public void initData() {

        int mSeekPosition = getIntent().getIntExtra(IntentKey.EXTRA_POSITION, 0);
        String videoUrl = getIntent().getStringExtra(IntentKey.EXTRA_PATH);

        //1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();
        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);

        playView.setPlayer(player);

        player.setVideoListener(this);
        player.addListener(this);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, QHApplication.userAgent);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(videoUrl), dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        onLoadingStatus(CommonCode.General.DATA_LOADING);

    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.setPlayWhenReady(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        if (player != null) {
            player.setPlayWhenReady(false);
            player.clearVideoSurface();
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.d(TAG, "width = " + width + " / height = " + height);
        Log.d(TAG, "unapliedRotationDegrees = " + unappliedRotationDegrees + " / pixelWidthHeightRatio = " + pixelWidthHeightRatio);
        View view = playView.getVideoSurfaceView();
        if ((view instanceof TextureView) && unappliedRotationDegrees != 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(unappliedRotationDegrees, view.getWidth() / 2, view.getHeight() / 2);
            ((TextureView) view).setTransform(matrix);
        }
        ViewParent parent = view.getParent();
        if (parent != null && (parent instanceof AspectRatioFrameLayout)) {
            AspectRatioFrameLayout contentFrame = (AspectRatioFrameLayout) parent;
            if (contentFrame != null) {
                float aspectRatio = height == 0 ? 1 : (width * pixelWidthHeightRatio) / height;
                contentFrame.setAspectRatio(aspectRatio);
            }
        }

    }

    @Override
    public void onRenderedFirstFrame() {
        View view = playView.findViewById(R.id.exo_shutter);
        if (view != null)
            view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.d(TAG, "isLoading = " + isLoading);
        if (!isLoading) onLoadingStatus(CommonCode.General.DATA_SUCCESS);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d(TAG, "playWhenReady = " + playWhenReady + " / playbackState = " + playbackState);
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
        }
        View videoSurfaceView = playView.getVideoSurfaceView();
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Toasty.info(this, getString(R.string.play_video_error)).show();
    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
