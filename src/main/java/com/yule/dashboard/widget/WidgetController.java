package com.yule.dashboard.widget;

import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.widget.model.data.req.WidgetAddData;
import com.yule.dashboard.widget.model.data.req.WidgetPatchData;
import com.yule.dashboard.widget.model.data.resp.AddWidgetData;
import com.yule.dashboard.widget.model.data.resp.WidgetData;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/widget")
@Slf4j
public class WidgetController {
    private final WidgetService widgetService;


    @PostMapping
    public AddWidgetData addWidget(@RequestBody WidgetAddData data) {
        return widgetService.addWidget(data);
    }

    @GetMapping("/{page}")
    public List<WidgetData> getAllWidgets(@Min(1) @PathVariable int page) {
        return widgetService.getAllWidgets(page);
    }

    @PatchMapping
    public BaseResponse patchWidget(@RequestBody WidgetPatchData data) {
        return widgetService.patchWidget(data);
    }

    @DeleteMapping("/{id}")
    public BaseResponse removeWidget(@Min(1) @PathVariable Long id) {
        return widgetService.removeWidget(id);
    }
    @DeleteMapping("/all")
    public BaseResponse removeAllWidget() {
        return widgetService.removeAllWidget();
    }


}
