package com.tsp.new_tsp_admin.api.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class AdminUserDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true, example = "1")
    Integer rnum;

    @ApiModelProperty(required = true, value = "user Seq", hidden = true, example = "1")
    Integer idx;

    @NotEmpty(message = "유저 아이디 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user Id")
    String userId;

    @NotEmpty(message = "유저 패스워드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user Password")
    String password;

    @NotEmpty(message = "유저 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user Name", hidden = true)
    String name;

    @NotEmpty(message = "유저 이메일 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user email", hidden = true)
    String email;

    @NotEmpty(message = "유저 사용여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "user visible", hidden = true)
    String visible;

    @ApiModelProperty(value = "user Token", hidden = true)
    String userToken;

    @ApiModelProperty(value = "user refresh Token", hidden = true)
    String userRefreshToken;

    @ApiModelProperty(value = "role", hidden = true)
    Role role;
}
