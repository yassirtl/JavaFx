package com.esalaff.esalaff;

public class Ligne {
    private Integer idProd;
    private String nomProd;
    private Integer Qte;
    private Integer Price;
    private String nomClient;

    public Integer getIdProd() {
        return idProd;
    }

    public void setIdProd(Integer idProd) {
        this.idProd = idProd;
    }

    public Ligne(String nomClient, String nomProd, Integer qte, Integer price,Integer idprod) {

        this.nomClient = nomClient;
        this.nomProd = nomProd;
        this.Qte = qte;
        this.Price = price;
        this.idProd = idprod;
    }





    public String getNomProd() {
        return nomProd;
    }

    public void setNomProd(String nomProd) {
        this.nomProd = nomProd;
    }

    public Integer getQte() {
        return Qte;
    }

    public void setQte(Integer qte) {
        Qte = qte;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }
}
