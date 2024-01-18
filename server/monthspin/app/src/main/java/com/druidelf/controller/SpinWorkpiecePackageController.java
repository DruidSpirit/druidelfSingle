package com.druidelf.controller;

import com.druidelf.request.SpinWorkpiecePackageRequest;
import com.druidelf.service.SpinWorkpiecePackageService;
import druidelf.bean.spin.SpinWorkpiecePackage;
import druidelf.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spinWorkpiecePackage")
@RequiredArgsConstructor
public class SpinWorkpiecePackageController {

    private final SpinWorkpiecePackageService spinWorkpiecePackageService;

    @GetMapping(value = "getSpinWorkpiecePackageList")
    public ResponseData<Page<SpinWorkpiecePackage>> getSpinWorkpiecePackageList(SpinWorkpiecePackageRequest request ) {

        Page<SpinWorkpiecePackage> packageList = spinWorkpiecePackageService.getSpinWorkpiecePackageList(request);
        return ResponseData.SUCCESS(packageList);
    }
}
