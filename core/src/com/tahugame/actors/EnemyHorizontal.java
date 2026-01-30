package com.tahugame.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tahugame.TahuGame;
import com.tahugame.screens.GameScreen;

public class EnemyHorizontal {
    private Texture texture;
    private float x, y;
    private float width, height;
    private int hp;
    private int timer = 0;
    private boolean remove = false;
    private GameScreen screen;
    private int direction = 1; // 1 for right, -1 for left
    private float speed = 3;

    public EnemyHorizontal(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.x = x;
        this.y = y;

        texture = new Texture("images/Enemy.png");
        width = texture.getWidth();
        height = texture.getHeight();

        hp = 20 + GameScreen.stageLevel * 5;
    }

    public void update(float delta) {
        if (GameScreen.paused || GameScreen.freeze) return;

        timer++;

        // Move horizontally
        x += direction * speed;

        if (x <= 40) {
            direction = 1;
            x = 40;
        }
        if (x >= TahuGame.GAME_WIDTH - 40) {
            direction = -1;
            x = TahuGame.GAME_WIDTH - 40;
        }

        // Shoot patterns
        if (timer % 20 == 0) {
            shootWave();
        }

        if (timer % 45 == 0) {
            shootAimed();
        }
    }

    private void shootWave() {
        int bullets = 12;
        int speed = 5 + GameScreen.bulletSpeedScale();

        if (GameScreen.stageLevel == 4) speed = 5;

        for (int i = 0; i < bullets; i++) {
            int angle = (i * 30) + (int) (Math.sin(timer * 0.1) * 45);
            screen.addEnemyBullet(new EnemyBullet(x, y, angle, speed));
        }
    }

    private void shootAimed() {
        for (int i = 0; i < 8; i++) {
            double angle = (360.0 / 8) * i + timer * 2;
            double rad = Math.toRadians(angle);

            // Stage 4: Bullet lebih cepat
            float speed = 2.5f;
            if (GameScreen.stageLevel == 4) speed = 4f;

            float vx = (float) Math.cos(rad) * speed;
            float vy = (float) Math.sin(rad) * speed;
            screen.addEnemyBullet(new EnemyBullet(x, y, vx, vy));
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x - width / 2, y - height / 2, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    public void hit() {
        hp--;
        if (hp <= 0) {
            remove = true;
        }
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void dispose() {
        texture.dispose();
    }
}
