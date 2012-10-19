package com.cs281.face.invaders;

import android.graphics.*;

public class Background {
	
	// Member Variables
	protected int mWidth;
	protected int mHeight;
	protected int mColor;
	protected Bitmap mBitmap;
	
	
	// Constructors
	public Background(int width, int height, int color)
	{
		mWidth = width;
		mHeight = height;
		mColor = color;
		mBitmap = null;
	}
	
	public Background(Bitmap bmp)
	{
		mColor = 0;
		mBitmap = bmp;
		mWidth = bmp.getWidth();
		mHeight = bmp.getHeight();
	}
	
	
	// General methods
	public void Update()
	{
		// Do nothing (the original code actually does nothing here!)
	}
	
	public void Draw(/*TODO: put some graphics handle object here*/
					 Object someGraphicsHandle)
	{
		// TODO: Implement graphics code
		
		// Draw the background
		if (mBitmap != null)
		{
			// TODO: Code to draw bitmap
		}
		else
		{
			// TODO: Code to draw filled rectangle
		}
	}
	
	
	// Accessor Methods
	
	// Declared "final" because Java defaults to virtual functions
	// and "final" indicates that a function is nonvirtual.
	// These functions were nonvirtual in the C++ source.
	public final int GetWidth()
	{ 
		return mWidth; 
	}
	
	public final int GetHeight()
	{
		return mHeight;
	}
}

