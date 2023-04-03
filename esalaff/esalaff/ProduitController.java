package com.esalaff.esalaff;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProduitController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;


    @FXML
    private TextField txtproduct;

    @FXML
    private TextField txtprice;

    @FXML
    private Button btnAddProd;
    @FXML
    private TableView<Produit> tbvProduct;

    @FXML
    private TableColumn<Produit, Integer> col_id_p;

    @FXML
    private TableColumn<Produit, String > col_product_name;

    @FXML
    private TableColumn<Produit, String> col_price;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProduct();
    }


    public ObservableList<Produit> getProducts(){
        ObservableList<Produit> Produits = FXCollections.observableArrayList();
        String query = "select * from Produit";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
                Produit st = new Produit();
                st.setId(rs.getInt("id"));
                st.setProductName(rs.getNString("ProductName"));
                st.setPrice(rs.getInt("Price"));
                Produits.add(st);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Produits;
    }
    public void showProduct(){
        ObservableList<Produit> list = getProducts();
        tbvProduct.setItems(list);
        col_id_p.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("id"));
        col_product_name.setCellValueFactory(new PropertyValueFactory<Produit,String>("ProductName"));
        col_price.setCellValueFactory(new PropertyValueFactory<Produit,String>("Price"));
    }


    void createClient(ActionEvent event){
        String insert = "insert into Produit(ProductName, Price) values(?,?)";
        con = DBConnection.getCon();

        try {
            st=con.prepareStatement(insert);
            st.setString(1,txtproduct.getText());
            st.setString(2,txtprice.getText());

            st.executeUpdate();
            showProduct();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    int id = 0;
    private Stage stage;
    private Scene scene;

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
    void createProduct(ActionEvent event){
        String insert = "insert into Produit( ProductName, Price) values(?,?)";
        con = DBConnection.getCon();
        try {
            st=con.prepareStatement(insert);
            st.setString(1,txtproduct.getText());
            st.setString(2,txtprice.getText());
            st.executeUpdate();
            showProduct();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void clear(){
        txtproduct.setText(null);
        txtprice.setText(null);

    }
    @FXML
    void clearfield(ActionEvent event){
        txtproduct.setText(null);
        txtprice.setText(null);
    }
    @FXML
    void getData(MouseEvent event) {
        Produit produit = tbvProduct.getSelectionModel().getSelectedItem();
        id = produit.getId();
        txtproduct.setText(produit.getProductName());
        txtprice.setText(produit.getPrice().toString());
        showProduct();

    }
    @FXML
    void updateClient(ActionEvent event){
        String update = "update Produit set ProductName = ?, Price = ? where id = ?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(update);
            st.setString(1,txtproduct.getText());
            st.setString(2,txtprice.getText());
            st.setInt(3,id);
            st.executeUpdate();
            clear();
            showProduct();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void deleteProduit(ActionEvent event){
        String delete = "delete from Produit where id = ?";
        con = DBConnection.getCon();
        try {

            st = con.prepareStatement(delete);
            st.setInt(1,id);
            st.executeUpdate();
            showProduct();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    Connection connection = null;



}