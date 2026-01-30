package com.tahugame.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tahugame.screens.GameScreen;

public class Enemy {
    private Texture texture;
    private float x, y;
    private float width, height;
    private int hp;
    private int timer = 0;
    private boolean remove = false;
    private GameScreen screen;

    public Enemy(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.x = x;
        this.y = y;

        texture = new Texture("images/Enemy.png");
        width = texture.getWidth();
        height = texture.getHeight();

        hp = 25;
    }

    public void update(float delta) {
        if (GameScreen.paused || GameScreen.freeze) return;

        timer++;

        // Shoot pattern based on stage level
        if (timer % 30 == 0) {
            shootCircle();
        }

        if (GameScreen.stageLevel >= 2 && timer % 60 == 0) {
            shootAimed();
        }
    }

    private void shootCircle() {
        int bullets = 8 + GameScreen.stageLevel * 2;
        int speed = 4 + GameScreen.bulletSpeedScale();

        float baseSpeed = 3;
        if (GameScreen.stageLevel == 3) baseSpeed = 4.5f;
        if (GameScreen.stageLevel == 4) baseSpeed = 5f;


        for (int i = 0; i < bullets; i++) {
            int angle = (i * 360 / bullets) + timer;
            screen.addEnemyBullet(new EnemyBullet(x, y, angle, speed));
        }
    }

    private void shootAimed() {
        float px = screen.getPlayer().getX();
        float py = screen.getPlayer().getY();

        double angle = Math.atan2(py - y, px - x);

        // Stage 3 & 4: Bullet lebih cepat
        float speed = 4 + GameScreen.bulletSpeedScale();
        if (GameScreen.stageLevel >= 3) speed += 1.5f;

        float vx = (float) Math.cos(angle) * speed;
        float vy = (float) Math.sin(angle) * speed;
        screen.addEnemyBullet(new EnemyBullet(x, y, vx, vy));
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
