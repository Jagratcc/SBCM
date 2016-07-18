package fiveexceptions.com.m01sbcm.sync.model;

import java.util.List;

public class SyncResult {

	private int responseCode = 0;
	private String msg = "";
	private String serverSyncTime = "";
		
	private List<Cast> castList;
	private List<EducationField> educationFieldList;
	private List<EducationStatus> educationStatusList;
	private List<Manglic> manglicList;
	private List<MotherTongue> motherTongueList;
	private List<ProfileColor> profileColorList;
	private List<Profession> professionList;
	private List<City> cityList;
	private List<Region> regionList;	
	private List<State> stateList;	
	private List<News_Event> newsList;
	private List<News_Event> eventList;	
	private List<NewsEventSource> newsEventSourceList;
	private List<Comment> commentList;
	private List<Like> likeList;
	
	public List<Like> getLikeList() {
		return likeList;
	}
	public void setLikeList(List<Like> likeList) {
		this.likeList = likeList;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	public List<NewsEventSource> getNewsEventSourceList() {
		return newsEventSourceList;
	}
	public void setNewsEventSourceList(List<NewsEventSource> newsEventSourceList) {
		this.newsEventSourceList = newsEventSourceList;
	}
	public List<News_Event> getEventList() {
		return eventList;
	}
	public void setEventList(List<News_Event> eventList) {
		this.eventList = eventList;
	}
	public List<News_Event> getNewsList() {
		return newsList;
	}
	public void setNewsList(List<News_Event> newsList) {
		this.newsList = newsList;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getServerSyncTime() {
		return serverSyncTime;
	}
	public void setServerSyncTime(String serverSyncTime) {
		this.serverSyncTime = serverSyncTime;
	}
	public List<Cast> getCastList() {
		return castList;
	}
	public void setCastList(List<Cast> castList) {
		this.castList = castList;
	}
	public List<EducationField> getEducationFieldList() {
		return educationFieldList;
	}
	public void setEducationFieldList(List<EducationField> educationFieldList) {
		this.educationFieldList = educationFieldList;
	}
	public List<EducationStatus> getEducationStatusList() {
		return educationStatusList;
	}
	public void setEducationStatusList(List<EducationStatus> educationStatusList) {
		this.educationStatusList = educationStatusList;
	}
	public List<Manglic> getManglicList() {
		return manglicList;
	}
	public void setManglicList(List<Manglic> manglicList) {
		this.manglicList = manglicList;
	}
	public List<MotherTongue> getMotherTongueList() {
		return motherTongueList;
	}
	public void setMotherTongueList(List<MotherTongue> motherTongueList) {
		this.motherTongueList = motherTongueList;
	}
	public List<ProfileColor> getProfileColorList() {
		return profileColorList;
	}
	public void setProfileColorList(List<ProfileColor> profileColorList) {
		this.profileColorList = profileColorList;
	}
	public List<Profession> getProfessionList() {
		return professionList;
	}
	public void setProfessionList(List<Profession> professionList) {
		this.professionList = professionList;
	}
	public List<City> getCityList() {
		return cityList;
	}
	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}
	public List<Region> getRegionList() {
		return regionList;
	}
	public void setRegionList(List<Region> regionList) {
		this.regionList = regionList;
	}
	public List<State> getStateList() {
		return stateList;
	}
	public void setStateList(List<State> stateList) {
		this.stateList = stateList;
	}

	
}
