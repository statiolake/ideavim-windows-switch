package com.example.ideavim_fcitx_switch

import com.intellij.openapi.command.CommandAdapter
import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandListener
import com.intellij.openapi.command.CommandProcessor
import com.maddyhome.idea.vim.extension.VimExtension
import org.apache.commons.lang.StringUtils

class Restore : VimExtension {
    private val FCITX_OFF_COMMAND_NAMES = arrayOf("Vim Exit Insert Mode")
    private val FCITX_ON_COMMAND_NAMES = arrayOf(
            "Vim Insert After Cursor", "Vim Insert After Line End", "Vim Insert Before Cursor",
            "Vim Insert Before First non-Blank", "Vim Insert Character Above Cursor",
            "Vim Insert Character Below Cursor",
            "Vim Enter", "Vim Insert at Line Start", "Vim Insert New Line Above",
            "Vim Insert New Line Below", "Vim Insert Previous Text", "Vim Insert Previous Text",
            "Vim Insert Register"
    )
    private var stateChangedListener: CommandListener? = null

    override fun getName() = "fcitx-switch-restore"

    override fun init() {
        stateChangedListener = stateChangedListener ?: createListener()
        CommandProcessor.getInstance().addCommandListener(stateChangedListener!!)
        switchOff()
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
            if (FCITX_OFF_COMMAND_NAMES.contains(commandName)) {
                switchOff()
            } else if (FCITX_ON_COMMAND_NAMES.contains(commandName)) {
                switchOn()
            }
        }
    }

    private fun switchOn()  = Runtime.getRuntime().exec("fcitx-remote -o")
    private fun switchOff() = Runtime.getRuntime().exec("fcitx-remote -c")
}