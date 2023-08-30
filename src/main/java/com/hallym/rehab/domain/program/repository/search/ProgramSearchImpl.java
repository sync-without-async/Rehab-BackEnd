package com.hallym.rehab.domain.program.repository.search;

import com.hallym.rehab.domain.program.dto.program.ProgramListResponseDTO;
import com.hallym.rehab.domain.program.entity.*;
import com.hallym.rehab.domain.program.entity.QProgram;
import com.hallym.rehab.domain.program.entity.QVideo;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ProgramSearchImpl extends QuerydslRepositorySupport implements ProgramSearch {


    /*
    추후 외부에서 도메인 클래스를 지정하는 설계로 변경 시 사용 예정
     */
//    public ProgramSearchImpl(Class<?> domainClass) {
//        super(domainClass);
//    }

    public ProgramSearchImpl() {
        super(Program.class); //항상 Program으로 ProgramSearchImpl 인스턴스 생성
    }

    @Override
    public Page<ProgramListResponseDTO> searchProgramList(String[] types, String keyword, Pageable pageable) {

        QProgram program = QProgram.program;
        QVideo qVideo = QVideo.video;

        JPQLQuery<Program> programJPQLQuery = from(program);
        programJPQLQuery.leftJoin(qVideo).on(qVideo.program.eq(program));

        BooleanBuilder is_deleted = new BooleanBuilder();
        is_deleted.or(program.is_deleted.eq(false));
        programJPQLQuery.where(is_deleted);

        programJPQLQuery.groupBy(program);

        if( (types != null) && keyword != null ) {
            String type = String.join("", types);
            log.info(type);
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            switch (type) {
                case "t": //title
                    booleanBuilder.or(program.programTitle.contains(keyword));
                    break;
                case "c": //category
                    if (keyword.contains("ARMS"))
                        booleanBuilder.or(program.category.eq(Category.ARMS));
                    else if (keyword.contains("KNEES"))
                        booleanBuilder.or(program.category.eq(Category.KNEES));
                    else if (keyword.contains("THIGHS"))
                        booleanBuilder.or(program.category.eq(Category.THIGHS));
                    else
                        booleanBuilder.or(program.category.eq(Category.SHOULDERS));
                    break;
                case "p": //position
                    if (keyword.contains("누운 자세"))
                        booleanBuilder.or(program.position.eq(Position.LYING));
                    else if (keyword.contains("앉은 자세"))
                        booleanBuilder.or(program.position.eq(Position.SITTING));
                    else
                        booleanBuilder.or(program.position.eq(Position.STANDING));
                    break;
                case "cp":
                    String[] split = keyword.split(",");
                    switch (split[0]) {
                        case "ARMS":
                            booleanBuilder.and(program.category.eq(Category.ARMS));
                            break;
                        case "KNEES":
                            booleanBuilder.and(program.category.eq(Category.KNEES));
                            break;
                        case "THIGHS":
                            booleanBuilder.and(program.category.eq(Category.THIGHS));
                            break;
                        default:
                            booleanBuilder.and(program.category.eq(Category.SHOULDERS));
                            break;
                    }

                    switch (split[1]) {
                        case "누운 자세":
                            booleanBuilder.and(program.position.eq(Position.LYING));
                            break;
                        case "앉은 자세":
                            booleanBuilder.and(program.position.eq(Position.SITTING));
                            break;
                        case "선 자세":
                            booleanBuilder.and(program.position.eq(Position.STANDING));
                            break;
                    }

            }//end for
            programJPQLQuery.where(booleanBuilder);
        }//end if

        //pno > 0
        programJPQLQuery.where(program.pno.gt(0L));

        getQuerydsl().applyPagination(pageable, programJPQLQuery);

        JPQLQuery<Tuple> tupleJPQLQuery = programJPQLQuery.select(program, qVideo.countDistinct());
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<ProgramListResponseDTO> dtoList = tupleList.stream().map(tuple -> {

            Program program1 = (Program) tuple.get(program);

            double totalPlayTime = 0L;
            Set<Video> videoList = program1.getVideo(); // 프로그램의 비디오 리스트를 가져옵니다.

            for (Video v : videoList) {
                totalPlayTime += v.getPlayTime(); // 각 비디오의 재생 시간을 더합니다.
            }

            ProgramListResponseDTO dto = ProgramListResponseDTO.builder()
                    .pno(program1.getPno())
                    .programTitle(program1.getProgramTitle())
                    .description(program1.getDescription())
                    .category(program1.getCategory())
                    .position(program1.getPosition())
                    .totalPlayTime(totalPlayTime)
                    .regDate(program1.getRegDate())
                    .build();

            return dto;
        }).collect(Collectors.toList());

        long totalCont = programJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCont);
    }

}
