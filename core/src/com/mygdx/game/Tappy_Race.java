package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Tappy_Race extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background, finish, orangeCar, blueCar, purpleCar, yellowCar, greenCar, hearseCar, carHolder, start;
	Texture[] lights = new Texture[3];
	Texture redLight, yellowLight, greenLight;
	long countDownTime,raceStartTime;
	float totalTime, raceTime;
	int numClicks;
	boolean gameStarted;
	int i = 0;
	int gameState;
	boolean inPreRace;
	float carY, deltaY, drag, velocity;
	float finishLine, finishDistance;
	float width, height, carWidth, carHeight;
	Actor noCarSelected;
	private Stage stage;
	Skin buttonSkin, yellowSkin, orangeSkin, blueSkin, greenSkin, purpleSkin;
	Actor backGroundSkin;
	Batch backBatch;
	TextButton newGameButton;
	TextButton quitGameButton, orangeCarButton, blueCarButton, yellowCarButton, greenCarButton, purpleCarButton, startButton;

	@Override
	public void create() {
		inPreRace = false;
 		gameStarted = false;
		batch = new SpriteBatch();
		totalTime = 3.5f;
		carWidth = 0;
		velocity = 0;
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
		backBatch = new SpriteBatch();
		backGroundSkin = new Actor();backGroundSkin.draw(backBatch, 1f);
		lights[0] = redLight;
		lights[1] = yellowLight;
		lights[2] = greenLight;
		countDownTime = 0;
		raceStartTime = 0;
		numClicks = 1;
		deltaY = 0;
		finishLine = Gdx.graphics.getHeight()/2;
		stage = new Stage(new ScreenViewport());
		// Add UI elements to screen
		stage = new Stage();
		noCarSelected = new Actor();noCarSelected.setName("You must select a car before racing!");
		Gdx.input.setInputProcessor(stage);// Make the stage consume events
		createBasicSkin();
		newGameButton = new TextButton("Start game", buttonSkin); // Use the initialized skin
	//	quitGameButton = new TextButton("Quit", buttonSkin); // Use the initialized skin
		orangeCarButton = new TextButton("ORANGE", orangeSkin);orangeCarButton.setWidth(200);orangeCarButton.setHeight(200);
		blueCarButton = new TextButton("BLUE", blueSkin);blueCarButton.setWidth(200);blueCarButton.setHeight(200);
		yellowCarButton = new TextButton("YELLOW", yellowSkin);yellowCarButton.setWidth(200);yellowCarButton.setHeight(200);
		greenCarButton = new TextButton("GREEN", greenSkin);greenCarButton.setWidth(200);greenCarButton.setHeight(200);
		purpleCarButton = new TextButton("PURPLE", purpleSkin);purpleCarButton.setWidth(200);purpleCarButton.setHeight(200);
		//green = new Sprite(greenCar);
		//SpriteDrawable gd = new SpriteDrawable(green);
		Image b = new Image(background);
		b.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		int buttonSpacer = Gdx.graphics.getWidth()/20;
		newGameButton.setPosition(Gdx.graphics.getWidth()/2 - newGameButton.getWidth()/2, Gdx.graphics.getHeight()/2);
	//	quitGameButton.setPosition(Gdx.graphics.getWidth()/2 - newGameButton.getWidth() - buttonSpacer, Gdx.graphics.getHeight()/2);
		orangeCarButton.setPosition(buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		blueCarButton.setPosition(orangeCarButton.getX() + orangeCarButton.getWidth() + buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		yellowCarButton.setPosition(blueCarButton.getX() + blueCarButton.getWidth() + buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		greenCarButton.setPosition(yellowCarButton.getX() + yellowCarButton.getWidth() + buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		purpleCarButton.setPosition(greenCarButton.getX() + greenCarButton.getWidth() + buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		stage.addActor(b);
		stage.addActor(newGameButton);
//		stage.addActor(quitGameButton);
		stage.addActor(orangeCarButton);
		stage.addActor(blueCarButton);
		stage.addActor(yellowCarButton);
		stage.addActor(greenCarButton);
		stage.addActor(purpleCarButton);
		addActionListeners();
	}

	/***
	 * Create skin for Buttons
	 */
	private void createBasicSkin() {
		//Create a font
		BitmapFont font = new BitmapFont();
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

		//Create a button style
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
	 * Add Action Listeners to menu buttons
	 */
	public void addActionListeners(){
		newGameButton.addListener(new EventListener()
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
		orangeCarButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED BLUE");
				carHolder = orangeCar;
				return true;
			}
		});
		blueCarButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED BLUE");
				carHolder = blueCar;
				return true;
			}
		});
		yellowCarButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED YELLOW");
				carHolder = yellowCar;
				return true;
			}
		});
		greenCarButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED GREEN");
				carHolder = greenCar;
				return true;
			}
		});
		purpleCarButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("YOU TOUCHED PURPLE");
				carHolder = purpleCar;
				return true;
			}
		});
	}

	public void start() {
	//	carHolder = orangeCar;
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
		numClicks = 1;
		inPreRace = true; // move this to actions lister method?
		gameStarted = false; // delete this, add to end of aciton listebner?

	}

	@Override
	public void render() {
		batch.begin();
	//	stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
		if (inPreRace) { // three second countdown..tapps are not yet valid
//			batch.begin();
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(carHolder, (Gdx.graphics.getWidth() / 2 - carWidth/2), carY, carWidth, carHeight);
			batch.draw(finish, 655, 1280, Gdx.graphics.getWidth() / 11, Gdx.graphics.getWidth() / 12);
			if (TimeUtils.timeSinceNanos(countDownTime) > 1000000000 && i < 3) {
				batch.draw(lights[i], 522, 1440, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
				countDownTime = TimeUtils.nanoTime();
				i++;
			} else if (i >= 3 && TimeUtils.timeSinceNanos(countDownTime) > 1000000000) {
				gameStarted = true;
				raceStartTime = TimeUtils.nanoTime();
				inPreRace = false;
				gameState = 0;
				velocity = 1;
			} else if (i < 3 && TimeUtils.timeSinceNanos(countDownTime) < 1000000000) {
				batch.draw(lights[i], 522, 1440, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
			}
		} else if (gameStarted) {
			if (gameState == 1) { // screen has been tapped, car is in motion
				if (Gdx.input.justTouched()) {
					numClicks++;
					carY += velocity;
					velocity+=.55;
				}
				if (carY < finishLine + finishDistance-25) {
					velocity += drag;
					carY += velocity;
					setCarMovement();
				} else {
					gameState = 2;
				}
			} else if (gameState == 0) { // user tapped once
				if (Gdx.input.justTouched()) {
					gameState = 1;
				}
			} else if (gameState == 2) { // game over
				raceTime = (TimeUtils.timeSinceNanos(raceStartTime))/1000000000.0f; // seconds ... how to get some decimal places? just concatinate a decimal?
				System.out.format("%.2f", raceTime).println("This was the time it took to raceEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE " + raceTime);
				stopShit();
			}
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(finish, 655, 1280, Gdx.graphics.getWidth() / 11, Gdx.graphics.getWidth() / 12);
			batch.draw(carHolder, (Gdx.graphics.getWidth() / 2 - carWidth/2), carY, carWidth, carHeight);
			//batch.end();
		}
		else if (!inPreRace && !gameStarted){// game started = false? menu elements
			//batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			stage.act();
			stage.addActor(backGroundSkin);
			stage.draw();
		}
		batch.end();
	}

	public void setCarMovement(){
		deltaY = (carY - 135)*.0075f;
		carWidth = width / (4.5f + ((deltaY*deltaY*deltaY)*.055f));
		carHeight = height / (4.5f + ((deltaY*deltaY*deltaY)*.055f));
	}
	public void stopShit(){
		System.out.println("This was the time it took to race... HAS IT ENDEDDDDDDDDDD " + raceTime);
		gameStarted = false;
	}
		@Override
		public void dispose () {
			batch.dispose();
		}
	}


