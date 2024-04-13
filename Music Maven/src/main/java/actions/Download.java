package actions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import files.Song;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTableView;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import main.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Download {

    public static void downloadSongs(ObservableList<Song> songs, MFXTableView<Song> songTable, Label downloadLabel,
                                     MFXProgressBar downloadProgress, MFXProgressSpinner loadingSpinner, String downloadPath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i = 0; i < songs.size(); i++){
                        Controller.updateDownload(i, songs.size(), downloadLabel, downloadProgress, loadingSpinner, songTable);
                        ProcessBuilder builder = new ProcessBuilder(
                                "cmd.exe", "/c", "cd " + downloadPath + " && spotdl --bitrate 320k " + songs.get(i).getUrl()
                        );
                        builder.redirectErrorStream(true);
                        Process p1 = builder.start();
                        int exitCode = p1.waitFor();
                    }
                    Controller.updateDownload(songs.size(), songs.size(), downloadLabel, downloadProgress, loadingSpinner, songTable);
                }catch (IOException e){
                    e.printStackTrace();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
