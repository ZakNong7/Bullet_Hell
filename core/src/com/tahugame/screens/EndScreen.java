package com.tahugame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tahugame.TahuGame;

public class EndScreen extends BaseScreen {
    private Texture background;
    private Texture menuButton;
    private Music endMusic;
    private boolean isWin;

    private BitmapFont font;
    private GlyphLayout layout;
    private Rectangle menuButtonBounds;

    public EndScreen(TahuGame game, boolean isWin) {
        super(game);
        this.isWin = isWin;

        if (isWin) {
            background = new Texture("images/win-bg.png");
            endMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/win.mp3"));
        } else {
            background = new Texture("images/lose-bg.png");
            endMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/lose.mp3"));
        }

        menuButton = new Texture("images/menu-btn.png");

        font = new BitmapFont();
        font.getData().setScale(3f);
        layout = new GlyphLayout();

        float centerX = (TahuGame.GAME_WIDTH - 220) / 2;
        menuButtonBounds = new Rectangle(centerX, 120, 220, 60);

        endMusic.setVolume(0.7f);
        endMusic.play();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0, TahuGame.GAME_WIDTH, TahuGame.GAME_HEIGHT);

        batch.draw(menuButton, menuButtonBounds.x, menuButtonBounds.y, menuButtonBounds.width, menuButtonBounds.height);

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touch = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            if (menuButtonBounds.contains(touch.x, touch.y)) {
                endMusic.stop();
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        menuButton.dispose();
        endMusic.dispose();
        font.dispose();
    }
}