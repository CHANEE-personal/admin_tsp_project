package com.tsp.new_tsp_admin.api.comment.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.model.service.AdminModelJpaQueryRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("어드민 코멘트 Repository Test")
class AdminCommentJpaQueryRepositoryTest {
    @Mock private AdminCommentJpaQueryRepository mockAdminCommentJpaQueryRepository;
    private final AdminCommentJpaQueryRepository adminCommentJpaQueryRepository;
    private final AdminModelJpaQueryRepository adminModelJpaQueryRepository;
    private final EntityManager em;

    private AdminModelEntity adminModelEntity;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createAdminComment() {
        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .favoriteCount(1)
                .viewCount(1)
                .modelMainYn("Y")
                .newYn("N")
                .status("draft")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        em.persist(adminModelEntity);
        AdminModelDTO adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("model")
                .visible("Y")
                .build();

        adminCommentDTO = AdminCommentEntity.toDto(adminCommentEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createAdminComment();
    }

    @Test
    @Disabled
    @DisplayName("어드민코멘트리스트조회테스트")
    void 어드민코멘트리스트조회테스트() {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("jpaStartPage", 1);
        commentMap.put("size", 3);

        // then
        assertThat(adminCommentJpaQueryRepository.findAdminCommentList(commentMap)).isNotEmpty();
    }

    @Test
    @DisplayName("어드민코멘트Mockito조회테스트")
    void 어드민코멘트Mockito조회테스트() {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("jpaStartPage", 1);
        commentMap.put("size", 3);

        List<AdminCommentDTO> commentList = new ArrayList<>();
        commentList.add(AdminCommentDTO.builder().idx(1L)
                .commentType("model").commentTypeIdx(adminModelEntity.getIdx())
                .comment("model").build());

        // when
        when(mockAdminCommentJpaQueryRepository.findAdminCommentList(commentMap)).thenReturn(commentList);
        List<AdminCommentDTO> newCommentList = mockAdminCommentJpaQueryRepository.findAdminCommentList(commentMap);

        // then
        assertThat(newCommentList.get(0).getComment()).isEqualTo(commentList.get(0).getComment());
        assertThat(newCommentList.get(0).getCommentType()).isEqualTo(commentList.get(0).getCommentType());
        assertThat(newCommentList.get(0).getCommentTypeIdx()).isEqualTo(commentList.get(0).getCommentTypeIdx());

        // verify
        verify(mockAdminCommentJpaQueryRepository, times(1)).findAdminCommentList(commentMap);
        verify(mockAdminCommentJpaQueryRepository, atLeastOnce()).findAdminCommentList(commentMap);
        verifyNoMoreInteractions(mockAdminCommentJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminCommentJpaQueryRepository);
        inOrder.verify(mockAdminCommentJpaQueryRepository).findAdminCommentList(commentMap);
    }

    @Test
    @DisplayName("어드민코멘트BDD조회테스트")
    void 어드민코멘트BDD조회테스트() {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("jpaStartPage", 1);
        commentMap.put("size", 3);

        List<AdminCommentDTO> commentList = new ArrayList<>();
        commentList.add(AdminCommentDTO.builder().idx(1L)
                .commentType("model").commentTypeIdx(adminModelEntity.getIdx())
                .comment("model").build());

        // when
        given(mockAdminCommentJpaQueryRepository.findAdminCommentList(commentMap)).willReturn(commentList);
        List<AdminCommentDTO> newCommentList = mockAdminCommentJpaQueryRepository.findAdminCommentList(commentMap);

        // then
        assertThat(newCommentList.get(0).getComment()).isEqualTo(commentList.get(0).getComment());
        assertThat(newCommentList.get(0).getCommentType()).isEqualTo(commentList.get(0).getCommentType());
        assertThat(newCommentList.get(0).getCommentTypeIdx()).isEqualTo(commentList.get(0).getCommentTypeIdx());

        // verify
        then(mockAdminCommentJpaQueryRepository).should(times(1)).findAdminCommentList(commentMap);
        then(mockAdminCommentJpaQueryRepository).should(atLeastOnce()).findAdminCommentList(commentMap);
        then(mockAdminCommentJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}