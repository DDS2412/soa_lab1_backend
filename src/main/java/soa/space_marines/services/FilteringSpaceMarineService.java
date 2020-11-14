package soa.space_marines.services;

import soa.space_marines.dto.FilteringObjectDto;
import soa.space_marines.enums.AstartesCategory;
import soa.space_marines.enums.MeleeWeapon;
import soa.space_marines.models.SpaceMarine;
import soa.space_marines.utils.Converter;

import java.util.*;
import java.util.stream.Collectors;

public class FilteringSpaceMarineService {
    public List<SpaceMarine> filterBy(List<SpaceMarine> spaceMarines, FilteringObjectDto filteringObjectDto){
        return spaceMarines
                .stream()
                .filter(spaceMarine -> filterByArgs(spaceMarine, filteringObjectDto))
                .collect(Collectors.toList());
    }

    public FilteringObjectDto prepareFilteringObjectDto(Map<String, String[]> filterReq){
        String[] filterParams = filterReq.get("filter");
        FilteringObjectDto filteringObjectDto = new FilteringObjectDto();

        if (filterParams == null){
            return filteringObjectDto;
        }

        for (String param : filterParams){
            String[] values = filterReq.get(param);
            if (values == null) continue;
            if (values.length != 1) continue;

            switch (param){
                case "name": {
                    filteringObjectDto.setName(values[0].toLowerCase());
                    break;
                }
                case "health": {
                    filteringObjectDto.setHealth(Converter.longConvert(values[0]));
                    break;
                }
                case "height": {
                    filteringObjectDto.setHeight(Converter.intConvert(values[0]));
                    break;
                }
                case "category": {
                    Optional<AstartesCategory> category = Arrays.stream(AstartesCategory.values())
                            .filter(c -> c.ordinal() == Converter.intConvert(values[0]))
                            .findFirst();
                    filteringObjectDto.setCategory(category.orElse(null));
                    break;
                }
                case "meleeWeapon": {
                    Optional<MeleeWeapon> meleeWeapon = Arrays.stream(MeleeWeapon.values())
                            .filter(w -> w.ordinal() == Converter.intConvert(values[0]))
                            .findFirst();
                    filteringObjectDto.setMeleeWeapon(meleeWeapon.orElse(null));
                    break;
                }
                case "chapterName": {
                    filteringObjectDto.setChapterName(values[0].toLowerCase());
                    break;
                }
                case "chapterMarinesCount": {
                    filteringObjectDto.setChapterMarinesCount(Converter.intConvert(values[0]));
                    break;
                }
                case "xPosition": {
                    filteringObjectDto.setXPosition(Converter.doubleConvert(values[0]));
                    break;
                }
                case "yPosition": {
                    filteringObjectDto.setYPosition(Converter.longConvert(values[0]));
                    break;
                }
            }
        }
        return filteringObjectDto;
    }

    private boolean filterByArgs(SpaceMarine spaceMarine, FilteringObjectDto filteringObjectDto) {
        boolean isValid = true;

        if (filteringObjectDto.getName() != null)
            isValid &= filteringObjectDto.getName().equals(spaceMarine.getName().toLowerCase());
        if (filteringObjectDto.getHealth() != null)
            isValid &= filteringObjectDto.getHealth().equals(spaceMarine.getHealth());
        if (filteringObjectDto.getHeight() != null)
            isValid &= filteringObjectDto.getHeight().equals(spaceMarine.getHeight());
        if (filteringObjectDto.getCategory() != null)
            isValid &= filteringObjectDto.getCategory().equals(spaceMarine.getCategory());
        if (filteringObjectDto.getMeleeWeapon() != null)
            isValid &= filteringObjectDto.getMeleeWeapon().equals(spaceMarine.getMeleeWeapon());
        if (filteringObjectDto.getChapterName() != null)
            isValid &= filteringObjectDto.getChapterName().equals(spaceMarine.getChapter().getName().toLowerCase());
        if (filteringObjectDto.getChapterMarinesCount() != null)
            isValid &= filteringObjectDto.getChapterMarinesCount().equals(spaceMarine.getChapter().getMarinesCount());
        if (filteringObjectDto.getXPosition() != null)
            isValid &= filteringObjectDto.getXPosition().equals(spaceMarine.getCoordinates().getX());
        if (filteringObjectDto.getYPosition() != null)
            isValid &= filteringObjectDto.getYPosition().equals(spaceMarine.getCoordinates().getY());

        return isValid;
    }
}
