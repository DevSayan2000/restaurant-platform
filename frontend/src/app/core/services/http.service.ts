import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface HttpOptions {
  headers?: HttpHeaders | { [header: string]: string | string[] };
  params?: HttpParams | { [param: string]: string | number | boolean };
  responseType?: 'json' | 'blob' | 'text';
  observe?: 'body' | 'response';
}

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  private readonly baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  /* ---------------- GET ---------------- */

  get<T>(url: string, options?: HttpOptions & { observe?: 'body' }): Observable<T>;
  get<T>(url: string, options: HttpOptions & { observe: 'response' }): Observable<HttpResponse<T>>;
  get<T>(url: string, options?: HttpOptions): Observable<any> {
    return this.http.get(this.buildUrl(url), options as any);
  }

  /* ---------------- POST ---------------- */

  post<T>(url: string, body: unknown, options?: HttpOptions & { observe?: 'body' }): Observable<T>;
  post<T>(
    url: string,
    body: unknown,
    options: HttpOptions & { observe: 'response' }
  ): Observable<HttpResponse<T>>;
  post<T>(url: string, body: unknown, options?: HttpOptions): Observable<any> {
    return this.http.post(this.buildUrl(url), body, options as any);
  }

  /* ---------------- PUT ---------------- */

  put<T>(url: string, body: unknown, options?: HttpOptions & { observe?: 'body' }): Observable<T>;
  put<T>(
    url: string,
    body: unknown,
    options: HttpOptions & { observe: 'response' }
  ): Observable<HttpResponse<T>>;
  put<T>(url: string, body: unknown, options?: HttpOptions): Observable<any> {
    return this.http.put(this.buildUrl(url), body, options as any);
  }

  /* ---------------- PATCH ---------------- */

  patch<T>(url: string, body: unknown, options?: HttpOptions & { observe?: 'body' }): Observable<T>;
  patch<T>(
    url: string,
    body: unknown,
    options: HttpOptions & { observe: 'response' }
  ): Observable<HttpResponse<T>>;
  patch<T>(url: string, body: unknown, options?: HttpOptions): Observable<any> {
    return this.http.patch(this.buildUrl(url), body, options as any);
  }

  /* ---------------- DELETE ---------------- */

  delete<T>(url: string, options?: HttpOptions & { observe?: 'body' }): Observable<T>;
  delete<T>(
    url: string,
    options: HttpOptions & { observe: 'response' }
  ): Observable<HttpResponse<T>>;
  delete<T>(url: string, options?: HttpOptions): Observable<any> {
    return this.http.delete(this.buildUrl(url), options as any);
  }

  /* ---------------- UTIL ---------------- */

  private buildUrl(endpoint: string): string {
    if (endpoint.startsWith('http')) {
      return endpoint;
    }

    return `${this.baseUrl.replace(/\/$/, '')}/${endpoint.replace(/^\//, '')}`;
  }
}
