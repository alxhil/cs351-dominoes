package dominosGUI;

//Alex Hill 2019
//CS 351 Project 2



import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;


public class GameManager extends Application {

    private Pane root;
    private static Rectangle r1, r2, r3, r4,r5;
    private static Text t1,t2;
    private static int gameAcc = 0;
    private static int dominoNumber;
    private static Boolean firstMove = false;
    private static Boolean pickableDomino = true;
    private static Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private static ArrayList<Dominos> enemyHand = new ArrayList<>();
    private static ArrayList<Dominos> playerHand = new ArrayList<>();
    private static ArrayList<Dominos> gameDeck = new ArrayList<>();
    private static ArrayList<Dominos> shownDominos = new ArrayList<>();



    public static void main(String[] args) {
        launch(args);
    }



    private Parent defaultGame() {
        root = new Pane();
        root.setPrefSize(1600D, 980D);
        AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {
                gameLoop();
            }
        };
        //r1 is background

        alert.setTitle("Bad Move");
        alert.setHeaderText("You played a bad move!");
        alert.setContentText("You played a bad move, try again!");

        t1 = new Text(55,915,"Bone Yard");
        t2 = new Text(55,945,"Dominos: "+gameDeck.size());
        r1 = new Rectangle(1600,1000,Color.GRAY);
        //r2 is player hand box
        r2 = new Rectangle(500,100,Color.BLUE);
        //r3 is enemy hand box
        //r3 = new Rectangle(300,50,Color.RED);
        //r4 is game board box
        r4 = new Rectangle(1600,300,Color.GREEN);
        //r5 is boneyard
        r5 = new Rectangle(100,100,Color.PURPLE);
        r5.setOnMouseClicked(boneYardClick);
        root.getChildren().addAll(r1,r2,r4,r5,t1,t2);
        boardBoxSetup();
        timer.start();
        createDominosDeck();
        for(int i = 0; i < gameDeck.size(); i++){
            //gameDeck.get(i).getImageView().setRotate(
            //        gameDeck.get(i).getImageView().getRotate()+90);
            root.getChildren().add(gameDeck.get(i).getImageView());
            gameDeck.get(i).getImageView().setTranslateY(9999);
            gameDeck.get(i).getImageView().setTranslateX(97);
        }
        shuffleDominos();
        System.out.println(gameDeck.size());
        createHand(playerHand);
        playerHandPos();

        return root;
    }

    //Sets coordinates for boxes
    private void boardBoxSetup() {
        r2.setTranslateX(250);
        r2.setTranslateY(770);
        r4.setTranslateX(150);
        r4.setTranslateY(370);
        r5.setTranslateX(50);
        r5.setTranslateY(900);
    }

    //Creates deck and loads in pictures
    private void createDominosDeck() {
        for(int i = 6; i >= 0; i--){
            for(int j = i; j >= 0; j--){
                String temp = "/res/"+i+"-"+j+".png";
                System.out.println(temp);

                gameDeck.add(new Dominos(j,i, new ImageView(
                        new Image(getClass().getResourceAsStream(temp))), 0));
            }
        }
    }

    private void playerHandPos(){

        for(int i = 0; i < playerHand.size(); i++){
            playerHand.get(i).getImageView().setTranslateX(300 +(50*i));
            playerHand.get(i).getImageView().setTranslateY(800);
        }
    }

    private void shownDominosPos(){
        int yChange = 50;
        for(int i = 0; i < shownDominos.size(); i++){
            shownDominos.get(i).getImageView().setTranslateX(400+i*35);
            if(i%2 == 0){
                shownDominos.get(i).getImageView().setTranslateY(500);
            } else {
                shownDominos.get(i).getImageView().setTranslateY(460);
            }

        }
    }

    private void shuffleDominos(){
        Collections.shuffle(gameDeck);
    }


    private void createHand(ArrayList<Dominos> a) {
        for(int i = 0; i < 7; i++) {
            Dominos temp = gameDeck.get(gameDeck.size()-1);
            gameDeck.remove(gameDeck.size()-1);
            System.out.println("Removed a domino, size is now"+gameDeck.size());
            a.add(temp);
        }
    }



    private void gameLoop() {

        for (int i = 0; i < playerHand.size(); i++) {
            isClickable(playerHand.get(i));
        }

        if(shownDominos.size() > 0){
            playerHandPos();
            shownDominosPos();

            t2.setText("Dominos: "+gameDeck.size());
            if(pickableDomino && dominoNumber != -1) {

                //Game Logic
                if (playerHand.get(dominoNumber).getFirstSide() == shownDominos.get(shownDominos.size() - 1).getSecSide()) {
                    firstSideMove();
                } else if (playerHand.get(dominoNumber).getSecSide() == shownDominos.get(shownDominos.size() - 1).getSecSide()) {
                    secSideMove();
                } else if (playerHand.get(dominoNumber).getFirstSide() == shownDominos.get(0).getFirstSide()) {
                    placeBackwards();
                } else if (playerHand.get(dominoNumber).getSecSide() == shownDominos.get(0).getFirstSide()) {
                    placeBackwards();
                } else if (shownDominos.get(0).getFirstSide() == 0) {
                    placeBackwards();
                } else if (playerHand.get(dominoNumber).getFirstSide() == 0) {
                    secSideMoveBlank();
                } else if (playerHand.get(dominoNumber).getSecSide() == 0) {
                    firstSideMove();
                } else if (shownDominos.get(shownDominos.size() -1).getSecSide() == 0) {
                    firstSideMove();
                }  else {
                    alert.show();
                    pickableDomino = false;
                }
            }


        }
        else if(shownDominos.size() == 0 && firstMove){
            shownDominos.add(playerHand.get(dominoNumber));
            playerHand.remove(dominoNumber);
            dominoRotate();
            dominoNumber = -1;

        }
    }

    private void placeBackwards() {
        shownDominos.add(0,playerHand.get(dominoNumber));
        playerHand.remove(dominoNumber);
        pickableDomino = false;
        dominoRotate();
        shownDominos.get(0).getImageView().setRotate(shownDominos.get(0).getImageView().getRotate() + 180);
        shownDominos.get(0).getImageView().setRotate(shownDominos.get(0).getImageView().getRotate() + 90);
        shownDominos.get(shownDominos.size() -1).getImageView().setRotate(shownDominos.get(shownDominos.size() -1).getImageView().getRotate() + 90);
    }

    //Reverses a domino to change orientation, also rotates the image
    private void reverseDomino(Dominos d) {
        int temp;
        temp = d.getFirstSide();
        d.setFirstSide(d.getSecSide());
        d.setSecSide(temp);
        d.getImageView().setRotate(d.getImageView().getRotate()+180);
    }

    private void badMove(Dominos d){
        d.getImageView().setOnMouseClicked(badMoveClick);
    }

    private void firstSideMove() {
        shownDominos.add(playerHand.get(dominoNumber));
        playerHand.remove(dominoNumber);
        pickableDomino = false;
        dominoRotate();
    }

    private void secSideMove() {
        reverseDomino(playerHand.get(dominoNumber));
        shownDominos.add(playerHand.get(dominoNumber));
        playerHand.remove(dominoNumber);
        pickableDomino = false;
        dominoRotate();
    }
    private void secSideMoveBlank() {
        reverseDomino(playerHand.get(dominoNumber));
        shownDominos.add(playerHand.get(dominoNumber));
        playerHand.remove(dominoNumber);
        pickableDomino = false;
        dominoRotate();
        shownDominos.get(shownDominos.size()-1).getImageView().setRotate(shownDominos.get(shownDominos.size()-1).getImageView().getRotate()+180);
    }

    //Reset method


    public void start(Stage stage) {
        stage.setTitle("Dominos V1.0");
        stage.setScene(new Scene(defaultGame()));





        stage.show();
    }

    public void dominoRotate() {
        shownDominos.get(shownDominos.size()-1).getImageView().setRotate(shownDominos.get(shownDominos.size()-1).getImageView().getRotate()+90);
        shownDominos.get(shownDominos.size()-1).getImageView().setRotate(shownDominos.get(shownDominos.size()-1).getImageView().getRotate()+180);
    }

    private void isClickable(Dominos d) {
        d.getImageView().setOnMouseClicked(dominoOnMousePressedEventHandler);
    }

    EventHandler<MouseEvent> dominoOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    //ImageView temp = (ImageView)(t.getSource());
                    double temp = t.getSceneX();

                    System.out.println(t.getSceneX());
                    //It was the only way... I tried for hours
                    //on a method to interact with the object and figure out which object I am clicking on
                    //It looks ugly but it works
                    if(temp >= 300 && temp < 350){
                        dominoNumber = 0;
                    }
                    else if(temp >= 350 && temp < 400){
                        dominoNumber = 1;
                    }
                    else if(temp >= 400 && temp < 450){
                        dominoNumber = 2;
                    }
                    else if(temp >= 450 && temp < 500){
                        dominoNumber = 3;
                    }
                    else if(temp >= 500 && temp < 550){
                        dominoNumber = 4;
                    }
                    else if(temp >= 550 && temp < 600){
                        dominoNumber = 5;
                    }
                    else if(temp >= 600 && temp < 650){
                        dominoNumber = 6;
                    }
                    else if(temp >= 650 && temp < 700){
                        dominoNumber = 7;
                    }
                    else if(temp >= 700 && temp < 750){
                        dominoNumber = 8;
                    }
                    pickableDomino = true;
                    firstMove = true;



                    //System.out.println(temp.getFirstSide());
                    //System.out.println(temp.getSecSide());


                }
            };
    EventHandler<MouseEvent> boneYardClick =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    playerHand.add(gameDeck.get(gameDeck.size() -1));
                    gameDeck.remove(gameDeck.size() -1);

                }
            };
    EventHandler<MouseEvent> badMoveClick =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                alert.show();

                }
            };





}
