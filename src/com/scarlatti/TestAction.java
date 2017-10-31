package com.scarlatti;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by pc on 10/21/2017.
 */
public class TestAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        TestDialog.main(null);
    }

    @Override
    public void update(final AnActionEvent e) {

    }
}
