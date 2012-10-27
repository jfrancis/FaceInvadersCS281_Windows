// John
package com.cs281.face.invaders;

import android.os.Bundle;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import java.util.Random;

import com.cs281.face.invaders.Sprite.BOUNDSACTION;
import com.cs281.face.invaders.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this.getApplicationContext();
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    // Added to allow loading of Bitmaps. May be a better way
    private static Context mContext;
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
		gGame = new GameEngine(640, 480); // TODO: Put in appropriate numbers here
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
		
		// TODO: Create offscreen bitmap??? May need to if graphics flicker
		
		// Load the Bitmaps
		gSplashBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													 R.drawable.ic_hamm);
		gDesertBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									 R.drawable.ic_hamm);
		gCarBitmap = BitmapFactory.decodeResource(mContext.getResources(),
												  R.drawable.ic_hamm);
		gSmCarBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													R.drawable.ic_hamm);
		gMissileBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													  R.drawable.ic_hamm);
		gBlobboBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													 R.drawable.ic_hamm);
		gBMissileBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									   R.drawable.ic_hamm);
		gJellyBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									R.drawable.ic_hamm);
		gJMissileBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													   R.drawable.ic_hamm);
		gTimmyBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									R.drawable.ic_hamm);
		gTMissileBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									   R.drawable.ic_hamm);
		gSmExplosionBitmap = BitmapFactory.decodeResource(mContext.getResources(),
														  R.drawable.ic_hamm);
		gLgExplosionBitmap = BitmapFactory.decodeResource(mContext.getResources(),
														  R.drawable.ic_hamm);
		gGameOverBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									   R.drawable.ic_hamm);
		
		gBackground = new StarryBackground(600, 450, 100, 50);
		
		// Start the game for demo mode
		gDemo = true;
		NewGame();
	}
	
	public final static void GameEnd()
	{
		// TODO: This line may change for the Android platform
		//gGame.CloseMIDIPlayer();
		
		// TODO: Is this necessary since Java does Garbage Collection???
		gGame.CleanupSprites();
		
		// Save the hi scores
		WriteHiScores();
	}
	
	public void ErrorQuit(String errorMsg)
	{
		// Displays the error message in a message box
		AlertDialog dispError = new AlertDialog.Builder(this).create();  
		dispError.setTitle("Error");
		// Use below if necessary
		//dispError.setCancelable(false); // This blocks the 'BACK' button  
		dispError.setMessage(errorMsg);  
		dispError.setButton("OK", new DialogInterface.OnClickListener() {  
		    
		    public void onClick(DialogInterface dialog, int which) {  
		        dialog.dismiss();                      
		    }  
		});  
		dispError.show();
	}
	
	// TODO: Is this method necessary on the Android platform?
	public final static void GameActivate()
	{
		if (!gDemo)
		{
			// Resume the background music
			// TODO: Add Android sound code
			//gGame.PlayMIDISong("", false);
		}
	}
	
	// TODO: Is this method necessary on the Android platform?
	public final static void GameDeactivate()
	{
		if (!gDemo)
		{
			// Pause the background music
			// TODO: Add Android sound code
			//gGame.PauseMIDISong();
		}
	}
	
	// TODO: Add Android specific drawing code
	public final static void GamePaint(Canvas canvas)
	{
		// Draw the background
		gBackground.Draw(canvas);
		
		// Draw the desert bitmap
		canvas.drawBitmap(gDesertBitmap, 0, 371, null);
		
		// Draw the sprites
		gGame.DrawSprites(canvas);
		
		
		Paint textPaint = new Paint();
		textPaint.setTypeface(Typeface.DEFAULT);
		textPaint.setColor(Color.WHITE);
		
		if (gDemo)
		{
			// Draw the splash screen image
			canvas.drawBitmap(gSplashBitmap, 142, 20, null);
			
			// Draw the hi scores
			int x = 275;
			int y = 230;
			
			textPaint.setTextAlign(Align.CENTER);
			
			for (int i = 0; i < 5; i++)
			{
				String text = String.format("%d", gHiScores[i]);
				canvas.drawText(text, x, y, textPaint);
				
				y += 20;
			}
		}
		else
		{
			// Draw the score
			String text = String.format("%d", gScore);
			textPaint.setTextAlign(Align.RIGHT);
			canvas.drawText(text, 460, 0, textPaint);
			
			// Draw the number of remaining lives (cars)
			for (int i = 0; i < gNumLives; i++)
			{
				canvas.drawBitmap(gSmCarBitmap, 520 + 
								  (gSmCarBitmap.getWidth() * i),
								  10, null);
			}
			
			// Draw the game over message if necessary
			if (gGameOver)
			{
				canvas.drawBitmap(gGameOverBitmap, 170, 100, null);
			}
		}
	}
	
	public final static void GameCycle(Canvas canvas)
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
			
			GamePaint(canvas);
		}
		else
		{
			if (--gGameOverDelay == 0)
			{
				// Stop the music and switch to demo mode
				// TODO: Add Android sound code
				//gGame.PauseMIDISong();
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
				if (gSpreadShot)
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
		AlienSprite sprite = null;
		
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
		return false;
	}
	
	public final static boolean WriteHiScores()
	{
		// TODO: Android code to write hi scores to a file
		return false;
	}
	
	
}
