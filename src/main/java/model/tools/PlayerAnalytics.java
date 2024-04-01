package model.tools;

import java.io.Serializable;

public record PlayerAnalytics (
    String uid,
    String email,
    String pseudo,
    String region,
    int bestScore,
    int playedGames,
    int wonGames,
    int lostGames
) implements Serializable {}
