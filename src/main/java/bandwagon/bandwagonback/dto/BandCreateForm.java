package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class BandCreateForm {

    private String name;
    private String description;
    private String avatarUrl;

    public BandCreateForm () {}

    public BandCreateForm (String name, String description,String avatarUrl) {
        this.name = name;
        this.description = description;
        this.avatarUrl = avatarUrl;
    }
}
