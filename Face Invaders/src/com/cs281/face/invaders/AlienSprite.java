package com.cs281.face.invaders;

import android.graphics.*;
import java.util.Random;


public class AlienSprite extends Sprite {
	
	// Constructor
	public AlienSprite(Bitmap bmp, Rect rcBounds, BOUNDSACTION baBoundsAction)
	{
		super(bmp, rcBounds, baBoundsAction);
	}
	
	
	// General Methods
	public short Update()
	{
		// Call the base sprite Update() method
		short saSpriteAction;
		saSpriteAction = super.Update();
		
		// See if the alien should fire a missile
		Random rand = new Random();
		if (rand.nextInt(MainActivity.gDifficulty / 2) == 0)
		{
			saSpriteAction |= SA_ADDSPRITE;
		}
		
		return saSpriteAction;
	}
	
	public Sprite AddSprite()
	{	
		// Create a new missile sprite
		Rect rcBounds = new Rect(0, 0, 640, 410);
		Rect rcPos = GetPosition();
		
		Sprite sprite = null;
		if (GetBitmap() == MainActivity.gBlobboBitmap)
		{
			// Blobbo missile
			sprite = new Sprite(MainActivity.gBMissileBitmap, rcBounds, 
								BOUNDSACTION.BA_DIE);
			sprite.SetVelocity(0, 7);
		}
		else if (GetBitmap() == MainActivity.gJellyBitmap)
		{
			// Jelly missile
			sprite = new Sprite(MainActivity.gJMissileBitmap, rcBounds, 
								BOUNDSACTION.BA_DIE);
			sprite.SetVelocity(0, 5);
		}
		else
		{
			// Timmy missile
			sprite = new Sprite(MainActivity.gTMissileBitmap, rcBounds, 
					            BOUNDSACTION.BA_DIE);
			sprite.SetVelocity(0, 3);
		}
		
		// Set the missile sprite's position and return it
		sprite.SetPosition(rcPos.left + (GetWidth() / 2), rcPos.bottom);
		
		return sprite;
	}
}
