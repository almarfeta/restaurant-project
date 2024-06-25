import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private pathUrl: string = "http://localhost:8080/api/v1/user";

  constructor(private http: HttpClient) { }

  public checkAuthStatus(): Observable<Object> {
    return this.http.get(this.pathUrl + "/check");
  }

  public getUserData(): Observable<Object> {
    return this.http.get(this.pathUrl + "/data");
  }
}
