import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptorService implements HttpInterceptor {

  private publicPaths: string[] = [
    "http://localhost:8080/api/v1/auth/register",
    "http://localhost:8080/api/v1/auth/authenticate",
    "http://localhost:8080/api/v1/auth/logout",
    "http://localhost:8080/api/v1/restaurant/all",
    "http://localhost:8080/api/v1/restaurant/by",
    "http://localhost:8080/api/v1/review/all/restaurant"
  ]

  public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    for (let i = 0; i < this.publicPaths.length; i++) {
      if (req.url.startsWith(this.publicPaths[i])) {
        return next.handle(req);
      }
    }
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    });
    return next.handle(req);
  }
}
