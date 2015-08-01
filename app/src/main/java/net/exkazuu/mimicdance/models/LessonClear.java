package net.exkazuu.mimicdance.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import net.exkazuu.mimicdance.Timer;
import net.exkazuu.mimicdance.activities.DragViewListener;

import java.util.Date;
import java.util.List;

@Table(name = "LessonClear")
public class LessonClear extends Model {
    @Column(name = "Created_at")
    public Date created_at = new Date(System.currentTimeMillis());

    @Column(name = "ExamineeId")
    public String examineeId;

    @Column(name = "LessonNumber")
    public int lessonNumber;

    @Column(name = "Milliseconds")
    public long milliseconds;

    @Column(name = "MoveCount")
    public int moveCount;

    public static void createAndSave(int lessonNumber) {
        LessonClear lessonClear = new LessonClear();
        List<PreQuestionnaireResult> pre = new Select().from(PreQuestionnaireResult.class).orderBy("Created_at DESC").limit(1).execute();
        if (pre.size() == 1) {
            boolean isCelared = new Select().from(LessonClear.class)
                .where("ExamineeId = ?", pre.get(0).examineeId)
                .where("LessonNumber = ?", lessonNumber).limit(1).execute().size() > 0;
            if (!isCelared) {
                lessonClear.examineeId = pre.get(0).examineeId;
                lessonClear.lessonNumber = lessonNumber;
                lessonClear.milliseconds = Timer.stop();
                lessonClear.moveCount = DragViewListener.getMoveCount();
                lessonClear.save();
            }
        }
        DragViewListener.reset();
    }
}
