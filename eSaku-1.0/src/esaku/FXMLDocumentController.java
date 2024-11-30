package esaku;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button close;
    
    @FXML
    private Button loginBtn;

    @FXML
    private TextField nama_kesiswaan;

    @FXML
    private TextField nip;
    
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void loginKesiswaan() {
        String sql = "SELECT * FROM kesiswaan WHERE nip = ? AND nama_kesiswaan = ?";
        
        connect = database.connectDb();
        
        try {
            Alert alert; 
            
            if (nama_kesiswaan.getText().isEmpty() || nip.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText("Harap isi semua kolom");
                alert.showAndWait();
                return;
            }
            
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, nip.getText());
            prepare.setString(2, nama_kesiswaan.getText());
            
            result = prepare.executeQuery();
            
            if (result.next()) {
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Login Berhasil!");
                alert.showAndWait();
                
                loginBtn.getScene().getWindow().hide();
                
                Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("NIP atau Nama Salah!");
                alert.showAndWait();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
}