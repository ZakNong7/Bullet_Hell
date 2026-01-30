package com.tahugame.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tahugame.TahuGame;
import com.tahugame.screens.GameScreen;

public class PlayerBullet {
    private float x, y;
    private float width = 8;
    private float height = 16;
    private float speed = 12;
    private boolean remove = false;
    
    public PlayerBullet(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void update(float delta) {
        if (GameScreen.paused || GameScreen.freeze) return;
        
        float actualSpeed = GameScreen.slow ? speed * 0.5f : speed;
        y += actualSpeed;  // Positive Y = UP in LibGDX
        
        if (y > TahuGame.GAME_HEIGHT + 50) {
            remove = true;
        }
    }
    
    public void render(SpriteBatch batch) {
        // Draw simple colored rectangle for bullet
        batch.setColor(Color.CYAN);
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
