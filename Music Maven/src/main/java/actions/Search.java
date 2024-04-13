package actions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import files.Song;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTableView;
import javafx.scene.control.Label;
import main.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Search {
    public static void searchForSong(MFXTableView<Song> songTable, Label songCounterLabel, MFXProgressSpinner loadingSpinner,
                                     String url, String infoFilePath, int[] counter){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Controller.incrementCounter(songCounterLabel, loadingSpinner, counter);
                try{
                    Process p1 = Runtime.getRuntime().exec("spotdl save " + url + " --save-file info/info.spotdl");
                    int exitCode = p1.waitFor();
                    String musicInfo = new String(Files.readAllBytes(Paths.get(infoFilePath)));
                    Gson gson = new Gson();
                    JsonArray jsonArray = gson.fromJson(musicInfo, JsonArray.class);
                    JsonObject songJson = jsonArray.get(0).getAsJsonObject();
                    Song newSong = new Song(songJson.get("name").getAsString(), songJson.get("artist").getAsString(),
                            songJson.get("album_name").getAsString(), url, songJson.get("year").getAsInt(),
                            songJson.get("duration").getAsInt());
                    Controller.decrementCounter(songTable, songCounterLabel, loadingSpinner, counter, newSong);
                }catch (IOException e){
                    e.printStackTrace();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
