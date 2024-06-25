import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { SearchResultsComponent } from './components/search-results/search-results.component';
import { RestaurantPageComponent } from './components/restaurant-page/restaurant-page.component';
import { AuthGuard } from './auth.guard';
import { ManagerDashboardComponent } from './components/manager-dashboard/manager-dashboard.component';
import { ReviewsDashboardComponent } from './components/reviews-dashboard/reviews-dashboard.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/search-results/all',
    pathMatch: 'full'
  },
  {
    path: 'search-results/:key',
    component: SearchResultsComponent
  },
  {
    path: 'restaurant-page/:id',
    component: RestaurantPageComponent
  },
  {
    path: 'my-reviews',
    component: ReviewsDashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'dashboard',
    component: ManagerDashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
