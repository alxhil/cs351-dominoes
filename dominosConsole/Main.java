package dominosConsole;

//Console Based

import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static String gameString = "";
    private static Boolean gameLoop = true;
    private static Boolean firstStartup = true;
    private static Boolean firstTurn = true;
    //Basically the boneyard
    private static LinkedList<Domino> dominoList = new LinkedList<>();
    //Dominos that are on the board
    private static LinkedList<Domino> gameBoard = new LinkedList<>();

    //player hand
    private static LinkedList<Domino> myHand = new LinkedList<>();
    //enemy hand
    private static LinkedList<Domino> enemyHand = new LinkedList<>();



    public static void main(String[] args) {
        createDominos();
        System.out.println(gameString);
        GameLoop();



    }


    private static void GameLoop() {
        while (gameLoop) {
            //First start up stuff
            if (firstStartup) {
                shuffleDeck();
                createHand(myHand);
                createHand(enemyHand);
                firstStartup = false;
            }

            System.out.println(readBoard(gameBoard)+"\n");
            System.out.println(readHand(myHand)+"\n");
            int gameInt = sc.nextInt();
            if(gameInt >= 0 && gameInt <= 9){
                if(gameInt == 0){
                    myHand.add(dominoList.get(dominoList.size() -1));
                    dominoList.remove(dominoList.size() -1);
                } else if (firstTurn) {
                    handToBoard(gameInt);
                    firstTurn = false;
                } else if (gameBoard.size() != 0){
                    if((myHand.get(gameInt-1).getNumOne() == gameBoard.get(gameBoard.size() -1).getNumTwo())) {
                        gameBoard.add(myHand.get(gameInt-1));
                        myHand.remove(gameInt-1);
                    } else if (myHand.get(gameInt-1).getNumTwo() == gameBoard.get(gameBoard.size() -1).getNumTwo()){
                        gameBoard.add(reverseDomino(myHand.get(gameInt-1)));
                        myHand.remove(gameInt-1);
                    } else if (myHand.get(gameInt-1).getNumTwo() == gameBoard.get(0).getNumOne()){
                        gameBoard.add(0, myHand.get(gameInt-1));
                        myHand.remove(gameInt-1);
                    } else if (myHand.get(gameInt-1).getNumOne() == gameBoard.get(0).getNumOne()){
                        gameBoard.add(0, reverseDomino(myHand.get(gameInt-1)));
                        myHand.remove(gameInt-1);
                    } else if (myHand.get(gameInt-1).getNumOne() == 0){
                        gameBoard.add(myHand.get(gameInt-1));
                        myHand.remove(gameInt-1);
                    } else if (myHand.get(gameInt-1).getNumTwo() == 0){
                        gameBoard.add(reverseDomino(myHand.get(gameInt-1)));
                        myHand.remove(gameInt-1);
                    } else if (gameBoard.get(gameBoard.size() -1).getNumTwo() == 0){
                        gameBoard.add(myHand.get(gameInt-1));
                        myHand.remove(gameInt-1);
                    } else if (gameBoard.get(0).getNumOne() == 0){
                        gameBoard.add(0, myHand.get(gameInt-1));
                        myHand.remove(gameInt-1);
                    } else {
                        System.out.println("Error");
                    }
                }
            } else {
                System.out.println("Something bad happened, try again");
            }
            enemyPlay();


        }
    }

    private static void enemyPlay() {
        int nothingHappened = 0;
        boolean doNothing = false;
        if(enemyHand.size() == 0 && dominoList.size() == 0){
            gameLoop = false;
            System.out.println("Computer won.");
        } else if (myHand.size() == 0 && dominoList.size() == 0){
            gameLoop = false;
            System.out.println("Human won.");
        }
        for(int i = 0; i < enemyHand.size(); i++){
            if(!doNothing) {
                if ((enemyHand.get(i).getNumOne() == gameBoard.get(gameBoard.size() - 1).getNumTwo())) {
                    gameBoard.add(enemyHand.get(i));
                    enemyHand.remove(i);
                    doNothing = true;
                } else if (enemyHand.get(i).getNumTwo() == gameBoard.get(gameBoard.size() - 1).getNumTwo()){
                    gameBoard.add(reverseDomino(enemyHand.get(i)));
                    enemyHand.remove(i);
                    doNothing = true;
                } else if (enemyHand.get(i).getNumOne() == gameBoard.get(0).getNumOne()) {
                    gameBoard.add(0, reverseDomino(enemyHand.get(i)));
                    enemyHand.remove(i);
                    doNothing = true;
                } else if (enemyHand.get(i).getNumTwo() == gameBoard.get(0).getNumOne()) {
                    gameBoard.add(0, enemyHand.get(i));
                    enemyHand.remove(i);
                    doNothing = true;
                } else if (gameBoard.get(0).getNumOne() == 0) {
                    gameBoard.add(0, enemyHand.get(i));
                    enemyHand.remove(i);
                    doNothing = true;
                } else if (gameBoard.get(0).getNumOne() == 0) {
                    gameBoard.add(reverseDomino(enemyHand.get(i)));
                    enemyHand.remove(i);
                    doNothing = true;
                } else if (gameBoard.get(gameBoard.size() - 1).getNumTwo() == 0) {
                    gameBoard.add(enemyHand.get(i));
                    enemyHand.remove(i);
                    doNothing = true;
                } else {
                    nothingHappened++;
                }
            }
        }
        if(nothingHappened == enemyHand.size()) {
            if(dominoList.size() > 0) {
                enemyHand.add(dominoList.get(dominoList.size() - 1));
                dominoList.remove(dominoList.size() - 1);
                enemyPlay();
            }else {
                gameLoop = false;
                System.out.println("Human player won. Computer ran out of moves");
            }
        }
    }


    //Creates dominos at start of game
    private static void createDominos() {
        for (int j = 6; j >= 0; j--) {

            for (int i = j; i >= 0; i--) {
                dominoList.add(new Domino(j, i));

            }
        }
    }


    //Creates hands for player and enemy player
    private static void createHand(LinkedList<Domino> dom) {
        for (int i = 0; i < 7; i++) {
            Domino temp = dominoList.get(dominoList.size() -1);
            dominoList.remove(dominoList.size() -1);
            dom.add(temp);
        }
    }

    //Shuffles dominos at start of game
    private static void shuffleDeck() {
        Collections.shuffle(dominoList);
    }

    private static Domino reverseDomino (Domino d) {
        int temp;
        temp = d.getNumOne();
        d.setNumOne(d.getNumTwo());
        d.setNumTwo(temp);
        return d;
    }

    //Moves dominos from your hand to the board if the options are viable.
    //Also hands boneyard logic
    private static void handToBoard(int place) {
        //Boneyard placing
        if(place == 0 && dominoList.size() > 0){
            myHand.add(dominoList.get(dominoList.size() -1));
            dominoList.remove(dominoList.size() -1);
        } else if(place > 0){
            gameBoard.add(myHand.get(place - 1));
            myHand.remove(place - 1);
        } else if (place == 0 && dominoList.size() == 0){
            gameLoop = false;
            System.out.println("Mission Failed");
        } else{
            System.out.println("Something weird happened");
        }

    }



    //Returns string for dominos in your hand
    private static String readHand(LinkedList<Domino> hand) {
        String handString = "";
        for (int i = 0; i < hand.size(); i++) {
            handString += " "+ (i+1) +") " + hand.get(i).getNumOne() + ":" + hand.get(i).getNumTwo() + "";
        }
        handString += "\n Press 0 to pick from Boneyard\n"+"Boneyard has "+dominoList.size()+" pieces in it";
        return handString;
    }

    //Returns string for dominos shown on screen
    private static String readBoard(LinkedList<Domino> board){
        String boardString = "Game Board: ";
        if(board.size() == 0){
            return boardString;
        }
        for(int i = 0; i < board.size(); i++){
            boardString += ""+board.get(i).getNumOne()+":"+board.get(i).getNumTwo()+" ";
        }

        return boardString;
    }


}
