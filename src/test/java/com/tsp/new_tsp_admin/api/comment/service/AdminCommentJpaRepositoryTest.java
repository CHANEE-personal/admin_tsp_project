package com.tsp.new_tsp_admin.api.comment.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.model.service.AdminModelJpaRepository;
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
class AdminCommentJpaRepositoryTest {
    @Mock private AdminCommentJpaRepository mockAdminCommentJpaRepository;
    private final AdminCommentJpaRepository adminCommentJpaRepository;
    private final AdminModelJpaRepository adminModelJpaRepository;
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

        Long modelIdx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentTypeIdx(modelIdx)
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
        assertThat(adminCommentJpaRepository.findAdminCommentList(commentMap)).isNotEmpty();
    }

    @Test
    @Disabled
    @DisplayName("어드민코멘트상세조회테스트")
    void 어드민코멘트상세조회테스트() {
        // given
        adminCommentEntity = AdminCommentEntity.builder()
                .idx(1L).build();

        // when
        adminCommentDTO = adminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx());
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
        when(mockAdminCommentJpaRepository.findAdminCommentList(commentMap)).thenReturn(commentList);
        List<AdminCommentDTO> newCommentList = mockAdminCommentJpaRepository.findAdminCommentList(commentMap);

        // then
        assertThat(newCommentList.get(0).getComment()).isEqualTo(commentList.get(0).getComment());
        assertThat(newCommentList.get(0).getCommentType()).isEqualTo(commentList.get(0).getCommentType());
        assertThat(newCommentList.get(0).getCommentTypeIdx()).isEqualTo(commentList.get(0).getCommentTypeIdx());

        // verify
        verify(mockAdminCommentJpaRepository, times(1)).findAdminCommentList(commentMap);
        verify(mockAdminCommentJpaRepository, atLeastOnce()).findAdminCommentList(commentMap);
        verifyNoMoreInteractions(mockAdminCommentJpaRepository);

        InOrder inOrder = inOrder(mockAdminCommentJpaRepository);
        inOrder.verify(mockAdminCommentJpaRepository).findAdminCommentList(commentMap);
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
        given(mockAdminCommentJpaRepository.findAdminCommentList(commentMap)).willReturn(commentList);
        List<AdminCommentDTO> newCommentList = mockAdminCommentJpaRepository.findAdminCommentList(commentMap);

        // then
        assertThat(newCommentList.get(0).getComment()).isEqualTo(commentList.get(0).getComment());
        assertThat(newCommentList.get(0).getCommentType()).isEqualTo(commentList.get(0).getCommentType());
        assertThat(newCommentList.get(0).getCommentTypeIdx()).isEqualTo(commentList.get(0).getCommentTypeIdx());

        // verify
        then(mockAdminCommentJpaRepository).should(times(1)).findAdminCommentList(commentMap);
        then(mockAdminCommentJpaRepository).should(atLeastOnce()).findAdminCommentList(commentMap);
        then(mockAdminCommentJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트상세Mockito조회테스트")
    void 어드민코멘트상세Mockito조회테스트() {
        // given
        AdminCommentEntity adminCommentEntity = AdminCommentEntity.builder()
                .idx(1L)
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(adminModelEntity.getIdx())
                .visible("Y")
                .build();

        adminCommentDTO = AdminCommentEntity.toDto(adminCommentEntity);

        // when
        when(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx())).thenReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(commentInfo.getIdx()).isEqualTo(1);
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        verify(mockAdminCommentJpaRepository, times(1)).findOneAdminComment(adminCommentEntity.getIdx());
        verify(mockAdminCommentJpaRepository, atLeastOnce()).findOneAdminComment(adminCommentEntity.getIdx());
        verifyNoMoreInteractions(mockAdminCommentJpaRepository);

        InOrder inOrder = inOrder(mockAdminCommentJpaRepository);
        inOrder.verify(mockAdminCommentJpaRepository).findOneAdminComment(adminCommentEntity.getIdx());
    }

    @Test
    @DisplayName("어드민코멘트상세BDD조회테스트")
    void 어드민코멘트상세BDD조회테스트() {
        AdminCommentEntity adminCommentEntity = AdminCommentEntity.builder()
                .idx(1L)
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(adminModelEntity.getIdx())
                .visible("Y")
                .build();

        adminCommentDTO = AdminCommentEntity.toDto(adminCommentEntity);

        // when
        given(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx())).willReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(commentInfo.getIdx()).isEqualTo(1);
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        then(mockAdminCommentJpaRepository).should(times(1)).findOneAdminComment(adminCommentEntity.getIdx());
        then(mockAdminCommentJpaRepository).should(atLeastOnce()).findOneAdminComment(adminCommentEntity.getIdx());
        then(mockAdminCommentJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트등록Mockito테스트")
    void 어드민코멘트등록Mockito테스트() {
        // given
        adminCommentJpaRepository.insertAdminComment(adminCommentEntity);

        // when
        when(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx())).thenReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        verify(mockAdminCommentJpaRepository, times(1)).findOneAdminComment(adminCommentEntity.getIdx());
        verify(mockAdminCommentJpaRepository, atLeastOnce()).findOneAdminComment(adminCommentEntity.getIdx());
        verifyNoMoreInteractions(mockAdminCommentJpaRepository);

        InOrder inOrder = inOrder(mockAdminCommentJpaRepository);
        inOrder.verify(mockAdminCommentJpaRepository).findOneAdminComment(adminCommentEntity.getIdx());
    }

    @Test
    @DisplayName("어드민코멘트등록BDD테스트")
    void 어드민코멘트등록BDD테스트() {
        // given
        adminCommentJpaRepository.insertAdminComment(adminCommentEntity);

        // when
        when(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx())).thenReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        then(mockAdminCommentJpaRepository).should(times(1)).findOneAdminComment(adminCommentEntity.getIdx());
        then(mockAdminCommentJpaRepository).should(atLeastOnce()).findOneAdminComment(adminCommentEntity.getIdx());
        then(mockAdminCommentJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트수정Mockito테스트")
    void 어드민코멘트수정Mockito테스트() {
        // given
        Long idx = adminCommentJpaRepository.insertAdminComment(adminCommentEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .idx(idx)
                .comment("코멘트 테스트1")
                .commentType("model")
                .commentTypeIdx(adminModelEntity.getIdx())
                .visible("Y")
                .build();

        AdminCommentDTO adminCommentDTO = AdminCommentEntity.toDto(adminCommentEntity);

        adminCommentJpaRepository.updateAdminComment(adminCommentEntity);

        // when
        when(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx())).thenReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트1");

        // verify
        verify(mockAdminCommentJpaRepository, times(1)).findOneAdminComment(adminCommentEntity.getIdx());
        verify(mockAdminCommentJpaRepository, atLeastOnce()).findOneAdminComment(adminCommentEntity.getIdx());
        verifyNoMoreInteractions(mockAdminCommentJpaRepository);

        InOrder inOrder = inOrder(mockAdminCommentJpaRepository);
        inOrder.verify(mockAdminCommentJpaRepository).findOneAdminComment(adminCommentEntity.getIdx());
    }

    @Test
    @DisplayName("공지사항수정BDD테스트")
    void 공지사항수정BDD테스트() {
        // given
        Long idx = adminCommentJpaRepository.insertAdminComment(adminCommentEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .idx(idx)
                .comment("코멘트 테스트1")
                .commentType("model")
                .commentTypeIdx(adminModelEntity.getIdx())
                .visible("Y")
                .build();

        AdminCommentDTO adminCommentDTO = AdminCommentEntity.toDto(adminCommentEntity);

        adminCommentJpaRepository.updateAdminComment(adminCommentEntity);

        // when
        when(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx())).thenReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트1");

        // verify
        then(mockAdminCommentJpaRepository).should(times(1)).findOneAdminComment(adminCommentEntity.getIdx());
        then(mockAdminCommentJpaRepository).should(atLeastOnce()).findOneAdminComment(adminCommentEntity.getIdx());
        then(mockAdminCommentJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트삭제테스트")
    void 어드민코멘트삭제테스트() {
        // given
        em.persist(adminCommentEntity);

        Long entityIdx = adminCommentEntity.getIdx();
        Long idx = adminCommentJpaRepository.deleteAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(entityIdx).isEqualTo(idx);
    }

    @Test
    @DisplayName("어드민코멘트삭제Mockito테스트")
    void 어드민코멘트삭제Mockito테스트() {
        // given
        em.persist(adminCommentEntity);
        adminCommentDTO = AdminCommentEntity.toDto(adminCommentEntity);

        // when
        when(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx())).thenReturn(adminCommentDTO);
        Long deleteIdx = adminCommentJpaRepository.deleteAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminCommentJpaRepository, times(1)).findOneAdminComment(adminCommentEntity.getIdx());
        verify(mockAdminCommentJpaRepository, atLeastOnce()).findOneAdminComment(adminCommentEntity.getIdx());
        verifyNoMoreInteractions(mockAdminCommentJpaRepository);

        InOrder inOrder = inOrder(mockAdminCommentJpaRepository);
        inOrder.verify(mockAdminCommentJpaRepository).findOneAdminComment(adminCommentEntity.getIdx());
    }

    @Test
    @DisplayName("어드민코멘트삭제BDD테스트")
    void 어드민코멘트삭제BDD테스트() {
        // given
        em.persist(adminCommentEntity);
        adminCommentDTO = AdminCommentEntity.toDto(adminCommentEntity);

        // when
        given(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx())).willReturn(adminCommentDTO);
        Long deleteIdx = adminCommentJpaRepository.deleteAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(mockAdminCommentJpaRepository.findOneAdminComment(adminCommentEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminCommentJpaRepository).should(times(1)).findOneAdminComment(adminCommentEntity.getIdx());
        then(mockAdminCommentJpaRepository).should(atLeastOnce()).findOneAdminComment(adminCommentEntity.getIdx());
        then(mockAdminCommentJpaRepository).shouldHaveNoMoreInteractions();
    }
}