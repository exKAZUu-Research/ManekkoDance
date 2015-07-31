package net.exkazuu.mimicdance.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.exkazuu.mimicdance.CharacterImageViewSet;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.Timer;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

public class EvaluationActivity extends BaseActivity {

    private int lessonNumber;
    private String piyoCode;

    private UnrolledProgram piyoProgram;
    private UnrolledProgram altPiyoProgram;
    private UnrolledProgram coccoProgram;
    private UnrolledProgram altCoccoProgram;

    private CharacterImageViewSet piyoViewSet;
    private CharacterImageViewSet altPiyoViewSet;
    private CharacterImageViewSet coccoViewSet;
    private CharacterImageViewSet altCoccoViewSet;

    private Thread thread;
    private CommandExecutor commandExecutor;

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: is this necessary?
        if (commandExecutor != null) {
            commandExecutor.paused = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        initializeScreen();
        initializeComponents();
    }

    /**
     * Intentから送られてきたプログラムをロードしてフィールドに格納する
     */
    private void loadData() {
        Intent intent = getIntent();
        lessonNumber = intent.getIntExtra("lessonNumber", 1);
        piyoCode = intent.getStringExtra("piyoCode");
        String coccoCode = Lessons.getCoccoCode(lessonNumber);

        Block piyoBlock = CodeParser.parse(piyoCode);
        Block coccoBlock = CodeParser.parse(coccoCode);
        piyoProgram = piyoBlock.unroll(true);
        altPiyoProgram = piyoBlock.unroll(false);
        coccoProgram = coccoBlock.unroll(true);
        altCoccoProgram = coccoBlock.unroll(false);
    }

    private void initializeScreen() {
        setContentView(R.layout.evaluation);
        FrameLayout altPiyoFrame = (FrameLayout) findViewById(R.id.alt_piyo);
        FrameLayout altCoccoFrame = (FrameLayout) findViewById(R.id.alt_cocco);
        altPiyoFrame.setVisibility(View.GONE);
        altCoccoFrame.setVisibility(View.GONE);
    }

    private void initializeComponents() {
        TextView playerEditText = (TextView) findViewById(R.id.txtStringCode);
        playerEditText.setText(piyoCode);

        piyoViewSet = CharacterImageViewSet.createPiyoLeft(this);
        altPiyoViewSet = CharacterImageViewSet.createPiyoRight(this);
        coccoViewSet = CharacterImageViewSet.createCoccoLeft(this);
        altCoccoViewSet = CharacterImageViewSet.createCoccoRight(this);

        Button button = (Button) this.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Handler handler = new Handler();
                if (thread == null || !thread.isAlive()) {
                    commandExecutor = new CommandExecutor(handler);
                    thread = new Thread(commandExecutor);
                    thread.start();
                }
            }
        });
    }

    public void startCoccoActivity(View view) {
        startCoccoActivity(lessonNumber, piyoCode, true);
    }

    public void startHelpActivity(View view) {
        startHelpActivity(false);
    }

    public void startCodingActivity(View view) {
        startCodingActivity(lessonNumber, piyoCode, true);
    }

    public final class CommandExecutor implements Runnable {
        public static final int SLEEP_TIME = 500;
        private final Handler handler;
        private boolean paused;

        private CommandExecutor(Handler handler) {
            this.handler = handler;
            this.paused = false;
        }

        public void run() {
            TextView playerEditText = (TextView) findViewById(R.id.txtStringCode);

            Interpreter piyoExecutor = Interpreter.createForPiyo(
                piyoProgram, piyoViewSet, playerEditText, getApplicationContext());
            Interpreter altPiyoExecutor = Interpreter.createForPiyo(
                altPiyoProgram, altPiyoViewSet, playerEditText, getApplicationContext());
            Interpreter coccoExecutor = Interpreter.createForCocco(coccoProgram, coccoViewSet);
            Interpreter altCoccoExecutor = Interpreter.createForCocco(altCoccoProgram, altCoccoViewSet);

            // 解析&実行(白)
            if (Lessons.hasIf(lessonNumber)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView whiteOrYellow = (TextView) findViewById(R.id.white_or_orange);
                        whiteOrYellow.setText("しろいひよこのばあい");
                        whiteOrYellow.setTextColor(0xFF807700);
                    }
                });
            }
            dance(piyoExecutor, coccoExecutor);

            if (Lessons.hasIf(lessonNumber)) {
                //表示するキャラクターを変更
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView whiteOrYellow = (TextView) findViewById(R.id.white_or_orange);
                        whiteOrYellow.setText("きいろのひよこのばあい");
                        whiteOrYellow.setTextColor(0xFFFF3300);

                        FrameLayout altPiyoFrame = (FrameLayout) findViewById(R.id.alt_piyo);
                        FrameLayout altCoccoFrame = (FrameLayout) findViewById(R.id.alt_cocco);
                        FrameLayout piyoFrame = (FrameLayout) findViewById(R.id.piyo);
                        FrameLayout coccoFrame = (FrameLayout) findViewById(R.id.cocco);

                        altPiyoFrame.setVisibility(View.VISIBLE);
                        altCoccoFrame.setVisibility(View.VISIBLE);
                        piyoFrame.setVisibility(View.GONE);
                        coccoFrame.setVisibility(View.GONE);
                    }
                });

                try {
                    Thread.sleep(SLEEP_TIME * 2);
                } catch (InterruptedException e) {
                }

                //解析と実行(黄色)
                dance(altPiyoExecutor, altCoccoExecutor);
            }

            handler.post(new Runnable() {
                public void run() {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.alertdialog_layout));

                    AlertDialog.Builder builder = new AlertDialog.Builder(EvaluationActivity.this);
                    builder.setTitle("あなたのこたえは…？");
                    builder.setView(layout);
                    ImageView true_ans = (ImageView) layout.findViewById(R.id.ans_true);
                    ImageView false_ans = (ImageView) layout.findViewById(R.id.ans_false);
                    TextView congratulate = (TextView) layout.findViewById(R.id.congratulate);

                    if (piyoProgram.countDifferences(coccoProgram) + altPiyoProgram.countDifferences(altCoccoProgram) == 0) {
                        false_ans.setVisibility(View.GONE);
                        if (lessonNumber == Lessons.getLessonCount()) {
                            builder.setNegativeButton("タイトルへもどる",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                        DialogInterface dialog,
                                        int which) {
                                        startTitleActivity(true);
                                    }
                                });
                        } else {
                            Timer.stop();
                            builder.setNegativeButton("つぎのレッスンにすすむ",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startCoccoActivity(
                                            Math.min(lessonNumber + 1, Lessons.getLessonCount()), "", true);
                                    }
                                });
                        }
                    } else {
                        int diffCount = piyoProgram.countDifferences(coccoProgram);
                        int size = coccoProgram.size();
                        if (Lessons.hasIf(lessonNumber)) {
                            diffCount += altPiyoProgram.countDifferences(altCoccoProgram);
                            size += altCoccoProgram.size();
                        }
                        boolean almostCorrect = diffCount <= size / 3;
                        if (almostCorrect) {
                            builder.setTitle("おしい！！！ " + diffCount + "コまちがっているよ");
                        } else {
                            builder.setTitle(diffCount + "コまちがっているよ");
                        }
                        true_ans.setVisibility(View.GONE);
                        congratulate.setVisibility(View.GONE);
                        builder.setPositiveButton("もういちどチャレンジ",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startCodingActivity(lessonNumber, piyoCode, true);
                                }
                            });
                        builder.setNegativeButton("べつのレッスンをえらぶ",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startLessonListActivity(true);
                                }
                            });
                    }
                    builder.show();
                }
            });
        }

        private void dance(Interpreter piyoExecutor, Interpreter coccoExecutor) {
            while (!paused && !(piyoExecutor.finished() && coccoExecutor.finished())) {
                for (int j = 0; j < 2; j++) {
                    handler.post(piyoExecutor);
                    handler.post(coccoExecutor);

                    try { /* 1秒待機 */
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (piyoExecutor.existsError()) {
                    break;
                }
            }
            coccoExecutor.finish();
            piyoExecutor.finish();
        }
    }
}
