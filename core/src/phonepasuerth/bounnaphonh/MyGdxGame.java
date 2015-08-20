package phonepasuerth.bounnaphonh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	//Explicit
	private SpriteBatch batch;
	private Texture wallpaperTexture;
	private OrthographicCamera objOrthographicCamera;//To make Auto size on device in difference screen
	private BitmapFont nameBitmapFont;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		//การกำนดขนาดของจอที่ต้องการ Setup screen
		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false,1280,720);

		//Setup WallPaper
		wallpaperTexture = new Texture("background.png");

		//SetUP BitMapFont
		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(Color.GREEN);
		nameBitmapFont.setScale(4);

	}	//Create เอาไว้กำนดค่า

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

		//Drawable BitMaoFont
		nameBitmapFont.draw(batch, "Coin Collection", 30, 500);

		batch.end();
	}	//Render user as loop
}	//Main Class
