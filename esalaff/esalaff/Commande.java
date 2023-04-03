package com.esalaff.esalaff;

import java.sql.Date;

public class Commande {
    private Integer Id_commande;
    private java.sql.Date Date;
    private Float Total;
    private Integer Client_id;
    public Integer getId_commande() {
        return Id_commande;
    }

    public void setId_commande(Integer id_commande) {
        Id_commande = id_commande;
    }

    public java.sql.Date getDate() {
        return Date;
    }

    public void setDate(java.sql.Date date) {
        Date = date;
    }

    public Float getTotal() {
        return Total;
    }

    public void setTotal(Float total) {
        Total = total;
    }

    public Integer getClient_id() {
        return Client_id;
    }

    public void setClient_id(Integer client_id) {
        Client_id = client_id;
    }
}
