package id.klinikrumah.internal.model;

public class Image {
    private String id;
    private String image;
    private String path;
    private String type;

    public Image(){}

    public Image(String id, String image, String path, String type){
        this.id = id;
        this.image = image;
        this.path = path;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
