package com.example.application.views.list;

import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll //@PermitAll аннотации к обоим представлениям, чтобы все вошедшие в систему пользователи могли получить к ним доступ.
@Route(value = "dashboard", layout = MainLayout.class) //12.1
@PageTitle("Dashboard | Vaadin CRM")
public class DashboardView extends VerticalLayout {
    private final CrmService service;

    public DashboardView(CrmService service) { //12.2
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); //12.3
        add(getContactStats(), getCompaniesChart());
    }

    private Component getContactStats() {
        Span stats = new Span(service.countContacts() + " contacts"); //12.4
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Chart getCompaniesChart() {
        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();
        service.findAllCompanies().forEach(company ->
                dataSeries.add(new DataSeriesItem(company.getName(), company.getEmployeeCount()))); //12.5
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }
}

/*
12.1 DashboardView сопоставляется с "dashboard" путем и используется MainLayout в качестве родительского макета.
12.2 Принимает CrmService в качестве параметра конструктора и сохраняет его как поле.
12.3 Центрирует содержимое макета.
12.4 Вызывает службу, чтобы получить количество контактов.
12.5 Вызывает службу, чтобы получить данные обо всех компаниях, затем создает DataSeriesItem для каждой службу, содержащую название компании и количество сотрудников. Не беспокойтесь об ошибке компиляции: отсутствующий метод будет добавлен на следующем шаге.
 */