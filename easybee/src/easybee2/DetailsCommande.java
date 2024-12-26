package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class DetailsCommande extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table_1;
    private DefaultTableModel model;
    private JTextField textField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DetailsCommande frame = new DetailsCommande();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public DetailsCommande() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 809, 500);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 800, 77);
        panel.setBackground(new Color(240, 240, 240));
        contentPane.add(panel);
        panel.setLayout(null);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setForeground(Color.BLACK);
        btnRetour.setFont(new Font("Arial", Font.PLAIN, 11));
        btnRetour.setBackground(Color.WHITE);
        btnRetour.setBounds(0, 0, 89, 23);
        btnRetour.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	 dispose(); 
                 new MenuPrincipalVendeur().setVisible(true);  
             }
    
        });
        panel.add(btnRetour);

        JTextPane txtpnListeDesProduit1 = new JTextPane();
        txtpnListeDesProduit1.setText("Detail d'une commande");
        txtpnListeDesProduit1.setFont(new Font("Arial", Font.BOLD, 14));
        txtpnListeDesProduit1.setBackground(new Color(240, 240, 240));
        txtpnListeDesProduit1.setBounds(20, 32, 300, 20);
        panel.add(txtpnListeDesProduit1);

        model = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "numero commande", "Date commande", "Status commande", "quantite preparer", "Quantite recup", "produit"
            }
        );

        table_1 = new JTable(model);
        table_1.setBounds(1, 54, 800, 200);
        JScrollPane scrollPane = new JScrollPane(table_1);
        scrollPane.setBounds(32, 98, 730, 226);
        contentPane.add(scrollPane);

        textField = new JTextField();
        textField.setBounds(232, 374, 51, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        // Déclaration de la JComboBox
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Arial", Font.BOLD, 11));
        comboBox.setBounds(293, 373, 150, 22);
        contentPane.add(comboBox);

        comboBox.addItem("Livraison complète");
        comboBox.addItem("Livraison partielle");

        // Bouton "Valider"
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Récupérer l'option sélectionnée dans le JComboBox
                String selectedOption = (String) comboBox.getSelectedItem();

                // Connexion à la base de données
                Connection conn = ConnexionBaseDeDonnees.getConnection();
                if (conn != null) {
                    try {
                        // Définir le statutCommande en fonction de la sélection dans le JComboBox
                        String statutCommande = "";
                        if (selectedOption.equals("Livraison complète")) {
                            statutCommande = "Complète";  // Statut pour livraison complète
                        } else if (selectedOption.equals("Livraison partielle")) {
                            statutCommande = "Partielle";  // Statut pour livraison partielle
                        }

                        // Requête SQL pour mettre à jour le statut de la commande
                        String query = "UPDATE cmdapprodepot SET statutCommande = ? WHERE id = ?";

                        // Préparer la requête
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, statutCommande);  // Mettre le statut dans le paramètre
                        stmt.setInt(2, Integer.parseInt(textField.getText()));  // Utiliser l'ID dans le champ textField

                        // Exécuter la mise à jour
                        int rowsUpdated = stmt.executeUpdate();

                        // Vérifier si la mise à jour a réussi
                        if (rowsUpdated > 0) {
                            System.out.println("Le statut de la commande a été mis à jour avec succès.");
                        } else {
                            System.out.println("Aucune commande trouvée avec cet ID.");
                        }

                        // Fermer les ressources
                        stmt.close();
                        conn.close();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Erreur : Connexion à la base de données non établie.");
                }
            }
        });
        btnValider.setBounds(452, 373, 89, 23);
        contentPane.add(btnValider);

        JTextPane txtpnEntrezLeNumero = new JTextPane();
        txtpnEntrezLeNumero.setFont(new Font("Arial", Font.BOLD, 11));
        txtpnEntrezLeNumero.setText("Entrez le numero de commande :");
        txtpnEntrezLeNumero.setBackground(new Color(255, 255, 255));
        txtpnEntrezLeNumero.setBounds(42, 373, 209, 20);
        contentPane.add(txtpnEntrezLeNumero);

        // Appel de la méthode pour charger les données de la base de données
        chargerDonnees();
    }

    private void chargerDonnees() {
        try {
            // Connexion à la base de données 
            Connection conn = ConnexionBaseDeDonnees.getConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = """
                    SELECT 
            cmdapprodepot.id AS id_commande,
            cmdapprodepot.dateCommande AS date_commande,
            cmdapprodepot.statutCommande AS statut_commande,
            detaillivraison.qteRecep AS quantite_recuperee,
            detaillivraison.qtePrepa AS quantite_preparee,
            produit.designationProduit AS designation_produit
        FROM cmdapprodepot
        JOIN detaillivraison ON cmdapprodepot.id = detaillivraison.idCmdeAppropDepot
        JOIN produit ON detaillivraison.idProduit = produit.id
        WHERE cmdapprodepot.statutCommande LIKE 'Préparée';
    """;

                ResultSet rs = stmt.executeQuery(query);

                // Parcourir les résultats et ajouter les données au modèle de table
                while (rs.next()) {
                    int idCommande = rs.getInt("id_commande");
                    Date dateCommande = rs.getDate("date_commande");
                    String statutCommande = rs.getString("statut_commande");
                    int quantitePreparee = rs.getInt("quantite_preparee");
                    int quantiteRecuperee = rs.getInt("quantite_recuperee");
                    String designationProduit = rs.getString("designation_produit");

                    model.addRow(new Object[]{
                        idCommande,
                        dateCommande,
                        statutCommande,
                        quantitePreparee,
                        quantiteRecuperee,
                        designationProduit
                    });
                }

                rs.close();
                stmt.close();
                conn.close();
            } else {
                System.out.println("Connexion à la base de données échouée.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}