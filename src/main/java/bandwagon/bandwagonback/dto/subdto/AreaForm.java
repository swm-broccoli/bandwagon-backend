package bandwagon.bandwagonback.dto.subdto;

import bandwagon.bandwagonback.domain.Area;
import lombok.Data;

@Data
public class AreaForm {

    private Long id;
    private String city;
    private String district;

    public AreaForm(Area area) {
        this.id = area.getId();
        this.city = area.getCity();
        this.district = area.getDistrict();
    }
}
