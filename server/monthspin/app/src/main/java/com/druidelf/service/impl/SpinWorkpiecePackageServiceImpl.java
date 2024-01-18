package com.druidelf.service.impl;

import com.druidelf.request.SpinWorkpiecePackageRequest;
import com.druidelf.service.SpinWorkpiecePackageService;
import druidelf.bean.spin.SpinWorkpiecePackage;
import druidelf.repository.spin.SpinWorkpiecePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpinWorkpiecePackageServiceImpl implements SpinWorkpiecePackageService {

    private final SpinWorkpiecePackageRepository spinWorkpiecePackageRepository;

    /**
     * 获取工件包分页列表
     */
    @Override
    public Page<SpinWorkpiecePackage> getSpinWorkpiecePackageList(SpinWorkpiecePackageRequest request) {

        SpinWorkpiecePackage spinWorkpiecePackage = new SpinWorkpiecePackage();
        Example<SpinWorkpiecePackage> of = Example.of(spinWorkpiecePackage);

        return spinWorkpiecePackageRepository.findAll(of,request);
    }
}
