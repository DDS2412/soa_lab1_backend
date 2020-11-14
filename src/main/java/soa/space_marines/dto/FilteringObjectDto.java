package soa.space_marines.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import soa.space_marines.enums.AstartesCategory;
import soa.space_marines.enums.MeleeWeapon;

@Data
@Accessors(chain = true)
public class FilteringObjectDto {
    private String name;
    private Long health;
    private Integer height;
    private AstartesCategory category;
    private MeleeWeapon meleeWeapon;
    private String chapterName;
    private Integer chapterMarinesCount;
    private Double xPosition;
    private Long yPosition;
}
