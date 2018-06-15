package com.example.ideavim_windows_switch

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef

interface LibImm32 : Library {
    companion object {
        val instance = Native.loadLibrary("imm32.dll", LibImm32::class.java) as LibImm32
    }
    fun ImmGetDefaultIMEWnd(hWnd: WinDef.HWND): WinDef.HWND
    fun ImmGetContext(hWnd: WinDef.HWND): WinDef.DWORD
    fun ImmGetOpenStatus(himc: WinDef.DWORD): WinDef.BOOL
}