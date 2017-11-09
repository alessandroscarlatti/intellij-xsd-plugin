package com.scarlatti;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.uiDesigner.core.GridConstraints;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * ~     _____                                    __
 * ~    (, /  |  /)                /)         (__/  )      /)        ,
 * ~      /---| // _ _  _  _ __  _(/ __ ___     / _ _  __ // _ _/_/_
 * ~   ) /    |(/_(//_)/_)(_(/ ((_(_/ ((_)   ) / (_(_(/ ((/_(_((_(__(_
 * ~  (_/                                   (_/
 * ~  Wednesday, 11/8/2017
 */
public class Viewer implements ToolWindowFactory {
    private JButton grabButton;
    private JPanel jPanel;
    private JTextField textField1;
    private JButton releaseButton;
    private JTextField textField2;

    private JFrame jFrame;

    public Viewer() {
        grabButton.addActionListener(e -> {
            grabWindow(textField1.getText());
        });
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(jPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    public void grabWindow(String text) {
        // see if I can identify the process ID
        System.out.println(Kernel32.INSTANCE.GetCurrentProcessId());

//        JDesktopPane jDesktopPane = new JDesktopPane();

        JInternalFrame jInternalFrame = new JInternalFrame("Embed Squirrel");
        jInternalFrame.setLocation(0,0);
        jInternalFrame.setSize(100,100);
        jInternalFrame.setVisible(true);

        jPanel.add(jInternalFrame, new GridConstraints());  // Grid constraints necessary to avoid nullptrexception


        WinDef.HWND fakeSquirrel;

        User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer data) {

                char[] windowText = new char[512];
                User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
                String wText = Native.toString(windowText);

                System.out.println(">>>" + wText.trim());

                if (wText.trim().equals(text)) {
                    embedWindow(hWnd);
                }

                return true;
            }
        }, null);
    }

    public void embedWindow(WinDef.HWND hWnd) {
        BaseTSD.LONG_PTR style = User32.INSTANCE.GetWindowLongPtr(hWnd, WinUser.GWL_STYLE);
        Long styleLong = style.longValue();
        styleLong |= new Long(WinUser.WS_CHILD);
        styleLong &= ~new Long(WinUser.WS_CAPTION);
        styleLong &= ~new Long(WinUser.WS_POPUP);

        User32.INSTANCE.SetWindowLongPtr(hWnd, WinUser.GWL_STYLE, new Pointer(styleLong));

        User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND ideHWND, Pointer data) {

                char[] windowText = new char[512];
                User32.INSTANCE.GetWindowText(ideHWND, windowText, 512);
                String wText = Native.toString(windowText);

                System.out.println(">>>" + wText.trim());

                if (wText.trim().equals(textField2.getText())) {  // this should fail, but that's OK.

                    User32.INSTANCE.SetParent(hWnd, ideHWND);
                    User32.INSTANCE.SetForegroundWindow(hWnd);

                }

                return true;
            }
        }, null);

    }
}
