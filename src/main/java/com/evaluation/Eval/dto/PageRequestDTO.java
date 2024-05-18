package com.evaluation.Eval.dto;


import org.springframework.data.domain.Sort.Direction;


import lombok.Data;

@Data
public class PageRequestDTO {
    int page = 0;
    int size = 8;
    String sortBy = "default";
    String direction = "asc";


    public Direction getDirection(){
        return Direction.fromString(direction);
    }

    public String getDirectionString(){
        return direction;
    }
}
