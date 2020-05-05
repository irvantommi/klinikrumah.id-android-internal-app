package id.klinikrumah.internal.model;

public class KRFile {
    private String id;
    private String path;
    private String type;

    public KRFile(){}

    public KRFile(String id, String path, String type){
        this.id = id;
        this.path = path;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
