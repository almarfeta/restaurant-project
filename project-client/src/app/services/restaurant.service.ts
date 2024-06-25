import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {

  private pathUrl: string = "http://localhost:8080/api/v1/restaurant";

  constructor(private http: HttpClient) { }

  public addRestaurant(restaurant: any): Observable<Object> {
    return this.http.post(this.pathUrl + '/add', restaurant);
  }

  public addRestaurantPicture(picture: any): Observable<Object> {
    return this.http.post(this.pathUrl + '/add/picture', picture);
  }

  public getRestaurants(page: number, rows: number, sortField: string, sortOrder: string): Observable<Object> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', rows.toString())
      .set('sortField', sortField)
      .set('sortOrder', sortOrder);
    return this.http.get(this.pathUrl + '/all', {params: params})
  }

  public getRestaurantsByName(name: string, page: number, rows: number, sortField: string, sortOrder: string): Observable<Object> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', rows.toString())
      .set('sortField', sortField)
      .set('sortOrder', sortOrder);
    return this.http.get(this.pathUrl + '/all/name/' + name, {params: params});
  }

  public getRestaurantsManagedByUser(): Observable<Object> {
    return this.http.get(this.pathUrl + "/managed");
  }

  public getRestaurantById(id: number): Observable<Object> {
    return this.http.get(this.pathUrl + '/by/name/' + id);
  }

  public updateRestaurant(restaurant: any): Observable<Object> {
    return this.http.put(this.pathUrl + '/update', restaurant);
  }

  public deleteRestaurant(id: number): Observable<Object> {
    return this.http.delete(this.pathUrl + '/delete/' + id);
  }

  public deletePicture(restaurantId: number, pictureId: number): Observable<Object> {
    return this.http.delete(this.pathUrl + '/delete/picture?restaurantId=' + restaurantId + '&pictureId=' + pictureId);
  }
}
