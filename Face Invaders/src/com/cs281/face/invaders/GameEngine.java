//Kevin Zeillmann
//Last changed: 10/17/12


//TODO: Fill out class

package com.cs281.face.invaders;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

//TODO: C++ version imports Sprite.h here

//C++ version notes joystick flags, but we ignore these

/*//-----------------------------------------------------------------
//Game Engine Function Declarations
//-----------------------------------------------------------------
BOOL GameInitialize(HINSTANCE hInstance);
void GameStart(HWND hWindow);
void GameEnd();
void GameActivate(HWND hWindow);
void GameDeactivate(HWND hWindow);
void GamePaint(HDC hDC);
void GameCycle();
void HandleKeys();
void MouseButtonDown(int x, int y, BOOL bLeft);
void MouseButtonUp(int x, int y, BOOL bLeft);
void MouseMove(int x, int y);
void HandleJoystick(JOYSTATE jsJoystickState);
BOOL SpriteCollision(Sprite* pSpriteHitter, Sprite* pSpriteHittee);
void SpriteDying(Sprite* pSprite);*/

//-----------------------------------------------------------------
//GameEngine Class
//-----------------------------------------------------------------
public class GameEngine
{

// Member Variables
	
//private static GameEngine*  m_pGameEngine;
//private HINSTANCE           m_hInstance;
//private HWND                m_hWindow;
//private TCHAR               m_szWindowClass[32];
//private TCHAR               m_szTitle[32];
//private WORD                m_wIcon, m_wSmallIcon;
private int                 m_iWidth, m_iHeight;
private int                 m_iFrameDelay;
private boolean                m_bSleep;
//private unsigned int                m_uiJoystickID;
//private RECT                m_rcJoystickTrip;
//private vector<Sprite*>     m_vSprites;
private int                m_uiMIDIPlayerID;

// Helper Methods
boolean               CheckSpriteCollision(Sprite* pTestSprite);

public:
// Constructor(s)/Destructor
       GameEngine(HINSTANCE hInstance, LPTSTR szWindowClass, LPTSTR szTitle,
         WORD wIcon, WORD wSmallIcon, int iWidth = 640, int iHeight = 480);

//virtual ~GameEngine(); - We don't need destructors since Java is garbage-collected

// General Methods
//static GameEngine*  GetEngine() { return m_pGameEngine; }; - note: Singleton pattern?
public boolean                Initialize(int iCmdShow);
//LRESULT is a messagehandler - not sure how we'll use it
//LRESULT             HandleEvent(HWND hWindow, UINT msg, WPARAM wParam,
//                     LPARAM lParam);
public void                ErrorQuit(LPTSTR szErrorMsg);
public boolean                InitJoystick();
public void                CaptureJoystick();
public void                ReleaseJoystick();
public void                CheckJoystick();
public void                AddSprite(Sprite* pSprite);
public void                DrawSprites(HDC hDC);
public void                UpdateSprites();
public void                CleanupSprites();
public Sprite*             IsPointInSprite(int x, int y);
public void                PlayMIDISong(LPTSTR szMIDIFileName = TEXT(""),
                     BOOL bRestart = TRUE);
public void                PauseMIDISong();
public void                CloseMIDIPlayer();

// Accessor Methods
HINSTANCE GetInstance() { return m_hInstance; };
HWND      GetWindow() { return m_hWindow; };
void      SetWindow(HWND hWindow) { m_hWindow = hWindow; };
LPTSTR    GetTitle() { return m_szTitle; };
//WORD      GetIcon() { return m_wIcon; };
//WORD      GetSmallIcon() { return m_wSmallIcon; };
int       GetWidth() { return m_iWidth; };
int       GetHeight() { return m_iHeight; };
int       GetFrameDelay() { return m_iFrameDelay; };
void      SetFrameRate(int iFrameRate) { m_iFrameDelay = 1000 /
           iFrameRate; };
boolean      GetSleep() { return m_bSleep; };
void      SetSleep(boolean bSleep) { m_bSleep = bSleep; };
};
