// John
package com.cs281.face.invaders;

import android.os.Bundle;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

import com.cs281.face.invaders.Sprite.BOUNDSACTION;
import com.cs281.face.invaders.R;


//MainActivity class: Primary Android interaction class
//  Contains methods called to start, pause, resume, and end application,
//  as well as methods for user interaction and error handling
//  Also contains implementations of special game power-ups
//  and additional game play options

public class MainActivity extends Activity implements OnTouchListener {

	private RenderView mRenderView;
	
	static public int mScreenWidth;
	static public int mScreenHeight;
	static public int mGameHeight;
	static public int mGroundLevel;
	static public int mButtonAreaHeight = 100;
	
	// Primary Android Activity methods
	
    @Override
    // First start of application
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        					 WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        Display display = getWindowManager().getDefaultDisplay();
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();
        mGameHeight = mScreenHeight - mButtonAreaHeight;
        
        mRenderView = new RenderView(this);
        
        mContext = this.getApplicationContext();
        
        mRenderView.setOnTouchListener(this);
        
        setContentView(mRenderView);
        
        // Initialize the game
        GameInitialize();
        GameStart();        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    // Begin user interactions
    protected void onResume()
    {
    	super.onResume();
    	
    	GameActivate();
    	mRenderView.resume();
    }
    
    @Override
    // Will resume activity soon
    protected void onPause()
    {
    	super.onPause();
    	
    	GameDeactivate();
    	mRenderView.pause();
    }
    
    @Override
    // End applicaton
    protected void onDestroy()
    {
    	GameEnd();
    }
    
    public boolean onTouch(View v, MotionEvent event)
    {
    	if (event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		HandleKeys(event.getX(), event.getY());
    	}
    	
    	return true;
    }
    
    class RenderView extends SurfaceView implements Runnable {
    	private Thread mRenderThread = null;
    	private SurfaceHolder mHolder;
    	private volatile boolean mRunning = false;
    	
    	public RenderView(Context context)
    	{
    		super(context);
    		mHolder = getHolder();
    	}
    	
    	public void resume()
    	{
    		mRunning = true;
    		mRenderThread = new Thread(this);
    		mRenderThread.start();
    	}
    	
    	public void run()
    	{
    		while (mRunning)
    		{
    			if (!mHolder.getSurface().isValid())
    			{
    				continue;
    			}
    			
    			Canvas canvas = mHolder.lockCanvas();
    			GameCycle(canvas);
    			mHolder.unlockCanvasAndPost(canvas);
    		}
    	}
    	
    	public void pause()
    	{
    		mRunning = false;
    		while (true)
    		{
    			try
    			{
    				mRenderThread.join();
    			}
    			catch (InterruptedException e)
    			{
    				// retry
    			}
    		}
    	}
    }
    
    // Added to allow loading of Bitmaps. Could revise in future
    private static Context mContext;
    
    /**
     * Code from SpaceOut.cpp.
     */

	// Global Variables

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
	public static Bitmap gPowerUpBitmap;
	public static Bitmap gExplosionPowerBitmap;
	
	public static StarryBackground gBackground;
	public static Sprite gCarSprite;
	
	public static int gFireInputDelay;
	public static int gMovementDelay;
	public static int gNumLives, gScore, gDifficulty;
	public static boolean gGameOver;
	public static int gGameOverDelay;
	public static boolean gDemo;
	public static int gHiScores[] = new int[5];
	
	// Global variables added by Daniel Dudugjian
	// These serve as flags to modify the game when powerups are used.
	
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
	
	// counter for recurring missiles
	public static int gRecurringMissiles = 0;
	
	public final static boolean GameInitialize()
	{
		gGame = new GameEngine(); // TODO: Put in appropriate numbers here
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
		
		// Could create off-screen bitmap if graphics flicker
		
		// Load the Bitmaps
		gSplashBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													 R.drawable.splash);//ic_hamm);
		gDesertBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									 R.drawable.desert);//ic_hamm);
		gCarBitmap = BitmapFactory.decodeResource(mContext.getResources(),
												  R.drawable.car);
		gSmCarBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													R.drawable.ic_hamm);
		gMissileBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													  R.drawable.missile);
		gBlobboBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													 R.drawable.blobbo);
		gBMissileBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 						  			 R.drawable.bmissile);
		gJellyBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									R.drawable.jelly);
		gJMissileBitmap = BitmapFactory.decodeResource(mContext.getResources(),
									  				   R.drawable.jmissile);
		gTimmyBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									R.drawable.timmy);
		gTMissileBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									   R.drawable.tmissile);
		gSmExplosionBitmap = BitmapFactory.decodeResource(mContext.getResources(),
														  R.drawable.smexplosion);
		gLgExplosionBitmap = BitmapFactory.decodeResource(mContext.getResources(),
														  R.drawable.lgexplosion);
		gGameOverBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				 									   R.drawable.gameover);
		gPowerUpBitmap = BitmapFactory.decodeResource(mContext.getResources(),
													  R.drawable.powerup1);
		gExplosionPowerBitmap = BitmapFactory.decodeResource(
												       mContext.getResources(),
												       R.drawable.esexplosion);
		
		gBackground = new StarryBackground(mScreenWidth, mGameHeight, 100, 50);
		
		mGroundLevel = mGameHeight - gDesertBitmap.getHeight() / 2;
		
		// Start the game for demo mode
		gDemo = true;
		NewGame();
	}
	
	public final static void GameEnd()
	{
		// This line may be changed with future music additions
		gGame.CloseMIDIPlayer();
		
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
	
	public final static void GameActivate()
	{
		if (!gDemo)
		{
			// Resume the background music
			// TODO: Change with music edits
			gGame.PlayMIDISong();
		}
	}
	
	public final static void GameDeactivate()
	{
		if (!gDemo)
		{
			// Pause the background music
			// TODO: Change with music edits
			//gGame.PauseMIDISong();
		}
	}
	
	public final static void GamePaint(Canvas canvas)
	{
		// Draw the background
		gBackground.Draw(canvas);
		
		// Clear the button area
		Paint buttonAreaPaint = new Paint();
		buttonAreaPaint.setColor(Color.BLACK);
		canvas.drawRect(new Rect(0, mGameHeight, 
								 mScreenWidth, 
								 mGameHeight + mButtonAreaHeight),
								 buttonAreaPaint);	
		
		canvas.drawBitmap(gDesertBitmap, 0, 
						  mGameHeight - gDesertBitmap.getHeight(), null);
		
		// Draw the sprites
		gGame.DrawSprites(canvas);
		
		Paint textPaint = new Paint();
		textPaint.setTypeface(Typeface.DEFAULT);
		textPaint.setColor(Color.WHITE);
		
		if (gDemo)
		{
			// Draw the splash screen image
			canvas.drawBitmap(gSplashBitmap, 
						      mScreenWidth / 2 - gSplashBitmap.getWidth() / 2,
						      mGameHeight / 2 - gSplashBitmap.getHeight() / 2,
						      null);
			
			// Draw the hi scores
			int x = mScreenWidth / 2;
			int y = mGameHeight / 2 - gSplashBitmap.getHeight() / 2 + 235;
			
			textPaint.setTextAlign(Align.CENTER);
			
			final int hiScoreSpacing = 20; 
			
			for (int i = 0; i < 5; i++)
			{
				String text = String.format("%d", gHiScores[i]);
				canvas.drawText(text, x, y, textPaint);
				
				y += hiScoreSpacing;
			}
		}
		else // In game
		{
			// Draw the score
			String text = String.format("%d", gScore);
			textPaint.setTextAlign(Align.RIGHT);
			canvas.drawText(text, 25, 10, 
					        textPaint);
			
			// Draw the number of remaining lives (cars)
			for (int i = 0; i < gNumLives; i++)
			{
				canvas.drawBitmap(gSmCarBitmap, 25 + 
								  (gSmCarBitmap.getWidth() * i),
								  5, null);
			}
			
			// Draw the game over message if necessary
			if (gGameOver)
			{
				canvas.drawBitmap(gGameOverBitmap, 
						mScreenWidth / 2 - gGameOverBitmap.getWidth() / 2,
					    mGameHeight / 2 - gGameOverBitmap.getHeight() / 2,
					    null);
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
			GamePaint(canvas);
			
			if (--gGameOverDelay == 0)
			{
				// Stop the music and switch to demo mode
				// TODO: Change with music edits
				// gGame.PauseMIDISong();
				gDemo = true;
				NewGame();
			}
		}
	}
	
	// TODO: This method may need further changes for touch screen input
	public final static void HandleKeys(float x, float y)
	{
		final int missileVelocity = -13;
		
		if (!gGameOver && !gDemo)
		{
			// Move the car based upon left/right key presses
			Point ptVelocity = new Point(gCarSprite.GetVelocity());
			
			if ((++gMovementDelay > 2) && x < gCarSprite.mRcPosition.left && y > 400)
				///*LEFTKEYPRESSED*/false /*TODO: Replace*/)
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
			else if ((++gMovementDelay > 2) && x > gCarSprite.mRcPosition.left && y > 400)
				///*RIGHTKEYPRESSED*/false /*TODO: Replace*/)
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
			if ((++gFireInputDelay > 2)) 
				//&& /*SPACEPRESSED*/false /*TODO: Replace*/)
			{
				// Fires three sprites in different directions
				if (gSpreadShot)
				{
					// Create a new missile sprite
					Rect rcBounds = new Rect(0, 0, mScreenWidth, mGameHeight);
					Rect rcPos = gCarSprite.GetPosition();
					Sprite sprite = new Sprite(gMissileBitmap, rcBounds, 
											   BOUNDSACTION.BA_DIE);
					sprite.SetPosition(rcPos.left + 15,
									   gCarSprite.mRcPosition.top - 5);
					sprite.SetVelocity(0, missileVelocity);
					gGame.AddSprite(sprite);
					
					sprite = new Sprite(gMissileBitmap, rcBounds,
										BOUNDSACTION.BA_DIE);
					sprite.SetPosition(rcPos.left + 15,
									   gCarSprite.mRcPosition.top - 5);
					sprite.SetVelocity(-3, -4);
					gGame.AddSprite(sprite);
					
					sprite = new Sprite(gMissileBitmap, rcBounds,
										BOUNDSACTION.BA_DIE);
					sprite.SetPosition(rcPos.left + 15,
									   gCarSprite.mRcPosition.top - 5);
					sprite.SetVelocity(3, -4);
					gGame.AddSprite(sprite);
					
					// Play the missile (fire) sound
					// TODO: Android music code
					
					// Reset the input delay
					gFireInputDelay = 0;
				}
				// Bullet bounces in screen area
				else if (gBouncingBullet && gRecurringMissiles < 30)
				{
					// Create a new missile sprite
					Rect rcBounds = new Rect(0, 0, mScreenWidth, mGameHeight);
					Rect rcPos = gCarSprite.GetPosition();
					Sprite sprite = new Sprite(gMissileBitmap, rcBounds,
											   BOUNDSACTION.BA_BOUNCE);
					sprite.SetPosition(rcPos.left + 15,
									   gCarSprite.mRcPosition.top - 5);
					sprite.SetVelocity(0, missileVelocity);
					gGame.AddSprite(sprite);
					
					gRecurringMissiles++;
					// Play the missile (fire) sound
					// TODO: Android music code
					
					// Reset the input delay
					gFireInputDelay = 0;
				}
				// Bullet runs from bottom to top of screen continuously
				else if (gWarpingBullet && gRecurringMissiles < 30)
				{
					// Create a new missile sprite
					Rect rcBounds = new Rect(0, 0, mScreenWidth, mGameHeight);
					Rect rcPos = gCarSprite.GetPosition();
					Sprite sprite = new Sprite(gMissileBitmap, rcBounds,
											   BOUNDSACTION.BA_WRAP);
					sprite.SetPosition(rcPos.left + 15,
									   gCarSprite.mRcPosition.top - 5);
					sprite.SetVelocity(0, missileVelocity);
					gGame.AddSprite(sprite);
					
					gRecurringMissiles++;
					// Play the missile (fire) sound
					// TODO: Android music code
					
					// Reset the input delay
					gFireInputDelay = 0;
				}
				else
				{
					// Create a new standard missile sprite
					Rect rcBounds = new Rect(0, 0, mScreenWidth, mGameHeight);
					Rect rcPos = gCarSprite.GetPosition();
					Sprite sprite = new Sprite(gMissileBitmap, rcBounds,
											   BOUNDSACTION.BA_DIE);
					sprite.SetPosition(rcPos.left + 15,
									   gCarSprite.mRcPosition.top - 5);
					sprite.SetVelocity(0, missileVelocity);
					gGame.AddSprite(sprite);
					
					// Play the missile (fire) sound
					// TODO: Android music code
					
					// Reset the input delay
					gFireInputDelay = 0;
				}
			}
		}
		
		// Start a new game based upon an Enter (Return) key press
		//if (true)///*ENTERPRESSED*/false /*TODO: Replace code*/)
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
		
		Random random = new Random();
		
		if ((hitter == gExplosionPowerBitmap && (hittee == gBlobboBitmap ||
				hittee == gJellyBitmap || hittee == gTimmyBitmap)) ||
				(hittee == gExplosionPowerBitmap && (hitter == gBlobboBitmap ||
				hitter == gJellyBitmap || hitter == gTimmyBitmap)))
		{
			// Play the explosion sound
			// TODO: Android music code
			
			// Kill both sprites
			if (hitter == gExplosionPowerBitmap)
			{
				spriteHittee.Kill();
			}
			else
			{
				spriteHitter.Kill();
			}
			
			spriteHitter.Kill();
			spriteHittee.Kill();
			
			if (random.nextInt(6) == 4)
			{
				Rect rcBounds = new Rect(0, 0, mScreenWidth, mGameHeight);
				Rect rcPos = spriteHitter.GetPosition();
				Sprite sprite = new Sprite(gPowerUpBitmap, rcBounds,
										   BOUNDSACTION.BA_DIE);
				sprite.SetPosition(rcPos.left, rcPos.top);
				sprite.SetVelocity(0, 4);
				gGame.AddSprite(sprite);
			}
			
			// Update the score
			gScore += 25;
			gDifficulty = Math.max(80 - (gScore / 20), 20);
		}
		
		if ((hitter == gMissileBitmap && (hittee == gBlobboBitmap ||
			hittee == gJellyBitmap || hittee == gTimmyBitmap)) ||
			(hittee == gMissileBitmap && (hitter == gBlobboBitmap ||
			hitter == gJellyBitmap || hitter == gTimmyBitmap)))
		{
			// Play the small explosion sound
			// TODO: Android music code
			
			// Kill both sprites
			if (gPiercingBullet)
			{
				if (hitter == gMissileBitmap)
				{
					if (spriteHitter.mBoundsAction == BOUNDSACTION.BA_BOUNCE ||
						spriteHitter.mBoundsAction == BOUNDSACTION.BA_WRAP)
					{
						gRecurringMissiles--;
					}
					spriteHittee.Kill();
				}
				else
				{
					if (spriteHittee.mBoundsAction == BOUNDSACTION.BA_BOUNCE ||
						spriteHittee.mBoundsAction == BOUNDSACTION.BA_WRAP)
					{
						gRecurringMissiles--;
					}
					spriteHitter.Kill();
				}
			}
			else
			{
				if (hitter == gMissileBitmap)
				{
					if (spriteHitter.mBoundsAction == BOUNDSACTION.BA_BOUNCE ||
						spriteHitter.mBoundsAction == BOUNDSACTION.BA_WRAP)
					{
						gRecurringMissiles--;
					}
				}
				else
				{
					if (spriteHittee.mBoundsAction == BOUNDSACTION.BA_BOUNCE ||
						spriteHittee.mBoundsAction == BOUNDSACTION.BA_WRAP)
					{
						gRecurringMissiles--;
					}
				}
				spriteHitter.Kill();
				spriteHittee.Kill();
			}
		
			// Create a large explosion sprite at the alien's position
			Rect rcBounds = new Rect(0, 0, mScreenWidth, mGameHeight);
			Rect rcPos;
			
			if (gExplosiveBullet)
			{				
				if (hitter == gMissileBitmap)
				{
					rcPos = spriteHittee.GetPosition();
				}
				else
				{
					rcPos = spriteHitter.GetPosition();
				}
			
				Sprite sprite = new Sprite(gExplosionPowerBitmap, rcBounds,
										   BOUNDSACTION.BA_STOP);
				sprite.SetNumFrames(4, true);
				sprite.SetPosition(rcPos.left, rcPos.top);
				gGame.AddSprite(sprite);
			
			}
			else
			{
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
				
				sprite.SetNumFrames(8,  true);
				sprite.SetPosition(rcPos.left, rcPos.top);
				gGame.AddSprite(sprite);
			}
			
			if (random.nextInt(6) == 4)
			{
				rcPos = spriteHitter.GetPosition();
				Sprite sprite = new Sprite(gPowerUpBitmap, rcBounds, 
										   BOUNDSACTION.BA_DIE);
				sprite.SetPosition(rcPos.left, rcPos.right);
				sprite.SetVelocity(0, 4);
				gGame.AddSprite(sprite);
			}
			
			// Update the score
			gScore += 25;
			gDifficulty = Math.max(80 - (gScore / 20), 20);
		}
		
		// Power-up hitting car code
		if ((hitter == gCarBitmap && hittee == gPowerUpBitmap) ||
			(hitter == gPowerUpBitmap && hittee == gCarBitmap))
		{
			int powerupNumber = random.nextInt(6);
			
			if (powerupNumber == 0)
			{
				++gNumLives;
			} 
			else if (powerupNumber == 1)
			{
				gBouncingBullet = false;
				gWarpingBullet = false;
				gPiercingBullet = false;
				gExplosiveBullet = false;
				gSpreadShot = true;
			} 
			else if (powerupNumber == 2)
			{
				gSpreadShot = false;
				gWarpingBullet = false;
				gPiercingBullet = false;
				gExplosiveBullet = false;
				gBouncingBullet = true;
			}
			else if (powerupNumber == 3)
			{
				gSpreadShot = false;
				gBouncingBullet = false;
				gPiercingBullet = false;
				gExplosiveBullet = false;
				gWarpingBullet = true;
			}
			else if (powerupNumber == 4)
			{
				gSpreadShot = false;
				gBouncingBullet = false;
				gWarpingBullet = false;
				gExplosiveBullet = false;
				gPiercingBullet = true;
			}
			else if (powerupNumber == 5)
			{
				gSpreadShot = false;
				gBouncingBullet = false;
				gWarpingBullet = false;
				gPiercingBullet = false;
				gExplosiveBullet = true;
			}
			
			if (hitter == gPowerUpBitmap)
			{
				spriteHitter.Kill();
			}
			else
			{
				spriteHittee.Kill();
			}
		}
		
		// See if an alien missile has collided with the car
		if ((hitter == gCarBitmap && (hittee == gBMissileBitmap ||
			hittee == gJMissileBitmap || hittee == gTMissileBitmap)) ||
			(hittee == gCarBitmap && (hitter == gBMissileBitmap ||
			hitter == gJMissileBitmap || hitter == gTMissileBitmap)))
		{
			// Play the large explosion sound
			// TODO: Android music code
			
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
			Rect rcBounds = new Rect(0, 0, mScreenWidth, mGameHeight);
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
			gCarSprite.SetPosition(mScreenWidth / 2, mGroundLevel);
			
			// See if the game is over
			if (--gNumLives == 0)
			{
				// Play the game over sound
				// TODO: Android music code
				
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
				// TODO: Android music code
			}
			
			// Create a small explosion sprite at the missile's position
			Rect rcBounds = new Rect(0, 0, mScreenWidth, mGameHeight);
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
		
		gBouncingBullet = false;
		gWarpingBullet = false;
		gPiercingBullet = false;
		gExplosiveBullet = false;
		gSpreadShot = false;
		gRecurringMissiles = 0;
		
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
			Rect rcBounds = new Rect(0, 0, mScreenWidth, mGameHeight);
			gCarSprite = new Sprite(gCarBitmap, rcBounds, BOUNDSACTION.BA_WRAP);
			gCarSprite.SetPosition(mScreenWidth / 2, mGroundLevel);
			gGame.AddSprite(gCarSprite);
			
			gGame.PlayMIDISong();
		}
	}
	
	public final static void AddAlien()
	{	  
		// Create a new random alien sprite
		Rect rcBounds = new Rect(0, 0, mScreenWidth, mGroundLevel - 5);
		AlienSprite sprite = null;
		
		final int spawnHeightBound = 
				(mGameHeight - gDesertBitmap.getHeight()) / 4 * 3;
		
		Random rand = new Random();
		switch(rand.nextInt(3))
		{
		case 0:
			// Blobbo
			sprite = new AlienSprite(gBlobboBitmap, rcBounds,
									 BOUNDSACTION.BA_BOUNCE);
			sprite.SetNumFrames(8, false);
			sprite.SetPosition(rand.nextInt(2) == 0 ? 0 : mScreenWidth, 
						rand.nextInt(spawnHeightBound));
			sprite.SetVelocity(rand.nextInt(7) - 2,
							   rand.nextInt(7) - 2);
			break;
		case 1:
			// Jelly
			sprite = new AlienSprite(gJellyBitmap, rcBounds, 
									 BOUNDSACTION.BA_BOUNCE);
			sprite.SetNumFrames(8, false);
			sprite.SetPosition(rand.nextInt(2) == 0 ? 0 : mScreenWidth, 
					rand.nextInt(mGameHeight - gDesertBitmap.getHeight()));
			sprite.SetVelocity(rand.nextInt(5) - 2,
							   rand.nextInt(5) + 3);
			break;
		case 2:
			// Timmy
			sprite = new AlienSprite(gTimmyBitmap, rcBounds, 
									 BOUNDSACTION.BA_WRAP);
			sprite.SetNumFrames(8, false);
			sprite.SetPosition(rand.nextInt(2) == 0 ? 0 : mScreenWidth, 
					rand.nextInt(mGameHeight - gDesertBitmap.getHeight()));
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
	
	@Override
	public void onBackPressed() {
		GameEnd();
		finish();
	}
	
}
