package com.druidelf.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


public class SpinWorkpiecePackageRequest extends PageRequest {

    public SpinWorkpiecePackageRequest(int page, int size) {
        super(page, size, Sort.unsorted());
    }
}
