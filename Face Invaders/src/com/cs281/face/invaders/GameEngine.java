//Kevin Zeillmann
//John Francis
//Amber Shindhelm
//Last changed: 10/27/12


package com.cs281.face.invaders;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.graphics.*;

import java.util.Iterator;
import java.util.Vector;

//-----------------------------------------------------------------
//GameEngine Class
//-----------------------------------------------------------------
public class GameEngine
{

	// Member Variables
	public int m_iWidth, m_iHeight;
	public int m_iFrameDelay;
	public boolean  m_bSleep;
	
	private Vector<Sprite> m_vSprites = new Vector<Sprite>();
	
	// Helper Methods
	protected boolean CheckSpriteCollision(Sprite testSprite)
	{
		int vectorSize = m_vSprites.size();//makes code slightly faster
		for(int i = 0; i < vectorSize; ++i)
		{
			if(testSprite == m_vSprites.get(i))
			{
				//do nothing
				//we don't care if an object collides with itself
			}
			else if(m_vSprites.get(i).TestCollision(testSprite))
			{
				return MainActivity.SpriteCollision(m_vSprites.get(i), testSprite);
			}
			else
			{
				//do nothing - we didn't collide
			}
		}
		
		return false;
	}
	
	// Constructor
	public GameEngine(int iWidth, int iHeight)
	{
		m_iWidth = iWidth;
		m_iHeight = iHeight;
		m_iFrameDelay = 50;   // 20 FPS default
		m_bSleep = true;
	}
	
	// General Methods
	public void ErrorQuit(String errorMsg)
	{
		// TODO: Fill in
	}
	
	public void AddSprite(Sprite sprite)
	{	
		// Add a sprite to the sprite vector
		if (sprite != null)
		{
			// See if there are sprites already in the sprite vector
			if (m_vSprites.size() > 0)
			{
				// Find a spot in the sprite vector to insert the sprite by 
				// its z-order
				int len = m_vSprites.size();
				for (int i = 0; i < len; i++)
				{
					Sprite current = m_vSprites.get(i);
					if (sprite.GetZOrder() < current.GetZOrder())
					{
						// Insert the sprite into the sprite vector
						m_vSprites.insertElementAt(sprite, i);
						return;
					}
				}
			}
			
			// The sprite's z-order is highest, so add it to the end of 
			// the vector
			m_vSprites.add(sprite);
		}
	}
	
	public void DrawSprites(Canvas canvas)
	{
		// TODO: Fill in
	}
	
	public void UpdateSprites()
	{
		// Update the sprites in the sprite vector
		Rect rcOldSpritePos;
		short saSpriteAction;

		int len = m_vSprites.size();
		for (int i = 0; i < len; i++)
		{
			Sprite current = m_vSprites.get(i);
			
			// Save the old sprite position in case we need to restore it
			rcOldSpritePos = current.GetPosition();
	
			// Update the sprite
			saSpriteAction = current.Update();
	
			// Handle the SA_ADDSPRITE sprite action
			if ((saSpriteAction & Sprite.SA_ADDSPRITE) > 0)
			    // Allow the sprite to add its sprite
			    AddSprite(current.AddSprite());
	
			// Handle the SA_KILL sprite action
			if ((saSpriteAction & Sprite.SA_KILL) > 0)
			{
			    // Notify the game that the sprite is dying
			    MainActivity.SpriteDying(current);
	
			    // Kill the sprite
			    m_vSprites.remove(i);
			    len--;
			    i--; // TODO: is this necessary?
			    
			    continue;
			}
	
			// See if the sprite collided with any others
			if (CheckSpriteCollision(current))
			{
			    // Restore the old sprite position
			    current.SetPosition(rcOldSpritePos);
			}
		}
	}
	
	public void CleanupSprites()
	{
		// TODO: Do we need this?
	}
	
	public Sprite IsPointInSprite(int x, int y)
	{
		// Iterate through our sprites vector
		// If we find a sprite that matches, we return it
		
		// Question: Should this be rewritten using iterators?
		// Notworthit.jpg
		int vectorSize = m_vSprites.size(); //makes code slightly faster
		for(int i = 0; i < vectorSize; ++i)
		{
			if(!m_vSprites.get(i).IsHidden() && 
					m_vSprites.get(i).IsPointInside(x,y))
				return m_vSprites.get(i);
		}
		
		return null;
	}
	
	/*public void                PlayMIDISong(LPTSTR szMIDIFileName = TEXT(""),
	                     BOOL bRestart = TRUE);
	public void                PauseMIDISong();
	public void                CloseMIDIPlayer();
	* TODO: Add Android sound methods
	*/
	
	public void SetFrameRate(int iFrameRate) 
	{
		m_iFrameDelay = 1000 / iFrameRate; 
	}

}