
import org.w3c.dom.ls.LSOutput;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    static Scanner sc = new Scanner(System.in);
    static Tile [] gameBoard;
    static int playerTurn = 1; // -1 for human, 1 for computer
    private static Random random = new Random();

    static HashMap<String, Integer> scores = new HashMap<>();






    public static void displayBoard()
    {


        for(int i = 0; i < gameBoard.length; i++)
        {
            if (i == 0 || i == 3 || i == 6)
            System.out.print("\t\t\t\t\t\t\t\t" + gameBoard[i].getState() + "   " + "|");

            else if (i == 1 || i == 4 || i == 7)
                System.out.print("    " + gameBoard[i].getState() + "    " + "|");
            else
                System.out.print("    " + gameBoard[i].getState());


            if( (i + 1) % 3 == 0)
            {
                System.out.print("\n");
                if(i != 8)
                System.out.println("\t\t\t\t\t\t\t----------------------------");
            }



        }
    }




    // ths method initializes the board
    public static void initBoard ()
    {
        gameBoard = new Tile [9];

        for(int i = 0; i < gameBoard.length; i++)
        {
            gameBoard[i] = new Tile();


        }


    }



    // this method resets the board

    public static void resetBoard()
    {

        for (Tile tile : gameBoard) {
            tile.setState(' ');
        }

    }



    public static void updateBoard(int move)
    {
        // if human's turn
        if(playerTurn == -1)
        {
            gameBoard[move].setState('O');
        }
        // if computer's turn
        else if (playerTurn == 1)
        {
            gameBoard[move].setState('X');
        }
        else
        {
            gameBoard[move].setState(' ');
            System.out.println("this should never print");
        }
    }


    // this method returns the marker of the winner (if there is already), "TIE" if its a tie , returns "-" if game is not yet over

    public static String checkBoard( )
    {

        // checks if theres a winner already
        for(int i = 0; i < 8; i++)
        {
            String line = switch (i) {
                case 0 -> String.valueOf(gameBoard[0].getState()) + gameBoard[3].getState() + gameBoard[6].getState();
                case 1 -> String.valueOf(gameBoard[1].getState()) + gameBoard[4].getState() + gameBoard[7].getState();
                case 2 -> String.valueOf(gameBoard[2].getState()) + gameBoard[5].getState() + gameBoard[8].getState();
                case 3 -> String.valueOf(gameBoard[0].getState()) + gameBoard[1].getState() + gameBoard[2].getState();
                case 4 -> String.valueOf(gameBoard[3].getState()) + gameBoard[4].getState() + gameBoard[5].getState();
                case 5 -> String.valueOf(gameBoard[6].getState()) + gameBoard[7].getState() + gameBoard[8].getState();
                case 6 -> String.valueOf(gameBoard[0].getState()) + gameBoard[4].getState() + gameBoard[8].getState();
                case 7 -> String.valueOf(gameBoard[2].getState()) + gameBoard[4].getState() + gameBoard[6].getState();
                default -> null;
            };

            if(line.equals("XXX"))
                return "X";

            if(line.equals("OOO"))
                return "O";



        }

        // if all boards are filled and no one won
        if(gameOver())
        {
            return "TIE";
        }

        // game not yet done
        return "-";



    }

    // this method returns true if game is over, false otherwise
    public static boolean gameOver()
    {
        int count = 0;

        for(int j = 0; j < 9; j++)
        {
            if(gameBoard[j].getState() != ' ')
            {
                count += 1;
            }

        }

        // if all boards are filled
        return count == 9;
    }



    // this method returns a random valid move
    public static int computerTurnRandom()
    {
        int min = 0;
        int max = 8;
        int num;


        do{
            num = random.nextInt(max - min + 1) + min;

        } while(gameBoard[num].getState() != ' ');

        return num;
    }

    // returns a hard coded move
    /*
    public static int computerTurnHuman()
    {


        // look for the O's

        // check first the position of o where theres many potential

        for(int i = 0; i < gameBoard.length; i++)
        {

            // if spot is available
            if(gameBoard[i].getState() == ' ')
            {

            }

            // check o's position diagonally, horizontally, and vertically
            // diagonal - 4 , vertical - 3, horizontal - 1

            // check if negative, then out of bounds


        }





    }


    */

    // computers turn AI
    public static int computerTurnMinimax()
    {
        // initial best score
        int bestScore = Integer.MIN_VALUE;
        int move = -1;

        /*

        X : +10  --- computer
        O : -10  --- human
        TIE: 0

        */

        for(int i = 0; i < gameBoard.length; i++)
        {
            // if tile is available
            if(gameBoard[i].getState() == ' ')
            {
                // depends on player move
               // playerTurn = 1;
                updateBoard(i);
                int score = miniMax(0, false);
                gameBoard[i].setState(' ');

                if(score > bestScore)
                {
                    bestScore = score;
                    move = i;

                }




            }

        }

        playerTurn = 1;
        return move;

    }


    public static int miniMax(int depth, boolean isMaximizing)
    {

        // check if theres winner
        String verdict = checkBoard();
        int result = checkWinner(verdict);

        // terminating statement
        if(result != -1)
        {
            if(result == 2)
                return scores.get("X");
            else if(result == 1)
                return scores.get("O");
            else
                return scores.get("TIE");
        }

        int bestScore;
        if(isMaximizing)
        {
            bestScore = Integer.MIN_VALUE;
            for(int i = 0; i < gameBoard.length; i++)
            {
                if(gameBoard[i].getState() == ' ')
                {
                    playerTurn = 1;
                    updateBoard(i);
                    int score = miniMax(depth + 1, false);
                    gameBoard[i].setState(' ');
                    bestScore = Math.max(score, bestScore);

                }
            }

        }
        else
        {
            bestScore = Integer.MAX_VALUE;
            for(int i = 0; i < gameBoard.length; i++)
            {
                if(gameBoard[i].getState() == ' ')
                {
                    // change player turn
                    playerTurn = -1;
                    updateBoard(i);
                    int score = miniMax(depth + 1, true);
                    gameBoard[i].setState(' ');

                    bestScore = Math.min(score, bestScore);



                }
            }


        }
        return bestScore;



/*
        int bestScore;
        boolean maximizing;


        if(isMaximizing)
        {
            bestScore = Integer.MIN_VALUE;
            maximizing = false;
            playerTurn = 1;
        }
        else
        {
            bestScore = Integer.MAX_VALUE;
            maximizing = true;
            playerTurn = -1;
        }

        for(int i = 0; i < gameBoard.length; i++)
        {
            if(gameBoard[i].getState() == '-')
            {
                updateBoard(i);
                int score = miniMax(depth + 1, maximizing);
                gameBoard[i].setState('-');

                if(isMaximizing)
                    bestScore = Math.max(score, bestScore);
                else
                    bestScore = Math.min(score, bestScore);

            }
        }

        return bestScore;



*/

























    }


    // winner message
    public static int checkWinner(String verdict)
    {

        switch (verdict) {
            case "X" -> {
                //System.out.println("Computer Wins!");
                return 2;
            }
            case "O" -> {
               // System.out.println("You Win!");
                return 1;
            }
            case "TIE" -> {
               // System.out.println("It's a tie!");
                return 0;
            }
        }
        return -1;

    }



    // stupid computer
    public static void startGame(int level)
    {
       // System.out.println("\t\t\t\t\t\t\tLevel Zero");

         // playerTurn  -1 for human, 1 for computer

        // COMPUTER or HUMAN
        String firstPlayer = "COMPUTER";


        int choice;

        do {
            int move = -1;
         //   System.out.println("\n\t\t\t\t\t\t\tSTART GAME");



            if(firstPlayer.equals("COMPUTER"))
            {
                playerTurn = 1;
            }
            else {
                displayBoard();
                playerTurn = -1;
            }


            while (!gameOver()) {

                if (playerTurn == 1) // computer's turn
                {
                    System.out.println("\n\t\t\t\t\t\t\tCOMPUTER'S MOVE: \n");

                    // stupid
                    if(level == 0)
                    {
                         move = computerTurnRandom();

                    }
                    // smart-ish
                    else if(level == 1)
                    {
                        System.out.println("level 1");
                        //move = computerTurnHuman();

                    }
                    // smart
                    else if(level == 2)
                    {
                        move = computerTurnMinimax();

                    }

                }
                else // human's turn
                {
                    System.out.println("\n\t\t\t\t\t\t\tYOUR TURN:");
                    System.out.print("\n\t\t\t\t\t\t\tEnter move: ");
                   // int temp = sc.nextInt();

                   // move = temp - 1;

                    move = sc.nextInt() - 1;

                }

                updateBoard(move);
                displayBoard();

                if(playerTurn == 1)
                {
                    playerTurn = -1;
                }
                else
                {
                    playerTurn = 1;
                }

                //check if theres winner
                String verdict = checkBoard();


                if(checkWinner(verdict) == 2 || checkWinner(verdict) == 1 || checkWinner(verdict) == 0)
                {
                    switch (verdict) {
                        case "X" -> System.out.println("\nComputer Wins!");

                        case "O" -> System.out.println("\nYou Win!");

                        case "TIE" -> System.out.println("\nIt's a tie!");

                    }


                    break;
                }

            }





            resetBoard();

            System.out.println("*************************************************************");
            System.out.println("\nPlay Again?");
            System.out.println("\n[1] Yes");
            System.out.println("[2] No");
            System.out.print("\nEnter Choice: ");
            choice = sc.nextInt();

            System.out.println("*************************************************************");

            if(firstPlayer.equals("COMPUTER"))
            {
                firstPlayer = "HUMAN";
            }
            else
            {
                firstPlayer = "COMPUTER";
            }


        } while(choice == 1);




    }




    public static void main (String [] args)
    {

        int nChoice;
        boolean exit = false;
        initBoard();   // initializing the board

        scores.put("X", 1);
        scores.put("O", -1);
        scores.put("TIE", 0);


        while(!exit)
        {
            System.out.println("*************************************************************");
          //  System.out.println("Welcome to Tic-Tac-Toe!");
            System.out.println("\n" +
                    "                                            ████████╗██╗ ██████╗    ████████╗ █████╗  ██████╗    ████████╗ ██████╗ ███████╗\n" +
                    "                                            ╚══██╔══╝██║██╔════╝    ╚══██╔══╝██╔══██╗██╔════╝    ╚══██╔══╝██╔═══██╗██╔════╝\n" +
                    "                                               ██║   ██║██║            ██║   ███████║██║            ██║   ██║   ██║█████╗  \n" +
                    "                                               ██║   ██║██║            ██║   ██╔══██║██║            ██║   ██║   ██║██╔══╝  \n" +
                    "                                               ██║   ██║╚██████╗       ██║   ██║  ██║╚██████╗       ██║   ╚██████╔╝███████╗\n" +
                    "                                               ╚═╝   ╚═╝ ╚═════╝       ╚═╝   ╚═╝  ╚═╝ ╚═════╝       ╚═╝    ╚═════╝ ╚══════╝\n" +
                    "                                                                                                                           \n");
            System.out.println("____________________________");
            System.out.println("\nChoose computer level: ");
            System.out.println("\n[1] Level 0 ");
            System.out.println("[2] Level 1 ");
            System.out.println("[3] Level 2 ");
            System.out.println("[4] Exit ");

            System.out.print("\nEnter choice: ");
            nChoice = sc.nextInt();

          //  System.out.println("_____________________________________________________________");

            // trap user's invalid move

            switch (nChoice) {
                case 1 -> startGame(0);
                case 2 -> startGame(1);
                case 3 -> startGame(2);
                case 4 -> System.exit(1);
                default -> exit = true;
            }




        }










    }




}
