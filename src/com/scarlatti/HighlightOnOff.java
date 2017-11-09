package com.scarlatti;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageAnnotators;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.psi.PsiFile;

/**
 * ~     _____                                    __
 * ~    (, /  |  /)                /)         (__/  )      /)        ,
 * ~      /---| // _ _  _  _ __  _(/ __ ___     / _ _  __ // _ _/_/_
 * ~   ) /    |(/_(//_)/_)(_(/ ((_(_/ ((_)   ) / (_(_(/ ((/_(_((_(__(_
 * ~  (_/                                   (_/
 * ~  Wednesday, 11/8/2017
 */
public class HighlightOnOff extends ToggleAction {

    private boolean selected;
    private XSDAnnotator2 xsdAnnotator2;

    public HighlightOnOff() {
        super("Highlight On/Off");

        selected = false;
        xsdAnnotator2 = new XSDAnnotator2();
    }

    @Override
    public boolean isSelected(AnActionEvent anActionEvent) {
        return selected;
    }

    @Override
    public void setSelected(AnActionEvent anActionEvent, boolean b) {
        javax.swing.JOptionPane.showMessageDialog(null, "selected set to " + b);
        selected = b;

        // can I get which file this is?
        PsiFile file = anActionEvent.getData(LangDataKeys.PSI_FILE);
        System.out.println("file is " + file);

        if (selected) {
            LanguageAnnotators.INSTANCE.addExplicitExtension(XMLLanguage.INSTANCE, xsdAnnotator2);
        } else {
            LanguageAnnotators.INSTANCE.removeExplicitExtension(XMLLanguage.INSTANCE, xsdAnnotator2);
        }
    }

    @Override
    public boolean displayTextInToolbar() {
        return true;
    }
}
