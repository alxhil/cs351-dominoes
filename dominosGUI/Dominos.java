package dominosGUI;

import javafx.scene.Node;
import javafx.scene.image.ImageView;


public class Dominos {
    private int firstSide;
    private int secSide;
    private String imgSrc;
    private ImageView imageView;
    public Dominos(int firstSide, int secSide, ImageView imageView){
        this.firstSide = firstSide;
        this.secSide = secSide;
        this.imageView = imageView;
    }

    public String getImgSrc(){
        return this.imgSrc;
    }

    public int getFirstSide(){
        return this.firstSide;
    }

    public int getSecSide(){
        return this.secSide;
    }

    public ImageView getImageView(){
        return this.imageView;
    }


}
