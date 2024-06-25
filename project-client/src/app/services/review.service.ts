import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private pathUrl: string = "http://localhost:8080/api/v1/review";

  constructor(private http: HttpClient) { }

  public addReview(review: any): Observable<Object> {
    return this.http.post(this.pathUrl + '/add', review);
  }

  public getReviewsForRestaurant(restaurantId: number): Observable<Object> {
    return this.http.get(this.pathUrl + '/all/restaurant/' + restaurantId);
  }

  public getReviewsForAuthor(): Observable<Object> {
    return this.http.get(this.pathUrl + '/all/author');
  }

  public getReviewsForAuthorAndRestaurant(restaurantId: number): Observable<Object> {
    return this.http.get(this.pathUrl + '/all/author/and/restaurant/' + restaurantId);
  }

  public deleteReview(id: number): Observable<Object> {
    return this.http.delete(this.pathUrl + '/delete/' + id);
  }
}
