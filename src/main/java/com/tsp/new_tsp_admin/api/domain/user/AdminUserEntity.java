package com.tsp.new_tsp_admin.api.domain.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_admin")
public class AdminUserEntity implements UserDetails {

	@Transient
	private Integer rnum;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx")
	Integer idx;

	@Column(name = "user_id")
	@NotEmpty(message = "유저 ID 입력은 필수입니다.")
	String userId;

	@Column(name = "password")
	@NotEmpty(message = "유저 Password 입력은 필수입니다.")
	String password;

	@Column(name = "name")
	@NotEmpty(message = "유저 이름 입력은 필수입니다.")
	String name;

	@Column(name = "email")
	@Email
	@NotEmpty(message = "유저 이메일 입력은 필수입니다.")
	String email;

	@Column(name = "visible")
	String visible;

	@Column(name = "user_token")
	String userToken;

	@Column(name = "user_refreshToken")
	String userRefreshToken;

	@Column(name = "creator", updatable = false)
	@ApiModelProperty(required = true, value = "등록자")
	private String creator;

	@Column(name = "updater", insertable = false)
	@ApiModelProperty(required = true, value = "수정자")
	private String updater;

	@Column(name = "create_time", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@ApiModelProperty(required = true, value = "등록 일자")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createTime;

	@Column(name = "update_time", insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@ApiModelProperty(required = true, value = "수정 일자")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateTime;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
