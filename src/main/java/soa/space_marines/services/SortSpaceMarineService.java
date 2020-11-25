package soa.space_marines.services;

import soa.space_marines.comparators.*;
import soa.space_marines.exception.BadSortException;
import soa.space_marines.models.SpaceMarine;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortSpaceMarineService {
    public List<SpaceMarine> sortByParams(List<SpaceMarine> spaceMarines, String[] sortParams, String sortState) throws BadSortException{
        sortState = sortState == null ? "asc" : sortState;
        for (String param : sortParams){
            if (param.equals("")){
                continue;
            }

            spaceMarines = spaceMarines
                    .stream()
                    .sorted(getComparator(param, sortState))
                    .collect(Collectors.toList());
        }

        return spaceMarines;
    }

    private Comparator<SpaceMarine> getComparator(String param, String sortState) throws BadSortException {
        Boolean sortStateBoolean = sortState.equals("asc");

        switch (param){
            case "name":{
                return new SpaceMarineNameComparator(sortStateBoolean);
            }
            case "health": {
                return new SpaceMarineHealthComparator(sortStateBoolean);
            }
            case "category": {
                return new SpaceMarineCategoryComparator(sortStateBoolean);
            }
            case "meleeWeapon": {
                return new SpaceMarineMeleeWeaponComparator(sortStateBoolean);
            }
            case "chapterName": {
                return new SpaceMarineChapterNameComparator(sortStateBoolean);
            }
            case "chapterMarinesCount": {
                return new SpaceMarineChapterMarinesCountComparator(sortStateBoolean);
            }
            case "xPosition": {
                return new SpaceMarineXPositionComparator(sortStateBoolean);
            }
            case "yPosition": {
                return new SpaceMarineYPositionComparator(sortStateBoolean);
            }
            default:{
                throw new BadSortException();
            }
        }
    }
}
