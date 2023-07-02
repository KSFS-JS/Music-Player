package controllers;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import models.MusicPlayer;

public class ListController {
	public void initPlayLists(MusicPlayer musicPlayer, ListView<String> playLists) {
		ArrayList<String> groups = musicPlayer.getPlayListGroup().getGroups();
		for(String song:groups) {
			playLists.getItems().add(song);
		}
	}
	
	public void deleteList(MusicPlayer musicPlayer, ListView<String> playLists, ContextMenu menu, Parent playListNode) {
		EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int index = playLists.getSelectionModel().getSelectedIndex();
				String group = playLists.getSelectionModel().getSelectedItem();
				
				try {
					musicPlayer.getPlayListGroup().removeGroup(group);
					playLists.getItems().remove(index);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		};
		
		menu.getItems().get(0).setOnAction(handler);
	}
}
