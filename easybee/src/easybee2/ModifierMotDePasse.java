package easybee2;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class ModifierMotDePasse extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPasswordField champAncienMotDePasse;         
    private JPasswordField champNouveauMotDePasse;       
    private JPasswordField champConfirmerMotDePasse;     
    private JButton boutonValider;

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ModifierMotDePasse frame = new ModifierMotDePasse();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ModifierMotDePasse() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 350, 320);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblAncienMotDePasse = new JLabel("Ancien mot de passe");
        lblAncienMotDePasse.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAncienMotDePasse.setBounds(20, 50, 150, 20);
        contentPane.add(lblAncienMotDePasse);

        champAncienMotDePasse = new JPasswordField();
        champAncienMotDePasse.setFont(new Font("Arial", Font.PLAIN, 11));
        champAncienMotDePasse.setBounds(20, 70, 300, 25);
        contentPane.add(champAncienMotDePasse);

        JLabel lblNouveauMotDePasse = new JLabel("Nouveau mot de passe");
        lblNouveauMotDePasse.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNouveauMotDePasse.setBounds(20, 110, 150, 20);
        contentPane.add(lblNouveauMotDePasse);

        champNouveauMotDePasse = new JPasswordField();
        champNouveauMotDePasse.setFont(new Font("Arial", Font.PLAIN, 11));
        champNouveauMotDePasse.setBounds(20, 130, 300, 25);
        contentPane.add(champNouveauMotDePasse);

        JLabel lblConfirmerMotDePasse = new JLabel("Confirmer le mot de passe");
        lblConfirmerMotDePasse.setFont(new Font("Arial", Font.PLAIN, 12));
        lblConfirmerMotDePasse.setBounds(20, 170, 150, 20);
        contentPane.add(lblConfirmerMotDePasse);

        champConfirmerMotDePasse = new JPasswordField();
        champConfirmerMotDePasse.setFont(new Font("Arial", Font.PLAIN, 11));
        champConfirmerMotDePasse.setBounds(20, 190, 300, 25);
        contentPane.add(champConfirmerMotDePasse);

        boutonValider = new JButton("Valider");
        boutonValider.setForeground(Color.WHITE);
        boutonValider.setBackground(new Color(0, 128, 255));
        boutonValider.setFont(new Font("Arial", Font.BOLD, 12));
        boutonValider.setBounds(115, 230, 100, 25);
        boutonValider.addActionListener(e -> changerMotDePasse());
        contentPane.add(boutonValider);
        
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
        contentPane.add(btnRetour);
    }

    private void changerMotDePasse() {
    	
        String ancienMotDePasse = new String(champAncienMotDePasse.getPassword());
        String nouveauMotDePasse = new String(champNouveauMotDePasse.getPassword());
        String confirmationMotDePasse = new String(champConfirmerMotDePasse.getPassword());

        if (ancienMotDePasse.isEmpty() || nouveauMotDePasse.isEmpty() || confirmationMotDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nouveauMotDePasse.equals(confirmationMotDePasse)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String identifiantUtilisateur = SeConnecter.identifiantUtilisateurConnecte; 

        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
            	
                String query = "SELECT motDePasse FROM Salarie WHERE identifiant = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, identifiantUtilisateur);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String motDePasseActuel = rs.getString("motDePasse");

                    if (motDePasseActuel.equals(ancienMotDePasse)) {
 
                        String updateQuery = "UPDATE Salarie SET motDePasse = ? WHERE identifiant = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                        updateStmt.setString(1, nouveauMotDePasse);
                        updateStmt.setString(2, identifiantUtilisateur);
                        updateStmt.executeUpdate();
                        updateStmt.close();

                        JOptionPane.showMessageDialog(this, "Mot de passe mis à jour avec succès.");
                        dispose();
                        new SeConnecter().setVisible(true); 
                    } else {
                        JOptionPane.showMessageDialog(this, "L'ancien mot de passe est incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } 

                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } 
    }
}
