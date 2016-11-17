package kattis20161029;

import java.util.*;

public class FoosballDynasty {

    public static class Team {

        public final int  first, second;

        public boolean    swapped = false;

        public int        dyn     = 0;

        public static int maxdyn  = 0;

        public Team(int f, int s) {
            first = f;
            second = s;
        }

        public int losekick() {
            if (swapped) return first;
            else return second;
        }

        public Team lose(int n) {
            Team t = null;
            if (swapped) t = new Team(second, n);
            else t = new Team(first, n);
            t.swapped = true;
            return t;
        }

        public void win() {
            swapped = !swapped;
            dyn++;
            if (maxdyn < dyn) maxdyn = dyn;
        }

        public String toString() {
            return "" + first + " " + second + " " + dyn + " / " + swapped;
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int playercnt = s.nextInt();
        String[] names = new String[playercnt];
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < playercnt; i++) {
            names[i] = s.next();
            if (i >= 4) queue.add(i);
        }
        String moves = s.next();
        s.close();

        Team[] teams = new Team[moves.length() + 3];
        Team white = teams[0] = new Team(0, 2);
        Team black = teams[1] = new Team(1, 3);
        int teamcnt = 2;
        for (int i = 0; i < moves.length(); i++) {
            // simluate game step
            char win = moves.charAt(i);
            int winoff = -1, windef = -1, loseoff = -1, losedef = -1;
            if (win == 'W') {
                white.win();
                queue.add(black.losekick());
                black = teams[teamcnt++] = black.lose(queue.remove());
            } else {
                black.win();
                queue.add(white.losekick());
                white = teams[teamcnt++] = white.lose(queue.remove());
            }
        }

        for (int i = 0; i < teamcnt; i++) {
            if (teams[i].dyn == Team.maxdyn) {
                System.out.println(names[teams[i].first] + " "
                        + names[teams[i].second]);
            }
        }

    }
}
