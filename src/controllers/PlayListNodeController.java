package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.InfoExtractor;
import models.MusicPlayer;
import models.Song;

public class PlayListNodeController implements Initializable{

	@FXML
    private TableView<Song> songTable;

    @FXML
    private Label nameLabel;
    
    @FXML
    private TableColumn<Song, String> titleCol;
    
    @FXML
    private TableColumn<Song, String> artistCol;

    @FXML
    private TableColumn<Song, String> durationCol;
    
    @FXML
    private ImageView addSong;
    
    @FXML
    private ImageView tablePlay;
    
    @FXML
    private ContextMenu menu;
    
    private MusicPlayer musicPlayer;
    private String currPlayList;
    private int currGroupIndex;
    private ImageView playStopButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		this.titleCol.setStyle("-fx-background-color: #303030; -fx-border-color: #666666; -fx-text-fill:#FFFFFF");
		this.artistCol.setStyle("-fx-background-color: #303030; -fx-border-color: #666666; -fx-text-fill:#FFFFFF");
		this.durationCol.setStyle("-fx-background-color: #303030; -fx-border-color: #666666; -fx-text-fill:#FFFFFF");
		this.titleCol.setReorderable(false);
		this.artistCol.setReorderable(false);
		this.durationCol.setReorderable(false);
		
		this.titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		this.artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
		this.durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
		
		menu.setStyle("-fx-background-color:#303030");
		this.songTable.setContextMenu(menu);
		
		this.initAddSongButton();
	}
	
	public void setMusicPlayer(MusicPlayer musicPlayer) {
		this.musicPlayer = musicPlayer;
	}
	
	public void setNameLabel(String name) {
		this.nameLabel.setText(name);
		this.currPlayList = name;
	}
	
	public void setPlayStopButton(ImageView playStopButton, double volume, Label titleLabel, Label authorLabel, Label songDuration, Label songStart, Slider songSlider, TimerController timerController) {
		this.playStopButton = playStopButton;
		this.initTablePlayButton(volume, titleLabel, authorLabel, songDuration, songStart, songSlider, timerController);
		this.initClickPlay(volume, titleLabel, authorLabel, songDuration, songStart, songSlider, timerController);
	}
	
	public void setTable(InfoExtractor extractor) {
		// Clear previous selection
		this.songTable.getSelectionModel().clearSelection();
		
		this.currGroupIndex = 0;
		this.songTable.getItems().clear();
		for (String file:extractor.getRawSongNames()) {
			String title = extractor.getTitle(file);
			String artist = extractor.getArtist(file);
			String duration = extractor.getLength(file);
			String path = extractor.getPath(file);
			String rawName = extractor.getRawName(file);
			this.songTable.getItems().add(new Song(title, artist, duration, path, rawName));
		}
	}
	
	public void setTable(InfoExtractor extractor, ArrayList<String> songs) {
		// Clear previous selection
		this.songTable.getSelectionModel().clearSelection();
		
		this.currGroupIndex = 1;
		this.songTable.getItems().clear();
		for (String file:songs) {
			String title = extractor.getTitle(file);
			String artist = extractor.getArtist(file);
			String duration = extractor.getLength(file);
			String path = extractor.getPath(file);
			String rawName = extractor.getRawName(file);
			this.songTable.getItems().add(new Song(title, artist, duration, path, rawName));
		}
	}
	
	public void updateTable() {
		if (currGroupIndex == 0) {
			setTable(musicPlayer.getInfoExtractor());
		}
		else {
			setTable(musicPlayer.getInfoExtractor(), musicPlayer.getPlayListGroup().getSongs(currPlayList));
		}
	}
	
	public void initAddSongButton() {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Add new songs");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac", "*.mp4"));
				List<File> selectedFile = fileChooser.showOpenMultipleDialog(new Stage());
				
				// Obtain file dir and songs existed
				File songDir = new File("songs");
				ArrayList<String> existedSongs = musicPlayer.getPlayListGroup().getSongs(currPlayList);
				// Obtain set from ArrayList for fast checking
				Set<String> songSet = new HashSet<String>();
				for (String name:existedSongs) {
					songSet.add(name);
				}
				
				ArrayList<String> songNames = new ArrayList<String>();
				
				if (selectedFile != null) {
				    for (File file: selectedFile) {
				    	// Only manipulate songs not existed
				    	String toAdd = file.getName();
				    	if (!songSet.contains(toAdd)) {
			    			if (!songDir.getAbsolutePath().equalsIgnoreCase(file.getParent())) {
			    				try {
									Files.copy(file.toPath(), songDir.toPath());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			    			}
							songNames.add(toAdd);
				    	}
				    }
				    
				    try {
				    	// Update group info
						musicPlayer.getPlayListGroup().addSongs(currPlayList, songNames);
						// Update Table View
						updateTable();
						// Update Play List
						musicPlayer.setPlayList(musicPlayer.getPlayListGroup().getSongs(currPlayList));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
				 }
			}
			
		};
		// Hook up
		addSong.setOnMouseClicked(handler);
	}
	
	public void initTablePlayButton(double volume, Label titleLabel, Label authorLabel, Label songDuration, Label songStart, Slider songSlider, TimerController timerController) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				musicPlayer.play(null, titleLabel, authorLabel, songDuration);
				musicPlayer.setVolume(volume);
				timerController.start(musicPlayer.getUnformattedDuration());
				
				File image = new File("src/assets/stopButton01.png");
				playStopButton.setImage(new Image(image.toURI().toString()));		
			}
		};
		
		tablePlay.setOnMouseClicked(handler);
	}
	
	public void initClickPlay(double volume, Label titleLabel, Label authorLabel, Label songDuration, Label songStart, Slider songSlider, TimerController timerController) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() >= 2) {
					String songName = songTable.getSelectionModel().getSelectedItem().getRawName();
					musicPlayer.play(songName, titleLabel, authorLabel, songDuration);
					musicPlayer.setVolume(volume);
					timerController.start(musicPlayer.getUnformattedDuration());
					
					File image = new File("src/assets/stopButton01.png");
					playStopButton.setImage(new Image(image.toURI().toString()));
				}
				
			}
			
		};
		
		songTable.setOnMouseClicked(handler);
	}
	
	public String getCurrentSongName() {
		String selected = musicPlayer.getCurrentRawName();
		if (!(selected == null)){
			return selected;
		}
		else {
			return null;
		}
	}
	
	public void initDeleteSong() throws IOException {
		EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Song selected = songTable.getSelectionModel().getSelectedItem();
				if (selected != null) {
					try {
						musicPlayer.deleteSong(currPlayList, selected.getRawName());
						updateTable();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		};
		
		this.menu.getItems().get(0).setOnAction(handler);
	}
}
