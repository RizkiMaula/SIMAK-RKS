/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports;

import config.Koneksi;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import views.Dashboard;

/**
 *
 * @author KELOMPOK 2 R6Q 2021 - KELOMPOK KKP R8Q 2022
 */
public class Laporan{
    
    private Connection conn;
    private Statement stmt;
    private ResultSet res;
    private PreparedStatement pst;
    private String query;
    
    public Laporan(String label, String id){
        conn = Koneksi.getKoneksi();
        
        try{
            query = "INSERT INTO lap_penggajian (temp_id) VALUES ("+id+")";
            pst = conn.prepareStatement(query);
            if (pst.executeUpdate() > 0){
                System.out.println("Data baru laporan penggajian berhasil ditambahkan!");
            }else{
                System.out.println("Gagal menambahkan data baru pada laporan penggajian!");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public Laporan(String label)
    {    
        conn = Koneksi.getKoneksi();
        try{
            query = "INSERT INTO lap_keuangan (id, tgl_dibuat, penjualan, beban_gaji, beban_ops, "
                    +"beban_utg, beban_lain, total_beban, laba_bersih) VALUES "
                    +"(default, CURDATE(), (SELECT SUM(tagihan) FROM penjualan), "
                    +"(SELECT SUM(gaji_diterima) FROM penggajian), "
                    +"(SELECT SUM(jumlah) FROM pengeluaran WHERE id_jenis=1), "
                    +"(SELECT SUM(jumlah) FROM pengeluaran WHERE id_jenis=3), "
                    +"(SELECT SUM(jumlah) FROM pengeluaran WHERE id_jenis=2), "
                    +"(SELECT ((SELECT SUM(gaji_diterima) FROM penggajian)+(SELECT SUM(jumlah) FROM pengeluaran))), "
                    +"(SELECT ((SELECT SUM(tagihan) FROM penjualan)-(SELECT (SELECT SUM(gaji_diterima) FROM penggajian)"
                    +"+(SELECT SUM(jumlah) FROM pengeluaran)) ) ))";
            pst = conn.prepareStatement(query);
            if (pst.executeUpdate() > 0){
                System.out.println("Data baru untuk laporan keuangan berhasil ditambahkan!");
                Dashboard.getDataPanel(Dashboard.tabelLapKeuangan);
            }else{
                System.out.println("Gagal menambahkan data laporan keuangan!");
            }
        }catch (SQLException e) {   
            System.out.println(e.getMessage());
        }
    }
}
