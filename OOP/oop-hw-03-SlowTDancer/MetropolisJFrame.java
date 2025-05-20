import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MetropolisJFrame extends JFrame {
    private JTextField metropolisTextField, continentTextField, populationTextField;
    private JButton addButton, searchButton;
    private JComboBox populationPulldown, matchPulldown;
    private MetropolisesTable mTable;
    private final static String TABLE_NAME = "metropolises";
    private static final String DB_NAME = "";
    private static final String USER_NAME = "";
    private static final String PASSWORD = "";
    public MetropolisJFrame() throws SQLException, ClassNotFoundException {
        super("Metropolis Viewer");
        setLayout(new BorderLayout(4, 4));
        setPreferredSize(new Dimension(800, 500));

        CreateUpperArea();
        CreateRightArea();
        CreateCenterArea();

        addListeners();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void addListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String metropolis = metropolisTextField.getText();
                metropolisTextField.setText("");
                String continent = continentTextField.getText();
                continentTextField.setText("");
                String population = populationTextField.getText();
                populationTextField.setText("");

                Metropolis val = new Metropolis(metropolis, continent, population);
                try {
                    mTable.add(val);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String metropolis = metropolisTextField.getText();
                metropolisTextField.setText("");

                int populationType = 0;
                if(!populationPulldown.getSelectedItem().equals("Population Larger Than")){
                    populationType++;
                }

                String continent = continentTextField.getText();
                continentTextField.setText("");

                int matchType = 0;
                if(!matchPulldown.getSelectedItem().equals("Exact Match")){
                    matchType++;
                }

                String population = populationTextField.getText();
                populationTextField.setText("");

                Metropolis val = new Metropolis(metropolis, continent, population);

                try {
                    mTable.search(val, populationType, matchType);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void CreateUpperArea() {
        JPanel panel = new JPanel();

        JLabel nameLabel = new JLabel("Metropolis:");
        JLabel continentLabel = new JLabel("Continent:");
        JLabel populationLabel = new JLabel("Population:");
        metropolisTextField = new JTextField(15);
        continentTextField = new JTextField(15);
        populationTextField = new JTextField(15);

        panel.add(nameLabel);
        panel.add(metropolisTextField);
        panel.add(continentLabel);
        panel.add(continentTextField);
        panel.add(populationLabel);
        panel.add(populationTextField);

        add(panel, BorderLayout.NORTH);
    }

    private void addSearchBox(JPanel panel){
        Box searchBox = new Box(BoxLayout.Y_AXIS);

        populationPulldown = new JComboBox<>(new String[]{"Population Larger Than", "Populations Smaller Than"});
        matchPulldown = new JComboBox<>(new String[]{"Exact Match", "Partial Match"});

        int checkBoxWidth = Math.max(populationPulldown.getMinimumSize().width, matchPulldown.getMinimumSize().width);
        populationPulldown.setMaximumSize(new Dimension(checkBoxWidth, populationPulldown.getMinimumSize().height));
        matchPulldown.setMaximumSize(new Dimension(checkBoxWidth, matchPulldown.getMinimumSize().height));

        searchBox.add(populationPulldown);
        searchBox.add(matchPulldown);
        panel.add(searchBox);

        searchBox.setBorder(new TitledBorder("Search Options"));
    }

    private void CreateRightArea(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        JPanel miniPanel = new JPanel(new GridLayout(4, 2));

        addButton = new JButton("Add");
        searchButton = new JButton("Search");

        int buttonWidth = Math.max(addButton.getMinimumSize().width, searchButton.getMinimumSize().width);
        searchButton.setMaximumSize(new Dimension(buttonWidth, searchButton.getMinimumSize().height));
        addButton.setMaximumSize(new Dimension(buttonWidth, addButton.getMinimumSize().height));

        miniPanel.add(addButton);
        for(int i = 0; i < 5; i++) miniPanel.add(Box.createRigidArea(new Dimension(50, 25)));
        miniPanel.add(searchButton);
        for(int i = 0; i < 5; i++) miniPanel.add(Box.createRigidArea(new Dimension(50, 25)));
        panel.add(miniPanel);
        addSearchBox(panel);

        add(panel, BorderLayout.EAST);
    }

    private void CreateCenterArea() throws SQLException, ClassNotFoundException {
        mTable = new MetropolisesTable(TABLE_NAME, DB_NAME, USER_NAME, PASSWORD);
        JTable table = new JTable(mTable);
        JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MetropolisJFrame frame = new MetropolisJFrame();
    }
}
