package instagram.media;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Media implements Serializable {

    private final String name;

    private final String internalName;

    private int likes;

    private boolean adultContent;

    private final ArrayList<String> comments;

    private double score;

    private int numVotes;

    private ImageIcon cover;

    public Media(String name) {
        this.name = name;
        internalName = name.trim().replaceAll("[^a-zA-Z0-9]+", "_").toLowerCase();
        likes = 0;
        adultContent = false;
        comments = new ArrayList<>();
        score = 0;
        numVotes = 0;
        cover = null;
    }

    public String getName() {
        return name;
    }

    public String getInternalName() {
        return internalName;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isAdultContent() {
        return adultContent;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public double getScore() {
        return score;
    }

    public ImageIcon getCover() {
        return cover;
    }

    public void addLike() {
        likes++;
    }

    public void tagAdultContent(boolean flag) {
        adultContent = flag;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void addScore(double sc) {
        score = ((score * numVotes) + sc) / (++numVotes);
    }

    public void setCover(ImageIcon img) {
        cover = img;
    }

    public String toString() {
        showCover();
        String msg = "\n\n© " + name + (adultContent ? " [+18]" : " [AP (all public)]") + ".";
        msg += "\n \u2606" + " " + Math.round(score * 100) / 100.0 + " (" + numVotes + " votes)";
        msg += "\n \uD83D\uDC4D" + " " + likes;
        msg += "\n " + comments.size() + " comments found on this media:";
        for (int i = 0; i < comments.size(); i++) msg += " \n #" + i + ": " + comments.get(i) + ".";
        return msg;
    }

    public void showCover() {
        if (cover == null) return;
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.add((new JPanel()).add(new JLabel(getCover())));
        frame.pack();
        frame.setVisible(true);
    }

    public void loadCover(String path) {
        setCover(new ImageIcon(Toolkit.getDefaultToolkit().createImage(path)));
    }
}