package com.zarkcigarettes.DailyDeepDive_ERP.api.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    protected LocalDateTime timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developerMessage;
    protected Map<?,?> data;
    protected Map<?,?> data_corresponding;
    protected Map<?,?> data_corresponding0;
    protected Map<?,?> data_corresponding1;
    protected Map<?,?> data_corresponding2;
    protected Map<?,?> data_corresponding3;
    protected Map<?,?> data_corresponding4;
    protected Map<?,?> data_corresponding5;
    protected Map<?,?> data_corresponding6;
    protected Map<?,?> data_corresponding7;
}
