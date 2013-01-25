package com.example.Checkers;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.example.Checkers.draw.entity.Squares;
import com.example.Checkers.draw.util.CheckersArrayAdapter;
import com.example.Checkers.draw.util.Constants;

import java.util.*;

public class MainActivity extends Activity {
    String[][] gridTwoDimensions;
    TreeMap<Integer, Squares> playingBoard;
    TreeMap<Integer, Squares> blackSquaresPlayingBoard;
    TreeMap<Integer, Squares> bluePiecesPlayingBoard;
    TreeMap<Integer, Squares> yellowPiecesPlayingBoard;

    Squares fromSquares;
    Squares toSquares;

    GridView gvMain;
    CheckersArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    private void init() {
        createTreeMapPlayingBoard();
        createOnlyBlackSquaresPlayingBoard();

        createOnlyBluePiecesPlayingBoard();
        createOnlyYellowPiecesPlayingBoard();

        updateAllAllowedSteps();

        setAdapter();

        gvMain = (GridView) findViewById(R.id.gvMain);
        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Squares square = (Squares) adapterView.getItemAtPosition(i);
                square.calculateAllowedSteps(blackSquaresPlayingBoard);

                if (fromSquares != null && !square.isPiece()) {
                    toSquares = (!square.isPiece()) ? square : null;

                    /*Обновялем Игровой стол, чтобы его перерисовать*/
                    playingBoard = adapter.swap(fromSquares, toSquares, playingBoard);

                    /* Обновляем Черные клетки, которыми мы пользуемся для обозначения рабочей области */
                    createOnlyBlackSquaresAndPiecesPlayingBoard();

                    /* Устанавливаем adapter и обновляем поле */
                    setAdapter();
                    gvMain.setAdapter(adapter);

                    toSquares = null;
                    fromSquares = null;
                }

                fromSquares = (square.isPiece()) ? square : null;

                String toast = "";
                if (square.getAllowedSteps().size() != 0) {
                    toast = " can go to: ";
                    for (int j = 0; j < square.getAllowedSteps().size(); j++) {
                        toast += (square.getAllowedSteps().size() - j > 1) ? square.getAllowedSteps().get(j) + " or " : square.getAllowedSteps().get(j);
                    }
                }
                makeToast("Item: " + adapterView.getItemAtPosition(i) + toast);
            }
        });

        gvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Squares square = (Squares) adapterView.getItemAtPosition(i);
                return true;
            }
        });

        gvMain.setAdapter(adapter);
        adjustGridView();
    }

    private void setAdapter() {
        adapter = new CheckersArrayAdapter<String>(this, R.layout.item_black, R.id.tvText, new ArrayList<Squares>(playingBoard.values()));
    }


    private void adjustGridView() {
        gvMain.setNumColumns(9);
//        gvMain.setColumnWidth(50);
        gvMain.setVerticalSpacing(5);
        gvMain.setHorizontalSpacing(5);
    }

    private void createTreeMapPlayingBoard() {
        gridTwoDimensions = new String[9][9];
        playingBoard = new TreeMap<Integer, Squares>();

        int j = 0;
        for (char a = 'a' - 1; a < 'i'; a++) {
            for (int i = 0; i < 9; i++) {
                if (j == 0) {
                    if (i == 0) {
                        gridTwoDimensions[j][i] = "";
                        playingBoard.put(10 * j + i, new Squares("", ""));
                    } else {
                        gridTwoDimensions[j][i] = "" + i;
                        playingBoard.put(10 * j + i, new Squares("" + i, ""));
                    }
                } else {
                    if (i == 0) {
                        gridTwoDimensions[j][i] = a + "";
                        playingBoard.put(10 * j + i, new Squares(a + "", ""));
                    } else {
                        gridTwoDimensions[j][i] = a + "" + i;

                        if ((i % 2 != 0 && j % 2 == 0) || (i % 2 == 0 && j % 2 != 0)) {
                            playingBoard.put(10 * j + i, new Squares(j + "" + i, Constants.BLACK));
                        } else {
                            playingBoard.put(10 * j + i, new Squares(j + "" + i, Constants.LIGHT));
                        }
                    }
                }
            }
            j++;
        }
    }

    private void createOnlyBlackSquaresPlayingBoard() {
        blackSquaresPlayingBoard = new TreeMap<Integer, Squares>();

        for (Map.Entry<Integer, Squares> entry : playingBoard.entrySet()) {
            if (entry.getValue().getColor().equals(Constants.BLACK))
                blackSquaresPlayingBoard.put(entry.getKey(), entry.getValue());
        }
    }

    private void createOnlyBlackSquaresAndPiecesPlayingBoard() {
//        new Runnable() {
//            @Override
//            public void run() {
        blackSquaresPlayingBoard = new TreeMap<Integer, Squares>();

        for (Map.Entry<Integer, Squares> entry : playingBoard.entrySet()) {
            if (!entry.getValue().getColor().equals(Constants.LIGHT) && !entry.getValue().getColor().equals(""))
                blackSquaresPlayingBoard.put(entry.getKey(), entry.getValue());
        }
//            }
//        };

    }

    private void createOnlyBluePiecesPlayingBoard() {
        bluePiecesPlayingBoard = new TreeMap<Integer, Squares>();
        int i = 0;
        for (Map.Entry<Integer, Squares> entry : blackSquaresPlayingBoard.entrySet()) {
            if (i < 12) {
                entry.getValue().setPiece(true);
                entry.getValue().setColor(Constants.BLUE);
                bluePiecesPlayingBoard.put(entry.getKey(), entry.getValue());
                i++;
            } else {
                break;
            }
        }
    }

    private void createOnlyYellowPiecesPlayingBoard() {
        yellowPiecesPlayingBoard = new TreeMap<Integer, Squares>();
        int i = 0;
        for (Map.Entry<Integer, Squares> entry : blackSquaresPlayingBoard.entrySet()) {
            if (i > 19) {
                entry.getValue().setPiece(true);
                entry.getValue().setColor(Constants.YELLOW);
                yellowPiecesPlayingBoard.put(entry.getKey(), entry.getValue());
            }
            i++;
        }
    }

    private void updateAllAllowedSteps() {
        for (Map.Entry<Integer, Squares> entry : blackSquaresPlayingBoard.entrySet()) {
            entry.getValue().calculateAllowedSteps(blackSquaresPlayingBoard);
        }
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
