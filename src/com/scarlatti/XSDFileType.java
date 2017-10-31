package com.scarlatti;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.*;

import javax.swing.*;
/**
 * ~     _____                                    __
 * ~    (, /  |  /)                /)         (__/  )      /)        ,
 * ~      /---| // _ _  _  _ __  _(/ __ ___     / _ _  __ // _ _/_/_
 * ~   ) /    |(/_(//_)/_)(_(/ ((_(_/ ((_)   ) / (_(_(/ ((/_(_((_(__(_
 * ~  (_/                                   (_/
 * ~  Monday, 10/30/2017
 */


public class XSDFileType extends LanguageFileType {
    public static final XSDFileType INSTANCE = new XSDFileType();

    private XSDFileType() {
        super(XSDLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "XSD Xml Schema Definition";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Xml Schema Definition";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "xsd";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null;  // TODO could add an icon later
    }
}