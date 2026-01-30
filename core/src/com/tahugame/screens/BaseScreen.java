package com.tahugame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tahugame.TahuGame;

public abstract class BaseScreen implements Screen {
    protected TahuGame game;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    
    public BaseScreen(TahuGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TahuGame.GAME_WIDTH, TahuGame.GAME_HEIGHT);
        
        viewport = new FitViewport(TahuGame.GAME_WIDTH, TahuGame.GAME_HEIGHT, camera);
    }
    
    public Viewport getViewport() {
        return viewport;
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    
    @Override
    public void pause() {}
    
    @Override
    public void resume() {}
    
    @Override
    public void hide() {}
    
    @Override
    public void dispose() {
        batch.dispose();
    }
}
