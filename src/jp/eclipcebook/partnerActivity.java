package jp.eclipcebook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PartnerActivity extends Activity {

	private String path = "mydata2.txt"; // file�ۑ�

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE); //�^�C�g���o�[��\��
		setTitle("����{���");
		setContentView(R.layout.partner);
		//doLoad();

		TextView editText1 = (TextView)findViewById(R.id.editText1);
		TextView editText2 = (TextView)findViewById(R.id.editText2);
		ImageView messageImageView1 = (ImageView)findViewById(R.id.imageView2);
		Intent intent = getIntent();
		String data = intent.getStringExtra("lesson");
		String message = intent.getStringExtra("message");
		editText1.setText(data);
		editText2.setText(message);
		if(message.equals("lesson1")) {
			messageImageView1.setImageResource(R.drawable.lesson_message1);
		}
		else if(message.equals("lesson2")) {
			messageImageView1.setImageResource(R.drawable.lesson_message2);
		}
		else if(message.equals("lesson3")) {
			messageImageView1.setImageResource(R.drawable.lesson_message3);
		}
		else if(message.equals("lesson4")) {
			messageImageView1.setImageResource(R.drawable.lesson_message4);
		}

		Button btn5 = (Button) this.findViewById(R.id.button5);
		btn5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Handler handler = new Handler();
				Thread trd = new Thread(new CommandExecutor(handler));
				trd.start();
			}
		});
	}

	private final class CommandExecutor implements Runnable {
		private final Handler handler;

		private CommandExecutor(Handler handler) {
			this.handler = handler;
		}

		public void run() {
			TextView editText1 = (TextView) findViewById(R.id.editText1);
			ImageView leftHand1 = (ImageView) findViewById(R.id.partnerLeftHand1);
			ImageView leftHand2 = (ImageView) findViewById(R.id.partnerLeftHand2);
			ImageView leftHand3 = (ImageView) findViewById(R.id.partnerLeftHand3);
			ImageView rightHand1 = (ImageView) findViewById(R.id.partnerRightHand1);
			ImageView rightHand2 = (ImageView) findViewById(R.id.partnerRightHand2);
			ImageView rightHand3 = (ImageView) findViewById(R.id.partnerRightHand3);
			ImageView basic = (ImageView) findViewById(R.id.partnerBasic);
			ImageView leftFoot1 = (ImageView) findViewById(R.id.partnerLeftFoot1);
			ImageView leftFoot2 = (ImageView) findViewById(R.id.partnerLeftFoot2);
			ImageView leftFoot3 = (ImageView) findViewById(R.id.partnerLeftFoot3);
			ImageView rightFoot1 = (ImageView) findViewById(R.id.partnerRightFoot1);
			ImageView rightFoot2 = (ImageView) findViewById(R.id.partnerRightFoot2);
			ImageView rightFoot3 = (ImageView) findViewById(R.id.partnerRightFoot3);
			String commandsText = editText1.getText().toString();

			List<String> commands = StringCommandParser.parse(commandsText);

			executeCommands(leftHand1, leftHand2, leftHand3, rightHand1,
					rightHand2, rightHand3, basic, leftFoot1, leftFoot2,
					leftFoot3, rightFoot1, rightFoot2, rightFoot3, commands);
		}

		private void executeCommands(ImageView lh1, ImageView lh2,
				ImageView lh3, ImageView rh1, ImageView rh2, ImageView rh3,
				ImageView basic, ImageView lf1, ImageView lf2, ImageView lf3,
				ImageView rf1, ImageView rf2, ImageView rf3,
				List<String> commands) {
			Runnable runnable = new StringCommandExecutor(lh1, lh2, lh3, rh1,
					rh2, rh3, basic, lf1, lf2, lf3, rf1, rf2, rf3, commands);
			for (int i = 0; i < commands.size(); i++) { /* ���&���s */
				handler.post(runnable); /* ���点�� */

				try { /* 1�b�ҋ@ */
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				handler.post(runnable); /* ���点�� */

				try { /* 1�b�ҋ@ */
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void doActionLeftHandUp(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("���r���グ��");
	}

	public void doActionLeftHandDown(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("���r��������");
	}

	public void doActionRightHandUp(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("�E�r���グ��");
	}

	public void doActionRightHandDown(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("�E�r��������");
	}

	public void doActionLeftFootUp(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("�������グ��");
	}

	public void doActionLeftFootDown(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("������������");
	}

	public void doActionRightFootUp(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("�E�����グ��");
	}

	public void doActionRightFootDown(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("�E����������");
	}

	public void doActionJump(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("�W�����v����");
	}

	public void doActionEnter(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("\n");
	}

	public void doActionLoop(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("loop");
	}

	public void doActionKoko(View view) {
		TextView editText1 = (TextView) this.findViewById(R.id.editText1);
		editText1.append("�����܂�");
	}

	/******************** �t�@�C���ۑ� doSave(View view) *************************/
	@SuppressLint("WorldReadableFiles")
	public void doSave() {
		EditText editText1 = (EditText) this.findViewById(R.id.editText1);
		Editable str = editText1.getText();
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

	/******************** �t�@�C�����[�h doLoad(View view) *************************/
	public void doLoad() {
		EditText editText1 = (EditText) this.findViewById(R.id.editText1);
		FileInputStream input = null;
		try {
			input = this.openFileInput(path);
			byte[] buffer = new byte[1000];
			input.read(buffer);
			String s = new String(buffer).trim();
			editText1.setText(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		final Activity activity = this;
		MenuItem item1 = menu.add("���s");
		MenuItem item2 = menu.add("�N���A");
		MenuItem item3 = menu.add("�v���C���[");

		item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				Toast toast = Toast.makeText(activity, "This is Menu1",
						Toast.LENGTH_SHORT);
				toast.show();
				return false;
			}
		});
		item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				TextView editText1 = (TextView) findViewById(R.id.editText1);
				editText1.getEditableText().clear();
				return false;
			}
		});
		item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				doSave();
				changeScreen();
				return false;
			}
		});
		return true;
	}

	private void changeScreen() {
		Intent intent = new Intent(getApplication(),
				jp.eclipcebook.MainActivity.class);
		TextView editText1 = (TextView)findViewById(R.id.editText1);
		TextView editText2 = (TextView)findViewById(R.id.editText2);
		intent.putExtra("lesson", editText1.getText().toString());
		intent.putExtra("message", editText2.getText().toString());
		this.startActivity(intent);
	}
}