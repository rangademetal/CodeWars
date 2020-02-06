package test;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Controller {

    Utilities fnc = new Utilities();


    String secretKey = "JavaWebBuilderCMS";
    boolean isOkay = false;
    boolean isVerified = false;

    InetAddress ip;
    String hostname;


    // Login form
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;

    // Register form
    @FXML
    private PasswordField passField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;

    @FXML
    private TextField getVerification;
    @FXML
    private AnchorPane rootPanelLogin;
    @FXML
    private AnchorPane rootPanelRegister;
    @FXML
    private AnchorPane rootPanelVerification;
    @FXML
    private AnchorPane rootPanelMain;

    static Connection con = null;
    static PreparedStatement pst = null;

    static String username;

    public void hello(ActionEvent env) {

        try {
            String isValid = "SELECT * FROM root_test where (EMAIL=? and PASSWORD=?)  AND verified = 'wdQgsjgiB7+0m5aJ1PO0gQ=='";
            String isNotValid = "SELECT * FROM root_test where (EMAIL=? and PASSWORD=?)  AND verified = '6+4X1RFml6IirbixxXvn3w=='";
            String sqlText = "SELECT verified FROM root_test where (EMAIL=? and PASSWORD=?)";
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://hosting1993073.online.pro:3306/00286862_test", "00286862_test", "Y6ufde_e");

            pst = conn.prepareStatement(sqlText);

            String user = fnc.encrypt(userField.getText(), secretKey);
            String pass = fnc.encrypt(passwordField.getText(), secretKey);
            username = fnc.encrypt(userField.getText(), secretKey);

            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();
            if (rs.next())  {
                if(rs.getString("verified").equals("wdQgsjgiB7+0m5aJ1PO0gQ==")) {
                    System.out.println("Working account");
                    AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("CMS.fxml"));
                    rootPanelLogin.getChildren().setAll(pane);
                }
                else if(rs.getString("verified").equals("6+4X1RFml6IirbixxXvn3w==")) {
                    AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("VerifiedForm.fxml"));
                    rootPanelLogin.getChildren().setAll(pane);

                }
            }
            conn.close();
        }

        catch (Exception ex) {
            System.out.println("Error " +ex.getMessage());
        }
    }

    @FXML
    public void registerScene(ActionEvent env) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
        rootPanelLogin.getChildren().setAll(pane);
    }

    @FXML
    public void loginScene(ActionEvent env) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            rootPanelRegister.getChildren().setAll(pane);
        }
        catch(Exception e) {
            System.out.println("Error! " +e.getMessage());
        }
    }



    @FXML
    public void verification(ActionEvent evt) {
        try {
            con = DriverManager.getConnection("jdbc:mysql://hosting1993073.online.pro:3306/00286862_test", "00286862_test", "Y6ufde_e");
            System.out.println("username "+username);
            isVerified = true;
            String verified = fnc.encrypt(Boolean.toString(isVerified), secretKey);
            System.out.println(verified);

            String code = getVerification.getText();
            System.out.println(code);

            String codeValid = fnc.encrypt(code, secretKey );
            System.out.println(codeValid);

            String sql = "UPDATE root_test set verified='"+verified+"' WHERE email='"+username+"' AND number='"+codeValid+"'";
            if()
            System.out.println("Input from your text field "+ code);
            pst = con.prepareStatement(sql);
            pst.executeUpdate();
            con.close();
            AnchorPane pane = FXMLLoader.load(getClass().getResource("CMS.fxml"));
            rootPanelVerification.getChildren().setAll(pane);
        }

        catch(Exception e) {
            System.out.println("Erorr - " +e.getMessage());

        }
    }

    @FXML
    public void from(ActionEvent ee) {

    }

    public void Register(ActionEvent env) {

        try {

            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();


            int randNumber = fnc.randomGenerator();
            System.out.println(randNumber);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://hosting1993073.online.pro:3306/00286862_test", "00286862_test", "Y6ufde_e");

            isOkay = false;
            String sql = "INSERT INTO root_test(email, username, number, verified, password, hostname) values (?,?,?,?,?,?)";

            String email = emailField.getText();

            if (passField.getText().equals("") && emailField.getText().equals("")) {

                System.out.println("Error " + isOkay);
            } else {
                if (fnc.isValid(email)) {
                    isOkay = true;
                    System.out.println(isOkay);
                }
            }
            if (isOkay == true)
            {
                SendMail send = new SendMail(emailField.getText(), fnc.encrypt(Integer.toString(randNumber), secretKey));
                send.mail();

                pst = con.prepareStatement(sql);
                pst.setString(1, fnc.encrypt(emailField.getText(), secretKey));
                pst.setString(2, fnc.encrypt(usernameField.getText(), secretKey));
                pst.setString(3, fnc.encrypt(Integer.toString(randNumber), secretKey));
                pst.setString(4, fnc.encrypt(Boolean.toString(isVerified), secretKey));
                pst.setString(5, fnc.encrypt(passField.getText(), secretKey));
                pst.setString(6, fnc.encrypt(hostname,secretKey));

                System.out.println("Working");

                pst.executeUpdate();
                con.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}