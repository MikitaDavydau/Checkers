package com.example.Checkers;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.example.Checkers.draw.entity.Squares;
import com.example.Checkers.draw.util.CheckersArrayAdapter;
import com.example.Checkers.draw.util.CheckersTreeMap;
import com.example.Checkers.draw.util.Constants;

import java.util.*;

public class MainActivity extends Activity {
    String[][] gridTwoDimensions;
    CheckersTreeMap<Integer, Squares> playingBoard;
    TreeMap<Integer, Squares> blackSquaresPlayingBoard;
    TreeMap<Integer, Squares> bluePiecesPlayingBoard;
    TreeMap<Integer, Squares> yellowPiecesPlayingBoard;

    Squares fromSquares;
    Squares toSquares;

    int yellowPieces = 0;
    int bluePieces = 0;

    String currentColor = Constants.YELLOW;

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
                    updatePlayingInformationRewriteBoard();
                }
                fromSquares = (square.isPiece() && square.getColor().equals(currentColor)) ? square : null;

                makeToast("Item: " + adapterView.getItemAtPosition(i) + createPositionToast(square));
            }
        });

        gvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Squares square = (Squares) adapterView.getItemAtPosition(i);
                //todo: create drag&drop action
                return true;
            }
        });

        blackSquaresPlayingBoard.get(61).setCrown(true);
        gvMain.setAdapter(adapter);
        adjustGridView();
    }

    private void updatePlayingInformationRewriteBoard() {
        /*Обновялем Игровой стол, чтобы его перерисовать*/
        playingBoard = adapter.swap(fromSquares, toSquares, playingBoard);
        if (playingBoard.isContentUpdate()) {
            /* Обновляем Черные клетки, которыми мы пользуемся для обозначения рабочей области */
            createOnlyBlackSquaresAndPiecesPlayingBoard();
            /* Устанавливаем adapter и обновляем поле */
            if (playingBoard.isScoreUpdate()) updateScore();
            changeCurrentColorResetFromTo();
            setAdapter();
            gvMain.setAdapter(adapter);
        }
    }

    private void createOnlyBlackSquaresAndPiecesPlayingBoard() {
        for (Map.Entry<Integer, Squares> entry : playingBoard.entrySet()) {
            if (!entry.getValue().getColor().equals(Constants.LIGHT) && !entry.getValue().getColor().equals("")) {
                blackSquaresPlayingBoard.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /* todo: обновить playingBoard
     * не обновляет playingBoard новыми значениями счета
     * точнее не обновдяем вид, а сама карта обновлена */
    private void updateScore() {
        bluePieces = 0;
        yellowPieces = 0;
        for (Map.Entry<Integer, Squares> entry : blackSquaresPlayingBoard.entrySet()) {
            if (entry.getValue().getColor().equals(Constants.BLUE)) {
                bluePieces++;
            } else if (entry.getValue().getColor().equals(Constants.YELLOW)) {
                yellowPieces++;
            }
        }
        playingBoard.get(93).setPosition(12 - yellowPieces + "");
        playingBoard.get(97).setPosition(12 - bluePieces + "");
    }

    private void setAdapter() {
        adapter = new CheckersArrayAdapter<String>(this, R.layout.item_black, R.id.tvText, new ArrayList<Squares>(playingBoard.values()));
    }

    private void changeCurrentColorResetFromTo() {
        currentColor = (currentColor.equals(Constants.BLUE)) ? Constants.YELLOW : Constants.BLUE;
        toSquares = null;
        fromSquares = null;
    }

    private String createPositionToast(Squares square) {
        String toast = "";
        if (square.getAllowedSteps().size() != 0) {
            toast = " can go to: ";
            for (int j = 0; j < square.getAllowedSteps().size(); j++) {
                toast += (square.getAllowedSteps().size() - j > 1) ? square.getAllowedSteps().get(j) + " or " : square.getAllowedSteps().get(j);
            }
        }
        return toast;
    }


    private void adjustGridView() {
        gvMain.setNumColumns(10);
//        gvMain.setColumnWidth(50);
        gvMain.setVerticalSpacing(5);
        gvMain.setHorizontalSpacing(5);
    }

    private void createTreeMapPlayingBoard() {
        gridTwoDimensions = new String[10][10];
        playingBoard = new CheckersTreeMap<Integer, Squares>();
        /* Заполнение будет построчное */
        int j = 0;
        for (char a = 'a' - 1; a < 'j'; a++) {
            for (int i = 0; i < 10; i++) {
                if (j == 0) {
                    if (i == 0) {
                        gridTwoDimensions[j][i] = "";
                        playingBoard.put(10 * j + i, new Squares("", ""));
                    } else if (i == 9) {
                        /* colorate right|left corner*/
                        playingBoard.put(10 * j + i, new Squares("", Constants.SIMPLE));
                    } else {
                        gridTwoDimensions[j][i] = "" + i;
                        playingBoard.put(10 * j + i, new Squares("" + i, ""));
                    }
                } else if (j == 9) {
                    /* colorate down line*/
                    switch (i) {
                        case 1:
                            playingBoard.put(10 * j + i, new Squares("Bl", Constants.SIMPLE));
                            break;
                        case 2:
                        case 6:
                            playingBoard.put(10 * j + i, new Squares("->", Constants.SIMPLE));
                            break;
                        case 3:
                            playingBoard.put(10 * j + i, new Squares("0", Constants.SIMPLE));
                            break;
                        case 5:
                            playingBoard.put(10 * j + i, new Squares("Yl", Constants.SIMPLE));
                            break;
                        case 7:
                            playingBoard.put(10 * j + i, new Squares("0", Constants.SIMPLE));
                            break;
                        default:
                            playingBoard.put(10 * j + i, new Squares("", Constants.SIMPLE));
                            break;
                    }
                } else {
                    if (i == 0) {
                        gridTwoDimensions[j][i] = a + "";
                        playingBoard.put(10 * j + i, new Squares(a + "", ""));
                    } else if (i == 9) {
                        /* colorate down */
                        playingBoard.put(10 * j + i, new Squares("", Constants.SIMPLE));
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
