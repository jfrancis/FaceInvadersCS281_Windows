// John
package com.cs281.face.invaders;

import com.cs281.face.invaders.Sprite.BOUNDSACTION;

import android.os.Bundle;
import android.graphics.*;
import android.app.Activity;
import android.view.Menu;
import java.util.Random;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
     * Code from SpaceOut.cpp. TODO: Should this be in this class???
     */
	//-----------------------------------------------------------------
	// Global Variables
	//-----------------------------------------------------------------
	public static GameEngine gGame;
	public static Bitmap gSplashBitmap;
	public static Bitmap gDesertBitmap;
	public static Bitmap gCarBitmap;
	public static Bitmap gSmCarBitmap;
	public static Bitmap gMissileBitmap;
	public static Bitmap gBlobboBitmap;
	public static Bitmap gBMissileBitmap;
	public static Bitmap gJellyBitmap;
	public static Bitmap gJMissileBitmap;
	public static Bitmap gTimmyBitmap;
	public static Bitmap gSmExplosionBitmap;
	public static Bitmap gLgExplosionBitmap;
	public static Bitmap gGameOverBitmap;
	
	public static StarryBackground gBackground;
	public static Sprite gCarSprite;
	
	public static int gFireInputDelay;
	public static int gMovementDelay;
	public static int gNumLives, gScore, gDifficulty;
	public static boolean gGameOver;
	public static int gGameOverDelay;
	public static boolean gDemo;
	public static int gHiScores[] = new int[5];
	
	//Global variables added by Daniel Dudugjian
	//These serve as flags to modify the game when powerups are used.
	/*Spread shot powerup.
	Good music.
	Extra Life powerup.
	Invulnerability.
	Bouncing bullet powerup.
	Laser powerup.
	Shields.
	Explosive Missile.*/
	public static boolean gSpreadShot = false;
	public static boolean gBouncingBullet = false;
	public static boolean gWarpingBullet = false;
	public static boolean gPiercingBullet = false;
	public static boolean gExplosiveBullet = false;
	
	public final static void NewGame()
	{
		// Clear the sprites
		gGame.CleanupSprites();
		
		// Initialize the game variables
		gFireInputDelay = 0;
		gScore = 0;
		gNumLives = 3;
		gDifficulty = 80;
		gGameOver = false;
		
		if (gDemo)
		{
			// Add a few aliens to the demo
			for (int i = 0; i < 6; i++)
			{
				AddAlien();
			}
		}
		else
		{
			// Create the car sprite
			Rect rcBounds = new Rect(0, 0, 600, 450);
			gCarSprite = new Sprite(gCarBitmap, rcBounds, BOUNDSACTION.BA_WRAP);
			gCarSprite.SetPosition(300, 405);
			gGame.AddSprite(gCarSprite);
			
			// TODO: Play the background music
		}
		
		// Clear the sprites
		gGame.CleanupSprites();
	}
	
	public final static void AddAlien()
	{	  
		// Create a new random alien sprite
		Rect rcBounds = new Rect(0, 0, 600, 410);
		AlienSprite sprite;
		
		Random rand = new Random();
		switch(rand.nextInt(3))
		{
		case 0:
			// Blobbo
			sprite = new AlienSprite(gBlobboBitmap, rcBounds,
									 BOUNDSACTION.BA_BOUNCE);
			sprite.SetNumFrames(8, false);
			sprite.SetPosition(rand.nextInt(2) == 0 ? 0 : 600, rand.nextInt(370));
			sprite.SetVelocity(rand.nextInt(7) - 2,
							   rand.nextInt(7) - 2);
			break;
		case 1:
			// Jelly
			sprite = new AlienSprite(gJellyBitmap, rcBounds, 
									 BOUNDSACTION.BA_BOUNCE);
			sprite.SetNumFrames(8, false);
			sprite.SetPosition(rand.nextInt(600), rand.nextInt(370));
			sprite.SetVelocity(rand.nextInt(5) - 2,
							   rand.nextInt(5) + 3);
			break;
		case 2:
			// Timmy
			sprite = new AlienSprite(gTimmyBitmap, rcBounds, 
									 BOUNDSACTION.BA_WRAP);
			sprite.SetNumFrames(8, false);
			sprite.SetPosition(rand.nextInt(600), rand.nextInt(370));
			sprite.SetVelocity(rand.nextInt(7) + 3, 0);
			break;
		}
		
		gGame.AddSprite(sprite);
	}
}
