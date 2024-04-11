/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import config.Koneksi;
import views.Dashboard;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

/**
 *
 * @author KELOMPOK 2 R6Q 2021 - KELOMPOK KKP R8Q 2022
 */
public class Produk extends javax.swing.JFrame {
    
    private PreparedStatement pst = null;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet res = null;
    private String query = null;
    private boolean verif;
    private String getId;
    private int getLastId;

    /** Creates new form StoreForm */
    public Produk() {
        initComponents();
        
        System.out.println(lastId());
    }
    
//  Overloading construct..  
    public Produk(String label, String data[]){
        initComponents();
        setStyle(label);
        setFilled(label, data);
    }
    
    private void setStyle(String label){
        if (label.equals("Tambah")){
            this.setTitle("Form Tambah Data");
            storeTitle.setText("Tambah "+storeTitle.getText());
            storeTitle.setBackground(new java.awt.Color(62,70,225));
        }else if (label.equals("Ubah")){
            this.setTitle("Form Ubah Data");
            storeTitle.setText("Ubah "+storeTitle.getText());
            storeTitle.setBackground(new java.awt.Color(0,204,153));
            btnSimpan.setBackground(new java.awt.Color(0,204,153));
            btnBatal.setBackground(new java.awt.Color(0,204,153));
            labelId.setForeground(new java.awt.Color(0,204,153));
            labelNama.setForeground(new java.awt.Color(0,204,153));
            labelJenis.setForeground(new java.awt.Color(0,204,153));
            labelHarga.setForeground(new java.awt.Color(0,204,153));
            inputId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputJenis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputHarga.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
        }
    }
    
    private void setFilled(String label, String data[]){
        if (label.equals("Ubah")){
            this.getId = unformatKode(data[0]);
            inputId.setText(data[0]);
            inputNama.setText(data[1]);
            if (data[2].equals("Coffe")){
                inputJenis.setSelectedIndex(0);
            }else if (data[2].equals("Milkshake")){
                inputJenis.setSelectedIndex(1);
            }else if (data[2].equals("Snack")){
                inputJenis.setSelectedIndex(2);
            }else if (data[2].equals("Aneka Nasi")){
                inputJenis.setSelectedIndex(3);
            }
            inputHarga.setText(data[3]);
        }else if (label.equals("Tambah")){
            String id = Dashboard.getKode("Produk", String.valueOf(lastId()));
            inputId.setText(id);
        }
    }
    
    private int lastId(){
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT MAX(id) FROM produk";
            res = stmt.executeQuery(query);
            if (res.next()){
                int getMax = Integer.valueOf(res.getString("MAX(id)"))+1;
                this.getLastId = getMax;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return this.getLastId;
    }
    
    private static String unformatKode(String id){
        String res[] = id.split("(?<=\\D)(?=\\d)");
        return res[1];
    }
    
    private void tambahData(){
        conn = Koneksi.getKoneksi();
        try{
            query = "INSERT INTO produk (id, id_kategori, nama, harga) VALUES (?,?,?,?)";
            pst = conn.prepareStatement(query);
            
            pst.setString(1, String.valueOf(lastId()));
            pst.setString(2, String.valueOf(inputJenis.getSelectedIndex()+1));
            pst.setString(3, inputNama.getText());
            pst.setString(4, inputHarga.getText());
            if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan ke database "+conn.getCatalog());
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelProduk);
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal ditambahkan: "+pst.getWarnings());
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void ubahData(){
        String id = getId;
        try{
            conn = Koneksi.getKoneksi();
            query = "UPDATE produk SET nama=?, id_kategori=?, harga=? WHERE id=?";
            pst = conn.prepareStatement(query);
            pst.setString(1, inputNama.getText());
            pst.setString(2, String.valueOf(inputJenis.getSelectedIndex()+1));
            pst.setString(3, inputHarga.getText());
            pst.setString(4, id);
            if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelProduk);
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal diubah: "+pst.getWarnings());
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        storeTitle = new javax.swing.JLabel();
        labelId = new javax.swing.JLabel();
        inputId = new javax.swing.JTextField();
        labelNama = new javax.swing.JLabel();
        inputNama = new javax.swing.JTextField();
        labelJenis = new javax.swing.JLabel();
        inputJenis = new javax.swing.JComboBox<>();
        labelHarga = new javax.swing.JLabel();
        inputHarga = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JLabel();
        btnBatal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        bg.setBackground(new java.awt.Color(35, 36, 54));
        bg.setVerifyInputWhenFocusTarget(false);

        storeTitle.setBackground(new java.awt.Color(62, 70, 225));
        storeTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        storeTitle.setForeground(new java.awt.Color(245, 245, 250));
        storeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        storeTitle.setText("Data Produk");
        storeTitle.setOpaque(true);

        labelId.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelId.setForeground(new java.awt.Color(51, 102, 255));
        labelId.setText("Kode Produk");

        inputId.setEditable(false);
        inputId.setBackground(new java.awt.Color(35, 36, 54));
        inputId.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputId.setForeground(new java.awt.Color(245, 245, 250));
        inputId.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputId.setText("Auto generate");
        inputId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputId.setOpaque(false);

        labelNama.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelNama.setForeground(new java.awt.Color(51, 102, 255));
        labelNama.setText("Nama Produk");

        inputNama.setBackground(new java.awt.Color(35, 36, 54));
        inputNama.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputNama.setForeground(new java.awt.Color(245, 245, 250));
        inputNama.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputNama.setOpaque(false);

        labelJenis.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelJenis.setForeground(new java.awt.Color(51, 102, 255));
        labelJenis.setText("Kategori");

        inputJenis.setBackground(new java.awt.Color(35, 36, 54));
        inputJenis.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputJenis.setForeground(new java.awt.Color(245, 245, 250));
        inputJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Coffee", "Milkshake", "Snack", "Aneka Nasi", "Flavours Tea" }));
        inputJenis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputJenis.setOpaque(false);

        labelHarga.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelHarga.setForeground(new java.awt.Color(51, 102, 255));
        labelHarga.setText("Harga Satuan");

        inputHarga.setBackground(new java.awt.Color(35, 36, 54));
        inputHarga.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputHarga.setForeground(new java.awt.Color(245, 245, 250));
        inputHarga.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputHarga.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputHarga.setOpaque(false);

        btnSimpan.setBackground(new java.awt.Color(62, 70, 225));
        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(245, 245, 250));
        btnSimpan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSimpan.setText("SIMPAN");
        btnSimpan.setOpaque(true);
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanMouseClicked(evt);
            }
        });

        btnBatal.setBackground(new java.awt.Color(62, 70, 225));
        btnBatal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBatal.setForeground(new java.awt.Color(245, 245, 250));
        btnBatal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnBatal.setText("BATAL");
        btnBatal.setOpaque(true);
        btnBatal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBatalMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(storeTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(labelNama, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(labelId, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(labelJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                                .addComponent(labelHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31))))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(storeTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelId, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNama, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseClicked
        if (inputNama.getText().equals("") || inputHarga.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Isi semua field terlebih dulu!");
        }else{
            if (storeTitle.getText().equals("Tambah Data Produk")){
                tambahData();
            }else if (storeTitle.getText().equals("Ubah Data Produk")){
                ubahData();
            }
        }
    }//GEN-LAST:event_btnSimpanMouseClicked

    private void btnBatalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBatalMouseClicked
        int confirm = JOptionPane.showConfirmDialog(null, "Keluar dan batalkan perubahan?", "Konfirmasi keluar",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION){
            this.dispose();
        }
    }//GEN-LAST:event_btnBatalMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Produk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel btnBatal;
    private javax.swing.JLabel btnSimpan;
    private javax.swing.JTextField inputHarga;
    private javax.swing.JTextField inputId;
    private javax.swing.JComboBox<String> inputJenis;
    private javax.swing.JTextField inputNama;
    private javax.swing.JLabel labelHarga;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelJenis;
    private javax.swing.JLabel labelNama;
    private static javax.swing.JLabel storeTitle;
    // End of variables declaration//GEN-END:variables

}
