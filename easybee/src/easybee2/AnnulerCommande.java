package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

public class AnnulerCommande extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> comboBoxIdCommande;
    private int selectedCommandeId = -1;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AnnulerCommande frame = new AnnulerCommande();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AnnulerCommande() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 535, 250);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel fond = new JPanel();
        fond.setBounds(0, 0, 520, 75);
        contentPane.add(fond);
        fond.setLayout(null);

        JLabel titre = new JLabel("Annuler une commande");
        titre.setFont(new Font("Arial", Font.BOLD, 12));
        titre.setBounds(20, 30, 300, 20);
        fond.add(titre);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setForeground(Color.BLACK);
        btnRetour.setFont(new Font("Arial", Font.PLAIN, 11));
        btnRetour.setBackground(Color.WHITE);
        btnRetour.setBounds(0, 0, 90, 25);
        btnRetour.addActionListener(e -> {
            dispose();
            new MenuPrincipalVendeur().setVisible(true);
        });
        fond.add(btnRetour);

        JLabel etape1 = new JLabel("1 - Sélectionner une commande");
        etape1.setFont(new Font("Arial", Font.ITALIC, 12));
        etape1.setBounds(25, 85, 250, 20);
        contentPane.add(etape1);

        comboBoxIdCommande = new JComboBox<>();
        comboBoxIdCommande.setForeground(Color.BLACK);
        comboBoxIdCommande.setFont(new Font("Arial", Font.PLAIN, 12));
        comboBoxIdCommande.setBounds(25, 115, 200, 20);
        comboBoxIdCommande.addActionListener(e -> chargerCommandeDetails());
        contentPane.add(comboBoxIdCommande);

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setForeground(Color.WHITE);
        btnAnnuler.setBackground(new Color(255, 0, 0));
        btnAnnuler.setFont(new Font("Arial", Font.PLAIN, 12));
        btnAnnuler.setBounds(250, 115, 185, 20);
        btnAnnuler.addActionListener(e -> annulerCommande());
        contentPane.add(btnAnnuler);

        chargerCommandeListe();
    }

    private void chargerCommandeListe() {
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "SELECT id FROM cmdapprodepot WHERE statutCommande IN ('En cours', 'Libre')";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                comboBoxIdCommande.removeAllItems();

                while (rs.next()) {
                    String commandeId = rs.getString("id");
                    comboBoxIdCommande.addItem(commandeId);
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void chargerCommandeDetails() {
        String selectedCommande = (String) comboBoxIdCommande.getSelectedItem();
        if (selectedCommande == null || selectedCommande.isEmpty()) {
            return;
        }

        selectedCommandeId = Integer.parseInt(selectedCommande);
    }

    private void annulerCommande() {
        if (selectedCommandeId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à annuler.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "UPDATE cmdapprodepot SET statutCommande = 'Annulée' WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, selectedCommandeId);
                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "La commande a été annulée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    chargerCommandeListe();
                    selectedCommandeId = -1;
                } else {
                    JOptionPane.showMessageDialog(this, "Impossible d'annuler la commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
