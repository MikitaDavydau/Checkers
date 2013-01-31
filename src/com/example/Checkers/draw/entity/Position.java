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

    public boolean isIOnTheBoard() {
        return this.i > 1 && this.i < 8;
    }

    public boolean isJOnTheBoard() {
        return this.j > 0 && this.j < 9;
    }

    public boolean isIValid() {
        return this.i > 0 && this.i < 9;
    }

    public boolean isJValid() {
        return this.j > 0 && this.j < 9;
    }

    public boolean isIJValid() {
        return (isIValid() && isJValid());
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

    public int setBluePositionLeft() {
        return (this.i + 1) * 10 + this.j - 1;
    }

    public int setBluePositionRight() {
        return (this.i + 1) * 10 + this.j + 1;
    }

    public int setYellowPositionLeft() {
        return (this.i - 1) * 10 + this.j - 1;
    }

    public int setYellowPositionRight() {
        return (this.i - 1) * 10 + this.j + 1;
    }

    public void setBlueMoveLeft() {
        this.i += 1;
        this.j -= 1;
    }

    public void setBlueMoveRight() {
        this.i += 1;
        this.j += 1;
    }

    public void setYellowMoveLeft() {
        this.i -= 1;
        this.j -= 1;
    }

    public void setYellowMoveRight() {
        this.i -= 1;
        this.j += 1;
    }
}
