// Todoリスト
/* キーボードを出した時の画像潰れ解消法　http://d.hatena.ne.jp/Superdry/20110715/1310754502 */
/* フラグメントによるTabの実装 */
/* 絵文字の実装 */

package jp.eclipcebook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends Activity {

	// private String path = "mydata.txt"; // file保存
	private String lesson;
	private String message;
	private String text_data;

	private ImageInEdit mImageEdit;
	private DetectableSoftKeyLayout DSKLayout;
	private FrameLayout fLayout;
	private LinearLayout buttonGroup2;
	private HorizontalScrollView iconList;
	private TextView editText1;
	private MediaPlayer bgm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("プレイヤー画面");
		setContentView(R.layout.main);

		editText1 = (TextView) this.findViewById(R.id.editText1);
		mImageEdit = (ImageInEdit) findViewById(R.id.imageInEdit);
		mImageEdit.buildLayer();
		DSKLayout = (DetectableSoftKeyLayout) findViewById(R.id.root);
		DSKLayout.setListener(listner);
		fLayout = (FrameLayout) findViewById(R.id.frameLayout_piyo);
		buttonGroup2 = (LinearLayout) findViewById(R.id.buttonGroup2);
		iconList = (HorizontalScrollView) findViewById(R.id.iconList);

		/******************* tabの実装と切り替え *****************/

		TabHost host = (TabHost) findViewById(R.id.tabhost);
		host.setup();

		TabSpec tab1 = host.newTabSpec("tab1");
		tab1.setIndicator("文字");
		tab1.setContent(R.id.tab1);
		host.addTab(tab1);

		TabSpec tab2 = host.newTabSpec("tab2");
		tab2.setIndicator("絵文字");
		tab2.setContent(R.id.tab2);
		host.addTab(tab2);

		final InterconversionStringAndImage isa = new InterconversionStringAndImage();

		host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				iconContainer icon = new iconContainer(getApplication());
				if (tabId == "tab2") {
					mImageEdit.setText(editText1.getText().toString());
					mImageEdit.replaceTextToImage(icon);

				} else if (tabId == "tab1") {
					String str = mImageEdit.getText().toString();
					str = isa.convertImageToString(str);
					editText1.setText(str);
					Log.v("文字", str);
				}
			}
		});


		/********** 音楽 **************/
		// MediaPlayer bgm1 = MediaPlayer.create(this, R.raw.ikusei_gamen); //
		// ゲーム音楽
		// bgm1.start(); // BGMスタート

		// doLoad(); // セーブデータをロード

		/********** Lesson data　の 取得 **************/
		Intent intent = getIntent();
		lesson = intent.getStringExtra("lesson");
		message = intent.getStringExtra("message");
		text_data = intent.getStringExtra("text_data");
		editText1 = (TextView) findViewById(R.id.editText1);
		editText1.setText(text_data);

		FrameLayout actionButton = (FrameLayout) this.findViewById(R.id.frameLayout_piyo);
		actionButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Handler handler = new Handler();
				Thread trd = new Thread(new CommandExecutor(handler));
				trd.start();
			}
		});

	}

	/******************** 構文解析＆実行 *************************/
	public final class CommandExecutor implements Runnable {
		private final Handler handler;

		private CommandExecutor(Handler handler) {
			this.handler = handler;
		}

		public void run() {
			editText1 = (TextView)findViewById(R.id.editText1);
			ImageView leftHand1 = (ImageView) findViewById(R.id.playerLeftHand1);
			ImageView rightHand1 = (ImageView) findViewById(R.id.playerRightHand1);
			ImageView basic = (ImageView) findViewById(R.id.playerBasic);
			ImageView leftFoot1 = (ImageView) findViewById(R.id.playerLeftFoot1);
			ImageView rightFoot1 = (ImageView) findViewById(R.id.playerRightFoot1);

			String commandsText = editText1.getText().toString(); // 1行ずつ配列に収納
			List<Integer> numberSorting = new ArrayList<Integer>();
			List<String> commands = new ArrayList<String>();
			StringCommandParser.parse(commandsText, numberSorting, commands);
			executeCommands(
					new ImageContainer(leftHand1, rightHand1, basic, leftFoot1, rightFoot1),
					commands, editText1, numberSorting);
		}

		private void executeCommands(ImageContainer images, List<String> expandedCommands,
				TextView editText1, List<Integer> numberSorting) {
			Runnable runnable = new StringCommandExecutor(images, expandedCommands, editText1,
					numberSorting);
			for (int i = 0; i < expandedCommands.size(); i++) { /* 解析&実行 */
				handler.post(runnable); /* 光らせる */

				try { /* 1秒待機 */
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				handler.post(runnable);

				try { /* 1秒待機 */
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (StringCommandExecutor.errorCheck) // for文から抜けて画像を初期化
					break;
			}

			if (StringCommandExecutor.errorCheck) {
				handler.post(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setTitle("間違った文を書いているよ");
						builder.setPositiveButton("もう一度Challenge",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										initializeImage();
									}
								});
						builder.show();
					}
				});
			} else {
				handler.post(new Runnable() {
					public void run() {
						initializeImage();
					}
				});
			}
		}

	}

	DetectableSoftKeyLayout.OnSoftKeyShownListener listner = new DetectableSoftKeyLayout.OnSoftKeyShownListener() {
		@Override
		public void onSoftKeyShown(boolean isShown) {
			if (isShown) {
				// ソフトキーボードが表示されている場合
				// postボタンを非表示にする
				fLayout.setVisibility(View.GONE);
				buttonGroup2.setVisibility(View.GONE);
				iconList.setVisibility(View.VISIBLE);
			} else {
				// ソフトキーボードが表示されてなければ、表示する
				fLayout.setVisibility(View.VISIBLE);
				buttonGroup2.setVisibility(View.VISIBLE);
				iconList.setVisibility(View.GONE);
			}
		}
	};

	/******************** ボタン(絵文字)の処理 *************************/
	public void doActionLeftHandUp(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace(Math.min(start, end), Math.max(start, end), "左腕を上げる");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_left_hand_up);
	}

	public void doActionLeftHandDown(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace(Math.min(start, end), Math.max(start, end), "左腕を下げる");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_left_hand_down);
	}

	public void doActionRightHandUp(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace(Math.min(start, end), Math.max(start, end), "右腕を上げる");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_right_hand_up);
	}

	public void doActionRightHandDown(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace(Math.min(start, end), Math.max(start, end), "右腕を下げる");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_right_hand_down);

	}

	public void doActionLeftFootUp(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace(Math.min(start, end), Math.max(start, end), "左足を上げる");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_left_foot_up);
	}

	public void doActionLeftFootDown(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace(Math.min(start, end), Math.max(start, end), "左足を下げる");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_left_foot_down);
	}

	public void doActionRightFootUp(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace(Math.min(start, end), Math.max(start, end), "右足を上げる");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_right_foot_up);
	}

	public void doActionRightFootDown(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace(Math.min(start, end), Math.max(start, end), "右足を下げる");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_right_foot_down);
	}

	public void doActionJump(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace(Math.min(start, end), Math.max(start, end), "ジャンプする");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_jump);
	}

	public void doActionLoop(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace( Math.min( start, end ), Math.max( start, end ), "loop");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_loop);
	}

	public void doActionKoko(View view) {
		editText1 = (TextView) this.findViewById(R.id.editText1);
		int start = editText1.getSelectionStart();
		int end = editText1.getSelectionEnd();
		Editable editable = (Editable) editText1.getText();
		editable.replace( Math.min( start, end ), Math.max( start, end ), "ここまで");
		// mImageEdit.insertResourceImage(MainActivity.this,
		// R.drawable.icon_kokomade);

	}


	public void initializeImage() {
		ImageView leftHand1 = (ImageView) findViewById(R.id.playerLeftHand1);
		ImageView rightHand1 = (ImageView) findViewById(R.id.playerRightHand1);
		ImageView basic = (ImageView) findViewById(R.id.playerBasic);
		ImageView leftFoot1 = (ImageView) findViewById(R.id.playerLeftFoot1);
		ImageView rightFoot1 = (ImageView) findViewById(R.id.playerRightFoot1);
		leftHand1.setImageResource(R.drawable.piyo_left_hand_up1);
		rightHand1.setImageResource(R.drawable.piyo_right_hand_up1);
		basic.setImageResource(R.drawable.piyo_basic);
		leftFoot1.setImageResource(R.drawable.piyo_left_foot_up1);
		rightFoot1.setImageResource(R.drawable.piyo_right_foot_up1);
		leftHand1.setVisibility(View.VISIBLE);
		rightHand1.setVisibility(View.VISIBLE);
		leftFoot1.setVisibility(View.VISIBLE);
		rightFoot1.setVisibility(View.VISIBLE);
	}

	/******************** ファイル保存 doSave(View view) *************************/
	/*
	 * @SuppressLint("WorldReadableFiles") public void doSave() { EditText
	 * editText1 = (EditText) this.findViewById(R.id.editText1); Editable str =
	 * editText1.getText(); FileOutputStream output = null; try { output =
	 * this.openFileOutput(path, Context.MODE_WORLD_READABLE);
	 * output.write(str.toString().getBytes()); } catch (FileNotFoundException
	 * e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace();
	 * } finally { try { output.close(); } catch (Exception e) {
	 * e.printStackTrace(); } } }
	 */

	/******************** ファイルロード doLoad(View view) *************************/
	/*
	 * public void doLoad() { EditText editText1 = (EditText)
	 * this.findViewById(R.id.editText1); FileInputStream input = null; try {
	 * input = this.openFileInput(path); byte[] buffer = new byte[1000];
	 * input.read(buffer); String s = new String(buffer).trim();
	 * editText1.setText(s); } catch (FileNotFoundException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
	 * finally { try { input.close(); } catch (Exception e) {
	 * e.printStackTrace(); } } }
	 */

	/******************** メニュー作成 *************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		MenuItem item1 = menu.add("ヘルプ");
		MenuItem item2 = menu.add("クリア");
		MenuItem item3 = menu.add("タイトル画面へ戻る");

		item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				changeHelpScreen();
				return false;
			}
		});
		item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				editText1.getEditableText().clear();
				return false;
			}
		});
		 item3.setOnMenuItemClickListener(new
		 MenuItem.OnMenuItemClickListener() {
		 public boolean onMenuItemClick(MenuItem item) {
				changeTitleScreen();
		 return false;
		 }
		 });

		return true;
	}

	/************************* インテント（画面遷移） *****************************/
	public void changeActionScreen(View view) {
		bgm = MediaPlayer.create(getApplicationContext(), R.raw.select);
		bgm.start();
		Intent intent = new Intent(this, jp.eclipcebook.ActionActivity.class);
		intent.putExtra("text_data", editText1.getText().toString());
		intent.putExtra("lesson", lesson);
		intent.putExtra("message", message);
		this.startActivity(intent);
	}

	public void changePartnerScreen(View view) { // お手本画面へ遷移
		bgm = MediaPlayer.create(getApplicationContext(), R.raw.select);
		bgm.start();
		Intent intent = new Intent(this, jp.eclipcebook.PartnerActivity.class);
		intent.putExtra("lesson", lesson);
		intent.putExtra("message", message);
		intent.putExtra("text_data", editText1.getText().toString());
		this.startActivity(intent);
	}
	
	public void changeHelpScreen() { // お手本画面へ遷移
		bgm = MediaPlayer.create(getApplicationContext(), R.raw.select);
		bgm.start();
		Intent intent = new Intent(this, jp.eclipcebook.Help.class);
		intent.putExtra("lesson", lesson);
		intent.putExtra("message", message);
		intent.putExtra("text_data", editText1.getText().toString());
		this.startActivity(intent);
	}

	private void changeTitleScreen() {
		Intent intent = new Intent(this, jp.eclipcebook.TitleActivity.class);
		this.startActivity(intent);
	}

	protected void onDestroy() {
		super.onDestroy();
		cleanupView(findViewById(R.id.root));
		System.gc();
	}

	public static final void cleanupView(View view) {
		if (view instanceof ImageButton) {
			ImageButton ib = (ImageButton) view;
			ib.setImageDrawable(null);
		} else if (view instanceof ImageView) {
			ImageView iv = (ImageView) view;
			iv.setImageDrawable(null);
			// } else if(view instanceof(XXX)) {
			// 他にもDrawableを使用する対象があればここで中身をnullに
		}
		view.setBackgroundDrawable(null);
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			int size = vg.getChildCount();
			for (int i = 0; i < size; i++) {
				cleanupView(vg.getChildAt(i));
			}
		}
	}

}
