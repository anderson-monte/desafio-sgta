package com.anderson_monte.desafio_tecnico_sgta.wrapper;

import lombok.Getter;

import java.util.List;

@Getter
public class PageableResponse<T>  {
    private List<T> content;
    private PageDetails page;

    @Getter
    public static class PageDetails {
        private int size;
        private int number;
        private int totalElements;
        private int totalPages;
    }
}
