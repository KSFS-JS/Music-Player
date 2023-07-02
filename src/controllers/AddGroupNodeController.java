package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.MusicPlayer;

public class AddGroupNodeController implements Initializable{
	@FXML
    private Button cancelButton;

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;
    
    private Stage secondaryStage;
    private MusicPlayer musicPlayer;
	private ListView<String> playLists;
    
    public void setStage(Stage secondaryStage) {
    	this.secondaryStage = secondaryStage;
    }
    
    public void setMusicPlayer(MusicPlayer musicPlayer) {
    	this.musicPlayer = musicPlayer;
    }
    
    public void setPlayLists(ListView<String> playLists) {
    	this.playLists = playLists;
    }

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		EventHandler<ActionEvent> saveHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String text = nameField.getText();
				if (text != "" || text != null) {
					try {
						musicPlayer.getPlayListGroup().addGroup(text);
						playLists.getItems().add(text);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				secondaryStage.close();
			}
		};
		
		EventHandler<ActionEvent> closeHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				secondaryStage.close();
			}
		};
		
		EventHandler<KeyEvent> enterHandler = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					saveButton.fire();
				}
			}
		};
		
		saveButton.setOnAction(saveHandler);
		cancelButton.setOnAction(closeHandler);
		nameField.setOnKeyPressed(enterHandler);
		
	}
}
