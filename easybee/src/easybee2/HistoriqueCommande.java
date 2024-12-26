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
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HistoriqueCommande extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table_1;
    private DefaultTableModel model;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HistoriqueCommande frame = new HistoriqueCommande();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public HistoriqueCommande() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 600, 54);
        panel.setBackground(new Color(240, 240, 240));
        contentPane.add(panel);
        panel.setLayout(null);

        // Gestion du bouton "Retour"
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

        JTextPane txtpnListeDesCommande = new JTextPane();
        txtpnListeDesCommande.setText("Historique des commandes préparées");
        txtpnListeDesCommande.setFont(new Font("Arial", Font.BOLD, 14));
        txtpnListeDesCommande.setBackground(new Color(240, 240, 240));
        txtpnListeDesCommande.setBounds(20, 32, 300, 20);
        panel.add(txtpnListeDesCommande);

        // Initialiser le modèle de table avec toutes les colonnes nécessaires
        model = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Numéro de commande", "Date commande", "Statut commande", "Nom salarié", "Prénom salarié", 
                "Nom produit"
            }
        );

        // Créer la table et appliquer le modèle
        table_1 = new JTable(model);
        table_1.setBounds(1, 54, 600, 200);

        // Créer un JScrollPane pour permettre le défilement
        JScrollPane scrollPane = new JScrollPane(table_1);
        scrollPane.setBounds(27, 78, 526, 135);

        contentPane.add(scrollPane);

        // Appel de la méthode pour charger les données de la base de données
        chargerDonnees();
    }

    private void chargerDonnees() {
        try {
            // Connexion à la base de données
            Connection conn = ConnexionBaseDeDonnees.getConnection(); // Assurez-vous que cette classe est correctement implémentée
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Requête SQL pour obtenir les données des commandes
                String query = """
                    SELECT
                        cmdapprodepot.id AS numero_commande,
                        cmdapprodepot.dateCommande AS date_commande,
                        cmdapprodepot.statutCommande AS statut_commande,
                        salarie.nomSalarie AS nom_salarie,
                        salarie.prenomSalarie AS prenom_salarie,
                        produit.designationProduit AS nom_produit
                    FROM 
                        cmdApproDepot
                    JOIN 
                        salarie ON cmdapprodepot.idSalarie = salarie.id
                    JOIN 
                        detailsProduit ON cmdapprodepot.id = detailsProduit.idCommande
                    JOIN
                        produit ON detailsProduit.idProduit = produit.id
                    WHERE 
                        cmdapprodepot.statutCommande = 'Préparée';
                """;

                ResultSet rs = stmt.executeQuery(query);

                // Parcourir les résultats et ajouter les données au modèle de table
                while (rs.next()) {
                    int numeroCommande = rs.getInt("numero_commande");
                    Date dateCommande = rs.getDate("date_commande");
                    String statutCommande = rs.getString("statut_commande");
                    String nomSalarie = rs.getString("nom_salarie");
                    String prenomSalarie = rs.getString("prenom_salarie");
                    String nomProduit = rs.getString("nom_produit");

                    // Ajouter les données au modèle de table
                    model.addRow(new Object[]{
                        numeroCommande,
                        dateCommande,
                        statutCommande,
                        nomSalarie,
                        prenomSalarie,
                        nomProduit
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
