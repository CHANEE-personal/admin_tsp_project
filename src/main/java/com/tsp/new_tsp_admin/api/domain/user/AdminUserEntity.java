package com.tsp.new_tsp_admin.api.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

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
