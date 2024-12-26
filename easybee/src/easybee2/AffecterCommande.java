package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

public class AffecterCommande extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel modele;
    private JComboBox<String> comboBoxMatricule;
    private int idCommandeSelectionnee = -1;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AffecterCommande frame = new AffecterCommande();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AffecterCommande() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 535, 350);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel fond = new JPanel();
        fond.setBounds(0, 0, 520, 75);
        contentPane.add(fond);
        fond.setLayout(null);

        JLabel titre = new JLabel("Affecter une commande à préparer");
        titre.setFont(new Font("Arial", Font.BOLD, 12));
        titre.setBounds(20, 30, 300, 20);
        fond.add(titre);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setForeground(Color.BLACK);
        btnRetour.setFont(new Font("Arial", Font.PLAIN, 11));
        btnRetour.setBackground(Color.WHITE);
        btnRetour.setBounds(0, 0, 90, 25);
        btnRetour.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new MenuPrincipalPreparateur().setVisible(true);  
            }
        });
        fond.add(btnRetour);

        JLabel etape1 = new JLabel("1 - Sélectionner une commande à préparer");
        etape1.setFont(new Font("Arial", Font.ITALIC, 12));
        etape1.setBounds(25, 85, 250, 20);
        contentPane.add(etape1);

        JLabel etape2 = new JLabel("2 - Sélectionner votre matricule");
        etape2.setFont(new Font("Arial", Font.ITALIC, 12));
        etape2.setBounds(340, 85, 180, 20);
        contentPane.add(etape2);

        JLabel etape3 = new JLabel("3 - Confirmer votre affectation");
        etape3.setFont(new Font("Arial", Font.ITALIC, 12));
        etape3.setBounds(340, 155, 165, 20);
        contentPane.add(etape3);

        comboBoxMatricule = new JComboBox<>();
        comboBoxMatricule.setForeground(Color.BLACK);
        comboBoxMatricule.setFont(new Font("Arial", Font.PLAIN, 11));
        comboBoxMatricule.setBounds(340, 110, 90, 20); 
        contentPane.add(comboBoxMatricule);

        JButton btnAffecter = new JButton("Affecter");
        btnAffecter.setBackground(Color.WHITE);
        btnAffecter.setFont(new Font("Arial", Font.PLAIN, 11));
        btnAffecter.setBounds(340, 180, 90, 20);
        btnAffecter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                affecterCommandeAuPreparateur(); 
            }
        });
        contentPane.add(btnAffecter);

        table = new JTable();
        modele = new DefaultTableModel();
        modele.addColumn("id");
        modele.addColumn("dateCommande");
        table.setModel(modele);
        table.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setBounds(32, 117, 278, 103);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(25, 115, 280, 100);  
        contentPane.add(scrollPane);  

        chargerDonneesTableau();
        chargerMatricules();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int ligne = table.getSelectedRow();
                if (ligne != -1) {
                    idCommandeSelectionnee = (int) modele.getValueAt(ligne, 0); 
                }
            }
        });
    }

    private void chargerDonneesTableau() {
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "SELECT id, dateCommande FROM cmdapprodepot WHERE statutCommande = 'Libre'";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                modele.setRowCount(0); 

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String dateCommande = rs.getString("dateCommande");
                    modele.addRow(new Object[]{id, dateCommande});
                }
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
    }

    private void chargerMatricules() {
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "SELECT matriculeSalarie FROM salarie WHERE idType = '2'";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                comboBoxMatricule.removeAllItems();

                while (rs.next()) {
                    String matricule = rs.getString("matriculeSalarie");
                    comboBoxMatricule.addItem(matricule); 
                }
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
    }

    private void affecterCommandeAuPreparateur() {
        if (idCommandeSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à préparer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String matriculeSelectionne = (String) comboBoxMatricule.getSelectedItem();
        if (matriculeSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un matricule.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idSalarie = obtenirIdSalarie(matriculeSelectionne);

        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "UPDATE cmdapprodepot SET idSalarie = ?, statutCommande = 'En cours' WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idSalarie);
                stmt.setInt(2, idCommandeSelectionnee);
                int lignesAffectees = stmt.executeUpdate();

                if (lignesAffectees > 0) {
                    chargerDonneesTableau();  
                    JOptionPane.showMessageDialog(this, "Commande affectée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    idCommandeSelectionnee = -1; 
                } 
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int obtenirIdSalarie(String matricule) {
        int idSalarie = -1;
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "SELECT id FROM salarie WHERE matriculeSalarie = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, matricule);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    idSalarie = rs.getInt("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return idSalarie;
    }
}
