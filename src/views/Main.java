package views;


import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.MusicPlayer;

public class Main extends Application{

	@Override
	public void start(Stage mainStage) throws Exception {
		try {
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainScene.fxml"));
			Parent root = (Parent)fxmlLoader.load();
			
			MusicPlayer musicPlayer = new MusicPlayer();
			
			MainController controller = fxmlLoader.<MainController>getController();
			controller.setMusicPlayer(musicPlayer);
			controller.setStage(mainStage);
			
			mainStage.setTitle("IMusic Player");
			mainStage.setScene(new Scene(root));
			mainStage.initStyle(StageStyle.TRANSPARENT);
			mainStage.setResizable(false);
			mainStage.show();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
