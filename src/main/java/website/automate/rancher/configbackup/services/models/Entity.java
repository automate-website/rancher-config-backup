package website.automate.rancher.configbackup.services.models;

public class Entity {

    private String id;

    private String name;

    public Entity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
