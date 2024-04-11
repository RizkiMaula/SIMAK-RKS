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
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author KELOMPOK 2 R6Q 2021 - KELOMPOK KKP R8Q 2022
 */
public class Karyawan extends javax.swing.JFrame {
    
    private PreparedStatement pst = null;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet res = null;
    private String query = null;
    private boolean verif;
    private String getId;
    private int getLastId;
    private String JK;

    /** Creates new form StoreForm */
    public Karyawan() {
        initComponents();
    }
    
//  Overloading construct..  
    public Karyawan(String label, String data[]){
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
            labelKelamin.setForeground(new java.awt.Color(0,204,153));
            labelNope.setForeground(new java.awt.Color(0,204,153));
            labelAlamat.setForeground(new java.awt.Color(0,204,153));
            labelJabatan.setForeground(new java.awt.Color(0,204,153));
            inputId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputKelamin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputNope.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputAlamat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
            inputJabatan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,204,153), 2));
        }
    }
    
    private void setFilled(String label, String data[]){
        if (label.equals("Ubah")){
            this.getId = unformatKode(data[0]);
            inputId.setText(data[0]);
            inputNama.setText(data[1]);
            if (data[2].equals("Laki-laki")){
                inputKelamin.setSelectedIndex(0);
            }else if (data[2].equals("Perempuan")){
                inputKelamin.setSelectedIndex(1);
            }
            for (int i=0; i<getDataJabatan().length; i++){
                if (data[3].equals(getDataJabatan()[i])){
                    inputJabatan.setSelectedItem(data[3]);
                }
            }
            inputNope.setText(data[4]);
            inputAlamat.setText(data[5]);
        }else if (label.equals("Tambah")){
            String id = Dashboard.getKode("Karyawan", String.valueOf(lastId()));
            inputId.setText(id);
        }
    }
    
    private int lastId(){
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT MAX(id) FROM karyawan";
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
    
    private String[] getDataJabatan(){
        ArrayList<String> data = new ArrayList<>();
        conn = Koneksi.getKoneksi();
        try{
            stmt = conn.createStatement();
            query = "SELECT DISTINCT nama FROM jabatan";
            res = stmt.executeQuery(query);
            while (res.next()){
                data.add(res.getString("nama"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        String jabatan[] = data.toArray(new String[0]);
        
        return jabatan;
    }
    
    private static String unformatKode(String id){
        String res[] = id.split("(?<=\\D)(?=\\d)");
        return res[1];
    }
    
    private void tambahData(){
        conn = Koneksi.getKoneksi();
        int idJabatan = inputJabatan.getSelectedIndex()+1;
        try{
            query = "INSERT INTO karyawan (id, nama, kelamin, alamat, kontak, id_jabatan) VALUES (?,?,?,?,?,?)";
            pst = conn.prepareStatement(query);
            
            pst.setString(1, String.valueOf(lastId()));
            pst.setString(2, inputNama.getText());
            pst.setString(3, String.valueOf(inputKelamin.getSelectedIndex()+1));
            pst.setString(4, inputAlamat.getText());
            pst.setString(5, inputNope.getText());
            pst.setString(6, String.valueOf(idJabatan));
            if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan ke database "+conn.getCatalog());
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelKaryawan);
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal ditambahkan: "+pst.getWarnings());
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private String setKelamin(String label){
        String jk = label.equals("0") ? "L" : "P";
        return jk;
    }
    
    private void ubahData(){
        conn = Koneksi.getKoneksi();
        String id = getId;
        try{
            query = "UPDATE karyawan SET nama=?, kelamin=?, alamat=?, kontak=?, id_jabatan=? WHERE id=?";
            pst = conn.prepareStatement(query);
            pst.setString(1, inputNama.getText());
            pst.setString(2, setKelamin(String.valueOf(inputKelamin.getSelectedIndex())));
            pst.setString(3, inputAlamat.getText());
            pst.setString(4, inputNope.getText());
            pst.setString(5, String.valueOf(inputJabatan.getSelectedIndex()+1));
            pst.setString(6, id);
            if (pst.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
                this.dispose();
                Dashboard.getDataPanel(Dashboard.tabelKaryawan);
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
        labelKelamin = new javax.swing.JLabel();
        inputKelamin = new javax.swing.JComboBox<>();
        labelNope = new javax.swing.JLabel();
        inputNope = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JLabel();
        btnBatal = new javax.swing.JLabel();
        labelJabatan = new javax.swing.JLabel();
        inputJabatan = new javax.swing.JComboBox<>();
        labelAlamat = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputAlamat = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        bg.setBackground(new java.awt.Color(35, 36, 54));
        bg.setVerifyInputWhenFocusTarget(false);

        storeTitle.setBackground(new java.awt.Color(62, 70, 225));
        storeTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        storeTitle.setForeground(new java.awt.Color(245, 245, 250));
        storeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        storeTitle.setText("Data Karyawan");
        storeTitle.setOpaque(true);

        labelId.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelId.setForeground(new java.awt.Color(51, 102, 255));
        labelId.setText("Kode Karyawan");

        inputId.setEditable(false);
        inputId.setBackground(new java.awt.Color(35, 36, 54));
        inputId.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputId.setForeground(new java.awt.Color(245, 245, 250));
        inputId.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputId.setText("Auto generate");
        inputId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputIdActionPerformed(evt);
            }
        });

        labelNama.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelNama.setForeground(new java.awt.Color(51, 102, 255));
        labelNama.setText("Nama");

        inputNama.setBackground(new java.awt.Color(35, 36, 54));
        inputNama.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputNama.setForeground(new java.awt.Color(245, 245, 250));
        inputNama.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));

        labelKelamin.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelKelamin.setForeground(new java.awt.Color(51, 102, 255));
        labelKelamin.setText("Jenis Kelamin");

        inputKelamin.setBackground(new java.awt.Color(35, 36, 54));
        inputKelamin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputKelamin.setForeground(new java.awt.Color(245, 245, 250));
        inputKelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-laki", "Perempuan" }));
        inputKelamin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));

        labelNope.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelNope.setForeground(new java.awt.Color(51, 102, 255));
        labelNope.setText("Homor HP");

        inputNope.setBackground(new java.awt.Color(35, 36, 54));
        inputNope.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputNope.setForeground(new java.awt.Color(245, 245, 250));
        inputNope.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputNope.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));

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

        labelJabatan.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelJabatan.setForeground(new java.awt.Color(51, 102, 255));
        labelJabatan.setText("Jabatan");

        inputJabatan.setBackground(new java.awt.Color(35, 36, 54));
        inputJabatan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inputJabatan.setForeground(new java.awt.Color(245, 245, 250));
        inputJabatan.setModel(new javax.swing.DefaultComboBoxModel<>(getDataJabatan()));
        inputJabatan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255), 2));
        inputJabatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputJabatanActionPerformed(evt);
            }
        });

        labelAlamat.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelAlamat.setForeground(new java.awt.Color(51, 102, 255));
        labelAlamat.setText("Alamat");

        jScrollPane1.setBackground(new java.awt.Color(35, 36, 54));

        inputAlamat.setBackground(new java.awt.Color(35, 36, 54));
        inputAlamat.setColumns(20);
        inputAlamat.setForeground(new java.awt.Color(245, 245, 250));
        inputAlamat.setRows(5);
        inputAlamat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(62, 70, 225), 2));
        jScrollPane1.setViewportView(inputAlamat);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(storeTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                                .addComponent(labelNama, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                                .addComponent(labelId, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(labelKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(labelJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelNope, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(inputNope, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61))))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(storeTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelId, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNama, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNope, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputNope, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
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
        if (inputNama.getText().equals("") || inputNope.getText().equals("") || inputAlamat.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Isi semua field terlebih dulu!");
        }else{
            if (storeTitle.getText().equals("Tambah Data Karyawan")){
                tambahData();
            }else if (storeTitle.getText().equals("Ubah Data Karyawan")){
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

    private void inputIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputIdActionPerformed

    private void inputJabatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputJabatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputJabatanActionPerformed

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
            java.util.logging.Logger.getLogger(Karyawan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Karyawan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Karyawan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Karyawan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new Karyawan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel btnBatal;
    private javax.swing.JLabel btnSimpan;
    private javax.swing.JTextArea inputAlamat;
    private javax.swing.JTextField inputId;
    private javax.swing.JComboBox<String> inputJabatan;
    private javax.swing.JComboBox<String> inputKelamin;
    private javax.swing.JTextField inputNama;
    private javax.swing.JTextField inputNope;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelAlamat;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelJabatan;
    private javax.swing.JLabel labelKelamin;
    private javax.swing.JLabel labelNama;
    private javax.swing.JLabel labelNope;
    private static javax.swing.JLabel storeTitle;
    // End of variables declaration//GEN-END:variables

}
