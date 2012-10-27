//Kevin Zeillmann
//Last changed: 10/17/12


//TODO: Fill out class

package com.cs281.face.invaders;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.graphics.*;
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
		// TODO: Fill in
	}
	
	// General Methods
	public boolean Initialize()
	{
		return false; //TODO: fill in
	}
	
	public void ErrorQuit(String errorMsg)
	{
		// TODO: Fill in
	}
	
	public void AddSprite(Sprite sprite)
	{
		// TODO: Fill in
	}
	
	public void DrawSprites(Canvas canvas)
	{
		// TODO: Fill in
	}
	
	public void UpdateSprites()
	{
		// TODO: Fill in
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
