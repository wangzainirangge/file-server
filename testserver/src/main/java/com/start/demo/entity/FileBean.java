package com.start.demo.entity;

public class FileBean {

    String id;
    String fileSize;
    String fileType;
    String originalName;
    String createTime;
    String digitalEnvelope;

    public FileBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getDigitalEnvelope() {
        return digitalEnvelope;
    }

    public void setDigitalEnvelope(String digitalEnvelope) {
        this.digitalEnvelope = digitalEnvelope;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "id='" + id + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileType='" + fileType + '\'' +
                ", originalName='" + originalName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", digitalEnvelope='" + digitalEnvelope + '\'' +
                '}';
    }
}
