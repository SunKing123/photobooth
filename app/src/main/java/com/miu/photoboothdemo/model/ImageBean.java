package com.miu.photoboothdemo.model;

public class ImageBean {


    private String name;

    private String path;

    private long createTime;

    public ImageBean(String name, String path, long createTime) {
        this.name = name;
        this.path = path;
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }


    public String getPath() {
        return path;
    }


    public long getCreateTime() {
        return createTime;
    }



}
