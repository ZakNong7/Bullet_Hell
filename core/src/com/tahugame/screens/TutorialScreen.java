package com.tahugame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tahugame.TahuGame;

public class TutorialScreen extends BaseScreen {
    private Texture background;
    private Texture backButton;
    private BitmapFont debugFont;
    private Rectangle backButtonBounds;

    public TutorialScreen(TahuGame game) {
        super(game);

        background = new Texture("images/cara_main-bg.png");
        backButton = new Texture("images/menu-btn.png");
        debugFont = new BitmapFont();

        float centerX = (TahuGame.GAME_WIDTH - 220) / 2;
        backButtonBounds = new Rectangle(centerX, 120, 220, 60);
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
        batch.draw(backButton, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height);
        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touch = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            if (backButtonBounds.contains(touch.x, touch.y)) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        backButton.dispose();
        debugFont.dispose();
    }
}