package com.sr.masharef.masharef.gallary;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.DialogToolbarInterface;
import com.sr.masharef.masharef.common.ImageLoader;

import java.io.IOException;

import static android.widget.FrameLayout.GONE;
import static android.widget.FrameLayout.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GalleryDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GalleryDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryDetailsFragment extends DialogFragment implements DialogToolbarInterface, OnCompletionListener, OnBufferingUpdateListener, OnTouchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView detailImage, detailAudioPlay, detailAudioPause;
    VideoView detailVideo;
    static String path;
    RelativeLayout detailAudio;
    MediaPlayer audioMedeia;
    Uri myUri;
    SeekBar detailSeekbar;
    private Handler mHandler = new Handler();
    ProgressDialog pDialog;
    ImageView backButtonImage;
    Button backButton;
    TextView title;
    static GalleryDetailsFragment fragment;
    View viewController;
    MediaController mediaController;
    private int lengthOfAudio;
    private String mTotalDuration, mElapsedTime;
    private int mediaFileLengthInMilliseconds;
    int passingPostion;
    private final Handler handler = new Handler();
    TextView textViewDuration;

    private OnFragmentInteractionListener mListener;

    public GalleryDetailsFragment() {
        // Required empty public constructor
    }

    public static GalleryDetailsFragment newInstance(String param1/*, String param2*/) {
        fragment = new GalleryDetailsFragment();
        Bundle args = new Bundle();
        path = param1;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AppTheme);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pDialog = new ProgressDialog(getActivity());
        View galleryDetailsView = inflater.inflate(R.layout.gallery_details, container, false);
        backButtonImage = (ImageView) galleryDetailsView.findViewById(R.id.cancel_image_view);
        backButton = (Button) galleryDetailsView.findViewById(R.id.cancel);
        title = (TextView) galleryDetailsView.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText("Detail");
        backButtonImage.setBackgroundResource(R.mipmap.ic_back);
        backButton.setVisibility(VISIBLE);
//        pDialog = new ProgressDialog(getActivity());
        mediaController = new MediaController(getActivity());
        audioMedeia = new MediaPlayer();
//        audioMedeia.setOnBufferingUpdateListener(this);
        audioMedeia.setOnCompletionListener(this);
        detailImage = (ImageView) galleryDetailsView.findViewById(R.id.details_image);
        detailVideo = (VideoView) galleryDetailsView.findViewById(R.id.details_video);
        detailAudio = (RelativeLayout) galleryDetailsView.findViewById(R.id.library_audio_layout);
        detailAudioPlay = (ImageView) galleryDetailsView.findViewById(R.id.audio_play_button);
        detailAudioPause = (ImageView) galleryDetailsView.findViewById(R.id.audio_pause_button);
        detailSeekbar = (SeekBar) galleryDetailsView.findViewById(R.id.audio_seekBar);
        detailSeekbar.setOnTouchListener(this);
        textViewDuration = (TextView) galleryDetailsView.findViewById(R.id.text_view_duration);
        viewController = (View) galleryDetailsView.findViewById(R.id.detail_controller_view);
        if (path.contains(".jpg")) {
            detailImage.setVisibility(VISIBLE);
            detailVideo.setVisibility(GONE);
            detailAudio.setVisibility(GONE);
            ImageLoader loader = new ImageLoader(getActivity(), FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            loader.loadImage(path, R.drawable.defaultimage, detailImage);
        } else {
            detailImage.setVisibility(GONE);
            detailAudio.setVisibility(VISIBLE);
            detailVideo.setVisibility(GONE);
            detailAudioPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!audioMedeia.isPlaying()) {
                        detailAudioPlay.setVisibility(View.GONE);
                        detailAudioPause.setVisibility(View.VISIBLE);
                        audioMedeia.start();
                        updateSeekProgress();
                        updateDuration();
                    }
                }
            });
            playAudio();
        }


        detailAudioPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                detailAudioPlay.setVisibility(View.VISIBLE);
                detailAudioPause.setVisibility(View.GONE);
                if (audioMedeia != null && audioMedeia.isPlaying()) {
                    audioMedeia.pause();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fragment.dismiss();
                pDialog.dismiss();
                if (detailVideo.isPlaying())
                    detailVideo.pause();

                if (audioMedeia.isPlaying()) {
                    audioMedeia.stop();
                    detailSeekbar.setProgress(0);
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    detailAudioPause.setVisibility(View.GONE);
                    detailAudioPlay.setVisibility(View.VISIBLE);
                }
            }
        });

        return galleryDetailsView;
    }

    private void playAudio() {
        try {
            detailAudioPlay.setVisibility(View.GONE);
            detailAudioPause.setVisibility(View.VISIBLE);
            path = path.replace(" ", "%20");
            myUri = Uri.parse(path);
            audioMedeia.setDataSource(path);
            audioMedeia.prepare();
            lengthOfAudio = audioMedeia.getDuration();
            mTotalDuration = getTimeString(lengthOfAudio);
            audioMedeia.start();
            updateSeekProgress();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        audioMedeia.setOnBufferingUpdateListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void updateSeekProgress() {
        if (audioMedeia.isPlaying()) {
            detailSeekbar.setProgress((int) (((float) audioMedeia.getCurrentPosition() / lengthOfAudio) * 100));
            updateDuration();
            handler.postDelayed(mUpdateTimeTask, 1000);
        }
    }

    void updateDuration() {
        mElapsedTime = getTimeString(audioMedeia.getCurrentPosition());
        textViewDuration.setText(mTotalDuration + "/" + mElapsedTime);
    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            updateSeekProgress();


        }
    };

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (audioMedeia.isPlaying()) {
            audioMedeia.stop();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        detailSeekbar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        detailAudioPlay.setVisibility(View.VISIBLE);
        detailAudioPause.setVisibility(View.GONE);
        detailSeekbar.setProgress(0);
        updateDuration();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (audioMedeia.isPlaying()) {
            SeekBar tmpSeekBar = (SeekBar) v;
            audioMedeia.seekTo((lengthOfAudio / 100) * tmpSeekBar.getProgress());
        }
        return false;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onDonePressed() {
    }

}
