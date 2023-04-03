package com.esalaff.esalaff;

import com.mysql.cj.xdevapi.PreparableStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import com.esalaff.esalaff.Client;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private Parent fxml;
    @FXML
    private AnchorPane root;

    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;



    @FXML
    private TextField txtnom;

    @FXML
    private TextField txtprenom;

    @FXML
    private TextField txttele;

    @FXML
    private TextField txtemail;

    @FXML
    private TableView<Client> tbv;
    int id = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    showclients();

    }
    public ObservableList<Client> getClients(){
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String query = "select * from clientest";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getNString("nom"));
                client.setPrenom(rs.getNString("prenom"));
                client.setTele(rs.getNString("tele"));
                client.setEmail(rs.getNString("email"));
                clients.add(client);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }
    public void showclients(){
        ObservableList<Client> list = getClients();
        tbv.setItems(list);
        col_id.setCellValueFactory(new PropertyValueFactory<Client,Integer>("id"));
        col_nom.setCellValueFactory(new PropertyValueFactory<Client,String>("nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<Client,String>("prenom"));
        col_tele.setCellValueFactory(new PropertyValueFactory<Client,String>("tele"));
        col_email.setCellValueFactory(new PropertyValueFactory<Client,String>("email"));
    }

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

    @FXML
    private Button btnsave;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnclear;

    @FXML
    private Button btnupdate;

    @FXML
    void createClient(ActionEvent event){
        String insert = "insert into clientest(nom, prenom, tele, email) values(?,?,?,?)";
        con = DBConnection.getCon();
            try {
                st=con.prepareStatement(insert);
                st.setString(1,txtnom.getText());
                st.setString(2,txtprenom.getText());
                st.setString(3,txttele.getText());
                st.setString(4,txtemail.getText());
                st.executeUpdate();
                showclients();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);}
    }
    @FXML
    void getData(MouseEvent event) {
        Client client = tbv.getSelectionModel().getSelectedItem();
        id = client.getId();
        txtnom.setText(client.getNom());
        txtprenom.setText(client.getPrenom());
        txttele.setText(client.getTele());
        txtemail.setText(client.getEmail());
        showclients();
    }
    void clear(){

        txtnom.setText(null);
        txtprenom.setText(null);
        txttele.setText(null);
        txtemail.setText(null);
    }
    @FXML
    void clearfield(ActionEvent event){

        txtnom.setText(null);
        txtprenom.setText(null);
        txttele.setText(null);
        txtemail.setText(null);
    }
    @FXML
    void deleteClient(ActionEvent event){
        String delete = "delete from clientest where id = ?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(delete);
            st.setInt(1,id);
            st.executeUpdate();
            showclients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void updateClient(ActionEvent event){
        String update = "update clientest set nom = ?, prenom = ?, tele = ?, email = ? where id = ?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(update);
            st.setString(1,txtnom.getText());
            st.setString(2,txtprenom.getText());
            st.setString(3,txttele.getText());
            st.setString(4,txtemail.getText());
            st.setInt(5,id);
            st.executeUpdate();
            clear();
            showclients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Stage stage;
    private Scene scene;
    @FXML
    public void tos2(ActionEvent event) {
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
    public void tos1(ActionEvent event) {
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


}

