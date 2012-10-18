package com.cs281.face.invaders;

import android.graphics.*;
import java.util.Random;

public class StarryBackground extends Background
{
	// Member Variables
	protected int mNumStars;
	protected int mTwinkleDelay;
	protected Point mPtStars[] = new Point[100];
	protected int mStarColors[] = new int[100];
	
	// Constructor
	public StarryBackground(int width, int height, int numStars,
	         				int twinkleDelay)
	{
		// Call base class constructor
		super(width, height, 0);
		
		mNumStars = Math.min(numStars, 100);
		mTwinkleDelay = twinkleDelay;
		
		Random rand = new Random();
		
		for (int i = 0; i < mNumStars; i++)
		{
			mPtStars[i].x = rand.nextInt(mWidth);
			mPtStars[i].y = rand.nextInt(mHeight);
			mStarColors[i] = Color.rgb(128, 128, 128);
		}
	}
	
	// General Methods
	@Override
	public void Update()
	{	
		// Randomly change the shade of the stars so that they twinkle
		
		Random rand = new Random();
		
		int iRGB;
		
		for (int i = 0; i < mNumStars; i++)
		{
			if (rand.nextInt(mTwinkleDelay) == 0)
			{
				iRGB = rand.nextInt(256);
				mStarColors[i] = Color.rgb(iRGB, iRGB, iRGB);
			}
		}
	}
	
	@Override
	public void Draw(/*TODO: put some graphics handle object here*/
					 Object someGraphicsHandle)
	{
		// TODO
	}
}