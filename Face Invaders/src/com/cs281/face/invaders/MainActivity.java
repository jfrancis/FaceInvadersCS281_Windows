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
	public static Bitmap gTMissileBitmap;
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
	
	
	public final static boolean GameInitialize()
	{
		gGame = new GameEngine(/*TODO: Fill in parameters*/);
		if (gGame == null)
		{
			return false;
		}
		
		// Set the frame rate
		gGame.SetFrameRate(30);
		
		return true;
	}
	
	public final static void GameStart()
	{
		// Read the hi scores
		ReadHiScores();
		
		// TODO: Create offscreen bitmap???
		
		// TODO: Load all of the bitmaps, possibly using BitmapFactory
//		gSplashBitmap = BitmapFactory.
//		gDesertBitmap = BitmapFactory.
//		gCarBitmap = BitmapFactory.
//		gSmCarBitmap = BitmapFactory.
//		gMissileBitmap = BitmapFactory.
//		gBlobboBitmap = BitmapFactory.
//		gBMissileBitmap = BitmapFactory.
//		gJellyBitmap = BitmapFactory.
//		gJMissileBitmap = BitmapFactory.
//		gTimmyBitmap = BitmapFactory.
//		gTMissileBitmap = BitmapFactory.
//		gSmExplosionBitmap = BitmapFactory.
//		gLgExplosionBitmap = BitmapFactory.
//		gGameOverBitmap = BitmapFactory.
		
		gBackground = new StarryBackground(600, 450, 100, 50);
		
		// Start the game for demo mode
		gDemo = true;
		NewGame();
	}
	
	public final static void GameEnd()
	{
		// TODO: This line may change for the Android platform
		gGame.CloseMIDIPlayer();
		
		// TODO: Is this necessary since Java does Garbage Collection???
		gGame.CleanupSprites();
		
		// Save the hi scores
		WriteHiScores();
	}
	
	// TODO: Is this method necessary on the Android platform?
	public final static void GameActivate()
	{
		if (!gDemo)
		{
			// Resume the background music
			gGame.PlayMIDISong("", false);
		}
	}
	
	// TODO: Is this method necessary on the Android platform?
	public final static void GameDeactivate()
	{
		if (!gDemo)
		{
			// Pause the background music
			gGame.PauseMIDISong();
		}
	}
	
	// TODO: Add Android specific drawing code
	public final static void GamePaint()
	{
		// Draw the background
		gBackground.Draw(null);
		
		// Draw the desert bitmap
		// TODO: Draw gDesertBitmap
		
		// Draw the sprites
		gGame.DrawSprites(null);
		
		if (gDemo)
		{
			// Draw the splash screen image
			// TODO: Draw gSplahBitmap
			
			// Draw the hi scores
			// TODO: Android drawing code
		}
		else
		{
			// Draw the score
			// TODO: Android code
			
			// Draw the number of remaining lives (cars)
			for (int i = 0; i < gNumLives; i++)
			{
				// TODO: Android code
			}
			
			// Draw the game over message if necessary
			if (gGameOver)
			{
				// TODO: Android code
			}
		}
	}
	
	public final static void GameCycle()
	{
		if (!gGameOver)
		{
			if (!gDemo)
			{
				// Randomly add aliens
				Random rand = new Random();
				if (rand.nextInt(gDifficulty) == 0)
				{
					AddAlien();
				}
			}
			
			// Update the background
			gBackground.Update();
			
			// Update the sprites
			gGame.UpdateSprites();
			
			// TODO: Android specific drawing code
		}
		else
		{
			if (--gGameOverDelay == 0)
			{
				// Stop the music and switch to demo mode
				gGame.PauseMIDISong();
				gDemo = true;
				NewGame();
			}
		}
	}
	
	// TODO: This method is probably going to need a major Android overhaul
	//		 to properly handle touch screen input
	public final static void HandleKeys()
	{
		if (!gGameOver && !gDemo)
		{
			// Move the car based upon left/right key presses
			Point ptVelocity = new Point(gCarSprite.GetVelocity());
			
			if ((++gMovementDelay > 2) && LEFTKEYPRESSED /*TODO: Replace*/)
			{
				// Move left
				if (ptVelocity.x > 0)
				{
					ptVelocity.x = 0;
				}
				else
				{
					ptVelocity.x = -5;
				}
				
				gCarSprite.SetVelocity(ptVelocity);
				gMovementDelay = 0;
			}
			else if ((++gMovementDelay > 2) && RIGHTKEYPRESSED /*TODO: Replace*/)
			{
				// Move right
				if (ptVelocity.x < 0)
				{
					ptVelocity.x = 0;
				}
				else
				{
					ptVelocity.x = 5;
				}
				
				gCarSprite.SetVelocity(ptVelocity);
				gMovementDelay = 0;
			}
			
			// Fire missiles based upon button press
			if ((++gFireInputDelay > 6) && SPACEPRESSED /*TODO: Replace*/)
			{
				if (spreadShot)
				{
					// Create a new missile sprite
					Rect rcBounds = new Rect(0, 0, 600, 450);
					Rect rcPos = gCarSprite.GetPosition();
					Sprite sprite = new Sprite(gMissileBitmap, rcBounds, 
											   BOUNDSACTION.BA_DIE);
					sprite.SetPosition(rcPos.left + 15, 400);
					sprite.SetVelocity(0, -7);
					gGame.AddSprite(sprite);
					
					sprite = new Sprite(gMissileBitmap, rcBounds,
										BOUNDSACTION.BA_DIE);
					sprite.SetPosition(rcPos.left + 15, 400);
					sprite.SetVelocity(-3, -4);
					gGame.AddSprite(sprite);
					
					sprite = new Sprite(gMissileBitmap, rcBounds,
										BOUNDSACTION.BA_DIE);
					sprite.SetPosition(rcPos.left + 15, 400);
					sprite.SetVelocity(3, -4);
					gGame.AddSprite(sprite);
					
					// Play the missile (fire) sound
					// TODO: Android code
					
					// Reset the input delay
					gFireInputDelay = 0;
				}
				else if (gBouncingBullet)
				{
					// Create a new missile sprite
					Rect rcBounds = new Rect(0, 0, 600, 450);
					Rect rcPos = gCarSprite.GetPosition();
					Sprite sprite = new Sprite(gMissileBitmap, rcBounds,
											   BOUNDSACTION.BA_BOUNCE);
					sprite.SetPosition(rcPos.left + 15, 400);
					sprite.SetVelocity(0, -7);
					gGame.AddSprite(sprite);
					
					// Play the missile (fire) sound
					// TODO: Android code
					
					// Reset the input delay
					gFireInputDelay = 0;
				}
				else if (gWarpingBullet)
				{
					// Create a new missile sprite
					Rect rcBounds = new Rect(0, 0, 600, 450);
					Rect rcPos = gCarSprite.GetPosition();
					Sprite sprite = new Sprite(gMissileBitmap, rcBounds,
											   BOUNDSACTION.BA_WRAP);
					sprite.SetPosition(rcPos.left + 15, 400);
					sprite.SetVelocity(0, -7);
					gGame.AddSprite(sprite);
					
					// Play the missile (fire) sound
					// TODO: Android code
					
					// Reset the input delay
					gFireInputDelay = 0;
				}
				else
				{
					// Create a new missile sprite
					Rect rcBounds = new Rect(0, 0, 600, 450);
					Rect rcPos = gCarSprite.GetPosition();
					Sprite sprite = new Sprite(gMissileBitmap, rcBounds,
											   BOUNDSACTION.BA_DIE);
					sprite.SetPosition(rcPos.left + 15, 400);
					sprite.SetVelocity(0, -7);
					gGame.AddSprite(sprite);
					
					// Play the missile (fire) sound
					// TODO: Android code
					
					// Reset the input delay
					gFireInputDelay = 0;
				}
			}
		}
		
		// Start a new game based upon an Enter (Return) key press
		if(ENTERPRESSED /*TODO: Replace code*/)
		{
			if (gDemo)
			{
				// Switch out of demo mode to start a new game
				gDemo = false;
				NewGame();
			}
			else if (gGameOver)
			{
				// Start a new game
				NewGame();
			}
		}
	}
	
	public final static boolean SpriteCollision(Sprite spriteHitter,
												Sprite spriteHittee)
	{
		// See if a player missile and an alien have collided
		Bitmap hitter = spriteHitter.GetBitmap();
		Bitmap hittee = spriteHittee.GetBitmap();
		
		if ((hitter == gMissileBitmap && (hittee == gBlobboBitmap ||
			hittee == gJellyBitmap || hittee == gTimmyBitmap)) ||
			(hittee == gMissileBitmap && (hitter == gBlobboBitmap ||
			hitter == gJellyBitmap || hitter == gTimmyBitmap)))
		{
			// Play the small explosion sound
			// TODO: Android code
			
			// Kill both sprites
			if (gPiercingBullet)
			{
				if (hitter == gMissileBitmap)
				{
					spriteHittee.Kill();
				}
				else
				{
					spriteHitter.Kill();
				}
			}
			else
			{
				spriteHitter.Kill();
				spriteHittee.Kill();
			}
		
			// Create a large explosion sprite at the alien's position
			Rect rcBounds = new Rect(0, 0, 600, 450);
			Rect rcPos;
			
			if (hitter == gMissileBitmap)
			{
				rcPos = spriteHittee.GetPosition();
			}
			else
			{
				rcPos = spriteHitter.GetPosition();
			}
			
			Sprite sprite = new Sprite(gLgExplosionBitmap, rcBounds,
									   BOUNDSACTION.BA_STOP);
			sprite.SetNumFrames(8, true);
			sprite.SetPosition(rcPos.left, rcPos.top);
			gGame.AddSprite(sprite);
			
			// Update the score
			gScore += 25;
			gDifficulty = Math.max(80 - (gScore / 20), 20);
		}
		
		// See if an alien missile has collided with the car
		if ((hitter == gCarBitmap && (hittee == gBMissileBitmap ||
			hittee == gJMissileBitmap || hittee == gTMissileBitmap)) ||
			(hittee == gCarBitmap && (hitter == gBMissileBitmap ||
			hitter == gJMissileBitmap || hitter == gTMissileBitmap)))
		{
			// Play the large explosion sound
			// TODO: Android code
			
			// Kill the missile sprite
			if (hitter == gCarBitmap)
			{
				spriteHittee.Kill();
			}
			else
			{
				spriteHitter.Kill();
			}
			
			// Create a large explosion sprite at the car's position
			Rect rcBounds = new Rect(0, 0, 600, 480);
			Rect rcPos;
			
			if (hitter == gCarBitmap)
			{
				rcPos = spriteHitter.GetPosition();
			}
			else
			{
				rcPos = spriteHittee.GetPosition();
			}
			
			Sprite sprite = new Sprite(gLgExplosionBitmap, rcBounds,
									   BOUNDSACTION.BA_STOP);
			sprite.SetNumFrames(8, true);
			sprite.SetPosition(rcPos.left, rcPos.top);
			gGame.AddSprite(sprite);
			
			// Move the car back to the start
			gCarSprite.SetPosition(300, 405);
			
			// See if the game is over
			if (--gNumLives == 0)
			{
				// Play the game over sound
				// TODO: Android code
				
				gGameOver = true;
				gGameOverDelay = 150;
				
				// Update the hi scores
				UpdateHiScores();
			}
		}
		
		return false;
	}
	
	public final static void SpriteDying(Sprite sprite)
	{
		// See if an alien missile sprite is dying
		if (sprite.GetBitmap() == gBMissileBitmap ||
			sprite.GetBitmap() == gJMissileBitmap ||
			sprite.GetBitmap() == gTMissileBitmap)
		{
			// Play the small explosion sound
			if (!gDemo)
			{
				// TODO: Android code
			}
			
			// Create a small explosion sprite at the missile's position
			Rect rcBounds = new Rect(0, 0, 600, 450);
			Rect rcPos = sprite.GetPosition();
			sprite = new Sprite(gSmExplosionBitmap, rcBounds,
								BOUNDSACTION.BA_STOP);
			sprite.SetNumFrames(8, true);
			sprite.SetPosition(rcPos.left, rcPos.top);
			gGame.AddSprite(sprite);
		}
	}
	
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
	
	public final static void UpdateHiScores()
	{
		// See if the current score made the hi score list
		int i;
		for (i = 0; i < 5; i++)
		{
			if (gScore > gHiScores[i])
			{
				break;
			}
		}
		
		// Insert the current score into the hi score list
		if (i < 5)
		{
			for (int j = 4; j > i; j--)
			{
				gHiScores[j] = gHiScores[j - 1];
			}
			
			gHiScores[i] = gScore;
		}
	}
	
	public final static boolean ReadHiScores()
	{
		// TODO: Android code to read hi scores from a file
	}
	
	public final static boolean WriteHiScores()
	{
		// TODO: Android code to write hi scores to a file
	}
	
	
}
