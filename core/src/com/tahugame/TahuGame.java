package com.tahugame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.tahugame.screens.MenuScreen;

public class TahuGame extends Game {
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 800;
    public static final String GAME_NAME = "Bullet Hell";

    public static TahuGame instance;
    public static Music menuMusic;

    @Override
    public void create() {
        instance = this;
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    public static void stopMenuMusic() {
        if (menuMusic != null && menuMusic.isPlaying()) {
            menuMusic.stop();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
        if (menuMusic != null) {
            menuMusic.dispose();
            menuMusic = null;  // Set ke null setelah dispose
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (menuMusic != null && menuMusic.isPlaying()) {
            menuMusic.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();
        // Cek apakah sedang di menu screen
        if (getScreen() instanceof MenuScreen && menuMusic != null) {
            menuMusic.play();
        }
    }
}