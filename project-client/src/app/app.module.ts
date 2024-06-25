import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { MaterialModule } from './material.module';
import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { SearchResultsComponent } from './components/search-results/search-results.component';
import { HeaderComponent } from './components/header/header.component';
import { AuthService } from './services/auth.service';
import { TokenInterceptorService } from './services/token-interceptor.service';
import { UserService } from './services/user.service';
import { RestaurantPageComponent } from './components/restaurant-page/restaurant-page.component';
import { ConfirmationService, MessageService } from 'primeng/api';
import { AuthGuard } from './auth.guard';
import { RestaurantService } from './services/restaurant.service';
import { ManagerDashboardComponent } from './components/manager-dashboard/manager-dashboard.component';
import { ReviewService } from './services/review.service';
import { UpvoteService } from './services/upvote.service';
import { ReviewsDashboardComponent } from './components/reviews-dashboard/reviews-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    SearchResultsComponent,
    HeaderComponent,
    RestaurantPageComponent,
    ManagerDashboardComponent,
    ReviewsDashboardComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [AuthGuard, AuthService, UserService, MessageService, ConfirmationService, RestaurantService, ReviewService, UpvoteService,
     { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptorService, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
