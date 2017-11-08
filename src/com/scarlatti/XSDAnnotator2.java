package com.scarlatti;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.psi.xml.XmlToken;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*;
import static com.intellij.psi.xml.XmlTokenType.*;
import static com.scarlatti.TextAttributesBuilder.FONT_STYLE_BOLD;

public class XSDAnnotator2 implements Annotator {

    /**
     * Entry point to this annotator.  IntelliJ calls this method for each applicable PsiElement.
     *
     * @param element the PsiElement under evaluation
     * @param holder  the annotation holder for this element
     */
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        doAnnotate(element, holder);
    }

    /**
     * Helper method to highlight a text range
     *
     * @param textRange the text range to highlight.
     * @param style     the style to apply to the text range.
     * @param h         the annotation holder that will take the highlight
     */
    public void highlight(TextRange textRange, TextAttributesKey style, AnnotationHolder h) {
        Annotation highlight = h.createInfoAnnotation(textRange, null);
        highlight.setTextAttributes(style);
    }

    /**
     * Helper method to highlight a text range
     *
     * @param textRange the text range to highlight.
     * @param style     the style to apply to the text range.
     * @param h         the annotation holder that will take the highlight
     */
    public void highlightWithAttributes(TextRange textRange, TextAttributes style, AnnotationHolder h) {
        Annotation highlight = h.createInfoAnnotation(textRange, null);
        highlight.setEnforcedTextAttributes(style);
    }

    /**
     * Helper method to gray out the name(s) and tags
     * for a single element.
     *
     * @param xmlTag the element being grayed out.
     * @param h      the annotation holder for the element.
     */
    public void grayOutTag(XmlTag xmlTag, AnnotationHolder h) {
        for (PsiElement psi : xmlTag.getChildren()) {
            if (psi instanceof XmlToken) {
                XmlToken token = (XmlToken) psi;
                IElementType tokenType = token.getTokenType();
                if (
                    tokenType == XML_START_TAG_START ||
                        tokenType == XML_END_TAG_START ||
                        tokenType == XML_NAME ||
                        tokenType == XML_TAG_END ||
                        tokenType == XML_EMPTY_ELEMENT_END
                    ) {
                    highlight(token.getTextRange(), LINE_COMMENT, h);
                }
            }
        }
    }

    /**
     * Core highlighting process.
     *
     * @param e the element being processed.
     * @param h the annotation holder for the element.
     */
    public void doAnnotate(PsiElement e, AnnotationHolder h) {
        if (e instanceof XmlTag) {
            XmlTag xmlTag = (XmlTag) e;

            if (xmlTag.getNamespacePrefix().equals("xs")) {
                // highlight the type names appropriately
                highlightTypeNames(xmlTag, h);

                // gray out tag and subtags if applicable
                grayOutTagAndSubTagsIfApplicable(xmlTag, h);

                // highlight attribute names
                highlightAttributeNames(xmlTag, h);
            }
        }
    }

    /**
     * Highlight type names for simple and complex types.
     *
     * @param xmlTag the XmlTag to highlight.
     * @param h      the annotation holder for the element.
     */
    public void highlightTypeNames(XmlTag xmlTag, AnnotationHolder h) {
        String tagName = xmlTag.getName();

        // highlight simple type names and complex type names similarly
        if (
            tagName.equals("xs:simpleType") ||
                tagName.equals("xs:complexType")
            ) {

            // highlight name attribute
            if (xmlTag.getAttribute("name") != null) {
                highlightWithAttributes(xmlTag.getAttribute("name").getValueElement().getTextRange(),
                    TextAttributesBuilder.from(NUMBER).withFontType(FONT_STYLE_BOLD).build(), h);
            }
        }

        // highlight element names differently from attribute names
        if (tagName.equals("xs:element")) {
            if (xmlTag.getAttribute("name") != null)
                highlight(xmlTag.getAttribute("name").getValueElement().getTextRange(), KEYWORD, h);
        }

        if (tagName.equals("xs:attribute")) {
            if (xmlTag.getAttribute("name") != null)
                highlight(xmlTag.getAttribute("name").getValueElement().getTextRange(), VALID_STRING_ESCAPE, h);
        }

        // highlight type attribute the same for elements and attribute nodes
        if (
            tagName.equals("xs:element") ||
                tagName.equals("xs:attribute")
            ) {

            if (xmlTag.getAttribute("type") != null)
                highlight(xmlTag.getAttribute("type").getValueElement().getTextRange(), NUMBER, h);

            if (xmlTag.getAttribute("ref") != null)
                highlight(xmlTag.getAttribute("ref").getValueElement().getTextRange(), VALID_STRING_ESCAPE, h);
        }
    }

    /**
     * Gray out the given tag if it is not a primary level tag.
     *
     * @param xmlTag the tag under consideration.
     * @param h      the annotation holder for the tag.
     */
    public void grayOutTagAndSubTagsIfApplicable(XmlTag xmlTag, AnnotationHolder h) {
        String tagName = xmlTag.getName();

        if (
            tagName.equals("xs:complexType") ||
                tagName.equals("xs:simpleType") ||
                tagName.equals("xs:enumeration")
            ) {
            // gray out types not defined with names
            if (xmlTag.getAttribute("name") == null) {
                grayOutTag(xmlTag, h);
            }
        }

        if (!(
            tagName.equals("xs:simpleType") ||
                tagName.equals("xs:complexType") ||
                tagName.equals("xs:element") ||
                tagName.equals("xs:attribute")
        )) {
            grayOutTag(xmlTag, h);
        } else {
            // if the parent is an element or sequence

        }

        // handle documentation
        if (tagName.equals("xs:documentation")) {
            // start recursive calls here...
            highlightDocumentation(xmlTag, h);
        }
    }

    /**
     * Highlight attribute names for certain cases.
     * All attribute names should be grayed out.
     *
     * @param xmlTag the xml tag to consider
     * @param h      the annotation holder
     */
    public void highlightAttributeNames(XmlTag xmlTag, AnnotationHolder h) {
        for (XmlAttribute attribute : xmlTag.getAttributes()) {


            // highlight value= for enumeration
            if (xmlTag.getName().equals("xs:enumeration") && attribute.getName().equals("value")) {

                int start = attribute.getNameElement().getTextRange().getStartOffset();
                int end = attribute.getValueElement().getTextRange().getStartOffset();  // grab the =

                highlight(new TextRange(start, end), STATIC_FIELD, h);
            } else {
                // gray out all other attribute names
                int start = attribute.getNameElement().getTextRange().getStartOffset();
                int end = attribute.getValueElement().getTextRange().getStartOffset();  // grab the =

                highlight(new TextRange(start, end), LINE_COMMENT, h);
            }

            // highlight the attribute and namespace value distinctly if the attribute is "base" or "type"
            if (attribute.getName().equals("base") || attribute.getName().equals("type")) {

                // start off by highlighting the whole value of the "base" attribute
                highlight(attribute.getValueElement().getTextRange(), NUMBER, h);

                // now look for a namespace element
                PsiElement[] children = attribute.getValueElement().getChildren();
                if (children.length > 2) {

                    PsiElement textElement = attribute.getValueElement().getChildren()[1];
                    int nsSeparatorIndex = textElement.getText().indexOf(':');

                    // if a namespace is present:
                    if (nsSeparatorIndex > 0) {

                        int nsStart = textElement.getTextRange().getStartOffset();
                        int nsEnd = nsStart + nsSeparatorIndex;

                        highlight(new TextRange(nsStart, nsEnd), INSTANCE_FIELD, h);
                    }
                }
            }
        }
    }

    /**
     * turn all non xml tags into string text.
     *
     * @param xmlTag the xml tag to consider
     * @param h      the annotation holder.
     */
    public void highlightDocumentation(XmlTag xmlTag, AnnotationHolder h) {
        for (PsiElement element : xmlTag.getChildren()) {
            if (element instanceof XmlText) {
                highlight(element.getTextRange(), STRING, h);
            } else if (element instanceof XmlTag) {
                highlightDocumentation((XmlTag) element, h);  // recursive call
            }
        }
    }
}