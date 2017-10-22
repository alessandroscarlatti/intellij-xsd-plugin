package com.scarlatti;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

/**
 * Created by pc on 10/21/2017.
 */
public class TestAnnotatorJava implements Annotator {

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof XmlTag) {
            XmlTag xmlTag = (XmlTag) element;
            String name = xmlTag.getName();

            if (name != null && name.startsWith("xs:element")) {

                TextRange range = xmlTag.getTextRange();

                if (xmlTag.getSubTags().length > 0) {
                    int start = xmlTag.getSubTags()[0].getTextRange().getStartOffset();
                    int end = xmlTag.getSubTags()[xmlTag.getSubTags().length - 1].getTextRange().getEndOffset();

//                    Annotation annotation = holder.createInfoAnnotation(new TextRange(range.getStartOffset(), start), null);
//                    annotation.setTextAttributes(DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);  // what does PREDEFINED_SYMBOL do??

                    Annotation annotation1 = holder.createInfoAnnotation(xmlTag.getAttribute("name").getValueElement().getTextRange(), null);
                    annotation1.setTextAttributes(DefaultLanguageHighlighterColors.KEYWORD);

                } else {
//                    Annotation annotation = holder.createInfoAnnotation(range, null);
//                    annotation.setTextAttributes(DefaultLanguageHighlighterColors.KEYWORD);

                    Annotation annotation1 = holder.createInfoAnnotation(xmlTag.getAttribute("name").getValueElement().getTextRange(), null);
                    annotation1.setTextAttributes(DefaultLanguageHighlighterColors.KEYWORD);
                }
            }
        }
    }
}