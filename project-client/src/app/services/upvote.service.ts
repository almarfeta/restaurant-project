import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UpvoteService {

  private pathUrl: string = "http://localhost:8080/api/v1/upvote";

  constructor(private http: HttpClient) { }

  public addUpVote(reviewId: number): Observable<Object> {
    return this.http.post(this.pathUrl + '/add/' + reviewId, null);
  }

  public getReviewsUpVotedByUser(restaurantId: number): Observable<Object> {
    return this.http.get(this.pathUrl + '/all/upvoted/restaurant/' + restaurantId);
  }

  public deleteUpVote(reviewId: number): Observable<Object> {
    return this.http.delete(this.pathUrl + '/delete/' + reviewId);
  }
}
