import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParticipante, getParticipanteIdentifier } from '../participante.model';

export type EntityResponseType = HttpResponse<IParticipante>;
export type EntityArrayResponseType = HttpResponse<IParticipante[]>;

@Injectable({ providedIn: 'root' })
export class ParticipanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/participantes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(participante: IParticipante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(participante);
    return this.http
      .post<IParticipante>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(participante: IParticipante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(participante);
    return this.http
      .put<IParticipante>(`${this.resourceUrl}/${getParticipanteIdentifier(participante) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(participante: IParticipante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(participante);
    return this.http
      .patch<IParticipante>(`${this.resourceUrl}/${getParticipanteIdentifier(participante) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IParticipante>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IParticipante[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addParticipanteToCollectionIfMissing(
    participanteCollection: IParticipante[],
    ...participantesToCheck: (IParticipante | null | undefined)[]
  ): IParticipante[] {
    const participantes: IParticipante[] = participantesToCheck.filter(isPresent);
    if (participantes.length > 0) {
      const participanteCollectionIdentifiers = participanteCollection.map(
        participanteItem => getParticipanteIdentifier(participanteItem)!
      );
      const participantesToAdd = participantes.filter(participanteItem => {
        const participanteIdentifier = getParticipanteIdentifier(participanteItem);
        if (participanteIdentifier == null || participanteCollectionIdentifiers.includes(participanteIdentifier)) {
          return false;
        }
        participanteCollectionIdentifiers.push(participanteIdentifier);
        return true;
      });
      return [...participantesToAdd, ...participanteCollection];
    }
    return participanteCollection;
  }

  protected convertDateFromClient(participante: IParticipante): IParticipante {
    return Object.assign({}, participante, {
      participanteDataDeNascimento: participante.participanteDataDeNascimento?.isValid()
        ? participante.participanteDataDeNascimento.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.participanteDataDeNascimento = res.body.participanteDataDeNascimento
        ? dayjs(res.body.participanteDataDeNascimento)
        : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((participante: IParticipante) => {
        participante.participanteDataDeNascimento = participante.participanteDataDeNascimento
          ? dayjs(participante.participanteDataDeNascimento)
          : undefined;
      });
    }
    return res;
  }
}
