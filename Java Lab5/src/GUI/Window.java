package GUI;

import java.awt.EventQueue;
import java.beans.PropertyVetoException;

import javax.swing.JFrame;

import JavaBeen.TeacherJournal;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Window {

	/**
	 * Okno ziarenka (JFC/Swing)
	 */
	private JFrame beenFrame;

	private final Action action = new SwingAction();

	/**
	 * Uruchomia aplikacjê.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.beenFrame.setVisible(true);
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
	 * Inicjalizacja zawartoœc okna ziarenka
	 */
	private void initialize() {

		beenFrame = new JFrame();

		beenFrame.setIconImage(
				Toolkit.getDefaultToolkit().getImage("C:\\Users\\PSzynal\\Documents\\Java\\Java(Lab5)\\img\\pwr.png"));

		beenFrame.setResizable(false);
		beenFrame.setTitle("Lab 5. Ziarna Java'y.");
		beenFrame.setBounds(100, 100, 547, 350);
		beenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		TeacherJournal Student_1 = new TeacherJournal();
		Student_1.setBounds(0, 54, 180, 267);

		TeacherJournal Student_2 = new TeacherJournal();
		Student_2.setBounds(180, 54, 180, 267);

		TeacherJournal Student_3 = new TeacherJournal();
		Student_3.setBounds(360, 54, 180, 267);

		beenFrame.getContentPane().setLayout(null);

		Student_1.setTest("Test 1");
		Student_1.setAverage(0);
		Student_1.setRate(0);
		beenFrame.getContentPane().add(Student_1);

		Student_2.setTest("Test 1");
		Student_2.setRate(0);
		Student_2.setAverage(0);
		beenFrame.getContentPane().add(Student_2);

		Student_3.setTest("Test 1");
		Student_3.setRate(0);
		Student_3.setAverage(0);
		beenFrame.getContentPane().add(Student_3);

		try {
			Student_3.setTitle("Student 3");

		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		try {
			Student_1.setTitle("Student 1");

		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		try {
			Student_2.setTitle("Student 2");

		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		Student_1.addPropertyChangeListener(Student_2);
		Student_1.addPropertyChangeListener(Student_3);

		Student_2.addPropertyChangeListener(Student_1);
		Student_2.addPropertyChangeListener(Student_3);

		Student_3.addPropertyChangeListener(Student_1);
		Student_3.addPropertyChangeListener(Student_2);

		JLabel lblNewLabel = new JLabel("Prace student\u00F3w");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		lblNewLabel.setBounds(10, 11, 521, 32);
		beenFrame.getContentPane().add(lblNewLabel);

	}

	/**
	 * 
	 * @return actrion
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Ta klasa udostêpnia domyœlne implementacje interfejsu JFC. Wspiera JavaBeans
	 * 
	 * @author PSzynal
	 *
	 */
	private class SwingAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		/**
		 * Ustawia wartoœæ o okreœlonej wartoœæi.s
		 */
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}
}