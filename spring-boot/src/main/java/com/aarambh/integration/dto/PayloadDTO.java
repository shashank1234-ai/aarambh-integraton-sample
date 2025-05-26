package com.aarambh.integration.dto;

public class PayloadDTO {
    private Context context;
    private Message message;

    // Getters and Setters

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static class Context {
        private String bpp_uri;
        private String action;

        public String getBpp_uri() {
            return bpp_uri;
        }

        public void setBpp_uri(String bpp_uri) {
            this.bpp_uri = bpp_uri;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public static class Message {
        private String order_id;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }
    }
}
