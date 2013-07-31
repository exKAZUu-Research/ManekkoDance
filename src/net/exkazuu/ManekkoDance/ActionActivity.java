package net.exkazuu.ManekkoDance;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.eclipcebook.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

//import android.media.MediaPlayer;

public class ActionActivity extends Activity {

	private String path = "mydata2.txt";
	private String lesson;
	private String message;
	private String text_data;
	public boolean answerCheckEnd = false;

	// private MediaPlayer bgm;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("実行画面");
		setContentView(R.layout.action_screen);
		// doLoad();

		TextView playerEditText = (TextView) findViewById(R.id.editTextActionScreen1);
		TextView partnerEditText = (TextView) findViewById(R.id.editTextActionScreen2);
		Intent intent = getIntent();
		lesson = intent.getStringExtra("lesson");
		message = intent.getStringExtra("message");
		text_data = intent.getStringExtra("text_data");
		String playerCommandsText = text_data;
		String partnerCommandsText = lesson;

		playerEditText.setText(playerCommandsText);
		partnerEditText.setText(partnerCommandsText);

		Button btn1 = (Button) this.findViewById(R.id.button1);
		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Handler handler = new Handler();
				Thread trd = new Thread(new CommandExecutor(handler));
				trd.start();
			}
		});

	}

	public final class CommandExecutor implements Runnable {
		private final Handler handler;

		private CommandExecutor(Handler handler) {
			this.handler = handler;
		}

		public void run() {
			ImageView playerImage1 = (ImageView) findViewById(R.id.playerLeftHand1);
			ImageView playerImage4 = (ImageView) findViewById(R.id.playerRightHand1);
			ImageView playerImage7 = (ImageView) findViewById(R.id.playerBasic);
			ImageView playerImage8 = (ImageView) findViewById(R.id.playerLeftFoot1);
			ImageView playerImage11 = (ImageView) findViewById(R.id.playerRightFoot1);

			ImageView partnerImage1 = (ImageView) findViewById(R.id.partnerLeftHand1);
			ImageView partnerImage4 = (ImageView) findViewById(R.id.partnerRightHand1);
			ImageView partnerImage7 = (ImageView) findViewById(R.id.partnerBasic);
			ImageView partnerImage8 = (ImageView) findViewById(R.id.partnerLeftFoot1);
			ImageView partnerImage11 = (ImageView) findViewById(R.id.partnerRightFoot1);

			TextView playerEditText = (TextView) findViewById(R.id.editTextActionScreen1);
			TextView partnerEditText = (TextView) findViewById(R.id.editTextActionScreen2);

			String playerCommandsText = playerEditText.getText().toString();
			String partnerCommandsText = partnerEditText.getText().toString();
			List<Integer> playerNumberSorting = new ArrayList<Integer>();
			List<String> playerCommands = new ArrayList<String>();
			List<Integer> partnerNumberSorting = new ArrayList<Integer>();
			List<String> partnerCommands = new ArrayList<String>();

			final AnswerCheck answer;

			StringCommandParser.parse(playerCommandsText, playerNumberSorting,
					playerCommands);
			StringCommandParser.parse(partnerCommandsText,
					partnerNumberSorting, partnerCommands);

			Runnable playerAction = new StringCommandExecutor(
					new ImageContainer(playerImage1, playerImage4,
							playerImage7, playerImage8, playerImage11),
					playerCommands, playerEditText, playerNumberSorting);
			Runnable partnerAction = new StringCommandExecutor(
					new ImageContainer(partnerImage1, partnerImage4,
							partnerImage7, partnerImage8, partnerImage11),
					partnerCommands);

			answer = new AnswerCheck(playerCommands, partnerCommands);
			answer.compare();
			answer.loopCheck(message, playerEditText);
			Log.v("tag", answer.show());
			// 解析&実行
			for (int i = 0; i < Math.max(playerCommands.size(),
					partnerCommands.size()); i++) {

				if (i < playerCommands.size())
					handler.post(playerAction); /* 光らせる */
				if (i < partnerCommands.size())
					handler.post(partnerAction);

				try { /* 1秒待機 */
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (i < playerCommands.size())
					handler.post(playerAction);
				if (i < partnerCommands.size())
					handler.post(partnerAction);
				try { /* 1秒待機 */
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (StringCommandExecutor.errorCheck)
					break;
			}

			handler.post(new Runnable() {
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ActionActivity.this);
					builder.setTitle(" ");
					if (answer.judge) {

						if (Integer.parseInt(message) == 10) {
							// bgm = MediaPlayer.create(getApplicationContext(),
							// R.raw.perfect);
							// bgm.start();
							builder.setIcon(R.drawable.answer_ture);
							builder.setNegativeButton("タイトルへ戻る",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// bgm =
											// MediaPlayer.create(getApplicationContext(),
											// R.raw.select);
											// bgm.start();
											Intent intent = new Intent(
													getApplication(),
													net.exkazuu.ManekkoDance.TitleActivity.class);
											startActivity(intent);
										}
									});

							builder.setPositiveButton("もう一度Challenge",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// bgm =
											// MediaPlayer.create(getApplicationContext(),
											// R.raw.select);
											// bgm.start();
											changeMainScreen();
										}
									});
						} else {
							// bgm = MediaPlayer.create(getApplicationContext(),
							// R.raw.good_answer);
							// bgm.start();

							builder.setIcon(R.drawable.answer_ture);
							Timer.stopTimer();
							builder.setNegativeButton("次のLessonに進む",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// bgm =
											// MediaPlayer.create(getApplicationContext(),
											// R.raw.select);
											// bgm.start();
											Intent intent = new Intent(
													getApplication(),
													net.exkazuu.ManekkoDance.PartnerActivity.class);
											int nextLessonNumber = Integer
													.parseInt(message) + 1;
											message = String
													.valueOf(nextLessonNumber);
											intent.putExtra("message", message);
											String str = LessonData
													.getLessonData(nextLessonNumber);
											lesson = str;
											intent.putExtra("lesson", lesson);
											startActivity(intent);
										}
									});

							builder.setNeutralButton(Timer.getResultTime(),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}

									);

							builder.setPositiveButton("もう一度Challenge",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// bgm =
											// MediaPlayer.create(getApplicationContext(),
											// R.raw.select);
											// bgm.start();
											changeMainScreen();
										}
									});
						}

					} else {
						// bgm = MediaPlayer.create(getApplicationContext(),
						// R.raw.fail);
						// bgm.start();
						builder.setIcon(R.drawable.answer_false);
						builder.setNegativeButton("Lessonを選択し直す",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// bgm =
										// MediaPlayer.create(getApplicationContext(),
										// R.raw.select);
										// bgm.start();
										Intent intent = new Intent(
												getApplication(),
												net.exkazuu.ManekkoDance.LessonList.class);
										startActivity(intent);
									}
								});

						builder.setPositiveButton("もう一度Challenge",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// bgm =
										// MediaPlayer.create(getApplicationContext(),
										// R.raw.select);
										// bgm.start();
										changeMainScreen();
									}
								});
					}

					builder.show();
				}
			});

		}

	}

	@SuppressLint("WorldReadableFiles")
	public void doSave() {
		EditText editText2 = (EditText) this
				.findViewById(R.id.editTextActionScreen2);
		Editable str = editText2.getText();
		FileOutputStream output = null;
		try {
			output = this.openFileOutput(path, Context.MODE_WORLD_READABLE);
			output.write(str.toString().getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// public void doLoad() {
	// EditText editText2 = (EditText)
	// this.findViewById(R.id.editTextActionScreen2);
	// FileInputStream input = null;
	// try {
	// input = this.openFileInput(path);
	// byte[] buffer = new byte[1000];
	// input.read(buffer);
	// String s = new String(buffer).trim();
	// editText2.setText(s);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// input.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		MenuItem item1 = menu.add("編集画面へ戻る");
		MenuItem item2 = menu.add("タイトルへ戻る");

		item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				changeMainScreen();
				return false;
			}
		});
		item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				doSave();
				changeTitleScreen();
				return false;
			}
		});
		return true;
	}

	public void changeMainScreen(View view) {
		Intent intent = new Intent(this, net.exkazuu.ManekkoDance.MainActivity.class);
		TextView playerEditText = (TextView) findViewById(R.id.editTextActionScreen1);
		TextView partnerEditText = (TextView) findViewById(R.id.editTextActionScreen2);
		intent.putExtra("text_data", playerEditText.getText().toString());
		intent.putExtra("lesson", partnerEditText.getText().toString());
		intent.putExtra("message", message);
		this.startActivity(intent);
	}

	private void changeMainScreen() {
		Intent intent = new Intent(this, net.exkazuu.ManekkoDance.MainActivity.class);
		TextView playerEditText = (TextView) findViewById(R.id.editTextActionScreen1);
		TextView partnerEditText = (TextView) findViewById(R.id.editTextActionScreen2);
		intent.putExtra("text_data", playerEditText.getText().toString());
		intent.putExtra("lesson", partnerEditText.getText().toString());
		intent.putExtra("message", message);
		this.startActivity(intent);
	}

	public void changePartnerScreen(View view) {
		Intent intent = new Intent(this, net.exkazuu.ManekkoDance.PartnerActivity.class);
		TextView playerEditText = (TextView) findViewById(R.id.editTextActionScreen1);
		TextView partnerEditText = (TextView) findViewById(R.id.editTextActionScreen2);
		intent.putExtra("text_data", playerEditText.getText().toString());
		intent.putExtra("lesson", partnerEditText.getText().toString());
		intent.putExtra("message", message);
		this.startActivity(intent);
	}

	private void changeTitleScreen() {
		Intent intent = new Intent(this, net.exkazuu.ManekkoDance.TitleActivity.class);
		this.startActivity(intent);
	}

	public void changeHelpScreen(View view) { // ヘルプ画面へ遷移
		// bgm = MediaPlayer.create(getApplicationContext(), R.raw.select);
		// bgm.start();
		Intent intent = new Intent(this, net.exkazuu.ManekkoDance.Help.class);
		intent.putExtra("lesson", lesson);
		intent.putExtra("message", message);
		intent.putExtra("text_data", text_data);
		this.startActivity(intent);
	}

	protected void onDestroy() {
		super.onDestroy();
		cleanupView(findViewById(R.id.actionRoot));
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
