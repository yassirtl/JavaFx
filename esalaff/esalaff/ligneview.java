package com.esalaff.esalaff;

import java.util.Date;

public class ligneview {
    private Integer id;
    private Integer Id_commande;
    private Integer Quantite;
    private String ProductName;
    private Float Price;
    private Date Date;
    private Float LigneTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_commande() {
        return Id_commande;
    }

    public void setId_commande(Integer id_commande) {
        Id_commande = id_commande;
    }

    public Integer getQuantite() {
        return Quantite;
    }

    public void setQuantite(Integer quantite) {
        Quantite = quantite;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Float getPrice() {
        return Price;
    }

    public void setPrice(Float price) {
        Price = price;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public Float getLigneTotal() {
        return LigneTotal;
    }

    public void setLigneTotal(Float ligneTotal) {
        LigneTotal = ligneTotal;
    }
}
