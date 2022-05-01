package com.tsp.new_tsp_admin.api.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
}
