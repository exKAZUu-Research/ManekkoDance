package net.exkazuu.mimicdance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.exkazuu.mimicdance.CharacterImageViewSet;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.Timer;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

public class CoccoActivity extends BaseActivity {

    private int lessonNumber;
    private String piyoCode;
    private UnrolledProgram coccoProgram;
    private UnrolledProgram altCoccoProgram;

    private Thread thread;
    private CharacterImageViewSet coccoViewSet;
    private CharacterImageViewSet altCoccoViewSet;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timer.start();

        loadData();
        initializeScreen();
        initializeComponents();
    }

    private void loadData() {
        Intent intent = getIntent();
        lessonNumber = intent.getIntExtra("lessonNumber", 1);
        piyoCode = intent.getStringExtra("piyoCode");
        String coccoCode = Lessons.getCoccoCode(lessonNumber);
        Block coccoBlock = CodeParser.parse(coccoCode);
        coccoProgram = coccoBlock.unroll(true);
        altCoccoProgram = coccoBlock.unroll(false);
    }

    private void initializeScreen() {
        setContentView(R.layout.cocco);

        ImageView messageImageView = (ImageView) findViewById(R.id.messageImageView);
        int drawableId = getResources().getIdentifier("lesson_message" + lessonNumber, "drawable", getPackageName());
        messageImageView.setImageResource(drawableId);

        if (lessonNumber <= 4) {
            FrameLayout altCocco = (FrameLayout) findViewById(R.id.alt_cocco);
            altCocco.setVisibility(View.GONE);
        }
    }

    private void initializeComponents() {
        coccoViewSet = CharacterImageViewSet.createCoccoLeft(this);
        altCoccoViewSet = CharacterImageViewSet.createCoccoRight(this);

        Button btnShowCocco = (Button) this.findViewById(R.id.btnShowCocco);
        btnShowCocco.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (thread == null || !thread.isAlive()) {
                    thread = new Thread(new CommandExecutor(new Handler()));
                    thread.start();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add("やりなおす").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startCodingActivity(null);
                return false;
            }
        });

        menu.add("タイトルにもどる").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startTitleActivity(true);
                return false;
            }
        });
        return true;
    }

    public void startCodingActivity(View view) {
        startCodingActivity(lessonNumber, piyoCode, true);
    }

    private final class CommandExecutor implements Runnable {
        private static final int SLEEP_TIME = 300;
        private final Handler handler;

        private CommandExecutor(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            Interpreter coccoExecutor = Interpreter.createForCocco(coccoProgram, coccoViewSet);
            Interpreter altCoccoExecutor = Interpreter.createForCocco(altCoccoProgram, altCoccoViewSet);
            while (!(coccoExecutor.finished() && altCoccoExecutor.finished())) {
                // アニメーションは2コマから構成
                for (int j = 0; j < 2; j++) {
                    handler.post(coccoExecutor);
                    handler.post(altCoccoExecutor);
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                    }
                }
            }
            handler.post(coccoExecutor);
            handler.post(altCoccoExecutor);
        }
    }
}
