package ohtu.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Audiobook
 */
public class Audiobook extends BookSuper {

    private List<Timestamp> timestampList = new ArrayList<>();
    private File mp3;

    public Audiobook(String title, String author, File mp3, List<Timestamp> timestamps, File cover) {
        this.title = title.trim();
        this.author = author.trim();
        this.mp3 = mp3;
        this.timestampList = timestamps;
    }

    public Audiobook(String title, String author, File mp3) {
        this.title = title.trim();
        this.author = author.trim();
        this.mp3 = mp3;
    }

    public Audiobook(String title, String author, File mp3, File cover) {
        this.title = title.trim();
        this.author = author.trim();
        this.mp3 = mp3;
        this.cover = cover;
    }

    /*
     * public Audiobook(int id, String title, String author, File mp3) { this.id =
     * id; this.title = title.trim(); this.author = author.trim(); this.mp3 = mp3; }
     */

    public void addTimestamp(Timestamp timestamp) {
        timestampList.add(timestamp);
    }

    public void addTimestamps(List<Timestamp> timestamps) {
        timestampList.addAll(timestamps);
    }

    public List<Timestamp> getTimestampList() {
        java.util.Collections.sort(timestampList);
        return timestampList;
    }

    public File getMp3() {
        return mp3;
    }

    public File getCover() {
        return cover;
    }

    @Override
    public boolean isEmpty() {
        if (this.author.isEmpty() || this.title.isEmpty() || this.mp3 == null) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + " (audio)";
    }
}