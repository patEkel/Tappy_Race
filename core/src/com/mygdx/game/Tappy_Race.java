package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;


public class Tappy_Race extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background, finish, car;
	Texture[] lights = new Texture[3];
	Texture redLight, yellowLight, greenLight;
	long startTime;
	float totalTime;
	int numClicks;
	boolean gameStarted;
	int i = 0;
	boolean inPreRace;
	float carY;
	float velocity;
	int gameState;
	float drag;
	float deltaY;
	float lastY;
	float changeInY;
	float finishDistance;
	float width;
	float height;
	float carWidth;
	float carHeight;
	float currentCarWidth;
	float currentCarHeight;
	float finishLine;

	@Override
	public void create() {
		inPreRace = true;
 		gameStarted = false;
		batch = new SpriteBatch();
		totalTime = 3.5f;
		carWidth = 0;
		velocity = 0;
		gameState = 0;
		drag = 0;
		background = new Texture("road_into.jpg");
		finish = new Texture("finish_removed.jpg");
		car = new Texture("orangeSportCar.jpg");
		redLight = new Texture("red_light.jpg");
		yellowLight = new Texture("yellow_light.jpg");
		greenLight = new Texture("green_light.jpg");
		lights[0] = redLight;
		lights[1] = yellowLight;
		lights[2] = greenLight;
		startTime = 0;
		numClicks = 1;
		lastY = 135;
		deltaY = 0;
		changeInY = 0;
		currentCarWidth = 4.5f;
		currentCarHeight = 4.5f;
		finishLine = Gdx.graphics.getHeight()/2;
		start();
	}

	public void start() {
		carY = 135;
		gameState = 1;
		inPreRace = true;
		i = 0;
		drag = 0;
		startTime = TimeUtils.nanoTime();
		gameStarted = false;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		carWidth = width / currentCarWidth;
		carHeight = height / currentCarWidth;
		finishDistance = height / 28;
		numClicks = 1;

	}

	@Override
	public void render() {
		batch.begin();
		if (inPreRace) {
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(car, (Gdx.graphics.getWidth() / 2 - carWidth/2), carY, carWidth, carHeight);
			batch.draw(finish, 655, 1280, Gdx.graphics.getWidth() / 11, Gdx.graphics.getWidth() / 12);
			if (TimeUtils.timeSinceNanos(startTime) > 1000000000 && i < 3) {
				batch.draw(lights[i], 522, 1440, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
				startTime = TimeUtils.nanoTime();
				i++;
			} else if (i >= 3 && TimeUtils.timeSinceNanos(startTime) > 1000000000) {
				gameStarted = true;
				inPreRace = false;
				gameState = 1;
				velocity = 1;
			} else if (i < 3 && TimeUtils.timeSinceNanos(startTime) < 1000000000) {
				batch.draw(lights[i], 522, 1440, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
			}
		} else if (gameStarted) {
			if (gameState == 1) {
				if (Gdx.input.justTouched()) {
					numClicks++;
					carY += velocity;
					velocity+=.55;
				}
				if (carY < finishLine + finishDistance) {
					velocity += drag;
					carY += velocity;
					setCarMovement();
				} else {
					gameState = 2;
				}
			} else if (gameState == 0) {
				if (Gdx.input.justTouched()) {
					gameState = 1;
				}
			} else if (gameState == 2) {
				//GAME OVER win winnnnderrr?
			}
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(finish, 655, 1280, Gdx.graphics.getWidth() / 11, Gdx.graphics.getWidth() / 12);
			batch.draw(car, (Gdx.graphics.getWidth() / 2 - carWidth/2), carY, carWidth, carHeight);
		}
		batch.end();
	}
	
	public void setCarMovement(){
		System.out.println("CarY is " + (carY - 135));
		deltaY = (carY - 135)*.0075f;
		carWidth = width / (4.5f + ((deltaY*deltaY*deltaY)*.055f));
		carHeight = height / (4.5f + ((deltaY*deltaY*deltaY)*.055f));
	}

		@Override
		public void dispose () {
			batch.dispose();
			//	img.dispose();
		}
	}


