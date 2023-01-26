package com.gary.backendv2.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest extends BaseRequest {


    private Double value;

    private String discription;
}
