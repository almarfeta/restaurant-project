import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegisterUserData } from '../interfaces/register-user-data';
import { LoginUserData } from '../interfaces/login-user-data';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private pathUrl: string = "http://localhost:8080/api/v1/auth";

  constructor(private http: HttpClient) { }

  public registerUser(user: RegisterUserData): Observable<Object> {
    return this.http.post(this.pathUrl + "/register", user);
  }

  public loginUser(user: LoginUserData): Observable<Object> {
    return this.http.post(this.pathUrl + "/authenticate", user);
  }

  public logoutUser(): Observable<Object> {
    return this.http.post(this.pathUrl + "/logout", null);
  }
}
