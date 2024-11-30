package esaku;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class dashboardController implements Initializable {

    @FXML
    private Button close;

    @FXML
    private Button dataKelas_btn;

    @FXML
    private AnchorPane dataKelas_form;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private AnchorPane jenisPelanggaran_form;

    @FXML
    private Button jp_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Button logout_btn;

    @FXML
    private TableView<pelanggaranData> jenisPelanggaran_table;

    @FXML
    private TableColumn<pelanggaranData, Integer> jenisPelanggaran_col_kode;
    
    @FXML
    private TableColumn<pelanggaranData, String> jenisPelanggaran_col_jp;

    @FXML
    private TableColumn<pelanggaranData, Integer> jenisPelanggaran_col_subkode;

    @FXML
    private TableColumn<pelanggaranData, Integer> jenisPelanggaran_col_poin;

    @FXML
    private TableColumn<pelanggaranData, String> jenisPelanggaran_col_penanganan;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public ObservableList<pelanggaranData> addPelanggaranData() {
        ObservableList<pelanggaranData> listPelanggaran = FXCollections.observableArrayList();
        String sql = "SELECT * FROM pelanggaran";

        try (Connection connect = database.connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            while (result.next()) {
                listPelanggaran.add(new pelanggaranData(
                        result.getInt("kode"),
                        result.getInt("sub_kode"),
                        result.getString("jenis_pelanggaran"),
                        result.getInt("poin"),
                        result.getString("penanganan")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPelanggaran;
    }

   public void addPelanggaranShowListData() {
    try {
        ObservableList<pelanggaranData> addPelanggaranListD = addPelanggaranData();
        
        if (addPelanggaranListD == null || addPelanggaranListD.isEmpty()) {
            System.out.println("Tidak ada data pelanggaran ditemukan");
            return;
        }
        
        jenisPelanggaran_col_kode.setCellValueFactory(new PropertyValueFactory<>("kode"));
        jenisPelanggaran_col_subkode.setCellValueFactory(new PropertyValueFactory<>("subKode"));
        jenisPelanggaran_col_jp.setCellValueFactory(new PropertyValueFactory<>("jenisPelanggaran"));
        jenisPelanggaran_col_poin.setCellValueFactory(new PropertyValueFactory<>("poin"));
        jenisPelanggaran_col_penanganan.setCellValueFactory(new PropertyValueFactory<>("penanganan"));
        
        jenisPelanggaran_table.setItems(addPelanggaranListD);
        
    } catch (Exception e) {
        System.err.println("Error saat menampilkan data pelanggaran: " + e.getMessage());
        e.printStackTrace();
    }
}
    private double xOffset = 0;
    private double yOffset = 0;

    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Stage stage = (Stage) main_form.getScene().getWindow();
                    stage.hide();
                    Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));

                    root.setOnMousePressed(event -> {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    });

                    root.setOnMouseDragged(event -> {
                        newStage.setX(event.getScreenX() - xOffset);
                        newStage.setY(event.getScreenY() - yOffset);
                        newStage.setOpacity(.8);
                    });

                    root.setOnMouseReleased(event -> {
                        newStage.setOpacity(1);
                    });

                    newStage.initStyle(StageStyle.TRANSPARENT);
                    newStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            dataKelas_form.setVisible(false);
            jenisPelanggaran_form.setVisible(false);
        } else if (event.getSource() == dataKelas_btn) {
            home_form.setVisible(false);
            dataKelas_form.setVisible(true);
            jenisPelanggaran_form.setVisible(false);
        } else if (event.getSource() == jp_btn) {
            home_form.setVisible(false);
            dataKelas_form.setVisible(false);
            jenisPelanggaran_form.setVisible(true);
            addPelanggaranShowListData();
        }
    }

    @FXML
    public void close() {
        System.exit(0);
    }

    @FXML
    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        home_form.setVisible(true);
        dataKelas_form.setVisible(false);
        jenisPelanggaran_form.setVisible(false);
    }
}