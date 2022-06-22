package com.tsp.new_tsp_admin.api.domain.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_admin")
public class AdminUserEntity {

	@Transient
	private Integer rnum;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx")
	private Integer idx;

	@Column(name = "user_id")
	@NotEmpty(message = "유저 ID 입력은 필수입니다.")
	private String userId;

	@Column(name = "password")
	@NotEmpty(message = "유저 Password 입력은 필수입니다.")
	private String password;

	@Column(name = "name")
	@NotEmpty(message = "유저 이름 입력은 필수입니다.")
	private String name;

	@Column(name = "email")
	@Email
	@NotEmpty(message = "유저 이메일 입력은 필수입니다.")
	private String email;

	@Column(name = "visible")
	private String visible;

	@Column(name = "user_token")
	private String userToken;

	@Column(name = "user_refresh_token")
	private String userRefreshToken;

	@Enumerated(EnumType.STRING)
	private Role role;

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
}
