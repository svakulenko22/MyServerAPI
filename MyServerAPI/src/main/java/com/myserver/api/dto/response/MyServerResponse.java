package com.myserver.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyServerResponse {
    private ResponseDetail status;
    private String content;

    public MyServerResponse(String content) {
        status = new ResponseDetail(200, "Status is OK");
        this.content = content;
    }

    public MyServerResponse(int code, String message) {
        status = new ResponseDetail(code, message);
        content = "";
    }
}


