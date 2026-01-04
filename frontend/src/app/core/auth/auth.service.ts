import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, of, switchMap, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private _authenticated: boolean = false;
    private _httpClient = inject(HttpClient);

    // -----------------------------------------------------------------------------------------------------
    // @ Accessors
    // -----------------------------------------------------------------------------------------------------

    /**
     * Setter & getter for access token
     */
    set accessToken(token: string) {
        sessionStorage.setItem('accessToken', token);
    }

    get accessToken(): string {
        return sessionStorage.getItem('accessToken') ?? '';
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Forgot password
     *
     * @param email
     */
    forgotPassword(email: string): Observable<any> {
        return this._httpClient.post('api/auth/forgot-password', email);
    }

    /**
     * Reset password
     *
     * @param password
     */
    resetPassword(password: string): Observable<any> {
        return this._httpClient.post('api/auth/reset-password', password);
    }

    /**
     * Sign in
     *
     * @param credentials
     */
    // signIn1(credentials: { username: string; password: string }): Observable<any> {
    //     // Throw error, if the user is already logged in
    //     if (this._authenticated) {
    //         return throwError('User is already logged in.');
    //     }

    //     // Store the access token in the local storage
    //     this.accessToken =
    //         'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTc0ODk0MDIzNn0.KoGIyPqwLPV_6az4iHv_CgKYI-bPBtmRimT3VkA94rA';

    //     // Set the authenticated flag to true
    //     this._authenticated = true;

    //     // Store the user on the user service
    //     this._userService.user = {
    //         name: 'dasw',
    //         email: credentials.username,
    //         id: '1',
    //     };

    //     return of({
    //         name: 'dasw',
    //         email: credentials.username,
    //         id: '1',
    //     });

    //     // return this._httpClient.post('api/auth/sign-in', credentials).pipe(
    //     //     switchMap((response: any) => {
    //     //         // Store the access token in the local storage
    //     //         this.accessToken = response.accessToken;

    //     //         // Set the authenticated flag to true
    //     //         this._authenticated = true;

    //     //         // Store the user on the user service
    //     //         this._userService.user = response.user;

    //     //         // Return a new observable with the response
    //     //         return of(response);
    //     //     })
    //     // );
    // }

    // signIn(bool,response,val): Observable<any>
    // {
    //     // Throw error, if the user is already logged in
    //     if ( this._authenticated )
    //     {
    //         // return throwError('User is already logged in.');
    //     } 

    //         if(bool){
    //             this.accessToken = response.access_token;
    //         sessionStorage.setItem('idToken',response.id_token)
    //         sessionStorage.setItem('refreshToken',response.refresh_token)
    //             // Set the authenticated flag to true
    //             this._authenticated = true;
    //             let user_data  = this._jwtService.getDecodedAccessToken(sessionStorage.getItem('idToken'))

    //             let user = {
    //                 id: user_data.name,
    //                 name: user_data.name,
    //                 email: user_data.email,
    //                 avatar: user_data.picture ? user_data.picture : null,
    //                 role: user_data.role,
    //                 auth: user_data.auth
    //             }
    //             // Store the user on the user service
    //             this._userService.user = user;

    //             // Store the user on the user service
    //             // this._userService.user = response.user;

    //             // Return a new observable with the response
    //             // this.triggerSetting();
    //             return of(response);
    //         }

               
            
    // }

    /**
     * Sign in using the access token
     */
    // signInUsingToken(): Observable<any> {
    //     // Sign in using the token
    //     return this._httpClient
    //         .post('api/auth/sign-in-with-token', {
    //             accessToken: this.accessToken,
    //         })
    //         .pipe(
    //             catchError(() =>
    //                 // Return false
    //                 of(false)
    //             ),
    //             switchMap((response: any) => {
    //                 // Replace the access token with the new one if it's available on
    //                 // the response object.
    //                 //
    //                 // This is an added optional step for better security. Once you sign
    //                 // in using the token, you should generate a new one on the server
    //                 // side and attach it to the response object. Then the following
    //                 // piece of code can replace the token with the refreshed one.
    //                 if (response.accessToken) {
    //                     this.accessToken = response.accessToken;
    //                 }

    //                 // Set the authenticated flag to true
    //                 this._authenticated = true;

    //                 // Store the user on the user service
    //                 this._userService.user = response.user;

    //                 // Return true
    //                 return of(true);
    //             })
    //         );
    // }

    /**
     * Sign out
     */
    signOut(): Observable<any> {
        // Remove the access token from the local storage
        sessionStorage.removeItem('accessToken');
        sessionStorage.clear();
        // Set the authenticated flag to false
        this._authenticated = false;
        document.title = 'IPM IOT';

        // Return the observable
        return of(true);
    }

    /**
     * Sign up
     *
     * @param user
     */
    signUp(user: {
        name: string;
        email: string;
        password: string;
        company: string;
    }): Observable<any> {
        return this._httpClient.post('api/auth/sign-up', user);
    }

    /**
     * Unlock session
     *
     * @param credentials
     */
    unlockSession(credentials: {
        email: string;
        password: string;
    }): Observable<any> {
        return this._httpClient.post('api/auth/unlock-session', credentials);
    }

    /**
     * Check the authentication status
     */
    check(): Observable<boolean> {
        // Check if the user is logged in
        if (this._authenticated) {
            return of(true);
        }

        // Check the access token availability
        if (!this.accessToken) {
            return of(false);
        }

        // Check the access token expire date
        // if (AuthUtils.isTokenExpired(this.accessToken)) {
        //     return of(false);
        // }

        // If the access token exists, and it didn't expire, sign in using it
        // return this.signInUsingToken();
        return of(true);
    }

    getAccessToken(): string | null {
        return this.accessToken;
    }
}
