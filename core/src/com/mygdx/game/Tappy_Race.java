// Simple Android game Written by Patrick Ekel
// Images taken from openclipart.org

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Class that uses libGDX for graphics rendering
 * Draws a car that accelerates down the track based on number of user clicks
 */
public class Tappy_Race extends ApplicationAdapter {
	private Batch batch;
	private BitmapFont font;
	private Texture background, finish, orangeCar, blueCar, purpleCar, yellowCar, greenCar, hearseCar, carHolder, blackBox, start, play, quit;
	private Texture[] lights = new Texture[3];
	private Texture redLight, yellowLight, greenLight;
	private long countDownTime,raceStartTime;
	private float raceTime;
	private boolean gameStarted;
	private int i;
	private int buttonSpacer;
	private int gameState;
	private boolean inPreRace, raceComplete;
	private float carY, deltaY, drag, velocity;
	private float finishLine, finishDistance;
	private float width, height, carWidth, carHeight;
	private Actor noCarSelected;
	private Stage stage;
	private Skin buttonSkin, yellowSkin, orangeSkin, blueSkin, greenSkin, purpleSkin;
	private Actor backGroundSkin;
	private Batch backBatch;
	private TextButton orangeCarButton, blueCarButton, yellowCarButton, greenCarButton, purpleCarButton, newGameButton, playButton, quitButton;

	/**
	 * Instantiate variables
	 */
	@Override
	public void create() {
		inPreRace = false;
 		gameStarted = false;
		raceComplete = false;
		batch = new SpriteBatch();
		font = new BitmapFont();font.setColor(Color.BLACK); font.getData().setScale(5);
		carWidth = 0;
		gameState = 0;
		drag = 0;
		background = new Texture("road_into.jpg");
		finish = new Texture("finish_removed.jpg");
		orangeCar = new Texture("orangeSportCar.jpg");
		greenCar = new Texture("greenCar.png");
		blueCar = new Texture("blueCar.png");
		yellowCar = new Texture("yellowCar.png");
		purpleCar = new Texture("purpleCar.png");
		hearseCar = new Texture("hearseCar.png");
		redLight = new Texture("red_light.jpg");
		yellowLight = new Texture("yellow_light.jpg");
		greenLight = new Texture("green_light.jpg");
		start = new Texture("start.png");
		play = new Texture("play.png");
		quit = new Texture("quit.png");
		backBatch = new SpriteBatch();
		backGroundSkin = new Actor();backGroundSkin.draw(backBatch, 1f);
		i = 0; // count for stoplights
		lights[0] = redLight;
		lights[1] = yellowLight;
		lights[2] = greenLight;
		countDownTime = 0;
		raceStartTime = 0;
		deltaY = 0;
		finishLine = Gdx.graphics.getHeight()/2;
		stage = new Stage(new ScreenViewport());
		// Add UI elements to screen
		stage = new Stage();
		noCarSelected = new Actor();noCarSelected.setName("You must select a car before racing!");
		Gdx.input.setInputProcessor(stage);// Make the stage consume events
		createBasicSkin();
		newGameButton = new TextButton("", buttonSkin); // Use the initialized skin
		playButton = new TextButton("", buttonSkin);playButton.setWidth(200);playButton.setHeight(200); // Use the initialized skin
		quitButton = new TextButton("", buttonSkin);quitButton.setWidth(200);quitButton.setHeight(200); // Use the initialized skin

		orangeCarButton = new TextButton("", orangeSkin);orangeCarButton.setWidth(200);orangeCarButton.setHeight(200);
		blueCarButton = new TextButton("", blueSkin);blueCarButton.setWidth(200);blueCarButton.setHeight(200);
		yellowCarButton = new TextButton("", yellowSkin);yellowCarButton.setWidth(200);yellowCarButton.setHeight(200);
		greenCarButton = new TextButton("", greenSkin);greenCarButton.setWidth(200);greenCarButton.setHeight(200);
		purpleCarButton = new TextButton("", purpleSkin);purpleCarButton.setWidth(200);purpleCarButton.setHeight(200);
		buttonSpacer = Gdx.graphics.getWidth()/20;

		Image b = new Image(background);
		b.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		newGameButton.setPosition(Gdx.graphics.getWidth()/2 - newGameButton.getWidth()/2, Gdx.graphics.getHeight()/2);
		orangeCarButton.setPosition(buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		blueCarButton.setPosition(orangeCarButton.getX() + orangeCarButton.getWidth() + buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		yellowCarButton.setPosition(blueCarButton.getX() + blueCarButton.getWidth() + buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		greenCarButton.setPosition(yellowCarButton.getX() + yellowCarButton.getWidth() + buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		purpleCarButton.setPosition(greenCarButton.getX() + greenCarButton.getWidth() + buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		stage.addActor(b);
		stage.addActor(newGameButton);
		stage.addActor(orangeCarButton);
		stage.addActor(blueCarButton);
		stage.addActor(yellowCarButton);
		stage.addActor(greenCarButton);
		stage.addActor(purpleCarButton);
		addActionListeners();
	}

	/**
	 * Start a new race
	 */
	public void start() {
		newGameButton.remove();
		orangeCarButton.remove();
		yellowCarButton.remove();
		greenCarButton.remove();
		purpleCarButton.remove();
		blueCarButton.remove();

		playButton.remove();
		quitButton.remove();
		velocity = 0;
		carY = 135;
		gameState = 1;
		i = 0;
		drag = 0;
		countDownTime = TimeUtils.nanoTime();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		carWidth = width / 4.5f;
		carHeight = height / 4.5f;
		finishDistance = height / 28;
		inPreRace = true; // move this to actions lister method?
		gameStarted = false; // delete this, add to end of aciton listebner?
		font.getData().setScale(5);
	}

	/**
	 * Draw Textures, determine current stage of race
	 */
	@Override
	public void render() {
		batch.begin();
		if (inPreRace) { // three second countdown..tapps are not yet valid
			draw();
			if (TimeUtils.timeSinceNanos(countDownTime) > 1000000000 && i < 3) {
				batch.draw(lights[i], (Gdx.graphics.getWidth()/2 - (Gdx.graphics.getWidth()/8)), Gdx.graphics.getHeight()/1.5f, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);
				countDownTime = TimeUtils.nanoTime();
				i++;
			} else if (i >= 2 && TimeUtils.timeSinceNanos(countDownTime) > 1000000000) {
				raceStartTime = TimeUtils.nanoTime();
				gameState = 0;
                gameStarted = true;
				inPreRace = false;

			} else if (i < 3 && TimeUtils.timeSinceNanos(countDownTime) < 1000000000) {
                batch.draw(lights[i], (Gdx.graphics.getWidth()/2 - (Gdx.graphics.getWidth()/8)), Gdx.graphics.getHeight()/1.5f, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
			}
		} else if (gameStarted) {
			if (gameState == 1) { // screen has been tapped, car is in motion
				if (Gdx.input.justTouched()) {
					carY += velocity;
					velocity+=.12;
				}
				if (carY < finishLine + finishDistance-25) {
					velocity += drag;
					carY += velocity;
					setCarMovement();
					draw();
				} else { // finish line has been reached
					gameState = 2;
				}
			} else if (gameState == 0) { // user tapped once
				if (Gdx.input.justTouched()) {
					velocity = .12f;
					gameState = 1;
                    carY += velocity;
                    setCarMovement();
                    draw();
				}
			} else if (gameState == 2) { // game over
				raceTime = (TimeUtils.timeSinceNanos(raceStartTime))/1000000000.0f; // seconds ... how to get some decimal places? just concatinate a decimal?
				raceComplete(); // changes game state to complete
			}
		}
		else if (gameState == 3){// game started = false? menu elements
//			Gdx.gl.glClearColor(1, 1, 1, 1);
//			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			//final float raceTime = (TimeUtils.timeSinceNanos(raceStartTime))/1000000000.0f; // seconds ... how to get some decimal places? just concatinate a decimal?
			raceComplete(); // changes game state to complete
		}
		else{// game started = false? menu elements
			Gdx.gl.glClearColor(1, 1, 1, 1);
			stage.addActor(backGroundSkin);
			stage.draw();
		}
		batch.end();
	}

	/**
	 * Helper method to draw race background, finish line, and car
	 */
	public void draw(){
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(carHolder, (Gdx.graphics.getWidth()/2 - carWidth/2), carY, carWidth, carHeight);
		batch.draw(finish, Gdx.graphics.getWidth()/2 - (finish.getWidth()), Gdx.graphics.getHeight()/1.825f, Gdx.graphics.getWidth() / 13, Gdx.graphics.getWidth() / 13);
	}

	/**
	 * Show race time, set action post race action listeners
	 */
	public void raceComplete(){
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // print background and car at new position
		font.getData().setScale(5);
		font.draw(batch, String.format("Your Time was " + raceTime, "%.2"), 50, Gdx.graphics.getWidth() / 2);
		playButton.setPosition((Gdx.graphics.getWidth() / 2) + Gdx.graphics.getWidth()/15, Gdx.graphics.getHeight()/2.5f);
		quitButton.setPosition((Gdx.graphics.getWidth() / 2) - Gdx.graphics.getWidth()/3.5f, Gdx.graphics.getHeight()/2.5f);
		stage.addActor(playButton);
		stage.addActor(quitButton);
		batch.draw(play, (Gdx.graphics.getWidth() / 2) + Gdx.graphics.getWidth()/15, Gdx.graphics.getHeight()/2.5f, Gdx.graphics.getWidth() / 5, Gdx.graphics.getWidth() / 5);
		batch.draw(quit, (Gdx.graphics.getWidth() / 2) - Gdx.graphics.getWidth()/3.5f, Gdx.graphics.getHeight()/2.5f, Gdx.graphics.getWidth() / 5, Gdx.graphics.getWidth() / 5);
		inPreRace = false;
		gameStarted = false;
		gameState = 3;
		velocity = 1;
		addEndOfGameActionListeners();

	}

	/***
	 * Create skin for Buttons
	 */
	private void createBasicSkin() {
		//Create a font
		buttonSkin = new Skin();
		buttonSkin.add("default", font);
		orangeSkin = new Skin();
		orangeSkin.add("default", font);
		blueSkin = new Skin();
		blueSkin.add("default", font);
		yellowSkin = new Skin();
		yellowSkin.add("default", font);
		greenSkin = new Skin();
		greenSkin.add("default", font);
		purpleSkin = new Skin();
		purpleSkin.add("default", font);

		//Create a texture
		Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth() / 8, (int) Gdx.graphics.getHeight() / 18, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		buttonSkin.add("start", start);
		orangeSkin.add("orangeCar", orangeCar);
		blueSkin.add("blueCar", blueCar);
		yellowSkin.add("yellowCar", yellowCar);
		greenSkin.add("greenCar", greenCar);
		purpleSkin.add("purpleCar", purpleCar);

		//Create the different car buttons
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = buttonSkin.newDrawable("start", Color.GRAY);
		textButtonStyle.down = buttonSkin.newDrawable("start", Color.DARK_GRAY);
		textButtonStyle.checked = buttonSkin.newDrawable("start", Color.DARK_GRAY);
		textButtonStyle.over = buttonSkin.newDrawable("start", Color.LIGHT_GRAY);
		textButtonStyle.font = buttonSkin.getFont("default");

		TextButton.TextButtonStyle orangeTextButtonStyle = new TextButton.TextButtonStyle();
		orangeTextButtonStyle.up = orangeSkin.newDrawable("orangeCar", Color.GRAY);
		orangeTextButtonStyle.down = orangeSkin.newDrawable("orangeCar", Color.DARK_GRAY);
		orangeTextButtonStyle.checked = orangeSkin.newDrawable("orangeCar", Color.DARK_GRAY);
		orangeTextButtonStyle.over = orangeSkin.newDrawable("orangeCar", Color.LIGHT_GRAY);
		orangeTextButtonStyle.font = orangeSkin.getFont("default");

		TextButton.TextButtonStyle blueTextButtonStyle = new TextButton.TextButtonStyle();
		blueTextButtonStyle.up = blueSkin.newDrawable("blueCar", Color.GRAY);
		blueTextButtonStyle.down = blueSkin.newDrawable("blueCar", Color.DARK_GRAY);
		blueTextButtonStyle.checked = blueSkin.newDrawable("blueCar", Color.DARK_GRAY);
		blueTextButtonStyle.over = blueSkin.newDrawable("blueCar", Color.LIGHT_GRAY);
		blueTextButtonStyle.font = blueSkin.getFont("default");

		TextButton.TextButtonStyle yellowTextButtonStyle = new TextButton.TextButtonStyle();
		yellowTextButtonStyle.up = yellowSkin.newDrawable("yellowCar", Color.GRAY);
		yellowTextButtonStyle.down = yellowSkin.newDrawable("yellowCar", Color.DARK_GRAY);
		yellowTextButtonStyle.checked = yellowSkin.newDrawable("yellowCar", Color.DARK_GRAY);
		yellowTextButtonStyle.over = yellowSkin.newDrawable("yellowCar", Color.LIGHT_GRAY);
		yellowTextButtonStyle.font = yellowSkin.getFont("default");

		TextButton.TextButtonStyle purpleTextButtonStyle = new TextButton.TextButtonStyle();
		purpleTextButtonStyle.up = purpleSkin.newDrawable("purpleCar", Color.GRAY);
		purpleTextButtonStyle.down = purpleSkin.newDrawable("purpleCar", Color.DARK_GRAY);
		purpleTextButtonStyle.checked = purpleSkin.newDrawable("purpleCar", Color.DARK_GRAY);
		purpleTextButtonStyle.over = purpleSkin.newDrawable("purpleCar", Color.LIGHT_GRAY);
		purpleTextButtonStyle.font = purpleSkin.getFont("default");

		TextButton.TextButtonStyle greenTextButtonStyle = new TextButton.TextButtonStyle();
		greenTextButtonStyle.up = greenSkin.newDrawable("greenCar", Color.GRAY);
		greenTextButtonStyle.down = greenSkin.newDrawable("greenCar", Color.DARK_GRAY);
		greenTextButtonStyle.checked = greenSkin.newDrawable("greenCar", Color.DARK_GRAY);
		greenTextButtonStyle.over = greenSkin.newDrawable("greenCar", Color.LIGHT_GRAY);
		greenTextButtonStyle.font = greenSkin.getFont("default");

		buttonSkin.add("default", textButtonStyle);
		orangeSkin.add("default", orangeTextButtonStyle);
		blueSkin.add("default", blueTextButtonStyle);
		yellowSkin.add("default", yellowTextButtonStyle);
		purpleSkin.add("default", purpleTextButtonStyle);
		greenSkin.add("default", greenTextButtonStyle);
	}

	/**
	 * Add Action to pre race buttons
	 */
	public void addActionListeners(){
		newGameButton.addListener(new ClickListener()
		{
			@Override
			public boolean handle(Event event)
			{
				System.out.println("YOU TOUCHED NEW GAME");
				if (carHolder == null){
					stage.addActor(noCarSelected);noCarSelected.toString();
					return false;
				}
				start();
				return true;
			}
		});
		orangeCarButton.addListener(new ClickListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED BLUE");
				carHolder = orangeCar;
				return true;
			}
		});
		blueCarButton.addListener(new ClickListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED BLUE");
				carHolder = blueCar;
				return true;
			}
		});
		yellowCarButton.addListener(new ClickListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED YELLOW");
				carHolder = yellowCar;
				return true;
			}
		});
		greenCarButton.addListener(new ClickListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED GREEN");
				carHolder = greenCar;
				return true;
			}
		});
		purpleCarButton.addListener(new ClickListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED PURPLE");
				carHolder = purpleCar;
				return true;
			}
		});
	}

	/**
	 * Add action listeners for end of post race buttons
	 */
	public void addEndOfGameActionListeners(){
		playButton.addListener(new EventListener()
		{
			@Override
			public boolean handle(Event event)
			{
				System.out.println("YOU TOUCHED START");
				start();
				return true;
			}
		});
		quitButton.addListener(new EventListener()
			{
			@Override
			public boolean handle(Event event)
			{
				System.out.println("YOU TOUCHED EXIT");
				//Gdx.app.exit();
				create(); // make setUp method that does not re-instanciate..?
				return true;
			}
		});
		}

	/**
	 * Set the car movement
	 */
	public void setCarMovement(){
		deltaY = (carY - 135)*.0075f;
		carWidth = width / (4.5f + ((deltaY*deltaY*deltaY)*.075f));
		carHeight = height / (4.5f + ((deltaY*deltaY*deltaY)*.075f));
	}

	/**
	 * Dispose the batch
	 */
	@Override
		public void dispose () {
			batch.dispose();
		}
	}


