package net.exkazuu.mimicdance.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by t-yokoi on 2015/08/01.
 */
@Table(name = "PostQuestionnaireResult")
public class PostQuestionnaireResult extends Model{
    @Column(name = "ExamineeId")
    public String examineeId;

    @Column(name = "Gladness")
    public int gladness;

    @Column(name = "Vexation")
    public int vexation;

    @Column(name = "DesireToPlay")
    public int desireToPlay;

    @Column(name = "AdditionalPlayTime")
    public int additionalPlayTime;

    @Column(name = "DesireToLearn")
    public int desireToLearn;

    @Column(name = "Fun")
    public int fun;

    @Column(name = "Feasibility")
    public int feasibility;

    @Column(name = "Usefulness")
    public int usefulness;

    @Column(name = "Opinion")
    public String opinion;
}
