package views;

import controllers.AddGroupNodeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.MusicPlayer;

public class AddGroupWindow extends Application{
	private MusicPlayer musicPlayer;
	private ListView<String> playLists;
	
	public AddGroupWindow(MusicPlayer musicPlayer, ListView<String> playLists) {
		this.musicPlayer = musicPlayer;
		this.playLists = playLists;
	}
	
	@Override
	public void start(Stage secondaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/AddGroupNode.fxml"));
		Parent root = (Parent)fxmlLoader.load();
		
		AddGroupNodeController controller = fxmlLoader.<AddGroupNodeController>getController();
		controller.setStage(secondaryStage);
		controller.setMusicPlayer(musicPlayer);
		controller.setPlayLists(playLists);
		
		secondaryStage.setTitle("Please Enter Group Name");
		secondaryStage.setScene(new Scene(root));
		secondaryStage.initStyle(StageStyle.TRANSPARENT);
		secondaryStage.setResizable(false);
		secondaryStage.show();
		
		
	}

}
