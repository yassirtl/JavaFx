package com.esalaff.esalaff;

import java.sql.Date;

public class Credit {
    private Integer Id_client;

    public Integer getId_client() {
        return Id_client;
    }

    public void setId_client(Integer id_client) {
        Id_client = id_client;
    }

    private Integer id_credit;
    private Float montant;
    private java.sql.Date Date;

    public Integer getId_credit() {
        return id_credit;
    }

    public void setId_credit(Integer id_credit) {
        this.id_credit = id_credit;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public java.sql.Date getDate() {
        return Date;
    }

    public void setDate(java.sql.Date date) {
        Date = date;
    }
}
