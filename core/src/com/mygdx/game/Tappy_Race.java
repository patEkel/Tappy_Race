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
	float width;
	float height;
	float carWidth;
	float carHeight;
	float finishLine;



	@Override
	public void create() {   /////////////// INSTANTIATE ALL VARIABLES HEREEEEE not UP TOPPPPPPP
		inPreRace = true;		//////////  "HARD" code car dimensions and adjust based on num clicks/ new xWid yWidth in that location......switch statement!@!///
 		gameStarted = false;
		batch = new SpriteBatch();
		totalTime = 3.5f;
		carWidth = 0;
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
		finishLine = Gdx.graphics.getHeight()/2;
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
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		carWidth = width / 4.5f;
		carHeight = height / 4.5f;

	}

	@Override
	public void render() {
		batch.begin();
		if (inPreRace) {
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(car, (Gdx.graphics.getWidth() / 2 - carWidth/2), 135, carWidth, carHeight);
			batch.draw(finish, 655, 1280, Gdx.graphics.getWidth() / 11, Gdx.graphics.getWidth() / 12);
			if (TimeUtils.timeSinceNanos(startTime) > 1000000000 && i < 3) {
				batch.draw(lights[i], 522, 1440, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
				startTime = TimeUtils.nanoTime();
				i++;
			} else if (i >= 3 && TimeUtils.timeSinceNanos(startTime) > 1000000000) {
				gameStarted = true;
				inPreRace = false;
				gameState = 1;
				velocity = 0;//200;
			} else if (i < 3 && TimeUtils.timeSinceNanos(startTime) < 1000000000) {
				batch.draw(lights[i], 522, 1440, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
			}
		} else if (gameStarted) {
			//setCarMovement();
			velocity = drag;
			if (gameState == 1) {
				if (Gdx.input.justTouched()) {
					numClicks++;
					//setCarMovement();
					carY += velocity;
				}
				if (carY < finishLine) {
					setCarMovement();
					velocity += drag;
					carY += velocity;

				} else {
					gameState = 2;
				}//gameState=0;
			} else if (gameState == 0) {
				if (Gdx.input.justTouched()) {
					gameState = 1;
				}
			} else if (gameState == 2) {								//////////////////											!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				//GAME OVER win winnnnderrr?                                            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
			}																//// gonnan need a switch here of some sort...to determine car width and height
			batch.draw(finish, 655, 1280, Gdx.graphics.getWidth() / 11, Gdx.graphics.getWidth() / 12);
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(car, (Gdx.graphics.getWidth() / 2 - carWidth/2), carY, carWidth, carHeight);
		}
		batch.end();
	}


	public void setCarMovement(){
		if (carY < height / 6){
			drag = .045f;velocity = 4;carWidth *=.998;/// 4.6f;
			carHeight*=.998; //return;
		}
		else if (carY < height / 5){
			drag = .075f;velocity = 4.5f;carWidth *=.995;/// 4.6f;
			carHeight*=.995;
		}
		else if (carY < height / 4){
			drag = .125f;velocity = 4.5f;carWidth *=.992;/// 4.6f;
			carHeight*=.992;
		}
		else if (carY < height / 3){
			drag = .145f;velocity = 4.75f;carWidth *=.988;/// 4.6f;
			carHeight*=.986;
		}
		else{
			drag = .175f;velocity = 5.2f;carWidth *=.979;/// 4.6f;
			carHeight*=.975;
		}
		/**
		if (carY < finishLine/3){  // drag a push? .. change to momentum..
			drag = .075f;velocity = 5;carWidth *=.98;/// 4.6f;
			carHeight*=.98; return;}// = height / 4.6f;return;	}
		else if (carY < finishLine/2.5){
			drag = .075f;velocity = 5;carWidth*= .98;// height / 4.8f;
			carHeight *= .98;return;}//4.8f;return;	}
		else if (carY < finishLine/2){
			drag = .1f;velocity = 6;carWidth = width / 5.1f;
			carHeight = height / 5.1f;return;	}
		else if (carY < finishLine/1.8){
			drag = .15f;velocity = 6;carWidth = width / 5.4f;
			carHeight = height / 5.4f;return;	}
		else if (carY < finishLine/1.6){
			drag = .25f;velocity = 7;carWidth = width / 5.8f;
			carHeight =  height/ 5.8f;return;	}
		else if (carY < finishLine/1.4){
			drag = .3f;velocity = 7;carWidth = width / 6.3f;
			carHeight = height / 6.3f;return;	}
		else if (carY < finishLine/1.2){
			drag = .35f;velocity = 8;carWidth = width / 6.8f;
			carHeight = height / 6.8f;return;	}
		else if (carY < finishLine/1.1){
			drag = .4f;velocity = 8;carWidth = width / 7.5f;
			carHeight = height / 7.5f;return;	}
		else if (carY < finishLine/1.08){
			drag = .5f;velocity = 8;carWidth = width / 8.5f;
			carHeight = height / 8.5f;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/10;
			carHeight = height /10;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/12;
			carHeight = height/12;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/14;
			carHeight = height/14;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/15;
			carHeight = height/13;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/17;
			carHeight = height/17;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/20;
			carHeight = height/20;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/22;
			carHeight = height/22;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/25;
			carHeight = height/25;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/28;
			carHeight = height/28;return;	}
		else if (carY < finishLine/1.05){
			drag = .6f;velocity = 8;carWidth = width/32;
			carHeight = height/32;return;	}

		else{
			drag = .75f;velocity = 8;carWidth = width/35;
			carHeight = height/35;
			//System.out.println(Gdx.graphics.getHeight());

		 }**/
	}

		@Override
		public void dispose () {
			batch.dispose();
			//	img.dispose();
		}
	}


