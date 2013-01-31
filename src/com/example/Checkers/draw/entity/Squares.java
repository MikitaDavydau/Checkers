package com.example.Checkers.draw.entity;

import com.example.Checkers.draw.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Squares {
    private final int SIDES = 4;

    private Position position;

    private String color;

    private boolean crown;
    private boolean allowedStep;
    private boolean piece;

    private List<String> allowedSteps;

    public Squares(String position, String color) {
        this.position = new Position(position);
        this.color = color;
        this.allowedStep = false;
        this.crown = false;
        this.allowedSteps = new ArrayList<String>();
    }

    public void calculateAllowedSteps(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        allowedSteps.clear();
        if (this.isPiece()) {
            if (this.isCrown()) {
                calculateAllowedCrownSteps(onlyBlackSquaresPlayingBoard);
            } else {
                addAllowedSteps(onlyBlackSquaresPlayingBoard);
            }
        }
    }

    private void calculateAllowedCrownSteps(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        Squares saveThisSquare = copySquare();

        for (int i = 0; i < SIDES; i++) {
            if (i < 2) {
                addAllowedSteps(onlyBlackSquaresPlayingBoard);
                while (getPosition().isIJValid()) {
                    if (moveOneStepToLeft(onlyBlackSquaresPlayingBoard))
                        addAllowedCrownStepsLeft(onlyBlackSquaresPlayingBoard);
                }
            } else {
                while (getPosition().isIJValid()) {
                    if (moveOneStepToRight(onlyBlackSquaresPlayingBoard))
                        addAllowedCrownStepsRight(onlyBlackSquaresPlayingBoard);
                }
            }
            restoreSquare(saveThisSquare);
            changeColor();
        }
    }

    private boolean moveOneStepToLeft(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        boolean stepFlag = true;
        Position currentPosition = this.getPosition();
        if (isBlue()) {
            currentPosition.setBlueMoveLeft();
        } else {
            currentPosition.setYellowMoveLeft();
        }
        currentPosition.convertIJToString();
        if (onlyBlackSquaresPlayingBoard.get(currentPosition.getIJ()) != null && !onlyBlackSquaresPlayingBoard.get(currentPosition.getIJ()).isPiece()) {
            this.setPosition(currentPosition.getPosition());
            stepFlag = true;
        }
        return stepFlag;
    }

    private boolean moveOneStepToRight(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        boolean stepFlag = true;
        Position currentPosition = this.getPosition();
        if (isBlue()) {
            currentPosition.setBlueMoveRight();
        } else {
            currentPosition.setYellowMoveRight();
        }
        currentPosition.convertIJToString();
        if (onlyBlackSquaresPlayingBoard.get(currentPosition.getIJ()) != null && !onlyBlackSquaresPlayingBoard.get(currentPosition.getIJ()).isPiece()) {
            this.setPosition(currentPosition.getPosition());
            stepFlag = true;
        }
        return stepFlag;
    }

    private void addAllowedCrownStepsRight(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        int positionToRight;
        if (getPosition().isIOnTheBoard()) {
            if (getPosition().isJOnTheBoard()) {
                if (isBlue()) {
                    positionToRight = getPosition().setBluePositionRight();
                } else {
                    positionToRight = getPosition().setYellowPositionRight();
                }
                if (onlyBlackSquaresPlayingBoard.containsKey(positionToRight)) {
                    getStepRightByPosition(positionToRight, onlyBlackSquaresPlayingBoard);
                }
            }
        }
        if (allowedSteps.size() > 0) this.allowedStep = true;
    }

    private void addAllowedCrownStepsLeft(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        int positionToLeft;
        if (getPosition().isIOnTheBoard()) {
            if (getPosition().isJOnTheBoard()) {
                if (isBlue()) {
                    positionToLeft = getPosition().setBluePositionLeft();
                } else {
                    positionToLeft = getPosition().setYellowPositionLeft();
                }
                if (onlyBlackSquaresPlayingBoard.containsKey(positionToLeft)) {
                    getStepLeftByPosition(positionToLeft, onlyBlackSquaresPlayingBoard);
                }
            }
        }
        if (allowedSteps.size() > 0) this.allowedStep = true;
    }

    private void addAllowedSteps(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        int positionToLeft;
        int positionToRight;
        /* по i мы еще не Дамка и можем ходить */
        if (getPosition().isIValid()) {
            if (getPosition().isJValid()) {
                /* Добавляем к текущей позиции значение и проверием что на той клетке
                * все равно мы тут проверяем наличие данной клетки, т.е. нам не надо
                * узнавать в каких рамках находяться j*/
                if (isBlue()) {
                    positionToLeft = getPosition().setBluePositionLeft();
                    positionToRight = getPosition().setBluePositionRight();
                } else {
                    positionToLeft = getPosition().setYellowPositionLeft();
                    positionToRight = getPosition().setYellowPositionRight();
                }

                if (onlyBlackSquaresPlayingBoard.containsKey(positionToLeft)) {
                    getStepLeftByPosition(positionToLeft, onlyBlackSquaresPlayingBoard);
                }

                if (onlyBlackSquaresPlayingBoard.containsKey(positionToRight)) {
                    getStepRightByPosition(positionToRight, onlyBlackSquaresPlayingBoard);
                }
            } else {
                /* Когда по j у нас только один ход */
            }
        } else {
            /* если Шашка перешла на позицию Дамки */
        }
        if (allowedSteps.size() > 0) this.allowedStep = true;
    }

    private void getStepLeftByPosition(int positionToLeft, TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        Squares stepToLeftSquare = onlyBlackSquaresPlayingBoard.get(positionToLeft);
        if (stepToLeftSquare.isPiece()) {
            if (isBlue()) {
                if (!isBlue(stepToLeftSquare)) {
                    getLeftPosition(onlyBlackSquaresPlayingBoard);
                }
            } else {
                if (!isYellow(stepToLeftSquare)) {
                    getLeftPosition(onlyBlackSquaresPlayingBoard);
                }
            }
        } else {
            allowedSteps.add(positionToLeft + "");
        }
    }

    private void getStepRightByPosition(int position, TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        Squares stepToRightSquare = onlyBlackSquaresPlayingBoard.get(position);
        if (stepToRightSquare.isPiece()) {
            if (isBlue()) {
                if (!isBlue(stepToRightSquare)) {
                    geRightPosition(onlyBlackSquaresPlayingBoard);
                }
            } else {
                if (!isYellow(stepToRightSquare)) {
                    geRightPosition(onlyBlackSquaresPlayingBoard);
                }
            }
        } else {
            allowedSteps.add(position + "");
        }
    }

    private void getLeftPosition(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        /* Попался */
        /* Надо проверить: если ли куда ходит
        * меняем точку перехода*/
        int position = (isBlue()) ? (getI() + 2) * 10 + getJ() - 2 : (getI() - 2) * 10 + getJ() - 2;
            /* Проверяем валидность точки */
        if (onlyBlackSquaresPlayingBoard.containsKey(position)) {
                /* Получаем значение данной клетки */
            Squares stepToLeftSquare = onlyBlackSquaresPlayingBoard.get(position);
                /* Если есть место */
            if (!stepToLeftSquare.isPiece()) {
                allowedSteps.add(position + "");
            } else {
                /* Нету места */
            }
        } else {
            /* Такой позиции не существет */
        }
    }

    private void geRightPosition(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        /* Попался */
        /* Надо проверить: если ли куда ходит
        * меняем точку перехода*/
        int positionToRight = (isBlue()) ? (getI() + 2) * 10 + getJ() + 2 : (getI() - 2) * 10 + getJ() + 2;
        /* Проверяем валидность точки */
        if (onlyBlackSquaresPlayingBoard.containsKey(positionToRight)) {
            /* Получаем значение данной клетки */
            Squares stepToRightSquare = onlyBlackSquaresPlayingBoard.get(positionToRight);
            /* Если есть место */
            if (!stepToRightSquare.isPiece()) {
                allowedSteps.add(positionToRight + "");
            } else {
            /* Нету места */
            }
        } else {
        /* Такой позиции не существет */
        }
    }

    private Squares copySquare() {
        Squares squares = new Squares(this.getPosition().getPosition(), this.getColor());
        squares.setCrown(this.isCrown());
        squares.setPiece(this.isPiece());
        squares.allowedSteps = this.getAllowedSteps();
        return squares;
    }

    private void changeColor() {
        this.color = (isBlue()) ? Constants.YELLOW : Constants.BLUE;
    }

    private void restoreSquare(Squares squares) {
        this.setPosition(squares.getPosition().getPosition());
    }

    private boolean isBlue() {
        return this.getColor().equals(Constants.BLUE);
    }

    private boolean isBlue(Squares squares) {
        return squares.getColor().equals(Constants.BLUE);
    }

    private boolean isYellow(Squares squares) {
        return squares.getColor().equals(Constants.YELLOW);
    }

    private int getI() {
        return this.position.getI();
    }

    private int getJ() {
        return this.position.getJ();
    }

    public void makePieceDie() {
        this.color = Constants.BLACK;
        this.piece = false;
        this.allowedSteps.clear();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = new Position(position);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isCrown() {
        return crown;
    }

    public void setCrown(boolean crown) {
        this.crown = crown;
    }

    public boolean isAllowedStep() {
        return allowedStep;
    }

    public List<String> getAllowedSteps() {
        return allowedSteps;
    }

    public boolean isPiece() {
        return piece;
    }

    public void setPiece(boolean piece) {
        this.piece = piece;
    }

    public String toString() {
        return this.position.getPosition();
    }

}
