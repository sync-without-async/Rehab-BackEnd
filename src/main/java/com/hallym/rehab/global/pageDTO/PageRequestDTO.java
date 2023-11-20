package com.hallym.rehab.global.pageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type;

    private String keyword;
    private String sortBy;
    private String metrics_rate;

    public String[] getTypes(){
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }

    public Pageable getPageable(String...props) {
        Sort sort = Sort.unsorted();
        if ("oldest".equals(sortBy)) {
            sort = sort.and(Sort.by("cno").ascending());
        } else {
            sort = sort.and(Sort.by("cno").descending());
        }

        if ("high".equals(metrics_rate)) {
            sort = sort.and(Sort.by("metrics_rate").descending());
        } else if ("low".equals(metrics_rate)){
            sort = sort.and(Sort.by("metrics_rate").ascending());
        }

        return PageRequest.of(this.page -1, this.size, sort);
    }

    private String link;

    public String getLink() { //검색 조건과 페이징 조건 들을 문자열로 구성

        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

            builder.append("&size=" + this.size);


            if(type != null && type.length() > 0){
                builder.append("&type=" + type);
            }

            if(keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
            link = builder.toString();
        }

        return link;
    }

}
