package frame;

import javax.swing.JPanel;

/**
 * <p> A form is an abstract class for a "page".        
 * For example, the menu form, the map form ...  </p>   
 * 
 * <p> Same as when you're in internet, you have different  
 * pages (form), but only one window.  <p/> 
 * 
 * @implNote <b>!!! ALL FORM'S SUBCLASS MUST HAVE A    
 * CONSTRUCTOR WHITHOUT ARGUMENTS </b>(example : MapView()) 
 * 
 * @author Arthur Deck
 * @version 1
 */
public abstract class Form extends JPanel {
    public Form() {
        super();
    }

    /**
     * 
     * <p> This method set the form on the current GameFram.  </p>
     * For example : MapView.setOn, set the view of the map,    
     * wich is use for play on the frame.   
     * 
     * @param g : the current GameFrame
     */
    public abstract void setOn(GameFrame g);
}
