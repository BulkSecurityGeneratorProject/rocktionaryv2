import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Cancion } from './cancion.model';
import { createRequestOption } from '../../shared';
import {WindowService} from '../../windowref.service';
import {YoutubeModel} from '../../models/Youtube';

export type EntityResponseType = HttpResponse<Cancion>;

@Injectable()
export class CancionService {

    private resourceUrl =  SERVER_API_URL + 'api/cancions';
    private token = this.windowRef.getNativeWindow().spotifyToken || 'Bearer eqwe';
    private baseUrl = `https://api.spotify.com/v1/tracks`;

    constructor(private http: HttpClient, private windowRef: WindowService) { }


    getCancion (id) {
        const headers = { 'Authorization': this.token };
        return this.http.get(`${this.baseUrl}/${id}`, {headers: headers})
    }


    getCountryFlag (ccode) {
        let url = `http://flagpedia.net/data/flags/normal/${ccode}.png`;

        return this.http.get(url)
    }

    getYoutubeVideo (group, song): Observable<YoutubeModel> {
        return this.http.get(`https://www.googleapis.com/youtube/v3/search?part=snippet&q=${group} ${song + ' lyrics'}}&maxResults=1&key=AIzaSyBh4jKVZPAs4VFdpr2RAdPa_3bHFVRjQXQ&type=video`)
    }

    create(cancion: Cancion): Observable<EntityResponseType> {
        const copy = this.convert(cancion);
        return this.http.post<Cancion>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(cancion: Cancion): Observable<EntityResponseType> {
        const copy = this.convert(cancion);
        return this.http.put<Cancion>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Cancion>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Cancion[]>> {
        const options = createRequestOption(req);
        return this.http.get<Cancion[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Cancion[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Cancion = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Cancion[]>): HttpResponse<Cancion[]> {
        const jsonResponse: Cancion[] = res.body;
        const body: Cancion[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Cancion.
     */
    private convertItemFromServer(cancion: Cancion): Cancion {
        const copy: Cancion = Object.assign({}, cancion);
        return copy;
    }

    /**
     * Convert a Cancion to a JSON which can be sent to the server.
     */
    private convert(cancion: Cancion): Cancion {
        const copy: Cancion = Object.assign({}, cancion);
        return copy;
    }
}
