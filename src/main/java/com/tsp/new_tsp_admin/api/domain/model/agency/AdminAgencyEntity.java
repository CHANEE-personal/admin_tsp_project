package com.tsp.new_tsp_admin.api.domain.model.agency;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_agency")
public class AdminAgencyEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "agency_name")
    @NotEmpty(message = "소속사명 입력은 필수입니다.")
    private String agencyName;

    @Column(name = "agency_description")
    @Lob
    @NotEmpty(message = "소속사 상세내용 입력은 필수입니다.")
    private String agencyDescription;

    @Column(name = "favorite_count")
    private Integer favoriteCount;

    @Column(name = "visible")
    @NotEmpty(message = "소속사 노출 여부 선택은 필수입니다.")
    private String visible;

    @OneToMany(mappedBy = "adminAgencyEntity")
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    @OneToOne(mappedBy = "adminAgencyEntity", cascade = ALL, fetch = LAZY)
    private AdminModelEntity adminModelEntity;

    public static AdminAgencyDTO toDto(AdminAgencyEntity entity) {
        return AdminAgencyDTO.builder()
                .idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .agencyName(entity.getAgencyName())
                .agencyDescription(entity.getAgencyDescription())
                .favoriteCount(entity.getFavoriteCount())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public List<AdminAgencyDTO> toDtoList(List<AdminAgencyEntity> entityList) {
        List<AdminAgencyDTO> list = new ArrayList<>(entityList.size());
        entityList.forEach(adminAgencyEntity -> list.add(toDto(adminAgencyEntity)));
        return list;
    }
}
