package edu.eci.arsw.blueprints.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record Point(int x, int y) { }
