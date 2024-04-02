package model.tools;

import java.io.Serializable;

public record PlayerCredentials (
    String uid,
    String email,
    String pseudo,
    String region
) implements Serializable {}