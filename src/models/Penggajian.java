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
public class Penggajian extends javax.swing.JFrame {
    
    private PreparedStatement pst = null;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet res = null;
    private String query = null;
    private boolean verif;
    private String getId;
    private int resId;
    private int getLastId;

    /** Creates new form StoreForm */
    public Penggajian() {
        initComponents();
    }
    
//  Overloading construct..  
    public Penggajian(String label, String data[]){
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
            labelBonus.setForeground(new java.awt.Color(0,204,153));
            inputId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputWaktu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputBonus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
        }
    }
    
    private void setFilled(String label, String data[]){
        if (label.equals("Ubah")){
            this.getId = unformatKode(data[0]);
            inputId.setText(data[0]);
            
            LocalTime dTime = LocalTime.now();
            String dateTime = data[1]+" "+String.valueOf(dTime);
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").parse(dateTime);
                inputWaktu.setValue(date);
                inputWaktu.setEnabled(false);
            } catch (ParseException ex) {
                Logger.getLogger(Penggajian.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            for (int i=0; i<getDataKaryawan().length; i++){
                if (data[2].equals(getDataKaryawan()[i])){
                    inputNama.setSelectedItem(data[2]);
                }
            }
            
            inputBonus.setText(data[3]);
        }else if (label.equals("Tambah")){
            String id = Dashboard.getKode("Penggajian", String.valueOf(lastId()));
            inputId.setText(id);
        }
    }
    
    private int lastId(){
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT MAX(id) FROM penggajian";
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
        int idKrw = (inputNama.getSelectedIndex()+1);
        String date = new SimpleDateFormat("yyyy/MM/dd").format(inputWaktu.getValue());
        int bonusKrw = Integer.valueOf(inputBonus.getText());
        
//        String qr = "INSERT INTO penggajian (id, tgl_gajian, id_karyawan, bonus_thr, gapok, gaji_diterima) VALUES "
//                +"(?, ?, ?, ?, "
//                +"(SELECT gapok FROM jabatan WHERE id=(SELECT id_jabatan FROM karyawan WHERE id=5)) "
//                +"(300000(SELECT gapok FROM jabatan WHERE id=(SELECT id_jabatan FROM karyawan WHERE id=5))))";
        
        try{
            conn = Koneksi.getKoneksi();
            query = "INSERT INTO penggajian (id, tgl_gajian, id_karyawan, bonus_thr, gapok, gaji_diterima) VALUES "
                    +"(?, ?, ?, ?, (SELECT gapok FROM jabatan WHERE id=(SELECT id_jabatan FROM karyawan WHERE id="+idKrw+")), "
                    + "("+bonusKrw+"+(SELECT gapok FROM jabatan WHERE id=(SELECT id_jabatan FROM karyawan WHERE id="+idKrw+"))))";
            
            pst = conn.prepareStatement(query);
            pst.setString(1, String.valueOf(lastId()));
            pst.setString(2, date);
            pst.setString(3, String.valueOf(idKrw));
            pst.setString(4, String.valueOf(bonusKrw));
            if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan ke database "+conn.getCatalog());
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelPenggajian);
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal ditambahkan: "+pst.getWarnings());
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private int setIdKaryawan(){
        Integer idKrw[] = getIdKaryawan().clone();
        int setId = inputNama.getSelectedIndex();
        
        for (int i=0; i<getIdKaryawan().length; i++){
            if (setId == getIdKaryawan()[i]){
                this.resId = getIdKaryawan()[i];
            }
        }
        return (resId+1);
    }
    
    private void ubahData(){
        String id = getId;
        String idKrw = String.valueOf(setIdKaryawan());
        int bonusKrw = Integer.valueOf(inputBonus.getText());        
        try{
            query = "UPDATE penggajian SET id_karyawan=?, bonus_thr=?, "
                    + "gapok=(SELECT gapok FROM jabatan WHERE id=(SELECT id_jabatan FROM karyawan WHERE id="+idKrw+")), "
                    + "gaji_diterima=("+bonusKrw+"+((SELECT gapok FROM jabatan WHERE id=(SELECT id_jabatan FROM karyawan WHERE id="+idKrw+")))) "
                    + "WHERE id=?";
            pst = conn.prepareStatement(query);
            pst.setString(1, idKrw);
            pst.setString(2, String.valueOf(bonusKrw));
            pst.setString(3, id);
            if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelPenggajian);
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal diubah: "+pst.getWarnings());
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private String[] getDataKaryawan(){
        ArrayList<String> data = new ArrayList<>();
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT DISTINCT nama FROM karyawan";
            res = stmt.executeQuery(query);
            while (res.next()){
                data.add(res.getString("nama"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        String krw[] = data.toArray(new String[0]);
        
        return krw;
    }
    
    private Integer[] getIdKaryawan(){
        ArrayList<Integer> data = new ArrayList<Integer>();
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT DISTINCT id FROM karyawan";
            res = stmt.executeQuery(query);
            while (res.next()){
                data.add(res.getInt("id"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        Integer krw[] = data.toArray(new Integer[0]);
        
        return krw;
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
        labelBonus = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JLabel();
        btnBatal = new javax.swing.JLabel();
        inputWaktu = new javax.swing.JSpinner();
        inputBonus = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        bg.setBackground(new java.awt.Color(35, 36, 54));
        bg.setVerifyInputWhenFocusTarget(false);

        storeTitle.setBackground(new java.awt.Color(62, 70, 225));
        storeTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        storeTitle.setForeground(new java.awt.Color(245, 245, 250));
        storeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        storeTitle.setText("Data Penggajian");
        storeTitle.setOpaque(true);

        labelId.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelId.setForeground(new java.awt.Color(51, 102, 255));
        labelId.setText("Kode Penggajian");

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
        labelWaktu.setText("Tanggal Gajian");

        labelNama.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelNama.setForeground(new java.awt.Color(51, 102, 255));
        labelNama.setText("Nama Karyawan");

        inputNama.setBackground(new java.awt.Color(35, 36, 54));
        inputNama.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        inputNama.setForeground(new java.awt.Color(245, 245, 250));
        inputNama.setModel(new javax.swing.DefaultComboBoxModel<>(getDataKaryawan()));
        inputNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputNama.setOpaque(false);

        labelBonus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelBonus.setForeground(new java.awt.Color(51, 102, 255));
        labelBonus.setText("Bonus /THR");

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

        inputBonus.setBackground(new java.awt.Color(35, 36, 54));
        inputBonus.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        inputBonus.setForeground(new java.awt.Color(245, 245, 250));
        inputBonus.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputBonus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputBonus.setOpaque(false);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(storeTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
            .addGroup(bgLayout.createSequentialGroup()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(bgLayout.createSequentialGroup()
                                    .addGap(132, 132, 132)
                                    .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgLayout.createSequentialGroup()
                                    .addComponent(labelBonus, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(inputBonus, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(bgLayout.createSequentialGroup()
                                    .addComponent(labelNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(180, 180, 180)))
                            .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(bgLayout.createSequentialGroup()
                                    .addComponent(labelWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(inputWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(bgLayout.createSequentialGroup()
                                    .addComponent(labelId, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(storeTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelId, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputWaktu, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNama, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelBonus, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputBonus, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65))
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
        if (storeTitle.getText().equals("Tambah Data Penggajian")){
            tambahData();
        }else if (storeTitle.getText().equals("Ubah Data Penggajian")){
            ubahData();
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
            java.util.logging.Logger.getLogger(Penggajian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Penggajian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Penggajian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Penggajian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Penggajian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel btnBatal;
    private javax.swing.JLabel btnSimpan;
    private javax.swing.JTextField inputBonus;
    private javax.swing.JTextField inputId;
    private javax.swing.JComboBox<String> inputNama;
    private javax.swing.JSpinner inputWaktu;
    private javax.swing.JLabel labelBonus;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelNama;
    private javax.swing.JLabel labelWaktu;
    private static javax.swing.JLabel storeTitle;
    // End of variables declaration//GEN-END:variables

}
