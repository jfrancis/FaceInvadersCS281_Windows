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
private TCHAR               m_szWindowClass[32];
private TCHAR               m_szTitle[32];
//private WORD                m_wIcon, m_wSmallIcon;
private int                 m_iWidth, m_iHeight;
private int                 m_iFrameDelay;
private boolean                m_bSleep;
//private unsigned int                m_uiJoystickID;
//private RECT                m_rcJoystickTrip;
//private vector<Sprite*>     m_vSprites;
private unsigned int                m_uiMIDIPlayerID;

// Helper Methods
boolean               CheckSpriteCollision(Sprite* pTestSprite);

public:
// Constructor(s)/Destructor
       GameEngine(HINSTANCE hInstance, LPTSTR szWindowClass, LPTSTR szTitle,
         WORD wIcon, WORD wSmallIcon, int iWidth = 640, int iHeight = 480);

//virtual ~GameEngine(); - We don't need destructors since Java is garbage-collected

// General Methods
//static GameEngine*  GetEngine() { return m_pGameEngine; }; - note: Singleton pattern?
boolean                Initialize(int iCmdShow);
LRESULT             HandleEvent(HWND hWindow, UINT msg, WPARAM wParam,
                     LPARAM lParam);
void                ErrorQuit(LPTSTR szErrorMsg);
BOOL                InitJoystick();
void                CaptureJoystick();
void                ReleaseJoystick();
void                CheckJoystick();
void                AddSprite(Sprite* pSprite);
void                DrawSprites(HDC hDC);
void                UpdateSprites();
void                CleanupSprites();
Sprite*             IsPointInSprite(int x, int y);
void                PlayMIDISong(LPTSTR szMIDIFileName = TEXT(""),
                     BOOL bRestart = TRUE);
void                PauseMIDISong();
void                CloseMIDIPlayer();

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
