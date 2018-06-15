package com.example.ideavim_windows_switch

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser

class Switcher {
    enum class InputMode {
        AlphaNumeric { override val value = 0.toLong() },
        Japanese { override val value = 1.toLong() };
        abstract val value: Long
    }

    var currentInputMode: InputMode
        get() {
            val hwnd = getCurrentWindowHandle()
            return getCurrentInputMode(hwnd)
        }
        set(f) {
            val hwnd = getCurrentWindowHandle()
            val himewnd = getCurrentImeWnd(hwnd)
            setCurrentInputMode(himewnd, f)
        }

    fun getCurrentWindowHandle(): WinDef.HWND {
        var hwnd = LibUser32.instance.GetActiveWindow()
        if (hwnd == Pointer.NULL) {
            val lpgui = WinUser.GUITHREADINFO()
            if (LibUser32.instance.GetGUIThreadInfo(WinDef.DWORD(0), lpgui) != WinDef.BOOL(false)) {
                hwnd = lpgui.hwndActive
            } else {
                throw RuntimeException("GetGUIThreadInfo() failed.")
            }
        }
        return hwnd
    }

    fun getCurrentImeWnd(hwnd: WinDef.HWND) = LibImm32.instance.ImmGetDefaultIMEWnd(hwnd)

    fun getCurrentInputMode(hwnd: WinDef.HWND): InputMode {
        val himc = LibImm32.instance.ImmGetContext(hwnd)
        return if (LibImm32.instance.ImmGetOpenStatus(himc) == WinDef.BOOL(true)) {
            InputMode.Japanese
        } else {
            InputMode.AlphaNumeric
        }
    }

    fun setCurrentInputMode(himewnd: WinDef.HWND, inputMode: Switcher.InputMode) {
        LibUser32.instance.SendMessageW(
                himewnd,
                WinDef.UINT(0x0283),    // WM_IME_CONTROL
                WinDef.WPARAM(0x0006),  // IMC_SETOPENSTATUS
                WinDef.LPARAM(inputMode.value)
        )
    }
}