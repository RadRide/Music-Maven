package files;

public class Song {

    private String name, artist, album, duration, url;
    private int year, seconds;

    public Song(String name, String artist, String album, String url, int year, int seconds){
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.seconds = seconds;
        this.url = url;
        setDuration(seconds);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(int seconds) {
        this.duration = (int)(seconds / 60) + ":" + (seconds % 60);
    }

    public int getYear() {
        return year;
    }
    public String getYearAsString(){
        return String.valueOf(year);
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getSeconds() {
        return seconds;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
