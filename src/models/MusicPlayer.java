package models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class MusicPlayer{
	private InfoExtractor extractor;
	private PlayListGroup playListGroup;
	private String mode;
	private ArrayList<String> playList;
	private int songIndex;
	private List<Integer> playIndex;
	private Media source;
	private MediaPlayer song;

	public MusicPlayer() throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		// Check if song folder exists
		File songFolder = new File("Songs");
		if (!songFolder.exists()) {
			songFolder.mkdir();
		}
		
		this.extractor = new InfoExtractor(songFolder.getPath());
		this.playListGroup = new PlayListGroup();
		this.mode = "normal";
		// play list for songs
		this.playList = null;
		// curr song index
		this.songIndex = 0;
		// play order
		this.playIndex = null;
		// Audio processor
		this.source = null;
		this.song = null;
	}
	
	public PlayListGroup getPlayListGroup() {
		return this.playListGroup;
	}
	
	public InfoExtractor getInfoExtractor() {
		return this.extractor;
	}
	
	public void setPlayList(ArrayList<String> list) {
		this.playList = list;
	}
	
	public void initPlayIndex() {
		this.playIndex = new ArrayList<Integer>();
		for (int i=0;i < this.playList.size();i++) {
			this.playIndex.add(i);
		}
		Collections.shuffle(this.playIndex);
	}
	
	public void initSong(String path, Label titleLabel, Label authorLabel, Label songDuration) {
		this.source = new Media(path);
		this.song = new MediaPlayer(this.source);
		
		// Set up autoplay
		this.song.setOnStopped(() -> this.next(titleLabel, authorLabel, songDuration));
		
		this.song.play();
		titleLabel.setText(this.getCurrentTitle());
		authorLabel.setText(this.getCurrentAuthor());
		songDuration.setText(this.getDuration());
	}
	
	public void play(String songName, Label titleLabel, Label authorLabel, Label songDuration) {
		// If paused before but resume same song
		if (this.song != null && this.song.getStatus() == MediaPlayer.Status.PAUSED && this.playList.get(this.songIndex) == songName) {
			this.song.play();
		}
		// If not paused or stopped
		else if(this.playList.size() > 0){
			this.initPlayIndex();
			String path;
			if(songName == null) {
				if(this.mode == "normal") {
					this.songIndex = 0;
				}
				else {
					this.songIndex = this.playIndex.indexOf(this.songIndex);
				}
				path = this.extractor.getPath(this.playList.get(this.songIndex));
			}
			else {
				// Set songIndex to selected song
				this.songIndex = this.playList.indexOf(songName);
				path = this.extractor.getPath(songName);
			}
			// If current playing or paused but selected another song, then re-init handler
			if(this.song != null && (this.song.getStatus() == Status.PLAYING || this.song.getStatus() == Status.PAUSED)) {
				this.song.setOnStopped(null);
				this.song.stop();
			}
			this.initSong(path, titleLabel, authorLabel, songDuration);
		}
	}
	
	public void next(Label titleLabel, Label authorLabel, Label songDuration) {
		if (this.playList.size() > 0) {
			// Detach handler
			this.song.setOnStopped(null);
			this.song.stop();
			String path;
			
			// Increment index
			this.songIndex ++;
			if (this.songIndex >= this.playList.size()) {
				this.songIndex = 0;
				// If random mode and reach end of playIndex, shuffle and start new random sequence
				if (this.mode == "random") {
					this.initPlayIndex();
				}
			}
			
			if(this.mode == "normal") {
				// If normal, just use the index
				path = this.extractor.getPath(this.playList.get(this.songIndex));
			}
			else {
				// If random, just 
				path = this.extractor.getPath(this.playList.get(this.playIndex.get(this.songIndex)));
			}
			this.initSong(path, titleLabel, authorLabel, songDuration);
		}
	}
	
	public void previous(Label titleLabel, Label authorLabel, Label songDuration) {
		if (this.playList.size() > 0) {
			// Detach Handler
			this.song.setOnStopped(null);
			this.song.stop();
			
			String path;
			
			// Increment index
			this.songIndex --;
			if (this.songIndex < 0) {
				this.songIndex = this.playList.size() - 1;
				// If random mode and reach end of playIndex, shuffle and start new random sequence
				if (this.mode == "random") {
					this.initPlayIndex();
				}
			}
			
			if(this.mode == "normal") {
				// If normal, just use the index
				path = this.extractor.getPath(this.playList.get(this.songIndex));
			}
			else {
				// If random, just 
				path = this.extractor.getPath(this.playList.get(this.playIndex.get(this.songIndex)));
			}
			
			this.initSong(path, titleLabel, authorLabel, songDuration);
		}
	}
	
	public void pause() {
		this.song.pause();
	}
	
	public void resume() {
		this.song.play();
	}
	
	public void setVolume(double vol) {
		if (this.song != null) {
			this.song.setVolume(vol);
		}
	}
	
	public void setMode() {
		if (this.mode == "normal") {
			this.mode = "random";
		}
		else {
			this.mode = "normal";
		}
	}
	
	public void setProgress(Double percentage) {
		Status status = this.song.getStatus();
		if (this.song != null && (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.PLAYING)) {
			Double totalTime = this.song.getTotalDuration().toMillis();
			Duration toSeek = new Duration(totalTime * percentage);
			
			this.song.seek(toSeek);
		}
	}
	
	public Status getStatus() {
		if (this.song == null) {
			return Status.UNKNOWN;
		}
		else {
			return this.song.getStatus();
		}
	}
	
	public String getCurrentTitle() {
		return this.extractor.getTitle(this.playList.get(this.songIndex));
	}
	
	public String getCurrentAuthor() {
		return this.extractor.getArtist(this.playList.get(this.songIndex));
	}
	
	public String getDuration() {
		int s = Integer.valueOf(this.extractor.getLength(this.playList.get(this.songIndex)));
		String formatDuration = String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
		return formatDuration;
	}
	
	public int getUnformattedDuration() {
		return Integer.valueOf(this.extractor.getLength(this.playList.get(this.songIndex)));
	}
	
	public String getCurrentRawName() {
		if (this.song != null) {
			return this.extractor.getRawName(this.playList.get(this.songIndex));
		}
		else {
			return null;
		}
	}
	
	public void deleteSong(String group, String song) throws IOException {
		this.playList.remove(song);
		this.playListGroup.removeSong(group, song);
	}
}
