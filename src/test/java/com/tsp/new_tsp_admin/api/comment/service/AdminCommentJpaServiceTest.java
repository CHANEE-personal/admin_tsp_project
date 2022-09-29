package com.tsp.new_tsp_admin.api.comment.service;

import com.tsp.new_tsp_admin.api.comment.mapper.AdminCommentMapper;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.model.service.AdminModelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.faq.mapper.FaqMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("어드민 코멘트 Service Test")
class AdminCommentJpaServiceTest {
    @Mock AdminCommentJpaService mockAdminCommentJpaService;
    private final AdminCommentJpaService adminCommentJpaService;
    private final AdminModelJpaRepository adminModelJpaRepository;

    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;
    private AdminModelEntity adminModelEntity;

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
                .status("draft")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        Long modelIdx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();
        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(modelIdx)
                .visible("Y")
                .build();

        adminCommentDTO = AdminCommentMapper.INSTANCE.toDto(adminCommentEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createAdminComment();
    }

    @Test
    @DisplayName("어드민 코멘트 리스트 조회 테스트")
    void 어드민코멘트리스트조회테스트() throws Exception {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("jpaStartPage", 1);
        commentMap.put("size", 3);

        // then
        assertThat(adminCommentJpaService.findAdminCommentList(commentMap)).isNotEmpty();
    }

    @Test
    @DisplayName("어드민 코멘트 리스트 조회 Mockito 테스트")
    void 어드민코멘트리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("jpaStartPage", 1);
        commentMap.put("size", 3);

        List<AdminCommentDTO> returnCommentList = new ArrayList<>();

        returnCommentList.add(AdminCommentDTO.builder().idx(1L).comment("코멘트테스트").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());
        returnCommentList.add(AdminCommentDTO.builder().idx(2L).comment("코멘트테스트2").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());

        // when
        when(mockAdminCommentJpaService.findAdminCommentList(commentMap)).thenReturn(returnCommentList);
        List<AdminCommentDTO> commentList = mockAdminCommentJpaService.findAdminCommentList(commentMap);

        // then
        assertAll(
                () -> assertThat(commentList).isNotEmpty(),
                () -> assertThat(commentList).hasSize(2)
        );

        assertThat(commentList.get(0).getIdx()).isEqualTo(returnCommentList.get(0).getIdx());
        assertThat(commentList.get(0).getComment()).isEqualTo(returnCommentList.get(0).getComment());
        assertThat(commentList.get(0).getCommentType()).isEqualTo(returnCommentList.get(0).getCommentType());
        assertThat(commentList.get(0).getCommentTypeIdx()).isEqualTo(returnCommentList.get(0).getCommentTypeIdx());

        // verify
        verify(mockAdminCommentJpaService, times(1)).findAdminCommentList(commentMap);
        verify(mockAdminCommentJpaService, atLeastOnce()).findAdminCommentList(commentMap);
        verifyNoMoreInteractions(mockAdminCommentJpaService);

        InOrder inOrder = inOrder(mockAdminCommentJpaService);
        inOrder.verify(mockAdminCommentJpaService).findAdminCommentList(commentMap);
    }

    @Test
    @DisplayName("어드민 코멘트 리스트 조회 BDD 테스트")
    void 어드민코멘트리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("jpaStartPage", 1);
        commentMap.put("size", 3);

        List<AdminCommentDTO> returnCommentList = new ArrayList<>();

        returnCommentList.add(AdminCommentDTO.builder().idx(1L).comment("코멘트테스트").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());
        returnCommentList.add(AdminCommentDTO.builder().idx(2L).comment("코멘트테스트2").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());

        // when
        given(mockAdminCommentJpaService.findAdminCommentList(commentMap)).willReturn(returnCommentList);
        List<AdminCommentDTO> commentList = mockAdminCommentJpaService.findAdminCommentList(commentMap);

        // then
        assertAll(
                () -> assertThat(commentList).isNotEmpty(),
                () -> assertThat(commentList).hasSize(2)
        );

        assertThat(commentList.get(0).getIdx()).isEqualTo(returnCommentList.get(0).getIdx());
        assertThat(commentList.get(0).getComment()).isEqualTo(returnCommentList.get(0).getComment());
        assertThat(commentList.get(0).getCommentType()).isEqualTo(returnCommentList.get(0).getCommentType());
        assertThat(commentList.get(0).getCommentTypeIdx()).isEqualTo(returnCommentList.get(0).getCommentTypeIdx());

        // verify
        then(mockAdminCommentJpaService).should(times(1)).findAdminCommentList(commentMap);
        then(mockAdminCommentJpaService).should(atLeastOnce()).findAdminCommentList(commentMap);
        then(mockAdminCommentJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트상세Mockito조회테스트")
    void 어드민코멘트상세Mockito조회테스트() throws Exception {
        // given
        AdminCommentEntity adminCommentEntity = AdminCommentEntity.builder()
                .idx(1L)
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(adminModelEntity.getIdx())
                .visible("Y")
                .build();

        adminCommentDTO = AdminCommentMapper.INSTANCE.toDto(adminCommentEntity);

        // when
        when(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity)).thenReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity);

        // then
        assertThat(commentInfo.getIdx()).isEqualTo(1);
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        verify(mockAdminCommentJpaService, times(1)).findOneAdminComment(adminCommentEntity);
        verify(mockAdminCommentJpaService, atLeastOnce()).findOneAdminComment(adminCommentEntity);
        verifyNoMoreInteractions(mockAdminCommentJpaService);

        InOrder inOrder = inOrder(mockAdminCommentJpaService);
        inOrder.verify(mockAdminCommentJpaService).findOneAdminComment(adminCommentEntity);
    }

    @Test
    @DisplayName("FAQ상세BDD조회테스트")
    void FAQ상세BDD조회테스트() throws Exception {
        // given
        AdminCommentEntity adminCommentEntity = AdminCommentEntity.builder()
                .idx(1L)
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(adminModelEntity.getIdx())
                .visible("Y")
                .build();

        adminCommentDTO = AdminCommentMapper.INSTANCE.toDto(adminCommentEntity);

        // when
        given(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity)).willReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity);

        // then
        assertThat(commentInfo.getIdx()).isEqualTo(1);
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        then(mockAdminCommentJpaService).should(times(1)).findOneAdminComment(adminCommentEntity);
        then(mockAdminCommentJpaService).should(atLeastOnce()).findOneAdminComment(adminCommentEntity);
        then(mockAdminCommentJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트등록Mockito테스트")
    void 어드민코멘트등록Mockito테스트() throws Exception {
        // given
        adminCommentJpaService.insertAdminComment(adminCommentEntity);

        // when
        when(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity)).thenReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity);

        // then
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        verify(mockAdminCommentJpaService, times(1)).findOneAdminComment(adminCommentEntity);
        verify(mockAdminCommentJpaService, atLeastOnce()).findOneAdminComment(adminCommentEntity);
        verifyNoMoreInteractions(mockAdminCommentJpaService);

        InOrder inOrder = inOrder(mockAdminCommentJpaService);
        inOrder.verify(mockAdminCommentJpaService).findOneAdminComment(adminCommentEntity);
    }

    @Test
    @DisplayName("FAQ등록BDD테스트")
    void FAQ등록BDD테스트() throws Exception {
        // given
        adminCommentJpaService.insertAdminComment(adminCommentEntity);

        // when
        given(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity)).willReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity);

        // then
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        then(mockAdminCommentJpaService).should(times(1)).findOneAdminComment(adminCommentEntity);
        then(mockAdminCommentJpaService).should(atLeastOnce()).findOneAdminComment(adminCommentEntity);
        then(mockAdminCommentJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트수정Mockito테스트")
    void 어드민코멘트수정Mockito테스트() throws Exception {
        // given
        Long idx = adminCommentJpaService.insertAdminComment(adminCommentEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .idx(idx)
                .comment("코멘트 테스트1")
                .commentType("model")
                .commentTypeIdx(adminModelEntity.getIdx())
                .visible("Y")
                .build();

        AdminCommentDTO adminCommentDTO = AdminCommentMapper.INSTANCE.toDto(adminCommentEntity);

        adminCommentJpaService.updateAdminComment(adminCommentEntity);

        // when
        when(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity)).thenReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity);

        // then
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트1");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        verify(mockAdminCommentJpaService, times(1)).findOneAdminComment(adminCommentEntity);
        verify(mockAdminCommentJpaService, atLeastOnce()).findOneAdminComment(adminCommentEntity);
        verifyNoMoreInteractions(mockAdminCommentJpaService);

        InOrder inOrder = inOrder(mockAdminCommentJpaService);
        inOrder.verify(mockAdminCommentJpaService).findOneAdminComment(adminCommentEntity);
    }

    @Test
    @DisplayName("FAQ수정BDD테스트")
    void FAQ수정BDD테스트() throws Exception {
        // given
        Long idx = adminCommentJpaService.insertAdminComment(adminCommentEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .idx(idx)
                .comment("코멘트 테스트1")
                .commentType("model")
                .commentTypeIdx(adminModelEntity.getIdx())
                .visible("Y")
                .build();

        AdminCommentDTO adminCommentDTO = AdminCommentMapper.INSTANCE.toDto(adminCommentEntity);

        adminCommentJpaService.updateAdminComment(adminCommentEntity);

        // when
        given(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity)).willReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity);

        // then
        assertThat(commentInfo.getComment()).isEqualTo("코멘트 테스트1");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        then(mockAdminCommentJpaService).should(times(1)).findOneAdminComment(adminCommentEntity);
        then(mockAdminCommentJpaService).should(atLeastOnce()).findOneAdminComment(adminCommentEntity);
        then(mockAdminCommentJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트삭제테스트")
    void 어드민코멘트삭제테스트() throws Exception {
        // given
        adminCommentJpaService.insertAdminComment(adminCommentEntity);

        Long entityIdx = adminCommentEntity.getIdx();
        Long idx = adminCommentJpaService.deleteAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(entityIdx).isEqualTo(idx);
    }

    @Test
    @DisplayName("어드민코멘트삭제Mockito테스트")
    void 어드민코멘트삭제Mockito테스트() throws Exception {
        // given
        adminCommentJpaService.insertAdminComment(adminCommentEntity);
        adminCommentDTO = AdminCommentMapper.INSTANCE.toDto(adminCommentEntity);

        // when
        when(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity)).thenReturn(adminCommentDTO);
        Long deleteIdx = adminCommentJpaService.deleteAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminCommentJpaService, times(1)).findOneAdminComment(adminCommentEntity);
        verify(mockAdminCommentJpaService, atLeastOnce()).findOneAdminComment(adminCommentEntity);
        verifyNoMoreInteractions(mockAdminCommentJpaService);

        InOrder inOrder = inOrder(mockAdminCommentJpaService);
        inOrder.verify(mockAdminCommentJpaService).findOneAdminComment(adminCommentEntity);
    }

    @Test
    @DisplayName("어드민코멘트삭제BDD테스트")
    void 어드민코멘트삭제BDD테스트() throws Exception {
        // given
        adminCommentJpaService.insertAdminComment(adminCommentEntity);
        adminCommentDTO = AdminCommentMapper.INSTANCE.toDto(adminCommentEntity);

        // when
        given(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity)).willReturn(adminCommentDTO);
        Long deleteIdx = adminCommentJpaService.deleteAdminComment(adminCommentEntity.getIdx());

        // then
        assertThat(mockAdminCommentJpaService.findOneAdminComment(adminCommentEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminCommentJpaService).should(times(1)).findOneAdminComment(adminCommentEntity);
        then(mockAdminCommentJpaService).should(atLeastOnce()).findOneAdminComment(adminCommentEntity);
        then(mockAdminCommentJpaService).shouldHaveNoMoreInteractions();
    }
}