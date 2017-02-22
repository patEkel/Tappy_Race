package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Tappy_Race extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background, finish, orangeCar, blueCar, purpleCar, yellowCar, greenCar, hearseCar, carHolder;
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
	private Stage stage;
	Skin skin;
	Actor backGroundSkin;
	Batch backBatch;
	TextButton newGameButton;
	TextButton quitGameButton, orangeCarButton, blueCarButton, yellowCarButton, greenCarButton, purpleCarButton;
	Sprite green;
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
		Gdx.input.setInputProcessor(stage);// Make the stage consume events
		createBasicSkin();
		newGameButton = new TextButton("New game", skin); // Use the initialized skin
		quitGameButton = new TextButton("Quit", skin); // Use the initialized skin
		orangeCarButton = new TextButton("ORANGE", skin);
		blueCarButton = new TextButton("BLUE", skin);
		yellowCarButton = new TextButton("YELLOW", skin);
		greenCarButton = new TextButton("GREEN", skin);
		purpleCarButton = new TextButton("PURPLE", skin);
		green = new Sprite(greenCar);
		SpriteDrawable gd = new SpriteDrawable(green);
		Image b = new Image(background);
		b.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		int buttonSpacer = Gdx.graphics.getWidth()/20;
		newGameButton.setPosition(Gdx.graphics.getWidth()/2 + buttonSpacer, Gdx.graphics.getHeight()/2);
		quitGameButton.setPosition(Gdx.graphics.getWidth()/2 - newGameButton.getWidth() - buttonSpacer, Gdx.graphics.getHeight()/2);
	//	orangeCarButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/10-orangeCarButton.getWidth() - buttonSpacer, Gdx.graphics.getHeight()/2.5f);
	//	blueCarButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/10+blueCarButton.getWidth() - (2*buttonSpacer), Gdx.graphics.getHeight()/2.5f);
	//	yellowCarButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/10+(2*yellowCarButton.getWidth()) - buttonSpacer, Gdx.graphics.getHeight()/2.5f);
		greenCarButton.setBackground(gd);greenCarButton.setPosition(buttonSpacer, Gdx.graphics.getHeight()/2.5f);
	//	purpleCarButton.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2.5f);
		stage.addActor(b);
		stage.addActor(newGameButton);
		stage.addActor(quitGameButton);
		stage.addActor(orangeCarButton);
		stage.addActor(blueCarButton);
		stage.addActor(yellowCarButton);
		stage.addActor(greenCarButton);
	//	stage.addActor(purpleCarButton);
		addActionListeners();
	}

	/***
	 * Create skin for Buttons
	 */
	private void createBasicSkin() {
		//Create a font
		BitmapFont font = new BitmapFont();
		skin = new Skin();
		skin.add("default", font);
		//Create a texture
		Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth() / 8, (int) Gdx.graphics.getHeight() / 18, Pixmap.Format.RGB888);
		//Pixmap imagePixmap = new Pixmap((int) Gdx.graphics.getWidth() / 8, (int) Gdx.graphics.getHeight() / 18, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		//skin.add("background", new Texture(pixmap));
		skin.add("background", background);////////right hereeeeeeeeeeeeeeeeee
		//Create a button style
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
		textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
		textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
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

				start();
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
			batch.draw(orangeCar, (Gdx.graphics.getWidth() / 2 - carWidth/2), carY, carWidth, carHeight);
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


