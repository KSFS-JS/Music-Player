package models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlayListGroup {
	private JSONObject groups;
	private File listFile;

	public PlayListGroup() throws IOException {
		// Check existence
		File playLists = new File("PlayLists.json");
		if (!playLists.exists()) {
			playLists.createNewFile();
			Files.writeString(playLists.toPath(), "{}");
		}
		
		// Parse json file into song groups
		String pre_json = Files.readString(playLists.toPath());
		JSONObject groups = new JSONObject(pre_json);
		
		// Construct attributes
		this.listFile = playLists;
		this.groups = groups;
		
	}
	
	public void writeToFile() throws IOException {
		Files.writeString(this.listFile.toPath(), this.groups.toString());
	}
	
	public void addGroup(String group) throws IOException {
		/*
		 * Add a new music group into PlayListGroup
		 */
		if (!this.groups.has(group)) {
			// If group not present, just add an empty list
			this.groups.put(group, new ArrayList<String>());
			
			this.writeToFile();
		}
		// If existed, just do nothing
	}
	
	public void addSongs(String group, ArrayList<String> titles) throws IOException {
		/*
		 * Add a list of songs into given group, 
		 */
		if (titles.size() == 0) {
			return;
		}
		ArrayList<String> nonRepeated = new ArrayList<String>();
		
		// Obtain existing songs
		JSONArray songArray = (JSONArray) this.groups.get(group);
		List<Object> songList = songArray.toList();
		
		// Check repeat
		for (int i=0; i < titles.size(); i++) {
			String song = titles.get(i);
			if (!songList.contains(song)) {
				nonRepeated.add(song);
			}
		}
		// Add all Non repeat songs
		songList.addAll(nonRepeated);
		
		this.groups.put(group, songList);

		this.writeToFile();
	}
	
	public void removeGroup(String group) throws IOException {
		/*
		 * Remove given group from group lists
		 */
		if (this.groups.has(group)) {
			this.groups.remove(group);
			this.writeToFile();
		}
	}
	
	public void removeSong(String group, String song) throws IOException {
		// Obtain existing songs
		JSONArray songArray = (JSONArray) this.groups.get(group);
		ArrayList<Object> songList = (ArrayList<Object>) songArray.toList();
		songList.remove(song);
		
		// Rebuild group
		this.groups.put(group, songList);
		
		this.writeToFile();
	}
	
	public void removeSongs(String group, ArrayList<String> titles) throws IOException {
		/*
		 * Remove selected songs from given group
		 */
		if (titles.size() == 0) {
			return;
		}
		ArrayList<String> nonSelected = new ArrayList<String>();
		
		// Obtain existing songs
		JSONArray songArray = (JSONArray) this.groups.get(group);
		List<Object> songList = songArray.toList();
		
		// Check remaining
		for (int i=0; i < songList.size(); i++) {
			String song = (String) songList.get(i);
			if (!titles.contains(song)) {
				nonSelected.add(song);
			}
		}
		
		// Rebuild group
		this.groups.put(group, nonSelected);
		
		this.writeToFile();
	}
	
	public ArrayList<String> getGroups(){
		ArrayList<String> groups = new ArrayList<String>();
		
		Iterator<String> iter = this.groups.keys();
		// Iterate through all groups and obtain names
		while (iter.hasNext()) {
			groups.add(iter.next());
		}
		
		return groups;
	}
	
	public ArrayList<String> getSongs(String group){
		// Obtain lists
		JSONArray songArray = (JSONArray) this.groups.get(group);
		List<Object> songList = songArray.toList();
		
		ArrayList<String> songs = new ArrayList<String>();
		for (Object o: songList) {
			songs.add(o.toString());
		}
		return songs;
	}
}
