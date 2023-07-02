package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.MusicPlayer;


public class MainController implements Initializable{
	 @FXML
    private ContextMenu playListsDelete;

    @FXML
    private ImageView previousButton;

    @FXML
    private Slider songSlider;

    @FXML
    private ImageView playStopButton;

    @FXML
    private Label songDuration;

    @FXML
    private ImageView volumeButton;

    @FXML
    private ListView<String> playLists;

    @FXML
    private Button addPlayListButton;

    @FXML
    private ImageView nextButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label songLabel;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label songStart;

    @FXML
    private ImageView imageBlock;

    @FXML
    private Pane playListPane;

    @FXML
    private ImageView modeButton1;

    @FXML
    private Pane timePane;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;
    
    
    private MusicPlayer musicPlayer;
    private Stage stage;
    private Parent playListNode;
    private ClickableController clickableController;
    private ListController listController;
    private PlayListNodeController playListNodeController;
    private SliderController sliderController;
	private TimerController timerController;
    

    public void setMusicPlayer(MusicPlayer musicPlayer) {
    	this.musicPlayer = musicPlayer;
    	
    	// Set up controller for lists
    	this.listController.initPlayLists(this.musicPlayer, this.playLists);
    	this.listController.deleteList(musicPlayer, playLists, playListsDelete, playListNode);
    	
    	// Set up controller for clickables
		this.clickableController.setMySong(this.songLabel, this.playListPane, this.playListNode, this.musicPlayer, this.playListNodeController, this.playLists);
		this.clickableController.openStage(this.addPlayListButton, this.musicPlayer, this.playLists);
		this.clickableController.setPlayList(playLists, playListPane, playListNode, musicPlayer, playListNodeController);
		this.clickableController.setMode(musicPlayer, modeButton1);
		
		// Set up controller for sliders
		this.sliderController.setVolume(this.volumeButton, this.volumeSlider, this.musicPlayer);
		this.playListNodeController.setPlayStopButton(this.playStopButton, this.volumeSlider.getValue(), this.titleLabel, this.authorLabel, this.songDuration, this.songStart, this.songSlider, this.timerController);
		
		// Set up play function
		this.clickableController.setPlayFunction(musicPlayer, previousButton, playStopButton, nextButton, volumeSlider, playListNodeController, titleLabel, authorLabel, songDuration, timerController);
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    public void initPlayListNode() {
		// Init PlayListNode
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/PlayListNode.fxml"));
		try {
			this.playListNode = fxmlLoader.load();
			this.playListNodeController = fxmlLoader.<PlayListNodeController>getController();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void stylizeContextMenu() {
    	this.playListsDelete.setStyle("-fx-background: #303030");
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		this.initPlayListNode();
		
		// Obtain button controller initializer
		this.clickableController = new ClickableController();
		clickableController.initExitButton(this.exitButton);
		
		this.listController = new ListController();
		
		this.stylizeContextMenu();
		this.playLists.setContextMenu(playListsDelete);
		
		this.sliderController = new SliderController();
		this.timerController = new TimerController(songSlider, songStart);
	}
	
	@FXML
	public void exit(ActionEvent event) {
		this.stage.close();
	}
}
