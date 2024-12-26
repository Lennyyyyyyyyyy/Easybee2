package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SeDeconnecter extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SeDeconnecter frame = new SeDeconnecter();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SeDeconnecter() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 250);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblConfirmation = new JLabel("Êtes-vous sûr de vouloir vous déconnecter ?");
        lblConfirmation.setFont(new Font("Arial", Font.PLAIN, 14));
        lblConfirmation.setBounds(75, 80, 300, 25);
        contentPane.add(lblConfirmation);

        JButton btnOui = new JButton("Oui");
        btnOui.setForeground(Color.WHITE);
        btnOui.setBackground(Color.BLACK);
        btnOui.setFont(new Font("Arial", Font.BOLD, 12));
        btnOui.setBounds(110, 120, 80, 25);
        btnOui.addActionListener(this::deconnexion);
        contentPane.add(btnOui);

        JButton btnNon = new JButton("Non");
        btnNon.setForeground(Color.WHITE);
        btnNon.setBackground(Color.BLACK);
        btnNon.setFont(new Font("Arial", Font.BOLD, 12));
        btnNon.setBounds(220, 120, 80, 25);
        btnNon.addActionListener(e -> retourMenu());
        contentPane.add(btnNon);
    }

    private void deconnexion(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Déconnexion réussie.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        new SeConnecter().setVisible(true);  
        dispose();  
    }

    private void retourMenu() {

        if (SeConnecter.typeUtilisateurConnecte == 1) {

            new MenuPrincipalVendeur().setVisible(true);
        } else if (SeConnecter.typeUtilisateurConnecte == 2) {

            new MenuPrincipalPreparateur().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Accès interdit pour ce rôle.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }
}
