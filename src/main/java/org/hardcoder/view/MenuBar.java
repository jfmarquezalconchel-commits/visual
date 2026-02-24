package org.hardcoder.view;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que encapsula la barra de menús de la aplicación.
 */
public class MenuBar extends JMenuBar {
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;

    public MenuBar() {
        createFileMenu();
        createEditMenu();
        createHelpMenu();
        add(fileMenu);
        add(editMenu);
        add(Box.createHorizontalGlue()); // empuja help a la derecha
        add(helpMenu);
    }

    private void createFileMenu() {
        fileMenu = new JMenu("Ficheros");
        fileMenu.add(createMenuItem("Nuevo", "nuevo"));
        fileMenu.add(createMenuItem("Cargar", "cargar"));
        fileMenu.add(createMenuItem("Salvar", "salvar"));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Configuración", "config"));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Salir", "salir"));
    }

    private void createEditMenu() {
        editMenu = new JMenu("Edición");
        editMenu.add(createMenuItem("Buscar", "buscar"));
        editMenu.add(createMenuItem("Buscar y reemplazar", "buscar_reemplazar"));
    }

    private void createHelpMenu() {
        helpMenu = new JMenu("Ayuda");
        helpMenu.add(createMenuItem("Sobre nosotros", "sobre"));
    }

    private JMenuItem createMenuItem(String text, String actionCommand) {
        JMenuItem item = new JMenuItem(text);
        item.setActionCommand(actionCommand);
        return item;
    }

    // Getters para acceso a los items
    public JMenuItem getFileMenuItemByCommand(String command) {
        return getMenuItemByCommand(fileMenu, command);
    }

    public JMenuItem getEditMenuItemByCommand(String command) {
        return getMenuItemByCommand(editMenu, command);
    }

    public JMenuItem getHelpMenuItemByCommand(String command) {
        return getMenuItemByCommand(helpMenu, command);
    }

    private JMenuItem getMenuItemByCommand(JMenu menu, String command) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null && command.equals(item.getActionCommand())) {
                return item;
            }
        }
        return null;
    }
}

