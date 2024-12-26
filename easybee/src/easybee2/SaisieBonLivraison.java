package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

public class SaisieBonLivraison extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> commandeComboBox;
    private int selectedCommandId = -1;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SaisieBonLivraison frame = new SaisieBonLivraison();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SaisieBonLivraison() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 430);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel fond = new JPanel();
        fond.setBounds(0, 0, 635, 75);
        contentPane.add(fond);
        fond.setLayout(null);

        JLabel titre = new JLabel("Saisir un bon de livraison");
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

        JLabel instructionLabel1 = new JLabel("1 - Sélectionner une commande");
        instructionLabel1.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionLabel1.setBounds(25, 85, 250, 20);
        contentPane.add(instructionLabel1);

        commandeComboBox = new JComboBox<>();
        commandeComboBox.setForeground(Color.BLACK);
        commandeComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        commandeComboBox.setBounds(25, 115, 200, 20);
        commandeComboBox.addActionListener(e -> chargerCommandDetails());
        contentPane.add(commandeComboBox);

        JLabel etape2 = new JLabel("2 - Saisir les quantités préparées");
        etape2.setFont(new Font("Arial", Font.ITALIC, 12));
        etape2.setBounds(25, 150, 250, 20);
        contentPane.add(etape2);

        model = new DefaultTableModel();
        model.addColumn("désignation Produit");
        model.addColumn("qte Commandée");
        model.addColumn("qte Préparée");

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(25, 180, 585, 150);
        contentPane.add(scrollPane);

        JButton btnValider = new JButton("Valider");
        btnValider.setForeground(Color.WHITE);
        btnValider.setBackground(new Color(0, 123, 255));
        btnValider.setFont(new Font("Arial", Font.PLAIN, 12));
        btnValider.setBounds(220, 350, 200, 25);
        btnValider.addActionListener(e -> validerBonLivraison());
        contentPane.add(btnValider);

        chargerCommandList();
    }

    private void chargerCommandList() {
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "SELECT DISTINCT cmdapprodepot.id "
                        + "FROM cmdapprodepot "
                        + "WHERE statutCommande = 'En cours'";

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                commandeComboBox.removeAllItems();

                while (rs.next()) {
                    String commandeId = rs.getString("id");
                    commandeComboBox.addItem(commandeId);
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement des commandes.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erreur : Connexion à la base de données non établie.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerCommandDetails() {
        String selectedCommand = (String) commandeComboBox.getSelectedItem();

        if (selectedCommand == null || selectedCommand.isEmpty()) {
            return;
        }

        selectedCommandId = Integer.parseInt(selectedCommand);

        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "SELECT designationProduit, qteCmd, cmdapprodepot.id "
                        + "FROM detailsproduit "
                        + "JOIN produit ON detailsproduit.idProduit = produit.id "
                        + "JOIN cmdapprodepot ON detailsproduit.idCommande = cmdapprodepot.id "
                        + "WHERE idCommande = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, selectedCommandId);
                ResultSet rs = stmt.executeQuery();

                model.setRowCount(0);

                while (rs.next()) {
                    String designationProduit = rs.getString("designationProduit");
                    int qteCmd = rs.getInt("qteCmd");
                    model.addRow(new Object[]{designationProduit, qteCmd});
                }

                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void validerBonLivraison() {
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        
        if (conn != null) {
            try {
            	
                for (int row = 0; row < model.getRowCount(); row++) {
                    String qtePrepaStr = (String) model.getValueAt(row, 2);
                    
                    if (qtePrepaStr == null || qtePrepaStr.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Veuillez entrer la quantité préparée pour le produit : " + model.getValueAt(row, 0),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                }

                String insertQuery = "INSERT INTO bondelivraison (dateLivraison) VALUES (CURRENT_DATE)";
                PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                int idBonLivraison = rs.getInt(1);

                for (int row = 0; row < model.getRowCount(); row++) {
                    String designationProduit = (String) model.getValueAt(row, 0);
                    int qtePrepa = Integer.parseInt((String) model.getValueAt(row, 2)); 

                    int idCmdApproDepot = obtenirCmdApproDepotId(selectedCommandId);
                    int productId = obtenirProductId(designationProduit);

                    if (productId != -1) {
                        String detailQuery = "INSERT INTO detaillivraison (idBonLivraison, idProduit, idCmdeAppropDepot, qtePrepa) "
                                + "VALUES (?, ?, ?, ?)";
                        PreparedStatement stmtDetail = conn.prepareStatement(detailQuery);
                        stmtDetail.setInt(1, idBonLivraison);
                        stmtDetail.setInt(2, productId);
                        stmtDetail.setInt(3, idCmdApproDepot);
                        stmtDetail.setInt(4, qtePrepa);
                        stmtDetail.executeUpdate();
                    }
                }

                String updateQuery = "UPDATE cmdapprodepot SET statutCommande = 'Préparée' WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, selectedCommandId);
                updateStmt.executeUpdate();
                updateStmt.close();

                JOptionPane.showMessageDialog(this, "Bon de livraison validé avec succès !");
                chargerCommandList();  
                stmt.close();

            } catch (Exception e) {
                e.printStackTrace();
                
            }
        } 
    }


    private int obtenirProductId(String designationProduit) throws SQLException {
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            String query = "SELECT id FROM produit WHERE designationProduit = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, designationProduit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int productId = rs.getInt("id");
                stmt.close();
                return productId;
            }
            stmt.close();
        }
        throw new SQLException("Produit non trouvé pour la désignation : " + designationProduit);
    }

    private int obtenirCmdApproDepotId(int idCommande) throws SQLException {
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            String query = "SELECT id FROM cmdapprodepot WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idCommande);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idCmdApproDepot = rs.getInt("id");
                stmt.close();
                return idCmdApproDepot;
            }
            stmt.close();
        }
        throw new SQLException("Commande non trouvée pour l'identifiant : " + idCommande);
    }
}

