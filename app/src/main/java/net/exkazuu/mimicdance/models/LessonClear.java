package net.exkazuu.mimicdance.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "LessonClear")
public class LessonClear extends Model {
    @Column(name = "Created_at")
    public Date created_at = new Date(System.currentTimeMillis());

    @Column(name = "LessonNumber")
    public int lessonNumber;

    @Column(name = "Milliseconds")
    public long milliseconds;

    @Column(name = "MoveCount")
    public int moveCount;
}

