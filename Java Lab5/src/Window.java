import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.beans.PropertyVetoException;

import javax.swing.JFrame;

import bean.AdviceJComponent;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

public class Window {

	private JFrame frame;
	
	private final Action action = new SwingAction();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		AdviceJComponent advice1 = new AdviceJComponent();
		AdviceJComponent advice2 = new AdviceJComponent();
		AdviceJComponent advice3 = new AdviceJComponent();
		advice1.setBody("ssadad");
		advice1.setSum(0);
		advice1.setAmount(0);
		frame.getContentPane().add(advice1);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				advice1.setMaxRate(10);
				
			}
		});
		frame.getContentPane().add(btnNewButton);
		
		advice2.setBody("Body 1");
		advice2.setAmount(0);
		advice2.setSum(0);
		frame.getContentPane().add(advice2);
		
		advice3.setBody("Body 2");
		advice3.setAmount(0);
		advice3.setSum(0);
		frame.getContentPane().add(advice3);
		
		try {
			advice3.setTitle("sdfs");
			
		} catch(PropertyVetoException e) {
			e.printStackTrace();
		}
		try {
			advice1.setTitle("Title 1");
			
		} catch(PropertyVetoException e) {
			e.printStackTrace();
		}
		try {
			advice2.setTitle("Title 2");
			
		} catch(PropertyVetoException e) {
			e.printStackTrace();
		}
		
		
		advice1.addPropertyChangeListener(advice2);
		advice1.addPropertyChangeListener(advice3);
		advice2.addPropertyChangeListener(advice1);
		advice2.addPropertyChangeListener(advice3);
		advice3.addPropertyChangeListener(advice1);
		advice3.addPropertyChangeListener(advice2);
		
		advice2.setMaxRate(7);
		
	
	}

	public Action getAction() {
		return action;
	}

	private class SwingAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}