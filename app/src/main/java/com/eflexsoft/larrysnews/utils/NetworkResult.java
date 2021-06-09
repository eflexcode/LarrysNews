package com.eflexsoft.larrysnews.utils;

import com.eflexsoft.larrysnews.model.Article;
import com.eflexsoft.larrysnews.model.News;

import javax.annotation.Nullable;

public class NetworkResult {

    protected Object data;
//    protected String message;

    public NetworkResult(@Nullable Object data) {
//        this.message = message;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public static class Success {
        private Object data;

        public Success(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    public static class Error {
        private Object message;

        public Error(Object message) {
            this.message = message;
        }

        public Object getData() {
            return message;
        }

        public void setData(Object message) {
            this.message = message;
        }
    }

}