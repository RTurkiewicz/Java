package SwingAction;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Dodaje Ÿród³a sygnalu zmieniaj¹cego wartoœæ wi¹zana
 * 
 * @author Pawe³ Szynal
 *
 */
public class SwingAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public SwingAction() {
		putValue(NAME, "SwingAction");
		putValue(SHORT_DESCRIPTION, "Some short description");
	}

	public void actionPerformed(ActionEvent e) {
	}

}
