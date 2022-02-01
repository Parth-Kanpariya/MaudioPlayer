package com.example.maudioplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayVideo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayVideo extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlayerView videoView;
    private BottomNavigationView bottomNavigationView;
    private TextView VideoTitle;
    private ImageView playPause,Back,nextButton,prevButton;
   private Player player;
   private Video video;
   private int Position;
   private int SizeofList;
    private String PathOfVideo=null;
   private Uri originalUri=null;
    private String VideoTitleName=null;
    private ArrayList<Video>videoArrayList;

    public PlayVideo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayVideo.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayVideo newInstance(String param1, String param2) {
        PlayVideo fragment = new PlayVideo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_play_video, container, false);

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);


        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        playPause=rootView.findViewById(R.id.playPauseButton);
        Back=rootView.findViewById(R.id.back_button);
        nextButton=rootView.findViewById(R.id.next_Button);
        prevButton=rootView.findViewById(R.id.previous_button);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPlaying())
                {
                    pauseVideo();
                }else
                {
                    playVideo();
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigateUp();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPrevVideo(true);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPrevVideo(false);
            }
        });









        if(getArguments()!=null)
        {
            video= (Video) getArguments().getSerializable("ObjectOfVideo");
            Position=getArguments().getInt("Position");
            SizeofList=getArguments().getInt("SizeOfList");
            videoArrayList= (ArrayList<Video>) getArguments().getSerializable("VideoList");
            PathOfVideo=getArguments().getString("PATHOFVIDEO");
            originalUri=Uri.parse(getArguments().getString("uri"));
            VideoTitleName=getArguments().getString("VideoTitle");

        }
        VideoTitle=rootView.findViewById(R.id.VideoTitleInVideo);
        VideoTitle.setSelected(true);
        VideoTitle.setText(VideoTitleName);

//        Toast.makeText(getContext(),""+originalUri,Toast.LENGTH_SHORT).show();
        videoView=rootView.findViewById(R.id.exoPlayerView);
//        Toast.makeText(getContext(), ""+PathOfVideo, Toast.LENGTH_SHORT).show();


        createPlayer();


        //specify the location of media file





        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else
        {

        }



        //Setting MediaController and URI, then starting the videoView




        return rootView;
    }

    private void createPlayer()
    {
        player= new SimpleExoPlayer.Builder(getContext()).build();
        videoView.setPlayer(player);
        MediaItem mediaItem=MediaItem.fromUri(Uri.parse(videoArrayList.get(Position).getPath()));
        player.setMediaItem(mediaItem);
        player.prepare();
        playVideo();
    }

    private void playVideo()
    {
        playPause.setImageResource(R.drawable.ic_baseline_pause_24);
        player.play();
    }

    private void pauseVideo()
    {
        playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        player.pause();
    }

    private void nextPrevVideo(boolean isNext)
    {
        player.release();
        if(isNext)
        {

            setPosition(true);

        }
        else
        {

            setPosition(false);

        }
        createPlayer();
    }

    private void setPosition(boolean isIncrement)
    {
        if(!isIncrement)
        {
            if(SizeofList-1==Position)
            {
                Position=0;
            }else
            {
                Position++;
            }
        }else
        {
            if(Position==0)
            {
                Position=SizeofList-1;
            }else
            {
                Position-- ;
            }
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }
}