package com.tahugame.utils;

import java.util.Random;

public class ScreenShake {
    private float shakeIntensity = 0;
    private float shakeDuration = 0;
    private Random random = new Random();
    private float offsetX = 0;
    private float offsetY = 0;
    
    public void shake(float intensity) {
        this.shakeIntensity = intensity;
        this.shakeDuration = 0.3f; // 0.3 seconds
    }
    
    public void update() {
        if (shakeDuration > 0) {
            offsetX = (random.nextFloat() - 0.5f) * 2 * shakeIntensity;
            offsetY = (random.nextFloat() - 0.5f) * 2 * shakeIntensity;
            
            shakeDuration -= 0.016f; // Approximate delta for 60fps
            shakeIntensity *= 0.9f; // Decay
            
            if (shakeDuration <= 0) {
                shakeIntensity = 0;
                shakeDuration = 0;
                offsetX = 0;
                offsetY = 0;
            }
        } else {
            offsetX = 0;
            offsetY = 0;
        }
    }
    
    public float getOffsetX() {
        return offsetX;
    }
    
    public float getOffsetY() {
        return offsetY;
    }
}
