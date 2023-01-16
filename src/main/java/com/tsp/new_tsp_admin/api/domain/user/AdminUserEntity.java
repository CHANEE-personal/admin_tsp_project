package com.tsp.new_tsp_admin.api.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "tsp_admin")
public class AdminUserEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "user_id", unique = true)
    @NotEmpty(message = "유저 ID 입력은 필수입니다.")
    private String userId;

    @Column(name = "password")
    @NotEmpty(message = "유저 Password 입력은 필수입니다.")
    private String password;

    @Column(name = "name")
    @NotEmpty(message = "유저 이름 입력은 필수입니다.")
    private String name;

    @Column(name = "email", unique = true)
    @Email
    @NotEmpty(message = "유저 이메일 입력은 필수입니다.")
    private String email;

    @Column(name = "visible")
    @NotEmpty(message = "유저 사용 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "user_token")
    private String userToken;

    @Column(name = "user_refresh_token")
    private String userRefreshToken;

    @Enumerated(value = STRING)
    private Role role;

    public void update(AdminUserEntity adminUserEntity) {
        this.userId = adminUserEntity.userId;
        this.password = adminUserEntity.password;
        this.name = adminUserEntity.name;
        this.email = adminUserEntity.email;
        this.visible = adminUserEntity.visible;
        this.role = adminUserEntity.role;
    }

    public static AdminUserDTO toDto(AdminUserEntity entity) {
        if (entity == null) return null;
        return AdminUserDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .userId(entity.getUserId())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .visible(entity.getVisible())
                .userToken(entity.getUserToken())
                .userRefreshToken(entity.getUserRefreshToken())
                .role(entity.getRole())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<AdminUserDTO> toDtoList(List<AdminUserEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminUserEntity::toDto)
                .collect(Collectors.toList());
    }
}
