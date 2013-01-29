package com.example.Checkers.draw.util;

import android.content.Context;
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
    private int resource;
    private int textViewResourceId;

    public CheckersArrayAdapter(Context context, int resource, int textViewResourceId, List<Squares> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.playingBoard = objects;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
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

                        playingBoard.put(from, toSquare);
                        playingBoard.put(to, fromSquare);

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
                        if (Math.abs(from - to) > 12) {
                            int position;
                            if (toSquare.getColor().equals(Constants.BLUE)) {
                                position = from + ((from - to) / 2);
                            } else {
                                position = from - ((from - to) / 2);
                            }
                            playingBoard.setScoreUpdate(true);
                            playingBoard.get(position).makePieceDie();
                        }
                        stepFlag = true;
                    }
                }
            } else {
                makeToast("I haven't allowed steps");
            }
        } else {
            makeToast("This coordinates is not contains");
        }
        playingBoard.setContentUpdate(stepFlag);
        if (!stepFlag) makeToast("This is not allowed step");
        return playingBoard;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_black, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.tvText);
        textView.setText(playingBoard.get(position).getPosition().getPosition());

//        ImageView imageView = (ImageView) rowView.findViewById(R.id.squareImage);
//        imageView.setImageResource(R.drawable.piece_black);

        String s = playingBoard.get(position).getColor();
        if (s.startsWith(Constants.LIGHT)) rowView = inflater.inflate(R.layout.item_light, parent, false);
        else if (s.startsWith(Constants.BLUE)) rowView = inflater.inflate(R.layout.item_blue, parent, false);
        else if (s.startsWith(Constants.YELLOW)) rowView = inflater.inflate(R.layout.item_yellow, parent, false);

        return rowView;
    }

    private void makeToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
