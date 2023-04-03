package com.esalaff.esalaff;

public class LigneCommande {
    private Integer Prod_id;
    private Integer Commande_id;
    private Integer Quantite;
    private Integer LigneTotal;
    public Integer getProd_id() {
        return Prod_id;
    }

    public void setProd_id(Integer prod_id) {
        Prod_id = prod_id;
    }
    public Integer getCommande_id() {
        return Commande_id;
    }

    public void setCommande_id(Integer commande_id) {
        Commande_id = commande_id;
    }

    public Integer getQuantite() {
        return Quantite;
    }

    public void setQuantite(Integer quantite) {
        Quantite = quantite;
    }

    public Integer getLigneTotal() {
        return LigneTotal;
    }

    public void setLigneTotal(Integer ligneTotal) {
        LigneTotal = ligneTotal;
    }
}
