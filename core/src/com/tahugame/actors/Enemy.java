package com.tahugame.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tahugame.screens.GameScreen;

public class Enemy {
    private Texture texture;
    private float x, y;
    private int hp = 10;
    private int act = 0;
    private GameScreen screen;
    private boolean remove = false;

    public Enemy(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.x = x;
        this.y = y;
        texture = new Texture("images/Enemy.png");
    }

    public void update(float delta) {
        if (GameScreen.paused || GameScreen.freeze) return;

        act++;

        // Stage 3: Lebih aggresive!
        int shootInterval = GameScreen.stageLevel >= 3 ? 30 : 50;  // Stage 3 lebih cepat

        if (act % shootInterval == 0) {
            shootCircle();
        }

        if (act % 80 == 40) {
            shootAtPlayer();
        }

        // Stage 3: Tambahan wave pattern
        if (GameScreen.stageLevel >= 3 && act % 60 == 0) {
            shootWave();
        }
    }

    private void shootCircle() {
        int bulletCount = GameScreen.stageLevel >= 3 ? 16 : 12;  // Stage 3: 16 bullets

        for (int i = 0; i < bulletCount; i++) {
            double angle = (360.0 / bulletCount) * i + act;
            double rad = Math.toRadians(angle);
            float vx = (float) Math.cos(rad) * 3;
            float vy = (float) Math.sin(rad) * 3;
            screen.addEnemyBullet(new EnemyBullet(x, y, vx, vy));
        }
    }

    private void shootAtPlayer() {
        float px = screen.getPlayer().getX();
        float py = screen.getPlayer().getY();

        double angle = Math.atan2(py - y, px - x);

        // Stage 3: Shoot 3 bullets in spread pattern
        int spreadCount = GameScreen.stageLevel >= 3 ? 3 : 1;

        for (int i = 0; i < spreadCount; i++) {
            double spreadAngle = angle + Math.toRadians((i - 1) * 15);  // -15, 0, +15 degrees
            float speed = 4 + GameScreen.bulletSpeedScale();
            float vx = (float) Math.cos(spreadAngle) * speed;
            float vy = (float) Math.sin(spreadAngle) * speed;
            screen.addEnemyBullet(new EnemyBullet(x, y, vx, vy));
        }
    }

    private void shootWave() {
        // Wave pattern - bullets bergerak horizontal
        for (int i = 0; i < 5; i++) {
            float vx = -2 + i * 1;  // -2, -1, 0, 1, 2
            float vy = -3;
            screen.addEnemyBullet(new EnemyBullet(x, y, vx, vy));
        }
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

    public void render(SpriteBatch batch) {
        batch.draw(texture, x - texture.getWidth() / 2, y - texture.getHeight() / 2);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 20, y - 20, 40, 40);
    }
}
