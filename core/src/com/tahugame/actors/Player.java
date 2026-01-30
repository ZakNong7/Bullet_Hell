package com.tahugame.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tahugame.TahuGame;
import com.tahugame.screens.GameScreen;
import com.tahugame.utils.ScreenShake;

public class Player {
    private Texture texture;
    private float x, y;
    private float width, height;
    private int shootDelay = 2;
    private GameScreen screen;

    private float touchOffsetX = 0;
    private float touchOffsetY = 0;
    private boolean isDragging = false;

    public Player(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.x = x;
        this.y = y;

        texture = new Texture("images/char.png");
        width = texture.getWidth();
        height = texture.getHeight();
    }

    public void update(float delta) {
        if (!GameScreen.paused && !GameScreen.freeze) {
            move();
            shoot();
            handleTouchControl();
        }
    }

    private void move() {
        int speed = 6;

        // Keyboard controls (for desktop testing)
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) x -= speed;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) x += speed;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) y += speed;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) y -= speed;

        // Clamp position
        x = Math.max(20, Math.min(TahuGame.GAME_WIDTH - 20, x));
        y = Math.max(20, Math.min(TahuGame.GAME_HEIGHT - 20, y));
    }

    private void shoot() {
        if (GameScreen.freeze) return;

        shootDelay++;

        int rate = GameScreen.slow ? 20 : 15;
        if (shootDelay % rate == 2) {
            screen.addPlayerBullet(new PlayerBullet(x, y + 20));
        }
    }

    private void handleTouchControl() {
        if (Gdx.input.isTouched()) {
            Vector2 touch = screen.getViewport().unproject(
                    new Vector2(Gdx.input.getX(), Gdx.input.getY())
            );

            if (!isDragging) {
                // First touch - calculate offset dari touch ke player position
                touchOffsetX = x - touch.x;
                touchOffsetY = y - touch.y;
                isDragging = true;
            }

            // Move player dengan offset (jadi player ada DI ATAS jari, tidak tertutup)
            // Player akan 80-100px di atas posisi jari
            float targetX = touch.x + touchOffsetX;
            float targetY = touch.y + touchOffsetY;

            int speed = GameScreen.slow ? 3 : 5;

            x += (targetX - x) * 0.3f * speed;
            y += (targetY - y) * 0.3f * speed;

            // Clamp position - bisa ke seluruh layar
            x = Math.max(20, Math.min(TahuGame.GAME_WIDTH - 20, x));
            y = Math.max(20, Math.min(TahuGame.GAME_HEIGHT - 20, y));
        } else {
            isDragging = false;
        }
    }

    public void render(SpriteBatch batch, ScreenShake shake) {
        float shakeX = shake.getOffsetX();
        float shakeY = shake.getOffsetY();

        batch.draw(texture,
                x - width / 2 + shakeX,
                y - height / 2 + shakeY,
                width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 10, y - 10, 20, 20);
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void dispose() {
        texture.dispose();
    }
}