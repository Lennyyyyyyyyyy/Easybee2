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

public class MenuPrincipalPreparateur extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuPrincipalPreparateur frame = new MenuPrincipalPreparateur();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuPrincipalPreparateur() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 615, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnAfficherListeProduits = new JButton("Afficher la liste des produits");
		btnAfficherListeProduits.setBackground(new Color(255, 255, 255));
		btnAfficherListeProduits.setForeground(new Color(0, 0, 0));
		btnAfficherListeProduits.setFont(new Font("Arial", Font.BOLD, 10));
		btnAfficherListeProduits.setBounds(0, 0, 300, 100);
		contentPane.add(btnAfficherListeProduits);
		btnAfficherListeProduits.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                ListeProduit frame = new ListeProduit();
                frame.setVisible(true);  
            }
        });
		
		JButton btnAfficherCommandes = new JButton("Afficher la liste des commandes à préparer");
		btnAfficherCommandes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAfficherCommandes.setForeground(Color.BLACK);
		btnAfficherCommandes.setFont(new Font("Arial", Font.BOLD, 10));
		btnAfficherCommandes.setBackground(Color.WHITE);
		btnAfficherCommandes.setBounds(300, 0, 300, 100);
		contentPane.add(btnAfficherCommandes);
		btnAfficherCommandes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                ListeCommande frame = new ListeCommande();
                frame.setVisible(true);  
            }
        });
		
		JButton btnAffecterCommande = new JButton("Affecter une commande à préparer");
		btnAffecterCommande.setForeground(Color.BLACK);
		btnAffecterCommande.setFont(new Font("Arial", Font.BOLD, 10));
		btnAffecterCommande.setBackground(Color.WHITE);
		btnAffecterCommande.setBounds(0, 100, 300, 100);
		contentPane.add(btnAffecterCommande);
		btnAffecterCommande.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                AffecterCommande frame = new AffecterCommande();
                frame.setVisible(true);  
            }
        });
		
		JButton btnSaisirBonLivraison = new JButton("Saisir un bon de livraison");
		btnSaisirBonLivraison.setForeground(Color.BLACK);
		btnSaisirBonLivraison.setFont(new Font("Arial", Font.BOLD, 10));
		btnSaisirBonLivraison.setBackground(Color.WHITE);
		btnSaisirBonLivraison.setBounds(300, 100, 300, 100);
		contentPane.add(btnSaisirBonLivraison);
		btnSaisirBonLivraison.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                SaisieBonLivraison frame = new SaisieBonLivraison();
                frame.setVisible(true);  
            }
        });
		
		JButton btnImprimerBonLivraison = new JButton("Imprimer un bon de livraison");
		btnImprimerBonLivraison.setForeground(Color.BLACK);
		btnImprimerBonLivraison.setFont(new Font("Arial", Font.BOLD, 10));
		btnImprimerBonLivraison.setBackground(Color.WHITE);
		btnImprimerBonLivraison.setBounds(0, 200, 300, 100);
		contentPane.add(btnImprimerBonLivraison);
		btnImprimerBonLivraison.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                ImprimerBonLivraison frame = new ImprimerBonLivraison();
                frame.setVisible(true);  
            }
        });
		
		JButton btnHistoriqueCommande = new JButton("Historique des commandes préparées");
		btnHistoriqueCommande.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnHistoriqueCommande.setForeground(Color.BLACK);
		btnHistoriqueCommande.setFont(new Font("Arial", Font.BOLD, 10));
		btnHistoriqueCommande.setBackground(Color.WHITE);
		btnHistoriqueCommande.setBounds(300, 200, 300, 100);
		contentPane.add(btnHistoriqueCommande);
		btnHistoriqueCommande.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                HistoriqueCommande frame = new HistoriqueCommande();
                frame.setVisible(true);  
            }
        });
		
		JButton btnSeDeconnecter = new JButton("Se déconnecter");
		btnSeDeconnecter.setForeground(new Color(255, 255, 255));
		btnSeDeconnecter.setFont(new Font("Arial", Font.BOLD, 10));
		btnSeDeconnecter.setBackground(new Color(0, 0, 0));
		btnSeDeconnecter.setBounds(0, 300, 300, 50);
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
		btnModifierMotDePasse.setBounds(0, 350, 300, 50);
		contentPane.add(btnModifierMotDePasse);
		btnModifierMotDePasse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose ();
                ModifierMotDePasse frame = new ModifierMotDePasse();
                frame.setVisible(true);  
            }
        });
	}
}
