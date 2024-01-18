package com.druidelf.service;

import com.druidelf.request.SpinWorkpiecePackageRequest;
import druidelf.bean.spin.SpinWorkpiecePackage;
import org.springframework.data.domain.Page;

public interface SpinWorkpiecePackageService {

    /**
     * 获取工件包分页列表
     */
    Page<SpinWorkpiecePackage> getSpinWorkpiecePackageList( SpinWorkpiecePackageRequest request );
}
