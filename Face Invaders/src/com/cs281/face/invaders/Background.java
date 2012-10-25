package com.cs281.face.invaders;

import android.graphics.*;
import android.graphics.Paint.Style;

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
	
	public void Draw(Canvas canvas)
	{
		// Draw the background
		if (mBitmap != null)
		{
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
		else
		{
			Rect rect = new Rect(0, 0, mWidth, mHeight);
			Paint paint = new Paint();
			paint.setColor(mColor);
			paint.setStyle(Style.FILL);
			canvas.drawRect(rect, paint);
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

