package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    	private static String userId;
		DBConnect conn = null;
	
	    @FXML
	    private TextField username;

	    @FXML
	    private PasswordField password;

	    @FXML
	    private Button login;

	    @FXML
	    private Label wrongLogin;
	    
	    @FXML
        private ChoiceBox usertype;
	    
	    public LoginController() {  
	        conn = new DBConnect();
	    }
	    
	    public void setUserId(String data) {
	        this.userId = data;
	    }
	    public static String getUserId() {
	        return userId;
	    }
	    
	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	    	usertype.getItems().addAll("USER", "ADMIN");
	    }

	    public void gotoHome(ActionEvent event) throws IOException {
    		Connection connection;
			try {
				connection = conn.connect();
				Statement statement = connection.createStatement();
				String user = username.getText().toString();
				String pass = password.getText().toString();
				Object utypetemp = usertype.getValue();
				String utype = "";
				if(utypetemp != null) {
					utype = utypetemp.toString();
				}
				ResultSet resultSet = statement.executeQuery("SELECT id, username, password, user_type FROM users where username = '" + user + "' AND user_type = '" + utype.toLowerCase() + "';");
				if(resultSet.next() && resultSet.getString("username").equals(user) && resultSet.getString("password").equals(pass)) {
    	    		if(utype.equals("ADMIN") == true) {
    	    			userId = resultSet.getString("id").toString();
    	    			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/RestaurantLanding.fxml"));
        	            Parent secondPage = loader.load();
        	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        	            stage.setScene(new Scene(secondPage));
    	    		}
    	    		else {
    	    			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Customer.fxml"));
        	            Parent secondPage = loader.load();
        	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        	            stage.setScene(new Scene(secondPage));
    	    		}
    	    	}
    	    	else if(user == null) {
    	    		wrongLogin.setText("Please enter Username !");
    	    	}
    	    	else if(pass == null) {
    	    		wrongLogin.setText("Please enter Password !");
    	    	}
    	    	else if(utypetemp == null) {
    	    		wrongLogin.setText("Please select User Type !");
    	    	}
    	    	else {
    	    		wrongLogin.setText("Wrong Username or Password");
    	    	}
		        connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}     
	    }
	    
	    public void gotoSignup(ActionEvent event) throws Exception {
	    	try {
	    		FXMLLoader sloader = new FXMLLoader(getClass().getResource("../view/SignUp.fxml"));
	            Parent signupPage = sloader.load();
	            Stage sstage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            sstage.setScene(new Scene(signupPage));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
}
		
