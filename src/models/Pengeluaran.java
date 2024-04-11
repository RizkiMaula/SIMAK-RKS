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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author KELOMPOK 2 R6Q 2021 - KELOMPOK KKP R8Q 2022
 */
public class Pengeluaran extends javax.swing.JFrame {
    
    private PreparedStatement pst = null;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet res = null;
    private String query = null;
    private boolean verif;
    private String getId;
    private int resId;

    /** Creates new form StoreForm */
    public Pengeluaran() {
        initComponents();
    }
    
//  Overloading construct..  
    public Pengeluaran(String label, String data[]){
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
            labelJenis.setForeground(new java.awt.Color(0,204,153));
            labelWaktu.setForeground(new java.awt.Color(0,204,153));
            labelJumlah.setForeground(new java.awt.Color(0,204,153));
            labelKeterangan.setForeground(new java.awt.Color(0,204,153));
            inputId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputJenis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputJumlah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputWaktu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputKeterangan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
        }
    }
    
    private void setFilled(String label, String data[]){
        if (label.equals("Ubah")){
            this.getId = unformatKode(data[0]);
            LocalTime dTime = LocalTime.now();
            String dateTime = data[1]+" "+String.valueOf(dTime);
            
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").parse(dateTime);
                inputWaktu.setValue(date);
            } catch (ParseException ex) {
                Logger.getLogger(Pengeluaran.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            for (int i=0; i<getDataPengeluaran().length; i++){
                if (data[2].equals(getDataPengeluaran()[i])){
                    inputJenis.setSelectedItem(data[2]);
                }
            }
            inputId.setText(data[0]);
            inputJumlah.setText(data[3]);
            inputKeterangan.setText(data[4]);
        }else if (label.equals("Tambah")){
            conn = Koneksi.getKoneksi();
            try{
                stmt = conn.createStatement();
                query = "SELECT MAX(id) FROM pengeluaran";
                res = stmt.executeQuery(query);
                if (res.next()){
                    int getMax = Integer.valueOf(res.getString("MAX(id)"))+1;
                    String id = Dashboard.getKode("Pengeluaran", String.valueOf(getMax));
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
    
    private void tambahData(){
        int idOutcome = (inputJenis.getSelectedIndex()+1);
        String date = new SimpleDateFormat("yyyy/MM/dd").format(inputWaktu.getValue());
        try{
            conn = Koneksi.getKoneksi();
            query = "INSERT INTO pengeluaran (id, tgl_transaksi, id_jenis, jumlah, keterangan) VALUES (default,?,?,?,?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, date);
            pst.setString(2, String.valueOf(idOutcome));
            pst.setString(3, inputJumlah.getText());
            pst.setString(4, inputKeterangan.getText());
            if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan ke database "+conn.getCatalog());
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelPengeluaran);
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal ditambahkan: "+pst.getWarnings());
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private int setIdPengeluaran(){
        Integer idOutcome[] = getIdPengeluaran().clone();
        int setId = inputJenis.getSelectedIndex();
        
        for (int i=0; i<getIdPengeluaran().length; i++){
            if (setId == getIdPengeluaran()[i]){
                this.resId = getIdPengeluaran()[i];
            }
        }
        return (resId+1);
    }
    
    private void ubahData(){
        String id = getId;
        String idOutcome = String.valueOf(setIdPengeluaran());
        try{
            query = "UPDATE pengeluaran SET id_jenis=?, jumlah=?, keterangan=? WHERE id=?";
            pst = conn.prepareStatement(query);
            pst.setString(1, idOutcome);
            pst.setString(2, inputJumlah.getText());
            pst.setString(3, inputKeterangan.getText());
            pst.setString(4, id);
            if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelPengeluaran);
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal diubah: "+pst.getWarnings());
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private String[] getDataPengeluaran(){
        ArrayList<String> data = new ArrayList<>();
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT DISTINCT nama FROM jenis_pengeluaran";
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
    
    private Integer[] getIdPengeluaran(){
        ArrayList<Integer> data = new ArrayList<Integer>();
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT DISTINCT id FROM produk";
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
        labelJenis = new javax.swing.JLabel();
        inputJenis = new javax.swing.JComboBox<>();
        labelJumlah = new javax.swing.JLabel();
        inputJumlah = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JLabel();
        btnBatal = new javax.swing.JLabel();
        inputWaktu = new javax.swing.JSpinner();
        labelKeterangan = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputKeterangan = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        bg.setBackground(new java.awt.Color(35, 36, 54));
        bg.setVerifyInputWhenFocusTarget(false);

        storeTitle.setBackground(new java.awt.Color(62, 70, 225));
        storeTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        storeTitle.setForeground(new java.awt.Color(245, 245, 250));
        storeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        storeTitle.setText("Data Pengeluaran");
        storeTitle.setOpaque(true);

        labelId.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelId.setForeground(new java.awt.Color(51, 102, 255));
        labelId.setText("Kode Transaksi");

        inputId.setEditable(false);
        inputId.setBackground(new java.awt.Color(35, 36, 54));
        inputId.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        inputId.setForeground(new java.awt.Color(245, 245, 250));
        inputId.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputId.setText("Auto generate");
        inputId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputId.setOpaque(false);

        labelWaktu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelWaktu.setForeground(new java.awt.Color(51, 102, 255));
        labelWaktu.setText("Tanggal Transaksi");

        labelJenis.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelJenis.setForeground(new java.awt.Color(51, 102, 255));
        labelJenis.setText("Jenis Pengeluaran");

        inputJenis.setBackground(new java.awt.Color(35, 36, 54));
        inputJenis.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        inputJenis.setForeground(new java.awt.Color(245, 245, 250));
        inputJenis.setModel(new javax.swing.DefaultComboBoxModel<>(getDataPengeluaran()));
        inputJenis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputJenis.setOpaque(false);

        labelJumlah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelJumlah.setForeground(new java.awt.Color(51, 102, 255));
        labelJumlah.setText("Jumlah ");

        inputJumlah.setBackground(new java.awt.Color(35, 36, 54));
        inputJumlah.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        inputJumlah.setForeground(new java.awt.Color(245, 245, 250));
        inputJumlah.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputJumlah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputJumlah.setOpaque(false);

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

        inputWaktu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        inputWaktu.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.DAY_OF_YEAR));
        inputWaktu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputWaktu.setEnabled(false);

        labelKeterangan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelKeterangan.setForeground(new java.awt.Color(51, 102, 255));
        labelKeterangan.setText("Keterangan");

        jScrollPane1.setBackground(new java.awt.Color(35, 36, 54));

        inputKeterangan.setBackground(new java.awt.Color(35, 36, 54));
        inputKeterangan.setColumns(20);
        inputKeterangan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        inputKeterangan.setForeground(new java.awt.Color(245, 245, 250));
        inputKeterangan.setRows(5);
        inputKeterangan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        jScrollPane1.setViewportView(inputKeterangan);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(storeTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(labelKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(bgLayout.createSequentialGroup()
                            .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(labelId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(inputWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(inputId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(inputJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgLayout.createSequentialGroup()
                            .addComponent(labelJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(bgLayout.createSequentialGroup()
                            .addComponent(labelJenis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(180, 180, 180))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(storeTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelId, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputWaktu, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
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
        if (inputJumlah.getText().equals("") || inputKeterangan.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Isi semua field terlebih dulu!");
        }else{
            if (storeTitle.getText().equals("Tambah Data Pengeluaran")){
                tambahData();
            }else if (storeTitle.getText().equals("Ubah Data Pengeluaran")){
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
            java.util.logging.Logger.getLogger(Pengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pengeluaran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel btnBatal;
    private javax.swing.JLabel btnSimpan;
    private javax.swing.JTextField inputId;
    private javax.swing.JComboBox<String> inputJenis;
    private javax.swing.JTextField inputJumlah;
    private javax.swing.JTextArea inputKeterangan;
    private javax.swing.JSpinner inputWaktu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelJenis;
    private javax.swing.JLabel labelJumlah;
    private javax.swing.JLabel labelKeterangan;
    private javax.swing.JLabel labelWaktu;
    private static javax.swing.JLabel storeTitle;
    // End of variables declaration//GEN-END:variables

}
