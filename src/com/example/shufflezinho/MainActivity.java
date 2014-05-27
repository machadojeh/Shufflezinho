package com.example.shufflezinho;

import java.util.ArrayList;
import java.util.Random;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ArrayList<Music> all_music;
	private int[] shuffled_idxs;
	private int music_index = 0;
	
	private MediaPlayer mediaPlayer;
	private SeekBar seekBar;
	
	private TextView musicName;
	private TextView musicArtist;
	private TextView musicAlbum;
	
	private boolean stopped = false;
	private boolean paused = false;
	
	private final Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(!stopped){

			musicName = (TextView) findViewById(R.id.textView1);
			musicArtist = (TextView) findViewById(R.id.textView2);
			musicAlbum = (TextView) findViewById(R.id.textView3);
			
			all_music = getAllSongsFromInternal();
			initShuffledIds();
			
			Music current_music = all_music.get(shuffled_idxs[music_index]);
			mediaPlayer = MediaPlayer.create(getBaseContext(),Uri.parse(current_music.getFullpath()));
			
	    	Music music = all_music.get(shuffled_idxs[music_index]);
	    	
			musicName.setText("Título: "+music.getName());
			musicArtist.setText("Artista: "+music.getArtist_name());
			musicAlbum.setText("Album: "+music.getAlbum_name());
					
			seekBar = (SeekBar) findViewById(R.id.SeekBar01);
	        seekBar.setMax(mediaPlayer.getDuration());
	        seekBar.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					seekChange(v);
					return false;
				}
			});
			
	        ImageButton shuffle = (ImageButton)findViewById(R.id.button1);
	        shuffle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					initShuffledIds();
				}
			});
	        
			ImageButton play = (ImageButton)findViewById(R.id.button4);
			play.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mediaPlayer.start();
					paused = false;
					startPlayProgressUpdater();
				}
			});
			
			ImageButton next = (ImageButton)findViewById(R.id.button3);
			next.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					music_index = ((music_index+1) % shuffled_idxs.length);
					
					mediaPlayer.stop();
					
					Music current_music = all_music.get(shuffled_idxs[music_index]);
					mediaPlayer = MediaPlayer.create(getBaseContext(),Uri.parse(current_music.getFullpath()));
			    	
					musicName.setText("Título: "+current_music.getName());
					musicArtist.setText("Artista: "+current_music.getArtist_name());
					musicAlbum.setText("Album: "+current_music.getAlbum_name());
					
					mediaPlayer.start();
					startPlayProgressUpdater();
				}
			});
			
			ImageButton previous = (ImageButton)findViewById(R.id.button5);
			previous.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					music_index = ((music_index-1) % shuffled_idxs.length);
					
					mediaPlayer.stop();
					
					Music current_music = all_music.get(shuffled_idxs[music_index]);
					mediaPlayer = MediaPlayer.create(getBaseContext(),Uri.parse(current_music.getFullpath()));
					
					musicName.setText("Título: "+current_music.getName());
					musicArtist.setText("Artista: "+current_music.getArtist_name());
					musicAlbum.setText("Album: "+current_music.getAlbum_name());
					
					mediaPlayer.start();
					startPlayProgressUpdater();
				}
			});
			
			ImageButton pause = (ImageButton)findViewById(R.id.button2);
			pause.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mediaPlayer.pause();
					paused = true;
				}
			});
			stopped = true;
		}
	}

	private void initShuffledIds() {
		
		Random generator = new Random();
		
		shuffled_idxs = new int[all_music.size()];
		
		for(int i=0;i<shuffled_idxs.length;i++){
			shuffled_idxs[i] = i;
		}
		
		for(int i=0;i<shuffled_idxs.length;i++){
			long idx1 = (generator.nextInt() + System.currentTimeMillis())%shuffled_idxs.length;
			long idx2 = (generator.nextInt() + System.currentTimeMillis())%shuffled_idxs.length;
			
			int aux_idx = shuffled_idxs[(int)idx1];
			shuffled_idxs[(int)idx1] = shuffled_idxs[(int)idx2];
			shuffled_idxs[(int)idx2] = aux_idx;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public ArrayList<Music> getAllSongsFromInternal() 
	{
		ArrayList<Music> array_musics = new ArrayList<Music>();
	    String[] STAR = { "*" };        
	    Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	    System.out.println("DIRETORIO: "+MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.getPath());

	    String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

	    Cursor cursor = managedQuery(allsongsuri, STAR, selection, null, null);

	    if (cursor != null) {
	        if (cursor.moveToFirst()) {
	            do {
	                String song_name = cursor
	                        .getString(cursor
	                                .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
	                int song_id = cursor.getInt(cursor
	                        .getColumnIndex(MediaStore.Audio.Media._ID));

	                String fullpath = cursor.getString(cursor
	                        .getColumnIndex(MediaStore.Audio.Media.DATA));

	                String album_name = cursor.getString(cursor
	                        .getColumnIndex(MediaStore.Audio.Media.ALBUM));
	                //int album_id = cursor.getInt(cursor
	                        //.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

	                String artist_name = cursor.getString(cursor
	                        .getColumnIndex(MediaStore.Audio.Media.ARTIST));
	                //int artist_id = cursor.getInt(cursor
	                        //.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));

	                array_musics.add(new Music(song_id, song_name, fullpath, album_name, artist_name));

	            } while (cursor.moveToNext());

	        }
	        //cursor.close();

	    }

        return array_musics;
	}
	
	// This is event handler thumb moving event
    private void seekChange(View v){
    	//if(mediaPlayer.isPlaying()){
	    	SeekBar sb = (SeekBar)v;
			mediaPlayer.seekTo(sb.getProgress());
		//}
    }
    
    public void startPlayProgressUpdater() {
    	
		if (mediaPlayer.isPlaying()) {
	    	seekBar.setProgress(mediaPlayer.getCurrentPosition());
	    	seekBar.setMax(mediaPlayer.getDuration());
	    	
			Runnable notification = new Runnable() {
		        public void run() {
		        	startPlayProgressUpdater();
				}
		    };
		    handler.postDelayed(notification,1000);
    	}
		else{
			if(!paused){
				music_index = ((music_index+1) % shuffled_idxs.length);
				
				mediaPlayer.stop();
				
				Music current_music = all_music.get(shuffled_idxs[music_index]);
				mediaPlayer = MediaPlayer.create(getBaseContext(),Uri.parse(current_music.getFullpath()));
	
				musicName.setText("Título: "+current_music.getName());
				musicArtist.setText("Artista: "+current_music.getArtist_name());
				musicAlbum.setText("Album: "+current_music.getAlbum_name());
				
				mediaPlayer.start();
				startPlayProgressUpdater();
			}
    	}
    }
}
