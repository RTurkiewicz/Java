package JavaBeen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.beans.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Zairenko javy przdstawia dziennik nauczycielski
 * 
 * for reference, look here: <a href=
 * "http://tomasz.kubik.staff.iiar.pwr.wroc.pl/dydaktyka/Java/JavaWyk08-JBeans-TK.pdf">Java
 * Beans </a>
 * 
 * @author PSzynal
 *
 */
public class TeacherJournal extends JComponent implements Serializable, PropertyChangeListener, VetoableChangeListener {

	private static final long serialVersionUID = 1L;

	private PropertyChangeSupport propertyChangeListeners = new PropertyChangeSupport(this);
	private VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

	private String student;
	private String test;
	private int averageGrade;
	private int rate;
	private int maxRate = 5;
	private int tests_number = 0;

	@SuppressWarnings("rawtypes")

	private DefaultComboBoxModel model;

	public TeacherJournal() {
		propertyChangeListeners.addPropertyChangeListener(this);
		vetoableChangeSupport.addVetoableChangeListener(this);
		this.student = "Student";
		this.test = "Test";
		this.averageGrade = 0;
		this.rate = 0;
		initComponents();
	}

	public void setTitle(String student) throws PropertyVetoException {
		String student_name = student;
		// wysy³a zdarzenie zmian w³¹sciwoœci ziarenka
		vetoableChangeSupport.fireVetoableChange(new PropertyChangeEvent(this, "Student", student_name, student));

		this.student = student;

		setBorder(BorderFactory.createTitledBorder(student));
	}

	public int getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(int maxRate) {
		Integer oldRate = new Integer(this.maxRate);
		this.maxRate = maxRate;
		propertyChangeListeners
				.firePropertyChange(new PropertyChangeEvent(this, "maxValue", oldRate, new Integer(this.maxRate)));
	}

	@SuppressWarnings("rawtypes")
	JComboBox jComboBoxRates;
	JTextArea jTextAreaBody;
	JLabel jLabelAverage;
	JButton btnAddRate;

	private void addRate(int rate) {
		this.averageGrade += rate;
		this.tests_number++;
		if (tests_number > 0) {
			jLabelAverage.setText(Float.toString((float) averageGrade / tests_number));
		}

		else
			jLabelAverage.setText("0.0");
	}

	private void initComponents() {
		setMinimumSize(new Dimension(200, 300));
		setBorder(BorderFactory.createTitledBorder(student));

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(layout);

		btnAddRate = new JButton("Oce\u0144"); // oceñ ^^ -> ñ
		btnAddRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		constraints.gridx = 2;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.SOUTH;
		btnAddRate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				addRate(Integer.valueOf((String) jComboBoxRates.getSelectedItem()));
			}
		});
		layout.setConstraints(btnAddRate, constraints);

		add(btnAddRate);

		jTextAreaBody = new JTextArea(2, 10);
		jTextAreaBody.setText(test);
		JScrollPane scpArea = new JScrollPane(jTextAreaBody);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		constraints.gridheight = 6;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.anchor = GridBagConstraints.NORTH;
		layout.setConstraints(scpArea, constraints);
		add(scpArea);

		String[] dataRates = new String[maxRate];
		for (int i = 0; i < maxRate; i++)
			dataRates[i] = Integer.toString(i + 1);
		jComboBoxRates = new JComboBox<Object>(dataRates);
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.SOUTH;
		layout.setConstraints(jComboBoxRates, constraints);
		add(jComboBoxRates);

		if (rate != 0)
			jLabelAverage = new JLabel(Float.toString((float) averageGrade));
		else
			jLabelAverage = new JLabel("0.0");
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.NORTH;
		layout.setConstraints(jLabelAverage, constraints);
		add(jLabelAverage);
	}

	public String getStudent() {
		return student;
	}

	public void setTest(String test) {
		this.test = test;
		this.jTextAreaBody.setText(test);

	}

	public String getTest() {
		return test;
	}

	public void setAverage(int sum) {
		this.averageGrade = sum;
		if (rate > 0)
			jLabelAverage.setText(Float.toString((((float) sum + (float) rate)) / (float) tests_number));
		else
			jLabelAverage.setText("0.0");
	}

	public int getAverage() {
		return averageGrade;
	}

	public void setRate(int mark) {
		this.rate = mark;
		if (mark > 0)
			jLabelAverage.setText(Float.toString(((float) averageGrade + (float) mark) / tests_number));
		else
			jLabelAverage.setText("0.0");
	}

	public int getRate() {
		return rate;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	/**
	 * Ta metoda zostanie wywo³ana, gdy w³aœciwoœæ zostanie zmieniona.
	 */
	public synchronized void propertyChange(PropertyChangeEvent evt) {
		evt.getPropertyName();
		maxRate = (int) evt.getNewValue();
		String[] dataRates = new String[maxRate];
		for (int i = 0; i < maxRate; i++)
			dataRates[i] = Integer.toString(i + 1);
		model = new DefaultComboBoxModel(dataRates);
		jComboBoxRates.setModel(model);
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
	}

	@Override
	public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
		setTest("vetoableChange");
		if ((String) evt.getNewValue() == "aaa")
			throw new PropertyVetoException("", evt);
		else
			student = evt.getNewValue().toString();
	}

	public void removeVetoableChangeListener(VetoableChangeListener l) {
		vetoableChangeSupport.removeVetoableChangeListener(l);
	}

	public void addVetoableChangeListener(VetoableChangeListener l) {
		vetoableChangeSupport.addVetoableChangeListener(l);
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener p) {
		propertyChangeListeners.addPropertyChangeListener(p);
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener p) {
		propertyChangeListeners.removePropertyChangeListener(p);
	}
}
