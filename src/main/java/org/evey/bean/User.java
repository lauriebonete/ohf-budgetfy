package org.evey.bean;

import org.evey.annotation.JoinSet;
import org.evey.annotation.UniqueField;
import org.evey.utility.SecurityUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER")
public class User extends BaseEntity{

	@Column(name="FIRST_NAME")
	private String firstName;

	@Column(name="LAST_NAME")
	private String lastName;

	@Column(name="MIDDLE_NAME")
	private String middleName;

	@Column(name="BIRTH_DATE")
	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE", referencedColumnName = "ID")
	private FileDetail personImage;

	@Column(name = "IMAGE", insertable = false, updatable = false)
	private Long personImageId;

	@Column(name="USERNAME", unique=true, nullable=false)
	private String username;

	@Column(name="PASSWORD", nullable=false)
	private String password;

	@Column(name="HIRED_DATE")
	@Temporal(TemporalType.DATE)
	private Date hiredDate;

	@Column(name="UNTIL_DATE")
	@Temporal(TemporalType.DATE)
	private Date untilDate;

	@Column(name="EXPIRATION_DATE")
	@Temporal(TemporalType.DATE)
	private Date accountExpirationDate;

	@Column(name="PIN_DIGIT", unique=true)
	@UniqueField
	private String pinDigit;

	@JoinSet
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_USER_ROLE",
			joinColumns = { @JoinColumn(name = "USER_ID") },
			inverseJoinColumns = { @JoinColumn(name = "USER_ROLE_ID") })
	private Set<UserRole> userRole;

	private transient List<Long> userRoleList;
	private transient String userRoleDisplay;
	private transient String userDisplay;
	private transient String completeName;
	private transient String fullName;
	private transient String newPassword;
//	private Role role;
//	private Schedule schedule;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
        this.password = password;
	}
	public Date getHiredDate() {
		return hiredDate;
	}
	public void setHiredDate(Date hiredDate) {
		this.hiredDate = hiredDate;
	}
	public Date getUntilDate() {
		return untilDate;
	}
	public void setUntilDate(Date untilDate) {
		this.untilDate = untilDate;
	}
	public Date getAccountExpirationDate() {
		return accountExpirationDate;
	}
	public void setAccountExpirationDate(Date accountExpirationDate) {
		this.accountExpirationDate = accountExpirationDate;
	}

	public String getPinDigit() {
		return pinDigit;
	}

	public void setPinDigit(String pinDigit) {
		this.pinDigit = pinDigit;
	}

	public Set<UserRole> getUserRole() {
		return userRole;
	}

	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public FileDetail getPersonImage() {
		return personImage;
	}

	public void setPersonImage(FileDetail personImage) {
		this.personImage = personImage;
	}

	public Long getPersonImageId() {
		return personImageId;
	}

	public void setPersonImageId(Long personImageId) {
		this.personImageId = personImageId;
	}

	public void setUserRoleList(List<Long> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public void setUserRoleDisplay(String userRoleDisplay) {
		this.userRoleDisplay = userRoleDisplay;
	}

	public void setUserDisplay(String userDisplay) {
		this.userDisplay = userDisplay;
	}

	public String getFullName() {
		return this.lastName+", "+this.firstName;
	}
	public String getCompleteName(){
		return this.firstName+" "+this.lastName;
	}

	public List<Long> getUserRoleList() {
		if(this.userRole!=null){
			List<Long> userRoleIds = new ArrayList<>();
			for(UserRole userRole: this.userRole){
				userRoleIds.add(userRole.getId());
			}
			return userRoleIds;
		}
		return userRoleList;
	}

	public String getUserRoleDisplay() {
		if(this.userRole!=null){
			StringBuilder roleDisplayBuilder = new StringBuilder();
			for(UserRole userRole: this.userRole){
				if(roleDisplayBuilder.length()>0){
					roleDisplayBuilder.append(", ");
				}
				roleDisplayBuilder.append(userRole.getRoleName());
			}
			return roleDisplayBuilder.toString();
		}
		return userRoleDisplay;
	}

	public String getUserDisplay() {
		return username+" - "+firstName+" "+lastName;
	}

	public Boolean isMatchPassword(String password){
		return SecurityUtil.isMatchPassword(this.password,password);
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		if(newPassword!=null){
			this.newPassword = SecurityUtil.encryptPassword(newPassword);
		}
		this.newPassword = newPassword;
	}
}
