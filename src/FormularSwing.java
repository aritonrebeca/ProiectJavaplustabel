import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FormularSwing extends JFrame {
    private JTextField marcaTextField;
    private JTextField serieTextField;
    private JTextField culoareTextField;
    private JRadioButton anMaiMicRadioButton;
    private JRadioButton anMaiMareRadioButton;
    private JTextField modelMotorTextField;
    private JTable dataTable;

    public FormularSwing() {
        setTitle("Formular cu Swing");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crearea elementelor UI
        marcaTextField = new JTextField(20);
        serieTextField = new JTextField(20);
        culoareTextField = new JTextField(20);
        anMaiMicRadioButton = new JRadioButton("An mai mic de 2000");
        anMaiMareRadioButton = new JRadioButton("An mai mare de 2000");
        ButtonGroup anGroup = new ButtonGroup();
        anGroup.add(anMaiMicRadioButton);
        anGroup.add(anMaiMareRadioButton);
        modelMotorTextField = new JTextField(20);

        JButton saveButton = new JButton("Salvează");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvareInJSON();
                afisareDateInTabel();
            }
        });

        JButton cancelButton = new JButton("Anulează");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Tabel pentru afișarea datelor
        DefaultTableModel modelTabel = new DefaultTableModel();
        modelTabel.addColumn("Marca");
        modelTabel.addColumn("Serie");
        modelTabel.addColumn("Culoare");
        modelTabel.addColumn("An");
        modelTabel.addColumn("Model Motor");

        dataTable = new JTable(modelTabel);
        JScrollPane scrollPane = new JScrollPane(dataTable);

        // Adăugarea elementelor în panou cu FlowLayout
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Marca mașinii:"));
        panel.add(marcaTextField);
        panel.add(new JLabel("Seria mașinii:"));
        panel.add(serieTextField);
        panel.add(new JLabel("Culoarea mașinii:"));
        panel.add(culoareTextField);
        panel.add(new JLabel("Anul mașinii:"));
        panel.add(anMaiMicRadioButton);
        panel.add(anMaiMareRadioButton);
        panel.add(new JLabel("Model motor:"));
        panel.add(modelMotorTextField);
        panel.add(saveButton);
        panel.add(cancelButton);

        // Adăugarea panoului și tabelului în fereastra principală
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Setarea aspectului radio button-urilor
        UIManager.put("RadioButton.focus", UIManager.get("CheckBox.focus"));

        // Afisare initiala a datelor din fisierul JSON
        afisareDateInTabel();

        setVisible(true);
    }

    private void salvareInJSON() {
        JSONObject jsonObiect = new JSONObject();
        jsonObiect.put("Marca", marcaTextField.getText());
        jsonObiect.put("Serie", serieTextField.getText());
        jsonObiect.put("Culoare", culoareTextField.getText());
        jsonObiect.put("An", anMaiMicRadioButton.isSelected() ? "Mai mic de 2000" : "Mai mare de 2000");
        jsonObiect.put("ModelMotor", modelMotorTextField.getText());

        try (FileWriter fileWriter = new FileWriter("date.json", true)) {
            fileWriter.write(jsonObiect.toJSONString() + "\n");
            JOptionPane.showMessageDialog(this, "Date salvate cu succes în fișierul JSON!", "Salvare reușită", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la salvarea datelor în fișierul JSON!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afisareDateInTabel() {
        DefaultTableModel modelTabel = (DefaultTableModel) dataTable.getModel();
        modelTabel.setRowCount(0); // Curăță tabelul înainte de a adăuga date noi

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("date.json"))) {
            String linie;
            JSONParser jsonParser = new JSONParser();

            while ((linie = bufferedReader.readLine()) != null) {
                try {
                    JSONObject jsonObiect = (JSONObject) jsonParser.parse(linie);
                    Vector<Object> rand = new Vector<>();
                    rand.add(jsonObiect.get("Marca"));
                    rand.add(jsonObiect.get("Serie"));
                    rand.add(jsonObiect.get("Culoare"));
                    rand.add(jsonObiect.get("An"));
                    rand.add(jsonObiect.get("ModelMotor"));
                    modelTabel.addRow(rand);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormularSwing();
            }
        });
    }
}
