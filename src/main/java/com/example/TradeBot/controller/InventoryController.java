package com.example.TradeBot.controller;

import com.example.TradeBot.dto.PageRequest;
import com.example.TradeBot.service.inventory.InventoryServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ua.avada.dentalcolouradminpanel.dto.country.CountryResponse;
import ua.avada.dentalcolouradminpanel.dto.statistic.request.StatisticsChartRequest;
import ua.avada.dentalcolouradminpanel.dto.statistic.response.*;
import ua.avada.dentalcolouradminpanel.service.statistic.StatisticService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class InventoryController {
    private final InventoryServiceImpl statisticService;

    @RequestMapping({"/", ""})
    public RedirectView redirectToIndex() {
        return new RedirectView("statistics");
    }

    @RequestMapping("/inventory")
    public ModelAndView showInventoryPage(Model model) {
        return new ModelAndView("pages/inventory");
    }

    @PostMapping("/inventory/getPage")
    @ResponseBody
    public ResponseEntity<?> getOrderChart(
            @Valid @RequestBody PageRequest pageRequest,
            BindingResult bindingResult
    ){
        // validation
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        //action
        return null;
    }

}
