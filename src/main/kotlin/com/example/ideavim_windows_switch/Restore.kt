package com.example.ideavim_windows_switch

import com.intellij.openapi.command.CommandAdapter
import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandListener
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.diagnostic.Logger
import com.maddyhome.idea.vim.extension.VimExtension
import org.apache.commons.lang.StringUtils

class Restore : VimExtension {
    private val IME_OFF_COMMAND_NAMES = arrayOf("Vim Exit Insert Mode")
    private val IME_ON_COMMAND_NAMES = arrayOf(
            "Vim Insert After Cursor", "Vim Insert After Line End", "Vim Insert Before Cursor",
            "Vim Insert Before First non-Blank", "Vim Insert Character Above Cursor",
            "Vim Insert Character Below Cursor",
            "Vim Enter", "Vim Insert at Line Start", "Vim Insert New Line Above",
            "Vim Insert New Line Below", "Vim Insert Previous Text", "Vim Insert Previous Text",
            "Vim Insert Register"
    )
    private var stateChangedListener: CommandListener? = null
    private val switcher = Switcher()
    private val log = Logger.getInstance("Restore")
    private var lastStatus = Switcher.InputMode.AlphaNumeric

    override fun getName() = "ime-switch-restore"

    override fun init() {
        stateChangedListener = stateChangedListener ?: createListener()
        CommandProcessor.getInstance().addCommandListener(stateChangedListener!!)
    }
    override fun dispose() {
        stateChangedListener ?: return
        CommandProcessor.getInstance().removeCommandListener(stateChangedListener!!)
        stateChangedListener = null
    }

    private fun createListener(): CommandListener = object : CommandAdapter() {
        override fun beforeCommandFinished(event: CommandEvent?) {
            super.beforeCommandFinished(event)
            val commandName = event?.commandName
            if (StringUtils.isBlank(commandName)) return
            if (IME_OFF_COMMAND_NAMES.contains(commandName)) {
                lastStatus = switcher.currentInputMode
                switcher.currentInputMode = Switcher.InputMode.AlphaNumeric
            } else if (IME_ON_COMMAND_NAMES.contains(commandName)) {
                switcher.currentInputMode = lastStatus
            }
        }
    }
}