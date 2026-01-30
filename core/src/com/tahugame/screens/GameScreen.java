package com.tahugame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tahugame.TahuGame;
import com.tahugame.actors.*;
import com.tahugame.utils.ScreenShake;

public class GameScreen extends BaseScreen {
    private Texture background;
    private Texture pauseOverlay;
    private Texture pauseButtonTex;
    private Texture slowButtonTex;
    private Texture resumeButtonTex;  // Tambahkan ini
    private Texture menuButtonTex;
    
    private Rectangle pauseButtonBounds;
    private Rectangle slowButtonBounds;
    private Rectangle resumeButtonBounds;
    private Rectangle menuButtonBounds;
    
    private Player player;
    private Array<Enemy> enemies;
    private Array<EnemyHorizontal> enemiesHorizontal;
    private Array<Boss> bosses;
    private Array<PlayerBullet> playerBullets;
    private Array<EnemyBullet> enemyBullets;
    
    private BitmapFont font;
    private BitmapFont debugFont;
    private GlyphLayout layout;
    
    public static boolean paused = false;
    public static boolean slow = false;
    public static boolean freeze = false;
    public static int level = 1;
    public static int stageLevel = 1;
    public static int slowEnergy = 500;
    public static final int MAX_SLOW = 500;
    
    private boolean showingStage = true;
    private int stageTimer = 0;
    private String stageText = "";
    
    private Music gameMusic;
    private Music bossMusic;
    private boolean gameOver = false;
    
    private int playerHealth = 100;
    private ScreenShake screenShake;
    
    private boolean slowFromButton = false;
    
    public GameScreen(TahuGame game) {
        super(game);

        background = new Texture("images/game.png");
        pauseButtonTex = new Texture("images/pause_btn.png");  // Tambahkan ini
        slowButtonTex = new Texture("images/slow_btn.png");    // Tambahkan ini
        pauseOverlay = new Texture("images/pause_overlay.png");
        resumeButtonTex = new Texture("images/resume_btn.png");  // Tambahkan ini
        menuButtonTex = new Texture("images/menu-btn.png");
        
        font = new BitmapFont();
        font.getData().setScale(2.5f);
        layout = new GlyphLayout();
        
        debugFont = new BitmapFont();
        debugFont.getData().setScale(1.2f);
        
        // Pause button 40x40 at top right
        pauseButtonBounds = new Rectangle(550, 750, 40, 40);
        
        // Slow button 40x40 at bottom right
        slowButtonBounds = new Rectangle(550, 10, 40, 40);
        
        // Pause menu buttons
        float centerX = (TahuGame.GAME_WIDTH - 220) / 2;
        resumeButtonBounds = new Rectangle(centerX, 370, 220, 60);
        menuButtonBounds = new Rectangle(centerX, 300, 220, 60);
        
        enemies = new Array<>();
        enemiesHorizontal = new Array<>();
        bosses = new Array<>();
        playerBullets = new Array<>();
        enemyBullets = new Array<>();
        
        screenShake = new ScreenShake();
        player = new Player(this, 300, 150);
        
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/game.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.7f);
        gameMusic.play();
        
        paused = false;
        slow = false;
        freeze = true;
        level = 1;
        stageLevel = 1;
        slowEnergy = MAX_SLOW;
        playerHealth = 100;
        
        showStageText("STAGE 1");
    }
    
    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        if (!gameOver) {
            handleInput();

            if (!paused) {
                if (showingStage) {
                    stageTimer++;
                    if (stageTimer >= 180) {
                        showingStage = false;
                        freeze = false;
                        spawnLevel();
                    }
                } else {
                    updateGame(delta);
                }

                handleSlowMo();
            }
        }

        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();  // ← Draw HARUS di dalam begin/end!

        batch.draw(background, 0, 0, TahuGame.GAME_WIDTH, TahuGame.GAME_HEIGHT);

        if (!gameOver) {
            for (EnemyBullet bullet : enemyBullets) {
                bullet.render(batch);
            }

            for (PlayerBullet bullet : playerBullets) {
                bullet.render(batch);
            }

            for (Enemy enemy : enemies) {
                enemy.render(batch);
            }

            for (EnemyHorizontal enemy : enemiesHorizontal) {
                enemy.render(batch);
            }

            for (Boss boss : bosses) {
                boss.render(batch);
            }

            player.render(batch, screenShake);

            drawUI();

            if (showingStage) {
                layout.setText(font, stageText);
                font.draw(batch, stageText,
                        (TahuGame.GAME_WIDTH - layout.width) / 2,
                        TahuGame.GAME_HEIGHT / 2 + 50);
            }

            // Draw buttons SETELAH background tapi SEBELUM pause overlay
            if (!paused) {
                batch.draw(pauseButtonTex, pauseButtonBounds.x, pauseButtonBounds.y,
                        pauseButtonBounds.width, pauseButtonBounds.height);
                batch.draw(slowButtonTex, slowButtonBounds.x, slowButtonBounds.y,
                        slowButtonBounds.width, slowButtonBounds.height);
            }

            // Pause overlay di paling atas
            if (paused) {
                float overlayX = (TahuGame.GAME_WIDTH - pauseOverlay.getWidth()) / 2;
                float overlayY = (TahuGame.GAME_HEIGHT - pauseOverlay.getHeight()) / 2;
                batch.draw(pauseOverlay, overlayX, overlayY);

                layout.setText(font, "PAUSED");
                font.draw(batch, "PAUSED",
                        (TahuGame.GAME_WIDTH - layout.width) / 2, 500);

                // Draw tombol resume & menu
                batch.draw(resumeButtonTex, resumeButtonBounds.x, resumeButtonBounds.y,
                        resumeButtonBounds.width, resumeButtonBounds.height);
                batch.draw(menuButtonTex, menuButtonBounds.x, menuButtonBounds.y,
                        menuButtonBounds.width, menuButtonBounds.height);
            }
        }

        batch.end();
    }
    
    private void updateGame(float delta) {
        if (!freeze) {
            player.update(delta);
            screenShake.update();
            
            for (int i = playerBullets.size - 1; i >= 0; i--) {
                PlayerBullet bullet = playerBullets.get(i);
                bullet.update(delta);
                if (bullet.shouldRemove()) playerBullets.removeIndex(i);
            }
            
            for (int i = enemyBullets.size - 1; i >= 0; i--) {
                EnemyBullet bullet = enemyBullets.get(i);
                bullet.update(delta);
                if (bullet.shouldRemove()) enemyBullets.removeIndex(i);
            }
            
            for (int i = enemies.size - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);
                enemy.update(delta);
                if (enemy.shouldRemove()) enemies.removeIndex(i);
            }
            
            for (int i = enemiesHorizontal.size - 1; i >= 0; i--) {
                EnemyHorizontal enemy = enemiesHorizontal.get(i);
                enemy.update(delta);
                if (enemy.shouldRemove()) enemiesHorizontal.removeIndex(i);
            }
            
            for (int i = bosses.size - 1; i >= 0; i--) {
                Boss boss = bosses.get(i);
                boss.update(delta);
                if (boss.shouldRemove()) bosses.removeIndex(i);
            }
            
            checkCollisions();
            
            if (enemies.size == 0 && enemiesHorizontal.size == 0 && bosses.size == 0 && !showingStage) {
                int recoverAmount = stageLevel * 5;
                playerHealth += recoverAmount;
                if (playerHealth > 100) playerHealth = 100;
                
                level++;
                
                if (level > 5) {
                    gameMusic.stop();
                    game.setScreen(new EndScreen(game, true));
                    dispose();
                    return;
                }
                
                freeze = true;
                stageTimer = 0;
                showingStage = true;
                
                if (level == 5) {
                    gameMusic.stop();
                    bossMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/boss.mp3"));
                    bossMusic.setLooping(true);
                    bossMusic.setVolume(0.7f);
                    bossMusic.play();
                    showStageText("WARNING!! BOSS");
                } else {
                    showStageText("STAGE " + level);
                }
            }
        }
    }
    
    private void checkCollisions() {
        for (int i = playerBullets.size - 1; i >= 0; i--) {
            PlayerBullet bullet = playerBullets.get(i);
            Rectangle bulletBounds = bullet.getBounds();
            
            boolean hit = false;
            
            for (Enemy enemy : enemies) {
                if (bulletBounds.overlaps(enemy.getBounds())) {
                    enemy.hit();
                    hit = true;
                    break;
                }
            }
            
            if (!hit) {
                for (EnemyHorizontal enemy : enemiesHorizontal) {
                    if (bulletBounds.overlaps(enemy.getBounds())) {
                        enemy.hit();
                        hit = true;
                        break;
                    }
                }
            }
            
            if (!hit) {
                for (Boss boss : bosses) {
                    if (bulletBounds.overlaps(boss.getBounds())) {
                        boss.hit();
                        hit = true;
                        break;
                    }
                }
            }
            
            if (hit && i < playerBullets.size) {
                playerBullets.removeIndex(i);
            }
        }
        
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet bullet = enemyBullets.get(i);
            if (bullet.getBounds().overlaps(player.getBounds())) {
                enemyBullets.removeIndex(i);
                playerHealth--;
                screenShake.shake(8);
                
                if (playerHealth <= 0) {
                    gameMusic.stop();
                    if (bossMusic != null) bossMusic.stop();
                    game.setScreen(new EndScreen(game, false));
                    dispose();
                    return;
                }
            }
        }
    }
    
    private void handleInput() {
        if (Gdx.input.justTouched()) {
            // CRITICAL FIX: Use viewport.unproject
            Vector2 touch = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            
            Gdx.app.log("GAME", "Touch: " + (int)touch.x + "," + (int)touch.y);
            
            if (pauseButtonBounds.contains(touch.x, touch.y)) {
                Gdx.app.log("GAME", "PAUSE");
                togglePause();
            } else if (paused) {
                if (resumeButtonBounds.contains(touch.x, touch.y)) {
                    Gdx.app.log("GAME", "RESUME");
                    togglePause();
                } else if (menuButtonBounds.contains(touch.x, touch.y)) {
                    Gdx.app.log("GAME", "MENU");
                    gameMusic.stop();
                    if (bossMusic != null) bossMusic.stop();
                    game.setScreen(new MenuScreen(game));
                    dispose();
                }
            }
        }
        
        if (!paused && Gdx.input.isTouched()) {
            Vector2 touch = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            slowFromButton = slowButtonBounds.contains(touch.x, touch.y);
        } else {
            slowFromButton = false;
        }
    }
    
    private void handleSlowMo() {
        slow = false;
        if (paused || freeze) return;
        
        if (slowFromButton && slowEnergy >= 50) {
            slow = true;
            slowEnergy -= stageLevel;
        }
        
        if (slowEnergy < 0) slowEnergy = 0;
        if (!slow && slowEnergy < MAX_SLOW) slowEnergy += 1;
    }
    
    private void togglePause() {
        paused = !paused;
        if (paused) {
            gameMusic.pause();
            if (bossMusic != null) bossMusic.pause();
        } else {
            gameMusic.play();
            if (bossMusic != null) bossMusic.play();
        }
    }
    
    private void spawnLevel() {
        stageLevel = level;
        
        if (level >= 1 && level <= 3) {
            enemies.add(new Enemy(this, 300, 680));
        } else if (level == 4) {
            enemiesHorizontal.add(new EnemyHorizontal(this, 300, 680));
        } else if (level == 5) {
            bosses.add(new Boss(this, 300, 680));
        }
    }
    
    private void showStageText(String text) {
        this.stageText = text;
        freeze = true;
        showingStage = true;
        stageTimer = 0;
    }
    
    private void drawUI() {
        font.draw(batch, "HP: " + playerHealth, 20, 770);
        
        float barX = 430;
        float barY = 25;
        float barWidth = 100;
        float barHeight = 10;
        float energyPercent = (float) slowEnergy / MAX_SLOW;
        
        batch.setColor(0.3f, 0.3f, 0.3f, 1f);
        batch.draw(background, barX, barY, barWidth, barHeight, 0, 0, 1, 1);
        
        batch.setColor(0f, 1f, 1f, 1f);
        batch.draw(background, barX, barY, barWidth * energyPercent, barHeight, 0, 0, 1, 1);
        
        batch.setColor(1f, 1f, 1f, 1f);
    }
    
    public void addPlayerBullet(PlayerBullet bullet) {
        playerBullets.add(bullet);
    }
    
    public void addEnemyBullet(EnemyBullet bullet) {
        enemyBullets.add(bullet);
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public static int bulletSpeedScale() {
        switch (stageLevel) {
            case 1: return 0;
            case 2: return 0;
            case 3: return 1;
            case 4: return 2;
            case 5: return 6;
        }
        return 0;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        pauseOverlay.dispose();
        font.dispose();
        debugFont.dispose();
        gameMusic.dispose();
        if (bossMusic != null) bossMusic.dispose();
        pauseButtonTex.dispose();
        slowButtonTex.dispose();
        resumeButtonTex.dispose();   // Tambahkan ini
        menuButtonTex.dispose();
    }
}
