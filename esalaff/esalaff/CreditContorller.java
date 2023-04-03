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
import java.sql.*;
import java.util.ResourceBundle;

public class CreditContorller implements Initializable {

    @FXML
    private ComboBox<Client> comboboxClient;
    @FXML
    private TableView<Credit> tbv_Credit;

    @FXML
    private TextArea txtShowCredit;

    @FXML
    private TextArea txtMontant;

    @FXML
    private TextArea txtShowReste;

    @FXML
    private TableColumn<Credit, Date> Col_Date;

    @FXML
    private TableColumn<Credit, Float> Col_Montant;

    @FXML
    private TableColumn<Credit, Float> Col_Reste;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getClients();
        comboboxClient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> observableValue, Client client, Client t1) {
                    //System.out.println(t1.getNom());
                    txtShowCredit.setText(Float.toString(getCredit(t1.getId())));
                    showCredits();
            }
        });
    }
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
                        }
                    }
                } ;
            }
        };
        // Just set the button cell here:
        comboboxClient.setButtonCell(cellFactory.call(null));
        comboboxClient.setCellFactory(cellFactory);
        try {
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getNString("nom"));
                comboboxClient.getItems().add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }
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
    @FXML
    void savePayment(ActionEvent event){
        Client Selectedclient = comboboxClient.getValue();
        Integer id= Selectedclient.getId();
        Float credit = getCredit(id)-Float.parseFloat(txtMontant.getText());
        saveCredit(id,credit);
        //Float fff = Float.parseFloat(txtMontant.getText());
        WritePayment(id,Float.parseFloat(txtMontant.getText()));
        txtShowCredit.setText(Float.toString(getCredit(id)));
        clear();
        showCredits();
    }
    public ObservableList<Credit> GetCredits(Integer id_client){
        ObservableList<Credit> credits = FXCollections.observableArrayList();
        String query = "select * from Credit where Id_client = ?";
        connection = DBConnection.getCon();


        try {
            st = connection.prepareStatement(query);
            st.setInt(1,id_client);
            rs = st.executeQuery();
            while(rs.next()){
                Credit credit = new Credit();
                credit.setId_credit(rs.getInt("Id_payment"));
                credit.setMontant(rs.getFloat("Montant"));
                credit.setDate(rs.getDate("Date"));
                credits.add(credit);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //showCredits();

        return credits;
    }
    public void showCredits(){
        if((comboboxClient.getValue())!=null){
            System.out.println((comboboxClient.getValue()).getId());
            ObservableList<Credit> list = GetCredits((comboboxClient.getValue()).getId());
            System.out.println(list.stream().count());

            tbv_Credit.setItems(list);

            Col_Montant.setCellValueFactory(new PropertyValueFactory<Credit,Float>("Montant"));
            Col_Date.setCellValueFactory(new PropertyValueFactory<Credit,Date>("Date"));
        }
    }
    void WritePayment(Integer id_Client,Float Montant){

        String query = "insert into Credit(Id_client,Montant ,Date ) values(?,?,?)";
        connection = DBConnection.getCon();
        Client client = comboboxClient.getValue();
        java.util.Date date=new java.util.Date();
        java.sql.Date sqlDate=new java.sql.Date(date.getTime());
        try {
            st = connection.prepareStatement(query);
            st.setInt(1,client.getId());
            st.setFloat(2,Float.parseFloat(txtMontant.getText()));
            st.setDate(3,sqlDate);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void saveCredit(Integer ClientId,Float Credit){
        String query = "update Clientest set Credit = ? where id = ?";
        try {
            st=connection.prepareStatement(query);
            st.setFloat(1, Credit);
            st.setInt(2,ClientId);
            st.executeUpdate();
            //clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void clear(){
        txtMontant.setText(null);
    }

}