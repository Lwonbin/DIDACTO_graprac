package com.didacto.controller.v1.monitoring.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class SampleImageResponse {
    private String imageName;
    private String imageData;
}