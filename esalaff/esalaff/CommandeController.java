package com.esalaff.esalaff;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class CommandeController implements Initializable {

    @FXML
    private ComboBox<Produit> myComboboxProd;
    int id = 0;
    private Stage stage;
    private Scene scene;
    @FXML
    private TableView<Ligne> tbv_ligne_comm;
    @FXML
    private TextField txtQte;
    @FXML
    private TableColumn<Ligne, String> col_produit_ligne;

    @FXML
    private TableColumn<Ligne, Integer> col_qte_ligne;

    @FXML
    private TableColumn<Ligne, Integer> col_prix_ligne;

    @FXML
    private TableColumn<Ligne, String> col_client_test;

    @FXML
    void toDashboardd(ActionEvent event) {
        try {
            Parent fxml =  FXMLLoader.load(getClass().getResource("/FXML/Dashboard.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxml);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ObservableList<Produit> produits = FXCollections.observableArrayList();
     @Override
    public void initialize(URL location, ResourceBundle resources) {
        getClients();
        produits =  getProduit();
        tbv_ligne_comm.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ligne>() {
             @Override
             public void changed(ObservableValue<? extends Ligne> observableValue, Ligne ligne, Ligne t1) {
                 getData(null);
             }
         });
    }
    void desableCombo(){
         if(!lst.isEmpty())
         {
             myCombobox.setDisable(true);
         }else {
             myCombobox.setDisable(false);
             //myCombobox.getEditor().setEditable(true);
         }
    }
    @FXML
    private ComboBox<Client> myCombobox;
    Connection connection = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    public ObservableList<Client> getClients(){
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String query = "select * from clientest";
        connection = DBConnection.getCon();
        Callback<ListView<Client>, ListCell<Client>> cellFactory = new Callback<ListView<Client>, ListCell<Client>>() {
            @Override
            public ListCell<Client> call(ListView<Client> l) {
                return new ListCell<Client>() {
                    @Override
                    protected void updateItem(Client item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getId() + "    " + item.getNom());
                        }}
                } ;}};

        // Just set the button cell here:
        myCombobox.setButtonCell(cellFactory.call(null));
        myCombobox.setCellFactory(cellFactory);
        try {
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getNString("nom"));
                myCombobox.getItems().add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }
    public ObservableList<Produit> getProduit(){
        String query = "select * from Produit";
        connection = DBConnection.getCon();
        Callback<ListView<Produit>, ListCell<Produit>> cellFactory = new Callback<ListView<Produit>, ListCell<Produit>>() {
            @Override
            public ListCell<Produit> call(ListView<Produit> l) {
                return new ListCell<Produit>() {
                    @Override
                    protected void updateItem(Produit item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getId() + "    " + item.getProductName());
                        }
                    }
                } ;
            }
        };
        // Just set the button cell here:
        myComboboxProd.setButtonCell(cellFactory.call(null));
        myComboboxProd.setCellFactory(cellFactory);
        try {
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
                Produit produit = new Produit();
                produit.setId(rs.getInt("id"));
                produit.setProductName(rs.getNString("ProductName"));
                produit.setPrice(rs.getInt("Price"));
                myComboboxProd.getItems().add(produit);
                produits.add(produit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produits;
    }
    Integer totalPrice = 0;
    Integer LingePrix;
    ObservableList<Ligne> lst = FXCollections.observableArrayList();

    @FXML
    void addligne(ActionEvent event) {
        Client client = myCombobox.getValue();
        Produit produit = myComboboxProd.getValue();
        LingePrix = Integer.parseInt(txtQte.getText()) * produit.getPrice();
        totalPrice = totalPrice+LingePrix;
        Ligne ln =  new Ligne(client.getNom(),produit.getProductName(),
                Integer.parseInt(txtQte.getText()),LingePrix,produit.getId());
        ln.setPrice(LingePrix);
        lst.add(ln);
        tbv_ligne_comm.setItems(lst);
        col_produit_ligne.setCellValueFactory(new PropertyValueFactory<Ligne,String>("nomProd"));
        col_qte_ligne.setCellValueFactory(new PropertyValueFactory<Ligne,Integer>("Qte"));
        col_prix_ligne.setCellValueFactory(new PropertyValueFactory<Ligne,Integer>("Price"));
        col_client_test.setCellValueFactory(new PropertyValueFactory<Ligne,String>("nomClient"));
        desableCombo();

    };
    @FXML
    void Deleteligne(ActionEvent event) {
        Ligne ligne = tbv_ligne_comm.getSelectionModel().getSelectedItem();
        if(ligne!=null){
            lst.remove(ligne);
            tbv_ligne_comm.setItems(lst);
        }
        desableCombo();
    }
    @FXML
    void getData(MouseEvent event) {
        Ligne ligne = tbv_ligne_comm.getSelectionModel().getSelectedItem();
        Optional<Produit> prod = produits.stream().filter(pro-> pro.getId() == ligne.getIdProd()).findFirst();
        if (prod.isPresent())
        {
            Produit produit = prod.get();
            myComboboxProd.setValue(produit);
            String s = ligne.getQte().toString();
            txtQte.setText(Integer.toString(ligne.getQte()));
        }
    }
    @FXML
    void SaveCommande(ActionEvent event) {
        String insert = "insert into Commande( Date, Total, Client_id) values(?,?,?)";
        connection = DBConnection.getCon();
        Client client = myCombobox.getValue();
        java.util.Date date=new java.util.Date();
        java.sql.Date sqlDate=new java.sql.Date(date.getTime());
        try {
            st = connection.prepareStatement(insert);
            st.setDate(1,sqlDate);
            st.setInt(2,totalPrice);
            st.setInt(3,client.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);}
        String query = "SELECT LAST_INSERT_ID() AS Commande";
         Integer commande_id = 0;
        try {
            st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                 commande_id = rs.getInt(1);
                System.out.println(commande_id);}
        } catch (SQLException e) {
            throw new RuntimeException(e);}
        String query_ligne = "insert into LigneCommande(Prod_id, Commande_id, Quantite, LigneTotal) values(?,?,?,?)";
        Integer finalCommande_id = commande_id;
        lst.forEach((ligne -> {
                try {
                    st=connection.prepareStatement(query_ligne);
                st.setInt(1,ligne.getIdProd());
                st.setInt(2, finalCommande_id);
                st.setInt(3,ligne.getQte());
                st.setInt(4,LingePrix);
                st.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);}}));
        Client Selectedclient = myCombobox.getValue();
        Integer id= Selectedclient.getId();
        Float OldCredit = getCredit(id);
        OldCredit = OldCredit + totalPrice ;
        System.out.println(OldCredit);
        saveCredit(id,OldCredit);
    }
    //return Credit of the client
    Float getCredit(Integer ClientId){
        Float Credit = 0.0f;
        String query = "select Credit from clientest where id = ?";
        connection = DBConnection.getCon();
        try {
            st = connection.prepareStatement(query);
            st.setInt(1,ClientId);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                Credit = rs.getFloat(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Credit;
    };
    void saveCredit(Integer ClientId,Float Credit){
        String query = "update Clientest set Credit = ? where id = ?";
        try {
            st=connection.prepareStatement(query);
            st.setFloat(1, Credit);
            st.setInt(2,ClientId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void UpdateLigneCommande(ActionEvent event){
        Ligne ligne = tbv_ligne_comm.getSelectionModel().getSelectedItem();
        //lst.remove(ligne);
        ligne.setQte(Integer.parseInt(txtQte.getText()));
        //lst.(ligne);
        tbv_ligne_comm.refresh();
    }
}






