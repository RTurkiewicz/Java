package bean;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.beans.*;

public class AdviceJComponent extends JComponent implements Serializable, PropertyChangeListener, VetoableChangeListener {

    private PropertyChangeSupport propertyChangeListeners = new PropertyChangeSupport(this);
    private VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

    private String title;
    private String body;
    private int sum;
    private int amount;
    private int maxRate = 1;

    public AdviceJComponent(){
        propertyChangeListeners.addPropertyChangeListener(this);
        vetoableChangeSupport.addVetoableChangeListener(this);
        this.title="Title";
        this.body="Body";
        this.sum = 0;
        this.amount = 0;
        initComponents();
    }

    public void setTitle(String title) throws PropertyVetoException  {
        String oldTitle = title;
        vetoableChangeSupport.fireVetoableChange(new PropertyChangeEvent(this,"title", oldTitle, title));

        this.title = title;

        setBorder(BorderFactory.createTitledBorder(title));
    }

    public int getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(int maxRate) {
        Integer oldRate =  new Integer(this.maxRate);
        this.maxRate = maxRate;
        propertyChangeListeners.firePropertyChange(new PropertyChangeEvent(this, "maxValue", oldRate, new Integer(this.maxRate)));
    }

    JComboBox jComboBoxRates;
    JTextArea jTextAreaBody;
    JLabel jLabelAverage;
    JButton btnAddRate;


    private void addRate(int rate){
        this.sum+=rate;
        this.amount++;
        if(amount>0)
            jLabelAverage.setText(Float.toString((float)sum/(float)amount));
        else
            jLabelAverage.setText("0.0");
    }



    private void initComponents(){
        setMinimumSize(new Dimension(200, 300));
        setBorder(BorderFactory.createTitledBorder(title));

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);

        btnAddRate = new JButton( "OK"  );
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
                addRate(Integer.valueOf((String)jComboBoxRates.getSelectedItem()));
            }
        });
        layout.setConstraints( btnAddRate, constraints );

        add( btnAddRate );

        jTextAreaBody = new JTextArea(2,10);
        jTextAreaBody.setText(body);
        JScrollPane scpArea = new JScrollPane( jTextAreaBody );
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 6;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        layout.setConstraints( scpArea, constraints );
        add( scpArea );

        String []dataRates = new String[maxRate];
        for (int i=0; i<maxRate; i++)
            dataRates[i]=Integer.toString(i+1);
        jComboBoxRates = new JComboBox( dataRates );
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.SOUTH;
        layout.setConstraints( jComboBoxRates, constraints );
        add( jComboBoxRates );

        if(amount!=0)
            jLabelAverage = new JLabel( Float.toString((float)sum/(float)amount)  );
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
        layout.setConstraints( jLabelAverage, constraints );
        add( jLabelAverage );
    }





    public String getTitle() {
        return title;
    }

    public void setBody(String body) {
        this.body = body;
        this.jTextAreaBody.setText(body);

    }

    public String getBody(){
        return body;
    }

    public void setSum(int sum){
        this.sum = sum;
        if(amount>0)
            jLabelAverage.setText(Float.toString((float)sum/(float)amount));
        else
            jLabelAverage.setText("0.0");
    }

    public int getSum(){
        return sum;
    }

    public void setAmount(int amount){
        this.amount=amount;
        if(amount>0)
            jLabelAverage.setText(Float.toString((float)sum/(float)amount));
        else
            jLabelAverage.setText("0.0");
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        //setBody("propChange");
        evt.getPropertyName();
        maxRate=(int)evt.getNewValue();
        String []dataRates = new String[maxRate];
        for (int i=0; i<maxRate; i++)
            dataRates[i]=Integer.toString(i+1);
        DefaultComboBoxModel model = new DefaultComboBoxModel( dataRates );
        jComboBoxRates.setModel(model);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }
    private void readObject(ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        setBody("vetoableChange");
        if ((String)evt.getNewValue() == "aaa")
            throw new PropertyVetoException("",evt);
        else
            title = evt.getNewValue().toString();
    }

    public void removeVetoableChangeListener(VetoableChangeListener l) {
        vetoableChangeSupport.removeVetoableChangeListener(l);
    }

    public void addVetoableChangeListener(VetoableChangeListener l) {
        vetoableChangeSupport.addVetoableChangeListener(l);
    }

    public synchronized void addPropertyChangeListener (
            PropertyChangeListener p) {
        propertyChangeListeners.addPropertyChangeListener (p);
    }

    public synchronized void removePropertyChangeListener (
            PropertyChangeListener p) {
        propertyChangeListeners.removePropertyChangeListener (p);
    }
}
