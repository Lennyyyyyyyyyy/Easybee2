package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MenuPrincipalVendeur extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuPrincipalVendeur frame = new MenuPrincipalVendeur();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuPrincipalVendeur() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 615, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnFormulaireCommande = new JButton("Formulaire de commande");
		btnFormulaireCommande.setBackground(new Color(255, 255, 255));
		btnFormulaireCommande.setForeground(new Color(0, 0, 0));
		btnFormulaireCommande.setFont(new Font("Arial", Font.BOLD, 10));
		btnFormulaireCommande.setBounds(0, 0, 300, 100);
		contentPane.add(btnFormulaireCommande);
		btnFormulaireCommande.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                FormulaireCommande frame = new FormulaireCommande();
                frame.setVisible(true);  
            }
        });
		
		JButton btnDetailsCommande = new JButton("Afficher le détails d'une commande et/ou valider une commande partielle ou complète");
		btnDetailsCommande.setForeground(Color.BLACK);
		btnDetailsCommande.setFont(new Font("Arial", Font.BOLD, 10));
		btnDetailsCommande.setBackground(Color.WHITE);
		btnDetailsCommande.setBounds(0, 100, 300, 100);
		contentPane.add(btnDetailsCommande);
		btnDetailsCommande.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                DetailsCommande frame = new DetailsCommande();
                frame.setVisible(true);  
            }
        });
		
		JButton btnHistoriqueReaprovisionnement = new JButton("Historique de réaprovisionnement");
		btnHistoriqueReaprovisionnement.setForeground(Color.BLACK);
		btnHistoriqueReaprovisionnement.setFont(new Font("Arial", Font.BOLD, 10));
		btnHistoriqueReaprovisionnement.setBackground(Color.WHITE);
		btnHistoriqueReaprovisionnement.setBounds(300, 0, 300, 100);
		contentPane.add(btnHistoriqueReaprovisionnement);
		btnHistoriqueReaprovisionnement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                HistoriqueReaprovisionnement frame = new HistoriqueReaprovisionnement();
                frame.setVisible(true);  
            }
        });
		
		JButton btnAfficherBonLivraison = new JButton("Afficher les bons de livraison");
		btnAfficherBonLivraison.setForeground(Color.BLACK);
		btnAfficherBonLivraison.setFont(new Font("Arial", Font.BOLD, 10));
		btnAfficherBonLivraison.setBackground(Color.WHITE);
		btnAfficherBonLivraison.setBounds(300, 100, 300, 100);
		contentPane.add(btnAfficherBonLivraison);
		btnAfficherBonLivraison.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                AfficherBonLivraison frame = new AfficherBonLivraison();
                frame.setVisible(true);  
            }
        });
		
		JButton btnSeDeconnecter = new JButton("Se déconnecter");
		btnSeDeconnecter.setForeground(new Color(255, 255, 255));
		btnSeDeconnecter.setFont(new Font("Arial", Font.BOLD, 10));
		btnSeDeconnecter.setBackground(new Color(0, 0, 0));
		btnSeDeconnecter.setBounds(300, 200, 300, 50);
		contentPane.add(btnSeDeconnecter);
		btnSeDeconnecter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                SeDeconnecter frame = new SeDeconnecter();
                frame.setVisible(true);  
            }
        });

		
		JButton btnModifierMotDePasse = new JButton("Modifier son mot de passe");
		btnModifierMotDePasse.setForeground(Color.BLACK);
		btnModifierMotDePasse.setFont(new Font("Arial", Font.BOLD, 10));
		btnModifierMotDePasse.setBackground(Color.WHITE);
		btnModifierMotDePasse.setBounds(300, 250, 300, 50);
		contentPane.add(btnModifierMotDePasse);
		btnModifierMotDePasse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                ModifierMotDePasse frame = new ModifierMotDePasse();
                frame.setVisible(true);  
            }
        });
		
		JButton btnAnnulerCommande = new JButton("Annuler une commande");
		btnAnnulerCommande.setForeground(Color.BLACK);
		btnAnnulerCommande.setFont(new Font("Arial", Font.BOLD, 10));
		btnAnnulerCommande.setBackground(Color.WHITE);
		btnAnnulerCommande.setBounds(0, 200, 300, 100);
		contentPane.add(btnAnnulerCommande);
		btnAnnulerCommande.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                AnnulerCommande frame = new AnnulerCommande();
                frame.setVisible(true);  
            }
        });
	}
}
