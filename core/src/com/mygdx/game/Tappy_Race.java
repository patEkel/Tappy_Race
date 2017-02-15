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
	float totalTime = 3.5f;
	int numClicks = 1;
	boolean gameStarted = false;
	int i = 0;
	boolean inPreRace = true;
	float carY = 0;
	float velocity = 0;
	int gameState = 0;
	float drag = 0;


	@Override
	public void create() {   /////////////// INSTANTIATE ALL VARIABLES HEREEEEE not UP TOPPPPPPP
		inPreRace = true;		//////////  "HARD" code car dimensions and adjust based on num clicks/ new xWid yWidth in that location
 		gameStarted = false;
		batch = new SpriteBatch();
		totalTime = 3.5f;
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
		start();
	}

	public void start() {
		//carY = (Gdx.graphics.getHeight() / 2 - car.getHeight() / 8); //new Timer().scheduleAtFixedRate(task, after, interval)
		carY = 135;
		gameState = 1;
		inPreRace = true;
		i = 0;
		drag = 0;
		startTime = TimeUtils.nanoTime();
		gameStarted = false;
	}

	@Override
	public void render() {
		batch.begin();
		if (inPreRace) {
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(car, (Gdx.graphics.getWidth() / 2 - car.getWidth() / 5.5f), 135, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
			batch.draw(finish, 655, 1280, Gdx.graphics.getWidth() / 11, Gdx.graphics.getWidth() / 12);
			if (TimeUtils.timeSinceNanos(startTime) > 1000000000 && i < 3) {
				batch.draw(lights[i], 522, 1440, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
				startTime = TimeUtils.nanoTime();
				i++;
			} else if (i >= 3 && TimeUtils.timeSinceNanos(startTime) > 1000000000) {
				gameStarted = true;
				inPreRace = false;
				gameState = 1;
				velocity = 200;
			} else if (i < 3 && TimeUtils.timeSinceNanos(startTime) < 1000000000) {
				batch.draw(lights[i], 522, 1440, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
			}
		} else if (gameStarted) {
			setDrag();
			velocity = drag;
			if (gameState == 1) {
				if (Gdx.input.justTouched()) {
					velocity = 10; //switch statement on velocity?
					numClicks++;
					carY += 5;
				}
				//gameState = 0;
				if (carY < Gdx.graphics.getHeight() / 2) {
					velocity += drag;
					carY += velocity;
					//carY+=drag;
				} else {
					gameState = 2;
				}//gameState=0;
			} else if (gameState == 0) {
				if (Gdx.input.justTouched()) {
					gameState = 1;
				}
			} else if (gameState == 2) {
				//GAME OVER win winnnnderrr?
			}
			batch.draw(finish, 655, 1280, Gdx.graphics.getWidth() / 11, Gdx.graphics.getWidth() / 12);
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(car, (Gdx.graphics.getWidth() / 2 - (1/numClicks)*50), carY, Gdx.graphics.getWidth() / (4+(numClicks/10)), Gdx.graphics.getHeight() / (4+(numClicks/10)));
		}
		batch.end();
	}


	public void setDrag() {
		switch (numClicks) {
			case (1):
				drag = .01f;
				break;
			case (2):
				drag = .012f;
				break;
			case (3):
				drag = .015f;
				break;
			case (4):
				drag = .018f;
				break;
			case (5):
				drag = .12f;
				break;
			case (6):
				drag = .18f;
				break;
			case (7):
				drag = .15f;
				break;
			case (8):
				drag = .2f;
				break;
			default:
				drag = .3f;
				break;
		}
	}

		@Override
		public void dispose () {
			batch.dispose();
			//	img.dispose();
		}
	}


