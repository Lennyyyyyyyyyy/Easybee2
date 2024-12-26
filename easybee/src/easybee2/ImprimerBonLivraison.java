package easybee2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

public class ImprimerBonLivraison extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> ComboBoxIdBonLivraison;
    private int selectedBonLivraisonId = -1;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ImprimerBonLivraison frame = new ImprimerBonLivraison();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ImprimerBonLivraison() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 535, 250);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel fond = new JPanel();
        fond.setBounds(0, 0, 520, 75);
        contentPane.add(fond);
        fond.setLayout(null);

        JLabel titre = new JLabel("Imprimer un bon de livraison");
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


        JLabel etape1 = new JLabel("1 - Sélectionner un bon de livraison");
        etape1.setFont(new Font("Arial", Font.ITALIC, 12));
        etape1.setBounds(25, 85, 250, 20);
        contentPane.add(etape1);

        ComboBoxIdBonLivraison = new JComboBox<>();
        ComboBoxIdBonLivraison.setForeground(Color.BLACK);
        ComboBoxIdBonLivraison.setFont(new Font("Arial", Font.PLAIN, 12));
        ComboBoxIdBonLivraison.setBounds(25, 115, 200, 20);
        ComboBoxIdBonLivraison.addActionListener(e -> chargerBonLivraisonDetails());
        contentPane.add(ComboBoxIdBonLivraison);

        JButton btnImprimer = new JButton("Imprimer");
        btnImprimer.setForeground(Color.WHITE);
        btnImprimer.setBackground(new Color(0, 123, 255));
        btnImprimer.setFont(new Font("Arial", Font.PLAIN, 12));
        btnImprimer.setBounds(250, 115, 185, 20);
        btnImprimer.addActionListener(e -> printBonLivraison());
        contentPane.add(btnImprimer);

        chargerBonLivraisonListe();
    }

    private void chargerBonLivraisonListe() {
        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "SELECT bondelivraison.id FROM bondelivraison";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                ComboBoxIdBonLivraison.removeAllItems();

                while (rs.next()) {
                    String bonLivraisonId = rs.getString("id");
                    ComboBoxIdBonLivraison.addItem(bonLivraisonId);
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 
    }

    private void chargerBonLivraisonDetails() {
        String selectedBonLivraison = (String) ComboBoxIdBonLivraison.getSelectedItem();
        if (selectedBonLivraison == null || selectedBonLivraison.isEmpty()) {
            return;
        }

        selectedBonLivraisonId = Integer.parseInt(selectedBonLivraison);
    }

    private void printBonLivraison() {
        if (selectedBonLivraisonId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un bon de livraison à imprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex >= 1) {
                    return NO_SUCH_PAGE;
                }

                graphics.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
                graphics.setFont(new Font("Arial", Font.PLAIN, 12));

                graphics.drawString("Bon de livraison", 100, 100);

                String commandeId = obtenirCommandeIdFromBonLivraison(selectedBonLivraisonId);
                graphics.drawString("Commande : " + commandeId, 100, 130);

                graphics.drawString("Date de Livraison : " + java.time.LocalDate.now(), 100, 160);

                Connection conn = ConnexionBaseDeDonnees.getConnection();
                if (conn != null) {
                    try {
                    	String query = "SELECT " +
                    		    "produit.designationProduit, " +
                    		    "detailsproduit.qteCmd, " +
                    		    "detaillivraison.qtePrepa " +
                    		    "FROM " +
                    		    "detaillivraison " +
                    		    "JOIN detailsproduit ON " +
                    		    "detaillivraison.idCmdeAppropDepot = detailsproduit.idCommande " +
                    		    "AND detaillivraison.idProduit = detailsproduit.idProduit " +
                    		    "JOIN produit ON " +
                    		    "detailsproduit.idProduit = produit.id " +
                    		    "WHERE " +
                    		    "detaillivraison.idBonLivraison = ? " +
                    		    "ORDER BY produit.designationProduit";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setInt(1, selectedBonLivraisonId);
                        ResultSet rs = stmt.executeQuery();

                        int y = 190;
                        while (rs.next()) {
                            String designationProduit = rs.getString("designationProduit");
                            int qteCmd = rs.getInt("qteCmd");
                            int qtePrepa = rs.getInt("qtePrepa");

                            graphics.drawString("Produit: " + designationProduit, 100, y);
                            graphics.drawString("QteCmd: " + qteCmd, 300, y);
                            graphics.drawString("QtePrepa: " + qtePrepa, 500, y);
                            y += 30;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                return PAGE_EXISTS;
            }
        });

        boolean isPrinted = job.printDialog();
        if (isPrinted) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    private String obtenirCommandeIdFromBonLivraison(int bonLivraisonId) {
        String commandeId = null;

        Connection conn = ConnexionBaseDeDonnees.getConnection();
        if (conn != null) {
            try {
                String query = "SELECT idCmdeAppropDepot FROM detaillivraison WHERE idBonLivraison = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, bonLivraisonId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    commandeId = rs.getString("idCmdeAppropDepot");
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return commandeId;
    }
}