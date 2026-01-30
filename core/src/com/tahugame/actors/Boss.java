package com.tahugame.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tahugame.TahuGame;
import com.tahugame.screens.GameScreen;

public class Boss {
    private Texture texture;
    private float x, y;
    private float width, height;
    private int phase = 1;
    private int hp = 30;
    private int timer = 0;
    private int rotation = 0;
    private boolean remove = false;
    private GameScreen screen;
    
    private int direction = 1; // 1 for right, -1 for left
    private float speed = 3;
    private static final float LEFT_BOUND = 40;
    private static final float RIGHT_BOUND = 560;
    
    public Boss(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.x = x;
        this.y = y;
        
        texture = new Texture("images/boss2.png");
        width = texture.getWidth();
        height = texture.getHeight();
        
        phase = 1;
        hp = 30;
    }
    
    public void update(float delta) {
        if (GameScreen.paused || GameScreen.freeze) return;
        
        timer++;
        rotation += 3 + phase * 2;
        
        moveHorizontal();
        
        if (phase == 1) {
            phaseOne();
        } else if (phase == 2) {
            phaseTwo();
        } else if (phase == 3) {
            phaseThree();
        }
        
        if (hp <= 0) {
            nextPhase();
        }
    }
    
    private void moveHorizontal() {
        x += direction * speed;
        
        if (x <= LEFT_BOUND) {
            direction = 1;
            x = LEFT_BOUND;
        }
        
        if (x >= RIGHT_BOUND) {
            direction = -1;
            x = RIGHT_BOUND;
        }
    }
    
    private void phaseOne() {
        rotatingRing(18, 6);
        waveCollapse();
    }
    
    private void phaseTwo() {
        rotatingRing(26, 7);
        waveCollapse();
        aimedPattern();
    }
    
    private void phaseThree() {
        rotatingRing(38, 8);
        chaosSpiral();
        wallLock();
    }
    
    private void rotatingRing(int bullets, int baseSpeed) {
        if (timer % 10 != 0) return;
        
        int speed = bossSpeed(baseSpeed);
        
        for (int i = 0; i < bullets; i++) {
            int angle = (i * 360 / bullets) + rotation;
            screen.addEnemyBullet(new EnemyBullet(x, y, angle, speed));
        }
    }
    
    private void aimedPattern() {
        int rate = 10;
        if (timer % rate != 0) return;
        
        Player player = screen.getPlayer();
        
        int dx = (int) (player.getX() - x);
        int dy = (int) (player.getY() - y);
        
        int baseAngle = (int) Math.toDegrees(Math.atan2(dy, dx));
        
        int bullets = 5;
        int spread = 12;
        
        int speed = 5 + GameScreen.bulletSpeedScale();
        
        for (int i = 0; i < bullets; i++) {
            int angle = baseAngle - spread * 2 + i * spread;
            screen.addEnemyBullet(new EnemyBullet(x, y, angle, speed));
        }
    }
    
    private void waveCollapse() {
        if (timer % 20 != 0) return;
        
        for (int i = 0; i < 18; i++) {
            int angle = i * 20 + (int) (Math.sin(timer * 0.2) * 60);
            screen.addEnemyBullet(new EnemyBullet(x, y, angle, 4));
        }
    }
    
    private void chaosSpiral() {
        if (timer % 2 != 0) return;
        
        screen.addEnemyBullet(new EnemyBullet(x, y, rotation, 6));
    }
    
    private void wallLock() {
        if (timer % 90 != 0) return;
        
        for (int px = 40; px < TahuGame.GAME_WIDTH; px += 20) {
            screen.addEnemyBullet(new EnemyBullet(px, y, 270, 3));
        }
    }
    
    private int bossSpeed(int base) {
        return base + phase * 2 + GameScreen.bulletSpeedScale();
    }
    
    private void nextPhase() {
        phase++;
        timer = 0;
        
        if (phase == 2) {
            hp = 35;
        } else if (phase == 3) {
            hp = 50;
        } else {
            // Boss defeated - game will handle win condition
            remove = true;
        }
    }
    
    public void render(SpriteBatch batch) {
        batch.draw(texture, x - width / 2, y - height / 2, width, height);
        
        // Draw health bar
        float barWidth = 200;
        float barHeight = 10;
        float barX = (TahuGame.GAME_WIDTH - barWidth) / 2;
        float barY = 750;
        
        // Background
        batch.setColor(0.3f, 0.3f, 0.3f, 1f);
        batch.draw(texture, barX, barY, barWidth, barHeight);
        
        // Health
        float maxHp = (phase == 1) ? 30 : (phase == 2) ? 35 : 50;
        float hpPercent = hp / maxHp;
        
        if (phase == 1) batch.setColor(0f, 1f, 0f, 1f);
        else if (phase == 2) batch.setColor(1f, 1f, 0f, 1f);
        else batch.setColor(1f, 0f, 0f, 1f);
        
        batch.draw(texture, barX, barY, barWidth * hpPercent, barHeight);
        batch.setColor(1f, 1f, 1f, 1f);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }
    
    public void hit() {
        hp--;
    }
    
    public boolean shouldRemove() {
        return remove;
    }
    
    public void dispose() {
        texture.dispose();
    }
}
