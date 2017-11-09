package com.scarlatti;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.JBCheckboxMenuItem;

/**
 * ~     _____                                    __
 * ~    (, /  |  /)                /)         (__/  )      /)        ,
 * ~      /---| // _ _  _  _ __  _(/ __ ___     / _ _  __ // _ _/_/_
 * ~   ) /    |(/_(//_)/_)(_(/ ((_(_/ ((_)   ) / (_(_(/ ((/_(_((_(__(_
 * ~  (_/                                   (_/
 * ~  Wednesday, 11/8/2017
 */
public class PopupAction extends AnAction {

    public PopupAction() {
        super("XSD Highlighting");
    }

    /**
     * A very simple experiment to try to show a message pane
     * when the action is performed.
     */
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        javax.swing.JOptionPane.showMessageDialog(null, "What!");
    }

    @Override
    public boolean displayTextInToolbar() {
        return true;
    }
}
