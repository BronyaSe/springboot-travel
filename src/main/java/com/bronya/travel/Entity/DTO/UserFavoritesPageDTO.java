package com.bronya.travel.Entity.DTO;

import com.bronya.travel.Entity.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFavoritesPageDTO {
    private List<Route> favorites;
    private Long total;
}
