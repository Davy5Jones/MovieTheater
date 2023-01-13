package com.jb.MovieTheater.beans.mongo;

import com.jb.MovieTheater.beans.SuperBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Document("theaters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theater extends SuperBean {
    @MongoId
    private String id;
    @NotBlank(message = "Theater name can't be blank")
    private String name;
    private List<Integer> rows = new ArrayList<>();

    public Theater(String name, List<Integer> rows) {
        this.name = name;
        this.rows = rows;
    }
}
