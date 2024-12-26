package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.JScrollPane; // Importer JScrollPane pour la barre de défilement
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ListeProduit extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table_1;
    private DefaultTableModel model;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ListeProduit frame = new ListeProduit();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ListeProduit() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 400); // Agrandir la taille de la fenêtre pour voir plus de données
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 800, 77); // Ajuster la taille du panneau
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
                new MenuPrincipalPreparateur().setVisible(true);  
            }
        });
        panel.add(btnRetour);

        JTextPane txtpnListeDesProduit = new JTextPane();
        txtpnListeDesProduit.setText("Liste des produits");
        JTextPane txtpnListeDesProduit1 = new JTextPane();
        txtpnListeDesProduit1.setText("Liste des produits");
        txtpnListeDesProduit1.setFont(new Font("Arial", Font.BOLD, 14));
        txtpnListeDesProduit1.setBackground(new Color(240, 240, 240));
        txtpnListeDesProduit1.setBounds(20, 32, 300, 20);
      panel.add(txtpnListeDesProduit1);

        // Initialiser le modèle de table avec les nouvelles colonnes
        model = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Nom Produit", "Code Produit", "Stock en Magasin", "Stock en Entrepôt", "Stock Total", "Catégorie"
            }
        );

        // Créer la table et appliquer le modèle
        table_1 = new JTable(model);
        table_1.setBounds(1, 54, 800, 200); // Ajuster la taille de la table
     // Créer un JScrollPane pour permettre le défilement
        JScrollPane scrollPane = new JScrollPane(table_1);
        scrollPane.setBounds(32, 98, 730, 226); // Ajuster la taille du JScrollPane

        contentPane.add(scrollPane);


     

        contentPane.add(scrollPane);

        // Appel de la méthode pour charger les données de la base de données 
        chargerDonnees();
    }

    private void chargerDonnees() {
        try {
            // Connexion à la base de données 
            Connection conn = ConnexionBaseDeDonnees.getConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Requête SQL corrigée pour inclure la catégorie du produit
                String query = """
                		SELECT
                		produit.designationProduit AS nom_produit , 
                		produit.codeProduit AS code_produit , 
                		produit.stockMag AS stock_magasin ,
                		produit.stockEntrepot AS stock_entrepot , 
                		(produit.stockMag + produit.stockEntrepot) AS totalStock,
                		categorieproduit.nomCategorie AS categorie_produit
                	FROM produit
                	JOIN categorieproduit ON produit.idCategorie = categorieproduit.id;


                    
                """;

                ResultSet rs = stmt.executeQuery(query);

                // Parcourir les résultats et ajouter les données au modèle de table
                while (rs.next()) {
                    String nomProduit = rs.getString("nom_produit");
                    String codeProduit = rs.getString("code_produit");
                    int stockMagasin = rs.getInt("stock_magasin");
                    int stockEntrepot = rs.getInt("stock_entrepot");
                    int stockTotal = rs.getInt("totalStock");
                    String categorieProduit = rs.getString("categorie_produit");

                    // Ajouter les données au modèle de table
                    model.addRow(new Object[]{
                        nomProduit,
                        codeProduit,
                        stockMagasin,
                        stockEntrepot,
                        stockTotal,
                        categorieProduit
                    });
                }

                // Fermer la connexion et le statement
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