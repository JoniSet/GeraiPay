package com.optima.gerai_pay.Model;

public class ListLaporanPenjualan {
    String tgl, jml_transaksi, komisi;

    public ListLaporanPenjualan(String tgl, String jml_transaksi, String komisi) {
        this.tgl = tgl;
        this.jml_transaksi = jml_transaksi;
        this.komisi = komisi;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getJml_transaksi() {
        return jml_transaksi;
    }

    public void setJml_transaksi(String jml_transaksi) {
        this.jml_transaksi = jml_transaksi;
    }

    public String getKomisi() {
        return komisi;
    }

    public void setKomisi(String komisi) {
        this.komisi = komisi;
    }
}
