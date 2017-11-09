package com.scarlatti;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageAnnotators;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * ~     _____                                    __
 * ~    (, /  |  /)                /)         (__/  )      /)        ,
 * ~      /---| // _ _  _  _ __  _(/ __ ___     / _ _  __ // _ _/_/_
 * ~   ) /    |(/_(//_)/_)(_(/ ((_(_/ ((_)   ) / (_(_(/ ((/_(_((_(__(_
 * ~  (_/                                   (_/
 * ~  Wednesday, 11/8/2017
 */
public class PluginComponent implements ApplicationComponent {

    /**
     * Try to get the action manager, and register an action in it.
     */
    @Override
    public void initComponent() {
        System.out.println("initComponent called");
        ActionManager am = ActionManager.getInstance();
        PopupAction action = new PopupAction();
        HighlightOnOff toggle = new HighlightOnOff();

        // Passes an instance of your custom TextBoxes class to the registerAction method of the ActionManager class.
        am.registerAction("MyPluginAction", action);

        // Gets an instance of the WindowMenu action group.
        // "WindowMenu" is exact!
        DefaultActionGroup windowM = (DefaultActionGroup) am.getAction("ViewMenu");

        // Adds a separator and a new menu command to the WindowMenu group on the main menu.
        windowM.addSeparator();
        windowM.add(action);

        windowM.add(toggle);
    }

    /**
     * don't need to fill this one in
     */
    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        System.out.println("getting component name");
        return "XSDSyntaxHighlighter";
    }
}
