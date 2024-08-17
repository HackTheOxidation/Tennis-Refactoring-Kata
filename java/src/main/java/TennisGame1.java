public class TennisGame1 implements TennisGame {
    
    private final Player player1;
    private final Player player2;

    public enum TennisScore {
        Love,
        Fifteen,
        Thirty,
        Forty,
        Advantage,
        Win
    }

    public static class Player {
        private TennisScore score = TennisScore.Love;
        private final String name;

        public Player(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public TennisScore getScore() {
            return score;
        }

        public void incScore(Player other) {
            this.score = switch (this.score) {
                case Love -> TennisScore.Fifteen;
                case Fifteen -> TennisScore.Thirty;
                case Thirty -> TennisScore.Forty;
                case Forty ->
                    switch (other.getScore()) {
                        case Forty -> TennisScore.Advantage;
                        case Advantage -> {
                            other.score = TennisScore.Forty;
                            yield TennisScore.Forty;
                        }
                        default -> TennisScore.Win;
                    };
                case Advantage, Win -> TennisScore.Win;
            };
        }

        public Player compare(Player other) {
            if (this.getScore() == other.getScore()) return null;
            return this.getScore().ordinal() > other.getScore().ordinal() ? this : other;
        }

        public static boolean isWin(Player p1, Player p2) {
            return p1.getScore() == TennisScore.Win || p2.getScore() == TennisScore.Win;
        }
    }

    public TennisGame1(String player1Name, String player2Name) {
        this.player1 = new Player(player1Name);
        this.player2 = new Player(player2Name);
    }

    public void wonPoint(String playerName) {
        if (playerName.equals(player1.getName()))
            player1.incScore(player2);
        else
            player2.incScore(player1);
    }

    private String getScoreForTie(TennisScore score) {
        return switch (score.ordinal()) {
            case 0, 1, 2  -> score + "-All";
            default -> "Deuce";
        };
    }

    private boolean isInFinalStage() {
        return player1.getScore().ordinal() >= TennisScore.Forty.ordinal()
                && player2.getScore().ordinal() >= TennisScore.Forty.ordinal();
    }

    public String getScore() {
        Player bestPlayer = player1.compare(player2);
        if (bestPlayer == null) {
            return getScoreForTie(player1.getScore());
        } else if (Player.isWin(player1, player2)) {
            return "Win for " + bestPlayer.getName();
        } else if (isInFinalStage()) {
            return "Advantage " + bestPlayer.getName();
        } else {
            return player1.getScore().toString() + "-" + player2.getScore().toString();
        }
    }
}
