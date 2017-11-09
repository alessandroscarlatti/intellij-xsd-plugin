package com.scarlatti;

import com.intellij.lang.LanguageAnnotators;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by pc on 10/21/2017.
 */
public class XsdPlugin implements ProjectComponent {

    Project project;

    public XsdPlugin(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }



    @Override
    public void initComponent() {
        System.out.println("initComponent");
//        LanguageAnnotators.INSTANCE.addExplicitExtension(XSDLanguage.INSTANCE, new XSDAnnotator());
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "XSD Plugin";
    }
}
