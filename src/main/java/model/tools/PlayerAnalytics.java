package model.tools;

import java.io.Serializable;

public record PlayerAnalytics (
    String pseudo,
    int bestScore,
    int playedGames,
    int wonGames,
    int lostGames
) implements Serializable {}
