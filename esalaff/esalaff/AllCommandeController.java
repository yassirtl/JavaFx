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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class AllCommandeController implements Initializable {
    Connection connection = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    private Stage stage;
    private Scene scene;
    @FXML
    private TableView<Commande> tbv_Commande_client;
    @FXML
    private TableColumn<Commande, Date> Col_Date;
    @FXML
    private TableColumn<Commande, Float> Col_Total;

    @FXML
    private TableColumn<ligneview, String > Col_Prod;

    @FXML
    private TableColumn<ligneview, Integer> Col_Qte;

    @FXML
    private TableColumn<Commande, Integer> Col_Comm_Id;
    @FXML
    private TableColumn<ligneview, Float> Col_Prix;
    @FXML
    private TableColumn<ligneview, Float> Col_TotalV;
    @FXML
    private TableColumn<ligneview, Date> Col_DateV;


    @FXML
    private ComboBox<Client> ComboClientAllCom;
    @FXML
    private TableView<ligneview> tbv_Commande_detail;
    @FXML
    void toDashboard(ActionEvent event) {
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
    @FXML
    void Addcommande(ActionEvent event) {
        try {
            Parent fxml =  FXMLLoader.load(getClass().getResource("/FXML/Commande.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxml);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ComboClientAllCom.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> observableValue, Client client, Client t1) {
                //System.out.println(t1.getId());
                if (t1 != null)
                {
                    showCommande(t1.getId());
                }
            }
        });
        tbv_Commande_client.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Commande>() {
            @Override
            public void changed(ObservableValue<? extends Commande> observableValue, Commande commande, Commande t1) {
                Client client = ComboClientAllCom.getValue();
                if (t1 != null){
                    showLigneCommande(client.getId(), t1.getId_commande());
                }
            }
        });
        getClients();
    }

    public void showLigneCommande(Integer id_Client, Integer Id_Command){
        ObservableList<ligneview> list = GetLigneCommand(id_Client, Id_Command);
        tbv_Commande_detail.setItems(list);
        Col_Prod.setCellValueFactory(new PropertyValueFactory<ligneview, String>("ProductName"));
        Col_Prix.setCellValueFactory(new PropertyValueFactory<ligneview, Float>("ProductName"));
        Col_Qte.setCellValueFactory(new PropertyValueFactory<ligneview,Integer>("Quantite"));
        Col_DateV.setCellValueFactory(new PropertyValueFactory<ligneview, Date>("Date"));
        Col_TotalV.setCellValueFactory(new PropertyValueFactory<ligneview,Float>("Total"));
    }

    public ObservableList<ligneview> GetLigneCommand(Integer id_Client, Integer Id_Command){
        ObservableList<ligneview> ligneviews = FXCollections.observableArrayList();
        String query = "select * from ligneview where id = ? and Id_commande = ?";
        connection = DBConnection.getCon();
        try {
            st = connection.prepareStatement(query);
            st.setInt(1,id_Client);
            st.setInt(2,Id_Command);
            rs = st.executeQuery();
            while(rs.next()){
                ligneview lview = new ligneview();
                lview.setProductName(rs.getNString("ProductName"));
                lview.setQuantite(rs.getInt("Quantite"));
                lview.setPrice(rs.getFloat("Price"));
                lview.setLigneTotal(rs.getFloat("LigneTotal"));
                ligneviews.add(lview);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ligneviews;
    }

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
                        }
                    }
                } ;
            }
        };
        // Just set the button cell here:
        ComboClientAllCom.setButtonCell(cellFactory.call(null));
        ComboClientAllCom.setCellFactory(cellFactory);
        try {
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getNString("nom"));
                ComboClientAllCom.getItems().add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }
    /*public ObservableList<Commande> getComandeByClient(Integer Id_Client){
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        String query = "select * from Commande where Client_id = ?";
        connection = DBConnection.getCon();
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1,Id_Client);
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                Commande commande = new Commande();
                commande.setId_commande(rs.getInt("Id_commande"));
                commande.setClient_id(rs.getInt("Client_id"));
                commande.setTotal(rs.getFloat("Total"));
                commande.setDate(rs.getDate("Date"));
                commandes.add(commande);
                System.out.println(commande.getId_commande());
                System.out.println(commande.getClient_id());
                System.out.println(commande.getTotal());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return commandes;
    }*/

    public ObservableList<Commande> GetComandeByClient(Integer Id_Client){
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        String query = "select * from Commande where Client_id = ?";
        connection = DBConnection.getCon();
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1,Id_Client);
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                Commande commande = new Commande();
                commande.setId_commande(rs.getInt("Id_commande"));
                //Integer hh = rs.getInt("Id_commande");
                commande.setId_commande(rs.getInt("Id_commande"));
                commande.setTotal(rs.getFloat("Total"));
                commande.setDate(rs.getDate("Date"));
                commandes.add(commande);
                //System.out.println(hh);
                //System.out.println(commande.getId_commande());
                //System.out.println(commande.getTotal());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return commandes;
    }

    public void showCommande(Integer Id){
        //System.out.println(Id);
        ObservableList<Commande> list = GetComandeByClient(Id);
        tbv_Commande_client.setItems(list);
        Col_Date.setCellValueFactory(new PropertyValueFactory<Commande, Date>("Date"));
        Col_Total.setCellValueFactory(new PropertyValueFactory<Commande,Float>("Total"));
        //Col_Comm_Id.setCellValueFactory(new PropertyValueFactory<Commande,Integer>("Id_commande"));

    }
}
