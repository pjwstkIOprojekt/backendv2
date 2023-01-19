package com.gary.backendv2.model.dto.request;

import lombok.Data;


@Data
public class ReviewRequest extends BaseRequest {


    private Double value;

    private String discription;
}
