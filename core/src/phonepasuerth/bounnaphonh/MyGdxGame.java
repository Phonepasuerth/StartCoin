package phonepasuerth.bounnaphonh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	//Explicit
	private SpriteBatch batch;
	private Texture wallpaperTexture, cloudTexture,
			pigTexture, coinsTexture, rainTexture;
	private OrthographicCamera objOrthographicCamera;//To make Auto size on device in difference screen
	private BitmapFont nameBitmapFont, scoreBitmapFont;
	private int xCloudAnInt, yCloudAnInt = 600;
	private boolean cloudABoolean = true;
	private Rectangle pigRectangle,coinsRectangle, rainRectangle;//Reflection to correct or wrong match
	private Vector3 objVector3;
	private Sound pigSound,waterDropSound,coinsDropSound;
	private Array<Rectangle> coinsArray, rainArray;
	private long lastDropCoins, lastDropRain;
	private Iterator<Rectangle> coinsIterator, rainIterator;//==>java.util
	private int scoreAnInt = 0;


	@Override
	public void create () {
		batch = new SpriteBatch();

		//การกำนดขนาดของจอที่ต้องการ Setup screen
		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1280, 720);

		//Setup WallPaper
		wallpaperTexture = new Texture("background.png");

		//SetUP BitMapFont
		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(Color.GREEN);
		nameBitmapFont.setScale(4);
		//setUp Cloud
		cloudTexture = new Texture("cloud.png");
		//Setup pig

		pigTexture = new Texture("pig.png");
		// Setup Rectangle pig
		pigRectangle = new Rectangle();
		pigRectangle.x = 568;
		pigRectangle.y = 100;
		pigRectangle.width = 64;
		pigRectangle.height = 64;

		//Setup pig sound
		pigSound = Gdx.audio.newSound(Gdx.files.internal("pig.wav"));
		//setup coin
		coinsTexture = new Texture("coins.png");
		//Create coinArray
		coinsArray = new Array<Rectangle>();
		coinsRandomDrop();



		//Setup WaterDrop
		waterDropSound = Gdx.audio.newSound(Gdx.files.internal("water_drop.wav"));
		//Set coin drop
		coinsDropSound = Gdx.audio.newSound(Gdx.files.internal("coins_drop.wav"));
		//Setup scoreBitMapfont
		scoreBitmapFont = new BitmapFont();
		scoreBitmapFont.setColor(Color.BLUE);
		scoreBitmapFont.setScale(4);
		//Setup rainTexture
		rainTexture = new Texture("droplet.png");

		// Create RainArray
		rainArray = new Array<Rectangle>();
		rainRandomDrop();

	}	//Create เอาไว้กำนดค่า

	private void rainRandomDrop() {
		rainRectangle = new Rectangle();
		rainRectangle.x = MathUtils.random(0, 1136);//1136 from 1200-64
		rainRectangle.y = 800;
		rainRectangle.width=64;
		rainRectangle.height = 64;
		rainArray.add(rainRectangle);
		lastDropRain = TimeUtils.nanoTime();
	}

	private void coinsRandomDrop() {
		coinsRectangle = new Rectangle();
		coinsRectangle.x = MathUtils.random(0, 1136);// txt mobile screen - 64
		coinsRectangle.y = 800;//800 from the top to buttom
		coinsRectangle.width = 64;//coin pic width
		coinsRectangle.height = 64;//coin pic height
		coinsArray.add(coinsRectangle);
		lastDropCoins = TimeUtils.nanoTime();
	}//CoinsRandom

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Setup Screen
		objOrthographicCamera.update();
		batch.setProjectionMatrix(objOrthographicCamera.combined);

		//เอาไว้วาด object
		batch.begin();

		//Drawable Wallpaper


		batch.draw(wallpaperTexture, 0, 0);

		//Drawable Cloud
		batch.draw(cloudTexture, xCloudAnInt,yCloudAnInt);

		//Drawable BitMaoFont
		nameBitmapFont.draw(batch, "Coins Collection", 450, 700);
		// Drawable Pig
		batch.draw(pigTexture, pigRectangle.x, pigRectangle.y);

		// Drawable Coins
		for (Rectangle forCoins : coinsArray) {
			batch.draw(coinsTexture, forCoins.x, forCoins.y);
		}
		//Drawable score
		scoreBitmapFont.draw(batch,"score="+Integer.toString(scoreAnInt), 500,500);//
		//drawable Rain
		for (Rectangle forRain : rainArray) {
			batch.draw(rainTexture, forRain.x, forRain.y);
		}


			batch.end();

		//Move Cloud

		moveCloud();
		//Active When Touch Screen
		activeTouchScreen();
		//Random Drop Coins
		randomDropCoins();
		//Random Drop Rain
		randomDropRain();


	}	//Render user as loop

	private void randomDropRain() {
		if (TimeUtils.nanoTime() - lastDropRain > 1E9) {
			rainRandomDrop();
		}    //if
		rainIterator = rainArray.iterator();
		while (rainIterator.hasNext()) {

			Rectangle myRainRectangle = rainIterator.next();
			myRainRectangle.y -= 50 * Gdx.graphics.getDeltaTime();

			//When Rain drop into floor
			if (myRainRectangle.y+64<0) {
				waterDropSound.play();
				rainIterator.remove();
			}// if
			//When Rain Overlap Pig
			if (myRainRectangle.overlaps(pigRectangle)) {
				scoreAnInt -= 1;
				waterDropSound.play();
				rainIterator.remove();
			}    //if

		}    //While

	}	//randomDropRain

	private void randomDropCoins() {
		if (TimeUtils.nanoTime()-lastDropCoins>1E9) {//1E9 mean 10^9
			coinsRandomDrop();
		}
		coinsIterator = coinsArray.iterator();
		while (coinsIterator.hasNext()) {

			Rectangle myCoinsRectangle = coinsIterator.next();
			myCoinsRectangle.y -= 50 * Gdx.graphics.getDeltaTime();
			//when Coins into Floor(To clear memory
			if (myCoinsRectangle.y + 64 < 0) {
				waterDropSound.play();
				coinsIterator.remove();

			}// if loop
			//WHen coins overlap Pig
			if (myCoinsRectangle.overlaps(pigRectangle)) {
				scoreAnInt += 1;
				coinsDropSound.play();
				coinsIterator.remove();
			}// if

		}// while loop


	}//RandomDropCoins

	private void activeTouchScreen() {
		if (Gdx.input.isTouched()) {//Boolean on touch screen when touch
			//Sound effect pig
			pigSound.play();//to recall sound from file
			objVector3 = new Vector3();
			objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			if (objVector3.x < Gdx.graphics.getWidth()/2) {

				if (pigRectangle.x < 0) {
					pigRectangle.x = 0;
				} else {
					pigRectangle.x -= 10;
				}

			} else {
				if (pigRectangle.x > 1136) {
					pigRectangle.x = 1136;
				} else {
					pigRectangle.x += 10;
				}
			}
		}//If Statement

	}//Active TouchScreen

	private void moveCloud() {
		if (cloudABoolean) {
			if (xCloudAnInt<937) {
				xCloudAnInt += 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		} else {
			if (xCloudAnInt>0) {
				xCloudAnInt -= 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		}

	}//moveCLoud
}	//Main Class
