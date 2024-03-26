package model.tools;

public record PlayerAnalytics(
    String uid,
    String email,
    String pseudo,
    String region,
    int bestScore,
    int playedGames,
    int wonGames,
    int lostGames
) {}
