package org.jdownloader.gui.mainmenu;

import java.awt.event.ActionEvent;

import org.jdownloader.controlling.contextmenu.CustomizableAppAction;
import org.jdownloader.gui.IconKey;
import org.jdownloader.gui.translate._GUI;

import jd.gui.swing.jdgui.components.toolbar.actions.UpdateAction;

public class CheckForUpdatesAction extends CustomizableAppAction {
    public CheckForUpdatesAction() {
        setIconKey(IconKey.ICON_UPDATE);
        setName(_GUI.T.CheckForUpdatesAction_CheckForUpdatesAction());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new UpdateAction().actionPerformed(e);
    }

}
