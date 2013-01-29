package com.example.Checkers.draw.entity;

public class Position {
    private String position;
    private int i;
    private int j;
    private int ij;

    public Position(String position) {
        this.position = setPosition(position);
    }

    public String getPosition() {
        return position;
    }

    public String setPosition(String position) {

        this.position = position;
        try {
            this.ij = Integer.parseInt(position);

            this.i = this.ij / 10;
            this.j = this.ij % 10;
        } catch (NumberFormatException e) {
        }

        return getPosition();
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getIJ() {
        return ij;
    }

    public void setIJ(int ij) {
        this.ij = ij;
    }

    public String toString() {
        return position;
    }

    public void convertIJToString() {
        this.ij = this.i * 10 + this.j;
        this.position = this.ij + "";
    }
}
