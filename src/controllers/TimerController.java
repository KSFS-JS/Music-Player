package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.Duration;

public class TimerController {
	private Timeline timeline;
	private int currTime;
	private int totalTime;
	private Slider songSlider;
	private Label songStart;
	
	public TimerController(Slider songSlider, Label songStart) {
		this.timeline = new Timeline();
		this.songSlider = songSlider;
		this.songStart = songStart;
	}
	
	public String formattedTime() {
		return String.format("%02d:%02d:%02d", currTime / 3600, (currTime % 3600) / 60, (currTime % 60));
	}
	
	public void update() {
		this.currTime ++;
		double progress = (double)currTime / (double)totalTime;
		this.songSlider.setValue(progress*100);
		this.songStart.setText(this.formattedTime());
	}
	
	public void start(int totalTime) {
		this.initialize();
		
		this.totalTime = totalTime;
		this.timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> update()));
		this.timeline.setCycleCount(totalTime);
		
		this.timeline.play();
	}
	
	public void initialize() {
		this.timeline.stop();
		this.timeline.getKeyFrames().clear();
		this.timeline.setCycleCount(0);
		this.songSlider.setValue(0);
		this.songStart.setText("00:00:00");
		this.currTime = 0;
	}
	
	public void pause() {
		this.timeline.pause();
	}
	
	public void resume() {
		this.timeline.play();
	}
}
