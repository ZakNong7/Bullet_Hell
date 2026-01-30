package com.tahugame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tahugame.TahuGame;

public class MenuScreen extends BaseScreen {
    private Texture background;
    private Texture playButton;
    private Texture tutorialButton;
    private Texture creditButton;
    private Texture exitButton;  // Tambah ini
    private BitmapFont debugFont;

    private Rectangle playBounds;
    private Rectangle tutorialBounds;
    private Rectangle creditBounds;
    private Rectangle exitBounds;  // Tambah ini

    public MenuScreen(TahuGame game) {
        super(game);

        background = new Texture("images/menu.png");
        playButton = new Texture("images/main-btn.png");
        tutorialButton = new Texture("images/cara_main-btn.png");
        creditButton = new Texture("images/credit-btn.png");
        exitButton = new Texture("images/exit-btn.png");

        debugFont = new BitmapFont();
        debugFont.getData().setScale(1.2f);

        float centerX = (TahuGame.GAME_WIDTH - 220) / 2;

        playBounds = new Rectangle(centerX, 435, 220, 60);
        tutorialBounds = new Rectangle(centerX, 345, 220, 60);
        creditBounds = new Rectangle(centerX, 255, 220, 60);
        exitBounds = new Rectangle(centerX, 165, 220, 60);

        // Fix: Recreate music kalau null atau sudah disposed
        if (TahuGame.menuMusic == null) {
            TahuGame.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.mp3"));
            TahuGame.menuMusic.setLooping(true);
            TahuGame.menuMusic.setVolume(0.7f);
            TahuGame.menuMusic.play();
        } else if (!TahuGame.menuMusic.isPlaying()) {
            // Kalau music ada tapi tidak playing, play lagi
            TahuGame.menuMusic.play();
        }
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

        batch.draw(playButton, playBounds.x, playBounds.y, playBounds.width, playBounds.height);
        batch.draw(tutorialButton, tutorialBounds.x, tutorialBounds.y, tutorialBounds.width, tutorialBounds.height);
        batch.draw(creditButton, creditBounds.x, creditBounds.y, creditBounds.width, creditBounds.height);
        batch.draw(exitButton, exitBounds.x, exitBounds.y, exitBounds.width, exitBounds.height);  // Draw exit button

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touch = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            if (playBounds.contains(touch.x, touch.y)) {
                TahuGame.stopMenuMusic();
                game.setScreen(new GameScreen(game));
                dispose();
            } else if (tutorialBounds.contains(touch.x, touch.y)) {
                game.setScreen(new TutorialScreen(game));
                dispose();
            } else if (creditBounds.contains(touch.x, touch.y)) {
                game.setScreen(new CreditScreen(game));
                dispose();
            } else if (exitBounds.contains(touch.x, touch.y)) {
                // Exit game
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        playButton.dispose();
        tutorialButton.dispose();
        creditButton.dispose();
        exitButton.dispose();  // Dispose exit button
        debugFont.dispose();
    }
}