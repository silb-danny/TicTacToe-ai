import java.util.Enumeration;
import java.util.Hashtable;

public class State {
    public int minwin1 = 9;
    public int minwinm1 = 9;
    public int minwin1n = 0; // minimum to win number that is 1 1
    public int minwinm1n = 0; // minimum to in number that is 1 -1
    public int wins1;
    public int winsm1;
    public int draws = 0;
    public int changed;
    public Hashtable<String, State> next_states;
    public String current;
    public State() {
        wins1 = -1;
        winsm1 = -1;
    }
    public State(String current, int changed) {
        this.next_states = null;
        this.current = current;
        this.changed = changed;
    }
    public void updateWins(int wins1 ,int winsm1) {
        this.wins1 = wins1;
        this.winsm1 = winsm1;
    }

    public void setNext_states(Hashtable<String, State> next_states) {
        this.next_states = next_states;
    }

    public int[] getWins() {
        return new int[]{wins1,winsm1};
    }

    @Override
    public String toString() {
        String out = "wins 1: "+wins1+" \n";
        out += "wins -1: "+winsm1+" \n";
        out += "draws: "+draws+" \n";
        out += "1 #: "+minwin1n+" \n";
        out += "-1 #: "+minwinm1n+" \n";
        out += "min steps to wins 1: "+minwin1+" \n";
        out += "min steps to wins -1: "+minwinm1+" \n";
        out += "mystate :";
        for (char i: current.toCharArray()) {
            out += " "+i;
        }
        out += "\n";

        if(next_states != null) {
            int j = 0;
            Enumeration<String> keys = next_states.keys();
            while(keys.hasMoreElements()) {
                out += "state "+ ++j +":";
                String k = keys.nextElement();
                for (char i: k.toCharArray()) {
                    out += " "+i;
                }
                State s = next_states.get(k);
                out += " .win: "+s.draws+"\n";
//                out += "  .1 #: "+s.minwin1n;
//                out += "  .-1 #: "+s.minwinm1n+"\n";
            }
        }
        return out;
    }
}
