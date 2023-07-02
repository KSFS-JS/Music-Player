package controllers;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.MediaPlayer.Status;
import models.MusicPlayer;

public class SliderController {
	public void setVolume(ImageView volumeButton, Slider volumeSlider, MusicPlayer musicPlayer) {
		EventHandler<MouseEvent> volumeClick = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Parent pane = volumeButton.getParent();
				
				if (pane.getOpacity() == 1) {
					pane.setOpacity(0.7);
					
					volumeSlider.setVisible(true);
					volumeSlider.setDisable(false);
				}
				else {
					pane.setOpacity(1);
					
					volumeSlider.setVisible(false);
					volumeSlider.setDisable(true);
				}
			}
			
		};

		EventHandler<MouseEvent> sliderClick = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Status status = musicPlayer.getStatus();
				if (status == Status.PLAYING || status == Status.PAUSED || status == Status.READY) {
					musicPlayer.setVolume(volumeSlider.getValue());
				}
			}
			
		};
		
		EventHandler<ScrollEvent> sliderScroll = new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				double scrollAmount = event.getDeltaY();
				if (scrollAmount > 0) {
					volumeSlider.increment();
				}
				else {
					volumeSlider.decrement();
				}
				Status status = musicPlayer.getStatus();
				if (status == Status.PLAYING || status == Status.PAUSED || status == Status.READY) {
					musicPlayer.setVolume(volumeSlider.getValue());
				}
			}
			
		};
		
		volumeButton.setOnMouseClicked(volumeClick);
		volumeSlider.setOnMouseReleased(sliderClick);
		volumeSlider.setOnScroll(sliderScroll);
	}
}
