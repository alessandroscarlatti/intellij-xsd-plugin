package com.scarlatti;

import com.intellij.lang.Language;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nullable;

/**
 * ~     _____                                    __
 * ~    (, /  |  /)                /)         (__/  )      /)        ,
 * ~      /---| // _ _  _  _ __  _(/ __ ___     / _ _  __ // _ _/_/_
 * ~   ) /    |(/_(//_)/_)(_(/ ((_(_/ ((_)   ) / (_(_(/ ((/_(_((_(__(_
 * ~  (_/                                   (_/
 * ~  Monday, 10/30/2017
 */


public class XSDLanguage extends XMLLanguage {
    public static final XSDLanguage INSTANCE = new XSDLanguage();

    private XSDLanguage() {
        super("XSD");
    }

    @Nullable
    @Override
    public LanguageFileType getAssociatedFileType() {
        return XSDFileType.INSTANCE;
    }
}
