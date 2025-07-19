package com.example.springboot.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {
    @CrossOrigin(origins = {"http://localhost:5173"})
    @GetMapping("/api/reach/csrftoken")
    public ResponseCsrf csrfToken(CsrfToken token) {
        ResponseCsrf response = new ResponseCsrf();
        if(token == null)
        {
            response.setStatus(4);
            response.setCsrftoken("token error");
            return response;
        } else
        {
            response.setStatus(1);
            response.setCsrftoken(token.getToken());
            return response;
        }
    }
    // jsonで返すために作成
    static class ResponseCsrf
    {
        private int status;
        private String csrftoken;

        public void setStatus(int status)
        {
            this.status = status;
        }
        public void setCsrftoken(String csrftoken)
        {
            this.csrftoken = csrftoken;
        }
        public int getStatus()
        {
            return this.status;
        }
        public String getCsrftoken()
        {
            return this.csrftoken;
        }
    }
}
