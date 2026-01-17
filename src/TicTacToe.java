import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;

public class TicTacToe extends Application {
    public static final String[][] Board = new String[3][3];
    public static final String PlayerO = "O";
    public static final String PlayerX = "X";
    public static final String selected = "S";
    public static int Count = 1;
    public static int CountX = 0;
    public static int CountO = 0;
    public static int OldR = 0;
    public static int OldC = 0;
    public boolean FirstClick = true;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(600,600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.PINK);
        gc.fillRect(0,0,600,600);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(0,600,0,0);
        gc.strokeLine(600,0,0,0);
        gc.strokeLine(0,200,600,200);
        gc.strokeLine(200,0,200,600);
        gc.strokeLine(400,0,400,600);
        gc.strokeLine(400,0,400,600);
        gc.strokeLine(0,400,600,400);
        for(int r=0;r<Board.length;r++){
            for (int c=0;c<Board.length;c++) Board[r][c] = " ";}

        canvas.setOnMouseClicked( MouseEvent  -> {

            double x = MouseEvent.getX();
            double y = MouseEvent.getY();
            int r=0;
            int c=0;
            if (x < 200 && y < 200) {
                r = 0;
                c = 0;
            } else if (x > 200 && x < 400 && y < 200) {
                r = 0;
                c = 1;
            } else if (x > 400 && x < 600 && y < 200) {
                r = 0;
                c = 2;
            } else if (x < 200 && y > 200 && y < 400) {
                r = 1;
                c = 0;
            } else if (x > 200 && x < 400 && y > 200 && y < 400) {
                r = 1;
                c = 1;
            } else if (x > 400 && x < 600 && y > 200 && y < 400) {
                r = 1;
                c = 2;
            } else if (x < 200 && y > 400 && y < 600) {
                r = 2;
                c = 0;
            } else if (x > 200 && x < 400 && y > 400 && y < 600) {
                r = 2;
                c = 1;
            } else if (x > 400 && x < 600 && y > 400 && y < 600) {
                r = 2;
                c = 2;
            } if(Board[r][c].equals(PlayerX)&&Count%2==0 || Board[r][c].equals(PlayerO)&&Count%2!=0){return;} else {
                if (CountX <=2|| CountO<=2){
                    makeMove(gc,r,c,Count);
                    if (Count%2!=0){
                        CountX++;} else {CountO++;}
                } else  {
                    switch (Board[r][c]) {
                        case " " -> {
                            if (FirstClick) {
                                if (Count%2!=0){
                                    for (int a = 0; a < Board.length; a++) {
                                        for (int b = 0; b < Board.length; b++) {
                                            if (Board[a][b].equals(" ") || Board[a][b].equals(PlayerO)) {
                                                paintRed(gc, a, b);
                                            } else paintGreen(gc, a, b);
                                        }
                                    }} else { for (int a = 0; a < Board.length; a++) {
                                    for (int b = 0; b < Board.length; b++) {
                                        if (Board[a][b].equals(" ") || Board[a][b].equals(PlayerX)) {
                                            paintRed(gc, a, b);
                                        } else paintGreen(gc, a, b);
                                    }
                                }

                                }
                                FirstClick = false;
                            } else {
                                clear(gc, OldR, OldC);
                                Board[OldR][OldC] = " ";
                                if (Count % 2 != 0) {
                                    Board[r][c] = PlayerX;
                                } else Board[r][c] = PlayerO;
                                drawNew(gc, r, c);
                                FirstClick = true;
                                for (int a = 0; a < Board.length; a++) {
                                    for (int b = 0; b < Board.length; b++) {
                                        clear(gc, a, b);
                                        drawNew(gc, a, b);
                                    }
                                }
                                if (Count % 2 != 0) {
                                    if (checkWon(Board)){
                                        wonX(gc);
                                    }
                                } else {
                                    if (checkWon(Board)){
                                        wonO(gc);
                                    }
                                }
                                Count++;
                            }
                        }
                        case PlayerX, PlayerO -> {
                            Board[r][c] = selected;
                            OldR = r;
                            OldC = c;
                            FirstClick = false;
                            for (int a = 0; a < Board.length; a++) {
                                for (int b = 0; b < Board.length; b++) {
                                    if (Board[a][b].equals(" ")) {
                                        clear(gc, a, b);
                                    }
                                }
                            }
                            for (int a = 0; a < Board.length; a++) {
                                for (int b = 0; b < Board.length; b++) {
                                    if (Board[a][b].equals(" ")) {
                                        paintGreen(gc, a, b);
                                    }
                                    if (Board[a][b].equals(PlayerX) || Board[a][b].equals(PlayerO)) {
                                        clear(gc, a, b);
                                        drawNew(gc, a, b);
                                        paintRed(gc, a, b);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        Button loadbtn = new Button("Load");
        loadbtn.setPrefSize(150,75);
        loadbtn.setStyle("-fx-background-color: #FFB74D; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button savebtn = new Button("Save");
        savebtn.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-pref-width: 140px;-fx-pref-height: 45px;-fx-background-radius:10px;");

        Button end = new Button("Exit");
        end.setOnAction(e->{
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
            exitAlert.setTitle("Exit Game");
            exitAlert.setHeaderText("Exit");
            exitAlert.setContentText("Are you sure you want to exit?. \nClick Ok to Exit?");
            Optional<ButtonType> result = exitAlert.showAndWait();
            if (result.isPresent()&&result.get()==ButtonType.OK){
                Platform.exit();} else return;
        });
        end.setPrefSize(150,75);
        end.setStyle("-fx-background-color: #D64545; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        Button restart = new Button("Restart");
        restart.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-pref-width: 140px;-fx-pref-height: 45px;-fx-background-radius:10px;");
        restart.setOnAction(e->{
            doRestart(gc);
        });
        Button mainMenu = new Button("Main Menu");
        mainMenu.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-pref-width: 140px;-fx-pref-height: 45px;-fx-background-radius:10px;");
        Button pause = new Button("Pause");
        pause.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-pref-width: 140px;-fx-pref-height: 45px;-fx-background-radius:10px;");
        pause.setOnAction(e->{
            Alert pauseAlert = new Alert(Alert.AlertType.CONFIRMATION);
            pauseAlert.setTitle("Game Paused");
            pauseAlert.setHeaderText("Paused");
            pauseAlert.setContentText("The game is paused. \nClick Ok to continue.");
            pauseAlert.showAndWait();
        });
        Button start = new Button("Start Game");
        start.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        start.setPrefSize(150,75);
        start.setOnAction(e->{
            primaryStage.close();
            Stage game = new Stage();
            VBox side = new VBox(pause,savebtn,restart,mainMenu);
            side.setAlignment(Pos.CENTER);
            side.setSpacing(70);
            BorderPane root = new BorderPane();
            root.setCenter(canvas);
            root.setRight(side);
            Scene scene = new Scene(root);
            game.setScene(scene);
            game.setTitle("TicTacToe");
            game.show();
            doRestart(gc);
            savebtn.setOnAction(g->{
                save(Board,Count,game);
            });



            mainMenu.setOnAction(f->{

                VBox box2 = new VBox(start,loadbtn,end);
                box2.setBackground(new Background(new BackgroundFill(
                        Color.web("#F2F6FC"),CornerRadii.EMPTY, Insets.EMPTY
                )));
                box2.setAlignment(Pos.CENTER);
                box2.setSpacing(30);
                box2.setPrefSize(800,600);
                box2.setMaxSize(800,600);
                Scene main = new Scene(box2);
                primaryStage.setScene(main);
                primaryStage.setTitle("TicTacToe");
                primaryStage.show();
                game.close();
            });

        });

        loadbtn.setOnAction(e->{
            primaryStage.close();
            Stage game = new Stage();
            VBox side = new VBox(pause,savebtn,restart,mainMenu);
            side.setAlignment(Pos.CENTER);
            side.setSpacing(70);
            BorderPane root = new BorderPane();
            root.setCenter(canvas);
            root.setRight(side);
            Scene scene = new Scene(root);
            game.setScene(scene);
            game.setTitle("TicTacToe");
            game.show();
            doRestart(gc);
            load(game);
            for (int row = 0;row<Board.length;row++){
                for (int col = 0;col<Board.length;col++){
                    drawNew(gc,row,col);
                }
            }

            savebtn.setOnAction(g->{
                save(Board,Count,game);
            });



            mainMenu.setOnAction(f->{

                VBox box2 = new VBox(start,loadbtn,end);
                box2.setBackground(new Background(new BackgroundFill(
                        Color.web("#F2F6FC"),CornerRadii.EMPTY, Insets.EMPTY
                )));
                box2.setAlignment(Pos.CENTER);
                box2.setSpacing(30);
                box2.setPrefSize(800,600);
                box2.setMaxSize(800,600);
                Scene main = new Scene(box2);
                primaryStage.setScene(main);
                primaryStage.setTitle("TicTacToe");
                primaryStage.show();
                game.close();
            });
        });


            VBox box = new VBox(start,loadbtn,end);
            box.setBackground(new Background(new BackgroundFill(
                    Color.web("#F2F6FC"),CornerRadii.EMPTY, Insets.EMPTY
            )));
            box.setAlignment(Pos.CENTER);
            box.setSpacing(30);
            box.setPrefSize(800,600);
            box.setMaxSize(800,600);

            Scene scene = new Scene(box);
            primaryStage.setScene(scene);
            primaryStage.setTitle("TicTacToe");
            primaryStage.show(); }

         //isLegal, to check wether the move is legal//
    public static boolean isLegal(int r,int c){

        return Board[r][c].equals(" ");
    }
    public static void makeMove(GraphicsContext g, int r,int c,int count){
        if (isLegal(r, c)){
            if (r==0&&c==0) {
                drawR1C1(g,count);
            } else if (r==0&&c==1){
                drawR1C2(g,count);
            } else if (r==0&&c==2) {
                drawR1C3(g,count);
            } else if (r==1&&c==0) {
                drawR2C1(g,count);
            } else if (r==1&&c==1) {
                drawR2C2(g,count);
            } else if (r==1&&c==2) {
                drawR2C3(g,count);
            } else if (r==2&&c==0) {
                drawR3C1(g,count);
            } else if (r==2&&c==1) {
                drawR3C2(g,count);
            } else if (r==2&&c==2) {
                drawR3C3(g, count);
            }
            Count++;
        }}
    public static void drawNew(GraphicsContext g, int r,int c){
            if(r==0&&c==0){ if (Board[r][c].equals(PlayerX)){
                g.setStroke(Color.BLUE);
                g.strokeLine(10,10,200-10,200-10);
                g.strokeLine(200-10,10,10,200-10);
                Board[0][0] = PlayerX;
               } else if (Board[r][c].equals(PlayerO)) {g.setStroke(Color.PURPLE);
                g.strokeOval(10,10,200-20,200-20);
                Board[0][0] = PlayerO;}}
            else if (r==0&&c==1){ if (Board[r][c].equals(PlayerX)) {g.setStroke(Color.BLUE);
                g.strokeLine(200+10,10,400-10,200-10);
                g.strokeLine(400-10,10,200+10,200-10);
                Board[0][1] = PlayerX; } else if (Board[r][c].equals(PlayerO)) {g.setStroke(Color.PURPLE); g.strokeOval(200+10,10,200-20,200-20);
                Board[0][1] = PlayerO;}}
            else if (r==0&&c==2) { if (Board[r][c].equals(PlayerX)) {g.setStroke(Color.BLUE);
                g.strokeLine(400+10,10,600-10,200-10);
                g.strokeLine(600-10,10,400+10,200-10);
                Board[0][2] = PlayerX;} else if (Board[r][c].equals(PlayerO)) {g.setStroke(Color.PURPLE); g.strokeOval(400+10,10,200-20,200-20);
                Board[0][2] = PlayerO;}}
            else if (r==1&&c==0) { if(Board[r][c].equals(PlayerX)) {g.setStroke(Color.BLUE);
                g.strokeLine(10,200+10,200-10,400-10);
                g.strokeLine(200-10,200+10,10,400-10);
                Board[1][0] = PlayerX;} else if (Board[r][c].equals(PlayerO)) {g.setStroke(Color.PURPLE);g.strokeOval(10,200+10,200-20,200-20);
                Board[1][0] = PlayerO;}}
            else if (r==1&&c==1) { if(Board[r][c].equals(PlayerX)) {g.setStroke(Color.BLUE);
                g.strokeLine(200+10,200+10,400-10,400-10);
                g.strokeLine(200+10,400-10,400-10,200+10);
                Board[1][1] = PlayerX;} else if (Board[r][c].equals(PlayerO)) {g.setStroke(Color.PURPLE);g.strokeOval(200+10,200+10,200-20,200-20);
                Board[1][1] = PlayerO;}}
            else if (r==1&&c==2) { if(Board[r][c].equals(PlayerX)) {g.setStroke(Color.BLUE);
                g.strokeLine(400+10,200+10,600-10,400-10);
                g.strokeLine(600-10,200+10,400+10,400-10);
                Board[1][2] = PlayerX;} else if (Board[r][c].equals(PlayerO)) {g.setStroke(Color.PURPLE);g.strokeOval(400+10,200+10,200-20,200-20);
                Board[1][2] = PlayerO;}}
            else if (r==2&&c==0) { if(Board[r][c].equals(PlayerX)) {g.setStroke(Color.BLUE);
                g.strokeLine(10,400+10,200-10,600-10);
                g.strokeLine(200-10,400+10,10,600-10);
                Board[2][0] = PlayerX;} else if (Board[r][c].equals(PlayerO)) {g.setStroke(Color.PURPLE);g.strokeOval(10,400+10,200-20,200-20);
                Board[2][0] = PlayerO;}}
            else if (r==2&&c==1) { if(Board[r][c].equals(PlayerX)) {g.setStroke(Color.BLUE);
                g.strokeLine(200+10,400+10,400-10,600-10);
                g.strokeLine(400-10,400+10,200+10,600-10);
                Board[2][1] = PlayerX;} else if (Board[r][c].equals(PlayerO)) {g.setStroke(Color.PURPLE);g.strokeOval(200+10,400+10,200-20,200-20);
                Board[2][1] = PlayerO;}}
            else if (r==2&&c==2) { if(Board[r][c].equals(PlayerX)) {g.setStroke(Color.BLUE);
                g.strokeLine(400+10,400+10,600-10,600-10);
                g.strokeLine(600-10,400+10,400+10,600-10);
                Board[2][2] = PlayerX;} else if (Board[r][c].equals(PlayerO)) {g.setStroke(Color.PURPLE);g.strokeOval(400+10,400+10,200-20,200-20);
                Board[2][2] = PlayerO;} }}

    public static void drawR1C1(GraphicsContext g, int count){
        if(count%2!=0){
        g.setStroke(Color.BLUE);
        g.strokeLine(10,10,200-10,200-10);
        g.strokeLine(200-10,10,10,200-10);
            Board[0][0] = PlayerX;
        if (checkWon(Board)){
            wonX(g);
        }
        }
        else {g.setStroke(Color.PURPLE);g.strokeOval(10,10,200-20,200-20);
        Board[0][0] = PlayerO;
            if (checkWon(Board)){
               wonO(g);
            }}

    }
    public static void drawR1C2(GraphicsContext g, int count){
        if (count%2!=0){
        g.setStroke(Color.BLUE);
        g.strokeLine(200+10,10,400-10,200-10);
        g.strokeLine(400-10,10,200+10,200-10);
        Board[0][1] = PlayerX; if (checkWon(Board)){
                wonX(g);
            }
        }
        else {g.setStroke(Color.PURPLE); g.strokeOval(200+10,10,200-20,200-20);
        Board[0][1] = PlayerO;
            if (checkWon(Board)){
                wonO(g);
            }}
    }
    public static void drawR1C3(GraphicsContext g,int count){
        if (count%2!=0){
        g.setStroke(Color.BLUE);
        g.strokeLine(400+10,10,600-10,200-10);
        g.strokeLine(600-10,10,400+10,200-10);
            Board[0][2] = PlayerX;
            if (checkWon(Board)){
                wonX(g);
            }}
        else  {g.setStroke(Color.PURPLE);g.strokeOval(400+10,10,200-20,200-20);
            Board[0][2] = PlayerO;
            if (checkWon(Board)){
                wonO(g);
            }}
    }
    public static void drawR2C1(GraphicsContext g,int count){
        if (count%2!=0){
        g.setStroke(Color.BLUE);
        g.strokeLine(10,200+10,200-10,400-10);
        g.strokeLine(200-10,200+10,10,400-10);
            Board[1][0] = PlayerX; if (checkWon(Board)){
                wonX(g);
            }}
        else {g.setStroke(Color.PURPLE);g.strokeOval(10,200+10,200-20,200-20);
            Board[1][0] = PlayerO;
            if (checkWon(Board)){
                wonO(g);
            }}
    }
    public static void drawR2C2(GraphicsContext g,int count){
        if (count%2!=0){
        g.setStroke(Color.BLUE);
        g.strokeLine(200+10,200+10,400-10,400-10);
        g.strokeLine(200+10,400-10,400-10,200+10);
            Board[1][1] = PlayerX;
            if (checkWon(Board)){
                wonX(g);
            }}
        else {g.setStroke(Color.PURPLE);g.strokeOval(200+10,200+10,200-20,200-20);
            Board[1][1] = PlayerO;
            if (checkWon(Board)){
                wonO(g);
            }}
    }
    public static void drawR2C3(GraphicsContext g,int count){
        if (count%2!=0){
        g.setStroke(Color.BLUE);
        g.strokeLine(400+10,200+10,600-10,400-10);
        g.strokeLine(600-10,200+10,400+10,400-10);
            Board[1][2] = PlayerX;
            if (checkWon(Board)){
                wonX(g);
            }}
        else {g.setStroke(Color.PURPLE);g.strokeOval(400+10,200+10,200-20,200-20);
            Board[1][2] = PlayerO;
            if (checkWon(Board)){
                wonO(g);
            }}
    }
    public static void drawR3C1(GraphicsContext g, int count){
        if (count%2!=0){
        g.setStroke(Color.BLUE);
        g.strokeLine(10,400+10,200-10,600-10);
        g.strokeLine(200-10,400+10,10,600-10);
            Board[2][0] = PlayerX;
            if (checkWon(Board)){
                wonX(g);
            }}
        else {g.setStroke(Color.PURPLE);g.strokeOval(10,400+10,200-20,200-20);
        Board[2][0] = PlayerO;
            if (checkWon(Board)){
                wonO(g);
            }}
    }
    public static void drawR3C2(GraphicsContext g,int count){
        if (count%2!=0){
        g.setStroke(Color.BLUE);
        g.strokeLine(200+10,400+10,400-10,600-10);
        g.strokeLine(400-10,400+10,200+10,600-10);
            Board[2][1] = PlayerX;
            if (checkWon(Board)){
                wonX(g);
            }}
        else {g.setStroke(Color.PURPLE);g.strokeOval(200+10,400+10,200-20,200-20);
            Board[2][1] = PlayerO;
            if (checkWon(Board)){
                wonO(g);
            }}
    }
    public static void drawR3C3(GraphicsContext g,int count){
        if (count%2!=0){
        g.setStroke(Color.BLUE);
        g.strokeLine(400+10,400+10,600-10,600-10);
        g.strokeLine(600-10,400+10,400+10,600-10);
            Board[2][2] = PlayerX;
            if (checkWon(Board)){
                wonX(g);
            }}
        else {g.setStroke(Color.PURPLE);g.strokeOval(400+10,400+10,200-20,200-20);
            Board[2][2] = PlayerO;
            if (checkWon(Board)){
                wonO(g);
            }}
    }
    public static boolean checkWon(String[][] board) {
        if (board[0][0].equals(PlayerO) && board[0][1].equals(PlayerO) && board[0][2].equals(PlayerO) || board[0][0].equals(PlayerX) && board[0][1].equals(PlayerX) && board[0][2].equals(PlayerX)) {
            return true;
        } else if (board[1][0].equals(PlayerO) && board[1][1].equals(PlayerO) && board[1][2].equals(PlayerO) || board[1][0].equals(PlayerX) && board[1][1].equals(PlayerX) && board[1][2].equals(PlayerX)) {
            return true;} else if (board[2][0].equals(PlayerO) && board[2][1].equals(PlayerO) && board[2][2].equals(PlayerO) || board[2][0].equals(PlayerX) && board[2][1].equals(PlayerX) && board[2][2].equals(PlayerX)) {
            return true;}  else if (board[0][0].equals(PlayerO) && board[1][0].equals(PlayerO) && board[2][0].equals(PlayerO) || board[0][0].equals(PlayerX) && board[1][0].equals(PlayerX) && board[2][0].equals(PlayerX)) {
            return true;} else if (board[0][1].equals(PlayerO) && board[1][1].equals(PlayerO) && board[2][1].equals(PlayerO) || board[0][1].equals(PlayerX) && board[1][1].equals(PlayerX) && board[2][1].equals(PlayerX)) {
            return true;} else if (board[0][2].equals(PlayerO) && board[1][2].equals(PlayerO) && board[2][2].equals(PlayerO) || board[0][2].equals(PlayerX) && board[1][2].equals(PlayerX) && board[2][2].equals(PlayerX)) {
            return true;} else if (board[0][0].equals(PlayerO) && board[1][1].equals(PlayerO) && board[2][2].equals(PlayerO) || board[0][0].equals(PlayerX) && board[1][1].equals(PlayerX) && board[2][2].equals(PlayerX)) {
            return true;}else return board[2][0].equals(PlayerO) && board[1][1].equals(PlayerO) && board[0][2].equals(PlayerO) || board[2][0].equals(PlayerX) && board[1][1].equals(PlayerX) && board[0][2].equals(PlayerX);
    }
    public static void wonX(GraphicsContext gc){
            Alert alert = new Alert(Alert.AlertType.NONE);
            ButtonType restartGame = new ButtonType("Restart");
            ButtonType endGame = new ButtonType("End");
            alert.getButtonTypes().addAll(restartGame,endGame);
            alert.setTitle("Game Over");
            alert.setContentText("PlayerX Wins");
            alert.showAndWait().ifPresent(result -> {
                if (result==endGame){
                    Platform.exit();
                } else if (result == restartGame) {
                    doRestart(gc);
                }
            });

    }
    public static void wonO( GraphicsContext gc){
        Alert alert = new Alert(Alert.AlertType.NONE);
        ButtonType restartGame = new ButtonType("Restart");
        ButtonType endGame = new ButtonType("End");
        alert.getButtonTypes().addAll(restartGame,endGame);
        alert.setTitle("Game Over");
        alert.setContentText("PlayerO Wins");
        alert.showAndWait().ifPresent(result -> {
            if (result==endGame){
                Platform.exit();
            } else if (result == restartGame) {
                doRestart(gc);
            }
        });

    }
    public static void doRestart(GraphicsContext gc){
        Count = 0;
        CountO = 0;
        CountX = 0;
        gc.setFill(Color.PINK);
        gc.fillRect(0,0,600,600);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(0,600,0,0);
        gc.strokeLine(600,0,0,0);
        gc.strokeLine(0,200,600,200);
        gc.strokeLine(200,0,200,600);
        gc.strokeLine(400,0,400,600);
        gc.strokeLine(400,0,400,600);
        gc.strokeLine(0,400,600,400);
        for(int r=0;r<Board.length;r++){
            for (int c=0;c<Board.length;c++) Board[r][c] = " ";
        }
    }
    public static void paintGreen(GraphicsContext gc,int row,int col){
        gc.setFill(new Color(0.0,1.0,0.4,0.35));
        if (row==0&&col==0) { gc.fillRect(7,7,190,190);}
        else if (row==0&&col==1) { gc.fillRect(207,7,190,190);}
        else if (row==0&&col==2) { gc.fillRect(407,7,190,190);}
        else if (row==1&&col==0) { gc.fillRect(7,207,190,190);}
        else if (row==1&&col==1) { gc.fillRect(207,207,190,190);}
        else if (row==1&&col==2) { gc.fillRect(407,207,190,190);}
        else if (row==2&&col==0) { gc.fillRect(7,407,190,190);}
        else if (row==2&&col==1) { gc.fillRect(207,407,190,190);}
        else if (row==2&&col==2) { gc.fillRect(407,407,190,190);}}

    public static void paintRed(GraphicsContext gc,int row,int col){
        gc.setFill(new Color(1.0,0.1,0.1,0.35));
        if (row==0&&col==0) { gc.fillRect(7,7,190,190);}
        else if (row==0&&col==1) { gc.fillRect(207,7,190,190);}
        else if (row==0&&col==2) { gc.fillRect(407,7,190,190);}
        else if (row==1&&col==0) { gc.fillRect(7,207,190,190);}
        else if (row==1&&col==1) { gc.fillRect(207,207,190,190);}
        else if (row==1&&col==2) { gc.fillRect(407,207,190,190);}
        else if (row==2&&col==0) { gc.fillRect(7,407,190,190);}
        else if (row==2&&col==1) { gc.fillRect(207,407,190,190);}
        else if (row==2&&col==2) { gc.fillRect(407,407,190,190);}
    }
    public static void clear(GraphicsContext gc,int row,int col){
        gc.setFill(Color.PINK);
        if (row==0&&col==0) { gc.fillRect(7,7,190,190);}
        else if (row==0&&col==1) { gc.fillRect(207,7,190,190);}
        else if (row==0&&col==2) { gc.fillRect(407,7,190,190);}
        else if (row==1&&col==0) { gc.fillRect(7,207,190,190);}
        else if (row==1&&col==1) { gc.fillRect(207,207,190,190);}
        else if (row==1&&col==2) { gc.fillRect(407,207,190,190);}
        else if (row==2&&col==0) { gc.fillRect(7,407,190,190);}
        else if (row==2&&col==1) { gc.fillRect(207,407,190,190);}
        else if (row==2&&col==2) { gc.fillRect(407,407,190,190);}
    }
    public static void save(String[][] board,int count,Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file!=null){
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Turn:" + Count + "\n");
                writer.write("CountX:" + CountX + "\n");
                writer.write("CountO:" + CountO + "\n");
                writer.write("Board:\n");
                for (int r=0;r<Board.length;r++){
                    for (int c=0;c<Board.length;c++){
                        writer.write(Board[r][c]);
                        if (c<2){
                            writer.write(",");}
                    }
                    writer.write("\n");
                }
            } catch (IOException e){
                e.printStackTrace();}
        }
    }
    public static void load(Stage stage){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");

        File file= fileChooser.showOpenDialog(stage);
        if (file!=null) {
            String line;
            boolean readingBoard = false;
            int row = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Turn:")){
                        String value =line.split(":")[1];
                        Count = Integer.parseInt(value);
                    } else if (line.startsWith("CountX:")) {
                        String value =line.split(":")[1];
                        CountX = Integer.parseInt(value);
                    } else if (line.startsWith("CountO:")) {
                        String value =line.split(":")[1];
                        CountO = Integer.parseInt(value);
                    }
                    else if (line.equals("Board:")) {
                        readingBoard=true;
                    } else if (readingBoard && row<3) {
                        String[] values = line.split(",");
                        for (int col = 0;col<3;col++){
                            Board[row][col]=values[col].trim();
                            if (Board[row][col].isEmpty()) Board[row][col] = " ";
                        }
                        row++;
                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
