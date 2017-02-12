package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tappy_Race extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture badLog;
	Texture finish;
	int state=1;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("road_into.jpg");
		badLog = new Texture("badlogic.jpg");
		finish = new Texture("finish_removed.jpg");

	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(finish, 200, 200, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);

		batch.end();
		if (state == 1){

		}

	//	birds = new Texture[2];
	//	birds[0] = new Texture("bird.png");
	//	birds[1] = new Texture("bird2.png");
	//	birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	//	img.dispose();
	}
}
