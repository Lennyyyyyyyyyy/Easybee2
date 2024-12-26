package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SeConnecter extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField identifiantField;  
    private JPasswordField motDePasseField;
    public static int typeUtilisateurConnecte;
    public static String identifiantUtilisateurConnecte;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SeConnecter frame = new SeConnecter();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SeConnecter() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Se connecter");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBounds(140, 30, 120, 20);
        contentPane.add(lblTitle);

        JLabel lblIdentifiant = new JLabel("Identifiant");
        lblIdentifiant.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIdentifiant.setBounds(50, 80, 80, 20);
        contentPane.add(lblIdentifiant);

        identifiantField = new JTextField();  
        identifiantField.setFont(new Font("Arial", Font.PLAIN, 12));
        identifiantField.setBounds(150, 80, 200, 20);
        contentPane.add(identifiantField);
        identifiantField.setColumns(10);

        JLabel lblMotDePasse = new JLabel("Mot de passe");
        lblMotDePasse.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMotDePasse.setBounds(50, 120, 80, 20);
        contentPane.add(lblMotDePasse);

        motDePasseField = new JPasswordField();
        motDePasseField.setFont(new Font("Arial", Font.PLAIN, 12));
        motDePasseField.setBounds(150, 120, 200, 20);
        contentPane.add(motDePasseField);

        JButton btnConnexion = new JButton("Se connecter");
        btnConnexion.setBackground(Color.WHITE);
        btnConnexion.setFont(new Font("Arial", Font.PLAIN, 12));
        btnConnexion.setBounds(150, 160, 120, 25);
        btnConnexion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authentifierUtilisateur();
            }
        });
        contentPane.add(btnConnexion);
    }

    private void authentifierUtilisateur() {
        String identifiant = identifiantField.getText();  
        String motDePasse = new String(motDePasseField.getPassword()); 

        if (identifiant.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "SELECT salarie.id, salarie.identifiant, salarie.motDePasse, typesalarie.id "
                             + "FROM salarie "
                             + "JOIN typesalarie ON salarie.idType = typesalarie.id "
                             + "WHERE salarie.identifiant = ? AND salarie.motDePasse = ?";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, identifiant);  
                stmt.setString(2, motDePasse);   

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int typeId = rs.getInt("id");
                    typeUtilisateurConnecte = typeId;
                    identifiantUtilisateurConnecte = identifiant;

                    if (typeId == 1) { 
                        dispose(); 
                        new MenuPrincipalVendeur().setVisible(true);
                    } else if (typeId == 2) { 
                        dispose(); 
                        new MenuPrincipalPreparateur().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Accès interdit pour ce rôle.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Identifiant ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();  
            }
        } 
    }
}