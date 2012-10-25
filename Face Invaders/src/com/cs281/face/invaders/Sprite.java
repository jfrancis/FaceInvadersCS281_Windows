package com.cs281.face.invaders;

import android.graphics.*;
import java.util.Random;

public class Sprite {
	
	// SPRITEACTION definitions
	public static final short SA_NONE = 0x0000;
	public static final short SA_ADDSPRITE = 0x0001;
	public static final short SA_KILL = 0x0002;
	
	public enum BOUNDSACTION
	{
		BA_STOP,
		BA_WRAP,
		BA_BOUNCE,
		BA_DIE
	}

	
	// Member variables
	protected Bitmap mBitmap;
	protected int mNumFrames;
	protected int mCurFrame;
	protected int mFrameDelay;
	protected int mFrameTrigger;
	protected Rect mRcPosition = new Rect();
	protected Rect mRcCollision = new Rect();
	protected Point mPtVelocity = new Point();
	protected int mZOrder;
	protected Rect mRcBounds = new Rect();
	protected BOUNDSACTION mBoundsAction;
	protected boolean mHidden;
	protected boolean mDying;
	protected boolean mOneCycle;
	
	
	// Helper Methods
	protected final void UpdateFrame()
	{
		if ((mFrameDelay >= 0) && (--mFrameTrigger <= 0))
		{
			// Reset the frame trigger
			mFrameTrigger = mFrameDelay;
			
			// Increment the frame
			if (++mCurFrame >= mNumFrames)
			{
				// If it's a one-cycle frame animation, kill the sprite
				if (mOneCycle)
				{
					mDying = true;
				}
				else
				{
					mCurFrame = 0;
				}
			}
		}
	}
	
	protected void CalcCollisionRect()
	{
		int iXShrink = (mRcPosition.left - mRcPosition.right) / 12;
		int iYShrink = (mRcPosition.top - mRcPosition.bottom) / 12;
		mRcCollision.set(mRcPosition);
		mRcCollision.inset(-iXShrink, -iYShrink); // It appears that inset is
												  // equivalent to InflateRect
												  // when the parameters are
												  // negated. Need to 
												  // double-check this
	}
	
	
	// Constructors
	public Sprite(Bitmap bmp)
	{
		// Initialize the member variables
		mBitmap = bmp;
		mNumFrames = 1;
		mCurFrame = mFrameDelay = mFrameTrigger = 0;
		mRcPosition.set(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
		CalcCollisionRect();
		mPtVelocity.set(0, 0);
		mZOrder = 0;
		mRcBounds.set(0, 0, 640, 480); // TODO: These numbers will change 
									   // depending on screen resolution
		mBoundsAction = BOUNDSACTION.BA_STOP;
		mHidden = false;
		mDying = false;
		mOneCycle = false;
	}
	
	public Sprite(Bitmap bmp, Rect rcBounds, BOUNDSACTION boundsAction)
	{
		// Calculate a random position
		Random rand = new Random();
		int iXPos = rand.nextInt(rcBounds.right - rcBounds.left);
		int iYPos = rand.nextInt(rcBounds.bottom - rcBounds.top);
		
		// Initialize the member variables
		mBitmap = bmp;
		mNumFrames = 1;
		mCurFrame = mFrameDelay = mFrameTrigger = 0;
		mRcPosition.set(iXPos, iYPos, iXPos + mBitmap.getWidth(), 
						iYPos + mBitmap.getHeight());
		CalcCollisionRect();
		mPtVelocity.set(0, 0);
		mZOrder = 0;
		mRcBounds.set(rcBounds);
		mBoundsAction = boundsAction;
		mHidden = false;
		mDying = false;
		mOneCycle = false;
	}
	
	// TODO: It appears that the original code has a couple of mistakes in this
	// constructor. It is translated directly from the original code, including
	// the potential typos. May want to determine if these are in fact typos
	// and fix
	public Sprite(Bitmap bmp, Point ptPosition, Point ptVelocity, int zOrder,
				  Rect rcBounds, BOUNDSACTION boundsAction)
	{
		// Initialize the member variables
		mBitmap = bmp;
		mNumFrames = 1;
		mCurFrame = mFrameDelay = mFrameTrigger = 0;
		mRcPosition.set(ptPosition.x, ptPosition.y, 
						mBitmap.getWidth(), mBitmap.getHeight()); // TODO: may be incorrect,
						// why doesn't original code add the x and y to getWidth and getHeight
					    // as it does in the previous constructor? May be a typo in the original
						// code
		CalcCollisionRect();
		mPtVelocity.set(ptPosition.x, ptPosition.y); 
				     // TODO: Doesn't make any sense right now why original
					 // code assigns ptPosition to mPtVelocity instead of
					 // assigning ptVelocity to mPtVelocity. Probably another
					 // typo
		mZOrder = zOrder;
		mRcBounds.set(rcBounds);
		mBoundsAction = boundsAction;
		mHidden = false;
		mDying = false;
		mOneCycle = false;
	}
	
	
	// General Methods
	public short Update()
	{
		if (mDying)
		{
			return SA_KILL;
		}
		
		// Update the frame
		UpdateFrame();
		
		// Update the position
		Point ptNewPosition = new Point();
		Point ptSpriteSize = new Point();
		Point ptBoundsSize = new Point();
		ptNewPosition.x = mRcPosition.left + mPtVelocity.x;
		ptNewPosition.y = mRcPosition.top + mPtVelocity.y;
		ptSpriteSize.x = mRcPosition.right - mRcPosition.left;
		ptSpriteSize.y = mRcPosition.bottom - mRcPosition.top;
		ptBoundsSize.x = mRcBounds.right - mRcBounds.left;
		ptBoundsSize.y = mRcBounds.bottom - mRcBounds.top;
		
		// Check the bounds
		// Wrap?
		if (mBoundsAction == BOUNDSACTION.BA_WRAP)
		{
			if ((ptNewPosition.x + ptSpriteSize.x) < mRcBounds.left)
			{
				ptNewPosition.x = mRcBounds.right;
			}
			else if (ptNewPosition.x > mRcBounds.right)
			{
				ptNewPosition.x = mRcBounds.left - ptSpriteSize.x;
			}
			
			if ((ptNewPosition.y + ptSpriteSize.y)< mRcBounds.top)
			{
				ptNewPosition.y = mRcBounds.bottom;
			}
			else if (ptNewPosition.y > mRcBounds.bottom)
			{
				ptNewPosition.y = mRcBounds.top - ptSpriteSize.y;
			}
		}
		// Bounce?
		else if (mBoundsAction == BOUNDSACTION.BA_BOUNCE)
		{
			boolean bounce = false;
			Point ptNewVelocity = new Point(mPtVelocity);
			
			if (ptNewPosition.x < mRcBounds.left)
			{
				bounce = true;
				ptNewPosition.x = mRcBounds.left;
				ptNewVelocity.x = -ptNewVelocity.x;
			}
			else if ((ptNewPosition.x + ptSpriteSize.x) > mRcBounds.right)
			{
				bounce = true;
				ptNewPosition.x = mRcBounds.right - ptSpriteSize.x;
				ptNewVelocity.x = -ptNewVelocity.x;
			}
			
			if (ptNewPosition.y < mRcBounds.top)
			{
				bounce = true;
				ptNewPosition.y = mRcBounds.top;
				ptNewVelocity.y = -ptNewVelocity.y;
			}
			else if ((ptNewPosition.y + ptSpriteSize.y) > mRcBounds.bottom)
			{
				bounce = true;
				ptNewPosition.y = mRcBounds.bottom - ptSpriteSize.y;
				ptNewVelocity.y = -ptNewVelocity.y;
			}
			
			if (bounce)
			{
				SetVelocity(ptNewVelocity);
			}
		}
		// Die?
		else if (mBoundsAction == BOUNDSACTION.BA_DIE)
		{
			if ((ptNewPosition.x + ptSpriteSize.x) < mRcBounds.left ||
				ptNewPosition.x > mRcBounds.right ||
				(ptNewPosition.y + ptSpriteSize.y) < mRcBounds.top ||
				ptNewPosition.y > mRcBounds.bottom)
			{
				return SA_KILL;
			}
		}
		// Stop (default)
		else
		{
			if (ptNewPosition.x < mRcBounds.left ||
				ptNewPosition.x > (mRcBounds.right - ptSpriteSize.x))
			{
				ptNewPosition.x = 
						Math.max(mRcBounds.left,
								 Math.min(ptNewPosition.x,
										  mRcBounds.right - ptSpriteSize.x));
				SetVelocity(0, 0);
			}
			if (ptNewPosition.y < mRcBounds.top ||
				ptNewPosition.y > (mRcBounds.bottom - ptSpriteSize.y))
			{
				ptNewPosition.y = 
						Math.max(mRcBounds.top,
								 Math.min(ptNewPosition.y,
										  mRcBounds.bottom - ptSpriteSize.y));
				SetVelocity(0, 0);
			}
		}
		
		SetPosition(ptNewPosition);
		
		return SA_NONE;
	}
	
	public Sprite AddSprite()
	{
		return null;
	}
	
	public final void Draw(Canvas canvas)
	{
		
		if (mBitmap != null && !mHidden)
		{
			// Draw the appropriate frame, if necessary
			if (mNumFrames == 1)
			{
				canvas.drawBitmap(mBitmap, mRcPosition.left,
								  mRcPosition.right, null);
			}
			else
			{
				canvas.drawBitmap(mBitmap, 
						          new Rect(mCurFrame * GetWidth(), 0,
						        		   mCurFrame * GetWidth() + GetWidth(),
						        		   GetHeight()),
						          new Rect(mRcPosition.left, 
						        		   mRcPosition.top,
						        		   mRcPosition.left + GetWidth(),
						        		   mRcPosition.top + GetHeight()
						        		   ),
						          null);
			}
		}
	}
	
	public final boolean IsPointInside(int x, int y)
	{
		return mRcPosition.contains(x, y);
	}
	
	public final boolean TestCollision(Sprite testSprite)
	{
		Rect rcTest = testSprite.GetCollision();
		return mRcCollision.left <= rcTest.right &&
			   rcTest.left <= mRcCollision.right &&
			   mRcCollision.top <= rcTest.bottom &&
			   rcTest.top <= mRcCollision.bottom;
	}
	
	public final void Kill()
	{
		mDying = true;
	}
	
	
	// Accessor Methods
	public final Bitmap GetBitmap()
	{
		return mBitmap;
	}
	
	public final void SetNumFrames(int numFrames, boolean oneCycle)
	{
		// Set the number of frames and the one-cycle setting
		mNumFrames = numFrames;
		mOneCycle = oneCycle;
		
		// Recalculate the position
		Rect rect = GetPosition();
		rect.right = rect.left + ((rect.right - rect.left) / numFrames);
		SetPosition(rect);
	}
	
	public final void SetFrameDelay(int frameDelay)
	{
		mFrameDelay = frameDelay;
	}
	
	public final Rect GetPosition()
	{
		return mRcPosition;
	}
	
	public final void SetPosition(int x, int y)
	{
		mRcPosition.offset(x - mRcPosition.left, y - mRcPosition.top);
		CalcCollisionRect();
	}
	
	public final void SetPosition(Point ptPosition)
	{
		SetPosition(ptPosition.x, ptPosition.y);
	}
	
	public final void SetPosition(Rect rcPosition)
	{
		mRcPosition.set(rcPosition);
		CalcCollisionRect();
	}
	
	public final void OffsetPosition(int x, int y)
	{
		mRcPosition.offset(x, y);
		CalcCollisionRect();
	}
	
	public final Rect GetCollision()
	{
		return mRcCollision;
	}
	
	public final Point GetVelocity()
	{
		return mPtVelocity;
	}
	
	public final void SetVelocity(int x, int y)
	{
		mPtVelocity.x = x;
		mPtVelocity.y = y;
	}
	
	public final void SetVelocity(Point ptVelocity)
	{
		SetVelocity(ptVelocity.x, ptVelocity.y);
	}
	
	public final int GetZOrder() // The C++ code includes a boolean return type,
						   // even though the z order variable is an int.
						   // Was this a typo in the C++ code?
	{
		return mZOrder;
	}
	
	public final void SetZOrder(int zOrder)
	{
		mZOrder = zOrder;
	}
	
	public final void SetBounds(Rect rcBounds)
	{
		mRcBounds.set(rcBounds);
	}
	
	public final void SetBoundsAction(BOUNDSACTION ba)
	{
		mBoundsAction = ba;
	}
	
	public final boolean IsHidden()
	{
		return mHidden;
	}
	
	public final void SetHiden(boolean hidden)
	{
		mHidden = hidden;
	}
	
	public final int GetWidth()
	{
		return mBitmap.getWidth() / mNumFrames;
	}
	
	public final int GetHeight()
	{
		return mBitmap.getHeight();
	}
}
