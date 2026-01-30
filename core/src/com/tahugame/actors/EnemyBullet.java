package com.tahugame.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tahugame.TahuGame;
import com.tahugame.screens.GameScreen;

public class EnemyBullet {
    private float x, y;
    private float width = 8;
    private float height = 8;
    private float speedX, speedY;
    private boolean remove = false;

    public EnemyBullet(float x, float y, float speedX, float speedY) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
    }
    
    public void update(float delta) {
        if (GameScreen.paused || GameScreen.freeze) return;
        
        float multiplier = GameScreen.slow ? 0.3f : 1.0f;
        
        x += speedX * multiplier;
        y += speedY * multiplier;
        
        // Remove if out of bounds
        if (x < -50 || x > TahuGame.GAME_WIDTH + 50 ||
            y < -50 || y > TahuGame.GAME_HEIGHT + 50) {
            remove = true;
        }
    }
    
    public void render(SpriteBatch batch) {
        // Draw simple colored circle/square for bullet
        batch.setColor(Color.RED);
        batch.draw(getWhitePixel(), x - width / 2, y - height / 2, width, height);
        batch.setColor(Color.WHITE);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }
    
    public boolean shouldRemove() {
        return remove;
    }
    
    private static Texture whitePixel;
    private static Texture getWhitePixel() {
        if (whitePixel == null) {
            whitePixel = new Texture("images/char.png"); // Reuse any texture for color drawing
        }
        return whitePixel;
    }
}
