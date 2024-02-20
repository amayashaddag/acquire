package view;

import view.Menu.MenuView;

public class Debug {
    public static void main(String[] args) {
        /* GameFrame g = new GameFrame (null, null);
        g.setVisible(true);
        g.repaint(); */
        GameFrame h = new GameFrame(null, null); // Créer une instance de GameFrame
        h.setForm(new MenuView()); // Ajouter une instance de MenuView à GameFrame
        h.setVisible(true); // Rendre GameFrame visible
        
    }
}
