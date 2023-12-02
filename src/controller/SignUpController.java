package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DBConnect;

public class SignUpController implements Initializable {
	private static String userId;
	DBConnect conn = null;
	
	@FXML
    private TextField restaurantname;
	
	@FXML
    private TextField username;
	
	@FXML
    private PasswordField password;
	
	@FXML
    private PasswordField confirmpassword;
	
	@FXML
    private Button signup;
	
	@FXML
    private Label wrongsignup;
	    
	@FXML
    private ChoiceBox usertype;
	
    public SignUpController() {  
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

    public void Signup(ActionEvent event) throws IOException {
		Connection connection;
		try {
			connection = conn.connect();
			String rname = restaurantname.getText().toString();
			String user = username.getText().toString();
			String pass1 = password.getText().toString();
			String pass2 = confirmpassword.getText().toString();
			Object utypetemp = usertype.getValue();
			if(rname == null) {
				wrongsignup.setText("Please enter Restaurant name !");
	    	}
	    	else if(user == null) {
	    		wrongsignup.setText("Please enter Username !");
	    	}
	    	else if(pass1 == null) {
	    		wrongsignup.setText("Please enter Password !");
	    	}
	    	else if(pass2 == null) {
	    		wrongsignup.setText("Please enter Password again !");
	    	}
	    	else if(!pass1.equals(pass2)) {
	    		wrongsignup.setText("Please enter the same password twice !");
	    	}
	    	else if(utypetemp == null) {
	    		wrongsignup.setText("Please select User Type !");
	    	}
	    	else {
	    		String utype = "";
				if(utypetemp != null) {
					utype = utypetemp.toString().toLowerCase();
				}
				String sql = "INSERT INTO users (username,password,user_type,name) VALUES (?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, rname);
				preparedStatement.setString(2, user);
				preparedStatement.setString(3, pass1);
				preparedStatement.setString(4, utype);
				int rowsAffected = preparedStatement.executeUpdate();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT id FROM users where username = '" + user + "' AND user_type = '" + utype.toLowerCase() + "';");
				userId = resultSet.getString("id").toString();
				if (rowsAffected > 0) {
	            	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
	    			Parent root = loader.load();
	    			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	                stage.setScene(new Scene(root));
	            }
				 preparedStatement.close();
	    	}
	        connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}     
    }
}
