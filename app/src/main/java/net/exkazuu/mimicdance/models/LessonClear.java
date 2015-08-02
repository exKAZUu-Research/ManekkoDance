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

    @Column(name = "Sent")
    public boolean sent;

    public static void createAndSave(int lessonNumber) {
        LessonClear lessonClear = new LessonClear();
        List<PreQuestionnaireResult> pre = new Select().from(PreQuestionnaireResult.class).orderBy("Created_at DESC").limit(1).execute();
        List<PostQuestionnaireResult> post = new Select().from(PostQuestionnaireResult.class).orderBy("Created_at DESC").limit(1).execute();
        if (pre.size() == 1 && (post.size() == 0 || pre.get(0).examineeId != post.get(0).examineeId)) {
            lessonClear.examineeId = pre.get(0).examineeId;
        }
        lessonClear.lessonNumber = lessonNumber;
        lessonClear.milliseconds = Timer.stop();
        lessonClear.moveCount = DragViewListener.getMoveCount();
        lessonClear.save();
        DragViewListener.reset();
    }
}
