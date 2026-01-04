import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface HttpOptions {
  headers?: HttpHeaders | { [header: string]: string | string[] };
  params?: HttpParams | { [param: string]: string | number | boolean };
  responseType?: 'json';
  observe?: 'body';
}

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  private readonly baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  /** GET */
  get<T>(url: string, options?: HttpOptions): Observable<T> {
    return this.http.get<T>(this.buildUrl(url), options);
  }

  /** POST */
  post<T>(url: string, body: unknown, options?: HttpOptions): Observable<T> {
    return this.http.post<T>(this.buildUrl(url), body, options);
  }

  /** PUT */
  put<T>(url: string, body: unknown, options?: HttpOptions): Observable<T> {
    return this.http.put<T>(this.buildUrl(url), body, options);
  }

  /** PATCH */
  patch<T>(url: string, body: unknown, options?: HttpOptions): Observable<T> {
    return this.http.patch<T>(this.buildUrl(url), body, options);
  }

  /** DELETE */
  delete<T>(url: string, options?: HttpOptions): Observable<T> {
    return this.http.delete<T>(this.buildUrl(url), options);
  }

  /** Builds full API URL */
  private buildUrl(endpoint: string): string {
    if (endpoint.startsWith('http')) {
      return endpoint; // allow absolute URLs
    }

    return `${this.baseUrl.replace(/\/$/, '')}/${endpoint.replace(/^\//, '')}`;
  }
}
