package fiveexceptions.com.m01sbcm.sync.model;

public class User {

		private long id;
	    private  int userType;
		private String userName;
		private String password;
		private String mobileNum;
		private String emailId;
		private String displayName;
		private String socialMediaId;
		private String communityId;

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getMobileNum() {
			return mobileNum;
		}
		public void setMobileNum(String mobileNum) {
			this.mobileNum = mobileNum;
		}
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		public String getSocialMediaId() {
			return socialMediaId;
		}
		public void setSocialMediaId(String socialMediaId) {
			this.socialMediaId = socialMediaId;
		}
		public String getCommunityId() {
			return communityId;
		}
		public void setCommunityId(String communityId) {
			this.communityId = communityId;
		}
		
		

}
