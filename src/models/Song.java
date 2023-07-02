package models;

public class Song {
	private String title;
	private String artist;
	private String duration;
	private String path;
	private String rawName;
	
	public Song(String title, String artist, String duration, String path, String rawName) {
		this.title = title;
		this.artist = artist;
		this.duration = duration;
		this.path = path;
		this.rawName = rawName;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public String getDuration() {
		int s = Integer.valueOf(this.duration);
		String formatDuration = String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
		return formatDuration;
	}
	
	public String getOrigDuration() {
		return this.duration;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getRawName() {
		return this.rawName;
	}
}
