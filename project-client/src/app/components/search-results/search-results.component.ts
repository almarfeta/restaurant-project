import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {RestaurantService} from 'src/app/services/restaurant.service';
import {RestaurantSearchData} from 'src/app/interfaces/restaurant-search-data';

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html'
})
export class SearchResultsComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute,
              private restaurantService: RestaurantService) {
  }

  public keyWord: string = '';
  public restaurants: RestaurantSearchData[] = [];

  sortField: string = 'name';
  sortOrder: string = 'asc';

  first: number = 0;
  page: number = 0;
  rows: number = 10;
  totalRecords: number = 0;

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params: ParamMap) => {
      this.keyWord = params.get('key')!;
      this.fetchData();
    });
  }

  fetchData() {
    if (this.keyWord === 'all') {
      this.getRestaurants();
    } else {
      this.getRestaurantsByName();
    }
  }

  getRestaurants() {
    this.restaurantService.getRestaurants(this.page, this.rows, this.sortField, this.sortOrder).subscribe(
      (response: any) => {
        this.restaurants = response.data.page.restaurants;
        this.totalRecords = response.data.page.totalElements;
      }
    );
  }

  getRestaurantsByName() {
    this.restaurantService.getRestaurantsByName(this.keyWord, this.page, this.rows, this.sortField, this.sortOrder).subscribe(
      (response: any) => {
        this.restaurants = response.data.page.restaurants;
        this.totalRecords = response.data.page.totalElements;
      }
    );
  }

  sort(sortBy: string, sortOrder: string) {
    this.sortField = sortBy;
    this.sortOrder = sortOrder;
    this.fetchData();
  }

  onPageChange(event: any) {
    this.first = event.first;
    this.page = event.page;
    this.rows = event.rows;
    this.fetchData();
  }
}
