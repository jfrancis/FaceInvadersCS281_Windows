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
		return false; // TODO: Fill in
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
		// TODO: Fill in
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
