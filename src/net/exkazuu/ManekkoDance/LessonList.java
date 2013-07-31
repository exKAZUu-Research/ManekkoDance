package net.exkazuu.ManekkoDance;

import java.util.ArrayList;
import java.util.List;

import jp.eclipcebook.R;
import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class LessonList extends ListActivity {
	private MySpecialAdapter arrayadapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Lesson‘I‘ğ‰æ–Ê");
		List<String> lessonList = new ArrayList<String>(); // List‚Ìì¬•’Ç‰Á
		lessonList.add("Lesson‚P Šî–{“®ì1");
		lessonList.add("Lesson‚Q Šî–{“®ì2");
		lessonList.add("Lesson‚R Šî–{“®ì•œK");
		lessonList.add("Lesson‚S ‰—p“®ì1");
		lessonList.add("Lesson‚T ‰—p“®ì2");
		lessonList.add("Lesson‚U ‰—p“®ì3");
		lessonList.add("Lesson‚V ŒJ‚è•Ô‚µ1");
		lessonList.add("Lesson‚W ŒJ‚è•Ô‚µ2");
		lessonList.add("Lesson‚X ŒJ‚è•Ô‚µ3");
		lessonList.add("Lesson‚P‚O ŒJ‚è•Ô‚µ4");
		lessonList.add("Lesson‚P‚P ‚¨‚Ü‚¯1");
		lessonList.add("Lesson‚P‚Q ‚¨‚Ü‚¯2");
		lessonList.add("Lesson‚P‚R ‚¨‚Ü‚¯3");
		arrayadapter = new MySpecialAdapter(this,
				android.R.layout.simple_list_item_1, lessonList);
		this.setListAdapter(arrayadapter);
	}

	/************ List‚ª‘I‘ğ‚³‚ê‚½‚Ì“®ì **************/
	protected void onListItemClick(ListView l, View v, int position, long id) {
		/*
		 * SharedPreferences sp = PreferenceManager
		 * .getDefaultSharedPreferences(getApplication());
		 */
		MediaPlayer bgm = MediaPlayer
				.create(getApplicationContext(), R.raw.get);
		bgm.start();

		switch (position) {
		case 0:
			Intent intent0 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			// intent0.putExtra("lesson0", sp.getString("lesson0", null));
			intent0.putExtra("lesson", getText(R.string.answer_lesson1)); // ‚¨è–{‚Ì“š‚¦
			intent0.putExtra("message", "1");
			this.startActivity(intent0);
			break;
		case 1:
			Intent intent1 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent1.putExtra("lesson", getText(R.string.answer_lesson2));
			intent1.putExtra("message", "2");
			this.startActivity(intent1);
			break;
		case 2:
			Intent intent2 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent2.putExtra("lesson", getText(R.string.answer_lesson3));
			intent2.putExtra("message", "3");
			this.startActivity(intent2);
			break;
		case 3:
			Intent intent3 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent3.putExtra("lesson", getText(R.string.answer_lesson4));
			intent3.putExtra("message", "4");
			this.startActivity(intent3);
			break;

		case 4:
			Intent intent4 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent4.putExtra("lesson", getText(R.string.answer_lesson5));
			intent4.putExtra("message", "5");
			this.startActivity(intent4);
			break;

		case 5:
			Intent intent5 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent5.putExtra("lesson", getText(R.string.answer_lesson6));
			intent5.putExtra("message", "6");
			this.startActivity(intent5);
			break;

		case 6:
			Intent intent6 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent6.putExtra("lesson", getText(R.string.answer_lesson7));
			intent6.putExtra("message", "7");
			this.startActivity(intent6);
			break;

		case 7:
			Intent intent7 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent7.putExtra("lesson", getText(R.string.answer_lesson8));
			intent7.putExtra("message", "8");
			this.startActivity(intent7);
			break;

		case 8:
			Intent intent8 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent8.putExtra("lesson", getText(R.string.answer_lesson9));
			intent8.putExtra("message", "9");
			this.startActivity(intent8);
			break;

		case 9:
			Intent intent9 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent9.putExtra("lesson", getText(R.string.answer_lesson10));
			intent9.putExtra("message", "10");
			this.startActivity(intent9);
			break;

		case 10:
			Intent intent10 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent10.putExtra("lesson", getText(R.string.answer_lesson11));
			intent10.putExtra("message", "11");
			this.startActivity(intent10);
			break;

		case 11:
			Intent intent11 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent11.putExtra("lesson", getText(R.string.answer_lesson12));
			intent11.putExtra("message", "12");
			this.startActivity(intent11);
			break;

		case 12:
			Intent intent12 = new Intent(this,
					net.exkazuu.ManekkoDance.PartnerActivity.class);
			intent12.putExtra("lesson", getText(R.string.answer_lesson13));
			intent12.putExtra("message", "13");
			this.startActivity(intent12);
			break;

		default:
			break;
		}
		super.onListItemClick(l, v, position, id);
	}
}
