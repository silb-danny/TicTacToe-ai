import java.util.*;
public class TicTacToe {
    public static Scanner sc = new Scanner(System.in);
    public static int height;
    public static void main(String[] args) {
        int[] state = new int[9];
        height = (int) Math.sqrt(state.length);
        State posibs = new State(arrToString(state),0);
        build(posibs, state, 1);
//        findWins(posibs, posibs.current.toCharArray());
        System.out.println(posibs);
        System.out.println("play? (y/n)");
        char playAgain = sc.next().charAt(0);
        while(playAgain == 'y' || playAgain == 'Y') {
            state = new int[9];
            game(posibs,state);
            System.out.println("play? (y/n)");
            playAgain = sc.next().charAt(0);
        }
    }
    public static void game(State board, int[] state) {
        System.out.println("enter what player is (o,x)");
        int player,comp,pos,current = 1;
        if(sc.next().charAt(0) == 'x')
            player = 1;
        else
            player = -1;
        comp = player *-1;
        while(checkWin(state) == 0) {
            if(current == 1) {
                if(1 == player) {
                    System.out.println("enter new position (1-"+state.length+")");
                    pos = sc.nextInt() - 1;
                    while(state[pos] != 0) {
                        System.out.println("enter new position (1-"+state.length+")");
                        pos = sc.nextInt() - 1;
                    }
                    state[pos] = player;
                    board = board.next_states.get(arrToString(state));
                }else if(1 == comp) {
                    board = findMaxBoard(board, comp);
                    state[board.changed] = comp;
                    System.out.println("computer: ");
//                    comp *= -1;
//                    player *= -1;
                }
                printBoard(state);
                current *= -1;
            } else {
                if(player == -1) {
                    System.out.println("enter new position (1-"+state.length+")");
                    pos = sc.nextInt() - 1;
                    while(state[pos] != 0) {
                        System.out.println("enter new position (1-"+state.length+")");
                        pos = sc.nextInt() - 1;
                    }
                    state[pos] = player;
                    board = board.next_states.get(arrToString(state));
                }else if(-1 == comp) {
                    board = findMaxBoard(board, comp);
                    state[board.changed] = comp;
                    System.out.println("computer: ");
//                    comp *= -1;
//                    player *= -1;
                }
                printBoard(state);
                current *= -1;
            }
            if(checkWin(state) != 0 || containsElem(state,0) == 0)
                break;
        }
        int won = checkWin(state);
        if(won == 0)
            System.out.println("draw");
        if(won == -1)
            System.out.println((comp == -1)?"computer won":"player won");
        if(won == 1)
            System.out.println((comp == 1)?"computer won":"player won");
    }
    public static void printBoard(int[] arr) {
        System.out.println(" ");
        for (int i = 0; i < height; i++) {
            System.out.print(" |");
            for (int j = 0; j < arr.length/height; j++) {
                if(arr[height*i+j] == 0)
                    System.out.print("-|");
                if(arr[height*i+j] == 1)
                    System.out.print("X|");
                if(arr[height*i+j] == -1)
                    System.out.print("O|");
            }
            System.out.println();
        }
    }

    public static boolean otherWin(State board, int comp,boolean print) {
        // function checks if other player can win
        if(board.next_states != null) {
            Collection<State> values = board.next_states.values();
            for (State temp : values) {
                if(print)
                    System.out.println(temp);
                if (temp.next_states == null) {
                    if (comp == 1 && temp.winsm1 == 1 || comp == -1 && temp.wins1 == 1)
                        return true;
                }
            }
        }
        return false;
    }
    public static State findMaxBoard(State board, int comp) {
        // finding max out of min (minmax)
        int max = comp;
        Collection<State> values = board.next_states.values();
        State maxBoard = values.stream().findFirst().get();
        for (State temp : values) {
            max = Math.max(temp.draws*comp,max);
        }
        for (State temp : values) {
            if(temp.draws == max)
                maxBoard = temp;
        }
//        System.out.println(maxBoard);
        return maxBoard;
    }
    public static int containsElem(int[] array, int elem) {
    // checks how many times array contains certain element
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if(array[i] == elem)
                count ++;
        }
        return count;
    }
    public static String arrToString(int[] arr){
        String s = "";
        for (int i = 0; i < arr.length; i++) {
            s+=arr[i];
        }
        return s;
    }
    public static void build(State posibs, int[] state, int player) {
        // builds decision tree
        if(containsElem(state,0) > 0) { // not if there arent any zeros left or some one has won the game
            int type = checkWin(state);
            Hashtable<String, State> next_states = new Hashtable<String, State>();
            int[] newState = new int[state.length];
            if(type == 0) {
                int v = player*-1;
                for (int i = 0; i < state.length; i++) { // looping over board
                    if (state[i] == 0) { // checking if the spot is open
                        // creating new board
                        for (int j = 0; j < newState.length; j++) // cloning board
                            newState[j] = state[j];
                        newState[i] = player;
                        State newPosibs = new State(arrToString(newState),i);
                        next_states.put(arrToString(newState), newPosibs);
                        build(newPosibs, newState, player * -1);
                        if(player == -1)
                            v = Math.min(newPosibs.draws,v);
                        if(player == 1)
                            v = Math.max(newPosibs.draws,v);
                    }
                }
                posibs.setNext_states(next_states);
                posibs.draws = v;
            } else {
                posibs.draws = type;
            }
        }
        else {
            int type = checkWin(state);
            posibs.draws = type;
        }
    }

    public static int checkWin(int[] state) {
        // checks if there is a win and which player
//        int[][] wins = winCombinaions(state.length);
        int[][] wins = {
                {1,1,1,0,0,0,0,0,0},{0,0,0,1,1,1,0,0,0},{0,0,0,0,0,0,1,1,1},
                {1,0,0,1,0,0,1,0,0},{0,1,0,0,1,0,0,1,0},{0,0,1,0,0,1,0,0,1},
                {1,0,0,0,1,0,0,0,1},{0,0,1,0,1,0,1,0,0}};
        int sum;
        for (int i = 0; i < wins.length; i++) {
            sum = 0;
            for (int j = 0; j < state.length; j++) {
                sum += state[j]*wins[i][j];
//                System.out.print(state[j]+" "+wins[i][j]+" = " + state[j]*wins[i][j]+", ");
            }
            if(sum == height || sum == -height)
                return sum/height;
        }
        return 0;
    }
    public static int checkWin(char[] state) {
//        int[][] wins = winCombinaions(height*height);
        int[][] wins = {
                {1,1,1,0,0,0,0,0,0},{0,0,0,1,1,1,0,0,0},{0,0,0,0,0,0,1,1,1},
                {1,0,0,1,0,0,1,0,0},{0,1,0,0,1,0,0,1,0},{0,0,1,0,0,1,0,0,1},
                {1,0,0,0,1,0,0,0,1},{0,0,1,0,1,0,1,0,0}};
        int sum = 0;
        for (int i = 0; i < wins.length; i++) {
            sum = 0;
            int offset = 0;
            for (int j = 0; j < wins[i].length; j++) {
                if(wins[i][j] == 1 && state[j+offset] == '1')
                    sum += 1;
                if(wins[i][j] == 1 && state[j+offset] == '-')
                    sum += -1;
                if(state[j+offset] == '-')
                    offset ++;
            }
            if(sum == height || sum == -height)
                return sum/height;
        }
        return 0;
    }


    // my
    public static int[] findWins(State posibs, char[] state)
    {
        // recursively counts the number of wins in each possible state
        int[] out = new int[]{0,0};
        if(posibs.next_states != null) {
            Collection<State> values = posibs.next_states.values();
            int sum1 = 0; // sum of 1 wins
            int summ1 = 0; // sum of minus 1 wins
            int minw1 = 9;
            int minwm1 = 9;
            for (State value: values) {
                int[] update = findWins(value,value.current.toCharArray());
                sum1 += update[0];
                summ1 += update[1];
                if(value.wins1 > 0)
                    minw1 = Math.min(value.minwin1, minw1);
                if(value.winsm1 > 0)
                    minwm1 = Math.min(value.minwinm1, minwm1);
                if(value.minwin1 <= 2)
                    posibs.minwin1n ++;
                if(value.minwinm1 <= 2)
                    posibs.minwinm1n ++;
                posibs.draws += value.draws;
            }
            if(minw1 < 9)
                posibs.minwin1 = minw1 + 1;
            if(minwm1 < 9)
                posibs.minwinm1 = minwm1 + 1;
            posibs.updateWins(sum1,summ1);
            out[0] = sum1;
            out[1] = summ1;
        } else {
            int type = checkWin(state);
            if(type == -1) {
                posibs.updateWins(0, 1);
                posibs.minwinm1 = 0;
                out[1] = 1;
            }else if(type == 1) {
                posibs.updateWins(1, 0);
                posibs.minwin1 = 0;
                out[0] = 1;
            } else
                posibs.draws ++;
        }
        return out;
    }
    public static State findMaxBoardmy(State board, int comp) {
        // function finds correct new board to go to
        Collection<State> values = board.next_states.values();
        State maxBoard = values.stream().findFirst().get();
        ArrayList<State> mBoards = new ArrayList<>();
        int calc1 = 0,calc2 = 0;
        for (State temp : values) {
            System.out.println(temp);
//            System.out.println(temp);
//            System.out.println(otherWincur(temp,comp));
            if(comp == 1 && temp.wins1 <= 1 && temp.next_states == null)
                return temp;
            if(comp == -1 && temp.winsm1 <= 1 && temp.next_states == null)
                return temp;
            System.out.println("passed");
//            otherWincur(temp,comp);
            if(otherWin(temp, comp,false))
                continue;
            maxBoard = (temp.draws > maxBoard.draws || (comp == -1 && temp.winsm1 > maxBoard.winsm1) || comp == 1 && temp.wins1 > maxBoard.wins1 ) ? temp:maxBoard;
        }
        for (State temp : values) {
            if(otherWin(temp, comp,false))
                continue;
            if(temp.draws >= maxBoard.draws)
                mBoards.add(temp);
        }
        if(mBoards.size() > 1)
        // for cases where the other player might win or there might be a draw
        // the computer will prefer the state where there are the least player wins
            for (State temp : mBoards) {
//                if(temp.winsm1 == 40)
//                otherWin(temp,comp,true);
                if (comp == 1 && maxBoard.wins1 < temp.wins1)
                    maxBoard = temp;
                if (comp == -1 && maxBoard.winsm1 < temp.winsm1)
                    maxBoard = temp;
            }
        return maxBoard;
    }
    //    public static int[][] winCombinaions(int length){
//        int[][] combs = new int[height*2+2][length];
//        int y = 0;
//        for (int i = y;i < height; i++) {
//            int[] c = new int[length];
//            for (int j = 0; j < height; j++) {
//                c[i+j] = 1;
//            }
//            combs[i] = c;
//            y ++;
//        }
//        for (int i = y;i < height; i++) {
//            int[] c = new int[length];
//            for (int j = 0; j < height; j++) {
//                c[i+j*height] = 1;
//            }
//            combs[i] = c;
//            y ++;
//        }
//        int[] c = new int[length] ,x= new int[length];
//        for (int i = 0; i < height; i++) {
//            c[i+i*height] = 1;
//        }
//        combs[y ++] = c;
//        for (int i = 0; i < height; i++) {
//            c[height-1-i+height*i] = 1;
//        }
//        combs[y] = c;
//        return combs;
//    }
}