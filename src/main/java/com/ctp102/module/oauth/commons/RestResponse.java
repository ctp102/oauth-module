package com.ctp102.module.oauth.commons;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.*;

@Getter
public final class RestResponse {

    private Map<String, Object> status;
    private Map<String, Object> data;

    public RestResponse() {
    }

    public RestResponse(Map<String, Object> data) {
        this.data = data;
    }

    public RestResponse(Map<String, Object> status, Map<String, Object> data) {
        this.status = status;
        this.data = data;
    }

    public static final class Builder {
        private Map<String, Object> status;
        private Map<String, Object> data;
        private ArrayList items;

        public Builder() {
            this(HttpStatus.OK);
        }

        public Builder(HttpStatus httpStatus) {
            this.status = new LinkedHashMap<>() {{
                put("code", httpStatus.value());
                put("message", httpStatus.getReasonPhrase());
            }};
            this.data = new LinkedHashMap<>();
            this.items = new ArrayList();
        }

        public Builder addStatus(String k1, Object v1) {
            this.status.put(k1, v1);
            return this;
        }
        public Builder addStatus(String k1, Object v1, String k2, Object v2) {
            return this.addStatus(k1, v1).addStatus(k2, v2);
        }

        public Builder addData(String k1, Object v1) {
            this.data.put(k1, v1);
            return this;
        }

        public Builder addData(String k1, Object v1, String k2, Object v2) {
            return this.addData(k1, v1).addData(k2, v2);
        }

        public Builder addData(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
            return this.addData(k1, v1).addData(k2, v2).addData(k3, v3);
        }

        public Builder addData(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
            return this.addData(k1, v1).addData(k2, v2).addData(k3, v3).addData(k4, v4);
        }

        public Builder addItems(Object item) {
            this.data.put("items", item != null ? Arrays.asList(item) : new ArrayList());
            return this;
        }

        public Builder addItems(Collection item) {
            this.data.put("items", item != null ? Arrays.asList(item) : new ArrayList());
            return this;
        }

        public Builder addItems(Object item, Paging paging) {
            this.data.put("items", item != null ? Arrays.asList(item) : new ArrayList());
            this.data.put("paging", paging);
            return this;
        }

        public RestResponse build() {
            return new RestResponse(this.status, this.data);
        }

    }


}
