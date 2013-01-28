package com.example.Checkers.draw.entity;

import com.example.Checkers.draw.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Squares {
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

    public Squares(String position, String color, Boolean piece) {
        this.position = new Position(position);
        this.color = color;
        this.allowedStep = false;
        this.crown = false;
        this.allowedSteps = new ArrayList<String>();
        this.piece = false;
    }

    public void calculateAllowedSteps(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        allowedSteps.clear();
        if (this.isPiece()) {
            /* если у нас Дамка - другой расчет */
            if (this.isCrown()) {
            /* пока еще не дамка и врядли ей станет */
            } else {
                addAllowedSteps(onlyBlackSquaresPlayingBoard);
            }
        }
    }

    private void addAllowedSteps(TreeMap<Integer, Squares> onlyBlackSquaresPlayingBoard) {
        Squares stepToLeftSquare;
        Squares stepToRightSquare;

        int positionToLeft = 0;
        int positionToRight = 0;
        /* по i мы еще не Дамка и можем ходить */
        if (this.position.getI() > 1 && this.position.getI() < 8) {
            if (this.position.getJ() > 0 && this.position.getJ() < 9) {
                /* Добавляем к текущей позиции значение и проверием что на той клетке
                * все равно мы тут проверяем наличие данной клетки, т.е. нам не надо
                * узнавать в каких рамках находяться j*/
                if (this.getColor().equals(Constants.BLUE)) {
                    positionToLeft = (this.position.getI() + 1) * 10 + this.position.getJ() - 1;
                    positionToRight = (this.position.getI() + 1) * 10 + this.position.getJ() + 1;
                } else {
                    positionToLeft = (this.position.getI() - 1) * 10 + this.position.getJ() - 1;
                    positionToRight = (this.position.getI() - 1) * 10 + this.position.getJ() + 1;
                }

                if (onlyBlackSquaresPlayingBoard.containsKey(positionToLeft)) {
                    stepToLeftSquare = onlyBlackSquaresPlayingBoard.get(positionToLeft);
                    /* Мы нашли кого-то */
                    if (stepToLeftSquare.isPiece()) {
                        /* Если мы BLUE */
                        if (this.getColor().equals(Constants.BLUE)) {
                            /* Черт, это наш же */
                            if (stepToLeftSquare.getColor().equals(Constants.BLUE)) {
                                /* Ложная тревога */
                            } else {
                                /* Попался */
                                /* Надо проверить: если ли куда ходит
                                * меняем точку перехода*/
                                positionToLeft = (this.position.getI() + 2) * 10 + this.position.getJ() - 2;
                                /* Проверяем валидность точки */
                                if (onlyBlackSquaresPlayingBoard.containsKey(positionToLeft)) {
                                    /* Получаем значение данной клетки */
                                    stepToLeftSquare = onlyBlackSquaresPlayingBoard.get(positionToLeft);
                                    /* Если есть место */
                                    if (!stepToLeftSquare.isPiece()) {
                                        allowedSteps.add(positionToLeft + "");
                                    } else {
                                        /* Нету места */
                                    }
                                } else {
                                    /* Такой позиции не существет */
                                }
                            }
                        } else {
                            /* если не BLUE */
                            /* Черт, это наш же */
                            if (stepToLeftSquare.getColor().equals(Constants.YELLOW)) {
                                /* Ложная тревога */
                            } else {
                                /* Попался */
                                /* Надо проверить: если ли куда ходит
                                * меняем точку перехода*/
                                positionToLeft = (this.position.getI() - 2) * 10 + this.position.getJ() - 2;
                                /* Проверяем валидность точки */
                                if (onlyBlackSquaresPlayingBoard.containsKey(positionToLeft)) {
                                    /* Получаем значение данной клетки */
                                    stepToLeftSquare = onlyBlackSquaresPlayingBoard.get(positionToLeft);
                                    /* Если есть место */
                                    if (!stepToLeftSquare.isPiece()) {
                                        allowedSteps.add(positionToLeft + "");
                                    } else {
                                        /* Нету места */
                                    }
                                } else {
                                    /* Такой позиции не существет */
                                }
                            }
                        }
                    } else {
                        allowedSteps.add(positionToLeft + "");
                    }
                }

                if (onlyBlackSquaresPlayingBoard.containsKey(positionToRight)) {
                    stepToRightSquare = onlyBlackSquaresPlayingBoard.get(positionToRight);
                    if (stepToRightSquare.isPiece()) {
                                                            /* Если мы BLUE */
                        if (this.getColor().equals(Constants.BLUE)) {
                            /* Черт, это наш же */
                            if (stepToRightSquare.getColor().equals(Constants.BLUE)) {
                                /* Ложная тревога */
                            } else {
                                /* Попался */
                                /* Надо проверить: если ли куда ходит
                                * меняем точку перехода*/
                                positionToRight = (this.position.getI() + 2) * 10 + this.position.getJ() + 2;
                                /* Проверяем валидность точки */
                                if (onlyBlackSquaresPlayingBoard.containsKey(positionToRight)) {
                                    /* Получаем значение данной клетки */
                                    stepToLeftSquare = onlyBlackSquaresPlayingBoard.get(positionToRight);
                                    /* Если есть место */
                                    if (!stepToLeftSquare.isPiece()) {
                                        allowedSteps.add(positionToRight + "");
                                    } else {
                                        /* Нету места */
                                    }
                                } else {
                                    /* Такой позиции не существет */
                                }
                            }
                        } else {
                            /* если не BLUE */
                            /* Черт, это наш же */
                            if (stepToRightSquare.getColor().equals(Constants.YELLOW)) {
                                /* Ложная тревога */
                            } else {
                                /* Попался */
                                /* Надо проверить: если ли куда ходит
                                * меняем точку перехода*/
                                positionToRight = (this.position.getI() - 2) * 10 + this.position.getJ() + 2;
                                /* Проверяем валидность точки */
                                if (onlyBlackSquaresPlayingBoard.containsKey(positionToRight)) {
                                    /* Получаем значение данной клетки */
                                    stepToLeftSquare = onlyBlackSquaresPlayingBoard.get(positionToRight);
                                    /* Если есть место */
                                    if (!stepToLeftSquare.isPiece()) {
                                        allowedSteps.add(positionToRight + "");
                                    } else {
                                        /* Нету места */
                                    }
                                } else {
                                    /* Такой позиции не существет */
                                }
                            }
                        }
                    } else {
                        allowedSteps.add(positionToRight + "");
                    }
                }
            } else {
                /* Когда по j у нас только один ход */
            }
        } else {
            /* если Шашка перешла на позицию Дамки */
        }
        if (allowedSteps.size() > 0) this.allowedStep = true;
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
        return position.getPosition();
    }
}
