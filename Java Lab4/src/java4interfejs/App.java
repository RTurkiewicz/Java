package java4interfejs;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.awt.event.ActionEvent;

public class App {

	private JFrame frame;
	LoadGraph graphloader = new LoadGraph();
	private int[][] graf;
	private int dimension;
	private Class c1;
	private Class c2;
	private Object obj;
	private Method[] m1;
	private Method[] m2;
	private int score1;
	private int score2;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
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
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextArea textArea = new JTextArea();
		
		JButton btnNewButton = new JButton("Load Genetic");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					c1 = Class.forName("tsp.Genetic");
					m1 = c1.getMethods();
					int funcno = 0;
					for(int i = 0; i < m1.length; i++) {
						System.out.println(m1[i].getName() + "\n");
						if(m1[i].getName()=="GeneticExec") {
							funcno = i;
						}
					}
					Object newGene = c1.newInstance();
					m1[funcno].invoke(newGene, graf, dimension, textArea, true);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JButton btnLoadNeighbour = new JButton("Load Neighbour");
		btnLoadNeighbour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				c2 = Class.forName("tsp.NearestNeighbour");
				m2 = c2.getMethods();
				int funcno2 = 0;
				for(int i = 0; i < m2.length; i++) {
					System.out.println(m2[i].getName() + "\n");
					if(m2[i].getName()=="NearestNeighbourExec") {
						funcno2 = i;
					}
				}
				Object newNeigh = c2.newInstance();
				m2[funcno2].invoke(newNeigh, graf, dimension, textArea, true);
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton btnLoadGraph = new JButton("Load graph");
		btnLoadGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					graphloader.GraphLoading();
					graf = graphloader.getTablica();
					dimension = graf.length;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton btnBestof = new JButton("BestOf");
		btnBestof.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				c2 = Class.forName("tsp.NearestNeighbour");
				m2 = c2.getMethods();
				int funcno2 = 0;
				for(int i = 0; i < m2.length; i++) {
					System.out.println(m2[i].getName() + "\n");
					if(m2[i].getName()=="NearestNeighbourExec") {
						funcno2 = i;
					}
				}
				Object newNeigh = c2.newInstance();
				m2[funcno2].invoke(newNeigh, graf, dimension, textArea, true);
				c1 = Class.forName("tsp.Genetic");
				m1 = c1.getMethods();
				int funcno = 0;
				for(int i = 0; i < m1.length; i++) {
					System.out.println(m1[i].getName() + "\n");
					if(m1[i].getName()=="GeneticExec") {
						funcno = i;
					}
				}
				Object newGene = c1.newInstance();
				m1[funcno].invoke(newGene, graf, dimension, textArea, true);
				if(score1 < score2)
					textArea.setText("Genetic is faster");
				else
					textArea.setText("NearestNeighbour is faster");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(53)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnLoadGraph)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnNewButton)
							.addGap(40)
							.addComponent(btnLoadNeighbour)
							.addGap(54)
							.addComponent(btnBestof))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 518, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(51, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(27)
					.addComponent(btnLoadGraph)
					.addGap(33)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(btnLoadNeighbour)
						.addComponent(btnBestof))
					.addGap(48)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(37, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
