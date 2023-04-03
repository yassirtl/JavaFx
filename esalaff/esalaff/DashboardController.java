package com.esalaff.esalaff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    Connection connection = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtNbClient.setText(Integer.toString(GetNumberOFClient()));
        txtComandeTotal.setText(Integer.toString(GetNumberOFCommandes()));
        txtNbTotalCredit.setText(Integer.toString(GetTotalCredit()));
        txtChiffreAffaire.setText(Integer.toString(GetChiffreAffaire()));
    }

    @FXML
    private TextArea txtNbClient;

    @FXML
    private TextArea txtComandeTotal;

    @FXML
    private TextArea txtNbTotalCredit;

    @FXML
    private TextArea txtChiffreAffaire;

    @FXML
    private TableView<Produit> tbvProduct;
    @FXML
    private TableColumn<Produit, Integer> col_id_p;

    @FXML
    private TableColumn<Produit, String > col_product_name;

    @FXML
    private TableColumn<Produit, String> col_price;

    //
    private Stage stage;
    private Scene scene;
    public void tos1(ActionEvent event) {
        try {
            Parent fxml =  FXMLLoader.load(getClass().getResource("/FXML/Clientv.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxml);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void Tocommande(ActionEvent event) {
        try {
            Parent fxml =  FXMLLoader.load(getClass().getResource("/FXML/ToutCommande.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxml);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void ToCredit(ActionEvent event) {
        try {
            Parent fxml =  FXMLLoader.load(getClass().getResource("/FXML/Payment.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxml);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void toProduct(ActionEvent event) {
        try {
            Parent fxml =  FXMLLoader.load(getClass().getResource("/FXML/AddProduct.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxml);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Parent fxml;
    @FXML
    private AnchorPane root;

    Connection con = null;


    int id = 0;
    public ObservableList<Client> getClients(){
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String query = "select * from clientest";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
                Client st = new Client();
                st.setId(rs.getInt("id"));
                st.setNom(rs.getNString("nom"));
                st.setPrenom(rs.getNString("prenom"));
                st.setTele(rs.getNString("tele"));
                st.setEmail(rs.getNString("email"));
                clients.add(st);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    @FXML
    private TableView<Client> tbv;

    @FXML
    private TableColumn<Client, Integer> col_id;

    @FXML
    private TableColumn<Client, String> col_nom;

    @FXML
    private TableColumn<Client, String> col_prenom;

    @FXML
    private TableColumn<Client, String> col_tele;

    @FXML
    private TableColumn<Client, String> col_email;


    Integer GetNumberOFClient()
    {
        Integer numberOFClients= 0;
        String query = "SELECT COUNT(*) FROM clientest as numberOfClient";
        connection = DBConnection.getCon();
        try {
            st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                 numberOFClients = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return numberOFClients;
    }
    Integer GetNumberOFCommandes()
    {
        Integer numberOFCommandes= 0;
        String query = "SELECT COUNT(*) FROM Commande as numberOfCommands";
        connection = DBConnection.getCon();
        try {
            st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                numberOFCommandes = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return numberOFCommandes;
    }
    Integer GetTotalCredit()
    {
        Integer totalCredit= 0;
        String query = "SELECT sum(Credit) from clientest ";
        connection = DBConnection.getCon();
        try {
            st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                totalCredit = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalCredit;
    }
    Integer GetChiffreAffaire()
    {
        Integer chiffreAffaire= 0;
        String query = "SELECT sum(Total) from commande ";
        connection = DBConnection.getCon();
        try {
            st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                chiffreAffaire = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return chiffreAffaire;
    }


    @FXML
    public void LogOut(ActionEvent event) {
    toLogin(event);
    }
    @FXML
    public void toLogin(ActionEvent event) {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/FXML/Auth.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxml);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }








}
