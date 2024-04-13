package main;

import actions.Download;
import actions.Search;
import files.Song;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private MFXTextField urlField;
    @FXML
    private MFXButton submitButton, deleteButton, downloadButton, locationButton;
    @FXML
    private MFXTableView<Song> songTable;
    @FXML
    private MFXProgressSpinner loadingSpinner;
    @FXML
    private MFXProgressBar downloadProgress;
    @FXML
    private Label songCounterLabel, downloadLabel;
    private ObservableList<Song> songs = FXCollections.observableArrayList(new Song("a","a","a","a",2018, 254));
    private String currentDirPath, infoDirPath, infoFilePath, downloadPath;
    private int[] counter = new int[1];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Assign paths to different directories and files
        currentDirPath = System.getProperty("user.dir");
        infoDirPath = currentDirPath + "\\info";
        infoFilePath = infoDirPath + "\\info.spotdl";

        try{
            // Checks if the info directory does not exist and creates it
            File infoDir = new File(infoDirPath);
            if(!infoDir.exists()){
                Files.createDirectory(Paths.get(infoDirPath));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        counter[0] = 0;
        songCounterLabel.setText(String.valueOf(counter[0]));

        deleteButton.setDisable(true);
        downloadButton.setDisable(true);

        downloadLabel.setText("...");

        initTable();
    }

    public void addSong(ActionEvent event){
        if(!urlField.getText().isEmpty()){
            if(checkURL(urlField.getText())){
                String url = urlField.getText();
                if(url.contains("?")){
                    url = url.split("\\?si")[0];
                }
                urlField.clear();
                Search.searchForSong(songTable, songCounterLabel, loadingSpinner, url, infoFilePath, counter);
            }else{
                showAlert("Enter A Valid URL", Alert.AlertType.ERROR);
            }
        }
    }

    public void delete(ActionEvent event){
        songTable.getItems().remove(songTable.getSelectionModel().getSelectedValues().get(0));
        songTable.getSelectionModel().clearSelection();
    }

    public void chooseLocation(ActionEvent event){
        DirectoryChooser chooser = new DirectoryChooser();
        downloadPath = chooser.showDialog(((Node)(event.getSource())).getScene().getWindow()).getPath();
        if(downloadPath != null){
            downloadButton.setDisable(false);
        }
    }

    public void download(ActionEvent event){
        if(!songTable.getItems().isEmpty()){
            Download.downloadSongs(songTable.getItems(), songTable, downloadLabel, downloadProgress, loadingSpinner, downloadPath);
        }
    }

    private void initTable(){
        MFXTableColumn<Song> nameColumn = new MFXTableColumn<>("Name", true, Comparator.comparing(Song::getName));
        MFXTableColumn<Song> artistColumn = new MFXTableColumn<>("Artist", true, Comparator.comparing(Song::getArtist));
        MFXTableColumn<Song> albumColumn = new MFXTableColumn<>("Album", true, Comparator.comparing(Song::getAlbum));
        MFXTableColumn<Song> durationColumn = new MFXTableColumn<>("Duration", true, Comparator.comparing(Song::getDuration));
        MFXTableColumn<Song> yearColumn = new MFXTableColumn<>("Year", true, Comparator.comparing(Song::getYear));

        nameColumn.setRowCellFactory(song -> new MFXTableRowCell<>(Song::getName));
        artistColumn.setRowCellFactory(song -> new MFXTableRowCell<>(Song::getArtist));
        albumColumn.setRowCellFactory(song -> new MFXTableRowCell<>(Song::getAlbum));
        durationColumn.setRowCellFactory(song -> new MFXTableRowCell<>(Song::getDuration));
        yearColumn.setRowCellFactory(song -> new MFXTableRowCell<>(Song::getYear));

        songTable.getTableColumns().addAll(nameColumn, artistColumn, albumColumn, durationColumn, yearColumn);
        songTable.getFilters().addAll(
                new StringFilter<>("Name", Song::getName),
                new StringFilter<>("Artist", Song::getArtist),
                new StringFilter<>("Album", Song::getAlbum)
        );

        songTable.getSelectionModel().selectionProperty().addListener(new ChangeListener<ObservableMap<Integer, Song>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableMap<Integer, Song>> observableValue, ObservableMap<Integer, Song> integerSongObservableMap, ObservableMap<Integer, Song> t1) {
                if(t1 != null){
                    if(t1.size() != 0){
                        deleteButton.setDisable(false);
                    }else{
                        deleteButton.setDisable(true);
                    }
                }else{
                    deleteButton.setDisable(true);
                }
            }
        });

        songTable.getSelectionModel().setAllowsMultipleSelection(false);

        refreshTable();
    }

    public void refreshTable(){
        songTable.setItems(songs);
        songTable.getItems().clear();
    }

    public static void incrementCounter(Label songCounterLabel, MFXProgressSpinner loadingSpinner, int[] counter){
        Platform.runLater(
            new Runnable() {
                @Override
                public void run() {
                    counter[0]++;
                    songCounterLabel.setText(String.valueOf(counter[0]));
                    loadingSpinner.setVisible(true);
                }
            }
        );
    }

    public static void decrementCounter(MFXTableView<Song> songTable, Label songCounterLabel, MFXProgressSpinner loadingSpinner, int[] counter, Song newSong){
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        counter[0]--;
                        songCounterLabel.setText(String.valueOf(counter[0]));
                        if(counter[0] == 0){
                            loadingSpinner.setVisible(false);
                        }
                        songTable.getItems().add(newSong);
                    }
                }
        );
    }

    public static void updateDownload(int counter, int total, Label downloadLabel, MFXProgressBar downloadProgress, MFXProgressSpinner loadingSpinner,MFXTableView<Song> songTable){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                downloadProgress.setProgress((double)counter / (double)total);
                downloadLabel.setText(counter + " Out Of " + total);
                loadingSpinner.setVisible(true);
                if(counter == total){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Download Complete");
                    alert.setHeaderText(total + " Songs Have been Downloaded");
                    alert.showAndWait();
                    loadingSpinner.setVisible(false);
                    songTable.getItems().clear();
                }
            }
        });
    }

    private boolean checkURL(String url){
        if(url.contains("open.spotify.com/track/")){
            return true;
        }
        return false;
    }

    private void showAlert(String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}