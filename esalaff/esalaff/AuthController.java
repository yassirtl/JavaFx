package com.esalaff.esalaff;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.stage.Stage;
public class AuthController {

    private Stage stage;
    private Scene scene;
    Connection connection = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label txtEror;

    @FXML
    void Login(ActionEvent event) {
        String query = "SELECT * from Login where Email = ? and Password = ? ";
        connection = DBConnection.getCon();
        Auth auth = null;
        try {
            st = connection.prepareStatement(query);
            st.setString(1, txtEmail.getText());
            st.setString(2, txtPassword.getText());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                auth = new Auth();
                auth.setEmail(rs.getString(1));
                auth.setPassword(rs.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (auth != null) {
            toDashboardd(event);
        }else{
            txtEror.setText("Wrong Email or Password");
        }
    }

    void toDashboardd(ActionEvent event) {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/FXML/Dashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxml);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


