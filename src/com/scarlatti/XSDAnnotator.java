package com.scarlatti;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

/**
 * Created by pc on 10/21/2017.
 */
public class XSDAnnotator implements Annotator {

    public void doAnnotate(PsiElement e, AnnotationHolder h) {
        if (e instanceof XmlTag) {
            XmlTag xmlTag = (XmlTag) e;

            // highlight the type names appropriately
            highlightTypeNames(xmlTag, h);

            // gray out tag and subtags if applicable
            grayOutTagAndSubTagsIfApplicable(xmlTag, h);

            // highlight attribute names
            highlightAttributeNames(xmlTag, h);
        }
    }

    public void highlightTypeNames(XmlTag xmlTag, AnnotationHolder h) {
        String tagName = xmlTag.getName();

        // highlight simple type names and complex type names similarly
        if (
                tagName.endsWith("simpleType") ||
                tagName.endsWith("complexType")
        ) {

            // highlight name attribute
            if (xmlTag.getAttribute("name") != null) {
                Annotation annotation1 = h.createWeakWarningAnnotation(xmlTag.getAttribute("name").getValueElement().getTextRange(), null);
                annotation1.setTextAttributes(DefaultLanguageHighlighterColors.NUMBER);
            }

        }

        // highlight element names differently from attribute names
        if (tagName.endsWith("element")) {
            if (xmlTag.getAttribute("name") != null) {
                Annotation annotation1 = h.createWeakWarningAnnotation(xmlTag.getAttribute("name").getValueElement().getTextRange(), null);
                annotation1.setTextAttributes(DefaultLanguageHighlighterColors.KEYWORD);
            }
        }

        if (tagName.endsWith("attribute")) {
            if (xmlTag.getAttribute("name") != null) {
                Annotation annotation1 = h.createWeakWarningAnnotation(xmlTag.getAttribute("name").getValueElement().getTextRange(), null);
                annotation1.setTextAttributes(DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
            }
        }

        // highlight type attribute the same for elements and attribute nodes
        if (
            tagName.endsWith("element") ||
            tagName.endsWith("attribute")
        ) {

            if (xmlTag.getAttribute("type") != null) {
                Annotation annotation1 = h.createWeakWarningAnnotation(xmlTag.getAttribute("type").getValueElement().getTextRange(), null);
                annotation1.setTextAttributes(DefaultLanguageHighlighterColors.NUMBER);
            }

        }
    }

    public void grayOutTagAndSubTagsIfApplicable(XmlTag xmlTag, AnnotationHolder h) {
        String tagName = xmlTag.getName();
        if (!(
                tagName.endsWith("simpleType") ||
                tagName.endsWith("complexType") ||
                tagName.endsWith("element") ||
                tagName.endsWith("attribute")
        )) {

            if (xmlTag.getSubTags().length > 0) {

                XmlTag firstSubTag =  xmlTag.getSubTags()[0];
                XmlTag lastSubTag = xmlTag.getSubTags()[xmlTag.getSubTags().length - 1];

                int firstSubTagStartOffset = firstSubTag.getTextRange().getStartOffset();  // start this annotation from the beginning of the first subTag
                int lastSubtagEndOffset = lastSubTag.getTextRange().getEndOffset();       // end this annotation at the end of the last subTag

//                Annotation tagOpenHighlight = h.createInfoAnnotation(new TextRange(xmlTag.getTextRange().getStartOffset(), firstSubTagStartOffset), null);
//                tagOpenHighlight.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);

                Annotation tagFirstNameHighlight = h.createInfoAnnotation(xmlTag.getChildren()[1].getTextRange(), null);  // try to get xml tag name
                tagFirstNameHighlight.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);

//                Annotation tagCloseHighlight = h.createInfoAnnotation(new TextRange(lastSubtagEndOffset, xmlTag.getTextRange().getEndOffset()), null);
//                tagCloseHighlight.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);

                Annotation tagLastNameHighlight = h.createInfoAnnotation(xmlTag.getChildren()[xmlTag.getChildren().length - 2].getTextRange(), null);  // try to get xml tag name
                tagLastNameHighlight.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);

                return;
            } else {
                // this element does not have subtags so it's safe to gray out the whole thing
//                Annotation wholeTagHighlight = h.createInfoAnnotation(xmlTag.getTextRange(), null);
//                wholeTagHighlight.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);

                Annotation tagNameHighlight = h.createInfoAnnotation(xmlTag.getChildren()[1].getTextRange(), null);  // try to get xml tag name
                tagNameHighlight.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);

                return;
            }
        }

        // keep searching down the element tree
        for (XmlTag subTag : xmlTag.getSubTags()) {
            grayOutTagAndSubTagsIfApplicable(subTag, h);
        }
    }

    public void highlightAttributeNames(XmlTag xmlTag, AnnotationHolder h) {
        for (XmlAttribute attribute : xmlTag.getAttributes()) {
            // highlight attribute names if not "name" or "type"
            if (!(attribute.getName().equals("name") || attribute.getName().equals("type"))) {

                int start = attribute.getNameElement().getTextRange().getStartOffset();
                int end = attribute.getValueElement().getTextRange().getStartOffset();  // grab the =

                Annotation attributeHighlight = h.createInfoAnnotation(new TextRange(start, end), null);
                attributeHighlight.setTextAttributes(DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
            } else {
                // gray out attribute names if "name" or "type"
                int start = attribute.getNameElement().getTextRange().getStartOffset();
                int end = attribute.getValueElement().getTextRange().getStartOffset();  // grab the =

                Annotation attributeHighlight = h.createInfoAnnotation(new TextRange(start, end), null);
                attributeHighlight.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);
            }

            // highlight the attribute value if the attribute is "base"
            if (attribute.getName().equals("base")) {

                // start off by highlighting the whole value of the "base" attribute
                Annotation valHighlight = h.createInfoAnnotation(attribute.getValueElement().getTextRange(), null);
                valHighlight.setTextAttributes(DefaultLanguageHighlighterColors.NUMBER);

                // now look for a namespace element
                PsiElement[] children = attribute.getValueElement().getChildren();
                if (children.length > 2) {

                    PsiElement textElement = attribute.getValueElement().getChildren()[1];
                    int nsSeparatorIndex = textElement.getText().indexOf(':');

                    // if a namespace is present:
                    if (nsSeparatorIndex > 0) {

                        int nsStart = textElement.getTextRange().getStartOffset();
                        int nsEnd = nsStart + nsSeparatorIndex;

                        Annotation nsHighlight = h.createInfoAnnotation(new TextRange(nsStart, nsEnd), null);
                        nsHighlight.setTextAttributes(DefaultLanguageHighlighterColors.INSTANCE_FIELD);
                    }
                }
            }
        }
    }

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        doAnnotate(element,  holder);
    }
}