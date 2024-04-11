/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import config.Koneksi;
import views.Dashboard;

import java.sql.Connection;
import java.util.Date;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author KELOMPOK 2 R6Q 2021 - KELOMPOK KKP R8Q 2022
 */
public class Penjualan extends javax.swing.JFrame {
    
    private PreparedStatement pst = null;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet res = null;
    private String query = null;
    private boolean verif;
    private String getId;
    private int resId;

    /** Creates new form StoreForm */
    public Penjualan() {
        initComponents();
    }
    
//  Overloading construct..  
    public Penjualan(String label, String data[]){
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
            labelWaktu.setForeground(new java.awt.Color(0,204,153));
            labelJumlah.setForeground(new java.awt.Color(0,204,153));
            inputId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputJumlah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputWaktu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
        }
    }
    
    private void setFilled(String label, String data[]){
        if (label.equals("Ubah")){
            this.getId = unformatKode(data[0]);
            
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").parse(data[1]);
                inputWaktu.setValue(date);
            } catch (ParseException ex) {
                Logger.getLogger(Penjualan.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            inputWaktu.setEnabled(false);
            inputId.setText(data[0]);
            for (int i=0; i<getDataProduk().length; i++){
                if (data[2].equals(getDataProduk()[i])){
                    inputNama.setSelectedItem(data[2]);
                }
            }
            inputJumlah.setText(data[3]);
        }else if (label.equals("Tambah")){
            conn = Koneksi.getKoneksi();
            try{
                stmt = conn.createStatement();
                query = "SELECT MAX(id) FROM penjualan";
                res = stmt.executeQuery(query);
                if (res.next()){
                    int getMax = Integer.valueOf(res.getString("MAX(id)"))+1;
                    String id = Dashboard.getKode("Penjualan", String.valueOf(getMax));
                    inputId.setText(id);
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    private static String unformatKode(String id){
        String res[] = id.split("(?<=\\D)(?=\\d)");
        return res[1];
    }
    
    private String getDateTime(){
        String str;
        LocalDate dDate = LocalDate.now();
        LocalTime dTime = LocalTime.now();
        str = dDate.toString()+" "+dTime.toString();
        
        return str;
    }
    
    private void tambahData(){
        String doT = getDateTime();
        int idProduk = inputNama.getSelectedIndex()+1;
        int jum = Integer.valueOf(inputJumlah.getText());
        
        try{
            conn = Koneksi.getKoneksi();
            query = "INSERT INTO penjualan (id, tgl_transaksi, id_produk, jumlah, tagihan) VALUES (default,?,?,?, ("+jum+"*(SELECT harga FROM produk WHERE id="+idProduk+")))";
            pst = conn.prepareStatement(query);
            pst.setString(1, doT);
            pst.setString(2, String.valueOf(idProduk));
            pst.setString(3, String.valueOf(jum));
             if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan ke database "+conn.getCatalog());
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelPenjualan);
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal ditambahkan: "+pst.getWarnings());
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private int setIdProduk(){
        Integer idProduk[] = getIdProduk().clone();
        int setId = inputNama.getSelectedIndex();
        
        for (int i=0; i<getIdProduk().length; i++){
            if (setId == getIdProduk()[i]){
                this.resId = getIdProduk()[i];
            }
        }
        return (resId+1);
    }
    
    private void ubahData(){
        String id = getId;
        String idProduk = String.valueOf(setIdProduk());
        int jum = Integer.valueOf(inputJumlah.getText());
        try{
            query = "UPDATE penjualan SET id_produk=?, jumlah=?,"
                    + "tagihan=("+String.valueOf(jum)+"*(SELECT harga FROM produk WHERE id="+idProduk+")) WHERE id=?";
            pst = conn.prepareStatement(query);
            pst.setString(1, idProduk);
            pst.setString(2, String.valueOf(jum));
            pst.setString(3, id);
            if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelPenjualan);
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal diubah: "+pst.getWarnings());
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private String[] getDataProduk(){
        ArrayList<String> data = new ArrayList<>();
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT DISTINCT nama FROM produk";
            res = stmt.executeQuery(query);
            while (res.next()){
                data.add(res.getString("nama"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        String produk[] = data.toArray(new String[0]);
        
        return produk;
    }
    
    private Integer[] getIdProduk(){
        ArrayList<Integer> data = new ArrayList<Integer>();
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT id FROM produk";
            res = stmt.executeQuery(query);
            while (res.next()){
                data.add(res.getInt("id"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        Integer produk[] = data.toArray(new Integer[0]);
        
        return produk;
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
        labelWaktu = new javax.swing.JLabel();
        labelNama = new javax.swing.JLabel();
        inputNama = new javax.swing.JComboBox<>();
        labelJumlah = new javax.swing.JLabel();
        inputJumlah = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JLabel();
        btnBatal = new javax.swing.JLabel();
        inputWaktu = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        bg.setBackground(new java.awt.Color(35, 36, 54));
        bg.setVerifyInputWhenFocusTarget(false);

        storeTitle.setBackground(new java.awt.Color(62, 70, 225));
        storeTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        storeTitle.setForeground(new java.awt.Color(245, 245, 250));
        storeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        storeTitle.setText("Data Penjualan");
        storeTitle.setOpaque(true);

        labelId.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelId.setForeground(new java.awt.Color(51, 102, 255));
        labelId.setText("Kode Transaksi");

        inputId.setEditable(false);
        inputId.setBackground(new java.awt.Color(35, 36, 54));
        inputId.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputId.setForeground(new java.awt.Color(245, 245, 250));
        inputId.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputId.setText("Auto generat");
        inputId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));

        labelWaktu.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelWaktu.setForeground(new java.awt.Color(51, 102, 255));
        labelWaktu.setText("Waktu Transaksi");

        labelNama.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelNama.setForeground(new java.awt.Color(51, 102, 255));
        labelNama.setText("Nama Produk");

        inputNama.setBackground(new java.awt.Color(35, 36, 54));
        inputNama.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputNama.setForeground(new java.awt.Color(245, 245, 250));
        inputNama.setModel(new javax.swing.DefaultComboBoxModel<>(getDataProduk()));
        inputNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));

        labelJumlah.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelJumlah.setForeground(new java.awt.Color(51, 102, 255));
        labelJumlah.setText("Jumlah Beli");

        inputJumlah.setBackground(new java.awt.Color(35, 36, 54));
        inputJumlah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputJumlah.setForeground(new java.awt.Color(245, 245, 250));
        inputJumlah.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputJumlah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));

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

        inputWaktu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputWaktu.setModel(new javax.swing.SpinnerDateModel());
        inputWaktu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputWaktu.setEnabled(false);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(storeTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(labelId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelWaktu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inputWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inputId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(labelJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(labelNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60))))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(storeTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelId, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputWaktu, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNama, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
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
        if (inputJumlah.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Isi semua field terlebih dulu!");
        }else{
            if (storeTitle.getText().equals("Tambah Data Penjualan")){
                tambahData();
            }else if (storeTitle.getText().equals("Ubah Data Penjualan")){
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
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Penjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel btnBatal;
    private javax.swing.JLabel btnSimpan;
    private javax.swing.JTextField inputId;
    private javax.swing.JTextField inputJumlah;
    private javax.swing.JComboBox<String> inputNama;
    private javax.swing.JSpinner inputWaktu;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelJumlah;
    private javax.swing.JLabel labelNama;
    private javax.swing.JLabel labelWaktu;
    private static javax.swing.JLabel storeTitle;
    // End of variables declaration//GEN-END:variables

}
