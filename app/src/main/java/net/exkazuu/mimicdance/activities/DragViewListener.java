package net.exkazuu.mimicdance.activities;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.google.common.base.Joiner;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.IconType;

public class DragViewListener implements OnTouchListener {
    private final Activity activity;
    private final ImageView[][] cellIcons;
    private final String[][] cellTexts;
    private int oldX;
    private int oldY;
    private int initLeft;
    private int initTop;
    private static int moveCount;

    public DragViewListener(Activity activity, ImageView[][] cellIcons, String[][] cellTexts) {
        this.activity = activity;
        this.cellIcons = cellIcons;
        this.cellTexts = cellTexts;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        int left = view.getLeft() + (x - oldX);
        int top = view.getTop() + (y - oldY);

        oldX = x;
        oldY = y;

        Location to = new Location(
            top / cellIcons[0][0].getHeight(),
            Math.max(left / cellIcons[0][0].getWidth() - 1, 0));
        Location from = getCellLocation(view);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initLeft = view.getLeft();
                initTop = view.getTop();
                break;
            case MotionEvent.ACTION_MOVE:
                if (from == null || hasIcon(from.row, from.column)) {
                    view.layout(left, top, left + view.getWidth(), top + view.getHeight());
                }
                break;
            case MotionEvent.ACTION_UP:
                boolean moved = true;
                if (to.isWritableArea()) {
                    putProgramText(view, from, to);
                } else if (to.isDustboxArea()) {
                    removeProgramText(from);
                } else {
                    moved = false;
                }

                if (moved) {
                    moveCount++;
                }

                pushProgramTextsToLeft();
                setProgramIcons();
                setPiyoCode();
                showAnimation(view, moved);
                break;
        }
        return true;
    }

    private void putProgramText(View view, Location from, Location to) {
        if (cellIcons[to.row][to.column].getVisibility() == View.INVISIBLE) {
            return;
        }
        IconType iconType = IconType.getById(view.getId());
        if (iconType != null) {
            cellTexts[to.row][to.column] = iconType.text;
        } else if (from != null) {
            String tmp = cellTexts[from.row][from.column];
            cellTexts[from.row][from.column] = cellTexts[to.row][to.column];
            cellTexts[to.row][to.column] = tmp;
        }
    }

    private void removeProgramText(Location from) {
        if (from != null) {
            cellTexts[from.row][from.column] = "";
        }
    }

    private void pushProgramTextsToLeft() {
        for (int row = 0; row < cellTexts.length; row++) {
            for (int maxColumn = cellTexts[0].length; maxColumn >= 2; maxColumn--) {
                for (int column = 1; column < maxColumn; column++) {
                    if (!hasIcon(row, column - 1)) {
                        cellTexts[row][column - 1] = cellTexts[row][column];
                        cellTexts[row][column] = "";
                    }
                }
            }
        }
    }

    public void setProgramIcons() {
        for (int row = 0; row < cellTexts.length; row++) {
            for (int column = 0; column < cellTexts[0].length; column++) {
                IconType iconType = IconType.getByText(cellTexts[row][column]);
                if (iconType != null) {
                    cellIcons[row][column].setImageResource(iconType.drawable);
                } else if (column == 0 || hasIcon(row, column - 1)) {
                    cellIcons[row][column].setImageResource(R.drawable.icon_writable);
                } else {
                    cellIcons[row][column].setImageResource(R.drawable.icon_none);
                }
            }
        }
    }

    private void setPiyoCode() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < cellTexts.length; row++) {
            builder.append(Joiner.on(' ').join(cellTexts[row]).trim());
            builder.append('\n');
        }
        activity.getIntent().putExtra("piyoCode", builder.toString().trim());
    }

    public Location getCellLocation(View view) {
        int id = view.getId();
        for (int row = 0; row < cellTexts.length; row++) {
            for (int column = 0; column < cellTexts[0].length; column++) {
                int cellId = activity.getResources().getIdentifier("cell_" + row + "_" + column, "id", activity.getPackageName());
                if (id == cellId) {
                    return new Location(row, column);
                }
            }
        }
        return null;
    }

    private boolean hasIcon(int row, int column) {
        return !cellTexts[row][column].equals("");
    }

    private void showAnimation(final View view, boolean moved) {
        if (moved) {
            // 元の位置に戻す
            view.requestLayout();
            return;
        }
        Animation animation = new TranslateAnimation(
            0, initLeft - view.getLeft(), 0, initTop - view.getTop());
        animation.setDuration(500);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // ちらつき防止
                view.setAnimation(null);
                // 元の位置に戻す
                view.requestLayout();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public static int getMoveCount() {
        return moveCount;
    }

    public static void reset() {
        moveCount = 0;
    }

    private static class Location {
        public final int row;
        public final int column;

        public Location(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public boolean isWritableArea() {
            return 0 <= row && row <= 11 && 0 <= column && column <= 2;
        }

        public boolean isDustboxArea() {
            return 10 <= row && row <= 11 && 3 <= column && column <= 4;
        }
    }
}
