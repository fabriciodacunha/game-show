import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlay, getPlayIdentifier } from '../play.model';

export type EntityResponseType = HttpResponse<IPlay>;
export type EntityArrayResponseType = HttpResponse<IPlay[]>;

@Injectable({ providedIn: 'root' })
export class PlayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(play: IPlay): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(play);
    return this.http
      .post<IPlay>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(play: IPlay): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(play);
    return this.http
      .put<IPlay>(`${this.resourceUrl}/${getPlayIdentifier(play) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(play: IPlay): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(play);
    return this.http
      .patch<IPlay>(`${this.resourceUrl}/${getPlayIdentifier(play) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPlay>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlay[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlayToCollectionIfMissing(playCollection: IPlay[], ...playsToCheck: (IPlay | null | undefined)[]): IPlay[] {
    const plays: IPlay[] = playsToCheck.filter(isPresent);
    if (plays.length > 0) {
      const playCollectionIdentifiers = playCollection.map(playItem => getPlayIdentifier(playItem)!);
      const playsToAdd = plays.filter(playItem => {
        const playIdentifier = getPlayIdentifier(playItem);
        if (playIdentifier == null || playCollectionIdentifiers.includes(playIdentifier)) {
          return false;
        }
        playCollectionIdentifiers.push(playIdentifier);
        return true;
      });
      return [...playsToAdd, ...playCollection];
    }
    return playCollection;
  }

  protected convertDateFromClient(play: IPlay): IPlay {
    return Object.assign({}, play, {
      playData: play.playData?.isValid() ? play.playData.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.playData = res.body.playData ? dayjs(res.body.playData) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((play: IPlay) => {
        play.playData = play.playData ? dayjs(play.playData) : undefined;
      });
    }
    return res;
  }
}
