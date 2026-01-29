package io.github.dbc2201.cineconnectgraphqldemo.graphql;

import io.github.dbc2201.cineconnectgraphqldemo.security.CineConnectUserDetailsService.CineConnectUserPrincipal;
import io.github.dbc2201.cineconnectgraphqldemo.service.DashboardService;
import io.github.dbc2201.cineconnectgraphqldemo.service.DashboardService.PlatformStats;
import io.github.dbc2201.cineconnectgraphqldemo.service.DashboardService.UserDashboard;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

/**
 * GraphQL controller for dashboard and statistics queries.
 */
@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @QueryMapping
    public UserDashboard myDashboard(@AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return dashboardService.getUserDashboard(principal.getId());
    }

    @QueryMapping
    public PlatformStats platformStats() {
        return dashboardService.getPlatformStats();
    }
}
