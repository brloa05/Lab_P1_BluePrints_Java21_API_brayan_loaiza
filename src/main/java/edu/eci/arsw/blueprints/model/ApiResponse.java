package edu.eci.arsw.blueprints.model;

public record ApiResponse<T>(int code, String message, T data) {}
