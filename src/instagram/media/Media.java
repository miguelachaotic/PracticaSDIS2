package instagram.media;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
public class Media implements Serializable {
    private String name;
    private String internalName;
    private int likes;
    private boolean adultContent;
    private ArrayList<String> comments;
    private double score;
    private int numVotes;
    private ImageIcon cover;
    public Media(String name) {
        this.name = name;
        this.internalName = name.trim().replaceAll("[^a-zA-Z0-9]+", "_").toLowerCase();
        this.likes = 0;
        this.adultContent = false;
        this.comments = new ArrayList<>();
        this.score = 0;
        this.numVotes = 0;
        this.cover = null;
    }
    public String getName(){ return this.name; }
    public String getInternalName(){ return this.internalName; }
    public int getLikes() { return this.likes; }
    public boolean isAdultContent() {return this.adultContent; }
    public ArrayList<String> getComments() {return this.comments; }
    public double getScore() { return this.score; }
    public ImageIcon getCover() { return this.cover; }
    public void addLike() { this.likes++; }
    public void tagAdultContent(boolean flag) { this.adultContent = flag; }
    public void addComment(String comment) { this.comments.add(comment); }
    public void addScore(double sc) { score = ( (score * numVotes) + sc) / (++numVotes); }
    public void setCover(ImageIcon img) { this.cover = img; }
    public String toString() {
        showCover();
        String msg = "\n\n© "+this.name+(this.adultContent ? " [+18]" : " [AP (all public)]")+".";
        msg += "\n \u2606"+" "+Math.round(this.score*100)/100.0+" ("+this.numVotes+" votes)";
        msg += "\n \uD83D\uDC4D"+" "+this.likes;
        msg += "\n "+this.comments.size()+" comments found on this media:";
        for(int i=0; i<this.comments.size(); i++) msg += " \n #"+i+": "+this.comments.get(i)+".";
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