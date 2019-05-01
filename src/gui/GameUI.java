package gui;

import app.MainApp;
import constants.FontHolder;
import constants.ImageHolder;
import constants.SystemCache;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GameUI extends BorderPane{
	private Label scoreLabel;
	private Label cashLabel;
	private ImageView cashIcon;
	
	public GameUI() {
		SystemCache.getInstance().gameUI = this;
		
		this.setMouseTransparent(true);
		this.setWidth(MainApp.WINDOW_WIDTH);
		this.setHeight(MainApp.WINDOW_HEIGHT);
		this.setPadding(new Insets(10));
		
		scoreLabel = new Label("Score 000000");
		cashLabel = new Label("0");
		cashIcon = new ImageView(ImageHolder.getInstance().coinPile1);
		
		scoreLabel.setFont(FontHolder.getInstance().font36);
		cashLabel.setFont(FontHolder.getInstance().font36);
		scoreLabel.setTextFill(Color.WHITE);
		cashLabel.setTextFill(Color.WHITE);
		BorderPane top = new BorderPane();
		top.setLeft(scoreLabel);
		top.setRight(new Button("Enermy Info"));
		
		BorderPane bottom = new BorderPane();
		HBox hbox = new HBox(cashIcon, cashLabel);
		hbox.setSpacing(15);
		hbox.setAlignment(Pos.CENTER_LEFT);
		bottom.setLeft(hbox);
		bottom.setRight(new Button("Weapon Info"));

		this.setTop(top);
		this.setBottom(bottom);
		
	}
	
	public void setScore(int score) {
		scoreLabel.setText(String.format("Score 06%d", score));
	}
	
	public void setCash(int cash) {
		cashLabel.setText(String.format("%d", cash));
	}
}
