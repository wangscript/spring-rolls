package com.exam.entity.exam;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.paramecium.commons.SecurityUitls;
import org.paramecium.log.Log;
import org.paramecium.log.LoggerFactory;
import org.paramecium.security.UserDetails;

public class ExamSession implements Serializable{
	
	private static final long serialVersionUID = -5334056085235435760L;

	private final static Log logger = LoggerFactory.getLogger();
	
	private Integer id;
	
	private String title;// 考试描述
	
	private boolean choice;
	
	private Boolean audio;//是否是音频
	
	private int score;
	
	private int cnProportion = 1;// 中文权重
	
	private int enProportion = 1;// 英文权重
	
	private int punProportion = 1;// 标点权重
	
	private int numProportion = 1;// 数字权重
	
	private int longTime;
	
	private Date startDate;// 开始时间
	
	private Date endDate;// 结束时间
	
	private String audioPath;//音频路径
	
	private String textContent;
	
	private ConcurrentMap<Integer,QuestionChoice> questionChoices = new ConcurrentHashMap<Integer,QuestionChoice>();
	
	private ConcurrentMap<Integer,ExamineeSession> examinees = new ConcurrentHashMap<Integer, ExamineeSession>();
	
	/**
	 * 释放资源
	 */
	public synchronized void release(){
		if(examinees!=null && !examinees.isEmpty()){
			for(ExamineeSession examineeSession : examinees.values()){
				examineeSession.release();
			}
		}
		questionChoices.clear();
		examinees.clear();
		textContent = null;
	}
	
	/**
	 * 如果考完，提交后，就会清理该考生缓存
	 */
	public synchronized void removeExamineeSession(Integer examineeId){
		if(examineeId!=null){
			ExamineeSession examineeSession = examinees.get(examineeId);
			examineeSession.release();
			examinees.remove(examineeId);
			logger.info("考生:<"+examineeSession.getCode()+":"+examineeSession.getUsername()+">成功完成考试<"+this.getTitle()+">");
		}
	}
	
	public synchronized void addQuestionChoice(QuestionChoice questionChoice){
		questionChoices.put(questionChoice.getId(),questionChoice);
	}
	
	public Collection<QuestionChoice> getQuestionChoices(){
		return questionChoices.values();
	}
	
	public QuestionChoice getQuestionChoice(Integer id){
		if(id!=null){
			return questionChoices.get(id);
		}
		return null;
	}
	
	public synchronized void addExaminee(ExamineeSession examineeSession){
		examinees.put(examineeSession.getId(), examineeSession);
		logger.info("考生:<"+examineeSession.getCode()+":"+examineeSession.getUsername()+">成功加入考试<"+this.getTitle()+">");
	}

	public ExamineeSession getExaminee(Integer examineeId){
		return examinees.get(examineeId);
	}
	
	public Collection<ExamineeSession> getExamineeSessions(){
		return examinees.values();
	}
	
	public ExamineeSession getExaminee(){
		@SuppressWarnings("unchecked")
		org.paramecium.security.UserDetails<Examinee> user = (UserDetails<Examinee>) SecurityUitls.getLoginUser();
		if(user==null){
			return null;
		}
		Examinee examinee = user.getOtherInfo();
		if(examinee==null){
			return null;
		}
		return getExaminee(examinee.getId());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isChoice() {
		return choice;
	}

	public void setChoice(boolean choice) {
		this.choice = choice;
	}

	public int getCnProportion() {
		return cnProportion;
	}

	public void setCnProportion(int cnProportion) {
		this.cnProportion = cnProportion;
	}

	public int getEnProportion() {
		return enProportion;
	}

	public void setEnProportion(int enProportion) {
		this.enProportion = enProportion;
	}

	public int getPunProportion() {
		return punProportion;
	}

	public void setPunProportion(int punProportion) {
		this.punProportion = punProportion;
	}

	public int getNumProportion() {
		return numProportion;
	}

	public void setNumProportion(int numProportion) {
		this.numProportion = numProportion;
	}

	public int getLongTime() {
		return longTime;
	}

	public void setLongTime(int longTime) {
		this.longTime = longTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Boolean getAudio() {
		return audio;
	}

	public void setAudio(Boolean audio) {
		this.audio = audio;
	}
	
}
