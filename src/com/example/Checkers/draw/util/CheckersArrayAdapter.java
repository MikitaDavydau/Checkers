package com.example.Checkers.draw.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Checkers.R;
import com.example.Checkers.draw.entity.Squares;

import java.util.List;

public class CheckersArrayAdapter<T> extends ArrayAdapter<Squares> {

    private Context context;
    private List<Squares> playingBoard;

    public CheckersArrayAdapter(Context context, int resource, int textViewResourceId, List<Squares> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.playingBoard = objects;
    }


    public CheckersTreeMap<Integer, Squares> swap(Squares fromSquare, Squares toSquare, CheckersTreeMap<Integer, Squares> playingBoard) {
        boolean stepFlag = false;
        playingBoard.setContentUpdate(false);
        if (playingBoard.containsValue(fromSquare) && playingBoard.containsValue(toSquare)) {
            if (fromSquare.isAllowedStep()) {
                for (String s : fromSquare.getAllowedSteps()) {
                    if (s.equals(toSquare.getPosition().getPosition())) {
                        int from = Integer.parseInt(fromSquare.getPosition().getPosition());
                        int to = Integer.parseInt(toSquare.getPosition().getPosition());

                        toSquare.setPosition(String.valueOf(from));
                        fromSquare.setPosition(String.valueOf(to));

                        /* Хитрый способ получения значения клетки, которая находиться между 2мя
                        * т.е. допустим мы ходим: 54-36, перескакивая через Шашку
                        * система расчета для Желтых такая (все значения берутся абсолютно!):
                        * 1. Право верх - 54 - (54-36)/2 = 54 - 9 - получаем значение искомой клетки
                        * 2. Лево вверх - 54 - (54-32)/2 = 54 - 11
                        * 3. Право низ - 54 - (54-76)/2 = 54 - -11
                        * 4. Лево низ  - 54 - (54-72/2 = 54 - -9
                        * для Синих все то же самое, только меняем - на +, вот так:
                        * Право верх - 36 + (36-54)/2 = 36 + 9 - получаем значение искомой клетки
                        * и т.д.
                        *
                        * Math.abs(from - to) > 12 - используяется для того, чтомы точно понять, что мы перескакиваем,
                        * а не делаем просто ход. Ход (from - to): 9 либо 11*/
                        int position;
                        if (!fromSquare.isCrown()) {
                            if (Math.abs(from - to) > 12) {
                                if (toSquare.getColor().equals(Constants.BLUE)) {
                                    position = from + ((from - to) / 2);
                                } else {
                                    position = from - ((from - to) / 2);
                                }
                                playingBoard.setScoreUpdate(true);
                                playingBoard.get(position).makePieceDie();
                            }
                        } else {
                            if (Math.abs(from - to) > 12) {
                                /* Надо определить кратность движения
                                 * 1. Проверить на что делиться нацело
                                 * 2. Умножить и получить испокое значение*/
                                int positionRatio;
                                /* Если движемся вверх */
                                if ((from - to) % 9 == 0) {
                                    positionRatio = (from - to) / 9;
                                    position = from - (9 * (Math.abs(positionRatio) - 1));
                                } else {
                                /*Если вниз */
                                    positionRatio = (from - to) / 11;
                                    position = from + (11 * (Math.abs(positionRatio) - 1));
                                }

                                playingBoard.setScoreUpdate(true);
                                playingBoard.get(position).makePieceDie();
                            }
                        }

                        fromSquare = (isCorner(to / 10)) ? makeCrown(fromSquare) : fromSquare;

                        playingBoard.put(from, toSquare);
                        playingBoard.put(to, fromSquare);

                        stepFlag = true;
                    }
                }
            } else {
                if (playingBoard.isMakeToast()) makeToast("I haven't allowed steps");
            }
        } else {
            if (playingBoard.isMakeToast()) makeToast("This coordinates is not contains");
        }
        playingBoard.setContentUpdate(stepFlag);
        if (!stepFlag && playingBoard.isMakeToast()) makeToast("This is not allowed step");

        return playingBoard;
    }

    private boolean isCorner(int to) {
        return to == 8 || to == 1;
    }

    private Squares makeCrown(Squares fromSquare) {
        if (fromSquare.getColor().equals(Constants.BLUE) && fromSquare.getPosition().getI() == 8)
            fromSquare.setCrown(true);

        if (fromSquare.getColor().equals(Constants.YELLOW) && fromSquare.getPosition().getI() == 1)
            fromSquare.setCrown(true);

        return fromSquare;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_black, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.tvText);
        textView.setText(playingBoard.get(position).getPosition().getPosition());

        String s = playingBoard.get(position).getColor();
        Boolean isCrown = playingBoard.get(position).isCrown();

        if (s.startsWith(Constants.LIGHT)) {
            rowView = inflater.inflate(R.layout.item_light, parent, false);
        } else if (s.startsWith(Constants.BLUE)) {
            rowView = inflater.inflate(R.layout.item_blue, parent, false);
            if (isCrown) {
                rowView = setCrownText(rowView);
            }
        } else if (s.startsWith(Constants.YELLOW)) {
            rowView = inflater.inflate(R.layout.item_yellow, parent, false);
            if (isCrown) {
                rowView = setCrownText(rowView);
            }
        } else if (s.startsWith(Constants.SIMPLE)) {
            rowView = inflater.inflate(R.layout.item_simple, parent, false);
            rowView = setBoarderText(rowView, position);
        }
        return rowView;
    }

    private View setCrownText(View rowView) {
        TextView textView = (TextView) rowView.findViewById(R.id.tvText);
        textView.setTextColor(Color.BLACK);
        textView.setText("Cr");
        return rowView;
    }

    private View setBoarderText(View rowView, int position) {
        TextView textView = (TextView) rowView.findViewById(R.id.tvText);
        textView.setText(playingBoard.get(position).getPosition().getPosition());
        return rowView;
    }

    private void makeToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
