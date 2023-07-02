package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import models.MusicPlayer;
import views.AddGroupWindow;

public class ClickableController {
	public void initExitButton(Button exitButton){
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				exitButton.setText("X");
				exitButton.setOpacity(0.7);
			}
		};
		
		EventHandler<MouseEvent> handler2 = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				exitButton.setText(null);
				exitButton.setOpacity(1);
			}
		};
		
		exitButton.setOnMouseEntered(handler);
		exitButton.setOnMouseExited(handler2);
	}
	
	public void setMySong(Label songLabel, Pane playListPane, Parent playListNode, MusicPlayer musicPlayer, PlayListNodeController playListNodeController, ListView<String> playLists) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// clear previous selection
				playLists.getSelectionModel().clearSelection();
				
				playListNodeController.setMusicPlayer(musicPlayer);
				playListNodeController.setNameLabel("My Song");
				playListNodeController.setTable(musicPlayer.getInfoExtractor());
				
				musicPlayer.setPlayList(musicPlayer.getInfoExtractor().getRawSongNames());
				
				if (playListPane.getChildren().size() == 0) {
					playListPane.getChildren().add(playListNode);
				}
				else {
					playListPane.getChildren().set(0, playListNode);
				}
			}
			
		};
		
		songLabel.setOnMouseClicked(handler);
	}
	
	public void openStage(Button addPlayListButton, MusicPlayer musicPlayer, ListView<String> playLists) {
		EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				AddGroupWindow addGroupWindow = new AddGroupWindow(musicPlayer, playLists);
				try {
					addGroupWindow.start(new Stage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		addPlayListButton.setOnAction(handler);
	}
	
	public void setPlayList(ListView<String> playLists, Pane playListPane, Parent playListNode, MusicPlayer musicPlayer, PlayListNodeController playListNodeController) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// Pass musicPlayer to playListNode
				playListNodeController.setMusicPlayer(musicPlayer);
				// Obtain songs stored
				String group = playLists.getSelectionModel().getSelectedItem();
				playListNodeController.setNameLabel(group);
				ArrayList<String> songs = musicPlayer.getPlayListGroup().getSongs(group);
				// Update PlayList in musicPlayer
				playListNodeController.setTable(musicPlayer.getInfoExtractor(), songs);
				musicPlayer.setPlayList(songs);
				if (playListPane.getChildren().size() == 0) {
					playListPane.getChildren().add(playListNode);
				}
				else {
					playListPane.getChildren().set(0, playListNode);
				}
				try {
					playListNodeController.initDeleteSong();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		
		playLists.setOnMouseClicked(handler);
	}
	
	public void setMode(MusicPlayer musicPlayer, ImageView modeButton1) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Parent container = modeButton1.getParent();
				if (container.getOpacity() != 1) {
					container.setOpacity(1);
				}
				else {
					container.setOpacity(0.5);
				}
				
				musicPlayer.setMode();
			}
			
		};
		
		modeButton1.setOnMouseClicked(handler);
	}
	
	public void setPlayFunction(MusicPlayer musicPlayer, ImageView previousButton, ImageView playStopButton,
			ImageView nextButton, Slider volumeSlider, PlayListNodeController playListNodeController, Label titleLabel, Label authorLabel, Label songDuration, TimerController timerController) {
		EventHandler<MouseEvent> playStopHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (musicPlayer.getStatus() == Status.PLAYING) {
					musicPlayer.pause();
					timerController.pause();
					
					File image = new File("src/assets/playButton01.png");
					playStopButton.setImage(new Image(image.toURI().toString()));
				}
				else if (musicPlayer.getStatus() == Status.PAUSED){
					musicPlayer.resume();
					timerController.resume();
					
					File image = new File("src/assets/stopButton01.png");
					playStopButton.setImage(new Image(image.toURI().toString()));
				}
				else {
					String song = playListNodeController.getCurrentSongName();
					if (!(song == null)) {
						musicPlayer.play(song, titleLabel, authorLabel, songDuration);
						musicPlayer.setVolume(volumeSlider.getValue());
						File image = new File("src/assets/stopButton01.png");
						playStopButton.setImage(new Image(image.toURI().toString()));
					}
				}
			}
			
		};
		
		EventHandler<MouseEvent> previousHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				String song = playListNodeController.getCurrentSongName();
				if (!(song == null)) {
					musicPlayer.previous(titleLabel, authorLabel, songDuration);
					musicPlayer.setVolume(volumeSlider.getValue());
					timerController.start(musicPlayer.getUnformattedDuration());
				}
			}
			
		};
		
		EventHandler<MouseEvent> nextHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				String song = playListNodeController.getCurrentSongName();
				if (!(song == null)) {
					musicPlayer.next(titleLabel, authorLabel, songDuration);
					musicPlayer.setVolume(volumeSlider.getValue());
					timerController.start(musicPlayer.getUnformattedDuration());
				}
			}
			
		};
		
		previousButton.setOnMouseClicked(previousHandler);
		nextButton.setOnMouseClicked(nextHandler);
		playStopButton.setOnMouseClicked(playStopHandler);
	}
}
