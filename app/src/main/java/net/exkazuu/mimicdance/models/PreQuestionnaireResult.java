package net.exkazuu.mimicdance.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "PreQuestionnaireResult")
public class PreQuestionnaireResult extends Model {
    @Column(name = "Created_at")
    public Date created_at = new Date(System.currentTimeMillis());

    @Column(name = "ExamineeId")
    public String examineeId;

    @Column(name = "Sex")
    public String sex;

    @Column(name = "Age")
    public int age;

    @Column(name = "KnowledgeOfProgramming")
    public boolean knowledgeOfProgramming;

    @Column(name = "KnowledgeOfMimicDance")
    public boolean knowledgeOfMimicDance;

    @Column(name = "DesireToLearn")
    public int desireToLearn;

    @Column(name = "Fun")
    public int fun;

    @Column(name = "Feasibility")
    public int feasibility;

    @Column(name = "Usefulness")
    public int usefulness;

    @Column(name = "Sent")
    public boolean sent;
}

