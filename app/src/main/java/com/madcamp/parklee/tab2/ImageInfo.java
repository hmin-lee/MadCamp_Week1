package com.madcamp.parklee.tab2;

public class ImageInfo {
    private String resId;
    private String title;
    private String datetime;

    private ImageInfo() {

    }

    public String getResId() {
        return resId;
    }

    public String getTitle() {
        return title;
    }

    public String getDatetime() {
        return datetime;
    }

    public static class Builder {
        private String resId; //반드시 필요
        private String title; //선택
        private String datetime; //선택

        public Builder(String resId) {
            this.resId = resId;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDatetime(String datetime) {
            this.datetime = datetime;
            return this;
        }

        public ImageInfo build() {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.resId = resId;
            imageInfo.title = title;
            imageInfo.datetime = datetime;
            return imageInfo;
        }
    }
}
