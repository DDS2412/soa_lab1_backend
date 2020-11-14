package soa.space_marines.services;

import soa.space_marines.daos.SpaceMarineDAO;
import soa.space_marines.dto.FilteringObjectDto;
import soa.space_marines.dto.PageableSpaceMarinesDto;
import soa.space_marines.enums.AstartesCategory;
import soa.space_marines.models.SpaceMarine;

import java.util.ArrayList;
import java.util.List;

public class SpaceMarineService {
    private final SpaceMarineDAO spaceMarineDAO;
    private final FilteringSpaceMarineService filteringSpaceMarineService;
    private final SortSpaceMarineService sortSpaceMarineService;

    public SpaceMarineService(){
        this.spaceMarineDAO = new SpaceMarineDAO();
        this.filteringSpaceMarineService = new FilteringSpaceMarineService();
        this.sortSpaceMarineService = new SortSpaceMarineService();
    }

    public void saveSpaceMarine(SpaceMarine spaceMarine){
        this.spaceMarineDAO.save(spaceMarine);
    }

    public void updateSpaceMarine(SpaceMarine spaceMarine){
        this.spaceMarineDAO.upate(spaceMarine);
    }

    public SpaceMarine deleteSpaceMarineById(Integer id){
        SpaceMarine spaceMarine = this.spaceMarineDAO.findById(id);
        if (spaceMarine == null)
            return null;

        return this.spaceMarineDAO.delete(spaceMarine);
    }

    public void deleteSpaceMarineByHealth(long health){
        List<SpaceMarine> spaceMarines = this.spaceMarineDAO.findByHealth(health);
        spaceMarines.forEach(spaceMarine -> this.spaceMarineDAO.delete(spaceMarine));
    }

    public float calculateHealthMeanValue(List<Integer> spaceMarinesId){
        List<SpaceMarine> spaceMarines = new ArrayList<>();

        for (Integer id : spaceMarinesId){
            SpaceMarine spaceMarine = this.spaceMarineDAO.findById(id);
            if (spaceMarine != null){
                spaceMarines.add(spaceMarine);
            }
        }

        float result = 0;
        for (SpaceMarine spaceMarine : spaceMarines){
            result += (float) spaceMarine.getHealth();
        }

        if (spaceMarines.size() == 0) return -1;

        return result / spaceMarines.size();
    }

    public PageableSpaceMarinesDto findAllSpaceMarines(
            Integer atPage,
            Integer pageNumber,
            FilteringObjectDto filteringObjectDto,
            String[] sortParams,
            String sortState){
        List<SpaceMarine> spaceMarines = this.spaceMarineDAO.findAll();
        spaceMarines = filteringSpaceMarineService.filterBy(spaceMarines, filteringObjectDto);

        spaceMarines = sortSpaceMarineService.sortByParams(spaceMarines, sortParams, sortState);

        return getPageableSpaceMarinesDto(spaceMarines, atPage, pageNumber);
    }

    public SpaceMarine findSpaceMarine(Integer id){
        return this.spaceMarineDAO.findById(id);
    }

    public List<SpaceMarine> findSpaceMarineWhenCategoryGreater(Integer intCategory){
        if (intCategory > AstartesCategory.values().length - 1) return null;

        return this.spaceMarineDAO.findSpaceMarineWhenCategoryGreater(AstartesCategory.values()[intCategory]);
    }

    private PageableSpaceMarinesDto getPageableSpaceMarinesDto(List<SpaceMarine> spaceMarines, Integer atPage, Integer pageNumber){
        Integer totalPages;
        Integer elementsAtCurrentPage;

        if (spaceMarines.size() % atPage == 0){
            totalPages = spaceMarines.size() / atPage;
        } else {
            totalPages = spaceMarines.size() / atPage + 1;
        }

        if (totalPages < pageNumber) return null;
        if (totalPages == 0) return null;

        if (totalPages.equals(pageNumber)){
            elementsAtCurrentPage = (spaceMarines.size() - (pageNumber - 1) * atPage) % (atPage + 1);
        } else {
            elementsAtCurrentPage = atPage;
        }

        Integer firstElementId = (pageNumber - 1) * atPage;
        Integer lastElementId = firstElementId + elementsAtCurrentPage;

        List<SpaceMarine> resultSpaceMarines = spaceMarines.subList(firstElementId, lastElementId);

        return new PageableSpaceMarinesDto(resultSpaceMarines, pageNumber, totalPages,  atPage, spaceMarines.size(), totalPages.equals(pageNumber));
    }
}
