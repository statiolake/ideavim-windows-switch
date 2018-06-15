package com.example.ideavim_windows_switch
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.platform.win32.WinUser

interface LibUser32 : Library {
    companion object {
        val instance = Native.loadLibrary("user32.dll", LibUser32::class.java) as LibUser32
    }
    fun GetActiveWindow(): HWND
    fun SendMessageW(hWnd: HWND, Msg: UINT, wParam: WPARAM, lParam: LPARAM): LRESULT
    fun GetGUIThreadInfo(idThread: DWORD, lpgui: WinUser.GUITHREADINFO): BOOL
}
