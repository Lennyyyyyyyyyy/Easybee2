package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

public class AfficherBonLivraison extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AfficherBonLivraison frame = new AfficherBonLivraison();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AfficherBonLivraison() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel fond = new JPanel();
        fond.setBounds(0, 0, 585, 70);
        contentPane.add(fond);
        fond.setLayout(null);

        JLabel titre = new JLabel("Afficher la liste des bons de livraison");
        titre.setFont(new Font("Arial", Font.BOLD, 12));
        titre.setBounds(20, 30, 300, 20);
        fond.add(titre);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setFont(new Font("Arial", Font.PLAIN, 11));
        btnRetour.setBounds(0, 0, 90, 25);
        btnRetour.setBackground(Color.WHITE);
        btnRetour.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new MenuPrincipalVendeur().setVisible(true); 
            }
        });
        fond.add(btnRetour);

        table = new JTable();
        model = new DefaultTableModel();
        model.addColumn("id bon de livraison");
        model.addColumn("date de livraison");
        table.setModel(model);
        table.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 80, 550, 250);
        contentPane.add(scrollPane);

        chargerDonneesTableau();
    }

    private void chargerDonneesTableau() {
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
              
                String query = "SELECT id, dateLivraison FROM bondelivraison";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                model.setRowCount(0); 

                while (rs.next()) {
                    int idBonLivraison = rs.getInt("id");
                    String dateLivraison = rs.getString("dateLivraison");

                    model.addRow(new Object[]{idBonLivraison, dateLivraison});
                }

                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erreur : Connexion à la base de données non établie.");
        }
    }
}
