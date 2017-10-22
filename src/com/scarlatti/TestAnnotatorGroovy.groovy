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
public class TestAnnotatorGroovy implements Annotator {

    Closure annotateClosure

    public TestAnnotatorGroovy() {
        annotateClosure = { PsiElement e, AnnotationHolder h ->
            if (e instanceof XmlTag) {
                XmlTag xmlTag = (XmlTag) e;
                String name = xmlTag.getName();

                if (name != null && (
                        name.startsWith("xs:element") ||
                        name.startsWith("xs:simpleType") ||
                        name.startsWith("xs:complexType")
                )) {

                    if (xmlTag.getAttribute("name") != null) {
                        Annotation annotation1 = h.createInfoAnnotation(xmlTag.getAttribute("name").getValueElement().getTextRange(), null);
                        annotation1.setTextAttributes(DefaultLanguageHighlighterColors.KEYWORD);
                    }

                }

                if (name != null && (
                        name.startsWith("xs:complexType") ||
                        name.startsWith("xs:sequence") ||
                        name.startsWith("xs:restriction")
                )) {

                    int start = xmlTag.getSubTags()[0].getTextRange().getStartOffset();
                    int end = xmlTag.getSubTags()[xmlTag.getSubTags().length - 1].getTextRange().getEndOffset();

                    Annotation annotation1 = h.createInfoAnnotation(new TextRange(xmlTag.getTextRange().getStartOffset(), start), null);
                    annotation1.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);

                    Annotation annotation2 = h.createInfoAnnotation(new TextRange(end, xmlTag.getTextRange().getEndOffset()), null);
                    annotation2.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);

                }
            }
        }
    }


    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        annotateClosure(element, holder)
    }
}