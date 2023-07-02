package models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class InfoExtractor {
	private HashMap<String, HashMap<String, String>> songInfo;
	private ArrayList<String> rawSongNames;

	public InfoExtractor(String dir) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		// Obtain song list
		File folder = new File(dir);
		File[] songs = folder.listFiles();
		this.rawSongNames = new ArrayList<String>();
		
		HashMap<String, HashMap<String, String>> songInfo = new HashMap<String, HashMap<String, String>>();
		for(int i = 0; i < songs.length; i++) {
			// Obtain file and tag
			File file = new File(songs[i].getPath());
			AudioFile songFile = AudioFileIO.read(file);
			Tag songTag = songFile.getTag();
			
			// Obtain Info required
			String artist = songTag.getFirst(FieldKey.ARTIST);
			String title = songTag.getFirst(FieldKey.TITLE);
			String length = String.valueOf(songFile.getAudioHeader().getTrackLength());
			String fileName = file.getName();
			
			// Store info in map
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("artist", artist);
			map.put("title", title);
			map.put("length", length);
			map.put("path", file.toURI().toString());
			map.put("rawName", file.getName());
			rawSongNames.add(file.getName());
			// FileName as key and all other info as value
			songInfo.put(fileName, map);
		}
		// Attribute construction
		this.songInfo = songInfo;
	}
	
	public String getArtist(String fileName) {
		return this.songInfo.get(fileName).get("artist");
	}
	
	public String getLength(String fileName) {
		return this.songInfo.get(fileName).get("length");
	}
	
	public String getTitle(String fileName) {
		return this.songInfo.get(fileName).get("title");
	}
	
	public String getPath(String fileName) {
		return this.songInfo.get(fileName).get("path");
	}
	
	public String getRawName(String fileName) {
		return this.songInfo.get(fileName).get("rawName");
	}
	
	public Set<String> getFiles() {
		return this.songInfo.keySet();
	}
	
	public ArrayList<String> getRawSongNames() {
		return this.rawSongNames;
	}
}
