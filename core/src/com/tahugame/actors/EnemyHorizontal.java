package com.tahugame.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tahugame.TahuGame;
import com.tahugame.screens.GameScreen;

public class EnemyHorizontal {
    private Texture texture;
    private float x, y;
    private int hp = 15;
    private int act = 0;
    private GameScreen screen;
    private boolean remove = false;
    private int direction = 1;

    public EnemyHorizontal(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.x = x;
        this.y = y;
        texture = new Texture("images/Enemy.png");
    }

    public void update(float delta) {
        if (GameScreen.paused || GameScreen.freeze) return;

        act++;

        // Horizontal movement - lebih cepat di Stage 4
        float speed = 3f;
        x += direction * speed;

        if (x < 50 || x > TahuGame.GAME_WIDTH - 50) {
            direction *= -1;
        }

        // Stage 4: Shoot lebih sering
        if (act % 25 == 0) {  // Lebih cepat dari 40
            shootWave();
        }

        if (act % 35 == 0) {  // Lebih cepat dari 60
            shootSpiral();
        }

        // Stage 4: Tambahan aimed shots
        if (act % 45 == 0) {
            shootAtPlayer();
        }
    }

    private void shootWave() {
        // Wave pattern menuju player - 7 bullets
        for (int i = 0; i < 7; i++) {
            float angle = -90 + (i - 3) * 15;  // Spread dari -45 to +45 degrees
            double rad = Math.toRadians(angle);
            float speed = 4;
            float vx = (float) Math.cos(rad) * speed;
            float vy = (float) Math.sin(rad) * speed;
            screen.addEnemyBullet(new EnemyBullet(x, y, vx, vy));
        }
    }

    private void shootSpiral() {
        // Spiral pattern - 8 bullets
        for (int i = 0; i < 8; i++) {
            double angle = (360.0 / 8) * i + act * 3;
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

        // Shoot 5 bullets spread
        for (int i = 0; i < 5; i++) {
            double spreadAngle = angle + Math.toRadians((i - 2) * 10);
            float speed = 5;
            float vx = (float) Math.cos(spreadAngle) * speed;
            float vy = (float) Math.sin(spreadAngle) * speed;
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