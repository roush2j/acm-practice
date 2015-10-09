import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        InputLoop: while (s.hasNextLine()) {
            String gamestr = s.nextLine();
            Game game = new Game();
            for (int i = 0; i < gamestr.length(); i += 2) {
               boolean valid = game.roll(gamestr.charAt(i));
               if (!valid) break InputLoop;
            }
            System.out.println(game.score());
        }
        s.close();
    }

    public static class Game {

        private int score = 0, lastroll = 0;

        private int rollx = 0, rollr = 20;

        public boolean roll(char c) {
            int x;
            if
            (c >= '0' && c <= '9') {
                lastroll = c - '0';
                x = rollx - 1;
            } else if (c == '/') {
                lastroll = 10 - lastroll;
                x = 1;
            } else if (c == 'X') {
                lastroll = 10;
                x = 2;
                rollr--;
            } else return false;
            if (rollr-- > 0 && rollx > 0) score += lastroll;
            score += lastroll;
            rollx = x;
            return true;
        }

        public int score() {
            return score;
        }
    }
}
