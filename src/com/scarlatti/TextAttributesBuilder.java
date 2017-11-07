package com.scarlatti;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;

import java.awt.*;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.NUMBER;

/**
 * ~     _____                                    __
 * ~    (, /  |  /)                /)         (__/  )      /)        ,
 * ~      /---| // _ _  _  _ __  _(/ __ ___     / _ _  __ // _ _/_/_
 * ~   ) /    |(/_(//_)/_)(_(/ ((_(_/ ((_)   ) / (_(_(/ ((/_(_((_(__(_
 * ~  (_/                                   (_/
 * ~  Monday, 11/6/2017
 */
public class TextAttributesBuilder {

    private TextAttributes textAttributes;

    public static final int FONT_STYLE_REGULAR = 0;
    public static final int FONT_STYLE_BOLD = 1;
    public static final int FONT_STYLE_ITALIC = 2;
    public static final int FONT_STYLE_BOLD_ITALIC = 3;

    protected TextAttributesBuilder() {};

    public static TextAttributesBuilder from(TextAttributesKey key) {
        TextAttributesBuilder builder = new TextAttributesBuilder();
        builder.setTextAttributes(builder.cloneTextAttributes(key));
        return builder;
    }

    public TextAttributesBuilder withForegroundColor(Color color) {
        textAttributes.setForegroundColor(color);
        return this;
    }


    public TextAttributesBuilder withBackgroundColor(Color color) {
        textAttributes.setBackgroundColor(color);
        return this;
    }


    public TextAttributesBuilder withEffectColor(Color color) {
        textAttributes.setEffectColor(color);
        return this;
    }


    public TextAttributesBuilder withStripeColor(Color color) {
        textAttributes.setErrorStripeColor(color);
        return this;
    }


    public TextAttributesBuilder withEffectType(EffectType effectType) {
        textAttributes.setEffectType(effectType);
        return this;
    }


    public TextAttributesBuilder withFontType(int fontType) {
        textAttributes.setFontType(fontType);
        return this;
    }

    public TextAttributes build() {
        return textAttributes;
    }

    /**
     * Clone text attributes.
     *
     * @param original the original text attributes
     * @return the cloned text attributes.
     */
    protected TextAttributes cloneTextAttributes(TextAttributesKey original) {
        TextAttributes textAttributes = new TextAttributes();
        textAttributes.setAttributes(
            NUMBER.getDefaultAttributes().getForegroundColor(),
            NUMBER.getDefaultAttributes().getBackgroundColor(),
            NUMBER.getDefaultAttributes().getEffectColor(),
            NUMBER.getDefaultAttributes().getErrorStripeColor(),
            NUMBER.getDefaultAttributes().getEffectType(),
            NUMBER.getDefaultAttributes().getFontType()
        );
        return textAttributes;
    }

    public TextAttributes getTextAttributes() {
        return textAttributes;
    }

    public void setTextAttributes(TextAttributes textAttributes) {
        this.textAttributes = textAttributes;
    }
}
