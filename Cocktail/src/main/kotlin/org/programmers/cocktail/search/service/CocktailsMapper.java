package org.programmers.cocktail.search.service;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CocktailsMapper {

    @Autowired
    private ModelMapper modelMapper;

    public List<CocktailsTO> convertToCocktailsTOList(List<Cocktails> cocktailsList) {
        List<CocktailsTO> cocktailsTOList = cocktailsList.stream()
            .map(cocktails -> modelMapper.map(cocktails, CocktailsTO.class))
            .collect(Collectors.toList());
        return cocktailsTOList;
    }

    public List<Cocktails> convertToCocktailsList(List<CocktailsTO> cocktailsTOList) {
        List<Cocktails> cocktailsList = cocktailsTOList.stream()
            .map(cocktailsTO -> modelMapper.map(cocktailsTO, Cocktails.class))
            .collect(Collectors.toList());
        return cocktailsList;
    }

    public Cocktails convertToCocktails(CocktailsTO cocktailsTO) {
        return modelMapper.map(cocktailsTO, Cocktails.class);
    }

    public CocktailsTO convertToCocktailsTO(Cocktails cocktails) {
        return modelMapper.map(cocktails, CocktailsTO.class);
    }

}
