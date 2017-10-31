package com.scarlatti;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ~     _____                                    __
 * ~    (, /  |  /)                /)         (__/  )      /)        ,
 * ~      /---| // _ _  _  _ __  _(/ __ ___     / _ _  __ // _ _/_/_
 * ~   ) /    |(/_(//_)/_)(_(/ ((_(_/ ((_)   ) / (_(_(/ ((/_(_((_(__(_
 * ~  (_/                                   (_/
 * ~  Monday, 10/30/2017
 */
public class TestForm {
    private JCheckBox highlightYesNoCheckBox;
    private JPanel form;

    private boolean highlightYesOrNo = true;

    public TestForm() {
        highlightYesNoCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightYesOrNo = highlightYesNoCheckBox.isSelected();
            }
        });
    }

    public boolean getHighlightYesNo() {
        return highlightYesOrNo;
    }

    public JPanel getForm() {
        return form;
    }

    public void setForm(JPanel form) {
        this.form = form;
    }
}
